package com.twisted.game.content.areas.dungeons.waterfall;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;

public class WaterfallEntrance extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == 2010) {//TODO get id
                player.teleport(new Tile(2575, 9861));
                return true;
            }
        }
        return false;
    }
}
