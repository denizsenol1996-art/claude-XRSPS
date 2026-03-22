package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class BMMultiplierCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        int amt = Math.max(1, Math.min(5, Integer.parseInt(parts[1])));
        player.message("BM multiplier changed from " + World.getWorld().bmMultiplier + " to " + amt + ".");
        World.getWorld().bmMultiplier = amt;
        World.getWorld().sendWorldMessage("<col=ca0d0d> Double BM is now " + (amt == 1 ? "OFF" : "ON") + ".");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
