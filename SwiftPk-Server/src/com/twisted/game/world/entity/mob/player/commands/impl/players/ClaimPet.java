package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.npc.pets.PetAI;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

/**
 * @author Patrick van Elderen | November, 09, 2020, 10:17
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class ClaimPet implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        PetAI.pickup(player);
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
