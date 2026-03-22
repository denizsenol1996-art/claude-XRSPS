package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.Color;

/**
 * @author Patrick van Elderen | February, 16, 2021, 10:11
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class AncientWarriorAxe extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(2779);

        mob.forceChat("I CALL UP ON THE STRENGTH OF THE ANCIENT WARRIORS!");
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if (target instanceof Player) {
            Player playerTarget = (Player) target;
            if(hit.getDamage() > 0) {
                var drainPoints = hit.getDamage() * 35 / 100;
                playerTarget.message(Color.RED.tag()+"Your Defence and Prayer stats have been drained.");
                playerTarget.skills().alterSkill(Skills.DEFENCE, -drainPoints);
                playerTarget.skills().alterSkill(Skills.PRAYER, -drainPoints);
            }
        }
        CombatSpecial.drain(mob, CombatSpecial.ANCIENT_WARRIOR_AXE.getDrainAmount());
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
