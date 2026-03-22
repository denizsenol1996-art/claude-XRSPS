package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.seren;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.NpcCombatInfo;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Color;
import com.twisted.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.List;

public class FragmentOfSeren extends CommonCombatMethod {
    //updateseren
    private int attacks = 0;
    private boolean tornadoAttack = false;
    private NpcDefinition def;
    private int hp;
    private NpcCombatInfo combatInfo;

    public int hp() {
        return hp;
    }

    public void hp(int hp, int exceed) {
        this.hp = Math.min(maxHp() + exceed, hp);
    }

    public int maxHp() {
        return combatInfo == null ? 50 : combatInfo.stats.hitpoints;
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && World.getWorld().rollDie(2, 1)) {
            if (World.getWorld().rollDie(2, 1)) {
                meleeClawAttack(mob, target);

            } else {
                rangeAttack();
            }
        } else {
            var roll = World.getWorld().random(8);

            switch (roll) {
                case 0, 1 -> hideAttack((Player) target);
                case 2, 3 -> magicAttack(mob, target);
                case 4, 5 -> rangeAttack();
                case 6, 7, 8 -> tornadoAttack(mob, target);
            }
        }
        return true;
    } //8380


    private void meleeClawAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.forceChat("GET BACK!");
        mob.animate(8380);
        mob.face(null); // Stop facing the target
        Chain.bound(null).runFn(8, () -> {
            if (mob.isRegistered() && !mob.dead() && target != null && target.tile().inSqRadius(mob.tile(), 3)) {
                int first = World.getWorld().random(1, 30);
                int second = first / 2;
                target.hit(mob, first, 1);
                target.hit(mob, second, 1);
            }
        });
        mob.face(target.tile()); // Go back to facing the target.
    }

    private void magicAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.forceChat("Your life is mine mortal!");
        mob.animate(8379);
        var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
        World.getWorld().getPlayers().forEach(p -> Chain.bound(null).runFn(3, () -> {
            if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                new Projectile(mob, p, 1702, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
                p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                target.delayedGraphics(1704, 60, delay + 1);
            }
        }));
    }

    private void rangeAttack() {
        if (mob.dead()) {
            return;
        }
        mob.animate(8376);
        mob.forceChat("You'll never defeat me..");
        mob.face(null); // Stop facing the target
        World.getWorld().getPlayers().forEach(p -> Chain.bound(null).runFn(2, () -> {
            if (mob.isRegistered() && !mob.dead() && p != null && p.tile().inSqRadius(mob.tile(), 12)) {
                int tileDist = mob.tile().transform(1, 1, 0).getChevDistance(p.tile());
                var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

                new Projectile(mob, p, 1712, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 0).sendProjectile();

                p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
            }
        }));
        World.getWorld().getPlayers().forEach(p -> Chain.bound(null).runFn(3, () -> {
            if (mob.isRegistered() && !mob.dead() && p != null && p.tile().inSqRadius(mob.tile(), 12)) {
                int tileDist = mob.tile().transform(1, 1, 0).getChevDistance(p.tile());
                var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

                new Projectile(mob, p, 1712, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 0).sendProjectile();

                p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
            }
        }));
        mob.face(target.tile()); // Go back to facing the target.
    }

    private void hideAttack(Player target) {
        if (mob.dead()) {
            return;
        }
        Tile targetTile = target.tile().copy();
        if (target.tile().inArea(3280, 3988, 3306, 4013)) {
            Chain.bound(null).name("SerenHideTask").runFn(1, () -> {
                mob.face(target.tile()); // Face the target.
                mob.animate(8373);
                mob.lockNoDamage();
                target.message(Color.RED.wrap("The Seren Has Targeted You."));
            }).then(1, () -> {
                ((Npc) mob).hidden(true);// removes from client view
                mob.teleport(targetTile);// just sets new location, doesn't do any npc updating changes (npc doesn't support TELEPORT like players do)
            }).then(3, () -> {
                mob.forceChat("Taste my wrath!");
                mob.animate(8374);
                ((Npc) mob).hidden(false);
                mob.face(target.tile());
                mob.unlock();
                mob.getCombat().attack(target);
                if (target.tile().inSqRadius(targetTile, 2))
                    target.hit(mob, World.getWorld().random(55), 1);
            });
            mob.face(target.tile()); // Go back to facing the target.
        } else {
            //System.out.println("can't be teleport");
        }
    }
    private void tornadoSingleAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        if (target != null && target.tile().inSqRadius(target.tile(), 12)) {
            mob.animate(8378);
            Tile base = mob.tile().copy();
            var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
            var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

            mob.forceChat("Run my child...");

            final List<Tile> crystalSpots = new ArrayList<>(List.of(new Tile(0, 6, 0)));

            if (mob.hp() < 750) {
                crystalSpots.add(new Tile(3, 6, 0));
            }

            if (mob.hp() < 500) {
                crystalSpots.add(new Tile(World.getWorld().random(1, 4), World.getWorld().random(1, 4), 0));
            }

            if (mob.hp() < 250) {
                crystalSpots.add(new Tile(World.getWorld().random(3, 7), World.getWorld().random(2, 6), 0));
            }

            Tile centralCrystalSpot = new Tile(mob.getX(), mob.getY(), 0);
            Tile central = base.transform(centralCrystalSpot.x, centralCrystalSpot.y);
            ArrayList<Tile> spots = new ArrayList<>(crystalSpots);
            int[] ticker = new int[1];
            Chain.bound(null).runFn(2, () -> World.getWorld().tileGraphic(1718, central, 0, delay)).repeatingTask(1, t -> {
                if (ticker[0] == 10) {
                    t.stop();
                    return;
                }
                for (Tile spot : spots) {
                    World.getWorld().tileGraphic(1718, base.transform(spot.x, spot.y), 0, delay);
                }
                ArrayList<Tile> newSpots = new ArrayList<>();
                for (Tile spot : new ArrayList<>(spots)) {
                    final Tile curSpot = base.transform(spot.x, spot.y);
                    if (curSpot.equals(target.tile())) {
                        target.hit(mob, World.getWorld().random(1, 5), SplatType.HITSPLAT);
                    } else {
                        final Direction direction = Direction.getDirection(curSpot, target.tile());
                        Tile newSpot = spot.transform(direction.x, direction.y);
                        newSpots.add(newSpot);
                    }
                }
                spots.clear();
                spots.addAll(newSpots);
                ticker[0]++;
            });
        }
    }
    private void tornadoAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        World.getWorld().getPlayers().forEach(p -> {
            if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                mob.animate(8378);
                Tile base = mob.tile().copy();
                var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
                var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

                mob.forceChat("Run my child...");

                final List<Tile> crystalSpots = new ArrayList<>(List.of(new Tile(0, 6, 0)));

                if (mob.hp() < 750) {
                    crystalSpots.add(new Tile(3, 6, 0));
                }

                if (mob.hp() < 500) {
                    crystalSpots.add(new Tile(World.getWorld().random(1, 4), World.getWorld().random(1, 4), 0));
                }

                if (mob.hp() < 250) {
                    crystalSpots.add(new Tile(World.getWorld().random(3, 7), World.getWorld().random(2, 6), 0));
                }

                Tile centralCrystalSpot = new Tile(mob.getX(), mob.getY(), 0);
                Tile central = base.transform(centralCrystalSpot.x, centralCrystalSpot.y);
                ArrayList<Tile> spots = new ArrayList<>(crystalSpots);
                int[] ticker = new int[1];
                Chain.bound(null).runFn(2, () -> World.getWorld().tileGraphic(1718, central, 0, delay)).repeatingTask(1, t -> {
                    if (ticker[0] == 10) {
                        t.stop();
                        return;
                    }
                    for (Tile spot : spots) {
                        World.getWorld().tileGraphic(1718, base.transform(spot.x, spot.y), 0, delay);
                    }
                    ArrayList<Tile> newSpots = new ArrayList<>();
                    for (Tile spot : new ArrayList<>(spots)) {
                        final Tile curSpot = base.transform(spot.x, spot.y);
                        if (curSpot.equals(target.tile())) {
                            target.hit(mob, World.getWorld().random(1, 5), SplatType.HITSPLAT);
                        } else {
                            final Direction direction = Direction.getDirection(curSpot, target.tile());
                            Tile newSpot = spot.transform(direction.x, direction.y);
                            newSpots.add(newSpot);
                        }
                    }
                    spots.clear();
                    spots.addAll(newSpots);
                    ticker[0]++;
                });
            }
        });
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return tornadoAttack ? 8 : mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 5;
    }
}
