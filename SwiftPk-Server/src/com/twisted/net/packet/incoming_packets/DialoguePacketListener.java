package com.twisted.net.packet.incoming_packets;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.ClientToServerPackets;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

/**
 * Represents a packet used for handling dialogues.
 * This specific packet currently handles the action
 * for clicking the "next" option during a dialogue_old.
 * 
 * @author Professor Oak
 */

public class DialoguePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

        if (player == null || player.dead()) {
            return;
        }
        player.afkTimer.reset();

        if (packet.getOpcode() == ClientToServerPackets.DIALOGUE_OPCODE) {
            if (player.getDialogueManager().isActive()) {
                if (player.getDialogueManager().next()) {
                    return;
                }
            }
        }
    }
}
