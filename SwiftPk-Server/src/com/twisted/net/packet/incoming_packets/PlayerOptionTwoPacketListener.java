package com.twisted.net.packet.incoming_packets;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.route.routes.TargetRoute;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

import java.lang.ref.WeakReference;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class PlayerOptionTwoPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        player.stopActions(false);
        int index = packet.readShort() & 0xFFFF;
        if (index > World.getWorld().getPlayers().capacity())
            return;

        Player other = World.getWorld().getPlayers().get(index);
        if (other == null) {
            player.message("Unable to find player.");
        } else {
            player.debugMessage(String.format("Click 2 pid=%d", other.getIndex()));

            if (player.locked() || player.dead()) {
                return;
            }
            player.setEntityInteraction(other);
            if (!other.dead()) {
                player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(other));
                player.putAttrib(AttributeKey.INTERACTION_OPTION, 2);
                TargetRoute.set(player, other, () -> {
                    player.runFn(1, () -> {
                        player.face(other.tile());
                        player.setEntityInteraction(null);
                    });
                    if (player.getMovementQueue().isFollowing(other)) {
                        player.getMovementQueue().resetFollowing();
                        player.setEntityInteraction(null);
                    }
                    if (player.getController() != null) {
                        player.getController().onPlayerRightClick(player, other, 2);
                    }
                });
            }
        }
    }
}
