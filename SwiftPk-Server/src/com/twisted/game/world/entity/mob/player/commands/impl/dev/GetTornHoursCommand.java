package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

import java.util.Arrays;

public class GetTornHoursCommand implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        if (TournamentManager.getSettings() == null) {
            player.message("The tournament system has not been initialized and has no start hours.");
            return;
        }
        player.message("Tournament hours are set to: "+ Arrays.toString(TournamentManager.getSettings().getStartTimes()));
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
