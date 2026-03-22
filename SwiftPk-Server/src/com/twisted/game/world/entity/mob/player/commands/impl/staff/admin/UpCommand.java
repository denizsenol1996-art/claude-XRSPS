package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

/**
 * @author PVE
 * @Since september 13, 2020
 */
public class UpCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.teleport(player.tile().x, player.tile().y, Math.min(3, player.tile().level + 1));
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }
}
