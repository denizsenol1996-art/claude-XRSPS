package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.db.transactions.CollectPayments;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Color;

/**
 * @author Patrick van Elderen | May, 29, 2021, 19:47
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class RedeemCommand implements Command {

    public static boolean ENABLED = true;

    private long lastCommandUsed;

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(ENABLED) {
            if (System.currentTimeMillis() - lastCommandUsed >= 10000) {
                lastCommandUsed = System.currentTimeMillis();
                CollectPayments.INSTANCE.collectPayments(player);
            }
        } else {
            player.message(Color.RED.wrap("The ::redeem command has been temporarily disabled."));
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
