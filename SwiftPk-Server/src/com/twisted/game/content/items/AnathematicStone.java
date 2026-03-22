package com.twisted.game.content.items;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.CustomItemIdentifiers.ANATHEMATIC_STONE;
import static com.twisted.util.CustomItemIdentifiers.ANATHEMATIC_WAND;
import static com.twisted.util.ItemIdentifiers.KODAI_WAND;

public class AnathematicStone extends PacketInteraction {
    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == ANATHEMATIC_STONE || usedWith.getId() == ANATHEMATIC_STONE) && (use.getId() == KODAI_WAND || usedWith.getId() == KODAI_WAND)) {
            player.optionsTitled("Would you like to combine the stone with your wand?", "Yes", "No", () -> {
                if (!player.inventory().containsAll(ANATHEMATIC_STONE, KODAI_WAND)) {
                    return;
                }
                player.inventory().remove(new Item(ANATHEMATIC_STONE), true);
                player.inventory().remove(new Item(KODAI_WAND), true);
                player.inventory().add(new Item(ANATHEMATIC_WAND), true);
            });
            return true;
        }
        return false;
    }
}
