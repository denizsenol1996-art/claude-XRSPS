package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Color;

public class FoodCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {

        double totalAmountPaid = player.getAttribOr(AttributeKey.TOTAL_PAYMENT_AMOUNT, 0D);//you said 100$ amount right? yeah

      /*  if(player.ironMode() != IronMode.NONE) {
            player.message("As an ironman you cannot use this command.");
            return;
        }*/

        if (totalAmountPaid < 10 && player.ironMode() == IronMode.NONE) {//update19
            player.message(Color.RED.wrap("This command is only for Ruby+ and IronMans."));
            return;

        }


        if (!player.tile().inSafeZone() && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can only use this command at safe zones.");
            return;
        }

        if (WildernessArea.inWilderness(player.tile())) {
            player.message("You can only use this command at safe zones.");
            return;
        }

        if (totalAmountPaid > 99) {
            player.inventory().add(13441, 28);
        } else {
            player.inventory().add(385, 28);
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
