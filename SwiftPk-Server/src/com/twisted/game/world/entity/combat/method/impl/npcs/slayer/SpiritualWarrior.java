package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;

public class SpiritualWarrior extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);
        target.hit(mob, hit, CombatType.MELEE).checkAccuracy().submit();

        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }
}
