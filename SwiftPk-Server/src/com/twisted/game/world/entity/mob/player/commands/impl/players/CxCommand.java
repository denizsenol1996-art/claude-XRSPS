package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class CxCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://youtube.com/channel/UCGsXQ3YGHS_TGZgvmLptZ8g");
        player.message("Opening Cx channel in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
