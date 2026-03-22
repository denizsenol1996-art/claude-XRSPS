package com.twisted.game.content.items;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since November 02, 2021
 */
public class Dye extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == ORANGE_DYE || usedWith.getId() == ORANGE_DYE) && (use.getId() == TWISTED_BOW || usedWith.getId() == TWISTED_BOW)) {
            player.inventory().remove(new Item(ORANGE_DYE),true);
            player.inventory().remove(new Item(TWISTED_BOW),true);
            player.inventory().add(new Item(TWISTED_BOW_ORANGE),true);
            return true;
        }
        if ((use.getId() == PURPLE_DYE || usedWith.getId() == PURPLE_DYE) && (use.getId() == TWISTED_BOW || usedWith.getId() == TWISTED_BOW)) {
            player.inventory().remove(new Item(PURPLE_DYE),true);
            player.inventory().remove(new Item(TWISTED_BOW),true);
            player.inventory().add(new Item(TWISTED_BOW_PURPLE),true);
            return true;
        }
        if ((use.getId() == CLEANING_CLOTH || usedWith.getId() == CLEANING_CLOTH) && (use.getId() == TWISTED_BOW_ORANGE || usedWith.getId() == TWISTED_BOW_ORANGE)) {
            player.inventory().remove(new Item(TWISTED_BOW_ORANGE),true);
            player.inventory().add(new Item(TWISTED_BOW),true);
            return true;
        }
        if ((use.getId() == CLEANING_CLOTH || usedWith.getId() == CLEANING_CLOTH) && (use.getId() == TWISTED_BOW_PURPLE || usedWith.getId() == TWISTED_BOW_PURPLE)) {
            player.inventory().remove(new Item(TWISTED_BOW_PURPLE),true);
            player.inventory().add(new Item(TWISTED_BOW),true);
            return true;
        }
        return false;
    }
}
