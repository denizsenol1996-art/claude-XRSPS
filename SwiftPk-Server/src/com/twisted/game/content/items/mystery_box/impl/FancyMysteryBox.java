package com.twisted.game.content.items.mystery_box.impl;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | March, 24, 2021, 13:28
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class FancyMysteryBox extends PacketInteraction {

    public static final int FANCY_MYSTERY_BOX = 16457;

    private final List<Integer> rewards = Arrays.asList(FLIPPERS, MUDSKIPPER_HAT, RAINBOW_SCARF, SCYTHE, FLARED_TROUSERS, GNOME_SCARF, RAIN_BOW, HAM_JOINT, CAPE_OF_SKULLS, HEAVY_CASKET);

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == FANCY_MYSTERY_BOX) {
                player.inventory().remove(FANCY_MYSTERY_BOX);
                player.inventory().add(new Item(Utils.randomElement(rewards)));
                return true;
            }
        }
        return false;
    }
}
