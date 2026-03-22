package com.twisted.game.content.packet_actions.interactions.items;

import com.twisted.game.content.items.RockCake;
import com.twisted.game.content.items.teleport.ArdyCape;
import com.twisted.game.content.skill.impl.slayer.content.SlayerRing;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteractionManager;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 04, 2020
 */
public class ItemActionThree {

    public static void click(Player player, Item item) {
        int id = item.getId();

        if (PacketInteractionManager.checkItemInteraction(player, item, 3)) {
            return;
        }

        ArdyCape.onItemOption3(player, item);

        if(player.getRunePouch().quickFill(item.getId())) {
            return;
        }

        if(SlayerRing.onItemOption3(player, item)) {
            return;
        }

        if(RockCake.onItemOption3(player, item)) {
            return;
        }

        switch (id) {
            case SACK_OF_PRESENTS -> player.getSackOfPresents().clear();
            case LOOTING_BAG, LOOTING_BAG_22586 -> player.getLootingBag().depositWidget();
        }
    }
}
