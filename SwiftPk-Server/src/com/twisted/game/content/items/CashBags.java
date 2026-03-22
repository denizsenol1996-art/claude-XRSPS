package com.twisted.game.content.items;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

/**
 * @author Patrick van Elderen | May, 26, 2021, 14:54
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class CashBags extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == 10834) {//100M cash bag
                if(player.inventory().contains(10834)) {
                    player.inventory().remove(10834,1);
                    player.inventory().addOrDrop(new Item(995, 100_000_000));
                }
                return true;
            }
            if(item.getId() == 10833) {//10M cash bag
                if(player.inventory().contains(10833)) {
                    player.inventory().remove(10833, 1);
                    player.inventory().addOrDrop(new Item(995, 10_000_000));
                }
                return true;
            }
        }
        return false;
    }
}
