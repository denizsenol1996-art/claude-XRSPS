package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.formula.accuracy.data.MeleeAccuracy;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;

public class SaradominSword extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(1132);
        mob.graphic(1213, 100, 0);

        MeleeAccuracy accuracy = new MeleeAccuracy(mob, target, CombatType.MELEE);
        double chance = World.getWorld().random().nextDouble();
        boolean success = accuracy.success(chance);
        int meleeHit = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);
        int magicHit = CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC);
        if (success) {
            if (meleeHit > 0) {
                magicHit = World.getWorld().random(1, 16);
            }
        } else {
            meleeHit = 0;
            magicHit = 0;
        }

        Hit hit = target.hit(mob, meleeHit,1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        Hit hit2 = target.hit(mob, magicHit,1, CombatType.MAGIC).checkAccuracy();
        hit2.submit();
        CombatSpecial.drain(mob, CombatSpecial.SARADOMIN_SWORD.getDrainAmount());
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
