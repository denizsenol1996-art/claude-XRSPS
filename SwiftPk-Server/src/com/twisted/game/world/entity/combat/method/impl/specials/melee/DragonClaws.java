package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.util.Utils;

import static com.twisted.util.CustomItemIdentifiers.DRAGON_CLAWS_OR;

public class DragonClaws extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(7514);
        mob.graphic(1171);

        int first = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);
        if(first > 44) {
            first = mob.getAsPlayer().getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_CLAWS_OR) ? 45 : 44;
        }
        int secondHit = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);

        /* DRAGON CLAWS SPECIAL FORMULA */
        int maxHit = mob.getCombat().maximumMeleeHit();
        int second = first <= 0 ? secondHit : (first / 2);
        int third;
        int fourth;
        if (second > 0) {
            if (second == 1 || second % 2 == 0) {
                third = second / 2;
                fourth = second / 2;
            } else {
                fourth = 1 + (second / 2);
                third = fourth - 1;
            }
        } else {
            if (first <= 0) {
                int damage = (int) (maxHit * 0.75);
                third = Utils.random(1, damage);
                fourth = Utils.random(1, damage);
            } else {
                third = 0;
                fourth = 0;
            }
        }
        // If all 3 hits fail then we empower the fourth.
        if (first <= 0 && second <= 0 && third <= 0)
            fourth = Utils.random(maxHit, (int) (maxHit * 1.5));

        Hit hit = target.hit(mob, first,1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        Hit hit2 = target.hit(mob, second,1, CombatType.MELEE).checkAccuracy();
        hit2.submit();
        Hit hit3 = target.hit(mob, third,2, CombatType.MELEE).checkAccuracy();
        hit3.submit();
        Hit hit4 = target.hit(mob, fourth,2, CombatType.MELEE).checkAccuracy();
        hit4.submit();
        CombatSpecial.drain(mob, CombatSpecial.DRAGON_CLAWS.getDrainAmount());
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
