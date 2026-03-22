package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;

public class RunesCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(player.ironMode() != IronMode.NONE) {
            player.message("As an ironman you cannot use this command.");
            return;
        }

        if (!player.tile().inSafeZone() && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can only use this command at safe zones.");
            return;
        }

        if(WildernessArea.inWilderness(player.tile())) {
            player.message("You can only use this command at safe zones.");
            return;
        }

        Item[] runes = { new Item(554, 1000), new Item(555, 1000), new Item(556, 1000), new Item(557, 1000),
                new Item(558, 1000), new Item(559, 1000), new Item(560, 1000), new Item(561, 1000), new Item(562, 1000),
                new Item(563, 1000), new Item(564, 1000), new Item(565, 1000), new Item(9075, 1000), new Item(566, 1000) };
        if (player.inventory().hasCapacityFor(runes)) {
            player.message("You spawn some runes.");
            player.inventory().addAll(runes);
        } else {
            player.message("Your inventory does not have enough free space to do that.");
        }

    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
