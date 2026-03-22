package com.twisted.game.content.raids.theatre_of_blood;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.npc.pets.PetAI;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.loot.LootItem;
import com.twisted.game.world.items.loot.LootTable;
import com.twisted.util.Color;
import com.twisted.util.Utils;

//import static com.twisted.game.content.collection_logs.CollectionLog.TOB_RAIDS_KEY;
//import static com.twisted.game.content.collection_logs.LogType.BOSSES;
import static com.twisted.game.content.collection_logs.CollectionLog.TOB_RAIDS_KEY;
import static com.twisted.game.content.collection_logs.data.LogType.OTHER;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since January 07, 2022
 */
public class TheatreOfBloodRewards {

    public static void unlockLilZik(Player player) {
        OTHER.log(player, TOB_RAIDS_KEY, new Item(Pet.LIL_ZIK.item));
        if (!PetAI.hasUnlocked(player, Pet.LIL_ZIK)) {
            // Unlock the varbit. Just do it, rather safe than sorry.
            player.addUnlockedPet(Pet.LIL_ZIK.varbit);

            // RS tries to add it as follower first. That only works if you don't have one.
            var currentPet = player.pet();
            if (currentPet == null) {
                player.message("You have a funny feeling like you're being followed.");
                PetAI.spawnPet(player, Pet.LIL_ZIK, false);
            } else {
                // Sneak it into their inventory. If that fails, fuck you, no pet for you!
                if (player.inventory().add(new Item(Pet.LIL_ZIK.item), true)) {
                    player.message("You feel something weird sneaking into your backpack.");
                } else {
                    player.message("Speak to Probita to claim your pet!");
                }
            }

            Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " has received a: " + new Item(Pet.LIL_ZIK.item).name() + ".", "yell_item_drop");
            World.getWorld().sendWorldMessage("<img=452><shad=0>" + Color.RED.wrap(player.getUsername()) + " has unlocked the pet: <col=" + Color.HOTPINK.getColorValue() + ">" + new Item(Pet.LIL_ZIK.item).name() + "</col>.");
        } else {
            player.message("You have a funny feeling like you would have been followed...");
        }
    }

    public static boolean containsRare(Player partyMember) {
        for (Item item : partyMember.getRaidRewards().getItems()) {
            if (item == null)
                continue;
            if (TOBUniqueTable.allItems().stream().anyMatch(i -> i.matchesId(item.getId()))) {
                return true;
            }
        }
        return false;
    }

    public static void withdrawReward(Player player) {
        player.inventory().addOrBank(player.getRaidRewards().getItems());
        for (Item item : player.getRaidRewards().getItems()) {
            if (item == null)
                continue;
            if (TOBUniqueTable.allItems().stream().anyMatch(i -> i.matchesId(item.getId()))) {
                String worldMessage = "<img=452><shad=0>[<col=" + Color.RAID_PURPLE.getColorValue() + ">Theatre of blood</col>]</shad></col>: " + Color.BLUE.wrap(player.getUsername()) + " received " + Utils.getAOrAn(item.unnote().name()) + " <shad=0><col=AD800F>" + item.unnote().name() + "</shad>!";
                World.getWorld().sendWorldMessage(worldMessage);
                Utils.sendDiscordInfoLog(player, "Rare drop collected: (TOB)" + player.getUsername() + " withdrew " + item.unnote().name() + " ", "raids");
            }
        }
        player.getRaidRewards().clear();

        //Roll for pet
        if (World.getWorld().rollDie(650, 1)) {
            unlockLilZik(player);
        }
    }

    public static void displayRewards(Player player) { // shows
        int totalRewards = player.getRaidRewards().getItems().length;

        // clear slots
        player.getPacketSender().sendItemOnInterfaceSlot(12022, null, 0);
        player.getPacketSender().sendItemOnInterfaceSlot(12023, null, 0);
        player.getPacketSender().sendItemOnInterfaceSlot(12024, null, 0);

        player.getInterfaceManager().open(12220);

        if (totalRewards >= 1) {
            Item reward1 = player.getRaidRewards().getItems()[0];
            player.getPacketSender().sendItemOnInterfaceSlot(12222, reward1, 0);
        }

        if (totalRewards >= 2) {
            Item reward2 = player.getRaidRewards().getItems()[1];
            player.getPacketSender().sendItemOnInterfaceSlot(12223, reward2, 0);
        }

        if (totalRewards >= 3) {
            Item reward3 = player.getRaidRewards().getItems()[2];
            player.getPacketSender().sendItemOnInterfaceSlot(12224, reward3, 0);
        }
    }

    private static final LootTable TOBUniqueTable = new LootTable()
        .addTable(1,
            new LootItem(AVERNIC_DEFENDER, 1, 6),
            new LootItem(JUSTICIAR_FACEGUARD, 1, 5),
            new LootItem(JUSTICIAR_CHESTGUARD, 1, 4),
            new LootItem(JUSTICIAR_LEGGUARDS, 1, 4),
            new LootItem(SANGUINESTI_STAFF, 1, 3),
            new LootItem(GHRAZI_RAPIER, 1, 3),
            new LootItem(HOLY_ORNAMENT_KIT, 1, 2),
            new LootItem(SCYTHE_OF_VITUR, 1, 1),
            new LootItem(SANGUINE_DUST, 1, 1),
            new LootItem(SANGUINE_ORNAMENT_KIT, 1, 1)
        );

    private static final LootTable regularTable = new LootTable()
        .addTable(1,
            new LootItem(ARMADYL_GODSWORD, 1, 1),
            new LootItem(ARMADYL_CROSSBOW, 1, 1),
            new LootItem(DONATOR_MYSTERY_BOX, 1, 1),
            //   new LootItem(PKP_TICKET, World.getWorld().random(2000, 5000), 8),
            new LootItem(DRAGON_CROSSBOW, 1, 9),
            new LootItem(DRAGON_THROWNAXE, World.getWorld().random(25, 100), 9),
            new LootItem(DRAGON_KNIFE, World.getWorld().random(25, 100), 9),
            new LootItem(BANDOS_GODSWORD, 1, 7),
            new LootItem(ZAMORAK_GODSWORD, 1, 7),
            new LootItem(SARADOMIN_GODSWORD, 1, 7),
            new LootItem(ARMOUR_MYSTERY_BOX, 1, 6),
            new LootItem(WEAPON_MYSTERY_BOX, 1, 6),
            new LootItem(DRAGONFIRE_SHIELD, 1, 5),
            new LootItem(ABYSSAL_DAGGER_P_13271, 1, 5),
            new LootItem(IMBUEMENT_SCROLL, 5, 9),
            new LootItem(MOLTEN_GLASS, 125, 10), // molten glass
            new LootItem(560, 10000, 10), // death rune
            new LootItem(565, 10000, 10), // blood rune
            new LootItem(566, 10000, 10), // soul rune
            new LootItem(892, 2500, 10), // rune arrow
            new LootItem(11212, 1000, 10), // dragon arrow
            new LootItem(3050, 370, 10), // grimy toadflax
            new LootItem(208, 250, 10), // grimy ranarr weed
            new LootItem(210, 196, 10), // grimy irit
            new LootItem(212, 370, 10), // grimy avantoe
            new LootItem(214, 405, 10), // grimy kwuarm
            new LootItem(3052, 200, 10), // grimy snapdragon
            new LootItem(216, 400, 10), // grimy cadantine
            new LootItem(2486, 293, 10), // grimy lantadyme
            new LootItem(218, 212, 10), // grimy dwarf weed
            new LootItem(220, 856, 10), // grimy torstol
            new LootItem(443, 500, 10), // silver ore
            new LootItem(454, 1000, 10), // coal
            new LootItem(445, 1000, 10), // gold ore
            new LootItem(448, 500, 10), // mithril ore
            new LootItem(450, 350, 10), // adamantite ore
            new LootItem(452, 200, 10), // runite ore
            new LootItem(1624, 250, 10), // uncut sapphire
            new LootItem(1622, 225, 10), // uncut emerald
            new LootItem(1620, 200, 10), // uncut ruby
            new LootItem(1618, 175, 10), // uncut diamond
            new LootItem(7937, 10000, 10), // pure essence
            new LootItem(8781, 500, 10), // teak plank
            new LootItem(8783, 500, 10) // mahogany plank
        );

    public static void giveRewards(Player player) {
        //uniques
        int personalPoints = player.getAttribOr(AttributeKey.PERSONAL_POINTS, 0);
        if (personalPoints <= 10_000) {//Can't get any loot if below 10,000 points
            player.message("You need at least 10k points to get a drop from Raids.");
            return;
        }

        if (personalPoints > 180_000 && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            personalPoints = 180_000;
        }

        double chance = (float) personalPoints / 100 / 100.0;
        //System.out.println("chance: "+chance);
        Player rare = null;
        if (Utils.percentageChance((int) chance)) {
            Item item = rollUnique().copy();
            player.getRaidRewards().add(item);
            OTHER.log(player, TOB_RAIDS_KEY, item);
            rare = player;
        }

        //Only give normal drops when you did not receive any rares.
        //regular drops
        if (player != rare) {
            Item item1 = rollRegular().copy();
            Item item2 = rollRegular().copy();
            Item item3 = rollRegular().copy();
            player.getRaidRewards().add(item1);
            player.getRaidRewards().add(item2);
            player.getRaidRewards().add(item3);
            Utils.sendDiscordInfoLog(player, "Regular Drop: " + player.getUsername() + " Has just received " + item1.unnote().name() + ", " + item2.unnote().name() + " and " + item3.unnote().name() + " from Theatre of blood! Personal Points: " + Utils.formatNumber(personalPoints), "tob_reward");
        }
    }

    private static Item rollRegular() {
        return regularTable.rollItem();
    }

    private static Item rollUnique() {
        return TOBUniqueTable.rollItem();
    }
}
