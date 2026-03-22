package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.util.chainedwork.Chain;

public class InquisitorsMace extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(1060);

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        for (int index = 0; index < 6; index++) {
            Chain.bound(null).name("ele_bow_freeze_effect").cancelWhen(() -> {
                return !mob.tile().isWithinDistance(target.tile()) || target.dead(); // cancels as expected
            }).runFn(index * 4, () -> {
                Hit bleed = target.hit(mob, World.getWorld().random(1, 5),4, CombatType.MELEE).checkAccuracy();
                bleed.submit();
            });
        }
        CombatSpecial.drain(mob, CombatSpecial.INQUISITORS_MACE.getDrainAmount());
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
