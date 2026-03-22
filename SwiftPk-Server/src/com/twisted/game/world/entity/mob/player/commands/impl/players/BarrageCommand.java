package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.MagicSpellbook;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;

public class BarrageCommand implements Command {

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

        Item[] barrage_runes = {new Item(560, 400), new Item(565, 200), new Item(555, 600)};
        if (player.inventory().hasCapacityFor(barrage_runes)) {
            MagicSpellbook.changeSpellbook(player, MagicSpellbook.ANCIENT,false);
            player.inventory().addAll(barrage_runes);
        }
        player.message("You spawn some barrage casts.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
