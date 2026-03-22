package com.twisted.net.packet.incoming_packets;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.MapObjects;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.twisted.net.packet.incoming_packets.ObjectClickOnePacketListener.pathToAndTrigger;
import static org.apache.logging.log4j.util.Unbox.box;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class ObjectClickFourPacketListener implements PacketListener {

    private static final Logger logger = LogManager.getLogger(ObjectClickFourPacketListener.class);

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int x = packet.readLEShortA();
        final int id = packet.readShortA();
        final int y = packet.readLEShortA();

        if (player == null || player.dead()) {
            return;
        }

        //Make sure we aren't doing something else..
        if (player.busy()) {
            return;
        }

        player.afkTimer.reset();

        Tile tile = new Tile(x, y, player.tile().getLevel());
        Optional<GameObject> object = MapObjects.get(id, tile);

        //Fix object not found for instances
        if (object.isEmpty() && tile.getLevel() > 3) {
            tile = new Tile(x, y, player.tile().getLevel() % 4);
            object = MapObjects.get(id, tile);
            player.debugMessage(String.format("found real mapobj %s from %s", object.orElse(null), player.tile().level));
        }

        player.debugMessage("click4 " + String.format("objectId=%d, x=%d, y=%d", id, x, y));

        //Make sure the object actually exists in the region...
        if (object.isEmpty()) {
            //logger.info("Object with id {} does not exist for player " + player.toString() + " !", box(id));
           // Utils.sendDiscordInfoLog("Object op1 with id " + id + " does not exist for player " + player.toString() + "!");
            return;
        }

        GameObject gameObject = object.get();

        if (gameObject.definition() == null) {
            logger.error("ObjectDefinition for object {} is null for player " + player.toString() + ".", box(id));
            return;
        }

        if (player.locked() || player.dead()) {
            return;
        }
        player.stopActions(false);
        player.putAttrib(AttributeKey.INTERACTION_OBJECT, gameObject);
        player.putAttrib(AttributeKey.INTERACTION_OPTION, 4);

        //Do actions...
        player.faceObj(gameObject);

        pathToAndTrigger(player, gameObject, tile, 4);
    }
}
