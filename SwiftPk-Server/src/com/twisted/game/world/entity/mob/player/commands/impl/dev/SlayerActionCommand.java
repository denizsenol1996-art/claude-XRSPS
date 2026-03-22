package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.content.skill.impl.slayer.Slayer;
import com.twisted.game.content.skill.impl.slayer.master.impl.SlayerMasterDialogue;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class SlayerActionCommand implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        String action = parts[1];

        if (action.equalsIgnoreCase("reset")) {
            Slayer.cancelTask(player, true);
            player.message("Your slayer task has been reset, talk to any slayer master for a new task.");
        } else if (action.equalsIgnoreCase("task")) {
            player.getDialogueManager().start(new SlayerMasterDialogue());
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
