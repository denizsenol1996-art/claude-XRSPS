package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.GameServer;
import com.twisted.db.DatabaseExtensionsKt;
import com.twisted.game.GameEngine;
import com.twisted.game.content.mechanics.referrals.Referrals;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.PlayerStatus;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.entity.mob.player.commands.impl.kotlin.MiscKotlin;
import com.twisted.game.world.entity.mob.player.save.PlayerSave;
import com.twisted.util.PlayerPunishment;
import com.twisted.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class IPBanPlayerCommand implements Command {

    private static final Logger logger = LogManager.getLogger(IPBanPlayerCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (command.length() <= 6)
            return;
        String username = Utils.formatText(command.substring(6)); // after "ipban "
        if (GameServer.properties().enableSql && GameServer.properties().punishmentsToDatabase) {
            player.getDialogueManager().start(new BanDialogue(username));
        }

        if(GameServer.properties().punishmentsToLocalFile) {
            Optional<Player> playerToBan = World.getWorld().getPlayerByName(username);
            if (playerToBan.isPresent()) {
                if(playerToBan.get().getPlayerRights().isStaffMember(playerToBan.get()) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
                    player.message("You cannot ip ban this player.");
                    logger.warn(player.getUsername() + " tried to ip ban " + playerToBan.get().getUsername(), "warning");
                    return;
                }

                String IPToBan = playerToBan.get().getHostAddress();

                if (PlayerPunishment.ipBanned(IPToBan)) {
                    player.message("Player " + username + " already has an active ip ban.");
                    return;
                }

                //When in trade kick from trade first
                if(playerToBan.get().getStatus() == PlayerStatus.TRADING) {
                    playerToBan.get().getTrading().abortTrading();
                }

                //When in a duel forfeit for the player about to get banned
                if(playerToBan.get().getStatus() == PlayerStatus.DUELING) {
                    playerToBan.get().getDueling().onDeath();
                }

                //When in a gamble forfeit for the player about to get banned
                if(playerToBan.get().getStatus() == PlayerStatus.DUELING) {
                    playerToBan.get().getGamblingSession().abortGambling();
                }

                PlayerPunishment.addToIPBanList(IPToBan);
                playerToBan.get().requestLogout();

                player.message("Player " + username + " was successfully ip banned.");
                Utils.sendDiscordInfoLog(player, "Player " + username + " was ip banned by " + player.getUsername(), "sanctions");
            } else {
                //offline
                Player offlinePlayer = new Player();
                offlinePlayer.setUsername(Utils.formatText(username.substring(0, 1).toUpperCase() + username.substring(1)));

                GameEngine.getInstance().submitLowPriority(() -> {
                    try {
                        if (PlayerSave.loadOfflineWithoutPassword(offlinePlayer)) {
                            GameEngine.getInstance().addSyncTask(() -> {
                                String IPToBan = offlinePlayer.getHostAddress();

                                if(!PlayerSave.playerExists(offlinePlayer.getUsername())) {
                                    player.message("There is no such player profile.");
                                    return;
                                }

                                if (PlayerPunishment.ipBanned(IPToBan)) {
                                    player.message("Player " + offlinePlayer.getUsername() + " already has an active ip ban.");
                                    return;
                                }

                                PlayerPunishment.addToIPBanList(IPToBan);
                                player.message("Player " + offlinePlayer.getUsername() + " was successfully offline ip banned.");
                                Utils.sendDiscordInfoLog(player, "Player " + offlinePlayer.getUsername() + " was offline ip banned by " + player.getUsername(), "sanctions");
                            });
                        } else {
                            player.message("Something went wrong trying to offline ip ban "+offlinePlayer.getUsername());
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

    public static final class BanDialogue extends Dialogue {
        LocalDateTime unbanDate = null;
        private String username;

        public BanDialogue(String username) {

            this.username = username;
        }

        @Override
        protected void start(Object... parameters) {
            send(DialogueType.OPTION, "Enter Ban Duration", "Minutes", "Hours", "Days", "Permanent");
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
                public void handleSyntax(Player player, String reason) {
                    LocalDateTime unbanDate = null;
                    if (!type.equals("perm")) {
                        switch (type) {
                            case "hours":
                                unbanDate = LocalDateTime.now().plusHours(Long.parseLong(duration));
                                break;
                            case "days":
                                unbanDate = LocalDateTime.now().plusDays(Long.parseLong(duration));
                                break;
                            case "minutes":
                                unbanDate = LocalDateTime.now().plusMinutes(Long.parseLong(duration));
                                break;
                        }
                    } else {
                        unbanDate = LocalDateTime.now().plusYears(Long.parseLong(duration));
                    }
                    Timestamp unbanTimestamp = Timestamp.valueOf(unbanDate);
                    Optional<Player> plr = World.getWorld().getPlayerByName(username);
                    //System.out.println("Looking for " + username + " to ban");
                    if (plr.isPresent()) {
                        if (plr.get().getPlayerRights().greater(player.getPlayerRights())) {
                            player.message("You cannot ban that player!");
                            Utils.sendDiscordInfoLog(player, player.getUsername() + " tried to ban " + plr.get().getUsername(), "warning");
                            logger.warn(player.getUsername() + " tried to ban " + plr.get().getUsername(), "warning");
                            return;
                        }
                        plr.get().requestLogout();
                        MiscKotlin.INSTANCE.addIPBan(player, username, unbanTimestamp, reason, IPBanPlayerCommand.feedback(player));
                        player.message("Player " + username + " was successfully banned until " + unbanTimestamp + " for: " + reason);
                        Utils.sendDiscordInfoLog(player, "Player " + username + " was banned by " + player.getUsername() + " until " + unbanTimestamp + " for: " + reason, "sanctions");
                    } else {
                        final String player2Username = username;
                        if (Arrays.stream(player.getPlayerRights().getOwners()).anyMatch(name -> name.equalsIgnoreCase(player2Username))) {
                            player.message("You cannot ban that player!");
                            Utils.sendDiscordInfoLog(player, player.getUsername() + " tried to ban " + username, "warning");
                            logger.warn(player.getUsername() + " tried to ban " + username, "warning");
                            return;
                        }
                        DatabaseExtensionsKt.submit(Referrals.INSTANCE.getPlayerDbIdForName(username), id -> {
                            if (id == -1) {
                                player.message("There is no player by the name '"+username+"'");
                            } else {
                                MiscKotlin.INSTANCE.addIPBan(player, username, unbanTimestamp, reason, IPBanPlayerCommand.feedback(player));
                                player.message("Player " + username + " was successfully banned until " + unbanTimestamp + " for: " + reason);
                                Utils.sendDiscordInfoLog(player, "Player " + username + " was banned by " + player.getUsername() + " until " + unbanTimestamp + " for: " + reason, "sanctions");
                            }
                        });
                    }
                }
            });
            player.getPacketSender().sendEnterInputPrompt("Enter the reason:");
        }

    }

    public static Function1<? super List<? extends Player>, Unit> feedback(Player player) {
        return new Function1<List<? extends Player>, kotlin.Unit>() {
            @Override
            public kotlin.Unit invoke(List<? extends Player> players) {
                player.message(players.size()+" players were kicked matching the same ban criteria.");
                player.message("Names: "+Arrays.toString(players.stream().map(Player::getUsername).toArray()));
                //System.out.println("Names: "+Arrays.toString(players.stream().map(p -> p.getUsername()).toArray()));
                return null;
            }
        };
    }

}
