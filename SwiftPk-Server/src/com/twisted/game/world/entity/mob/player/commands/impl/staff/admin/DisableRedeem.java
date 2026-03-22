package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.entity.mob.player.commands.impl.players.RedeemCommand;

/**
 * @author Patrick van Elderen | June, 15, 2021, 20:34
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class DisableRedeem implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        RedeemCommand.ENABLED =! RedeemCommand.ENABLED;
        String msg = RedeemCommand.ENABLED ? "Enabled" : "Disabled";
        player.message("The redeem command is now "+msg+".");
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
