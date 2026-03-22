package com.twisted.game.content.areas.dungeons;

import com.twisted.game.content.packet_actions.interactions.objects.Ladders;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.ObjectIdentifiers.KINGS_LADDER;
import static com.twisted.util.ObjectIdentifiers.KINGS_LADDER_10230;

public class DagannothLair extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            //Climb the ladder down into the boss room.
            if(obj.getId() == KINGS_LADDER_10230) {
                Ladders.ladderDown(player, new Tile(2900, 4449),true);
                return true;
            }

            //Climb the ladder to get out of the boss room.
            if(obj.getId() == KINGS_LADDER) {
                Ladders.ladderUp(player, new Tile(1910, 4367),true);
                return true;
            }
        }
        return false;
    }
}
