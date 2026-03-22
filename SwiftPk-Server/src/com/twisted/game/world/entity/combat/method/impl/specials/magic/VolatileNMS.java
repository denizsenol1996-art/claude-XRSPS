package com.twisted.game.world.entity.combat.method.impl.specials.magic;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.magic.CombatSpells;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;

/**
 * The volatile nightmare staff has a special attack, Immolate, that consumes 55% of the player's special attack energy
 * to hit the target with 50% increased accuracy and dealing a high amount of damage. The special attack does not consume runes.
 *
 * The base damage for Immolate is scaled based on the player's Magic level, ranging from 50 at level 75 to 66 at level 99,
 * which can be increased with magic damage boosting items. At level 99, the maximum possible hit is 80, or 89 while on a
 * slayer assignment.
 *
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date februari 10, 2020 13:14
 */
public class VolatileNMS extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.graphic(1760);
        mob.animate(8532);

        mob.getCombat().setCastSpell(CombatSpells.VOLATILE_NIGHTMARE_STAFF.getSpell());
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 3, CombatType.MAGIC).checkAccuracy();
        hit.submit();
        if(hit.isAccurate()) {
            target.graphic(1759);
        }
        //Reset spell
        mob.getCombat().setCastSpell(null);

        //Drain spec after the attack
        CombatSpecial.drain(mob, CombatSpecial.VOLATILE_NIGHTMARE_STAFF.getDrainAmount());
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
