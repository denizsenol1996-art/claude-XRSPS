package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.GameServer;
import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;

/**
 * @author Patrick van Elderen | January, 25, 2021, 21:10
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class DuelArenaCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (!GameServer.properties().enableDueling && player.getPlayerRights().isDeveloperOrGreater(player)) {
            return;
        }

        Tile tile = GameServer.properties().duelTile;

        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
            return;
        }

        Teleports.basicTeleport(player, tile);
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
