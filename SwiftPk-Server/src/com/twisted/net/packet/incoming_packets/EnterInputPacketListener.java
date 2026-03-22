package com.twisted.net.packet.incoming_packets;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.ClientToServerPackets;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This packet manages the input taken from chat box interfaces that allow input,
 * such as withdraw x, bank x, enter name of friend, etc.
 *
 * @author Gabriel Hannason
 */

public class EnterInputPacketListener implements PacketListener {

    private static final Logger log = LoggerFactory.getLogger(EnterInputPacketListener.class);

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player == null || player.dead())
            return;

        player.afkTimer.reset();

        if (packet.getOpcode() == ClientToServerPackets.ENTER_SYNTAX_OPCODE) {
            String name = packet.readString();
            if (player.getEnterSyntax() != null) {
                player.getEnterSyntax().handleSyntax(player, name);
            } else {
                //log.trace("Received string input with no input handler set.");
            }
        } else if (packet.getOpcode() == ClientToServerPackets.ENTER_AMOUNT_OPCODE) {
            final long value = packet.readLong();
            final var dialogueStateId = packet.readByte();
            if (value <= 0) {
                return;
            }
            if (dialogueStateId != -1) {
                // Drag settings
                if (dialogueStateId == 3) {
                    player.getPacketSender().sendString(12697, "Drag setting: <col=ffffff>"+value + (value == 5 ? "(OSRS)" : value == 10 ? " (Pre-EOC)" : " (Custom)"));
                }
            } else if (player.getEnterSyntax() != null) {
                player.getEnterSyntax().handleSyntax(player, value);
            }
        } else {
            throw new AssertionError("Unhandled opcode: " + packet.getOpcode());
        }
    }
}
