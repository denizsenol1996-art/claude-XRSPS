package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.content.daily_tasks.DailyTaskButtons;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

/**
 * @author Patrick van Elderen | June, 23, 2021, 08:39
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class DisableDailyRewards implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        DailyTaskButtons.REWARDS_DISABLED =! DailyTaskButtons.REWARDS_DISABLED;
        String msg = DailyTaskButtons.REWARDS_DISABLED ? "Disabled" : "Enabled";
        player.message("The daily rewards are now "+msg+".");
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
