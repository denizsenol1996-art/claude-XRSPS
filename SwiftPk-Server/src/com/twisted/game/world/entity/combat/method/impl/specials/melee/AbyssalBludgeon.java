package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.skills.Skills;

public class AbyssalBludgeon extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(3299);
        //TODO it.player().sound(2715, 10)
        //TODO it.player().sound(1930, 30)

        int damage = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);
        damage *= 1 + (((mob.skills().xpLevel(Skills.PRAYER) - mob.skills().level(Skills.PRAYER)) * 0.5) / 100.0);
        Hit hit = target.hit(mob, damage,1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        target.graphic(1284, 0, 15);
        CombatSpecial.drain(mob, CombatSpecial.ABYSSAL_BLUDGEON.getDrainAmount());
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
