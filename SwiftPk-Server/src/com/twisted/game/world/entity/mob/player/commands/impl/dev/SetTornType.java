package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.content.tournaments.Tournament;
import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class SetTornType implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        if (TournamentManager.getSettings() == null) {
            player.message("The tournament system must be initialized using the conf file first.");
            return;
        } else {
            if (parts.length != 2) {
                player.message("Invalid use of command.");
                player.message("Use: ::settorntype [0-4]");
                return;
            }
            int type = Integer.parseInt(parts[1]);
            TournamentManager.setNextTorn(new Tournament(TournamentManager.settings.getTornConfigs()[type]));
            player.message("New PvP tournament type is: "+ TournamentManager.getNextTorn().fullName());
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
