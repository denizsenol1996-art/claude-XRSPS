package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;

/**
 * @author Patrick van Elderen | July, 12, 2021, 15:01
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class AncientStatiusWarhammer extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(1378);
        mob.graphic(844);

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        // Nerf a player's def if it's a player
        if (target.isPlayer()) {
            Player playerTarget = (Player) target;
            playerTarget.skills().alterSkill(Skills.DEFENCE, (int) -(playerTarget.skills().level(Skills.DEFENCE) * 0.3));
        } else if (target.isNpc()) {
            Npc npcTarget = (Npc) target;
            npcTarget.combatInfo().stats.defence = (int) Math.max(0, npcTarget.combatInfo().stats.defence - (npcTarget.combatInfo().stats.defence * 0.3));
        }
        CombatSpecial.drain(mob, CombatSpecial.ANCIENT_STATIUS_WARHAMMER.getDrainAmount());
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
