package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.content.packet_actions.interactions.items.ItemOnItem;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

/**
 * @author PVE
 * @Since augustus 24, 2020
 */
public class ItemOnItemPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int toSlot = packet.readUnsignedShort();
        int fromSlot = packet.readUnsignedShortA();

        if (fromSlot < 0 || toSlot < 0 || fromSlot > 27 || toSlot > 27) {
            return;
        }
        Item fromItem = player.inventory().get(fromSlot);
        Item toItem = player.inventory().get(toSlot);

        if (fromItem == null || toItem == null) return;

        if (!player.locked() && !player.dead()) {
            player.stopActions(false);
            player.putAttrib(AttributeKey.ITEM_SLOT, fromSlot);
            player.putAttrib(AttributeKey.ALT_ITEM_SLOT, toSlot);
            player.putAttrib(AttributeKey.FROM_ITEM, fromItem);
            player.putAttrib(AttributeKey.TO_ITEM, toItem);
            player.putAttrib(AttributeKey.ITEM_ID, fromItem.getId());
            player.putAttrib(AttributeKey.ALT_ITEM_ID, toItem.getId());

            player.debugMessage(String.format("from=[%d] to=[%d] fromslot=[%d] toslot=[%d]", fromItem.getId(), toItem.getId(), fromSlot, toSlot));

            player.afkTimer.reset();

            // Block packet when the bank pin hasn't been entered yet
            if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
                player.getBankPin().openIfNot();
                return;
            }

            if(player.askForAccountPin()) {
                player.sendAccountPinMessage();
                return;
            }

            ItemOnItem.itemOnItem(player, fromItem, toItem);
        }
    }
}
