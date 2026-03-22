package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Color;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

public class ConvertTokensCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (player.getInventory().contains(ItemIdentifiers.PLATINUM_TOKEN)) {
            final int amount = player.getInventory().count(ItemIdentifiers.PLATINUM_TOKEN);
            player.getInventory().remove(ItemIdentifiers.PLATINUM_TOKEN, amount);
            player.getInventory().add(CustomItemIdentifiers.SUMMER_TOKEN, amount);
            player.message("<shad=0>" + Color.GREEN.wrap("Successfully converted " + amount + " platinum tokens to summer tokens."));
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
