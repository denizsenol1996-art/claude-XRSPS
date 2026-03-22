package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.task.TaskManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskNamesCommand implements Command {

    private static final Logger logger = LogManager.getLogger(TaskNamesCommand.class);

    @Override
    public void execute(Player player, String command, String[] parts) {
        logger.info("Pending tasks: " + TaskManager.getPendingTaskNames());
        logger.info("Active tasks: " + TaskManager.getActiveTaskNames());
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
