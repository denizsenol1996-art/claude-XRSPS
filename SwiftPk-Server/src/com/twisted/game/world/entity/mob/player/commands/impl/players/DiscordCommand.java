package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class DiscordCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
      player.getPacketSender().sendURL("https://discord.gg/hazyrsps");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
