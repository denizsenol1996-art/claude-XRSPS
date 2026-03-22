package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.content.tasks.impl.Tasks;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

import static com.twisted.game.world.entity.AttributeKey.TASK;

/**
 * @author Patrick van Elderen | November, 15, 2020, 15:36
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class TaskCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (parts.length != 2) {
            player.message("usage ::task pvptask/skillingtask/pvmtask/reset");
            return;
        }
        String cmd = parts[1];
        if(cmd.equalsIgnoreCase("pvptask")) {
            player.getTaskMasterManager().giveTask(true, false, false);
        } else if(cmd.equalsIgnoreCase("skillingtask")) {
            player.getTaskMasterManager().giveTask(false,true,false);
        } else if(cmd.equalsIgnoreCase("pvmtask")) {
            player.getTaskMasterManager().giveTask(false,false,true);
        } else if(cmd.equalsIgnoreCase("reset")) {
            player.getTaskMasterManager().resetTask();
        } else if(cmd.equalsIgnoreCase("finish")) {
            Tasks task = player.getAttribOr(TASK, Tasks.NONE);
            if(task != null) {
                player.getTaskMasterManager().increase(task, task.getTaskAmount());
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
