package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.npc.pets.dialogue.NifflerD;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

/**
 * @author Patrick van Elderen | January, 11, 2021, 18:29
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class BankNifflerCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(player.nifflerPetOut()) {
            player.getDialogueManager().start(new NifflerD());
        } else {
            player.message("You do not have a Niffler pet out.");
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
