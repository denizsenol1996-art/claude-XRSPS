package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.items.Item;

/**
 * @author PVE
 * @Since september 13, 2020
 */
public class PetsCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        Pet[] pets = Pet.values();
        for (Pet pet : pets) {
            player.inventory().addOrBank(new Item(pet.item));
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }
}
