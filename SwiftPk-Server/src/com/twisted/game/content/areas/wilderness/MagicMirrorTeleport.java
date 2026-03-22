package com.twisted.game.content.areas.wilderness;

import com.twisted.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

public class MagicMirrorTeleport extends PacketInteraction {
    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            //Into the arena
            if (obj.getId() == 34683) {
                player.faceObj(obj);
                //Check to see if the player is teleblocked
                if (player.getTimerRepository().has(TimerKey.TELEBLOCK) || player.getTimerRepository().has(TimerKey.SPECIAL_TELEBLOCK)) {
                    player.teleblockMessage();
                    return true;
                }

                if (WildernessKeyPlugin.hasKey(player) && WildernessArea.inWilderness(player.tile())) {
                    player.message("You cannot teleport outside the Wilderness with the Wilderness key.");
                    return true;
                }

                player.lockDelayDamage();
                Chain.bound(null).runFn(1, () -> {
                    player.animate(2710);
                    player.message("You grab onto the mirror...");
                }).then(2, () -> {
                    player.animate(714);
                    player.graphic(111, 110, 0);
                }).then(4, () -> {
                    player.teleport(new Tile(3094, 3503, 0));
                    player.animate(-1);
                    player.unlock();
                    player.message("...and get taken by a magical force to home!");
                });
                return true;
            }
        }
        return false;
    }
}
