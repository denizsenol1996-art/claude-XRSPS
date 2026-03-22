package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;

public class DarkBeasts extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
       if (mob.tile().distance(target.tile()) <= 2) {
           int tileDist = mob.tile().transform(1, 1).distance(target.tile());
           new Projectile(mob, target, 130, 50, 12 * tileDist, 40, 30, 0).sendProjectile();
           target.hit(mob, CombatFactory.calcDamageFromType(mob, target,CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().submit();
       }
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target,CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy().submit();

        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 7;
    }
}
