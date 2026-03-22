package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.MagicSpellbook;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;

public class VengCommand implements Command {

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

        Item[] vengeance_runes = {new Item(9075, 400), new Item(560, 200), new Item(557, 1000)};
        if (player.inventory().hasCapacityFor(vengeance_runes)) {
            MagicSpellbook.changeSpellbook(player, MagicSpellbook.LUNAR,false);
            player.inventory().addAll(vengeance_runes);
        }
        player.message("You spawn some vengeance casts.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
