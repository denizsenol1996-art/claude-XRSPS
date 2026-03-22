package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.ItemIdentifiers;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date maart 20, 2020 16:50
 */
public class SaraBrewCommand implements Command {

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

        Item saradominBrew = new Item(ItemIdentifiers.SARADOMIN_BREW4);
        if (player.inventory().hasCapacityFor(saradominBrew)) {
            player.inventory().add(saradominBrew);
        } else {
            player.message("Your inventory does not have enough free space to do that.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
