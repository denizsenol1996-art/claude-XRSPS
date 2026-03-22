package com.twisted.game.content.packet_actions.interactions.items;

import com.twisted.game.world.entity.mob.npc.pets.PetAI;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 24, 2020
 */
public class ItemActionFour {

    public static boolean click(Player player, Item item) {
        if(PetAI.onItemOption4(player, item)) {
            return true;
        }

        return false;
    }
}
