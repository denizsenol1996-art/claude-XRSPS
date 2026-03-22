package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.world.InterfaceConstants;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.PlayerStatus;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

/**
 * This packet listener is called when an item is dragged onto another slot.
 *
 * @author relex lawl
 */

public class SwitchItemSlotPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int interfaceId = packet.readInt();
        final int inserting = packet.readByteC();
        final int fromSlot = packet.readLEShortA();
        final int toSlot = packet.readLEShort();

        if (player == null || player.dead()) {
            return;
        }

        player.afkTimer.reset();

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if (player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        if (player.getStatus() == PlayerStatus.TRADING || player.getStatus() == PlayerStatus.DUELING || player.getStatus() == PlayerStatus.GAMBLING) {
            return;
        }

        switch (interfaceId) {
            case 3214, InterfaceConstants.INVENTORY_STORE -> player.inventory().swap(fromSlot, toSlot);
            case InterfaceConstants.WITHDRAW_BANK -> {
                GroupIronman group = GroupIronman.getGroup(player.getUID());
                if (group == null) player.getBank().moveItem(inserting, fromSlot, toSlot);
                else group.getGroupBank().moveItem(inserting, fromSlot, toSlot);
            }
        }
    }
}
