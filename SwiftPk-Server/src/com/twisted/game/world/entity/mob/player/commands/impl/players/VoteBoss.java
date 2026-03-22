package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;

/**
 * @author the plateau
 */
public class VoteBoss implements Command {


    @Override
    public void execute(Player c, String command, String[] parts) {


        Tile tile = new Tile(3110, 3473);

        if (!Teleports.canTeleport(c, true, TeleportType.GENERIC)) {
            return;
        }

        Teleports.basicTeleport(c, tile);
        c.message("You have been teleported to vote boss area.");


    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
