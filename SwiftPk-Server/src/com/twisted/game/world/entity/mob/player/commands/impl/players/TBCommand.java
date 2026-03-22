package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.MagicSpellbook;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | January, 26, 2021, 19:11
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class TBCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(player.ironMode() != IronMode.NONE && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("As an ironman you cannot use this command.");
            return;
        }

        if (!player.tile().inSafeZone() && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can only use this command at safe zones.");
            return;
        }
        Item[] runes = {new Item(LAW_RUNE, 100), new Item(CHAOS_RUNE, 100), new Item(DEATH_RUNE, 100)};
        if (player.inventory().hasCapacityFor(runes)) {
            MagicSpellbook.changeSpellbook(player, MagicSpellbook.NORMAL,false);
            player.message("You spawn 100 tb casts.");
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
