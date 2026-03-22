package com.twisted.game.content.raids.chamber_of_xeric.great_olm;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;

/**
 * @author Patrick van Elderen | May, 16, 2021, 13:11
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class GreatOlmCombatMethod extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return 0;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 5;
    }
}
