package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.content.packet_actions.interactions.container.ThirdContainerAction;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class ThirdItemContainerActionPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int interfaceId = packet.readInt();
        int id = packet.readUnsignedShortA();
        int slot = packet.readUnsignedShortA();

        if (player == null || player.dead()) {
            return;
        }

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        player.debugMessage(String.format("third action, container: %d slot: %d id %d", interfaceId, slot, id));

        ThirdContainerAction.thirdAction(player, interfaceId, slot, id);
    }
}
