package com.twisted.game.content.items.combine;

import com.twisted.game.content.duel.Dueling;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | April, 08, 2021, 16:35
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class NeitiznotFaceguard extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == BASILISK_JAW || usedWith.getId() == BASILISK_JAW) && (use.getId() == HELM_OF_NEITIZNOT || usedWith.getId() == HELM_OF_NEITIZNOT)) {
            if (Dueling.screen_closed(player) && player.inventory().containsAll(BASILISK_JAW, HELM_OF_NEITIZNOT)) {
                if(!player.inventory().containsAll(BASILISK_JAW, HELM_OF_NEITIZNOT)) {
                    return true;
                }
                player.inventory().remove(new Item(BASILISK_JAW),true);
                player.inventory().remove(new Item(HELM_OF_NEITIZNOT),true);
                player.inventory().add(new Item(NEITIZNOT_FACEGUARD),true);
            }
            return true;
        }
        return false;
    }

}
