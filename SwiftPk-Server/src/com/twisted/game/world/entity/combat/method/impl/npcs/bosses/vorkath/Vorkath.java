package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.vorkath;

import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatConstants;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.animations.Animation;
import com.twisted.game.world.entity.masks.animations.Priority;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Boundary;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Tuple;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

import java.security.SecureRandom;
import java.util.*;

import static com.twisted.game.world.entity.AttributeKey.*;
import static com.twisted.util.NpcIdentifiers.ZOMBIFIED_SPAWN_8063;

public class Vorkath extends CommonCombatMethod {

    public static final SecureRandom RANDOM = new SecureRandom();

    private static final Animation ATTACK_ANIMATION = new Animation(7952, Priority.HIGH);
    private static final Animation MELEE_ATTACK_ANIMATION = new Animation(7953, Priority.HIGH);
    private static final Animation MELEE_ATTACK_ANIMATION_2 = new Animation(7951, Priority.HIGH);
    public static final Animation FIREBALL_ATTACK_ANIMATION = new Animation(7960, Priority.HIGH);
    private static final Animation FIREBALL_SPIT_ATTACK_ANIMATION = new Animation(7957, Priority.HIGH);
    private static final Graphic RANGED_END_GRAPHIC = new Graphic(1478, 50);
    private static final Graphic MAGIC_END_GRAPHIC = new Graphic(1480, 50);
    private static final Graphic REGULAR_DRAGONFIRE_END_GRAPHIC = new Graphic(157, 50);
    private static final Graphic VENOMOUS_DRAGONFIRE_END_GRAPHIC = new Graphic(1472, 50);
    private static final Graphic PRAYER_DRAGONFIRE_END_GRAPHIC = new Graphic(1473, 50);

    private static final int BREATH_START_HEIGHT = 25;
    private static final int BREATH_END_HEIGHT = 31;
    private static final int BREATH_DELAY = 30;
    private static final int BREATH_DURATION_START = 46;
    private static final int BREATH_DURATION_INCREMENT = 8;
    private static final int BREATH_CURVE = 15;
    private static final int BREATH_OFFSET = 255;
    private static final int TILE_OFFSET = 1;

    boolean poison;

    public enum Resistance {
        PARTIAL, FULL
    }

    public Resistance resistance = null;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
       if (mob.<Integer>getAttribOr(AttributeKey.VORKATH_CB_COOLDOWN, 0) > 0)
            return false;

        int count = mob.getAttribOr(VORKATH_NORMAL_ATTACK_COUNT, 6);
        int attackType = 0;

        if (count-- < 1) {
            count = 6; // reset back
            int major = mob.<Integer>getAttribOr(VORKATH_LAST_MAJOR_ATTACK, 0) == 0 ? 1 : 0; // switch last attack
            mob.putAttrib(VORKATH_LAST_MAJOR_ATTACK, major);
            attackType = major == 0 ? 6 : 7; // decide the major attack
        } else {
            if (mob.hasAttrib(VORKATH_LINEAR_ATTACKS)) // finish the remaining grouped triple attacks
                attackType = 4;
            else {
                // choose a random attack, only melee if close
                attackType = !CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) ? 2 + RANDOM.nextInt(4) : 1 + RANDOM.nextInt(5);
            }
        }
        mob.putAttrib(VORKATH_NORMAL_ATTACK_COUNT, count);

        switch (attackType) {
            case 1 -> melee();
            case 2 -> mage();
            case 3 -> range();
            case 4 -> tripleOrdered();
            case 5 -> bomb();
            case 6 -> acidSpitball();
            case 7 -> zombified((Player) target);
        }
        return true;
    }

    private void bomb() {
        //mob.forceChat("BOMB");
        mob.animate(FIREBALL_ATTACK_ANIMATION);
        Tile targPos = target.tile().copy();
        int dist = mob.tile().distance(targPos);
        new Projectile(mob.getCentrePosition(), targPos, 1491, 1, 40 + (20 * dist), 20, 30, 30, 0, 50, 25).sendProjectile();
        mob.runUninterruptable(7, () -> World.getWorld().getPlayers().forEachInArea(targPos.area(1), p -> {
            // up to 121 if on the exact bomb tile, and up to halfed damage when next to the bomb tile.
            p.hit(mob, p.tile().equals(targPos) ? Utils.random(121) : Utils.random(60));
            p.graphic(1466, 20, 0);
        }));
        Chain.bound(null).runFn(dist, () -> mob.setEntityInteraction(target));
    }

    private void range() {
        //mob.forceChat("range");
        mob.animate(ATTACK_ANIMATION);
        new Projectile(mob, target, 1477, BREATH_DELAY, mob.projectileSpeed(target), BREATH_START_HEIGHT, BREATH_END_HEIGHT, 1).sendProjectile();
        var delay = mob.getProjectileHitDelay(target);
        target.hit(mob, Utils.random(32), delay, CombatType.RANGED).checkAccuracy().submit();
        target.delayedGraphics(RANGED_END_GRAPHIC, delay);
        Chain.bound(null).runFn(1, () -> mob.setEntityInteraction(target));
    }

    private void mage() {
        //mob.forceChat("mage");
        mob.animate(ATTACK_ANIMATION);
        new Projectile(mob, target, 1479, BREATH_DELAY, mob.projectileSpeed(target), BREATH_START_HEIGHT, BREATH_END_HEIGHT, 1).sendProjectile();
        var delay = mob.getProjectileHitDelay(target);
        target.hit(mob, Utils.random(30), delay, CombatType.MAGIC).checkAccuracy().submit();
        target.delayedGraphics(MAGIC_END_GRAPHIC, delay);
        Chain.bound(null).runFn(1, () -> mob.setEntityInteraction(target));
    }

    private void melee() {
        //mob.forceChat("melee");
        mob.animate(MELEE_ATTACK_ANIMATION_2);
        target.hit(mob, Utils.random(32), CombatType.MELEE).checkAccuracy().submit();
        Chain.bound(null).runFn(1, () -> mob.setEntityInteraction(target));
    }

    private void tripleOrdered() {
        if (!mob.hasAttrib(VORKATH_LINEAR_ATTACKS) || mob.<List<Integer>>getAttrib(VORKATH_LINEAR_ATTACKS).isEmpty()) {
            List<Integer> attackIds = new LinkedList<>(Arrays.asList(0, 1, 2));
            Collections.shuffle(attackIds);
            mob.putAttrib(VORKATH_LINEAR_ATTACKS, attackIds);
        }
        LinkedList<Integer> attackIds = mob.getAttrib(VORKATH_LINEAR_ATTACKS);
        switch (attackIds.pop()) {
            case 0 -> {
                //mob.forceChat("venom");
                // venom
                mob.animate(ATTACK_ANIMATION);
                new Projectile(mob, target, 1470, BREATH_DELAY, mob.projectileSpeed(target), BREATH_START_HEIGHT, BREATH_END_HEIGHT, 1).sendProjectile();
                target.runOnceTask(3, r -> target.performGraphic(VENOMOUS_DRAGONFIRE_END_GRAPHIC));
                if (Utils.random(4) <= 3)
                    target.venom(mob);
                fireDamage();
            }
            case 1 -> {
                //mob.forceChat("purple");
                // purple prayer
                mob.animate(ATTACK_ANIMATION);
                new Projectile(mob, target, 1471, BREATH_DELAY, mob.projectileSpeed(target), BREATH_START_HEIGHT, BREATH_END_HEIGHT, 1).sendProjectile();
                target.runOnceTask(3, r -> target.performGraphic(PRAYER_DRAGONFIRE_END_GRAPHIC));
                fireDamage();
                if (target.isPlayer()) {
                    for (int i = 0; i < target.getAsPlayer().getPrayerActive().length; i++) {
                        Prayers.deactivatePrayer(target, i);
                    }
                    target.getAsPlayer().message("Your prayers have been disabled!");
                }
            }
            case 2 -> {
                //mob.forceChat("normal");
                // normal orange dragonfire from kbd
                target.runOnceTask(3, r -> target.performGraphic(REGULAR_DRAGONFIRE_END_GRAPHIC));
                new Projectile(mob, target, 393, BREATH_DELAY, mob.projectileSpeed(target), BREATH_START_HEIGHT, BREATH_END_HEIGHT, 1).sendProjectile();
                mob.animate(ATTACK_ANIMATION);
                fireDamage();
            }
        }
        if (attackIds.isEmpty()) // attacks done
            mob.clearAttrib(VORKATH_LINEAR_ATTACKS);
    }

    private void fireDamage() {
        if (target instanceof Player) {
            Player player = (Player) target;
            int max = 73;
            var antifire_charges = player.<Integer>getAttribOr(AttributeKey.ANTIFIRE_POTION, 0);
            var hasShield = CombatConstants.hasAntiFireShield(player);
            var superAntifire = player.<Boolean>getAttribOr(AttributeKey.SUPER_ANTIFIRE_POTION, false);
            var prayerProtection = Prayers.usingPrayer(player, Prayers.PROTECT_FROM_MAGIC);

            var vorkiPetout = player.hasPetOut("Vorki");
            var petTamerI = player.<Boolean>getAttribOr(AttributeKey.ANTI_FIRE_RESISTANT,false);

            //System.out.println(vorkiPetout);
            //System.out.println(petTamerI);
            if(vorkiPetout && petTamerI) {
                player.message("Your Vorki pet protects you completely from the heat of the dragon's breath!");
                max = 0;
            }

            //If player is wearing a anti-dragon shield max hit is 20
            if(hasShield) {
                max = 20;
            }

            //If player is using protect from magic max hit is 30
            if(prayerProtection) {
                max = 30;
            }

            //If player is using protect from magic and anti-fire shield max hit 20
            if(hasShield && prayerProtection) {
                max = 20;
            }

            //If player is using anti-fire shield and antifire potion max hit 10
            if(hasShield && antifire_charges > 0) {
                max = 10;
            }

            //If player is using anti-fire shield and super antifire potion max hit 0
            if(hasShield && superAntifire) {
                max = 0;
            }

            //If player is using protect from magic and anti-fire potion max hit remains 20
            if(prayerProtection && antifire_charges > 0) {
                max = 20;
            }

            //If player is using protect from magic and super anti-fire potion max hit is 10
            if(prayerProtection && superAntifire) {
                max = 10;
            }

            //If player is using anti-fire shield, protect from magic and anti-fire potion max hit is 10
            if(hasShield && prayerProtection && antifire_charges > 0) {
                max = 10;
            }

            //If player is using anti-fire shield, protect from magic and super anti-fire potion max hit is 0
            if(hasShield && prayerProtection && superAntifire) {
                max = 0;
            }

            var hit = World.getWorld().random(max);
            var delay = mob.getProjectileHitDelay(target);
            player.hit(mob, hit, delay, CombatType.MAGIC).submit();
            if (hit > 30) {
                // maxhit wasnt reduced by any factors
                player.message("You are badly burned by the dragon fire!");
            }
        }
    }

    private void poisonPools() {
        //mob.forceChat("Poison pools");
        if (!poison) {
            resistance = Resistance.PARTIAL;
            poison = true;
            mob.lockNoDamage();
            Boundary npcBounds = mob.boundaryBounds();
            Boundary poisonBounds = mob.boundaryBounds(6);
            List<GameObject> poisons = new ArrayList<>();
            List<Tile> poisonTiles = new ArrayList<>();
            for (int x = poisonBounds.getMinimumX(); x < poisonBounds.getMaximumX(); x++) {
                for (int y = poisonBounds.getMinimumY(); y < poisonBounds.getMaximumY(); y++) {
                    if (Utils.random(3) == 2) {
                        Tile tile = new Tile(x, y, mob.tile().getLevel());
                        if (!npcBounds.inside(tile)) {
                            GameObject obj = new GameObject(32000, tile, 10, Utils.random(3)).setSpawnedfor(Optional.of(target.getAsPlayer()));
                            poisons.add(obj);
                            poisonTiles.add(tile);
                        }
                    }
                }
            }
            Player[] yo = mob.closePlayers(64);
            for (GameObject object : poisons) {
                /*World.getWorld().getObjects().removeIf(o -> o.tile().equals(object.tile())
                    && o.getType() == object.getType());
                World.getWorld().getObjects().add(object);*/
                for (Player player : yo) {
                    player.getPacketSender().sendObject(object);
                }
            }

            for (Tile poisonTile : poisonTiles) {
                for (Player player : yo) {
                    new Projectile(mob.getCentrePosition(), poisonTile, 1, 1483, 50, 20, 30, 0, 25).sendFor(player);
                }
            }

            Task.repeatingTask(t -> {
                if (finished(mob) || t.tick >= 23) {
                    t.stop();
                } else {
                    for (Player p : yo) {
                        if (poisonTiles.contains(p.tile())) {
                            int hit = Utils.random(8);
                            p.hit(mob, hit, SplatType.POISON_HITSPLAT);
                            mob.heal(hit);
                        }
                    }
                }
            });

            Chain.bound(null).runFn(1 + 23 + 1, () -> {
                resistance = null;
                poison = false;
               // World.getWorld().getObjects().removeAll(poisons);
                poisons.forEach(object -> {
                  //  MapObjects.remove(object);
                    for (Player player : yo) {
                        player.getPacketSender().sendObjectRemoval(object);
                    }
                });
                poisons.clear();
                poisonTiles.clear();
            });
            mob.unlock(); // was locked so no damage can by taken
            Chain.bound(null).runFn(23, () -> mob.setEntityInteraction(target));
        }
    }

    private void acidSpitball() {
        //mob.forceChat("acidSpitball");
        mob.animate(FIREBALL_SPIT_ATTACK_ANIMATION); // this anim lasts like 15+ seconds
        poisonPools(); // this is a 2 in 1, does pools first then spitballs
        mob.putAttrib(AttributeKey.VORKATH_CB_COOLDOWN, 27);
        mob.runUninterruptable(2, this::startSpitball);
    }

    private void startSpitball() {
        //mob.forceChat("speed spitball");
        final Area area = new Area(2257, 4053, 2286, 4077).transform(0, 0, 0, 0, target.tile().level);
        Optional<Player> first = World.getWorld().getPlayers().search(p -> p.tile().inAreaZ(area));
        first.ifPresent(player -> TaskManager.submit(new Task() {
            int loops = 25;

            @Override
            protected void execute() {
                if (Vorkath.finished(mob)) {
                    stop();
                    return;
                }
                if (loops-- > 0) {
                    //mob.forceChat("pew " + loops);
                    Tile landed = player.tile();
                    new Projectile(mob.getCentrePosition(), landed, 0, 1482, 75, 20, 20, 20, 1).sendProjectile();

                    Chain.bound(null).runFn(1, () -> World.getWorld().getPlayers().forEachFiltered(p -> p.tile().equals(landed), p -> p.hit(mob, World.getWorld().random(1, 15), 1)));
                    //System.out.println("pew");
                    return;
                }
                mob.animate(-1);
                stop();
                Chain.bound(null).runFn(1, () -> {
                    mob.putAttrib(AttributeKey.VORKATH_CB_COOLDOWN, 0);
                    mob.setEntityInteraction(target);
                });
            }
        }));
    }

    public Npc spawn = null;

    private void zombified(Player target) {
        resistance = Resistance.FULL;
        mob.lockNoDamage();
        mob.animate(7960);
        new Projectile(mob, target, 395, BREATH_DELAY, 60, BREATH_START_HEIGHT, BREATH_END_HEIGHT, 1).sendProjectile();
        mob.freeze(30, target);
        target.graphic(369, 0, 30);
        Tile pos = Utils.randomElement(mob.getCentrePosition().area(7, t ->
            World.getWorld().meleeClip(t.x, t.y, t.level) == 0
                && t.isWithinDistance(target.getCentrePosition(), 10)
                && !t.isWithinDistance(target.getCentrePosition(), 3)
                && mob.getAsNpc().spawnTile().y - t.y <= 8));
        new Projectile(mob.getCentrePosition(), pos, 0, 1484, 65, 20, 20, 20, 1).sendProjectile();
        mob.repeatingTask(t -> { // ok to run forever, shouldnt get interrupted
            mob.putAttrib(VORKATH_CB_COOLDOWN, 5);
            if (t.tick == 3) {
                spawn = new Npc(ZOMBIFIED_SPAWN_8063, pos).respawns(false);
                spawn.faceEntity(target);
                spawn.getCombat().setTarget(target);
                World.getWorld().registerNpc(spawn);

                //Add the spawn to the instance list
                if (target != null && target instanceof Player) {
                    Player player = (Player) target;
                    player.getVorkathInstance().npcList.add(spawn);
                }
            } else if (t.tick > 3) {
                if (t.tick > 20) {
                    if (spawn != null && !spawn.dead() && !spawn.finished()) {
                        spawn.hit(spawn, spawn.hp());

                        //Remove the spawn from the instance list
                        if (target != null && target instanceof Player) {
                            Player player = (Player) target;
                            player.getVorkathInstance().npcList.remove(spawn);
                        }
                    }
                }
                if (spawn != null && (spawn.dead() || spawn.finished())) {
                    t.stop();
                    spawn = null;

                    //Remove the spawn from the instance list
                    if (target != null && target instanceof Player) {
                        Player player = (Player) target;
                        player.getVorkathInstance().npcList.remove(spawn);
                    }
                } else {
                    if (spawn != null && spawn.tile().distance(target.tile()) <= 1) {
                        spawn.hit(spawn, spawn.hp());
                        target.hit(mob,60, 1);
                        spawn.graphic(157);

                        //Remove the spawn from the instance list
                        if (target != null && target instanceof Player) {
                            Player player = (Player) target;
                            player.getVorkathInstance().npcList.remove(spawn);
                        }
                    }
                }
            }
        }).onStop(() -> {
            resistance = null;
            target.getTimerRepository().cancel(TimerKey.FROZEN);
            target.getTimerRepository().cancel(TimerKey.REFREEZE);
            mob.putAttrib(AttributeKey.VORKATH_CB_COOLDOWN, 0);
            mob.unlock();
            mob.getCombat().attack(target);
        });
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 15;
    }

    public static boolean finished(Mob mob) {
        if (mob != null) {
            Tuple<Integer, Player> player = mob.getAsNpc().getAttribOr(AttributeKey.OWNING_PLAYER, new Tuple<>(-1, null));
            return mob.isNpc() && (mob.dead() || !mob.isRegistered()) || (player.second() != null && (player.second().dead()
                || !player.second().isRegistered() || player.second().tile().distance(mob.tile()) > 30));
        }
        return false;
    }
}
