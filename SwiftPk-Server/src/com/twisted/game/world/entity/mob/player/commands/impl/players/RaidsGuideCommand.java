package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class RaidsGuideCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://youtu.be/bw_e9lo6qVo");
        player.message("Opening the raids guide in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
