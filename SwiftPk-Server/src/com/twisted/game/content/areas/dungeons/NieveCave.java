package com.twisted.game.content.areas.dungeons;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.ObjectIdentifiers.CREVICE_535;

public class NieveCave extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == CREVICE_535) {
                player.teleport(2376, 9452);
                return true;
            } else  if(obj.getId() == 536) {
                player.teleport(2379, 9452);
                return true;
            }
        } else if(option == 2) {
            if(obj.getId() == CREVICE_535) {
                int count = 0;
                for (Player p : World.getWorld().getPlayers()) {
                    if (p != null && p.tile().inArea(2344, 9434, 2376, 9462))
                        count++;
                    String pluralOr = count == 1 ? "" : "s";
                    DialogueManager.sendStatement(player,"There are currently "+count+" player"+pluralOr+" in the cave.");
                }
                return true;
            }
        }
        return false;
    }
}
