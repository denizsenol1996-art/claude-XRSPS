package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class CoxExitCommand implements Command {


    public void execute(Player player, String command, String[] parts) {
        if (player.getRaids() != null && player.getRaids().raiding(player)) {
            if (player.getRaids() != null) {
                player.getRaids().exit(player);
            }
            player.healPlayer();


        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}





