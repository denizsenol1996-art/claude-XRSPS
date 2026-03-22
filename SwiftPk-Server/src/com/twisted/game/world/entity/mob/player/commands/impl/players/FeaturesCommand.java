package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class FeaturesCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://discord.com/channels/945970006649339925/1195544927300501624");
        player.message("Opening the donator features in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
