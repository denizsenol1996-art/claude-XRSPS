package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;

public class AncientMace extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(6147);
        mob.graphic(1027);
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if (target.dead()) {
            return false;
        }

        //TODO in combat ignore prayer, mace ignores overheads
        if (target.isPlayer()) {
            Player t = (Player) target;
            Player p = (Player) mob;
            t.skills().alterSkill(Skills.PRAYER, -hit.getDamage());
            p.skills().alterSkill(Skills.PRAYER, hit.getDamage());
        }
        CombatSpecial.drain(mob, CombatSpecial.ANCIENT_MACE.getDrainAmount());
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
