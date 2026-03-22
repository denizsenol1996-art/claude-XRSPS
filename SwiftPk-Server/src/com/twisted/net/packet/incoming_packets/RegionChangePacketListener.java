package com.twisted.net.packet.incoming_packets;

import com.twisted.game.content.minigames.impl.Barrows;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.region.RegionManager;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegionChangePacketListener implements PacketListener {
private static final Logger logger = LogManager.getLogger(RegionChangePacketListener.class);

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.isAllowRegionChangePacket()) {
            try {
                RegionManager.loadMapFiles(player.tile().getX(), player.tile().getY());
                player.getPacketSender().deleteRegionalSpawns();
                GroundItemHandler.updateRegionItems(player);
                Barrows.onRegionChange(player);
                ObjectManager.onRegionChange(player);
                player.farming().updateObjects();
                player.setAllowRegionChangePacket(false);
                player.afkTimer.reset();
            } catch (Exception e) {
                logger.error(player.toString() + " has encountered an error loading region " + player.tile().region() + " regionid " + player.tile().region());
                logger.catching(e);
            }
        }
    }
}
