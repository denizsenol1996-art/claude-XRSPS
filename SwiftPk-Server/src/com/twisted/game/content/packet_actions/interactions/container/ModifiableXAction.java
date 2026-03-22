package com.twisted.game.content.packet_actions.interactions.container;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;
import com.twisted.game.world.items.Item;

import static com.twisted.game.world.InterfaceConstants.WITHDRAW_BANK;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class ModifiableXAction {

    public static void modifiableXAction(Player player, int slot, int interfaceId, int id, int amount) {
        if (interfaceId == WITHDRAW_BANK) {
            final Item item = player.getBank().get(slot);

            if (item == null || item.getId() != id) {
                return;
            }

            GroupIronman group = GroupIronman.getGroup(player.getUID());
            if (group == null) {
                player.getBank().withdraw(id, slot, amount);
            } else {
                group.getGroupBank().withdraw(id, slot, amount);
                group.update();
            }
        }
    }
}
