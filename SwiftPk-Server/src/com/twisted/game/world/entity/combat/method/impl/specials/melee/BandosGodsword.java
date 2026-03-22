package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.ItemIdentifiers;

public class BandosGodsword extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        final Player player = (Player) mob;
        player.animate(player.getEquipment().contains(ItemIdentifiers.BANDOS_GODSWORD_OR) ? 7643 : 7642);
        boolean gfx_gold = player.getAttribOr(AttributeKey.BGS_GFX_GOLD, false);
        player.graphic(gfx_gold ? 1748 : 1212);

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if(hit.getDamage() > 0) {
            var skills = new int[]{Skills.DEFENCE, Skills.STRENGTH, Skills.PRAYER, Skills.ATTACK, Skills.MAGIC, Skills.RANGED};
            var deductionTotal = hit.getDamage();
            for (int i = 0; i <= skills.length; i++) {
                if (deductionTotal <= 0) break;
                var take = deductionTotal;

                // Identify the targets current level in this stat
                var targetCurrentStat = target.isPlayer() ? target.skills().level(skills[i])
                    :
                    new int[] {
                        target.getAsNpc().combatInfo().stats.attack,
                        target.getAsNpc().combatInfo().stats.defence,
                        target.getAsNpc().combatInfo().stats.strength,
                        0,
                        target.getAsNpc().combatInfo().stats.ranged,
                        0,
                        target.getAsNpc().combatInfo().stats.magic
                    }[i];

                if (targetCurrentStat - take < 0) // Cap the amount we can take away to that available.
                    take = targetCurrentStat;

                // Now reduce the stat.
                if (target.isPlayer()) {
                    target.skills().setLevel(skills[i], target.skills().level(skills[i]) - take);
                } else {
                    switch (i) {
                        case 0 -> target.getAsNpc().combatInfo().stats.attack -= take;
                        case 1 -> target.getAsNpc().combatInfo().stats.defence -= take;
                        case 2 -> target.getAsNpc().combatInfo().stats.strength -= take;
                        case 4 -> target.getAsNpc().combatInfo().stats.ranged -= take;
                        case 6 -> target.getAsNpc().combatInfo().stats.magic -= take;
                    }

                }
                deductionTotal -= take;
            }
        }
        CombatSpecial.drain(mob, CombatSpecial.BANDOS_GODSWORD.getDrainAmount());
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
