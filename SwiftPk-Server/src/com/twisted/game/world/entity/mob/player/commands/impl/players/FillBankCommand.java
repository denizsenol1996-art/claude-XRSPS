package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.GameServer;
import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

import static com.twisted.game.GameConstants.BANK_ITEMS;
import static com.twisted.game.GameConstants.TAB_AMOUNT;

/**
 * @author PVE
 * @Since september 13, 2020
 */
public class FillBankCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (!GameServer.properties().pvpMode && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            return;
        }
        if (player.ironMode() != IronMode.NONE) {
            player.message("As an ironman you cannot use this command.");
            return;
        }
        player.getBank().addAll(BANK_ITEMS);
        System.arraycopy(TAB_AMOUNT,0, player.getBank().tabAmounts,0, TAB_AMOUNT.length);
        player.getBank().shift();
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
