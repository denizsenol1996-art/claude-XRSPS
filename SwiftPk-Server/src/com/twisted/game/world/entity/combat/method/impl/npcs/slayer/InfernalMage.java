package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;

/**
 * @author PVE
 * @Since augustus 07, 2020
 */
public class InfernalMage extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        mob.graphic(129, 92, 0);
        new Projectile(mob, target, 130, mob.projectileSpeed(target), 51, 43, 31, 0,16, 64).sendProjectile();
        int delay = mob.getProjectileHitDelay(target);
        int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC);
        target.hit(mob, hit, delay, CombatType.MAGIC).checkAccuracy().submit();

        if(hit > 0) {
            target.graphic(131, 124, delay);
        } else {
            target.graphic(85, 124, delay);
        }

        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }
}
