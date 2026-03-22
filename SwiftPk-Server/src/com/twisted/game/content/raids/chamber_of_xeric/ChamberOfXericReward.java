package com.twisted.game.content.raids.chamber_of_xeric;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.npc.pets.PetAI;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.Color;
import com.twisted.util.Utils;

import static com.twisted.game.content.collection_logs.CollectionLog.RAIDS_KEY;
import static com.twisted.game.content.collection_logs.data.LogType.OTHER;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | May, 13, 2021, 12:29
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ChamberOfXericReward {
    public static boolean raidTask;

    public static void unlockOlmlet(Player player) {
        OTHER.log(player, RAIDS_KEY, new Item(Pet.OLMLET.item));
        if (!PetAI.hasUnlocked(player, Pet.OLMLET)) {
            // Unlock the varbit. Just do it, rather safe than sorry.
            player.addUnlockedPet(Pet.OLMLET.varbit);

            // RS tries to add it as follower first. That only works if you don't have one.
            var currentPet = player.pet();
            if (currentPet == null) {
                player.message("You have a funny feeling like you're being followed.");
                PetAI.spawnPet(player, Pet.OLMLET, false);
            } else {
                // Sneak it into their inventory. If that fails, fuck you, no pet for you!
                if (player.inventory().add(new Item(Pet.OLMLET.item), true)) {
                    player.message("You feel something weird sneaking into your backpack.");
                } else {
                    player.message("Speak to Probita to claim your pet!");
                }
            }

            World.getWorld().sendWorldMessage("<img=1081>" + player.getUsername() + " has unlocked the pet: <col=" + Color.HOTPINK.getColorValue() + ">" + new Item(Pet.OLMLET.item).name() + "</col>.");
        } else {
            player.message("You have a funny feeling like you would have been followed...");
        }
    }

    public static void withdrawReward(Player player) {
        player.inventory().addOrBank(player.getRaidRewards().getItems());
        for (Item item : player.getRaidRewards().getItems()) {
            if (item == null)
                continue;
            if (ChamberLootTable.uniqueTable.allItems().stream().anyMatch(i -> item.matchesId(item.getId()))) {
                Utils.sendDiscordInfoLog(player, "Rare drop collected: " + player.getUsername() + " withdrew " + item.unnote().name() + " ", "raids");
                if (item.getValue() > 50_000) {
                    String worldMessage = "<img=1081>[<col=" + Color.RAID_PURPLE.getColorValue() + ">Chambers of Xerics</col>]</shad></col>: " + Color.BLUE.wrap(player.getUsername()) + " received " + Utils.getAOrAn(item.unnote().name()) + " <shad=0><col=AD800F>" + item.unnote().name() + "</shad>!";
                    World.getWorld().sendWorldMessage(worldMessage);
                }
            }
        }

        player.getRaidRewards().clear();

        //Roll for pet
        if (World.getWorld().rollDie(650, 1)) {
            unlockOlmlet(player);
        }
    }

    public static void displayRewards(Player player) { // shows
        int totalRewards = player.getRaidRewards().getItems().length;

        // clear slots
        player.getPacketSender().sendItemOnInterfaceSlot(12022, null, 0);
        player.getPacketSender().sendItemOnInterfaceSlot(12023, null, 0);
        player.getPacketSender().sendItemOnInterfaceSlot(12024, null, 0);

        player.getInterfaceManager().open(12020);

        if (totalRewards >= 1) {
            Item reward1 = player.getRaidRewards().getItems()[0];
            player.getPacketSender().sendItemOnInterfaceSlot(12022, reward1, 0);
        }

        if (totalRewards >= 2) {
            Item reward2 = player.getRaidRewards().getItems()[1];
            player.getPacketSender().sendItemOnInterfaceSlot(12023, reward2, 0);
        }

        player.getPacketSender().sendItemOnInterfaceSlot(12024, new Item(DARK_JOURNAL), 0);
    }

    private static int doubleChestRoll(Player player) {
        int chance;
        chance = switch (player.getMemberRights()) {
            case NONE, SAPPHIRE_MEMBER, RUBY_MEMBER -> 0;
            case EMERALD_MEMBER -> 1;
            case DIAMOND_MEMBER -> 2;
            case DRAGONSTONE_MEMBER -> 3;
            case ONYX_MEMBER -> 4;
            case ZENYTE_MEMBER -> 5;
        };
        return chance;
    }

    private static final int POINTS_PER_CHANCE = 8676;
    private static final double UNIQUE_LOOT_CHANCE = 0.01;

    public static void giveRewards(Player player) {
        int personalPoints = player.getAttribOr(AttributeKey.PERSONAL_POINTS, 0);
        int points_cap = 150_000;

        int chanceAttempts = personalPoints / POINTS_PER_CHANCE;

        Item rollRegular = ChamberLootTable.rollRegular();

        Item rollUnique = null;
        for (int i = 0; i < chanceAttempts; i++) {
            if (World.getWorld().random().nextDouble() < UNIQUE_LOOT_CHANCE) {
                rollUnique = ChamberLootTable.rollUnique();
                break;
            }
        }

        if (personalPoints <= 10_000) {
            player.message("You need at least 10k points to get a drop from Raids.");
            return;
        }

        if (personalPoints > points_cap) {
            personalPoints = points_cap;
        }

        if (rollUnique != null) {
            player.varps().varbit(1327, 3);
            player.getRaidRewards().add(rollUnique);
            OTHER.log(player, RAIDS_KEY, rollUnique);
            player.varps().varbit(5456, 2);
            Utils.sendDiscordInfoLog(player,"Rare drop: " + player.getUsername() + " Has just received " + rollUnique.unnote().name() + " from Chambers of Xeric! Party Points: " + Utils.formatNumber(personalPoints), "raids");
        } else {
            player.varps().varbit(1327, 1);
            player.varps().varbit(5456, 1);
            player.getRaidRewards().add(rollRegular);
            Utils.sendDiscordInfoLog(player,"Regular Drop: " + player.getUsername() + " Has just received " + rollRegular.unnote().name() + " from Chambers of Xeric! Personal Points: " + Utils.formatNumber(personalPoints), "raids");
        }
    }

}
