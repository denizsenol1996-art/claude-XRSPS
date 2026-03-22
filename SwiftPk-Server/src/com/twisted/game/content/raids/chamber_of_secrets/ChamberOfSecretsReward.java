package com.twisted.game.content.raids.chamber_of_secrets;

import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
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

import static com.twisted.game.content.collection_logs.CollectionLog.RAIDS_KEY;
import static com.twisted.game.content.collection_logs.data.LogType.BOSSES;
import static com.twisted.game.content.collection_logs.data.LogType.OTHER;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.ELIDINIS_WARD;
import static com.twisted.util.ItemIdentifiers.OSMUMTENS_FANG;

/**
 * @author Patrick van Elderen | May, 13, 2021, 12:29
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ChamberOfSecretsReward {

    public static void unlockNagini(Player player) {
        BOSSES.log(player, RAIDS_KEY, new Item(Pet.NAGINI.item));

        // RS tries to add it as follower first. That only works if you don't have one.
        var currentPet = player.pet();
        if (currentPet == null) {
            player.message("You have a funny feeling like you're being followed.");
            PetAI.spawnPet(player, Pet.NAGINI, false);
        } else {
            // Sneak it into their inventory. If that fails, fuck you, no pet for you!
            if (player.inventory().add(new Item(Pet.NAGINI.item), true)) {
                player.message("You feel something weird sneaking into your backpack.");
            }
        }

        World.getWorld().sendWorldMessage("<img=1081>" + player.getUsername() + " has unlocked the pet: <col=" + Color.HOTPINK.getColorValue() + ">" + new Item(Pet.NAGINI.item).name() + "</col>.");
    }

    public static void withdrawReward(Player player) {
        player.inventory().addOrBank(player.getRaidRewards().getItems());
        for (Item item : player.getRaidRewards().getItems()) {
            if (item == null)
                continue;
            if (ChamberOfSecretsReward.uniqueTable.allItems().stream().anyMatch(i -> item.matchesId(item.getId()))) {
                Utils.sendDiscordInfoLog(player, "Rare drop collected: " + player.getUsername() + " withdrew " + item.unnote().name() + " ", "raids");
                if (item.getValue() > 50_000) {
                    String worldMessage = "<img=1081>[<col=" + Color.RAID_PURPLE.getColorValue() + ">Chambers of Secrets</col>]</shad></col>: " + Color.BLUE.wrap(player.getUsername()) + " received " + Utils.getAOrAn(item.unnote().name()) + " <shad=0><col=AD800F>" + item.unnote().name() + "</shad>!";
                    World.getWorld().sendWorldMessage(worldMessage);
                }
            }
        }

        player.getRaidRewards().clear();

        //Roll for pet
        if (World.getWorld().rollDie(650, 1)) {
            unlockNagini(player);
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

    private static final LootTable uniqueTable = new LootTable()
        .addTable(1,
            new LootItem(LEGENDARY_MYSTERY_BOX, 1, 9),
            new LootItem(KEY_OF_BOXES, 1, 8),
            new LootItem(SWORD_OF_GRYFFINDOR, 1, 7),
            new LootItem( 25975, 1, 6),
            new LootItem(CLOAK_OF_INVISIBILITY, 1, 6),
            new LootItem(MARVOLO_GAUNTS_RING, 1, 5),
            new LootItem(ELIDINIS_WARD, 1, 4),
            new LootItem(OSMUMTENS_FANG, 1, 6),
            new LootItem(TOM_RIDDLE_DIARY, 1, 4),
            new LootItem(TALONHAWK_CROSSBOW, 1, 3),
            new LootItem(SALAZAR_SLYTHERINS_LOCKET, 1, 3),
            new LootItem( 25744, 1, 2),
            new LootItem(ELDER_WAND_HANDLE, 1, 1),
            new LootItem(ELDER_WAND_STICK, 1, 1)

        );

    private static final LootTable regularTable = new LootTable()
        .addTable(1,
            new LootItem(ARMADYL_GODSWORD, 1, 1),
            new LootItem(ARMADYL_CROSSBOW, 1, 1),
            new LootItem(DONATOR_MYSTERY_BOX, 1, 1),
            new LootItem(WEAPON_MYSTERY_BOX, World.getWorld().random(1, 3), 3),
            new LootItem(BLOOD_MONEY, World.getWorld().random(15000, 35000), 6),
            //new LootItem(DRAGON_CROSSBOW, 1, 6),
            //new LootItem(DRAGON_THROWNAXE, World.getWorld().random(125, 250), 5),
            //new LootItem(DRAGON_KNIFE, World.getWorld().random(125, 250), 5),
            new LootItem(BANDOS_GODSWORD, 1, 4),
            new LootItem(ZAMORAK_GODSWORD, 1, 4),
            new LootItem(SARADOMIN_GODSWORD, 1, 4),
            new LootItem(ARMOUR_MYSTERY_BOX, World.getWorld().random(1, 7), 3),
            new LootItem(DRAGONFIRE_SHIELD, 1, 3),
            new LootItem(ABYSSAL_DAGGER_P_13271, 1, 5),
            new LootItem(IMBUEMENT_SCROLL, 5, 4),
            new LootItem(TORN_PRAYER_SCROLL, 1, 3)
        );

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

    public static void giveRewards(Player player) {
        //uniques
        int personalPoints = player.getAttribOr(AttributeKey.PERSONAL_POINTS, 0);
        var ticket = player.<Integer>getAttribOr(AttributeKey.TICKET_RUNS_CHALLENGE_COMPLETED, 0) + 1;

        if (personalPoints <= 10_000) {//Can't get any loot if below 10,000 points
            player.message("You need at least 10k points to get a drop from Raids.");
            return;
        }
        if(ticket >= 300){
            AchievementsManager.activate(player, Achievements.RAIDS_GRIENDER_I, 1);
            player.putAttrib(AttributeKey.TICKET_RUNS_CHALLENGE_COMPLETED,0);
            player.getInventory().addOrBank(new Item(20391));
            World.getWorld().sendWorldMessage("[@blu@<shad=2>Cos</col></shad>] "+player.getUsername()+" has just received X1 raid rare ticket from cos!");
        }
        if (personalPoints > 90_000) {
            personalPoints = 90_000;
        }

        double chance = (float) personalPoints / 100 / 100.0;
        //System.out.println(chance);
        Player rare = null;
        if (Utils.percentageChance((int) chance)) {
            int rolls = Utils.percentageChance(doubleChestRoll(player)) ? 2 : 1;
            if (rolls == 2)
                player.message(Color.PURPLE.wrap("You received an extra drop roll because of your member rank."));
            for (int i = 0; i < rolls; i++) {
                Item item = rollUnique();
                boolean added = player.getRaidRewards().add(item);
                OTHER.log(player, RAIDS_KEY, item);
                Utils.sendDiscordInfoLog(player, "Rare drop: " + player.getUsername() + " Has just received " + item.unnote().name() + " from Chambers of Secrets! Party Points: " + Utils.formatNumber(personalPoints) + " [debug: added=" + added + "]", "raids");
                rare = player;
            }
        }

        //Only give normal drops when you did not receive any rares.
        //regular drops
        if (player != rare) {
            int rolls = Utils.percentageChance(doubleChestRoll(player)) ? 2 : 1;
            if (rolls == 2)
                player.message(Color.PURPLE.wrap("You received an extra drop roll because of your member rank."));
            for (int i = 0; i < rolls; i++) {
                Item item = rollRegular();
                player.getRaidRewards().add(item);
                Utils.sendDiscordInfoLog(player, "Regular Drop: " + player.getUsername() + " Has just received " + item.unnote().name() + " from Chambers of Secrets! Personal Points: " + Utils.formatNumber(personalPoints), "raids");
            }
        }
    }

    private static Item rollRegular() {
        return regularTable.rollItem();
    }

    private static Item rollUnique() {
        return uniqueTable.rollItem();
    }
}
