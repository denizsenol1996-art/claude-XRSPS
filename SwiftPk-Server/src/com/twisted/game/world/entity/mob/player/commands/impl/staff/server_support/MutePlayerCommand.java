package com.twisted.game.world.entity.mob.player.commands.impl.staff.server_support;

import com.twisted.GameServer;
import com.twisted.db.DatabaseExtensionsKt;
import com.twisted.db.transactions.MutePlayerDatabaseTransaction;
import com.twisted.game.GameEngine;
import com.twisted.game.content.mechanics.referrals.Referrals;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.PlayerPunishment;
import com.twisted.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

public class MutePlayerCommand implements Command {
    private static final Logger logger = LogManager.getLogger(MutePlayerCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (command.length() <= 5)
            return;
        String username = Utils.formatText(command.substring(5)); // after "mute "
        if (GameServer.properties().enableSql && GameServer.properties().punishmentsToDatabase) {
            player.getDialogueManager().start(new MuteDialogue(username));
        }

        if(GameServer.properties().punishmentsToLocalFile) {
            Optional<Player> playerToMute = World.getWorld().getPlayerByName(username);
            if (playerToMute.isPresent()) {
                if (playerToMute.get().getPlayerRights().isStaffMember(playerToMute.get()) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
                    player.message("You cannot mute this player.");
                    logger.warn(player.getUsername() + " tried to mute " + playerToMute.get().getUsername(), "warning");
                    return;
                }

                if (PlayerPunishment.muted(username)) {
                    player.message("Player " + username + " already has an active mute.");
                    return;
                }
            }

            playerToMute.ifPresent(value -> value.putAttrib(AttributeKey.MUTED,true));
            PlayerPunishment.mute(username);
            player.message("Player " + username + " was successfully muted.");
            Utils.sendDiscordInfoLog(player, "Player " + username + " was muted by " + player.getUsername(), "sanctions");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isSupportOrGreater(player));
    }

    public static final class MuteDialogue extends Dialogue {
        LocalDateTime unmuteDate = null;
        private String username;

        public MuteDialogue(String username) {

            this.username = username;
        }

        @Override
        protected void start(Object... parameters) {
            send(DialogueType.OPTION, "Enter Mute Duration", "Minutes", "Hours", "Days", "Permanent");
            setPhase(1);
        }

        @Override
        public void select(int option) {
            if (isPhase(1)) {
                if (option == 1) {
                    stop();
                    enterDuration("minutes");
                } else if (option == 2) {
                    stop();
                    enterDuration("hours");
                } else if (option == 3) {
                    stop();
                    enterDuration("days");
                } else {
                    stop();
                    enterReason("99", "perm");
                }
            }
        }

        private void enterDuration(String type) {
            player.getInterfaceManager().close();
            player.setEnterSyntax(new EnterSyntax() {
                @Override
                public void handleSyntax(Player player, String input) {
                    //This is a sync task because it needs to be done on the next tick, since inputs cannot be chained, it closes immediately if you try to put an input event inside another one.
                    GameEngine.getInstance().addSyncTask(() -> enterReason(input, type));
                }
            });
            if (!type.equals("perm")) {
                player.getPacketSender().sendEnterInputPrompt("Enter duration in " + type + ":");
            }
        }

        private void enterReason(String duration, String type) {
            player.getInterfaceManager().close();
            player.setEnterSyntax(new EnterSyntax() {
                @Override
                public void handleSyntax(Player player, String input) {
                    LocalDateTime unmuteDate = null;
                    if (!type.equals("perm")) {
                        switch (type) {
                            case "hours":
                                unmuteDate = LocalDateTime.now().plusHours(Long.parseLong(duration));
                                break;
                            case "days":
                                unmuteDate = LocalDateTime.now().plusDays(Long.parseLong(duration));
                                break;
                            case "minutes":
                                unmuteDate = LocalDateTime.now().plusMinutes(Long.parseLong(duration));
                                break;
                        }
                    } else {
                        unmuteDate = LocalDateTime.now().plusYears(Long.parseLong(duration));
                    }
                    Timestamp unmuteTimestamp = Timestamp.valueOf(unmuteDate);
                    Optional<Player> plr = World.getWorld().getPlayerByName(username);
                    //System.out.println("Looking for " + username + " to mute");
                    if (plr.isPresent()) {
                        if (plr.get().getPlayerRights().greater(player.getPlayerRights())) {
                            player.message("You cannot mute that player!");
                            Utils.sendDiscordInfoLog(player, player.getUsername() + " tried to mute " + plr.get().getUsername(), "warning");
                            logger.warn(player.getUsername() + " tried to mute " + plr.get().getUsername(), "warning");
                            return;
                        }
                        plr.get().putAttrib(AttributeKey.MUTED,true);
                        plr.get().message("You have been muted until " + unmuteTimestamp + " for: " + input);
                        GameServer.getDatabaseService().submit(new MutePlayerDatabaseTransaction(username, unmuteTimestamp, input));
                        player.message("Player " + username + " was successfully muted until " + unmuteTimestamp + " for: " + input);
                        Utils.sendDiscordInfoLog(player, "Player " + username + " was muted by " + player.getUsername() + " until " + unmuteTimestamp + " for: " + input, "sanctions");
                    } else {
                        final String player2Username = username;
                        if (Arrays.stream(player.getPlayerRights().getOwners()).anyMatch(name -> name.equalsIgnoreCase(player2Username))) {
                            player.message("You cannot mute that player!");
                            Utils.sendDiscordInfoLog(player, player.getUsername() + " tried to mute " + username, "warning");
                            logger.warn(player.getUsername() + " tried to mute " + username, "warning");
                            return;
                        }
                        DatabaseExtensionsKt.submit(Referrals.INSTANCE.getPlayerDbIdForName(username), id -> {
                            if (id == -1) {
                                player.message("There is no player by the name '"+username+"'");
                            } else {
                                GameServer.getDatabaseService().submit(new MutePlayerDatabaseTransaction(username, unmuteTimestamp, input));
                                player.message("Player " + username + " was successfully muted until " + unmuteTimestamp + " for: " + input);
                                Utils.sendDiscordInfoLog(player, "Player " + username + " was muted by " + player.getUsername() + " until " + unmuteTimestamp + " for: " + input, "sanctions");
                            }
                        });
                    }
                }
            });
            player.getPacketSender().sendEnterInputPrompt("Enter the reason:");
        }
    }

}
