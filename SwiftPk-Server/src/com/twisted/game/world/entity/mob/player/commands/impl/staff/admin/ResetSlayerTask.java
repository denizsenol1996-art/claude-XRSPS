package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.content.skill.impl.slayer.Slayer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Utils;

import java.util.Optional;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 18, 2020
 */
public class ResetSlayerTask implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length < 2) {
            player.message("Invalid syntax. Please enter a username.");
            player.message("::resetslayertask username");
            return;
        }
        final String player2 = Utils.formatText(command.substring(parts[0].length() + 1));
        Optional<Player> plr = World.getWorld().getPlayerByName(player2);
        if (plr.isPresent()) {

            Slayer.cancelTask(plr.get(), true);
            player.message("You have reset the slayer task for player "+plr.get().getUsername()+".");
            plr.get().message("Your slayer task has been reset, talk to any slayer master for a new task.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }

}
