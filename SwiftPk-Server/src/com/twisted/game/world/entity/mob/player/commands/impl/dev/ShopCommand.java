package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class ShopCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(parts.length != 2)
            return;
        int shop= Integer.parseInt(parts[1]);
        World.getWorld().shop(shop).open(player);
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
