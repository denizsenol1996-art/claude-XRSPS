package com.twisted.game.content.areas.dungeons.gnome_stronghold;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.ObjectIdentifiers.*;

public class GnomeEntrance extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            if (obj.getId() == CAVE_26709) {
                player.lock();
                player.animate(2796);
                Chain.bound(null).runFn(2, () -> {
                    player.resetAnimation();
                    player.teleport(2429, 9824, 0);
                    player.unlock();
                });
                return true;
            }
            if (obj.getId() == TUNNEL_27257 || obj.getId() == TUNNEL_27258) {
                player.lock();
                player.animate(2796);
                Chain.bound(null).runFn(2, () -> {
                    player.resetAnimation();
                    player.teleport(2430, 3424, 0);
                    player.unlock();
                });
                return true;
            }
        }
        return false;
    }
}
