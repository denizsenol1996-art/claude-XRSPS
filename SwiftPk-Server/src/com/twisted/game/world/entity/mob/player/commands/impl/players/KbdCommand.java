package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.content.areas.edgevile.dialogue.KbdCommandTeleport;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

/**
 * @author the plateau
 *
 *
 */
public class KbdCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getDialogueManager().start(new KbdCommandTeleport());
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
