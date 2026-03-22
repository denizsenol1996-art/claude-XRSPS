package com.twisted.game.content.packet_actions.interactions.objects;

import com.twisted.game.content.skill.impl.hunter.Hunter;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import org.apache.commons.lang.ArrayUtils;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date februari 21, 2020 21:17
 */
public class HunterObjects extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        final int[] HUNTER_OBJECTS = new int[]{9344, 9345, 9373, 9377, 9379, 9375, 9380, 9348, 9385, 9382};
        if (ArrayUtils.contains(HUNTER_OBJECTS, obj.getId())) {
            if (Hunter.pickup(player, obj)) {
                return true;
            }
            if (Hunter.claim(player, obj)) {
                return true;
            }
        }
        return false;
    }
}
