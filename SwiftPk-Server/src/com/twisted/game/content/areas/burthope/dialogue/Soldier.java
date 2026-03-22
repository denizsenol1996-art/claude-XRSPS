package com.twisted.game.content.areas.burthope.dialogue;

import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.NpcIdentifiers;

import java.util.Arrays;
import java.util.List;

/**
 * @author PVE
 * @Since juli 19, 2020
 */
public class Soldier extends PacketInteraction {

    private static final List<Integer> EATING_SOLDIERS = Arrays.asList(NpcIdentifiers.SOLDIER_4089, NpcIdentifiers.SOLDIER_4090, NpcIdentifiers.SOLDIER_4091);

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        //Soldiers training
        if(npc.id() == NpcIdentifiers.SOLDIER || npc.id() == NpcIdentifiers.SOLDIER_4087) {
            player.message("The soldier is busy training.");
            return true;
        }

        //Soldiers eating
        for (int SOLDIERS : EATING_SOLDIERS) {
            if(npc.id() == SOLDIERS) {
                player.message("The soldier is busy eating.");
                return true;
            }
        }
        return false;
    }
}
