package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;

/**
 * @author Patrick van Elderen | January, 11, 2021, 18:06
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 * @Chase - very sexy boi
 */
public class ZulrahCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(!player.getMemberRights().isRubyOrGreater(player)) {
            player.message("You have to be at least a Ruby Member to use this command.");
            return;
        }

        Tile tile = new Tile(2201, 3057, 0);

        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
            return;
        }

        Teleports.basicTeleport(player, tile);
        player.message("You have been teleported to Zulrah!");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
