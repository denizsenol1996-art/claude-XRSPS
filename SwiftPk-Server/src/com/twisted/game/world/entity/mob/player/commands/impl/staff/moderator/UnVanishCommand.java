package com.twisted.game.world.entity.mob.player.commands.impl.staff.moderator;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class UnVanishCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(player.getPlayerRights().isCm(player)){
            return;
        }
        player.looks().hide(false);
        player.message("You are now exposed again.");
    }//update5/2

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isModeratorOrGreater(player) || player.getUsername().equalsIgnoreCase("Swordsman75"));
    }
}
