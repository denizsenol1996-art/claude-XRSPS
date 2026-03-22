package com.twisted.game.content.areas.burthope.dialogue;

import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.NpcIdentifiers;

/**
 * @author PVE
 * @Since juli 19, 2020
 */
public class Sergeant extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        //Sergeants
        if (npc.id() == NpcIdentifiers.SERGEANT || npc.id() == NpcIdentifiers.SERGEANT_4085) {
            player.message("The Sergeant is busy training the soldiers.");
            return true;
        }
        return false;
    }
}
