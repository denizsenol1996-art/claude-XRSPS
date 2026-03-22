package com.twisted.game.content.packet_actions.interactions.equipment;

import com.twisted.game.content.items.teleport.ArdyCape;
import com.twisted.game.content.skill.impl.slayer.content.SlayerRing;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteractionManager;

public class EquipmentActions {

    public static boolean operate(Player player, int slot, Item item) {
        ArdyCape.onEquipmentOption(player, item, slot);

        if(PacketInteractionManager.onEquipmentAction(player, item, slot)) {
            return true;
        }

        if (SlayerRing.onEquipmentOption(player, item, slot)) {
            return true;
        }
        return false;
    }
}
