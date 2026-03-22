package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date maart 21, 2020 22:38
 */
public class WalkableInterfaceCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getInterfaceManager().openWalkable(Integer.parseInt(parts[1]));
    }

    @Override
    public boolean canUse(Player player) {

        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
