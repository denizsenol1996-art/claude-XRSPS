package com.twisted.game.world.items;

import com.twisted.fs.DefinitionRepository;
import com.twisted.fs.ItemDefinition;
import com.twisted.game.content.areas.wilderness.content.revenant_caves.AncientArtifacts;
import com.twisted.game.content.tradingpost.TradingPost;
import com.twisted.game.world.World;
import com.twisted.game.world.definition.BloodMoneyPrices;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.items.container.equipment.EquipmentInfo;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.twisted.game.GameConstants.PVP_ALLOWED_SPAWNS;
import static com.twisted.game.content.mechanics.break_items.BreakItemsOnDeath.*;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.CustomItemIdentifiers.DRAGON_CLAWS_OR;
import static com.twisted.util.CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR;
import static com.twisted.util.CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.BOW_OF_FAERDHINEN;
import static com.twisted.util.ItemIdentifiers.DRAGON_HUNTER_CROSSBOW_T;
import static com.twisted.util.ItemIdentifiers.MASORI_BODY_F;
import static com.twisted.util.ItemIdentifiers.MASORI_MASK;

/**
 * Represents an item.
 *
 * @author Professor Oak
 */

public class Item implements Cloneable {

    private static final Logger logger = LogManager.getLogger(Item.class);

    public static final Comparator<Item> ITEM_VALUE_COMPARATOR = Comparator.comparingInt(Item::getValue);

    /**
     * Returns an {@code Item} instance of the type {@code id}, and a stack size of {@code amount}.
     *
     * @throws IllegalArgumentException if {@code id} is invalid, or {@code amount} is {@code < 1}.
     */
    public static Item of(int id, int amount) {
        ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, id);
        if (def == null) {
            throw new IllegalArgumentException("undefined item: " + id);
        }
        if (amount < 1) {
            throw new IllegalArgumentException("amount zero or negative: " + amount);
        }
        return new Item(id, amount);
    }

    /**
     * Returns an {@code Item} instance of the type {@code id}, and a stack size of {@code 1}.
     *
     * @throws IllegalArgumentException if {@code id} is invalid.
     */
    public static Item of(int id) {
        ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, id);
        if (def == null) {
            throw new IllegalArgumentException("undefined item: " + id);
        }
        return new Item(id, 1);
    }

    /**
     * The item id.
     */
    private int id;

    /**
     * Amount of the item.
     */
    private int amount;

    public static void onServerStart() {
        Mob.accumulateRuntimeTo(() -> {
            checkDefs();
        }, t -> {
            System.out.println("itemdef special fields took " + t.toMillis() + " ms");
        });

    }

    private static void checkDefs() {
        for (int i : AUTO_KEPT_LIST) {
            ItemDefinition definition = World.getWorld().definitions().get(ItemDefinition.class, i);
            definition.autoKeptOnDeath = true;
        }
        for (AncientArtifacts value : AncientArtifacts.values()) {
            ItemDefinition definition = World.getWorld().definitions().get(ItemDefinition.class, value.getItemId());
            definition.changes = true;
        }
        World.getWorld().definitions().get(ItemDefinition.class, SARADOMINS_BLESSED_SWORD).changes = true;
        World.getWorld().definitions().get(ItemDefinition.class, SARAS_BLESSED_SWORD_FULL).changes = true;
        for (int i = 0; i < World.getWorld().definitions().total(ItemDefinition.class); i++) {
            if (Item.isCrystal(i)) {
                World.getWorld().definitions().get(ItemDefinition.class, SARAS_BLESSED_SWORD_FULL).isCrystal = true;
            }
        }
        int[] tradeable_special_items = {
            FROST_IMBUED_CAPE, SALVE_AMULET, SALVE_AMULETI, VENGEANCE_SKULL, FEROCIOUS_GLOVES, RING_OF_MANHUNTING, AVERNIC_DEFENDER, INFERNAL_CAPE, TWISTED_SLAYER_HELMET_I,
            TWISTED_SLAYER_HELMET_I_KBD_HEADS, PURPLE_SLAYER_HELMET_I, HYDRA_SLAYER_HELMET_I, TWISTED_SLAYER_HELMET_I_JAD,
            TWISTED_SLAYER_HELMET_I_INFERNAL_CAPE, TWISTED_SLAYER_HELMET_I_VAMP_DUST, RING_OF_TRINITY, RING_OF_PRECISION, RING_OF_SORCERY,
            SLAYER_HELMET_I, GREEN_SLAYER_HELMET_I, TURQUOISE_SLAYER_HELMET_I, RED_SLAYER_HELMET_I, BLACK_SLAYER_HELMET_I,
            DRAGON_DAGGERP_24949, GRANITE_MAUL_24944, PARTYHAT__SPECS_24945, ABYSSAL_TENTACLE_24948, FREMENNIK_KILT_24946, SPIKED_MANACLES_24947, FAWKES_24937, MOLTEN_DEFENDER, TWISTED_BOW_I,
            RING_OF_WEALTH_I, ZAWEKS_PET, MENDING_LIFE_BIRD, VOID_KNIGHT_ROBE, VOID_KNIGHT_GLOVES, VOID_KNIGHT_TOP, VOID_RANGER_HELM, VOID_MAGE_HELM, VOID_MELEE_HELM, 23154, 23156, 23158, 29111, 29112, 29113, 29114, 29115, 19556, 26469, 26471, 26473, 26475, 26477, 24942, 24943,
        };//SAVED ON DEATH - do not end last one with comma
        for (int i : tradeable_special_items) {
            World.getWorld().definitions().get(ItemDefinition.class, i).tradeable_special_items = true;
        }
        for (int i : PVP_ALLOWED_SPAWNS) {
            World.getWorld().definitions().get(ItemDefinition.class, i).pvpAllowed = true;
        }
    }

    /**
     * Gets the item's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the amount of the item.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * An Item object constructor.
     *
     * @param id     Item id.
     * @param amount Item amount.
     */
    public Item(int id, int amount) {
        this.id = id;
        this.amount = Math.max(amount, 0);
    }

    /**
     * An Item object constructor.
     *
     * @param item   the Item.
     * @param amount Item amount.
     */
    public Item(Item item, int amount) {
        id = item.id;
        this.amount = Math.max(amount, 0);
    }

    /**
     * An Item object constructor.
     *
     * @param id Item id.
     */
    public Item(int id) {
        this(id, 1);
    }

    public Item(Item item) {
        id = ((short) item.getId());
        amount = item.getAmount();
    }

    /**
     * Sets the identification of this item.
     *
     * @param id the new identification of this item.
     */
    public final void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the quantity of this item.
     *
     * @param amount the new quantity of this item.
     */
    public final void setAmount(int amount) {
        if (amount < 0)
            amount = 0;
        this.amount = amount;
    }

    /**
     * Checks if this item is valid or not.
     *
     * @return
     */
    public boolean isValid() {
        return id > 0 && amount >= 1;
    }

    /**
     * Determines if {@code item} is valid. In other words, determines if
     * {@code item} is not {@code null} and the {@link Item#id}.
     *
     * @param item the item to determine if valid.
     * @return {@code true} if the item is valid, {@code false} otherwise.
     */
    public static boolean valid(Item item) {
        return item != null && item.id > 0;
    }

    /**
     * Copying the item by making a new item with same values.
     */
    public Item copy() {
        Item item = new Item(id, amount);
        return item;
    }

    /**
     * Increment the amount by the specified amount.
     */
    public void incrementAmountBy(int amount) {
        if ((this.amount + amount) > Integer.MAX_VALUE) {
            this.amount = Integer.MAX_VALUE;
        } else {
            this.amount += amount;
        }
    }

    /**
     * Decrement the amount by the specified amount.
     */
    public void decrementAmountBy(int amount) {
        if ((this.amount - amount) < 1) {
            this.amount = 0;
        } else {
            this.amount -= amount;
        }
    }

    /**
     * Creates a new item with {@code newId} and the same amount as this instance.
     * The returned {@code Item} <strong>does not</strong> hold any references to
     * this one unless {@code id == newId}. It will throw an exception on an invalid
     * id.
     *
     * @param newId The new id to set.
     * @return The newly id set {@code Item}.
     */
    public Item createWithId(int newId) {
        if (id == newId) {
            return this;
        }
        return new Item(newId, amount);
    }

    /**
     * Creates a new item with {@code newAmount} and the same identifier as this
     * instance. The returned {@code Item} <strong>does not</strong> hold any
     * references to this one unless {@code amount == newAmount}. It will throw an
     * exception on overflows and negative values.
     *
     * @param newAmount The new amount to set.
     * @return The newly amount set {@code Item}.
     */
    public Item createWithAmount(int newAmount) {
        if (amount == newAmount) {
            return this;
        }
        return new Item(id, newAmount);
    }

    /**
     * Creates a new item with {@code amount + addAmount} and the same identifier.
     * The returned {@code Item} <strong>does not</strong> hold any references to
     * this one. It will also have a maximum amount of {@code
     * Integer.MAX_VALUE}.
     *
     * @param addAmount The amount to deposit.
     * @return The newly incremented {@code Item}.
     */
    public Item createAndIncrement(int addAmount) {
        if (addAmount < 0) { // Same effect as decrementing.
            return createAndDecrement(Math.abs(addAmount));
        }

        int newAmount = amount + addAmount;

        if (newAmount < amount) { // An overflow.
            newAmount = Integer.MAX_VALUE;
        }

        Item item = clone();
        item.setAmount(newAmount);
        return item;
    }

    /**
     * Creates a new item with {@code amount - removeAmount} and the same
     * identifier. The returned {@code Item} <strong>does not</strong> hold any
     * references to this one. It will also have a minimum amount of {@code 1}.
     *
     * @param removeAmount The amount to withdraw.
     * @return The newly incremented {@code Item}.
     */
    public Item createAndDecrement(int removeAmount) {
        if (removeAmount < 0) { // Same effect as incrementing.
            return createAndIncrement(-removeAmount);
        }

        int newAmount = amount - removeAmount;

        // Value too low, or an overflow.
        if (newAmount < 1 || newAmount > amount) {
            newAmount = 1;
        }

        Item clone = clone();
        clone.setAmount(newAmount);
        return clone;
    }

    /**
     * Increments the amount by {@code 1}.
     */
    public final void incrementAmount() {
        incrementAmountBy(1);
    }

    public BloodMoneyPrices getBloodMoneyPrice() {
        return definition(World.getWorld()).bm;
    }

    public boolean isTwoHanded() {
        EquipmentInfo info = World.getWorld().equipmentInfo();
        return info.typeFor(this.getId()) == 5;// If type is 5 it is a two-handed weapon
    }

    /**
     * Resolve this item's definition in the world's repository for definitions. No definition returns <code>null</code>.
     *
     * @param world The world to use to resolve the definition for this item.
     * @return The item's definitions, or <code>null</code> if that didn't work out.
     */
    public ItemDefinition definition(World world) {
        return world.definitions().get(ItemDefinition.class, id);
    }

    public ItemDefinition definition(DefinitionRepository repo) {
        return repo.get(ItemDefinition.class, id);
    }

    public Item unnote() {
        return unnote(World.getWorld().definitions());
    }

    public Item unnote(DefinitionRepository repo) {
        ItemDefinition def = definition(repo);
        if (def != null && def.noteModel > 0) {
            return new Item(def.notelink, amount); // Properties check not required: properties do not stick to notes.
        }

        return this;
    }

    public Item note() {
        ItemDefinition def = definition(World.getWorld());
        if (def == null || def.noteModel > 0 || def.notelink < 1) {
            return this;
        }

        return new Item(def.notelink, amount);
    }

    public boolean noted() {
        return noted(World.getWorld().definitions());
    }

    public boolean noted(DefinitionRepository repo) {
        return unnote(repo) != this;
    }

    public boolean noteable() {
        return id != note().getId();
    }

    public boolean stackable() {
        ItemDefinition def = definition(World.getWorld());
        //Item not in OSRS cache could be custom, lets flag as false
        if (def == null) {
            return false;
        }
        return def.stackable();
    }

    public boolean rawtradable() {
        ItemDefinition def = definition(World.getWorld());
        return (def == null || id == ItemIdentifiers.PLATINUM_TOKEN || id == COINS_995 || def.grandexchange || def.noteModel > 0 || def.notelink > 0 || TRADABLES.contains(id));
    }

    public String name() {
        ItemDefinition def = definition(World.getWorld());
        if (def == null) {
            return "Unknown name";
        }
        return def.name;
    }

    public boolean makeItemsTradeable() {
        return TRADABLES.contains(id);
    }

    /**
     * Items which normally would be untradeable.
     */
    public static final Set<Integer> TRADABLES = new HashSet<>() {
        {
            add(SERENIC);
            add(30091);
            add(23214);
            add(20400);
            add(WILDERNESS_KEY);
            add(ENCHANTED_SALAZAR_LOCKET);
            add(ZAWEKS_PET);
            add(HOLY_ORNAMENT_KIT);
            add(MENDING_LIFE_BIRD);
            add(SANGUINE_TWISTED_BOW);
            add(SANGUINE_SCYTHE_OF_VITUR);
            add(AFK_TICKET);
            add(CORRUPTED_CRYSTAL_HELM);
            add(CORRUPTED_CRYSTAL_BODY);
            add(CORRUPTED_CRYSTAL_LEGS);
            add(BOW_OF_FAERDHINEN);
            add(RING_OF_WEALTH_I);
            add(ESCAPE_KEY);
            add(ENCHANTED_KEY_II);
            add(ENCHANTED_KEY_I);
            add(26479);
            add(26421);
            add(DERANGED_ARCHAEOLOGIST);
            add(ANATHEMATIC_WAND);
            add(RING_OF_DIVINATION);
            add(DERANGED_MANIFESTO);
            add(ANTIQUE_EMBLEM_TIER_10);//DONE1
            add(ENCHANTED_BONES);
            add(ANATHEMATIC_STONE);
            add(ANATHEMA_WARD);
            add(MOLTEN_DEFENDER);
            add(MOLTEN_MYSTERY_BOX);
            add(MOLTEN_PARTYHAT);
            add(FROST_IMBUED_CAPE);
            add(LAVA_BABY);
            add(MOLTEN_KEY);
            add(ELVARG_JR);
            add(SNOWBIRD);
            add(PRESENT_13346);
            add(SNOWY_SLED);
            add(ICED_PARTYHAT);
            add(ARMADYL_FROSTSWORD);
            add(FROST_CLAWS);
            add(CustomItemIdentifiers.ELDER_ICE_MAUL);
            add(ICED_HWEEN_MASK);
            add(ICED_SANTA_HAT);
            add(WINTER_TOKEN);
            add(ABYSSAL_TENTACLE_24948);
            add(INFINITY_WINTER_BOOTS);
            add(UGLY_SANTA_HAT);
            add(TWISTED_BOW_I);
            add(TOTEMIC_HELMET);
            add(TOTEMIC_PLATEBODY);
            add(TOTEMIC_PLATELEGS);
            add(DARK_SAGE_HAT);
            add(DARK_SAGE_ROBE_TOP);
            add(DARK_SAGE_ROBE_BOTTOM);
            add(SARKIS_DARK_COIF);
            add(SARKIS_DARK_BODY);
            add(SARKIS_DARK_LEGS);
            add(SUMMER_TOKEN);
            add(LAVA_DHIDE_BODY);
            add(LAVA_DHIDE_CHAPS);
            add(LAVA_DHIDE_COIF);
            add(HWEEN_TOKENS);
            add(GRIM_HWEEN_MASK);
            add(GRIM_PARTYHAT);
            add(GRIM_SCYTHE);
            add(HWEEN_MYSTERY_CHEST);
            add(SKELETON_HELLHOUND_PET);
            add(HWEEN_MYSTERY_BOX);
            add(ANCESTRAL_HAT_I);
            add(ANCESTRAL_ROBE_TOP_I);
            add(ANCESTRAL_ROBE_BOTTOM_I);
            add(MYSTERY_TICKET);
            add(BLOOD_MONEY_CASKET_PROMO);
            add(MASORI_MASK);
            add(ItemIdentifiers.MASORI_BODY);
            add(ItemIdentifiers.MASORI_CHAPS);
            add(ItemIdentifiers.MASORI_MASK_F);
            add(MASORI_BODY_F);
            add(ItemIdentifiers.MASORI_CHAPS_F);
            add(ItemIdentifiers.ELIDINIS_WARD);
            add(ItemIdentifiers.ELIDINIS_WARD_F);
            add(ItemIdentifiers.ELIDINIS_WARD_OR);
            add(ItemIdentifiers.TUMEKENS_SHADOW);
            add(ItemIdentifiers.OSMUMTENS_FANG);
            add(LIGHTBEARER);
            add(ItemIdentifiers.MENAPHITE_ORNAMENT_KIT);
            add(ItemIdentifiers.OSMUMTENS_FANG_OR);
            add(TORVA_FULL_HELM);
            add(TORVA_PLATEBODY);
            add(TORVA_PLATELEGS);
            add(JALNIBREK);
            add(BLOOD_REAPER);
            add(YOUNGLLEF);
            add(CORRUPTED_YOUNGLLEF);
            add(PARTYHAT__SPECS_24945);
            add(LIME_PARTYHAT);
            add(ORANGE_PARTYHAT);
            add(WHITE_HWEEN_MASK);
            add(PURPLE_HWEEN_MASK);
            add(LIME_GREEN_HWEEN_MASK);
            add(FAWKES_24937);
            add(GAMBLER_SCROLL);
            add(CORRUPTING_STONE);
            add(CORRUPTED_RANGER_GAUNTLETS);
            add(LITTLE_NIGHTMARE);
            add(KERBEROS_PET);
            add(TALONHAWK_CROSSBOW);
            add(ELDER_WAND_STICK);
            add(ELDER_WAND_HANDLE);
            add(ELDER_WAND);
            add(SWORD_OF_GRYFFINDOR);
            add(30103);//dzone3
            add(DARK_BANDOS_CHESTPLATE);
            add(DARK_BANDOS_TASSETS);
            add(CORRUPTED_BOOTS);
            add(30326);
            add(29007);
            add(PRIMORDIAL_BOOTS_OR);
            add(DRAGON_SCIMITAR_OR);
            add(SARADOMINS_BLESSED_SWORD);
            add(ETHEREAL_PARTYHAT);
            add(ETHEREAL_HWEEN_MASK);
            add(ETHEREAL_SANTA_HAT);
            add(DARK_ARMADYL_HELMET);
            add(DARK_ARMADYL_CHESTPLATE);
            add(DARK_ARMADYL_CHAINSKIRT);
            add(CLAN_BOX);
            add(SHADOW_INQUISITOR_ORNAMENT_KIT);
            add(INQUISITORS_MACE_ORNAMENT_KIT);
            add(GENIE_PET);
            add(HAZY_COINS);
            add(BLIGHTED_ANCIENT_ICE_SACK);
            add(BLIGHTED_ENTANGLE_SACK);
            add(BLIGHTED_BIND_SACK);
            add(BLIGHTED_TELEPORT_SPELL_SACK);
            add(BLIGHTED_VENGEANCE_SACK);
            add(BLIGHTED_SNARE_SACK);
            add(ZENYTE_MYSTERY_BOX);
            add(RAIDS_MYSTERY_BOX);
            add(DARK_ELDER_MAUL);
            add(ANCIENT_WARRIOR_AXE_C);
            add(ANCIENT_WARRIOR_MAUL_C);
            add(ANCIENT_WARRIOR_SWORD_C);
            add(ANCIENT_WARRIOR_CLAMP);
            add(ANCIENT_FACEGAURD);
            add(ANCIENT_BARRELCHEST_PET);
            add(ANCIENT_CHAOS_ELEMENTAL_PET);
            add(ANCIENT_KING_BLACK_DRAGON_PET);
            add(ANCIENT_VESTAS_LONGSWORD);
            add(ANCIENT_STATIUSS_WARHAMMER);
            add(AMULET_OF_BLOOD_FURY);
            add(PEGASIAN_BOOTS_OR);
            add(ETERNAL_BOOTS_OR);
            add(VIGGORAS_CHAINMACE_C);
            add(CRAWS_BOW_C);
            add(THAMMARONS_STAFF_C);
            add(VENGEANCE_SKULL);
            add(WAMPA);
            add(BABY_SQUIRT);
            add(BABY_LAVA_DRAGON);
            add(ZRIAWK);
            add(MINI_NECROMANCER);
            add(PET_CORRUPTED_NECHRYARCH);
            add(FOUNDER_IMP);
            add(DHAROK_PET);
            add(JALTOK_JAD);
            add(PET_ZOMBIES_CHAMPION);
            add(BABY_DARK_BEAST_EGG);
            add(BABY_ABYSSAL_DEMON);
            add(FENRIR_GREYBACK_JR);
            add(FLUFFY_JR);
            add(DEMENTOR_PET);
            add(CENTAUR_MALE);
            add(CENTAUR_FEMALE);
            add(TZREKZUK);
            add(EPIC_PET_BOX);
            add(IMBUED_HEART);
            add(BERSERKER_NECKLACE_OR);
            add(LARRANS_KEY_TIER_II);
            add(LARRANS_KEY_TIER_III);
            add(AMULET_OF_TORTURE_OR);
            add(NECKLACE_OF_ANGUISH_OR);
            add(TORMENTED_BRACELET_OR);
            add(BLOODY_TOKEN);
            add(AVERNIC_DEFENDER);
            add(SALAZAR_SLYTHERINS_LOCKET);
            add(VORKATHS_HEAD_21907);
            add(MYSTERY_CHEST);
            add(DARK_CLAW);
            add(JAWA_PET);
            add(BABY_ARAGOG);
            add(INFERNAL_CAPE);
            add(ELDRITCH_ORB);
            add(HARMONISED_ORB);
            add(VOLATILE_ORB);
            add(RING_OF_MANHUNTING);
            add(RING_OF_SORCERY);
            add(RING_OF_PRECISION);
            add(RING_OF_TRINITY);
            add(TOXIC_BLOWPIPE);
            add(SERPENTINE_HELM);
            add(TOXIC_STAFF_OF_THE_DEAD);
            add(TRIDENT_OF_THE_SWAMP);
            add(SCYTHE_OF_VITUR);
            add(SANGUINESTI_STAFF);
            add(ItemIdentifiers.HOLY_SANGUINESTI_STAFF);
            add(ItemIdentifiers.HOLY_GHRAZI_RAPIER);
            add(23214);
            add(HOLY_SCYTHE_OF_VITUR);
            add(FEROCIOUS_GLOVES);
            add(DOUBLE_DROPS_LAMP);
            add(INQUISITORS_MACE);
            add(INQUISITORS_GREAT_HELM);
            add(INQUISITORS_HAUBERK);
            add(INQUISITORS_PLATESKIRT);
            add(RUNE_POUCH);
            add(27655);
            add(27660);
            add(30095);
            add(30101);
            add(LUMBERJACK_HAT);
            add(LUMBERJACK_TOP);
            add(LUMBERJACK_LEGS);
            add(LUMBERJACK_BOOTS);
            add(ANGLER_HAT);
            add(ANGLER_TOP);
            add(ANGLER_WADERS);
            add(ANGLER_BOOTS);
            add(PROSPECTOR_HELMET);
            add(PROSPECTOR_JACKET);
            add(PROSPECTOR_LEGS);
            add(PROSPECTOR_BOOTS);
            add(30101);
            add(27248);
            add(GRANITE_MAUL_12848);
            add(ABYSSAL_TENTACLE);
            add(MYSTERY_BOX);
            add(LARRANS_KEY);
            add(MYSTIC_STEAM_STAFF_12796);
            add(MALEDICTION_WARD_12806);
            add(ODIUM_WARD_12807);
            add(SEERS_RING_I);
            add(ARCHERS_RING_I);
            add(WARRIOR_RING_I);
            add(BERSERKER_RING_I);
            add(TREASONOUS_RING_I);
            add(TYRANNICAL_RING_I);
            add(RING_OF_THE_GODS_I);
            add(DRAGONFIRE_SHIELD);
            add(LIGHT_INFINITY_HAT);
            add(LIGHT_INFINITY_TOP);
            add(LIGHT_INFINITY_BOTTOMS);
            add(DARK_INFINITY_HAT);
            add(DARK_INFINITY_TOP);
            add(DARK_INFINITY_BOTTOMS);
            add(STOLEN_JEWELRY_BOX);
            add(DRAGON_CLAWS);
            add(GNOME_CHILD_HAT);
            add(MOONCLAN_HAT);
            add(MOONCLAN_ARMOUR);
            add(MOONCLAN_SKIRT);
            add(MOONCLAN_GLOVES);
            add(MOONCLAN_BOOTS);
            add(MOONCLAN_CAPE);
            add(TRIDENT_OF_THE_SEAS);
            add(TANZANITE_HELM);
            add(MAGMA_HELM);
            add(AMULET_OF_THE_DAMNED);
            add(ANCIENT_MACE);
            add(TOME_OF_FIRE);
            add(DRAGON_JAVELIN);
            add(ANCIENT_WYVERN_SHIELD);
            add(DRAGONFIRE_WARD);
            add(RING_OF_SUFFERING);
            add(RING_OF_SUFFERING_I);
            add(BRACELET_OF_ETHEREUM);
            add(CRAWS_BOW);
            add(VIGGORAS_CHAINMACE);
            add(THAMMARONS_SCEPTRE);
            add(VOLATILE_NIGHTMARE_STAFF);
            add(ELDRITCH_NIGHTMARE_STAFF);
            add(HARMONISED_NIGHTMARE_STAFF);
            add(GRANITE_MAUL_24225);
            add(BARRELCHEST_ANCHOR);
            add(NEITIZNOT_FACEGUARD);
            add(BLADE_OF_SAELDOR);
            add(TWISTED_ANCESTRAL_HAT);
            add(TWISTED_ANCESTRAL_ROBE_TOP);
            add(TWISTED_ANCESTRAL_ROBE_BOTTOM);
            add(FLIPPERS);
            add(MUDSKIPPER_HAT);
            add(RAINBOW_SCARF);
            add(SCYTHE);
            add(FLARED_TROUSERS);
            add(GNOME_SCARF);
            add(RAIN_BOW);
            add(HAM_JOINT);
            add(CAPE_OF_SKULLS);
            add(HEAVY_CASKET);
            add(BLOOD_MONEY);
            //Custom items that are not in the OSRS cache
            add(WEAPON_MYSTERY_BOX);
            add(ARMOUR_MYSTERY_BOX);
            add(LEGENDARY_MYSTERY_BOX);
            add(DONATOR_MYSTERY_BOX);
            add(GRAND_MYSTERY_BOX);
            add(PET_MYSTERY_BOX);
            add(PET_PAINT_BLACK);
            add(PET_PAINT_WHITE);
            add(FIVE_DOLLAR_BOND);
            add(TEN_DOLLAR_BOND);
            add(TWENTY_DOLLAR_BOND);
            add(FORTY_DOLLAR_BOND);
            add(FIFTY_DOLLAR_BOND);
            add(ONE_HUNDRED_DOLLAR_BOND);
            add(EARTH_ARROWS);
            add(FIRE_ARROWS);
            add(ANCIENT_WARRIOR_SWORD);
            add(ANCIENT_WARRIOR_MAUL);
            add(ANCIENT_WARRIOR_AXE);
            add(KEY_OF_BOXES);
            add(LAVA_WHIP);
            add(FROST_WHIP);
            add(ELEMENTAL_BOW);
            add(DONATOR_TICKET);
            add(VOTE_TICKET);
         /*   add(29111);
            add(29112);
            add(29113);
            add(29114);
            add(29115);*/
            add(LAVA_PARTYHAT);
            add(RECOVER_SPECIAL_4);
            add(RECOVER_SPECIAL_3);
            add(RECOVER_SPECIAL_2);
            add(RECOVER_SPECIAL_1);
            add(TWISTED_ANCESTRAL_COLOUR_KIT);
            add(RAINBOW_PARTYHAT);
            add(BLACK_PARTYHAT);
            add(BLACK_HWEEN_MASK);
            add(BLACK_SANTA_HAT);
            add(INVERTED_SANTA_HAT);
            add(EASTER_RING);
            add(EASTER_BASKET);
            add(SLED_4084);
            add(DRAGON_FULL_HELM_G);
            add(DRAGON_PLATEBODY_G);
            add(DRAGON_PLATELEGS_G);
            add(DRAGON_KITESHIELD_G);
            add(NIGHTMARE_STAFF);
            add(NIFFLER);
            add(GRIM_REAPER_PET);
            add(BARRELCHEST_PET);
            add(FAWKES);
            add(DRAGON_CLAWS_OR);
            add(ARMADYL_GODSWORD_OR);
            add(BANDOS_GODSWORD_OR);
            add(SARADOMIN_GODSWORD_OR);
            add(ZAMORAK_GODSWORD_OR);
            add(SLAYER_HELMET);
            add(SLAYER_HELMET_I);
            add(GREEN_SLAYER_HELMET);
            add(GREEN_SLAYER_HELMET_I);
            add(TURQUOISE_SLAYER_HELMET);
            add(TURQUOISE_SLAYER_HELMET_I);
            add(RED_SLAYER_HELMET);
            add(RED_SLAYER_HELMET_I);
            add(BLACK_SLAYER_HELMET);
            add(BLACK_SLAYER_HELMET_I);
            add(TWISTED_SLAYER_HELMET_I);
            add(TWISTED_SLAYER_HELMET_I_KBD_HEADS);
            add(PURPLE_SLAYER_HELMET_I);
            add(HYDRA_SLAYER_HELMET_I);
            add(AMULET_OF_FURY_OR);
            add(OCCULT_NECKLACE_OR);
            add(ATTACKER_ICON);
            add(COLLECTOR_ICON);
            add(DEFENDER_ICON);
            add(HEALER_ICON);
            add(SAELDOR_SHARD);
            add(PHARAOHS_HILT);
            add(DRAGON_HUNTER_CROSSBOW_T);
            //add(BOW_OF_FAERDHINEN_3);
            add(BLADE_OF_SAELDOR_8);
            add(CRYSTAL_BODY);
            add(CRYSTAL_HELM);
            add(CRYSTAL_LEGS);
            add(ARTIO_PET);
            add(ARACHNE_PET);
            add(SKORPIOS_PET);
            add(BLOOD_MONEY_PET);
            add(TWISTED_SLAYER_HELMET_I_INFERNAL_CAPE);
            add(TWISTED_SLAYER_HELMET_I_VAMP_DUST);
            add(TWISTED_SLAYER_HELMET_I_JAD);
            add(RING_OF_ELYSIAN);
            add(TOXIC_STAFF_OF_THE_DEAD_C);
            add(SHADOW_MACE);
            add(SHADOW_GREAT_HELM);
            add(SHADOW_HAUBERK);
            add(SHADOW_PLATESKIRT);
            add(SCYTHE_OF_VITUR_KIT);
            add(TWISTED_BOW_KIT);
            add(HWEEN_ARMADYL_GODSWORD);
            add(HWEEN_BLOWPIPE);
            add(HWEEN_DRAGON_CLAWS);
            add(HWEEN_CRAWS_BOW);
            add(HWEEN_CHAINMACE);
            add(HWEEN_GRANITE_MAUL);
            add(HAUNTED_CROSSBOW);
            add(HAUNTED_DRAGONFIRE_SHIELD);
            add(NAGINI);
            add(TOM_RIDDLE_DIARY);
            add(MARVOLO_GAUNTS_RING);
            add(CLOAK_OF_INVISIBILITY);
            add(ZARYTE_CROSSBOW);
            add(14025);
            add(14026);
            add(14027);
            add(14028);
            add(14035);
            add(14036);
            add(14037);
            add(14032);
            add(14033);
            add(14034);
            add(14029);
            add(14030);
            add(14031);
            add(30049);
            add(GRANDMASTER_HEART);
            add(GRANDMASTER_HELM);
            add(GRANDMASTER_MACE);
            add(ETHEREAL_SCYTHE);
            add(GRANDMASTER_BOOTS);
            add(GRANDMASTER_GLOVES);
            add(GRANDMASTER_CAPE);
            add(SEVENTY_FIVE_DOLLAR_BOND);
            add(GRANDMASTER_SCEPTRE);
            add(GRANDMASTER_BOW);
            add(298);
            add(CURSED_ELEMENT_PET);
            add(YOSHI_PET);
            add(THE_ONE_ABOVE_ALL_PET);
            add(RUINOUS_BOOK);
            add(VENATOR_BOW);
        }
    };

    // These untradable items will be send to the inventory or bank on death.
    public static final int[] AUTO_KEPT_LIST = new int[]{
        SACK_OF_PRESENTS,
        ELDER_WAND_RAIDS,
        KILLERS_KNIFE_21059,
        BEGINNER_WEAPON_PACK,
        BEGINNER_DRAGON_CLAWS,
        BEGINNER_AGS,
        BEGINNER_CHAINMACE,
        BEGINNER_CRAWS_BOW,
        VETERAN_HWEEN_MASK,
        VETERAN_PARTYHAT,
        VETERAN_SANTA_HAT,
        BLOOD_FIREBIRD,
        RING_OF_VIGOUR,
        26473,
        26496,
        26477,
        26475,
        26496,
        26471,
        26467,
        30315,
        30312,
        30309,
        26170,
        26172,
        26180,
        ELITE_VOID_TOP_24943,
        ELITE_VOID_ROBE_24942,
        VOID_MELEE_HELM_24941,
        VOID_MAGE_HELM_24940,
        VOID_RANGER_HELM_24939,
        VOID_KNIGHT_GLOVES_24938,
        RUNE_POUCH_I_BROKEN,
        AMULET_OF_FURY_OR_BROKEN,
        OCCULT_NECKLACE_OR_BROKEN,
        AMULET_OF_TORTURE_OR_BROKEN,
        NECKLACE_OF_ANGUISH_OR_BROKEN,
        TORMENTED_BRACELET_OR_BROKEN,
        DRAGON_DEFENDER_T_BROKEN,
        DRAGON_BOOTS_G_BROKEN,
        AVERNIC_DEFENDER,
        MAGMA_BLOWPIPE,
        ELDER_MAUL_21205,
        BRIMSTONE_RING,
        FARMING_CAPE,
        FARMING_CAPET,
        FARMING_HOOD,
        WOODCUTTING_CAPE,
        WOODCUT_CAPET,
        WOODCUTTING_HOOD,
        FIREMAKING_CAPE,
        FIREMAKING_CAPET,
        FIREMAKING_HOOD,
        COOKING_CAPE,
        COOKING_CAPET,
        COOKING_HOOD,
        FISHING_CAPE,
        FISHING_CAPET,
        FISHING_HOOD,
        SMITHING_CAPE,
        SMITHING_CAPET,
        SMITHING_HOOD,
        MINING_CAPE,
        MINING_CAPET,
        MINING_HOOD,
        AGILITY_CAPE,
        AGILITY_CAPET,
        AGILITY_HOOD,
        HERBLORE_CAPE,
        HERBLORE_CAPET,
        HERBLORE_HOOD,
        THIEVING_CAPE,
        THIEVING_CAPET,
        THIEVING_HOOD,
        CRAFTING_CAPE,
        CRAFTING_CAPET,
        CRAFTING_HOOD,
        FLETCHING_CAPE,
        FLETCHING_CAPET,
        FLETCHING_HOOD,
        SLAYER_CAPE,
        SLAYER_CAPET,
        SLAYER_HOOD,
        HUNTER_CAPE,
        HUNTER_CAPET,
        HUNTER_HOOD,
        CONSTRUCT_CAPE,
        CONSTRUCT_CAPET,
        CONSTRUCT_HOOD,
        RUNECRAFT_CAPE,
        RUNECRAFT_CAPET,
        RUNECRAFT_HOOD,
        HITPOINTS_CAPE,
        HITPOINTS_CAPET,
        HITPOINTS_HOOD,
        MAGIC_CAPE,
        MAGIC_CAPET,
        MAGIC_HOOD,
        PRAYER_CAPE,
        PRAYER_CAPET,
        PRAYER_HOOD,
        RANGING_CAPE,
        RANGING_CAPET,
        RANGING_HOOD,
        DEFENCE_CAPE,
        DEFENCE_CAPET,
        DEFENCE_HOOD,
        STRENGTH_CAPE,
        STRENGTH_CAPET,
        STRENGTH_HOOD,
        ATTACK_CAPE,
        ATTACK_CAPET,
        ATTACK_HOOD,
        MUSIC_CAPE,
        MUSIC_CAPET,
        MUSIC_HOOD,
        QUEST_POINT_CAPE,
        QUEST_POINT_CAPE_T,
        QUEST_POINT_HOOD,
        ACHIEVEMENT_DIARY_CAPE,
        ACHIEVEMENT_DIARY_CAPE_T,
        ACHIEVEMENT_DIARY_HOOD,
        MAX_CAPE_13342, MAX_CAPE, MAX_HOOD, FIRE_MAX_CAPE, FIRE_MAX_HOOD, SARADOMIN_MAX_CAPE, SARADOMIN_MAX_HOOD, ZAMORAK_MAX_CAPE, ZAMORAK_MAX_HOOD, GUTHIX_MAX_CAPE, GUTHIX_MAX_HOOD, ACCUMULATOR_MAX_CAPE, ACCUMULATOR_MAX_HOOD,
        MYTHICAL_MAX_CAPE, MYTHICAL_MAX_HOOD, ARDOUGNE_MAX_CAPE, ARDOUGNE_MAX_HOOD, INFERNAL_MAX_CAPE, INFERNAL_MAX_CAPE_21285, INFERNAL_MAX_HOOD, IMBUED_SARADOMIN_MAX_CAPE, IMBUED_SARADOMIN_MAX_HOOD, IMBUED_ZAMORAK_MAX_CAPE, IMBUED_ZAMORAK_MAX_HOOD,
        IMBUED_GUTHIX_MAX_CAPE, IMBUED_GUTHIX_MAX_HOOD, ASSEMBLER_MAX_CAPE, ASSEMBLER_MAX_HOOD,
        IMBUED_SARADOMIN_CAPE, IMBUED_GUTHIX_CAPE, IMBUED_ZAMORAK_CAPE, FROST_IMBUED_MAX_CAPE, FROST_IMBUED_MAX_HOOD,
        SARADOMIN_CAPE,
        ZAMORAK_CAPE,
        GUTHIX_CAPE,
        MYTHICAL_CAPE_22114,
        ARDOUGNE_CLOAK_4,
        MITHRIL_GLOVES,
        ADAMANT_GLOVES,
        BARROWS_GLOVES,
        BLACK_DEFENDER,
        MITHRIL_DEFENDER,
        ADAMANT_DEFENDER,
        RUNE_DEFENDER,
        DRAGON_DEFENDER,
        FIRE_CAPE,
        VOID_MAGE_HELM,
        VOID_RANGER_HELM,
        VOID_MELEE_HELM,
        VOID_KNIGHT_TOP,
        VOID_KNIGHT_ROBE,
        VOID_KNIGHT_GLOVES,
        ELITE_VOID_ROBE,
        ELITE_VOID_TOP,
        30099,
        30098,
        HEALER_HAT,
        FIGHTER_HAT,
        RUNNER_HAT,
        RANGER_HAT,
        FIGHTER_TORSO,
        RUNNER_BOOTS,
        PENANCE_SKIRT,
        ARDOUGNE_CLOAK_4,
        FEROCIOUS_GLOVES,
        AVERNIC_DEFENDER,
        ROYAL_SEED_POD,
        AVAS_ASSEMBLER,
        VENGEANCE_SKULL,
        INFERNAL_CAPE,
        PET_KREE_ARRA_WHITE,
        PET_ZILYANA_WHITE,
        PET_GENERAL_GRAARDOR_BLACK,
        PET_KRIL_TSUTSAROTH_BLACK,
        DRAGON_DEFENDER_T,
        DRAGON_BOOTS_G,
        RUNE_POUCH_I,
        SLAYER_HELMET_I,
        GREEN_SLAYER_HELMET_I,
        TURQUOISE_SLAYER_HELMET_I,
        RED_SLAYER_HELMET_I,
        BLACK_SLAYER_HELMET_I,
        TWISTED_SLAYER_HELMET_I,
        TWISTED_SLAYER_HELMET_I_KBD_HEADS,
        TWISTED_SLAYER_HELMET_I_JAD,
        TWISTED_SLAYER_HELMET_I_INFERNAL_CAPE,
        TWISTED_SLAYER_HELMET_I_VAMP_DUST,
        PURPLE_SLAYER_HELMET_I,
        HYDRA_SLAYER_HELMET_I,
        DOUBLE_DROPS_LAMP,
        RING_OF_MANHUNTING,
        HAZY_COINS,
        ATTACKER_ICON,
        COLLECTOR_ICON,
        DEFENDER_ICON,
        HEALER_ICON,
        DARK_ELDER_MAUL_UNTRADEABLE,
        MAGIC_SHORTBOW_I,
    };

    // Variants of crystal bow, shield, halberd, (i)
    // Note this does NOT INCLUDE 4212 ('new' bow) or 4224 ('new' shield)
    // Those are un-used crystal items, which drop as themselves.. they can be noted!
    // The halberd is a quest item and has no noted form.
    public static boolean isCrystal(int id) {
        return (id >= 4214 && id <= 4223) || (id >= 4225 && id <= 4234)
            || (id >= 11748 && id <= 11758)
            || (id >= 11759 && id <= 11769)
            || (id >= 13080 && id <= 13101);
    }

    public boolean skillcape() {
        return id >= 9747 && id <= 9814;
    }

    /**
     * Gets the value for this item.
     *
     * @return the value of this item.
     */
    public int getValue() {
        final ItemDefinition def = definition(World.getWorld());
        if (def == null) return 0;
        if (def.pvpAllowed) return 0;
        return TradingPost.TRADING_POST_VALUE_ENABLED ? TradingPost.getProtectionPrice(id) : getBloodMoneyPrice() == null ? 0 : getBloodMoneyPrice().value();
    }

    /**
     * Checks if this item is valid or not.
     *
     * @return
     */
    public boolean validate() {
        return id >= 0 && amount >= 0;
    }

    public boolean matchesId(int id) {
        return this.id == id;
    }

    @Override
    public Item clone() {
        return new Item(id, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item))
            return false;
        Item item = (Item) o;
        return item.getId() == this.getId()
            && item.getAmount() == this.getAmount();
    }

    public String toLongString() {
        return "Item{" +
            "id=" + id +
            ", name=" + name() +
            ", amount=" + amount +
            ", value=" + getValue() +
            ", noted=" + noted() + "}";
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + id +
            ", name=" + name() +
            ", amount=" + amount +
            ", noted=" + noted() +
            '}';
    }

    public String toShortString() {
        return name() + (getAmount() == 1 ? "" : " x" + getAmount()) + " id=" + getId() + "";
    }

    public String toShortValueString() {
        return name() + (getAmount() == 1 ? "" : " x" + getAmount()) +
            (getValue() == 0 ? "" :
                getAmount() == 1 ? " " + getValue() + "ea" :
                    " " + (long) getValue() * amount + "@" + getValue() + "ea");
    }

    public boolean equalIds(Item other) {
        return other != null && id == other.id;
    }

    public int getSellValue() {
        return (int) (getValue() * 0.35);
    }
}
