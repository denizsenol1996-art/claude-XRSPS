package com.twisted.game.content.areas.home;

import com.twisted.game.content.areas.home.dialogue.FairyFixitD;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.NpcIdentifiers.FAIRY_FIXIT_7333;

/**
 * @author Patrick van Elderen | April, 23, 2021, 13:52
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class FairyFixit extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == FAIRY_FIXIT_7333) {
                player.getDialogueManager().start(new FairyFixitD());
                return true;
            }
        }
        return false;
    }
}
