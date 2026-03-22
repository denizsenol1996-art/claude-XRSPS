package com.twisted.game.content.areas.wilderness.content.revenant_caves;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.CustomItemIdentifiers;

import java.util.ArrayList;

import static com.twisted.util.ItemIdentifiers.BLOOD_MONEY;

/**
 * It can be given to the Emblem Trader wandering around in the Revenant Caves
 * in exchange for {@code rewardAmount} Coins. Much like the bracelet of
 * ethereum, the emblem is always lost on death, even if it is the only item in
 * the player's inventory.
 * 
 * @author Patrick van Elderen | Zerikoth (PVE) | 23 sep. 2019 : 14:33
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 * @version 1.0
 */
public enum AncientArtifacts {
    
    ANCIENT_EMBLEM(21807,2_000),
    ANCIENT_TOTEM(21810,5_000),
    ANCIENT_STATUETTE(21813,8_000),
    ANCIENT_MEDALLION(22299,14_000),
    ANCIENT_EFFIGY(22302,20_000),
    ANCIENT_RELIC(22305,30_000),
    DARK_ANCIENT_EMBLEM(CustomItemIdentifiers.DARK_ANCIENT_EMBLEM,4_000),
    DARK_ANCIENT_TOTEM(CustomItemIdentifiers.DARK_ANCIENT_TOTEM,10_000),
    DARK_ANCIENT_STATUETTE(CustomItemIdentifiers.DARK_ANCIENT_STATUETTE,16_000),
    DARK_ANCIENT_MEDALLION(CustomItemIdentifiers.DARK_ANCIENT_MEDALLION,28_000),
    DARK_ANCIENT_EFFIGY(CustomItemIdentifiers.DARK_ANCIENT_EFFIGY,40_000),
    DARK_ANCIENT_RELIC(CustomItemIdentifiers.DARK_ANCIENT_RELIC,60_000);
    
    /**
     * The emblem item
     */
    private final int itemId;
    
    /**
     * The amount of coins is being rewarded
     */
    private final int rewardAmount;
    
    /**
     * The {@code EmblemTrader} constructor
     * 
     * @param itemId       The emblem
     * @param rewardAmount the coins amount we receive
     */
    AncientArtifacts(int itemId, int rewardAmount) {
        this.itemId = itemId;
        this.rewardAmount = rewardAmount;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRewardAmount() {
        return rewardAmount;
    }
    
    /**
     * Exchanges the emblems for coins
     * 
     * @param player The player trying to sell his emblems
     * @param sell   perform the sale don't just price check
     */
    public static int exchange(Player player, boolean sell) {
        ArrayList<AncientArtifacts> list = new ArrayList<>();
        
        for (AncientArtifacts emblem : AncientArtifacts.values()) {
            if (player.inventory().contains(emblem.getItemId())) {
                list.add(emblem);
            }
        }

        if (list.isEmpty()) {
            return 0;
        }

        int value = 0;

        for (AncientArtifacts emblem : list) {
            int amount = player.inventory().count(emblem.getItemId());
            if (amount > 0) {
                if (sell) {
                    if(!player.inventory().contains(emblem.getItemId())) {
                        return 0;
                    }
                    player.inventory().remove(emblem.getItemId(), amount);
                    int increase = emblem.getRewardAmount() * amount;
                    var blood_reaper = player.hasPetOut("Blood Reaper pet");
                    if(blood_reaper) {
                        int extraBM = increase * 10 / 100;
                        increase += extraBM;
                    }
                    player.inventory().addOrDrop(new Item(BLOOD_MONEY, increase));
                }
                value += (emblem.getRewardAmount() * amount);
            }
        }
        return value;
    }
    
}
