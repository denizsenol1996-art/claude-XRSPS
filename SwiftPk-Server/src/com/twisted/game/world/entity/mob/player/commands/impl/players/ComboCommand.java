package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;

import static com.twisted.util.ItemIdentifiers.COOKED_KARAMBWAN;

/**
 * @author Patrick van Elderen | June, 21, 2021, 14:30
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ComboCommand implements Command {

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

        if (player.inventory().hasCapacityFor(new Item(COOKED_KARAMBWAN))) {
            player.inventory().add(new Item(COOKED_KARAMBWAN,5));
        }
        player.message("You spawn some cooked karambwans.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
