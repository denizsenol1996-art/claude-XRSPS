package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class KillstreakCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.putAttrib(AttributeKey.KILLSTREAK, Integer.valueOf(parts[1]));
        player.message("Current killstreak set to "+parts[1]);
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
