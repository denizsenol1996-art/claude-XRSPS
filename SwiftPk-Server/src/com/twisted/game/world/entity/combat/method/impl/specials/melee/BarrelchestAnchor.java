package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.Utils;

public class BarrelchestAnchor extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(5870);
        mob.graphic(1027);
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if (target.isPlayer()) {
            Player playerTarget = (Player) target;

            int roll = Utils.random(4);
            int skill;
            int deduceVal = (int) (hit.getDamage() * 0.10);

            if (roll == 1) {
                skill = Skills.ATTACK;
            } else if (roll == 2) {
                skill = Skills.DEFENCE;
            } else if (roll == 3) {
                skill = Skills.RANGED;
            } else {
                skill = Skills.MAGIC;
            }

            playerTarget.skills().alterSkill(skill, -deduceVal);
        }
        CombatSpecial.drain(mob, CombatSpecial.BARRELSCHEST_ANCHOR.getDrainAmount());
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
