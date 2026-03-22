package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.fs.ItemDefinition;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;

import java.util.Arrays;

/**
 * @author PVE
 * @Since september 13, 2020
 */
public class IDefCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        int id = Integer.parseInt(parts[1]);
        ItemDefinition def = new Item(id).definition(World.getWorld());
        String opts = def.options == null ? "" : Arrays.toString(def.options);
        String iopts = def.ioptions == null ? "" : Arrays.toString(def.ioptions);
        player.message("Def %d options: %s %s", id, opts, iopts);
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }
}
