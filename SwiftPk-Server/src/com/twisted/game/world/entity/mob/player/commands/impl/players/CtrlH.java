package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.GameServer;
import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;

public class CtrlH implements Command {

    public void execute(Player player, String command, String[] parts) {
        Tile tile = GameServer.properties().defaultTile;
        if (!player.getMemberRights().isDiamondOrGreater(player) && !WildernessArea.inWilderness(player.tile())) {
            player.message("You need to become a Diamond to use that benefits or get out of wild.");
            return;
        }
        if (!Teleports.canTeleport(player, true, TeleportType.GENERIC)) {
            return;
        }

        Teleports.basicTeleport(player, tile);
        player.message("You have been teleported to home.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
