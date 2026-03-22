package com.twisted.game.world.entity.mob.player.commands;

import com.twisted.game.world.entity.mob.player.Player;

import static com.twisted.game.content.daily_tasks.DailyTaskUtility.DAILY_TASK_MANAGER_INTERFACE;

/**
 * @author Ynneh | 01/04/2022 - 11:34
 * <https://github.com/drhenny>
 */
public class OpenDailyTaskCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getInterfaceManager().open(DAILY_TASK_MANAGER_INTERFACE);
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
