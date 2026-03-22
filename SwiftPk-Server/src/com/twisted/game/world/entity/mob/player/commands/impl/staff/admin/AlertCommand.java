package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class AlertCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
       String msg = "";
       for (int i = 1; i < parts.length; i++) {
           msg += parts[i] + " ";
       }
        World.getWorld().sendWorldMessage("Alert##Notification##" + msg+ "##From: " + player.getUsername() );
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }

}
