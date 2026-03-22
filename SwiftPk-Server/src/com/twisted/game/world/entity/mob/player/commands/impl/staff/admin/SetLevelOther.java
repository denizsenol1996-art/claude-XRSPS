package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Utils;

import java.util.Optional;

public class SetLevelOther implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length < 4) {
            player.message("Invalid use of command.");
            player.message("Example: ::setlevelo hc_skii skill_id lvl");
            return;
        }
        String username = Utils.formatText(parts[1].replace("_", " ")); // after "setlevelo "
        try {
            int skill_id = Integer.parseInt(parts[2]);
            int lvl = Integer.parseInt(parts[3]);
            Optional<Player> plr = World.getWorld().getPlayerByName(username);
            if (plr.isPresent()) {
                if (player.getHostAddress().equalsIgnoreCase(plr.get().getHostAddress()) &&
                    !player.getPlayerRights().isDeveloperOrGreater(player)) {
                    player.message("You can't set levels for yourself or for players on the same IP.");
                    return;
                }

                if (!(skill_id >= 0 && skill_id < Skills.SKILL_COUNT)) {
                    player.message("Invalid skill id: " + skill_id);
                    return;
                }

                if (skill_id == Skills.HITPOINTS && lvl < 10) {
                    player.message("Hitpoints cannot go under <col=FF0000>10</col>.");
                    lvl = 10;
                }

                // Turn off prayers
                Prayers.closeAllPrayers(plr.get());

                plr.get().skills().setXp(skill_id, Skills.levelToXp(Math.min(99, lvl)));
                plr.get().skills().update();
                plr.get().skills().recalculateCombat();
                player.message("<col=FF0000>" + plr.get().getUsername()+ "'s " + Skills.SKILL_NAMES[skill_id] + "</col> set to <col=FF0000>" + plr.get().skills().levels()[skill_id] + "</col>.");
            } else {
                player.message("The player " + username + " is not online.");
            }
        } catch (NumberFormatException e) {
            player.message("Command failed: incorrect usage of the command.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
