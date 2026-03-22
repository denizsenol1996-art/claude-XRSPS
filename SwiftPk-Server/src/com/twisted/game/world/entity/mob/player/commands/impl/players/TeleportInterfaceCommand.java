package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class TeleportInterfaceCommand implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        player.setCurrentTabIndex(4);
        player.getInterfaceManager().open(29050);
    }

    @Override
    public boolean canUse(Player player) {
        return player.getMemberRights().isRubyOrGreater(player) || player.getPlayerRights().isAdminOrGreater(player);
    }
}
