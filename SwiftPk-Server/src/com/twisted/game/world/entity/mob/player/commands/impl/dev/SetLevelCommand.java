package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;

/**
 * @author PVE
 * @Since augustus 28, 2020
 */
public class SetLevelCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length < 3) {
            player.message("Usage: ::lvl skill level. Example: ::lvl 1 99");
        } else {
            int skill = Integer.parseInt(parts[1]);
            int lvl = Integer.parseInt(parts[2]);
            if (!(skill >= 0 && skill < Skills.SKILL_COUNT)) {
                player.message("Invalid skill id: " + skill);
                return;
            }
            // Any equip?
            if (!player.getPlayerRights().isDeveloperOrGreater(player)) {
                if (skill == Skills.HITPOINTS && lvl < 10) {
                    player.message("Hitpoints cannot go under <col=FF0000>10</col>.");
                    lvl = 10;
                }
                for (Item item : player.getEquipment().getItems()) {
                    if (item != null) {
                        player.message("You cannot change your levels while having <col=FF0000>equipment</col> on.");
                        return;
                    }
                }
            }

            // Turn off prayers
            Prayers.closeAllPrayers(player);

            player.skills().setXp(skill, Skills.levelToXp(Math.min(120, lvl)));
            player.skills().update();
            player.skills().recalculateCombat();
            player.message("Skill <col=FF0000>" + Skills.SKILL_NAMES[skill] + "</col> set to <col=FF0000>" + player.skills().levels()[skill] + "</col>.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
