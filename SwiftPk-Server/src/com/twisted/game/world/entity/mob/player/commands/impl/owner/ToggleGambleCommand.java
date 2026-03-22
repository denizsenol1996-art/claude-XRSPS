package com.twisted.game.world.entity.mob.player.commands.impl.owner;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;


public class ToggleGambleCommand implements Command {

    public static boolean gambleDisabled;

    @Override//updateox
    public void execute(Player player, String command, String[] parts) {

        if (parts[1].equalsIgnoreCase("on")) {
            gambleDisabled = false;
            player.message("@red@Gamble are now on");

        } else if (parts[1].equalsIgnoreCase("off")) {
            gambleDisabled = true;
            player.message("@red@Gamble are now off");

        } else {
            player.message("Player type ::togglegamble on or off");
        }

    }


    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
