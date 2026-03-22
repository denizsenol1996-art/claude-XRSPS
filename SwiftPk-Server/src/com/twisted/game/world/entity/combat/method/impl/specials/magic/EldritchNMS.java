package com.twisted.game.world.entity.combat.method.impl.specials.magic;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.magic.CombatSpells;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.skills.Skills;

/**
 * The eldritch nightmare staff has a special attack, Invocate, that consumes 75% of the player's special attack energy
 * to hit the target for a high amount of damage, and restores the caster's prayer points by 50% of the damage dealt.
 * Invocate can boost the caster's prayer points above their prayer level (like the ancient mace), up to a maximum of 120.
 * The special attack does not consume runes.
 *
 * The base damage for Invocate is scaled based on the player's Magic level, ranging from 39 at level 75 to 50 at level 99,
 * which can be increased with magic damage boosting items. At level 99, the maximum possible hit is 60, or 67 while on a
 * slayer assignment.
 *
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date februari 10, 2020 13:14
 */
public class EldritchNMS extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.graphic(1762);
        mob.animate(8532);

        mob.getCombat().setCastSpell(CombatSpells.ELDRITCH_NIGHTMARE_STAFF.getSpell());
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy();
        hit.submit();
        if(hit.isAccurate()) {
            target.graphic(1761);
        }

        if(target.isPlayer()) {
            var drain = hit.getDamage() * 35 / 100; // smite 35% of the damage dealt
            target.skills().alterSkill(Skills.PRAYER, -drain);
            mob.heal(drain);
        }

        //Reset spell
        mob.getCombat().setCastSpell(null);

        //Drain spec after the attack
        CombatSpecial.drain(mob, CombatSpecial.ELDRITCH_NIGHTMARE_STAFF.getDrainAmount());
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
