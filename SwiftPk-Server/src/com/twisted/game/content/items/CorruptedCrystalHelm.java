package com.twisted.game.content.items;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.CustomItemIdentifiers.CORRUPTING_STONE;
import static com.twisted.util.CustomItemIdentifiers.CORRUPTED_CRYSTAL_HELM;
import static com.twisted.util.ItemIdentifiers.CRYSTAL_HELM;

public class CorruptedCrystalHelm extends PacketInteraction{
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == CORRUPTING_STONE || usedWith.getId() == CORRUPTING_STONE) && (use.getId() == CRYSTAL_HELM || usedWith.getId() == CRYSTAL_HELM)) {
            player.optionsTitled("Would you like to combine the stone with your helm?", "Yes", "No", () -> {
                if (!player.inventory().containsAll(CORRUPTING_STONE, CRYSTAL_HELM)) {
                    return;
                }
                player.inventory().remove(new Item(CORRUPTING_STONE), true);
                player.inventory().remove(new Item(CRYSTAL_HELM), true);
                player.inventory().add(new Item(CORRUPTED_CRYSTAL_HELM), true);
            });
            return true;
        }
        return false;
    }
}
