package com.twisted.game.world.entity.combat.method.impl.npcs.raids.cos;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;

/**
 * @author Patrick van Elderen | May, 11, 2021, 12:34
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Centaur extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
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
