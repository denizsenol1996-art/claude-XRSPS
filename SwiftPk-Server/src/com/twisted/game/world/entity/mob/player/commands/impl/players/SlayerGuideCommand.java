package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class SlayerGuideCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("http://hazyrealm.com/slayer-guide/");
        player.message("Opening the slayer guide in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
