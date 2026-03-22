package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.ItemIdentifiers;

/**
 * @author PVE
 * @Since augustus 06, 2020
 */
public class Banshee extends CommonCombatMethod {

    private static final int[] DRAIN = { Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGED, Skills.MAGIC, Skills.PRAYER, Skills.AGILITY};

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());

        Player player = (Player) target;

        if(!player.getEquipment().contains(ItemIdentifiers.EARMUFFS) && !player.getEquipment().wearingSlayerHelm()) {
            player.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE) + 6, CombatType.MELEE).submit();
            for (int skill : DRAIN) {
                player.skills().alterSkill(skill, -5);
            }
            player.message("The banshee's deafening scream drains your stats!");
        } else {
            player.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
        }
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
