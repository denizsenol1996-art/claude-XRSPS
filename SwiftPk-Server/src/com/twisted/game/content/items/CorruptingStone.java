package com.twisted.game.content.items;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.CORRUPTED_YOUNGLLEF;
import static com.twisted.util.ItemIdentifiers.YOUNGLLEF;

public class CorruptingStone extends PacketInteraction {//update19

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == CORRUPTING_STONE || usedWith.getId() == CORRUPTING_STONE) && (use.getId() == YOUNGLLEF || usedWith.getId() == YOUNGLLEF)) {
            player.optionsTitled("Would you like to combine the stone with your pet?", "Yes", "No", () -> {
                if (!player.inventory().containsAll(CORRUPTING_STONE, YOUNGLLEF)) {
                    return;
                }
                player.inventory().remove(new Item(CORRUPTING_STONE), true);
                player.inventory().remove(new Item(YOUNGLLEF), true);
                player.inventory().add(new Item(CORRUPTED_YOUNGLLEF), true);
            });
            return true;
        }
        if ((use.getId() == CORRUPTING_STONE || usedWith.getId() == CORRUPTING_STONE) && (use.getId() == BOW_OF_FAERDHINEN || usedWith.getId() == BOW_OF_FAERDHINEN)) {
            player.optionsTitled("Would you like to combine the stone with your bow?", "Yes", "No", () -> {
                if (!player.inventory().containsAll(CORRUPTING_STONE, BOW_OF_FAERDHINEN)) {
                    return;
                }
                player.inventory().remove(new Item(CORRUPTING_STONE), true);
                player.inventory().remove(new Item(BOW_OF_FAERDHINEN), true);
                player.inventory().add(new Item(BOW_OF_FAERDHINEN_C), true);
            });
            return true;
        }
        return false;
    }
}

