package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;

/**
 * @author Patrick van Elderen | November, 28, 2020, 19:13
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class Dragon2HSword extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(3157);

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        CombatSpecial.drain(mob, CombatSpecial.DRAGON_2H_SWORD.getDrainAmount());
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
