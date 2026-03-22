package com.twisted.game.world.entity.combat.method.impl.npcs.bosses;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;

public class ThermonuclearSmokeDevil extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        int tileDist = mob.tile().transform(1, 1).distance(target.tile());
        new Projectile(mob, target, 644, 20, 12 * tileDist, 30, 30, 0).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
        target.graphic(643);

        return true;
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
