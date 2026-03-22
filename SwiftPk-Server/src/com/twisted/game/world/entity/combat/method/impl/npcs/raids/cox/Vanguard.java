package com.twisted.game.world.entity.combat.method.impl.npcs.raids.cox;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Tile;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.NpcIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 29, 2021
 */
public class Vanguard extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (mob.isNpc()) {
            Npc npc = mob.getAsNpc();

            if (npc.id() == VANGUARD_7527) {
                meleeAttack(npc, target);
            } else if (npc.id() == VANGUARD_7528) {
                rangeAttack(npc, target);
            } else if (npc.id() == VANGUARD_7529) {
                magicAttack(npc, target);
            }
        }
        return true;
    }

    private void meleeAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
    }

    private void rangeAttack(Mob mob, Mob target) {
        mob.animate(7446);
        var tileDist = mob.tile().transform(1, 1, 0).distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
        //First attack always targets the player
        new Projectile(mob, target, 1332, 20, 12 * tileDist, 35, 30, 0).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
        target.delayedGraphics(305, 100, delay);

        //Handle the second projectile
        var proj_two = new Tile(target.tile().x, target.tile().y + World.getWorld().random(2));
        new Projectile(mob.tile().transform(1, 1, 0), proj_two, 1, 1332, 100, 30, 50, 6, 0).sendProjectile();

        Chain.bound(null).runFn(7, () -> {
            World.getWorld().tileGraphic(305, proj_two, 5, 0);
            if (target.tile().equals(proj_two)) {
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 0, CombatType.RANGED).checkAccuracy().submit();
            }
        });
    }

    private void magicAttack(Mob mob, Mob target) {
        mob.animate(7436);
        var tileDist = mob.tile().transform(1, 1, 0).distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
        //First attack always targets the player
        new Projectile(mob, target, 1331, 20, 12 * tileDist, 35, 30, 0).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
        target.delayedGraphics(659, 100, delay);

        //Handle the second projectile
        var proj_two = new Tile(target.tile().x, target.tile().y + World.getWorld().random(2));
        new Projectile(mob.tile().transform(1, 1, 0), proj_two, 1, 1331, 100, 30, 50, 6, 0).sendProjectile();

        Chain.bound(null).runFn(7, () -> {
            World.getWorld().tileGraphic(659, proj_two, 5, 0);
            if (target.tile().equals(proj_two)) {
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 0, CombatType.MAGIC).checkAccuracy().submit();
            }
        });
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return mob.getAsNpc().id() == VANGUARD_7527 ? 1 : 8;
    }
}
