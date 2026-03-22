package com.twisted.game.content.seasonal_events.christmas;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.NpcIdentifiers.SANTA_CLAUS;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 12, 2021
 */
public class SantaClaus extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == SANTA_CLAUS) {
                player.getDialogueManager().start(new SantaClausD());
                return true;
            }
        } else if(option == 2) {
            if(npc.id() == SANTA_CLAUS) {
                World.getWorld().shop(48).open(player);
                return true;
            }
        }
        return false;
    }
}
