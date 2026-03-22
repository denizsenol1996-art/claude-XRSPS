package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;

public class TeleToLocationCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int z = player.tile().getLevel(); // stay on current lvl
        if (parts.length == 4) {
            z = Integer.parseInt(parts[3]);
        }
        player.teleport(new Tile(x, y, z));
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player) || player.getPlayerRights().isEventManager(player));
    }

}
