package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.save.PlayerSave;

public class ChangePasswordSyntax implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
        if (input.length() > 2 && input.length() < 20) {
            if (PlayerSave.playerExists(player.getUsername())) {
                player.setNewPassword(input);
            } else {
                player.setPassword(input);
            }
            //player.message("Your password is now: " + pass);
            player.message("You have successfully changed your password.");
        } else {
            player.message("Invalid password input.");
        }
    }

    @Override
    public void handleSyntax(Player player, long input) {

    }
}
