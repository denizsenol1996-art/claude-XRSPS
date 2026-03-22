package com.twisted.net.packet.incoming_packets;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class NpcActionThreePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int index = packet.readShort();
        Npc npc = World.getWorld().getNpcs().get(index);

        NpcActionOnePacketListener.handleNpcClicks(player, npc, 3);
    }
}
