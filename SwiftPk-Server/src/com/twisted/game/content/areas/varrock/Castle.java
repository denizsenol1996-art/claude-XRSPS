package com.twisted.game.content.areas.varrock;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.ObjectIdentifiers;
import com.twisted.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | April, 14, 2021, 19:18
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Castle extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if (obj.getId() == ObjectIdentifiers.STAIRCASE_11807) {
                player.lock();
                Chain.bound(player).name("CastleTask1").runFn(1, () -> {
                    player.teleport(new Tile(player.tile().x, 3476, 1));
                    player.unlock();
                });
                return true;
            }
            if (obj.getId() == ObjectIdentifiers.STAIRCASE_11799) {
                player.lock();
                Chain.bound(player).name("CastleTask2").runFn(1, () -> {
                    player.teleport(new Tile(player.tile().x, 3472, 0));
                    player.unlock();
                });
                return true;
            }
        }
        return false;
    }
}
