package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date februari 09, 2020 19:36
 */
public class LoopGFX implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        final int startId = Integer.parseInt(parts[1]);
        final int range = Integer.parseInt(parts[2]);

        if (startId < 0 || range <= 0) {
            return;
        }
        TaskManager.submit(new Task("LoopGFX", 3) {
            int gfx = startId;

            @Override
            protected void execute() {
                player.graphic(gfx, 100, 0);
                player.forceChat(""+gfx);
                gfx++;
                if (gfx >= range) {
                    this.stop();
                }
            }
        });
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
