package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;

public class DragonDagger extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(1062);
        mob.graphic(252, 92, 0);
        //TODO it.player().world().spawnSound(it.player().tile(), 2537, 0, 10)

        int h1 = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);
        int h2 = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);
        Hit hit = target.hit(mob, h1,1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        Hit hit2 = target.hit(mob, h2,target.isNpc() ? 0 : 1, CombatType.MELEE).checkAccuracy();
        hit2.submit();
        CombatSpecial.drain(mob, CombatSpecial.DRAGON_DAGGER.getDrainAmount());
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
