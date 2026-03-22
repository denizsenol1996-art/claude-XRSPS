package com.twisted.game.content.skill.impl.slayer.master.impl;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.NpcIdentifiers.THORODIN_5526;

/**
 * @author Patrick van Elderen | April, 24, 2021, 13:46
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class SlayerMaster extends PacketInteraction {

    @Override
    public boolean handleNpcInteraction(Player player, Npc npc, int option) {
        if(option == 1) {
            if (npc.id() == THORODIN_5526) {
                npc.face(player.tile());
                player.getDialogueManager().start(new SlayerMasterDialogue());
                return true;
            }
        }
        if(option == 2) {
            if (npc.id() == THORODIN_5526) {
                World.getWorld().shop(14).open(player);
                return true;
            }
        }
        if(option == 3) {
            if (npc.id() == THORODIN_5526) {
                player.getSlayerRewards().open();
                return true;
            }
        }
        return false;
    }
}
