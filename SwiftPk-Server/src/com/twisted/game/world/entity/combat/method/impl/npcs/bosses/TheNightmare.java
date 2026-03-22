package com.twisted.game.world.entity.combat.method.impl.npcs.bosses;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Color;
import com.twisted.util.chainedwork.Chain;

public class TheNightmare extends CommonCombatMethod {

    private enum Attacks {
        MELEE, RANGE, MAGIC, SPEED_ATTACK, HIDE_ATTACK, SPECIAL_ATTACK
    }

    private Attacks attack = Attacks.MELEE;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && World.getWorld().rollDie(2, 1)) {
            if(World.getWorld().rollDie(2,1)) {
                meleeClawAttack();//Neither of these hits are protectable
            } else {
                speedAttack();//One of these hits is protectable
            }
        } else {
            var roll = World.getWorld().random(8);

            switch (roll) {
                case 0, 1 -> hideAttack((Player) target);
                case 2, 3 -> magicAttack();
                case 4, 5 -> rangeAttack();
                case 6, 7, 8 -> specialAttack();
            }
        }
        return true;
    }

    private void meleeClawAttack() {
        attack = Attacks.MELEE;
        mob.animate(8594);
        mob.face(null); // Stop facing the target
        World.getWorld().getPlayers().forEach(p -> {
            if (mob.isRegistered() && !mob.dead() && p != null && p.tile().inSqRadius(mob.tile(), 12)) {
                int first = World.getWorld().random(1, 30);
                int second = first / 2;
                int third = second / 2;
                int fourth = third / 2;
                p.hit(mob, first, 1);
                p.hit(mob, second, 1);
                p.hit(mob, third, 2);
                p.hit(mob, fourth, 2);
            }
        });
        mob.face(target.tile()); // Go back to facing the target.
    }

    private void speedAttack() {
        attack = Attacks.SPEED_ATTACK;
        mob.animate(8597);
        mob.face(null); // Stop facing the target
        World.getWorld().getPlayers().forEach(p -> {
            if (mob.isRegistered() && !mob.dead() && p != null && p.tile().inSqRadius(mob.tile(), 12)) {
                mob.forceChat("DEADLY NARUTOOOOOOOOOOO!");
                int second = World.getWorld().random(1, 25);
                p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy().submit();
                p.hit(mob, second, 2);
            }
        });
        mob.face(target.tile()); // Go back to facing the target.
    }

    private void hideAttack(Player target) {
        if (target.getY() >= 3981) {
            attack = Attacks.HIDE_ATTACK;
            Tile targetTile = target.tile().copy();

            Chain.bound(null).name("TheNightmareHideAttackTask").runFn(1, () -> {

                mob.face(target.tile()); // Face the target.
                mob.animate(8607);
                mob.lockNoDamage();
                target.message(Color.RED.wrap("The Nightmare has targeted you."));


            }).then(3, () -> {
                ((Npc) mob).hidden(true);// removes from client view
                mob.teleport(targetTile);// just sets new location, doesn't do any npc updating changes (npc doesn't support TELEPORT like players do)
            }).then(3, () -> {
                mob.animate(8609);
                ((Npc) mob).hidden(false);
                mob.face(target.tile());
                mob.unlock();
                mob.getCombat().attack(target);
                if (target.tile().inSqRadius(targetTile, 2))
                    target.hit(mob, World.getWorld().random(55), 1);
            });
            mob.face(target.tile()); // Go back to facing the target.
        }
    }
    private void magicAttack() {
        attack = Attacks.MAGIC;
        mob.animate(8598);
        mob.face(null); // Stop facing the target
        Chain.bound(null).runFn(6, () -> {
            World.getWorld().getPlayers().forEach(p -> {
                if (mob.isRegistered() && !mob.dead() && p != null && p.tile().inSqRadius(mob.tile(), 12)) {
                    projectile_bombing(p);
                }
            });
            mob.face(target.tile()); // Go back to facing the target.
        });
    }

    private void projectile_bombing(Player player) {
        int x = player.tile().x;
        int y = player.tile().y;

        Tile projectile_one = new Tile(x, y);
        int projectile_one_distance = mob.tile().distance(projectile_one);
        int projectile_one_delay = Math.max(1, (20 + projectile_one_distance * 12) / 30);

        Tile projectile_two = new Tile(x + World.getWorld().random(2), y + World.getWorld().random(2));
        int projectile_two_distance = mob.tile().distance(projectile_two);
        int projectile_two_delay = Math.max(1, (20 + projectile_two_distance * 12) / 30);

        Tile projectile_three = new Tile(x + World.getWorld().random(2), y + World.getWorld().random(2));
        int projectile_three_distance = mob.tile().distance(projectile_three);
        int projectile_three_delay = Math.max(1, (20 + projectile_three_distance * 12) / 30);

        new Projectile(mob.tile(), projectile_one, 0, 1665, 24 * projectile_one_distance, projectile_one_delay, 50, 0, 0).sendProjectile();
        new Projectile(mob.tile(), projectile_two, 0, 1665, 24 * projectile_two_distance, projectile_two_delay, 50, 0, 0).sendProjectile();
        new Projectile(mob.tile(), projectile_three, 0, 1665, 24 * projectile_three_distance, projectile_three_delay, 50, 0, 0).sendProjectile();

        World.getWorld().tileGraphic(1717, projectile_one, 1, 24 * projectile_one_distance);
        World.getWorld().tileGraphic(1717, projectile_two, 1, 24 * projectile_two_distance);
        World.getWorld().tileGraphic(1717, projectile_three, 1, 24 * projectile_three_distance);

        Chain.bound(null).name("projectile_one_task").runFn(projectile_one_distance, () -> {
            if (player.tile().inSqRadius(projectile_one, 1))
                player.hit(mob, Math.min(20, World.getWorld().random(35)));
            if (player.tile().inSqRadius(projectile_two, 1))
                player.hit(mob, Math.min(20, World.getWorld().random(35)));
            if (player.tile().inSqRadius(projectile_three, 1))
                player.hit(mob, Math.min(20, World.getWorld().random(35)));
        });

        int explosive_x = projectile_two.x;
        int explosive_z = projectile_two.y;

        Tile ricochet_projectile_one = new Tile(explosive_x + World.getWorld().random(2), explosive_z + World.getWorld().random(2));
        int ricochet_projectile_one_distance = projectile_two.distance(ricochet_projectile_one);
        int ricochet_projectile_one_delay = Math.max(1, (20 + ricochet_projectile_one_distance * 12) / 30);

        Tile ricochet_projectile_two = new Tile(explosive_x + World.getWorld().random(2), explosive_z + World.getWorld().random(3));
        int ricochet_projectile_two_distance = projectile_two.distance(ricochet_projectile_two);
        int ricochet_projectile_two_delay = Math.max(1, (20 + ricochet_projectile_two_distance * 12) / 30);

        Chain.bound(null).name("projectile_two_task").runFn(projectile_two_distance, () -> {
            new Projectile(projectile_two, ricochet_projectile_one, 0, 1378, 50 * ricochet_projectile_one_distance, ricochet_projectile_one_delay, 0, 0, 0).sendProjectile();
            new Projectile(projectile_two, ricochet_projectile_two, 0, 1378, 50 * ricochet_projectile_two_distance, ricochet_projectile_two_delay, 0, 0, 0).sendProjectile();

            World.getWorld().tileGraphic(1718, ricochet_projectile_one, 1, 50 * ricochet_projectile_one_distance);
            World.getWorld().tileGraphic(1718, ricochet_projectile_two, 1, 50 * ricochet_projectile_two_distance);

        }).then(ricochet_projectile_one_delay, () -> {
            if (player.tile().inSqRadius(ricochet_projectile_one, 1))
                player.hit(mob, Math.min(20, World.getWorld().random(35)));
            if (player.tile().inSqRadius(ricochet_projectile_two, 1))
                player.hit(mob, Math.min(20, World.getWorld().random(35)));
        });
    }

    private void rangeAttack() {
        attack = Attacks.RANGE;
        mob.animate(8606);
        mob.face(null); // Stop facing the target
        World.getWorld().getPlayers().forEach(p -> Chain.bound(null).runFn(2, () -> {
            if (mob.isRegistered() && !mob.dead() && p != null && p.tile().inSqRadius(mob.tile(), 12)) {
                int tileDist = mob.tile().transform(1, 1, 0).distance(p.tile());
                var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

                new Projectile(mob, p, 1380, 120, 12 * tileDist, 100, 43, 0, 14, 5).sendProjectile();

                p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();

                Chain.bound(null).runFn(3, () -> {
                    p.runOnceTask(5, r -> {
                        final var tile = p.tile().copy();
                        new Projectile(mob.getCentrePosition(), tile, 1, 1637, 125, 40, 100, 0, 0, 16, 96).sendProjectile();
                        World.getWorld().tileGraphic(1638, tile, 0, 0);
                        if (p.tile().equals(tile)) {
                            for (int hits = 0; hits < 4; hits++) {
                                Chain.bound(null).name("the_nightmare_special_ranged_attack_task").runFn(hits * 3, () -> p.hit(mob, World.getWorld().random(6, 8)));
                            }
                        }
                    });
                });
            }
        }));
        mob.face(target.tile()); // Go back to facing the target.
    }

    private void specialAttack() {
        attack = Attacks.SPECIAL_ATTACK;
        // Go back to facing the target.
        mob.animate(8601);
        mob.face(null); // Stop facing the target
        World.getWorld().getPlayers().forEach(p -> {
            if (mob.isRegistered() && !mob.dead() && p != null && p.tile().inSqRadius(mob.tile(), 12)) {
                Tile tile_one = p.tile();
                Tile tile_two = tile_one.transform(World.getWorld().random(1, 2), World.getWorld().random(1, 2));
                Tile tile_three = tile_one.transform(World.getWorld().random(1, 2), World.getWorld().random(1, 2));
                Tile tile_four = tile_one.transform(World.getWorld().random(1, 2), World.getWorld().random(1, 2));
                Tile tile_five = tile_one.transform(World.getWorld().random(1, 2), World.getWorld().random(1, 2));

                World.getWorld().tileGraphic(1727, tile_one, 100, 0);
                World.getWorld().tileGraphic(1727, tile_two, 100, 0);
                World.getWorld().tileGraphic(1727, tile_three, 100, 0);
                World.getWorld().tileGraphic(1727, tile_four, 100, 0);
                World.getWorld().tileGraphic(1727, tile_five, 100, 0);

                Chain.bound(null).runFn(10, () -> {
                    if (p.tile().inSqRadius(tile_one, 2) || p.tile().inSqRadius(tile_two, 2) || p.tile().inSqRadius(tile_three, 2) || p.tile().inSqRadius(tile_four, 2) || p.tile().inSqRadius(tile_five, 2)) {
                        p.hit(mob, World.getWorld().random(1, 30), 1);
                    }
                });
            }
        });
        mob.face(target.tile()); // Go back to facing the target.
    }
    @Override
    public int getAttackSpeed(Mob mob) {
        return attack.equals(Attacks.MAGIC) ? 12 : mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 12;
    }
}
