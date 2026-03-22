package com.twisted.game.world.entity.mob.player.commands.impl.staff.moderator;

import com.twisted.game.GameEngine;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.PlayerPunishment;
import com.twisted.util.Utils;

/**
 * @author Patrick van Elderen | February, 04, 2021, 14:07
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class UnIPMuteCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (command.length() <= 8)
            return;
        String IPToRemove = Utils.formatText(command.substring(9)); // after "unipmute "

        if (IPToRemove.isEmpty()) {
            player.message("You must enter a valid IP.");
            return;
        }
        GameEngine.getInstance().submitLowPriority(() -> {
            try {
                GameEngine.getInstance().addSyncTask(() -> {
                    if (!PlayerPunishment.IPmuted(IPToRemove)) {
                        player.message("This IP address is not listed as IP muted");
                        return;
                    }

                    PlayerPunishment.unIPMuteUser(IPToRemove);
                    player.message("The IP: " + IPToRemove + " was successfully removed from the mute list.");
                    Utils.sendDiscordInfoLog(player, "The IP: " + IPToRemove + " was successfully removed from the mute list and was unmuted by " + player.getUsername(), "sanctions");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }

}
