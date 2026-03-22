package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

/**
 * For {@link TournamentManager}
 */
public class SetTornLobbyTime implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        if (TournamentManager.getSettings() == null) {
            player.message("The tournament system must be initialized using the conf file first.");
            return;
        } else {
            if (parts.length != 2) {
                player.message("Invalid use of command.");
                player.message("Use: ::settornlobbytime 30");
                return;
            }
            int seconds = Integer.parseInt(parts[1]);
            TournamentManager.getSettings().setLobbyTime(seconds);
            player.message("New lobby wait time is: "+ seconds + " seconds");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
