package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;

/**
 * @author Patrick van Elderen | June, 11, 2021, 10:23
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class RaidsTeleportCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        Tile tile = new Tile(1245, 3561);

        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
            return;
        }

        Teleports.basicTeleport(player, tile);
        player.message("You have been teleported to the raiding area.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
