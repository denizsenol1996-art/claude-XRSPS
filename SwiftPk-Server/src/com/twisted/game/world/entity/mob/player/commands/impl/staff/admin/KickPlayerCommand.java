package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class KickPlayerCommand implements Command {

    private static final Logger logger = LogManager.getLogger(KickPlayerCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        String player2 = Utils.formatText(command.substring(5)); // after "kick "
        Optional<Player> plr = World.getWorld().getPlayerByName(player2);
        if (plr.isPresent()) {
            if (plr.get().getPlayerRights().greater(player.getPlayerRights())) {
                player.message("You cannot kick that player!");
                logger.warn(player.getUsername() + " tried to kick " + plr.get().getUsername(), "warning");
                Utils.sendDiscordInfoLog(player, player.getUsername() + " tried to kick " + plr.get().getUsername(), "warning");
                return;
            }
            plr.get().requestLogout();
            player.message("Player " + player2 + " ("+plr.get().getUsername()+") has been kicked.");
            Utils.sendDiscordInfoLog(player, player.getUsername() + " has kicked " + plr.get().getUsername(), "sanctions");
        } else {
            player.message("Player " + player2 + " does not exist or is not online.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isSupportOrGreater(player));
    }

}
