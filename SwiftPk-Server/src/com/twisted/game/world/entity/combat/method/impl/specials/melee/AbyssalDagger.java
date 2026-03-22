package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;

public class AbyssalDagger extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(3300);
        mob.graphic(1283, 0, 0);
        //TODO mob.sound(2537);
        //TODO mob.sound(2537); // yes same sound twice on 07

        int h1 = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);
        int h2 = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);

        if(h1 > 0) {
            Hit hit = target.hit(mob, h1,1, CombatType.MELEE).checkAccuracy();
            hit.submit();
            Hit hit2 = target.hit(mob, h2,target.isNpc() ? 1 : 1, CombatType.MELEE).checkAccuracy();
            hit2.submit();
        } else {
            //Blocked
            Hit hit = target.hit(mob, 0,1, CombatType.MELEE).setAccurate(false);
            hit.submit();
            Hit hit2 = target.hit(mob, 0,target.isNpc() ? 1 : 1, CombatType.MELEE).setAccurate(false);
            hit2.submit();
        }
        CombatSpecial.drain(mob, CombatSpecial.ABYSSAL_DAGGER.getDrainAmount());
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
