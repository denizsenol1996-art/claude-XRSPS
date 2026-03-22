package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since November 03, 2021
 */
public class VihticCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://www.youtube.com/c/Vihtic");
        player.message("Opening Vihtic's channel in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
