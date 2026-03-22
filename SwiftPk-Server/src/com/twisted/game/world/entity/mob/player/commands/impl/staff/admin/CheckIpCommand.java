package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.GameServer;
import com.twisted.db.transactions.FindPlayersFromIpDatabaseTransaction;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CheckIpCommand implements Command {
    private static final Logger logger = LogManager.getLogger(CheckIpCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (!GameServer.properties().enableSql) {
             player.message("This command is currently disabled.");
             return;
        }
        if (parts.length < 1) {
            player.message("Invalid syntax. Please use: ::checkip [IP]");
            player.message("Example: ::checkip 127.0.0.1 ");
            return;
        }
        String IP = parts[1];
        GameServer.getDatabaseService().submit(new FindPlayersFromIpDatabaseTransaction(IP), foundPlayers -> {
            String foundPlayersString = "";
            for (String foundPlayer : foundPlayers) {
                foundPlayersString = foundPlayersString.concat(foundPlayer + ", ");
            }
            player.message("The players with IP: " + IP + " are: " + foundPlayersString.replaceAll(", $", ""));
            logger.info("The players with IP: " + IP + " are: " + foundPlayersString.replaceAll(", $", ""));
        });
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }
}
