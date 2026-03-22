package com.twisted.game.content.skill.impl.thieving;

import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

public class CoinPouch extends PacketInteraction {
    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (item == null) return false;
        if (player.getInventory().contains(22521) && item.getId() == 22521) {
            addCoins(player, item, 100);
            return true;
        } else if (player.getInventory().contains(22522) && item.getId() == 22522) {
            addCoins(player, item, 200);
            return true;
        } else if (player.getInventory().contains(22523) && item.getId() == 22523) {
            addCoins(player, item, 300);
            return true;
        } else if (player.getInventory().contains(22524) && item.getId() == 22524) {
            addCoins(player, item, 400);
            return true;
        } else if (player.getInventory().contains(22525) && item.getId() == 22525) {
            addCoins(player, item, 500);
            return true;
        } else if (player.getInventory().contains(22526) && item.getId() == 22526) {
            addCoins(player, item, 600);
            return true;
        }
        return false;
    }

    private void addCoins(Player player, Item item, int max) {
        boolean blood_reaper = player.hasPetOut("Blood Reaper pet");
        var thievingBoostPerk = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.MORE_BM_THIEVING);
        for (int index = 0; index < item.getAmount(); index++) {
            player.getInventory().remove(item);
            int amount = random(15, max);
            int extraBM = amount * 10 / 100;
            int thievingBoost = amount *= 10.0 / 100;
            if (blood_reaper) amount += extraBM;
            if (thievingBoostPerk) amount += thievingBoost;
            player.getInventory().add(new Item(13307, amount));
        }
    }

    public int random(int min, int max) {
        return World.getWorld().random(min, max) * 10;
    }

}
