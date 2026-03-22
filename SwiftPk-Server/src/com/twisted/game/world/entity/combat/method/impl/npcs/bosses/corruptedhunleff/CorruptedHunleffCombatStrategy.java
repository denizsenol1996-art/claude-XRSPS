package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.corruptedhunleff;

import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Color;
import com.twisted.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.List;

public class CorruptedHunleffCombatStrategy extends CommonCombatMethod {

    private Task stompTask;

    private void checkStompTask(Mob mob, Mob target) {
        if (stompTask == null) {
            stompTask = new Task("checkHunleffStompTask", 7) {
                @Override
                protected void execute() {
                    if (mob.dead() || !mob.isRegistered() || !mob.getAsNpc().canSeeTarget(mob, target)) {
                        stop();
                        return;
                    }
                    World.getWorld().getPlayers().forEachInRegion(6810, p -> {
                        if (p.boundaryBounds().inside(mob.tile(), mob.getSize())) {
                            stompAttack(mob, target);
                        }
                    });
                }
            }.bind(mob);
            TaskManager.submit(stompTask);
        }
    }

    /**
     * If a player is underneath the Hunllef when it tries to attack the player, the Hunllef will perform a stomp attack which deals very high damage,
     * similar to the stomp attack used by the Corporeal Beast. This is the only attack that does not count towards the 4 attacks.
     */
    public void stompAttack(Mob mob, Mob target) {
        mob.animate(8420);
        target.hit(mob, World.getWorld().random(30, 51), 1, CombatType.MELEE).setAccurate(true).submit();
    }

    private int attacks = 0;
    private boolean tornadoAttack = false;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        checkStompTask(mob, target);
        attacks++;

        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            if(World.getWorld().rollDie(2,1)) {
                mob.animate(8420);
                mob.getAsNpc().combatInfo().maxhit = 50;
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).setAccurate(true).submit();
            } else {
                //Standard attacks: The Hunllef uses two main standard attacks, a ranged crystal-like attack and a magic based orb attack.
                // The Hunllef will alternate between the two attack styles every 4 attacks.
                if (attacks == 4) {
                    rangeAttack(mob, target);
                    attacks = 0;
                } else {
                    magicAttack(mob, target);
                }
            }
        } else {
            //Standard attacks: The Hunllef uses two main standard attacks, a ranged crystal-like attack and a magic based orb attack.
            // The Hunllef will alternate between the two attack styles every 4 attacks.
            if (attacks == 4) {
                rangeAttack(mob, target);
                attacks = 0;
            } else {
                magicAttack(mob, target);
            }
        }

        //25% chance to spawn tornados
        if (World.getWorld().rollDie(4, 1)) {
            tornadoAttack = true;
            tornadoAttack(mob, target);
        }
        return true;
    }

    private void rangeAttack(Mob mob, Mob target) {
        var tileDist = mob.tile().transform(1, 1, 0).distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
        Projectile projectile = new Projectile(mob, target, 1705, 35, 20 * tileDist, 45, 30, 0);
        projectile.sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
    }

    private void magicAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        //25% chance to disable prayers
        if(World.getWorld().rollDie(4,1)) {
            prayerDisableAttack(mob, target);
        } else {
            var tileDist = mob.tile().transform(1, 1, 0).distance(target.tile());
            var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
            Projectile projectile = new Projectile(mob, target, 1713, 35, 20 * tileDist, 45, 30, 0);
            projectile.sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
            target.delayedGraphics(1709, 100, delay);
        }
    }

    /**
     * When the Hunllef is attacking using magic-based attacks, it has a chance to use an attack which turns off prayers. These attacks use a different game
     * sound, hence it is recommended to use sounds during the fight.
     */
    private void prayerDisableAttack(Mob mob, Mob target) {
        var tileDist = mob.tile().transform(1, 1, 0).distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
        Projectile projectile = new Projectile(mob, target, 1708, 35, 20 * tileDist, 45, 30, 0);
        projectile.sendProjectile();
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy();
        hit.submit();
        if(hit.isAccurate()) {
            Prayers.closeAllPrayers(target);
            if(target.isPlayer()) {
                target.message(Color.RED.wrap("Your prayers have been disabled"));
            }
        }
    }

    /**
     * The Hunllef summons multiple tornados that chase the player and deal high damage if they reach the players location, these tornadoes disappear after
     * a brief period. The number of tornados summoned increases as the Hunllef takes damage.
     */
    private void tornadoAttack(Mob mob, Mob target) {
        mob.animate(8418);
        Tile base = mob.tile().copy();

        final List<Tile> crystalSpots = new ArrayList<>(List.of(new Tile(0, 6, 0)));

        if(mob.hp() < 750) {
            crystalSpots.add(new Tile(3, 6, 0));
        }

        if(mob.hp() < 500) {
            crystalSpots.add(new Tile(World.getWorld().random(1,4), World.getWorld().random(1,4), 0));
        }

        if(mob.hp() < 250) {
            crystalSpots.add(new Tile(World.getWorld().random(3,7), World.getWorld().random(2,6), 0));
        }

        Tile centralCrystalSpot = new Tile(39, 14, 0);
        Tile central = base.transform(centralCrystalSpot.x, centralCrystalSpot.y);
        ArrayList<Tile> spots = new ArrayList<>(crystalSpots);
        int[] ticker = new int[1];
        Chain.bound(null).runFn(2, () -> World.getWorld().tileGraphic(1718, central, 0, 0)).repeatingTask(1, t -> {
            if (ticker[0] == 10) {
                t.stop();
                return;
            }
            for (Tile spot : spots) {
                World.getWorld().tileGraphic(1718, base.transform(spot.x, spot.y), 0, 0);
            }
            ArrayList<Tile> newSpots = new ArrayList<>();
            for (Tile spot : new ArrayList<>(spots)) {
                final Tile curSpot = base.transform(spot.x, spot.y);
                if (curSpot.equals(target.tile())) {
                    target.hit(mob, World.getWorld().random(1, 35), SplatType.HITSPLAT);
                } else {
                    final Direction direction = Direction.getDirection(curSpot, target.tile());
                    Tile newSpot = spot.transform(direction.x, direction.y);
                    newSpots.add(newSpot);
                }
            }
            // visual debug
            /*ArrayList<GroundItem> markers = new ArrayList<>(1);
            for (Tile step : newSpots) {
                GroundItem marker = new GroundItem(new Item(ItemIdentifiers.VIAL, 1), new Tile(base.transform(step.x, step.y).x,
                    base.transform(step.x, step.y).y, mob.getZ()), null);
                GroundItemHandler.createGroundItem(marker);
                markers.add(marker);
            }
            Task.runOnceTask(1, c -> {
                markers.forEach(GroundItemHandler::sendRemoveGroundItem);
            });*/
            spots.clear();
            spots.addAll(newSpots);
            ticker[0]++;
        });
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return tornadoAttack ? 8 : mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }
}
