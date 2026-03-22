package com.twisted.game.content.seasonal_events.christmas;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.twisted.util.CustomItemIdentifiers.*;

public class WinterCasket extends PacketInteraction {

    private final List<Item> items = Arrays.asList(
        new Item(ICED_PARTYHAT),
        new Item(ICED_HWEEN_MASK),
        new Item(ICED_SANTA_HAT),
        new Item(UGLY_SANTA_HAT),
        new Item(SNOWY_SLED),
        new Item(FROST_WHIP),
        new Item(FROST_CLAWS),
        new Item(ARMADYL_FROSTSWORD),
        new Item(INFINITY_WINTER_BOOTS),
        new Item(FROST_IMBUED_CAPE),
        new Item(DRAGON_HUNTER_CROSSBOW_T),
        new Item(ELDER_ICE_MAUL)
    );

    private Item reward() {
        return Utils.randomElement(items);
    }

    private void openCasket(Player player) {
        if(player.inventory().contains(WINTER_ITEM_CASKET)) {
            player.inventory().remove(WINTER_ITEM_CASKET);
            Item reward = reward();
            player.inventory().add(reward);
            World.getWorld().sendWorldMessage("<img=505><shad=0>"+ Color.MEDRED.wrap("[News]:")+"</shad> "+Color.PURPLE.wrap(player.getUsername()) + " received 1x "+Color.HOTPINK.wrap(reward.unnote().name())+" from the Event Item Casket!");
        }
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == WINTER_ITEM_CASKET) {
                openCasket(player);
                return true;
            }
        }
        return false;
    }
}
