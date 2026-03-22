package com.twisted.game.world.entity.combat.method.impl.npcs.barrows;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;

public class Ahrims extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        new Projectile(mob, target, 156, 40, 12 * tileDist, 30, 30, 0).sendProjectile();
        var delay = Math.max(Math.max(1, 20 + tileDist * 12 / 30), 4);
        if (delay > 2)
            delay = 2;
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();

        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getAsNpc().getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
