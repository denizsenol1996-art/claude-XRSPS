package com.twisted.game.world.entity.mob.player.commands.impl.owner;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;


public class ToggleDuelCommand implements Command {

    public static boolean duelDisabled;

    @Override//updateox
    public void execute(Player player, String command, String[] parts) {

        if (parts[1].equalsIgnoreCase("on")) {
            duelDisabled = false;
            player.message("@red@Duel are now on");

        } else if (parts[1].equalsIgnoreCase("off")) {
            duelDisabled = true;
            player.message("@red@Duel are now off");

        } else {
            player.message("Player type ::toggleDuel on or off");
        }

    }


    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
