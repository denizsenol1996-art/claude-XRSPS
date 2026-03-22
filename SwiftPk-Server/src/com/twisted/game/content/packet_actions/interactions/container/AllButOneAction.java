package com.twisted.game.content.packet_actions.interactions.container;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;
import com.twisted.game.world.items.Item;

import static com.twisted.game.world.InterfaceConstants.WITHDRAW_BANK;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class AllButOneAction {

    public static void allButOne(Player player, int slot, int interfaceId, int id) {
        if (interfaceId == WITHDRAW_BANK) {
            Item item = player.getBank().get(slot);
            if (item == null || item.getId() != id || item.getAmount() <= 1) return;
            GroupIronman group = GroupIronman.getGroup(player.getUID());
            if (group == null) {
                player.getBank().withdraw(id, slot, item.getAmount() - 1);
            } else {
                group.getGroupBank().withdraw(id, slot, item.getAmount() - 1);
                group.update();
            }
        }
    }

}
