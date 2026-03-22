package com.twisted.game.world.entity.mob.player.commands;

import com.twisted.game.world.entity.mob.player.Player;

public interface Command {

    abstract void execute(Player player, String command, String[] parts);

    abstract boolean canUse(Player player);
}
