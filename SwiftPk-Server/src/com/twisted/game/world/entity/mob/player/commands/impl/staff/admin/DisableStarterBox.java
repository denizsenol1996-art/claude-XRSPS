package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.content.new_players.StarterBox;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class DisableStarterBox implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        StarterBox.STARTER_BOX_ENABLED =! StarterBox.STARTER_BOX_ENABLED;
        String msg = StarterBox.STARTER_BOX_ENABLED ? "Enabled" : "Disabled";
        player.message("The starter box is now "+msg+".");
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
