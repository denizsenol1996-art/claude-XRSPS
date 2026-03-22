package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.GameServer;
import com.twisted.db.transactions.UnbanPlayerDatabaseTransaction;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.entity.mob.player.save.PlayerSave;
import com.twisted.util.PlayerPunishment;
import com.twisted.util.Utils;

public class UnBanPlayerCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (command.length() <= 6)
            return;
        String username = Utils.formatText(command.substring(6)); // after "unban "

        if (GameServer.properties().enableSql && GameServer.properties().punishmentsToDatabase) {
            GameServer.getDatabaseService().submit(new UnbanPlayerDatabaseTransaction(username));
        }

        if(GameServer.properties().punishmentsToLocalFile) {
            if (!PlayerSave.playerExists(username)) {
                player.message("Player " + username + " does not exist.");
                return;
            }

            //Remove from regular ban list
            if(PlayerPunishment.banned(username)) {
                PlayerPunishment.unban(username);
            }
        }
        player.message("Player " + username + " was successfully unbanned.");
        Utils.sendDiscordInfoLog(player, "Player " + username + " was unbanned by " + player.getUsername(), "sanctions");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isModeratorOrGreater(player));
    }

}
