package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;

/**
 * @author The plateau
 */
public class CtrlS implements Command {

    public void execute(Player player, String command, String[] parts) {

        if (!player.getMemberRights().isDiamondOrGreater(player) && !WildernessArea.inWilderness(player.tile())) {
            player.message("You need to become a Diamond to use that benefits or get out of wild.");
            return;
        }
        Tile tile = new Tile(3079, 3493, 0);

        if (Teleports.canTeleport(player, true, TeleportType.GENERIC)) {
            Teleports.basicTeleport(player, tile);
        }

        player.message("You have been teleported to the shops area.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
