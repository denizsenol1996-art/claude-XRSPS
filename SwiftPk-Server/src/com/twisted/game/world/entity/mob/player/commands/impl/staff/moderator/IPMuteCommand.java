package com.twisted.game.world.entity.mob.player.commands.impl.staff.moderator;

import com.twisted.GameServer;
import com.twisted.game.GameEngine;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.entity.mob.player.save.PlayerSave;
import com.twisted.util.PlayerPunishment;
import com.twisted.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * @author Patrick van Elderen | January, 16, 2021, 11:01
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class IPMuteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(IPMuteCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (command.length() <= 7)
            return;
        String username = Utils.formatText(command.substring(7)); // after "ipmute "
        if (GameServer.properties().enableSql && GameServer.properties().punishmentsToDatabase) {
            //TODO future
        }

        if(GameServer.properties().punishmentsToLocalFile) {
            Optional<Player> playerToMute = World.getWorld().getPlayerByName(username);
            if (playerToMute.isPresent()) {
                if (playerToMute.get().getPlayerRights().isStaffMember(playerToMute.get()) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
                    player.message("You cannot ip mute this player.");
                    logger.warn(player.getUsername() + " tried to ip mute " + playerToMute.get().getUsername(), "warning");
                    return;
                }

                String IPToMute = playerToMute.get().getHostAddress();

                if (PlayerPunishment.IPmuted(IPToMute)) {
                    player.message("Player " + playerToMute.get().getUsername() + " already has an active ip mute.");
                    return;
                }

                playerToMute.get().putAttrib(AttributeKey.MUTED, true);
                PlayerPunishment.ipmute(IPToMute);
                player.message("Player " + playerToMute.get().getUsername() + " was successfully ip muted.");
                Utils.sendDiscordInfoLog(player, "Player " + playerToMute.get().getUsername() + " was ip muted by " + player.getUsername(), "sanctions");
            } else {
                //offline
                Player offlinePlayer = new Player();
                offlinePlayer.setUsername(Utils.formatText(username.substring(0, 1).toUpperCase() + username.substring(1)));

                GameEngine.getInstance().submitLowPriority(() -> {
                    try {
                        if (PlayerSave.loadOfflineWithoutPassword(offlinePlayer)) {
                            GameEngine.getInstance().addSyncTask(() -> {
                                String IPToMute = offlinePlayer.getHostAddress();

                                if (PlayerPunishment.IPmuted(IPToMute)) {
                                    player.message("Player " + offlinePlayer.getUsername() + " already has an active ip mute.");
                                    return;
                                }

                                offlinePlayer.putAttrib(AttributeKey.MUTED, true);
                                PlayerPunishment.ipmute(IPToMute);
                                player.message("Player " + offlinePlayer.getUsername() + " was successfully offline ip muted.");
                                Utils.sendDiscordInfoLog(player, "Player " + offlinePlayer.getUsername() + " was offline ip muted by " + player.getUsername(), "sanctions");
                            });
                        } else {
                            player.message("Something went wrong trying to offline ip mute "+offlinePlayer.getUsername());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }

}
