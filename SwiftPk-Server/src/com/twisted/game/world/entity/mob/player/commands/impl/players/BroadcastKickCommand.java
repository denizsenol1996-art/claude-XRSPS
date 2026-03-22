package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class BroadcastKickCommand implements Command {

    String[] urls = {
      "https://www.kick.com/rippinheadz"
    };

    @Override
    public void execute(Player player, String command, String[] parts) {
        String url = null;
        if (player.getUsername().equalsIgnoreCase("rippinheadz")) {
            url = urls[0];
        }
        if (url == null) {
            player.message("You currently do not have a broadcast link setup.");
            return;
        }
        World.getWorld().sendWorldMessage("osrsbroadcast##" + player.getUsername() + " is live on www.kick.com! Click here to view" + "%%" + url);
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
