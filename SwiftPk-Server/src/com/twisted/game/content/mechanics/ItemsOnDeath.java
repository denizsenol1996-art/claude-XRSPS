package com.twisted.game.content.mechanics;

import com.twisted.GameServer;
import com.twisted.fs.ItemDefinition;
import com.twisted.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.twisted.game.content.areas.wilderness.content.revenant_caves.AncientArtifacts;
import com.twisted.game.content.items_kept_on_death.ItemsKeptOnDeath;
import com.twisted.game.content.mechanics.break_items.BrokenItem;
import com.twisted.game.content.minigames.MinigameManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.bountyhunter.emblem.BountyHunterEmblem;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.player.GameMode;
import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.rights.PlayerRights;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.net.packet.incoming_packets.PickupItemPacketListener;
import com.twisted.test.unit.IKODTest;
import com.twisted.test.unit.PlayerDeathConvertResult;
import com.twisted.test.unit.PlayerDeathDropResult;
import com.twisted.util.Color;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.Utils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.twisted.util.CustomItemIdentifiers.SUMMER_AMULET;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | June, 27, 2021, 12:56
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ItemsOnDeath {

    private static final Logger playerDeathLogs = LogManager.getLogger("PlayerDeathsLogs");
    private static final Level PLAYER_DEATHS;

    static {
        PLAYER_DEATHS = Level.getLevel("PLAYER_DEATHS");
    }

    /**
     * The items the Player lost.
     */
    private static final List<Item> lostItems = new ArrayList<>();

    /**
     * If our account has the ability for the custom Pet Shout mechanic - where when you kill someone
     * your pet will shout something.
     */
    public static boolean hasShoutAbility(Player player) {
        // Are we a user with the mechanic enabled
        return player.getAttribOr(AttributeKey.PET_SHOUT_ABILITY, false);
    }

    public static boolean isNotInWildAndNotHardCode(Player player) {
        return !WildernessArea.inWilderness(player.tile()) && player.ironMode() != IronMode.HARDCORE;
    }

    /**
     * Calculates and drops all of the items from {@code player} for {@code killer}.
     *
     * @return
     */
    public static PlayerDeathDropResult droplootToKiller(Player player, Mob killer) {
        var in_tournament = player.inActiveTournament() || player.isInTournamentLobby();
        var donator_zone = player.tile().region() == 13462;
        var vorkath_area = player.tile().region() == 9023;
        var hydra_area = player.tile().region() == 5536;
        var zulrah_area = player.tile().region() == 9007 || player.tile().region() == 9008;
        var safe_accounts = player.getUsername().equalsIgnoreCase("Box test");
        var duel_arena = player.getDueling().inDuel() || player.getDueling().endingDuel();
        var pest_control = player.tile().region() == 10536;
        var raids_area = player.getRaids() != null && player.getRaids().raiding(player);
        var minigame_safe_death = player.getMinigame() != null && player.getMinigame().getType().equals(MinigameManager.ItemType.SAFE);
        var hunleff_area = player.tile().region() == 6810;

        if (player.mode() == GameMode.DARK_LORD && !raids_area) {
            stripDarkLordRank(player);
        }

        // If we're in FFA clan wars, don't drop our items.
        // Have these safe area checks before we do some expensive code ... looking for who killed us.
        if (donator_zone || vorkath_area || zulrah_area || hydra_area || safe_accounts || duel_arena || pest_control || raids_area || in_tournament || minigame_safe_death || hunleff_area) {
            playerDeathLogs.log(PLAYER_DEATHS, "Player: " + player.getUsername() + " died in a safe area " + (killer != null && killer.isPlayer() ? " to " + killer.toString() : ""));
            Utils.sendDiscordInfoLog(player, "Player: " + player.getUsername() + " died in a safe area " + (killer != null && killer.isPlayer() ? " to " + killer.toString() : ""), "playerdeaths");
            Utils.sendDiscordInfoLog(player,"Safe deaths activated for: " + player.getUsername() + "" + (killer != null && killer.isPlayer() ? " to " + killer.toString() : "" + " donator_zone: " + donator_zone + " vorkath_area: " + vorkath_area + " hydra_area: " + hydra_area + " zulrah_area: " + zulrah_area + " in safe_accounts: " + safe_accounts + " duel_arena: " + duel_arena + " pest_control: " + pest_control + " raids_area: " + raids_area + " in_tournament: " + in_tournament + " minigame_safe_death: " + minigame_safe_death + " hunleff_area: " + hunleff_area), "playerdeaths");
            return null;
        }

        // If it's not a safe death, turn a Hardcore Ironman into a regular.
        if (player.ironMode() == IronMode.HARDCORE) {
            stripHardcoreRank(player);
        }

        if (isNotInWildAndNotHardCode(player) || player.getPlayerRights().isOwner(player) || player.getPlayerRights().isYoutuber(player)) {
            return null;
        }

        Player theKiller = killer == null || killer.isNpc() ? player : killer.getAsPlayer();

        final Tile tile = player.tile();

        // Game Lists
        LinkedList<Item> toDrop = new LinkedList<>();
        List<Item> keep = new LinkedList<>();
        List<Item> toDropPre = new LinkedList<>();

        // Unit Testing Lists
        List<Item> outputDrop = new ArrayList<>(toDrop.size());
        List<Item> outputKept = new ArrayList<>(1);
        List<Item> outputDeleted = new ArrayList<>(0);
        List<PlayerDeathConvertResult> outputConverted = new ArrayList<>(0);

        player.getEquipment().forEach(toDropPre::add);
        player.inventory().forEach(item -> {
            if (!item.matchesId(30099) && !item.matchesId(30098) && !item.matchesId(LOOTING_BAG) && !item.matchesId(LOOTING_BAG_22586) && !item.matchesId(RUNE_POUCH)) { // always lost
                toDropPre.add(item);
            } else {
                outputDeleted.add(item); // looting bag goes into deleted
            }
        });

        if (player.getInventory().contains(30098) || player.getInventory().contains(30099) && !player.getSkullType().equals(SkullType.RED_SKULL)) {
            keep.add(new Item(30099));
        }
        player.getEquipment().clear(); // everything gets cleared no matter what
        player.inventory().clear();


        toDrop.addAll(toDropPre);

        // Any extra custom logic here for alwaysKept under special circumstances
        List<Item> keptPets = toDrop.stream().filter(i -> {
            Pet petByItem = Pet.getPetByItem(i.getId());
            if (petByItem == null)
                return false;
            boolean canTransfer = petByItem.varbit == -1;
            boolean loseByTransfer = player.getSkullType() != SkullType.NO_SKULL;
            return !(canTransfer && loseByTransfer); // this will be lost on death
        }).toList();

        keep.addAll(keptPets);
        for (Item keptPet : keptPets) {
            toDrop.remove(keptPet);
        }

        //System.out.println("Dropping: " + Arrays.toString(toDrop.toArray()));

        // remove always kept before calculating kept-3 by value
        List<Item> alwaysKept = toDrop.stream().filter(ItemsKeptOnDeath::alwaysKept).toList();
        IKODTest.debug("death alwaysKept list : " + Arrays.toString(alwaysKept.stream().map(Item::toShortValueString).toArray()));
        keep.addAll(alwaysKept);
        toDrop.removeIf(ItemsKeptOnDeath::alwaysKept);

        dropBloodMoneyForItems(toDrop, player, theKiller);

        // Sort remaining lost items by value.
        toDrop.sort((o1, o2) -> {
            o1 = o1.unnote();
            o2 = o2.unnote();

            ItemDefinition def = o1.definition(World.getWorld());
            ItemDefinition def2 = o2.definition(World.getWorld());

            int v1 = 0;
            int v2 = 0;

            if (def != null) {
                v1 = o1.getValue();
                if (v1 <= 0 && !GameServer.properties().pvpMode) {
                    v1 = o1.getBloodMoneyPrice().value();
                }
            }
            if (def2 != null) {
                v2 = o2.getValue();
                if (v2 <= 0 && !GameServer.properties().pvpMode) {
                    v2 = o2.getBloodMoneyPrice().value();
                }
            }

            return Integer.compare(v2, v1);
        });
        int keptItems = (Skulling.skulled(player) ? 0 : 3);

        // On Ultimate Iron Man, you drop everything!
        if (player.ironMode() == IronMode.ULTIMATE) {
            keptItems = 0;
        }

        boolean protection_prayer = Prayers.usingPrayer(player, Prayers.PROTECT_ITEM) || Prayers.usingPrayer(player, Prayers.PROTECT_ITEM_2);
        if (protection_prayer) {
            keptItems++;
        }

        //#Update as of 16/02/2021 when smited you're actually smited the pet effect will not work!
        var reaper = player.hasPetOut("Grim Reaper pet") || player.hasPetOut("Blood Reaper pet");
        if (reaper && protection_prayer) {
            keptItems++;
        }
        if (player.getSkullType().equals(SkullType.RED_SKULL) || player.mode().isDarklord()) {
            keptItems = 0;
        }
        IKODTest.debug("keeping " + keptItems + " items");

        while (keptItems-- > 0 && !toDrop.isEmpty()) {
            Item head = toDrop.peek();
            if (head == null) {
                keptItems++;
                toDrop.poll();
                continue;
            }

            keep.add(new Item(head.getId(), 1));

            //Always drop wildy keys
            if (head.getId() == CustomItemIdentifiers.WILDERNESS_KEY) {
                if (WildernessArea.inWilderness(player.tile())) {
                    player.inventory().remove(CustomItemIdentifiers.WILDERNESS_KEY, Integer.MAX_VALUE);
                    PickupItemPacketListener.respawn(Item.of(CustomItemIdentifiers.WILDERNESS_KEY), tile, 3);
                    WildernessKeyPlugin.announceKeyDrop(player, tile);
                    keep.remove(head);
                }
            }

            if (head.getAmount() == 1) { // Amount 1? Remove the item entirely.
                Item delete = toDrop.poll();
                IKODTest.debug("kept " + delete.toShortString());
            } else { // Amount more? Subtract one amount.
                int index = toDrop.indexOf(head);
                toDrop.set(index, new Item(head, head.getAmount() - 1));
                IKODTest.debug("kept " + toDrop.get(index).toShortString());
            }
        }
        for (Item item : keep) {
            if (GameServer.properties().pvpMode) {//Only in PvP worlds
                // Handle item breaking..
                BrokenItem brokenItem = BrokenItem.get(item.getId());
                if (brokenItem != null) {
                    player.getPacketSender().sendMessage("Your " + item.unnote().name() + " has been broken. You can fix it by talking to").sendMessage("Perdu who is located in Edgevile at the furnace.");
                    item.setId(brokenItem.brokenItem);

                    //Drop bm for the killer
                    GroundItem groundItem = new GroundItem(new Item(BLOOD_MONEY, (int) brokenItem.bmDrop), player.tile(), theKiller);
                    GroundItemHandler.createGroundItem(groundItem);
                }
            }
            player.inventory().add(item, true);
        }
        if (outputDeleted.stream().anyMatch(i -> i.getId() == 30098 || i.getId() == 30099)) {

            Item[] lootingBag = player.getLootingBag().toNonNullArray(); // bypass check if carrying bag since inv is cleared above
            toDrop.addAll(Arrays.asList(lootingBag));
            playerDeathLogs.log(PLAYER_DEATHS, player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") looting bag lost items: " + Arrays.toString(Arrays.asList(lootingBag).toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""));
            Utils.sendDiscordInfoLog(player,player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") looting bag lost items: " + Arrays.toString(Arrays.asList(lootingBag).toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""), "playerdeaths");
            player.getLootingBag().clear();
            IKODTest.debug("looting bag had now: " + Arrays.toString(Arrays.asList(lootingBag).toArray()));
        }
        // Looting bag items are NOT in top-3 kept from prot item/unskulled. Always lost.
        if (outputDeleted.stream().anyMatch(i -> i.getId() == LOOTING_BAG || i.getId() == LOOTING_BAG_22586)) {
            Item[] lootingBag = player.getLootingBag().toNonNullArray(); // bypass check if carrying bag since inv is cleared above
            toDrop.addAll(Arrays.asList(lootingBag));
            playerDeathLogs.log(PLAYER_DEATHS, player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") looting bag lost items: " + Arrays.toString(Arrays.asList(lootingBag).toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""));
            Utils.sendDiscordInfoLog(player,player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") looting bag lost items: " + Arrays.toString(Arrays.asList(lootingBag).toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""), "playerdeaths");

            player.getLootingBag().clear();
            IKODTest.debug("looting bag had now: " + Arrays.toString(Arrays.asList(lootingBag).toArray()));
        }

        // Rune pouch items are NOT in top-3 kept from prot item/unskulled. Always lost.
        Item[] runePouch = player.getRunePouch().toNonNullArray(); // bypass check if carrying pouch since inv is cleared above
        toDrop.addAll(Arrays.asList(runePouch));
        player.getRunePouch().clear();
        IKODTest.debug("rune pouch had now: " + Arrays.toString(Arrays.asList(runePouch).toArray()));

        if (player.hasPetOut("Niffler") || player.hasPetOut("Ziffler")) {
            //Get the current stored item list
            var nifflerItemsStored = player.<ArrayList<Item>>getAttribOr(AttributeKey.NIFFLER_ITEMS_STORED, new ArrayList<Item>());
            if (nifflerItemsStored != null) {
                toDrop.addAll(nifflerItemsStored);
                playerDeathLogs.log(PLAYER_DEATHS, player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") niffler lost items: " + Arrays.toString(nifflerItemsStored.toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""));
                Utils.sendDiscordInfoLog(player,player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") niffler lost items: " + Arrays.toString(nifflerItemsStored.toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""), "playerdeaths");

                nifflerItemsStored.clear();
                IKODTest.debug("niffler had now: " + Arrays.toString(nifflerItemsStored.toArray()));
            }
        }

        lostItems.clear();
        IKODTest.debug("Dropping now: " + Arrays.toString(toDrop.stream().map(Item::toShortString).toArray()));

        outputKept.addAll(keep);
        IKODTest.debug("Kept-3: " + Arrays.toString(keep.stream().map(Item::toShortString).toArray()));

        Mob lastAttacker = player.getAttribOr(AttributeKey.LAST_DAMAGER, null);
        final boolean npcFlag = lastAttacker != null && lastAttacker.isNpc() && lastAttacker.getAsNpc().getBotHandler() != null;

        LinkedList<Item> toDropConverted = new LinkedList<>();

        toDrop.forEach(item -> {
            if (item.getId() == CustomItemIdentifiers.WILDERNESS_KEY) {
                if (WildernessArea.inWilderness(player.tile())) {
                    player.inventory().remove(CustomItemIdentifiers.WILDERNESS_KEY, Integer.MAX_VALUE);
                    PickupItemPacketListener.respawn(Item.of(CustomItemIdentifiers.WILDERNESS_KEY), tile, 3);
                    WildernessKeyPlugin.announceKeyDrop(player, tile);
                    return;
                }
            }

            if (item.getId() == AncientArtifacts.ANCIENT_EFFIGY.getItemId()
                || item.getId() == AncientArtifacts.ANCIENT_EMBLEM.getItemId()
                || item.getId() == AncientArtifacts.ANCIENT_MEDALLION.getItemId()
                || item.getId() == AncientArtifacts.ANCIENT_RELIC.getItemId()
                || item.getId() == AncientArtifacts.ANCIENT_STATUETTE.getItemId()
                || item.getId() == AncientArtifacts.ANCIENT_TOTEM.getItemId()) {
                GroundItemHandler.createGroundItem(new GroundItem(new Item(item.getId()), player.tile(), theKiller));
                outputDrop.add(new Item(item.getId()));
                // dont add to toDropConverted, we're manually dropping it
                return;
            }

            //Drop emblems but downgrade them a tier.
            if (item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_1.getItemId()
                || item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_2.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_3.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_4.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_5.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_6.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_7.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_8.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_9.getItemId() ||
                item.getId() == BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_10.getItemId()) {

                //Tier 1 shouldnt be dropped cause it cant be downgraded
                if (item.matchesId(BountyHunterEmblem.ANTIQUE_EMBLEM_TIER_1.getItemId())) {
                    return;
                }

                final int lowerEmblem = item.getId() - 2;

                ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, lowerEmblem);
                GroundItemHandler.createGroundItem(new GroundItem(new Item(lowerEmblem), player.tile(), theKiller));
                theKiller.message("<col=ca0d0d>" + player.getUsername() + " dropped a " + def.name + "!");
                outputDrop.add(new Item(lowerEmblem));
                // dont add to toDropConverted, we're manually dropping it
                return;
            }

            // IKODTest.debug("dc2: "+item.toShortString());

            // if we've got to here, add the original or changed SINGLE item to the newer list
            toDropConverted.add(item);
        });
        // replace the original list with the newer list which reflects changes
        toDrop = toDropConverted;

        // Dropping in-game the finalized items list on death here
        toDrop.forEach(item -> {
            //   IKODTest.debug("dropping check: "+item.toShortString());

            if (ItemsKeptOnDeath.alwaysKept(item)) {
                //System.out.println("Autokeep");
                //QOL OSRS doesn't drop them anymore but spawns in inventory.
                player.inventory().add(item);
                outputKept.add(item);
                return;
            }

            //Drop item
            //System.out.println("Creating ground item " + item.getId());
            //Add the items to the lost list regardless of if the player died to a bot.
            lostItems.add(item);

            boolean diedToSelf = theKiller == player;
            //System.out.println("died to npc "+npcFlag+" or died to self "+diedToSelf);
            boolean nifflerShouldLoot = !diedToSelf && !npcFlag;
            //System.out.println("nifflerShouldLoot "+nifflerShouldLoot);

            //Niffler should only pick up items of monsters and players that you've killed.
            if (theKiller.nifflerPetOut() && theKiller.nifflerCanStore(player) && nifflerShouldLoot) {
                if (item.getValue() > 0) {
                    theKiller.nifflerStore(item);//The player is our target
                }
            } else {
                GroundItem g = new GroundItem(item, player.tile(), theKiller);
                GroundItemHandler.createGroundItem(g);
                g.pkedFrom(player.getUsername()); // Mark item as from PvP to avoid ironmen picking it up.
            }
            outputDrop.add(item);
        });

        GroundItemHandler.createGroundItem(new GroundItem(new Item(BONES), player.tile(), theKiller));
        outputDrop.add(new Item(BONES));
        playerDeathLogs.log(PLAYER_DEATHS, player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") lost items: " + Arrays.toString(lostItems.stream().map(Item::toShortString).toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""));
        Utils.sendDiscordInfoLog(player,player.getUsername() + " (Skulled: " + Skulling.skulled(player) + ") lost items: " + Arrays.toString(lostItems.stream().map(Item::toShortString).toArray()) + (killer != null && killer.isPlayer() ? " to " + killer.getMobName() : ""), "playerdeaths");
        //Reset last attacked by, since we already handled it above, and the player is already dead.
        player.clearAttrib(AttributeKey.LAST_DAMAGER);
        return new PlayerDeathDropResult(theKiller, outputDrop, outputKept, outputDeleted, outputConverted);
    }

    final static Map<Item, Item> ITEMS_TO_BLOOD_MONEY = Map.of
        (
            Item.of(SUMMER_AMULET), Item.of(BLOOD_MONEY, 500_000),
            Item.of(LOOTING_BAG), Item.of(BLOOD_MONEY, 1250),
            Item.of(LOOTING_BAG_22586), Item.of(BLOOD_MONEY, 1250)
        );

    private static void dropBloodMoneyForItems(final LinkedList<Item> toDrop, final Player player, final Player killer) {
        for (final var entry : ITEMS_TO_BLOOD_MONEY.entrySet()) {
            final Item key = entry.getKey();
            final Item val = entry.getValue();
            toDrop.removeIf(item -> {
                if (item.matchesId(key.getId())) {
                    GroundItemHandler.createGroundItem(new GroundItem(val, player.tile(), killer));
                    return true;
                }
                return false;
            });
        }
    }

    private static void stripHardcoreRank(Player player) {
        player.ironMode(IronMode.REGULAR); // Revert mode
        if (!player.getPlayerRights().isStaffMemberOrYoutuber(player)) {
            player.setPlayerRights(PlayerRights.IRON_MAN);
            player.getPacketSender().sendRights();
        }
        World.getWorld().sendWorldMessage("<img=504>" + Color.RED.wrap("[Hardcore fallen]:") + " " + Color.BLUE.wrap(player.getUsername()) + " has fallen as a Hardcore Iron Man!");
        player.message("You have fallen as a Hardcore Iron Man', your Hardcore status has been revoked.");
    }

    private static void stripDarkLordRank(Player player) {
        if (player.getParticipatingTournament() != null) {
            return;
        }
        if (player.getRaids() != null) {
            return;
        }
        var lives = player.<Integer>getAttribOr(AttributeKey.DARK_LORD_LIVES, 3) - 1;
        player.putAttrib(AttributeKey.DARK_LORD_LIVES, lives);
        if (lives == 0) {
            if (!player.getPlayerRights().isStaffMemberOrYoutuber(player)) {
                player.setPlayerRights(PlayerRights.PLAYER);
                player.getPacketSender().sendRights();
            }
            player.mode(GameMode.TRAINED_ACCOUNT);
            player.message("You have fallen as a Dark Lord', your status has been revoked.");
            World.getWorld().sendWorldMessage("<img=1081>" + Color.PURPLE.wrap(player.getUsername()) + Color.RED.wrap("has fallen as a Dark Lord!"));
        }
    }
}
