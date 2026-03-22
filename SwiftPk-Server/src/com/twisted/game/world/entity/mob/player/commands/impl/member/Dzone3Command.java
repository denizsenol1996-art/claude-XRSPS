package com.twisted.game.world.entity.mob.player.commands.impl.member;

import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;

public class Dzone3Command implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        double totalAmountPaid = player.getAttribOr(AttributeKey.TOTAL_PAYMENT_AMOUNT, 0D);

        if (totalAmountPaid < 750 && !player.getPlayerRights().isAdminOrGreater(player)) {
            player.message("@red@You need 750 total donated to access this area.");
            return;
        }

        if (WildernessArea.inWilderness(player.tile()) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can't use this command in the wilderness.");
            return;
        }

        if (Teleports.canTeleport(player, true, TeleportType.GENERIC)) {
            Teleports.basicTeleport(player, new Tile(2918, 2793));
        }

    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
