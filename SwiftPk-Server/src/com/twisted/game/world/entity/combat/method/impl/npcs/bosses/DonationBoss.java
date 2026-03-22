package com.twisted.game.world.entity.combat.method.impl.npcs.bosses;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.position.Tile;
import com.twisted.util.chainedwork.Chain;

public class DonationBoss extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        var roll = World.getWorld().random(8);

        switch (roll) {
            case 0, 1 -> frozen(mob, target);
            case 2 -> specialAttack();
            case 6, 7, 8 -> rangeAttack();
            default -> magicAttack(mob, target);

            //
        }
        return true;
    }

    private void specialAttack() {
        // Go back to facing the target.
        mob.animate(-1);
        mob.face(null); // Stop facing the target
        World.getWorld().getPlayers().forEach(p -> {
            if (p.tile().region() == 11571) {
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
            }
        });
        mob.face(target.tile()); // Go back to facing the target.
    }

    private void frozen(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.animate(711);
        mob.face(null);
        var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
        World.getWorld().getPlayers().forEach(p -> Chain.bound(null).runFn(3, () -> {
            if (p.tile().region() == 11571) {
                if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                    new Projectile(mob, p, 1404, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
                    p.freeze(5, target);
                    p.graphic(369);
                    p.hit(target, World.getWorld().random(15));
                }
            }
        }));

    }

    private void magicAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.animate(711);
        mob.face(null);
        var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
        World.getWorld().getPlayers().forEach(p -> Chain.bound(null).runFn(3, () -> {
            if (p.tile().region() == 11571) {
                if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                    new Projectile(mob, p, 1404, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
                    p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                    target.delayedGraphics(1404, 60, delay + 1);
                }
            }
        }));
    }

    private void rangeAttack() {
        if (mob.dead()) {
            return;
        }
        mob.animate(712);
        mob.face(null);
        World.getWorld().getPlayers().forEach(p -> Chain.bound(null).runFn(2, () -> {
            if (p.tile().region() == 11571) {
                if (mob.isRegistered() && !mob.dead() && p != null && p.tile().inSqRadius(mob.tile(), 12)) {
                    int tileDist = mob.tile().transform(1, 1, 0).getChevDistance(p.tile());
                    var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

                    new Projectile(mob, p, 1405, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 0).sendProjectile();

                    p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
                }
            }
        }));
        mob.face(target.tile()); // Go back to facing the target.

    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }
}
