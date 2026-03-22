package com.twisted.game.content.syntax.impl;


import com.twisted.game.GameEngine;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.save.PlayerSave;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class ChangeOtherPasswordSyntax implements EnterSyntax {
    private static final Logger logger = LogManager.getLogger(ChangeOtherPasswordSyntax.class);
    private Optional<Player> player2;
    private String player2Name;

    public ChangeOtherPasswordSyntax(Optional<Player> player2, String player2Name) {
        this.player2 = player2;
        this.player2Name = player2Name;
    }
    @Override
    public void handleSyntax(Player player, String input) {

        if (input.length() > 2 && input.length() < 20) {
            if (player2.isPresent()) {
                if (PlayerSave.playerExists(player2.get().getUsername())) {
                    player2.get().setNewPassword(input);
                } else {
                    player2.get().setPassword(input);
                }
                player2.get().setNewPassword(input);
                player.message("You have changed the password of online player " + player2.get().getUsername());
            } else {
                Player plr2 = new Player();
                plr2.setUsername(player2Name.substring(0, 1).toUpperCase() + player2Name.substring(1));
                GameEngine.getInstance().submitLowPriority(() -> {
                    String password = BCrypt.hashpw(input, BCrypt.gensalt());
                    GameEngine.getInstance().addSyncTask(() -> {
                        plr2.setPassword(password);
                        GameEngine.getInstance().submitLowPriority(() -> {
                            try {
                                if (PlayerSave.loadOffline(plr2, password)) {
                                    PlayerSave.save(plr2);
                                    player.message("You have changed the password of offline player " + player2Name.substring(0, 1).toUpperCase() + player2Name.substring(1));
                                } else {
                                    player.message("Something went wrong changing the password of offline player " + player2Name.substring(0, 1).toUpperCase() + player2Name.substring(1));
                                }
                            } catch (Exception e) {
                                logger.catching(e);
                            }
                        });
                    });
                });
            }
        } else {
            player.message("Invalid password input.");
        }
    }

    @Override
    public void handleSyntax(Player player, long input) {

    }
}
