package com.twisted.game.content.items;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Utils;

import static com.twisted.util.CustomItemIdentifiers.BIG_CHEST;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * Might as well add this just in case we ever go eco, took 2min to convert.
 * Can easy use for PvP too :).
 *
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 20, 2020
 */
public class Caskets extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 1) {
            if (item.getId() == CASKET) {
                openCasket(player, CASKET);
                return true;
            }
            if (item.getId() == CASKET_7956) {
                openCasket(player, CASKET_7956);
                return true;
            }
            if (item.getId() == BIG_CHEST) {
                openChest(player);
                return true;
            }
        }
        return false;
    }

    private void openCasket(Player player, int id) {
        if (!player.inventory().contains(id))
            return;

        var amount = 0;
        if (id == CASKET) {
            amount = World.getWorld().random(500, 2_500);
            var blood_reaper = player.hasPetOut("Blood Reaper pet");
            if(blood_reaper) {
                int extraBM = amount * 10 / 100;
                amount += extraBM;
            }
            player.inventory().remove(new Item(CASKET), true);
            player.inventory().add(new Item(BLOOD_MONEY, amount), true);
            player.message("You open the casket and find " + Utils.formatNumber(amount) + " blood money!");
        } else if (id == CASKET_7956) {
            amount = World.getWorld().random(2_500, 5_000);
            var blood_reaper = player.hasPetOut("Blood Reaper pet");
            if(blood_reaper) {
                int extraBM = amount * 10 / 100;
                amount += extraBM;
            }
            player.inventory().remove(new Item(CASKET_7956), true);
            player.inventory().add(new Item(BLOOD_MONEY, amount), true);
            player.message("You open the casket and find " + Utils.formatNumber(amount) + " blood money!");
        }
    }

    private void openChest(Player player) {
        if (!player.inventory().contains(BIG_CHEST))
            return;

        var amount = 10_000;
        var blood_reaper = player.hasPetOut("Blood Reaper pet");
        if(blood_reaper) {
            int extraBM = amount * 10 / 100;
            amount += extraBM;
        }
        player.inventory().remove(new Item(BIG_CHEST), true);
        player.inventory().add(new Item(BLOOD_MONEY, amount), true);
        player.message("You open the chest and find 10.000 blood money!");
    }

}
