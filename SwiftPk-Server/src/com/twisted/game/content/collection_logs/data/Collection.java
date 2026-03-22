package com.twisted.game.content.collection_logs.data;

import com.twisted.game.content.wilderness.keys.WildernessKeys;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.items.Item;
import com.twisted.util.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.twisted.game.content.collection_logs.CollectionLog.RAIDS_KEY;
import static com.twisted.game.content.collection_logs.CollectionLog.TOB_RAIDS_KEY;
import static com.twisted.util.CustomItemIdentifiers.BABY_LAVA_DRAGON;
import static com.twisted.util.CustomItemIdentifiers.BARRELCHEST_PET;
import static com.twisted.util.CustomItemIdentifiers.DRAGON_CLAWS_OR;
import static com.twisted.util.CustomItemIdentifiers.HOLY_GHRAZI_RAPIER;
import static com.twisted.util.CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR;
import static com.twisted.util.CustomItemIdentifiers.NIFFLER;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.CustomNpcIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.DRAGON_HUNTER_CROSSBOW_T;
import static com.twisted.util.ItemIdentifiers.HOLY_SANGUINESTI_STAFF;
import static com.twisted.util.NpcIdentifiers.*;

/**
 * @author PVE
 * @Since juli 15, 2020
 */
public enum Collection {
    // bosses
    BARRELCHEST(AttributeKey.BARRELCHESTS_KILLED, LogType.BOSSES, "Barrelchests", new int[]{NpcIdentifiers.BARRELCHEST_6342}, AttributeKey.BARRELCHEST_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX),new Item(FIVE_DOLLAR_BOND)},
        //Drops
        new Item(BARRELCHEST_PET),
        new Item(ANCIENT_WARRIOR_SWORD), new Item(ANCIENT_WARRIOR_AXE), new Item(ANCIENT_WARRIOR_MAUL), new Item(KEY_OF_BOXES)),

    CALLISTO(AttributeKey.CALLISTOS_KILLED, LogType.BOSSES, "Callisto", new int[]{NpcIdentifiers.CALLISTO, NpcIdentifiers.CALLISTO_6609}, AttributeKey.CALLISTO_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 3)},
        //Drops
        new Item(ItemIdentifiers.CALLISTO_CUB),
        new Item(DRAGON_PICKAXE), new Item(TYRANNICAL_RING), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(KEY_OF_BOXES)),

    CERBERUS(AttributeKey.CERBERUS_KILLED, LogType.BOSSES, "Cerberus", new int[]{NpcIdentifiers.CERBERUS}, AttributeKey.CERBERUS_LOG_CLAIMED, new Item[]{new Item(PRIMORDIAL_BOOTS_OR), new Item(PEGASIAN_BOOTS_OR), new Item(ETERNAL_BOOTS_OR)},
        //Drops
        new Item(ItemIdentifiers.HELLPUPPY),
        new Item(PRIMORDIAL_CRYSTAL), new Item(PEGASIAN_CRYSTAL), new Item(ETERNAL_CRYSTAL), new Item(SMOULDERING_STONE), new Item(JAR_OF_SOULS)),

    CHAOS_ELEMENTAL(AttributeKey.CHAOS_ELEMENTALS_KILLED, LogType.BOSSES, "Chaos Elemental", new int[]{NpcIdentifiers.CHAOS_ELEMENTAL}, AttributeKey.CHAOS_ELEMENTAL_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX)},
        //Drops
        new Item(ItemIdentifiers.PET_CHAOS_ELEMENTAL),
        new Item(DRAGON_2H_SWORD), new Item(DRAGON_PICKAXE), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(KEY_OF_BOXES), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(ELEMENTAL_BOW)),

    CHAOS_FANATIC(AttributeKey.CHAOS_FANATICS_KILLED, LogType.BOSSES, "Chaos Fanatic", new int[]{NpcIdentifiers.CHAOS_FANATIC}, AttributeKey.CHAOS_FANATIC_LOG_CLAIMED, new Item[]{new Item(BLOOD_MONEY_CASKET, 2)},
        //Drops
        new Item(ItemIdentifiers.PET_CHAOS_ELEMENTAL),
        new Item(ODIUM_WARD_12807), new Item(MALEDICTION_WARD_12806), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX)),

    CORPOREAL_BEAST(AttributeKey.CORPOREAL_BEASTS_KILLED, LogType.BOSSES, "Corporeal Beast", new int[]{NpcIdentifiers.CORPOREAL_BEAST}, AttributeKey.CORPOREAL_BEAST_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 3), new Item(CustomItemIdentifiers.MYSTERY_TICKET, 2), new Item(FIVE_DOLLAR_BOND)},
        //Drops
        new Item(ItemIdentifiers.PET_DARK_CORE),
        new Item(ELYSIAN_SPIRIT_SHIELD), new Item(SPECTRAL_SPIRIT_SHIELD), new Item(ARCANE_SPIRIT_SHIELD), new Item(HOLY_ELIXIR)),

    CRAZY_ARCHAEOLOGIST(AttributeKey.CRAZY_ARCHAEOLOGISTS_KILLED, LogType.BOSSES, "Crazy Archaeologist", new int[]{NpcIdentifiers.CRAZY_ARCHAEOLOGIST}, AttributeKey.CRAZY_ARCHAEOLOGIST_LOG_CLAIMED, new Item[]{new Item(ODIUM_WARD), new Item(MALEDICTION_WARD)},
        //Drops
        new Item(ODIUM_SHARD_2), new Item(MALEDICTION_SHARD_2), new Item(FEDORA), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX)),

    DEMONIC_GORILLA(AttributeKey.DEMONIC_GORILLAS_KILLED, LogType.BOSSES, "Demonic Gorilla", new int[]{NpcIdentifiers.DEMONIC_GORILLA, NpcIdentifiers.DEMONIC_GORILLA_7145, NpcIdentifiers.DEMONIC_GORILLA_7146, NpcIdentifiers.DEMONIC_GORILLA_7147, NpcIdentifiers.DEMONIC_GORILLA_7148, NpcIdentifiers.DEMONIC_GORILLA_7149}, AttributeKey.DEMONIC_GORILLA_LOG_CLAIMED, new Item[]{new Item(AMULET_OF_TORTURE_OR), new Item(NECKLACE_OF_ANGUISH_OR), new Item(TORMENTED_BRACELET_OR)},
        //Drops
        new Item(DRAGON_JAVELIN), new Item(LIGHT_BALLISTA), new Item(HEAVY_BALLISTA), new Item(NECKLACE_OF_ANGUISH), new Item(TORMENTED_BRACELET), new Item(RING_OF_SUFFERING), new Item(AMULET_OF_TORTURE)),

    KING_BLACK_DRAGON(AttributeKey.KING_BLACK_DRAGONS_KILLED, LogType.BOSSES, "King Black Dragon", new int[]{NpcIdentifiers.KING_BLACK_DRAGON}, AttributeKey.KING_BLACK_DRAGON_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 3), new Item(DRAGONFIRE_SHIELD, 2)},
        //Drops
        new Item(ItemIdentifiers.PRINCE_BLACK_DRAGON),
        new Item(KBD_HEADS), new Item(DRAGON_PICKAXE), new Item(DRAGONFIRE_SHIELD), new Item(ARMADYL_GODSWORD), new Item(BANDOS_GODSWORD), new Item(SARADOMIN_GODSWORD), new Item(ZAMORAK_GODSWORD), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(KEY_OF_BOXES)),

    KRAKEN(AttributeKey.KRAKENS_KILLED, LogType.BOSSES, "Kraken", new int[]{NpcIdentifiers.KRAKEN}, AttributeKey.KRAKEN_LOG_CLAIMED, new Item[]{new Item(ANCIENT_WYVERN_SHIELD), new Item(DRAGONFIRE_WARD)},
        //Drops
        new Item(ItemIdentifiers.PET_KRAKEN),
        new Item(ABYSSAL_TENTACLE), new Item(TRIDENT_OF_THE_SEAS), new Item(JAR_OF_DIRT)),

    LAVA_DRAGON(AttributeKey.LAVA_DRAGONS_KILLED, LogType.BOSSES, "Lava Dragon", new int[]{NpcIdentifiers.LAVA_DRAGON}, AttributeKey.LAVA_DRAGON_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 3)},
        //Drops
        new Item(DRAGONFIRE_SHIELD), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(KEY_OF_BOXES)),

    LIZARDMAN_SHAMAN(AttributeKey.LIZARDMAN_SHAMANS_KILLED, LogType.BOSSES, "Lizardman Shaman", new int[]{NpcIdentifiers.LIZARDMAN_SHAMAN, NpcIdentifiers.LIZARDMAN_SHAMAN_6767}, AttributeKey.LIZARDMAN_SHAMAN_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX, 5)},
        //Drops
        new Item(DRAGON_WARHAMMER)),

    SCORPIA(AttributeKey.SCORPIAS_KILLED, LogType.BOSSES, "Scorpia", new int[]{NpcIdentifiers.SCORPIA}, AttributeKey.SCORPIA_LOG_CLAIMED, new Item[]{new Item(OCCULT_NECKLACE_OR), new Item(CustomItemIdentifiers.MYSTERY_TICKET)},
        //Drops
        new Item(ItemIdentifiers.SCORPIAS_OFFSPRING),
        new Item(ODIUM_WARD_12807), new Item(MALEDICTION_WARD_12806), new Item(OCCULT_NECKLACE)),

    THERMONUCLEAR_SMOKE_DEVIL(AttributeKey.THERMONUCLEAR_SMOKE_DEVILS_KILLED, LogType.BOSSES, "Thermonuclear Smoke Devil", new int[]{NpcIdentifiers.THERMONUCLEAR_SMOKE_DEVIL}, AttributeKey.THERMONUCLEAR_SMOKE_DEVIL_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX, 3)},
        //Drops
        new Item(ItemIdentifiers.PET_SMOKE_DEVIL),
        new Item(MYSTERY_BOX)),

    TZTOK_JAD(AttributeKey.JADS_KILLED, LogType.BOSSES, "Tztok-Jad", new int[]{NpcIdentifiers.TZTOKJAD}, AttributeKey.TZTOK_JAD_LOG_CLAIMED, new Item[]{new Item(DRAGON_CLAWS), new Item(FIVE_DOLLAR_BOND)},
        //Drops
        new Item(ItemIdentifiers.TZREKJAD),
        new Item(DRAGON_CLAWS), new Item(INFERNAL_CAPE)),

    VENENATIS(AttributeKey.VENENATIS_KILLED, LogType.BOSSES, "Venenatis", new int[]{NpcIdentifiers.VENENATIS, NpcIdentifiers.VENENATIS_6610}, AttributeKey.VENENATIS_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 1)},
        //Drops
        new Item(ItemIdentifiers.VENENATIS_SPIDERLING),
        new Item(TREASONOUS_RING), new Item(DRAGON_PICKAXE), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(KEY_OF_BOXES)),

    VETION(AttributeKey.VETIONS_KILLED, LogType.BOSSES, "Vet'ion", new int[]{NpcIdentifiers.VETION_REBORN}, AttributeKey.VETION_LOG_CLAIMED, new Item[]{new Item(KEY_OF_BOXES)},
        //Drops
        new Item(VETION_JR_13180),
        new Item(DRAGON_PICKAXE), new Item(RING_OF_THE_GODS), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(KEY_OF_BOXES)),

    VORKATH(AttributeKey.VORKATHS_KILLED, LogType.BOSSES, "Vorkath", new int[]{NpcIdentifiers.VORKATH}, AttributeKey.VORKATH_LOG_CLAIMED, new Item[]{new Item(VORKATHS_HEAD_21907), new Item(DRAGON_HUNTER_CROSSBOW), new Item(FIVE_DOLLAR_BOND)},
        //Drops
        new Item(ItemIdentifiers.VORKI),
        new Item(VORKATHS_HEAD), new Item(DRAGONFIRE_SHIELD), new Item(DRAGON_CROSSBOW), new Item(DRAGONFIRE_WARD), new Item(DRAGON_HUNTER_CROSSBOW), new Item(JAR_OF_DECAY), new Item(DRAGONBONE_NECKLACE)),

    TEKTON(AttributeKey.TEKTONS_KILLED, LogType.BOSSES, "Tekton", new int[]{NpcIdentifiers.TEKTON_7542}, AttributeKey.TEKTON_LOG_CLAIMED, new Item[]{new Item(ELDER_MAUL)},
        //Drops
        new Item(ItemIdentifiers.TEKTINY),
        new Item(DRAGON_SWORD), new Item(DRAGON_HARPOON), new Item(DINHS_BULWARK), new Item(KODAI_WAND), new Item(ELDER_MAUL), new Item(ANCESTRAL_HAT), new Item(ANCESTRAL_ROBE_TOP), new Item(ANCESTRAL_ROBE_BOTTOM), new Item(GHRAZI_RAPIER)),

    SKOTIZO(AttributeKey.SKOTIZOS_KILLED, LogType.BOSSES, "Skotizo", new int[]{NpcIdentifiers.SKOTIZO}, AttributeKey.SKOTIZO_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(FIVE_DOLLAR_BOND)},
        //Dropscustom
        new Item(ItemIdentifiers.SKOTOS),
        new Item(DARK_CLAW), new Item(JAR_OF_DARKNESS), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(JUSTICIAR_FACEGUARD), new Item(JUSTICIAR_CHESTGUARD), new Item(JUSTICIAR_LEGGUARDS)),

    ZOMBIES_CHAMPION(AttributeKey.ZOMBIES_CHAMPIONS_KILLED, LogType.BOSSES, "Zombies Champion", new int[]{NpcIdentifiers.ZOMBIES_CHAMPION}, AttributeKey.ZOMBIES_CHAMPION_LOG_CLAIMED, new Item[]{new Item(VESTAS_LONGSWORD), new Item(STATIUSS_WARHAMMER), new Item(ZURIELS_STAFF), new Item(FIVE_DOLLAR_BOND)},
        //Drops
        new Item(PET_ZOMBIES_CHAMPION),
        new Item(ARMADYL_CROSSBOW), new Item(ARMADYL_GODSWORD), new Item(VESTAS_LONGSWORD), new Item(VESTAS_CHAINBODY), new Item(VESTAS_PLATESKIRT), new Item(STATIUSS_WARHAMMER), new Item(STATIUSS_FULL_HELM), new Item(STATIUSS_PLATEBODY), new Item(STATIUSS_PLATELEGS), new Item(MORRIGANS_COIF), new Item(MORRIGANS_LEATHER_BODY), new Item(MORRIGANS_LEATHER_CHAPS), new Item(ZURIELS_STAFF), new Item(ZURIELS_HOOD), new Item(ZURIELS_ROBE_TOP), new Item(ZURIELS_ROBE_BOTTOM)),

    BRUTAL_LAVA_DRAGON(AttributeKey.BRUTAL_LAVA_DRAGONS_KILLED, LogType.BOSSES, "Brutal Lava Dragon", new int[]{BRUTAL_LAVA_DRAGON_FLYING}, AttributeKey.BRUTAL_LAVA_DRAGON_LOG_CLAIMED, new Item[]{new Item(LAVA_DHIDE_BODY), new Item(LAVA_DHIDE_CHAPS), new Item(LAVA_DHIDE_COIF), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX)},
        //Drops
        new Item(BABY_LAVA_DRAGON),
        new Item(DAGONHAI_HAT), new Item(DAGONHAI_ROBE_TOP), new Item(DAGONHAI_ROBE_BOTTOM), new Item(LAVA_DHIDE_COIF), new Item(LAVA_DHIDE_BODY), new Item(LAVA_DHIDE_CHAPS), new Item(LAVA_WHIP), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(KEY_OF_BOXES), new Item(LAVA_PARTYHAT), new Item(INFERNAL_CAPE), new Item(EPIC_PET_BOX)),

    ZULRAH(AttributeKey.ZULRAHS_KILLED, LogType.BOSSES, "Zulrah", new int[]{NpcIdentifiers.ZULRAH, NpcIdentifiers.ZULRAH_2044, NpcIdentifiers.ZULRAH_2043}, AttributeKey.ZULRAH_LOG_CLAIMED, new Item[]{new Item(KEY_OF_BOXES), new Item(MAGMA_BLOWPIPE), new Item(FIVE_DOLLAR_BOND)},
        //Drops
        new Item(PET_SNAKELING),
        new Item(TANZANITE_HELM), new Item(MAGMA_HELM), new Item(JAR_OF_SWAMP), new Item(TRIDENT_OF_THE_SWAMP), new Item(SERPENTINE_HELM), new Item(TOXIC_STAFF_OF_THE_DEAD), new Item(TOXIC_BLOWPIPE), new Item(ZULANDRA_TELEPORT), new Item(UNCUT_ONYX), new Item(ZULRAHS_SCALES)),

    ALCHEMICAL_HYDRA(AttributeKey.ALCHY_KILLED, LogType.BOSSES, "Alchemical Hydra", new int[]{NpcIdentifiers.ALCHEMICAL_HYDRA, NpcIdentifiers.ALCHEMICAL_HYDRA_8616, NpcIdentifiers.ALCHEMICAL_HYDRA_8617, NpcIdentifiers.ALCHEMICAL_HYDRA_8618, NpcIdentifiers.ALCHEMICAL_HYDRA_8619, NpcIdentifiers.ALCHEMICAL_HYDRA_8620, NpcIdentifiers.ALCHEMICAL_HYDRA_8621, NpcIdentifiers.ALCHEMICAL_HYDRA_8622}, AttributeKey.ALCHEMICAL_HYDRA_LOG_CLAIMED, new Item[]{new Item(KEY_OF_BOXES), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX),  new Item(FIVE_DOLLAR_BOND)},
        //Drops
        new Item(ItemIdentifiers.IKKLE_HYDRA),
        new Item(HYDRAS_CLAW), new Item(HYDRA_TAIL), new Item(FEROCIOUS_GLOVES), new Item(BRIMSTONE_RING), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(DRAGON_KNIFE), new Item(DRAGON_THROWNAXE), new Item(JAR_OF_CHEMICALS), new Item(ALCHEMICAL_HYDRA_HEADS)),

    GIANT_MOLE(AttributeKey.KC_GIANTMOLE, LogType.BOSSES, "Giant Mole", new int[]{NpcIdentifiers.GIANT_MOLE}, AttributeKey.GIANT_MOLE_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX)},
        //Drops
        new Item(NIFFLER),
        new Item(DRAGON_CLAWS), new Item(KEY_OF_BOXES)),

    CORRUPTED_NECHRYARCH(AttributeKey.CORRUPTED_NECHRYARCHS_KILLED, LogType.BOSSES, "Corrupted Nechryarch", new int[]{CustomNpcIdentifiers.CORRUPTED_NECHRYARCH}, AttributeKey.CORRUPTED_NECHRYARCH_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 3), new Item(KEY_OF_BOXES),  new Item(FIVE_DOLLAR_BOND)},
        //Drops
        new Item(CustomItemIdentifiers.PET_CORRUPTED_NECHRYARCH),
        new Item(CORRUPTED_BOOTS), new Item(EPIC_PET_BOX), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(ARMADYL_GODSWORD), new Item(DRAGON_CLAWS), new Item(KEY_OF_BOXES)),

    ANCIENT_CHAOS_ELEMENTAL(AttributeKey.ANCIENT_CHAOS_ELEMENTALS_KILLED, LogType.BOSSES, "Ancient Chaos Elemental", new int[]{CustomNpcIdentifiers.ANCIENT_CHAOS_ELEMENTAL}, AttributeKey.ANCIENT_CHAOS_ELEMENTAL_LOG_CLAIMED, new Item[]{new Item(KEY_OF_BOXES), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 3)},
        //Drops
        new Item(CustomItemIdentifiers.ANCIENT_CHAOS_ELEMENTAL_PET),
        new Item(ELEMENTAL_BOW), new Item(RING_OF_VIGOUR), new Item(BLOOD_MONEY_CASKET), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(KEY_OF_BOXES), new Item(EPIC_PET_BOX)
    ),

    ANCIENT_BARRELCHEST(AttributeKey.ANCIENT_BARRELCHESTS_KILLED, LogType.BOSSES, "Ancient Barrelchest", new int[]{CustomNpcIdentifiers.ANCIENT_BARRELCHEST}, AttributeKey.ANCIENT_BARRELCHEST_LOG_CLAIMED, new Item[]{new Item(ANCIENT_WARRIOR_CLAMP), new Item(CustomItemIdentifiers.MYSTERY_TICKET)},
        //Drops
        new Item(CustomItemIdentifiers.ANCIENT_BARRELCHEST_PET),
        new Item(BLOOD_MONEY_CASKET), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(KEY_OF_BOXES), new Item(EPIC_PET_BOX), new Item(ANCIENT_WARRIOR_CLAMP)
    ),
    NEX(AttributeKey.NEX_KILLED, LogType.BOSSES, "Nex", new int[]{11278}, AttributeKey.NEX_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.MYSTERY_TICKET), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(TEN_DOLLAR_BOND)},
        //Drops
        new Item(CustomItemIdentifiers.NEXLING),
        new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(EPIC_PET_BOX), new Item(ZARYTE_VAMBRACES), new Item(ANCIENT_GODSWORD) , new Item(ZARYTE_CROSSBOW), new Item(ItemIdentifiers.TORVA_FULL_HELM), new Item(ItemIdentifiers.TORVA_PLATEBODY), new Item(ItemIdentifiers.TORVA_PLATELEGS)
    ),

    ANCIENT_KING_BLACK_DRAGON(AttributeKey.ANCIENT_KING_BLACK_DRAGONS_KILLED, LogType.BOSSES, "Ancient King Black Dragon", new int[]{CustomNpcIdentifiers.ANCIENT_KING_BLACK_DRAGON}, AttributeKey.ANCIENT_KING_BLACK_DRAGON_LOG_CLAIMED, new Item[]{new Item(KEY_OF_BOXES), new Item(ANCIENT_FACEGAURD)},
        //Drops
        new Item(CustomItemIdentifiers.ANCIENT_KING_BLACK_DRAGON_PET),
        new Item(DRAGONFIRE_SHIELD), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(KEY_OF_BOXES), new Item(EPIC_PET_BOX), new Item(ANCIENT_FACEGAURD)
    ),

    KERBEROS(AttributeKey.KERBEROS_KILLED, LogType.BOSSES, "Kerberos", new int[]{CustomNpcIdentifiers.KERBEROS}, AttributeKey.KERBEROS_LOG_CLAIMED, new Item[]{new Item(SAELDOR_SHARD, 100),new Item(CustomItemIdentifiers.MYSTERY_TICKET, 2)},
        //Drops
        new Item(CustomItemIdentifiers.KERBEROS_PET),
        new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(SAELDOR_SHARD), new Item(LARRANS_KEY_TIER_III), new Item(VAMPYRE_DUST), new Item(KEY_OF_BOXES), new Item(EPIC_PET_BOX), new Item(PHARAOHS_HILT)
    ),

    SKORPIOS(AttributeKey.SKORPIOS_KILLED, LogType.BOSSES, "Skorpios", new int[]{CustomNpcIdentifiers.SKORPIOS}, AttributeKey.SKORPIOS_LOG_CLAIMED, new Item[]{new Item(SAELDOR_SHARD, 100), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 2)},
        //Drops
        new Item(CustomItemIdentifiers.SKORPIOS_PET),
        new Item(KEY_OF_BOXES), new Item(EPIC_PET_BOX), new Item(LARRANS_KEY_TIER_III), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(SAELDOR_SHARD), new Item(DARK_ARMADYL_HELMET)
    ),

    ARACHNE(AttributeKey.ARACHNE_KILLED, LogType.BOSSES, "Arachne", new int[]{CustomNpcIdentifiers.ARACHNE}, AttributeKey.ARACHNE_LOG_CLAIMED, new Item[]{new Item(SAELDOR_SHARD, 100), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 2)},
        //Drops
        new Item(CustomItemIdentifiers.ARACHNE_PET),
        new Item(KEY_OF_BOXES), new Item(EPIC_PET_BOX), new Item(LARRANS_KEY_TIER_III), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(SAELDOR_SHARD), new Item(DARK_ARMADYL_CHAINSKIRT)
    ),

    ARTIO(AttributeKey.ARTIO_KILLED, LogType.BOSSES, "Artio", new int[]{CustomNpcIdentifiers.ARTIO}, AttributeKey.ARTIO_LOG_CLAIMED, new Item[]{new Item(SAELDOR_SHARD, 100), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 2)},
        //Drops
        new Item(CustomItemIdentifiers.ARTIO_PET),
        new Item(KEY_OF_BOXES), new Item(EPIC_PET_BOX), new Item(LARRANS_KEY_TIER_III), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(SAELDOR_SHARD), new Item(DARK_ARMADYL_CHESTPLATE)
    ),
//serenz
    SEREN(AttributeKey.SEREN_KILLED, LogType.BOSSES, "Seren", new int[]{NpcIdentifiers.FRAGMENT_OF_SEREN,8928}, AttributeKey.SEREN_LOG_CLAIMED, new Item[]{new Item(CRYSTAL_HELM), new Item(CRYSTAL_BODY), new Item(CRYSTAL_LEGS), new Item(CORRUPTING_STONE), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(FIVE_DOLLAR_BOND)},
        //DropsOX
        new Item(CustomItemIdentifiers.SERENIC),
        new Item(BLOOD_MONEY_CASKET), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(SAELDOR_SHARD), new Item(KEY_OF_BOXES), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(ANTIQUE_EMBLEM_TIER_10), new Item(CRYSTAL_HELM), new Item(CRYSTAL_BODY), new Item(CRYSTAL_LEGS), new Item(CORRUPTING_STONE), new Item(VOIDWAKER_27690)
    ),


    THE_NIGHTMARE(AttributeKey.THE_NIGHTMARE_KC, LogType.BOSSES, "The nightmare", new int[]{THE_NIGHTMARE_9430}, AttributeKey.THE_NIGTHMARE_LOG_CLAIMED, new Item[]{new Item(SHADOW_INQUISITOR_ORNAMENT_KIT), new Item(INQUISITORS_MACE_ORNAMENT_KIT), new Item(CustomItemIdentifiers.MYSTERY_TICKET),  new Item(FIVE_DOLLAR_BOND)},
        //Drops
        new Item(ItemIdentifiers.LITTLE_NIGHTMARE),
        new Item(ItemIdentifiers.CRYSTAL_KEY), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(BLOOD_MONEY_CASKET), new Item(ABYSSAL_BLUDGEON), new Item(ARMADYL_GODSWORD), new Item(DRAGON_CLAWS), new Item(DRAGON_WARHAMMER), new Item(KEY_OF_BOXES), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(EPIC_PET_BOX), new Item(INQUISITORS_MACE), new Item(INQUISITORS_GREAT_HELM), new Item(INQUISITORS_HAUBERK), new Item(INQUISITORS_PLATESKIRT), new Item(NIGHTMARE_STAFF), new Item(ELDRITCH_ORB), new Item(HARMONISED_ORB), new Item(VOLATILE_ORB), new Item(SHADOW_INQUISITOR_ORNAMENT_KIT), new Item(INQUISITORS_MACE_ORNAMENT_KIT)),

    CORRUPTED_HUNLEFF(AttributeKey.CORRUPTED_HUNLEFFS_KILLED, LogType.BOSSES, "Corrupted Hunleff", new int[]{NpcIdentifiers.CORRUPTED_HUNLLEF, NpcIdentifiers.CORRUPTED_HUNLLEF_9036, NpcIdentifiers.CORRUPTED_HUNLLEF_9037}, AttributeKey.CORRUPTED_HUNLEFF_LOG_CLAIMED, new Item[]{new Item(KEY_OF_BOXES, 2), new Item(CustomItemIdentifiers.MYSTERY_TICKET), new Item(FIVE_DOLLAR_BOND)},
        //Drops
        new Item(ItemIdentifiers.YOUNGLLEF),
        new Item(CORRUPTED_RANGER_GAUNTLETS), new Item(CRYSTAL_HELM), new Item(CRYSTAL_BODY), new Item(CRYSTAL_LEGS), new Item(BLOOD_MONEY_CASKET), new Item(BANDOS_GODSWORD_OR), new Item(SARADOMIN_GODSWORD_OR), new Item(ZAMORAK_GODSWORD_OR), new Item(EPIC_PET_BOX), new Item(KEY_OF_BOXES), new Item(CORRUPTING_STONE)

    ),
 //       ),
 //   AFK_STALL(AttributeKey.AFKSTALL_REWARD_CLAIMED_OPENED, LogType.OTHER, "Afk Stall", new int[]{ObjectIdentifiers.TEA_STALL_6574}, AttributeKey.AFK_STALL_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(KEY_OF_BOXES)},
        //new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX),
//        new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX)//, new Item(FIVE_DOLLAR_BOND)

    // mboxes
    DONATOR_MYSTERY_BOX(AttributeKey.DONATOR_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Donator mystery box", new int[]{CustomItemIdentifiers.DONATOR_MYSTERY_BOX}, AttributeKey.DONATOR_MYSTERY_BOX_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX, 5)},
        new Item(BLACK_ANKOU_MASK),
        new Item(BLACK_ANKOU_TOP),
        new Item(BLACK_ANKOUS_LEGGINGS),
        new Item(BLACK_ANKOU_GLOVES),
        new Item(BLACK_ANKOU_SOCKS),
        new Item(_3RD_AGE_DRUIDIC_CLOAK),
        new Item(_3RD_AGE_DRUIDIC_ROBE_TOP),
        new Item(_3RD_AGE_DRUIDIC_ROBE_BOTTOMS),
        new Item(_3RD_AGE_DRUIDIC_STAFF),
        new Item(RAINBOW_PARTYHAT),
        new Item(BLACK_PARTYHAT),
        new Item(BLACK_SANTA_HAT),
        new Item(BLACK_HWEEN_MASK),
        new Item(INVERTED_SANTA_HAT),
        new Item(PARTYHAT__SPECS),
        new Item(ANTISANTA_MASK),
        new Item(ANTISANTA_JACKET),
        new Item(ANTISANTA_PANTALOONS),
        new Item(ANTISANTA_GLOVES),
        new Item(ANTISANTA_BOOTS),
        new Item(_3RD_AGE_LONGSWORD),
        new Item(_3RD_AGE_BOW),
        new Item(_3RD_AGE_WAND),
        new Item(_3RD_AGE_PICKAXE),
        new Item(_3RD_AGE_FULL_HELMET),
        new Item(_3RD_AGE_PLATEBODY),
        new Item(_3RD_AGE_PLATELEGS),
        new Item(_3RD_AGE_MAGE_HAT),
        new Item(_3RD_AGE_ROBE_TOP),
        new Item(_3RD_AGE_ROBE),
        new Item(_3RD_AGE_RANGE_COIF),
        new Item(_3RD_AGE_RANGE_TOP),
        new Item(_3RD_AGE_RANGE_LEGS),
        new Item(_3RD_AGE_VAMBRACES),
        new Item(FANCY_BOOTS),
        new Item(FIGHTING_BOOTS),
        new Item(YELLOW_PARTYHAT),
        new Item(RED_PARTYHAT),
        new Item(BLUE_PARTYHAT),
        new Item(GREEN_PARTYHAT),
        new Item(WHITE_PARTYHAT),
        new Item(PURPLE_PARTYHAT),
        new Item(GREEN_HALLOWEEN_MASK),
        new Item(RED_HALLOWEEN_MASK),
        new Item(BLUE_HALLOWEEN_MASK),
        new Item(SANTA_HAT),
        new Item(4084),
        new Item(EASTER_BASKET),
        new Item(CAPE_OF_SKULLS),
        new Item(SANTA_MASK),
        new Item(SANTA_JACKET),
        new Item(SANTA_PANTALOONS),
        new Item(SANTA_GLOVES),
        new Item(SANTA_BOOTS),
        new Item(FLIPPERS),
        new Item(GOLDEN_CHEFS_HAT),
        new Item(GOLDEN_APRON),
        new Item(BUCKET_HELM_G),
        new Item(GILDED_DHIDE_VAMBRACES),
        new Item(GILDED_DHIDE_BODY),
        new Item(GILDED_DHIDE_CHAPS),
        new Item(GILDED_2H_SWORD),
        new Item(GILDED_SCIMITAR),
        new Item(GILDED_HASTA),
        new Item(GILDED_SQ_SHIELD),
        new Item(GILDED_AXE),
        new Item(GILDED_PICKAXE),
        new Item(SCYTHE),
        new Item(GNOME_SCARF),
        new Item(RAINBOW_SCARF),
        new Item(HEAVY_CASKET),
        new Item(HAM_JOINT)
    ),
    BONDS_CASKET(AttributeKey.BONDS_CASKET_OPENED, LogType.MYSTERY_BOX, "Bond's Casket", new int[]{CustomItemIdentifiers.BONDS_CASKET}, AttributeKey.BONDS_CASKET_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 2)},
        new Item(FIVE_DOLLAR_BOND),
        new Item(TEN_DOLLAR_BOND),
        new Item(TWENTY_DOLLAR_BOND),
        new Item(FORTY_DOLLAR_BOND),
        new Item(FIFTY_DOLLAR_BOND),
        new Item(ONE_HUNDRED_DOLLAR_BOND)

    ),
    ARMOUR_MYSTERY_BOX(AttributeKey.ARMOUR_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Armour mystery box", new int[]{CustomItemIdentifiers.ARMOUR_MYSTERY_BOX}, AttributeKey.ARMOUR_MYSTERY_BOX_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX, 5)},
        //new Item(JUSTICIAR_FACEGUARD),
        //new Item(JUSTICIAR_CHESTGUARD),
        //new Item(JUSTICIAR_LEGGUARDS),
        new Item(INQUISITORS_GREAT_HELM),
        new Item(INQUISITORS_HAUBERK),
        new Item(INQUISITORS_PLATESKIRT),
        new Item(DARK_INFINITY_HAT),
        new Item(DARK_INFINITY_TOP),
        new Item(DARK_INFINITY_BOTTOMS),
        new Item(LIGHT_INFINITY_HAT),
        new Item(LIGHT_INFINITY_TOP),
        new Item(LIGHT_INFINITY_BOTTOMS),
        new Item(ARMADYL_CHESTPLATE),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(BANDOS_CHESTPLATE),
        new Item(BANDOS_TASSETS),
        new Item(GUARDIAN_BOOTS),
        new Item(ARMADYL_HELMET),
        new Item(DAGONHAI_HAT),
        new Item(DAGONHAI_ROBE_TOP),
        new Item(DAGONHAI_ROBE_BOTTOM),
        new Item(DRAGON_FULL_HELM),
        new Item(DRAGON_PLATEBODY),
        new Item(GUTHANS_ARMOUR_SET),
        new Item(VERACS_ARMOUR_SET),
        new Item(TORAGS_ARMOUR_SET),
        new Item(KARILS_ARMOUR_SET),
        new Item(DHAROKS_ARMOUR_SET),
        new Item(DRAGON_DEFENDER),
        new Item(FIGHTER_TORSO),
        new Item(BLESSED_SPIRIT_SHIELD),
        new Item(ROBIN_HOOD_HAT),
        new Item(RANGER_BOOTS),
        new Item(DRAGON_BOOTS),
        new Item(INFINITY_BOOTS),
        new Item(INFINITY_HAT),
        new Item(INFINITY_TOP),
        new Item(INFINITY_BOTTOMS),
        new Item(MAGES_BOOK),
        new Item(OBSIDIAN_HELMET),
        new Item(OBSIDIAN_PLATEBODY),
        new Item(OBSIDIAN_PLATELEGS)
    ),

    WEAPON_MYSTERY_BOX(AttributeKey.WEAPON_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Weapon mystery box", new int[]{CustomItemIdentifiers.WEAPON_MYSTERY_BOX}, AttributeKey.WEAPON_MYSTERY_BOX_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX, 5)},
        new Item(INQUISITORS_MACE),
        new Item(VESTAS_LONGSWORD),
        new Item(STATIUSS_WARHAMMER),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(TOXIC_BLOWPIPE),
        new Item(ARMADYL_GODSWORD),
        new Item(ARMADYL_CROSSBOW),
        new Item(DRAGON_CLAWS),
        new Item(DRAGON_SWORD),
        new Item(DRAGON_WARHAMMER),
        new Item(ZURIELS_STAFF),
        new Item(VESTAS_SPEAR),
        new Item(HEAVY_BALLISTA),
        new Item(ZAMORAKIAN_HASTA),
        new Item(BLADE_OF_SAELDOR),
        new Item(ABYSSAL_DAGGER_P_13271),
        new Item(ABYSSAL_DAGGER),
        new Item(ZAMORAKIAN_SPEAR),
        new Item(BARRELCHEST_ANCHOR),
        new Item(SARADOMINS_BLESSED_SWORD),
        new Item(LIGHT_BALLISTA),
        new Item(DRAGON_CROSSBOW),
        new Item(STAFF_OF_LIGHT),
        new Item(STAFF_OF_THE_DEAD),
        new Item(STAFF_OF_BALANCE),
        new Item(BANDOS_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(ZAMORAK_GODSWORD),
        new Item(MASTER_WAND),
        new Item(GRANITE_MAUL_24225),
        new Item(DARK_BOW),
        new Item(ABYSSAL_TENTACLE),
        new Item(DRAGON_JAVELIN, 100)
    ),
    /*COX(AttributeKey.COX_KILLS, LogType.OTHER, "Cox", new int[]{TWISTED_BOW}, AttributeKey.COX_KILLS, new Item[]{new Item(-1, -1)},
        new Item(TWISTED_BOW)

    ),*/


    REVENANT_MYSTERY_BOX(AttributeKey.REVENANT_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Revenant mystery box", new int[]{REVENANT_MYSTER_BOX}, AttributeKey.REVENANT_MYSTERY_BOX_LOG_CLAIMED, new Item[]{new Item(REVENANT_MYSTER_BOX, 3)},
        new Item(VIGGORAS_CHAINMACE),
        new Item(CRAWS_BOW),
        new Item(THAMMARONS_SCEPTRE),
        new Item(AMULET_OF_AVARICE),
        new Item(STATIUSS_FULL_HELM),
        new Item(STATIUSS_PLATEBODY),
        new Item(STATIUSS_PLATELEGS),
        new Item(STATIUSS_WARHAMMER),
        new Item(VESTAS_CHAINBODY),
        new Item(VESTAS_PLATESKIRT),
        new Item(VESTAS_LONGSWORD),
        new Item(MORRIGANS_COIF),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_HOOD),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(ZURIELS_STAFF)
    ),


    ZENYTE_MYSTERY_BOX(AttributeKey.ZENYTE_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Zenyte mystery box", new int[]{CustomItemIdentifiers.ZENYTE_MYSTERY_BOX}, AttributeKey.ZENYTE_MYSTERY_BOX_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.ZENYTE_MYSTERY_BOX, 5)},
        new Item(CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR),
        new Item(SANGUINE_TWISTED_BOW),
        new Item(HOLY_SANGUINESTI_STAFF),
        new Item(DEMONIC_WINGS),
        new Item(DRAGON_COMPANION),
        new Item(ENCHANTED_SALAZAR_LOCKET),
        new Item(E_TOTEMIC_HELMET),
        new Item(E_TOTEMIC_PLATEBODY),
        new Item(E_TOTEMIC_PLATELEGS),
        new Item(SANGUINESTI_STAFF)
    ),

    MYSTERY_TICKET(AttributeKey.MYSTERY_TICKETS_OPENED, LogType.MYSTERY_BOX, "Mystery ticket", new int[]{CustomItemIdentifiers.MYSTERY_TICKET}, AttributeKey.MYSTERY_TICKET_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.MYSTERY_TICKET, 5)},
        new Item(CRAWS_BOW_C),
        new Item(THAMMARONS_STAFF_C),
        new Item(VIGGORAS_CHAINMACE_C),
        new Item(ARMADYL_GODSWORD_OR),
        new Item(DRAGON_CLAWS_OR),
        new Item(TOXIC_STAFF_OF_THE_DEAD_C),
        new Item(RING_OF_PRECISION),
        new Item(RING_OF_SORCERY),
        new Item(RING_OF_MANHUNTING),
        new Item(ANCIENT_FACEGAURD),
        new Item(AMULET_OF_TORTURE_OR),
        new Item(NECKLACE_OF_ANGUISH_OR),
        new Item(OCCULT_NECKLACE_OR),
        new Item(PEGASIAN_BOOTS_OR),
        new Item(PRIMORDIAL_BOOTS_OR),
        new Item(ETERNAL_BOOTS_OR),
        new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX),
        new Item(EPIC_PET_BOX),
        new Item(CustomItemIdentifiers.MYSTERY_CHEST)
    ),


    LEGENDARY_MYSTERY_BOX(AttributeKey.LEGENDARY_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Legendary mystery box", new int[]{CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX}, AttributeKey.LEGENDARY_MYSTERY_BOX_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 5)},
        new Item(ARCANE_SPIRIT_SHIELD),
        new Item(AVERNIC_DEFENDER),
        new Item(INFERNAL_CAPE),
        new Item(FEROCIOUS_GLOVES),
        new Item(HARMONISED_NIGHTMARE_STAFF),
        new Item(VOLATILE_NIGHTMARE_STAFF),
        new Item(SANGUINESTI_STAFF),
        new Item(DEXTEROUS_PRAYER_SCROLL),
        new Item(ARCANE_PRAYER_SCROLL),
        new Item(DONATOR_TICKET, 1000),
        new Item(CustomItemIdentifiers.MYSTERY_TICKET),
        new Item(LARRANS_KEY_TIER_III),
        new Item(ELDER_MAUL),
        new Item(INQUISITORS_GREAT_HELM),
        new Item(INQUISITORS_HAUBERK),
        new Item(INQUISITORS_PLATESKIRT),
        new Item(INQUISITORS_MACE),
        new Item(VESTAS_CHAINBODY),
        new Item(VESTAS_PLATESKIRT),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(STATIUSS_PLATEBODY),
        new Item(STATIUSS_PLATELEGS),
        new Item(LARRANS_KEY_TIER_II),
        new Item(STATIUSS_FULL_HELM),
        new Item(MORRIGANS_COIF),
        new Item(ZURIELS_HOOD),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TORMENTED_BRACELET),
        new Item(RING_OF_SUFFERING),
        new Item(SPECTRAL_SPIRIT_SHIELD),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(LARRANS_KEY_TIER_I),
        new Item(ItemIdentifiers.BLOOD_MONEY, 1000),
        new Item(DONATOR_TICKET, 250),
        new Item(VOTE_TICKET, 10),
        new Item(AMULET_OF_FURY, 10),
        new Item(OCCULT_NECKLACE, 10),
        new Item(DRAGON_BOOTS, 10)
    ),

    RAIDS_MYSTERY_BOX(AttributeKey.RAIDS_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Raids mystery box", new int[]{CustomItemIdentifiers.RAIDS_MYSTERY_BOX}, AttributeKey.RAIDS_MYSTERY_BOX_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.RAIDS_MYSTERY_BOX, 2)},
        new Item(TWISTED_BOW),
        new Item(SCYTHE_OF_VITUR),
        new Item(HOLY_GHRAZI_RAPIER),
        new Item(TOM_RIDDLE_DIARY),
        new Item(CLOAK_OF_INVISIBILITY),
        new Item(SWORD_OF_GRYFFINDOR),
        new Item(TWISTED_BOW_KIT),
        new Item(SHADOW_INQUISITOR_ORNAMENT_KIT),
        new Item(SANGUINESTI_STAFF),
        new Item(AVERNIC_DEFENDER),
        new Item(ELDER_MAUL),
        new Item(DRAGON_CLAWS),
        new Item(GHRAZI_RAPIER),
        new Item(ANCESTRAL_HAT),
        new Item(ANCESTRAL_ROBE_TOP),
        new Item(ANCESTRAL_ROBE_BOTTOM),
        new Item(KODAI_WAND)
    ),

    EPIC_PET_MYSTERY_BOX(AttributeKey.EPIC_PET_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Epic pet mystery box", new int[]{EPIC_PET_BOX}, AttributeKey.EPIC_PET_MYSTERY_BOX_LOG_CLAIMED, new Item[]{new Item(EPIC_PET_BOX, 3)},
        new Item(CustomItemIdentifiers.SNOWBIRD),
        new Item(CustomItemIdentifiers.ELVARG_JR),
        new Item(ItemIdentifiers.JALNIBREK),
        new Item(ItemIdentifiers.LITTLE_NIGHTMARE),
        new Item(CustomItemIdentifiers.RING_OF_ELYSIAN),
        new Item(CustomItemIdentifiers.BLOOD_MONEY_PET),
        new Item(CustomItemIdentifiers.KERBEROS_PET),
        new Item(CustomItemIdentifiers.SKORPIOS_PET),
        new Item(CustomItemIdentifiers.ARACHNE_PET),
        new Item(CustomItemIdentifiers.ARTIO_PET),
        new Item(CustomItemIdentifiers.JAWA_PET),
        new Item(CustomItemIdentifiers.DEMENTOR_PET),
        new Item(CustomItemIdentifiers.FENRIR_GREYBACK_JR),
        new Item(CustomItemIdentifiers.FLUFFY_JR),
        new Item(CustomItemIdentifiers.ANCIENT_KING_BLACK_DRAGON_PET),
        new Item(CustomItemIdentifiers.ANCIENT_CHAOS_ELEMENTAL_PET),
        new Item(CustomItemIdentifiers.ANCIENT_BARRELCHEST_PET),
        new Item(CustomItemIdentifiers.NIFFLER),
        new Item(CustomItemIdentifiers.BARRELCHEST_PET),
        new Item(CustomItemIdentifiers.WAMPA),
        new Item(CustomItemIdentifiers.BABY_ARAGOG),
        new Item(CustomItemIdentifiers.FOUNDER_IMP),
        new Item(CustomItemIdentifiers.CENTAUR_FEMALE),
        new Item(CustomItemIdentifiers.CENTAUR_MALE),
        new Item(CustomItemIdentifiers.BABY_LAVA_DRAGON),
        new Item(CustomItemIdentifiers.JALTOK_JAD),
        new Item(CustomItemIdentifiers.MINI_NECROMANCER),
        new Item(CustomItemIdentifiers.PET_CORRUPTED_NECHRYARCH),
        new Item(ItemIdentifiers.TZREKZUK),
        new Item(CustomItemIdentifiers.GRIM_REAPER_PET),
        new Item(CustomItemIdentifiers.GENIE_PET),
        new Item(CustomItemIdentifiers.DHAROK_PET),
        new Item(CustomItemIdentifiers.PET_ZOMBIES_CHAMPION),
        new Item(CustomItemIdentifiers.BABY_ABYSSAL_DEMON),
        new Item(CustomItemIdentifiers.BABY_DARK_BEAST_EGG),
        new Item(CustomItemIdentifiers.BABY_SQUIRT)
    ),

    MYSTERY_CHEST(AttributeKey.MYSTERY_CHESTS_OPENED, LogType.MYSTERY_BOX, "Mystery chest", new int[]{CustomItemIdentifiers.MYSTERY_CHEST}, AttributeKey.MYSTERY_CHEST_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.MYSTERY_CHEST, 2)},
        new Item(TWISTED_BOW_I), new Item(HOLY_SCYTHE_OF_VITUR),new Item(HOLY_SANGUINESTI_STAFF) ,new Item(ELYSIAN_SPIRIT_SHIELD), new Item(SHADOW_GREAT_HELM), new Item(SHADOW_HAUBERK), new Item(SHADOW_PLATESKIRT),
        new Item(ANCESTRAL_ROBE_BOTTOM_I), new Item(ANCESTRAL_ROBE_TOP_I), new Item(ANCESTRAL_HAT_I), new Item(SALAZAR_SLYTHERINS_LOCKET),
        new Item(CORRUPTED_BOOTS), new Item(RING_OF_TRINITY),
        new Item(CLOAK_OF_INVISIBILITY), new Item(TOM_RIDDLE_DIARY), new Item(DARK_ARMADYL_HELMET), new Item(DARK_ARMADYL_CHESTPLATE), new Item(DARK_ARMADYL_CHAINSKIRT),
        new Item(ELDER_WAND_HANDLE), new Item(ELDER_WAND_STICK), new Item(SWORD_OF_GRYFFINDOR), new Item(TALONHAWK_CROSSBOW), new Item(BLADE_OF_SAELDOR_8),
        new Item(DARK_BANDOS_CHESTPLATE), new Item(DARK_BANDOS_TASSETS), new Item(SHADOW_INQUISITOR_ORNAMENT_KIT)
    ),
    PRESENT(AttributeKey.PRESENTS_OPENED, LogType.MYSTERY_BOX, "Present", new int[]{PRESENT_13346}, AttributeKey.PRESENT_LOG_CLAIMED, new Item[]{new Item(PRESENT_13346, 3)},
        new Item(CustomItemIdentifiers.SNOWBIRD),
        new Item(CustomItemIdentifiers.ELDER_ICE_MAUL),
        new Item(DRAGON_HUNTER_CROSSBOW_T),
        new Item(FROST_IMBUED_CAPE),
        new Item(INFINITY_WINTER_BOOTS),
        new Item(CustomItemIdentifiers.MYSTERY_TICKET),
        new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX),
        new Item(SNOWY_SLED),
        new Item(UGLY_SANTA_HAT),
        new Item(ICED_SANTA_HAT),
        new Item(WISE_OLD_MANS_SANTA_HAT),
        new Item(ELDER_MAUL),
        new Item(FROST_CLAWS),
        new Item(ARMADYL_FROSTSWORD),
        new Item(ItemIdentifiers.BLACK_SANTA_HAT),
        new Item(ItemIdentifiers.INVERTED_SANTA_HAT),
        new Item(ItemIdentifiers.SANTA_HAT),
        new Item(ItemIdentifiers.CHRISTMAS_CRACKER),
        new Item(CustomItemIdentifiers.ABYSSAL_TENTACLE_24948),
        new Item(FROST_WHIP),
        new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX)
    ),

    MOLTEN_MYSTERY_BOX(AttributeKey.MOLTEN_MYSTERY_BOXES_OPENED, LogType.MYSTERY_BOX, "Molten mystery box", new int[]{CustomItemIdentifiers.MOLTEN_MYSTERY_BOX}, AttributeKey.MOLTEN_MYSTERY_BOX_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.MOLTEN_MYSTERY_BOX, 3)},
        new Item(MOLTEN_DEFENDER),
        new Item(CustomItemIdentifiers.LAVA_DHIDE_COIF),
        new Item(LAVA_DHIDE_BODY),
        new Item(LAVA_DHIDE_CHAPS),
        new Item(INFERNAL_CAPE),
        new Item(MOLTEN_PARTYHAT),
        new Item(LAVA_PARTYHAT),
        new Item(CustomItemIdentifiers.MYSTERY_TICKET),
        new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX),
        new Item(LAVA_WHIP),
        new Item(LAVA_DRAGON_MASK)
    ),

    // keys
    CRYSTAL_KEY(AttributeKey.CRYSTAL_KEYS_OPENED, LogType.KEYS, "Crystal key", new int[]{ItemIdentifiers.CRYSTAL_KEY}, AttributeKey.CRYSTAL_KEY_LOG_CLAIMED, new Item[]{new Item(ItemIdentifiers.CRYSTAL_KEY + 1, 10)},
        //Drops
        new Item(ItemIdentifiers.RING_OF_RECOIL + 1), new Item(ItemIdentifiers.GUTHIX_REST4 + 1), new Item(ItemIdentifiers.ANGLERFISH + 1), new Item(ItemIdentifiers.DRAGON_JAVELIN), new Item(ItemIdentifiers.DRAGON_KNIFE), new Item(ItemIdentifiers.DRAGON_DART), new Item(ItemIdentifiers.DRAGON_BOOTS + 1), new Item(ItemIdentifiers.STAMINA_POTION4 + 1), new Item(ItemIdentifiers.DARK_BOW), new Item(ItemIdentifiers.AMULET_OF_FURY), new Item(ItemIdentifiers.MAGES_BOOK), new Item(ItemIdentifiers.MASTER_WAND), new Item(ItemIdentifiers.DRAGONFIRE_SHIELD)),

    MOLTEN_KEY(AttributeKey.MOLTEN_KEYS_OPENED, LogType.KEYS, "Molten key", new int[]{CustomItemIdentifiers.MOLTEN_KEY}, AttributeKey.MOLTEN_KEY_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.MOLTEN_KEY, 5)},
        //Drops
        new Item(ItemIdentifiers.BLOOD_MONEY), new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET), new Item(ItemIdentifiers.DRAGONFIRE_SHIELD), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(CustomItemIdentifiers.PET_MYSTERY_BOX), new Item(DRAGON_BOOTS), new Item(ItemIdentifiers.DRAGON_CLAWS), new Item(CustomItemIdentifiers.LAVA_PARTYHAT), new Item(CustomItemIdentifiers.MOLTEN_PARTYHAT), new Item(CustomItemIdentifiers.MOLTEN_MYSTERY_BOX)),

    ENCHANTED_KEY_R(AttributeKey.ENCHANTED_KEYS_R_OPENED, LogType.KEYS, "Enchanted key (I)", new int[]{ENCHANTED_KEY_I}, AttributeKey.ENCHANTED_KEY_R_LOG_CLAIMED, new Item[]{new Item(ENCHANTED_KEY_I, 10)},
        new Item(ENCHANTED_BONES),
        new Item(BLOOD_MONEY_CASKET),
        new Item(BLESSED_SPIRIT_SHIELD),
        new Item(OCCULT_NECKLACE),
        new Item(MAGES_BOOK),
        new Item(SEERS_RING),
        new Item(MASTER_WAND),
        new Item(IMBUEMENT_SCROLL),
        new Item(TOME_OF_FIRE),
        new Item(DOUBLE_DROPS_LAMP)
    ),
    ENCHANTED_KEY_P(AttributeKey.ENCHANTED_KEYS_P_OPENED, LogType.KEYS, "Enchanted key (II)", new int[]{ENCHANTED_KEY_II}, AttributeKey.ENCHANTED_KEY_P_LOG_CLAIMED, new Item[]{new Item(ENCHANTED_KEY_II, 3)},
        new Item(ENCHANTED_KEY_I),
        new Item(CustomItemIdentifiers.MYSTERY_TICKET),
        new Item(ANATHEMA_WARD),
        new Item(DERANGED_MANIFESTO),
        new Item(RING_OF_DIVINATION),
        new Item(ANATHEMATIC_STONE)
    ),

    WILDERNESS_KEY(AttributeKey.WILDY_KEYS_OPENED, LogType.KEYS, "Wilderness key", new int[]{CustomItemIdentifiers.WILDERNESS_KEY}, AttributeKey.WILDERNESS_KEY_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.WILDERNESS_KEY, 5)},

        new Item(ANKOU_MASK),
        new Item(ANKOU_TOP),
        new Item(ANKOUS_LEGGINGS),
        new Item(ANKOU_GLOVES),
        new Item(ANKOU_SOCKS),
        new Item(NIGHTMARE_STAFF),
        new Item(VOLATILE_ORB),
        new Item(HARMONISED_ORB),
        new Item(ELDRITCH_ORB),
        new Item(PRIMORDIAL_BOOTS_OR),
        new Item(PEGASIAN_BOOTS_OR),
        new Item(ETERNAL_BOOTS_OR),
        new Item(KODAI_WAND),
        new Item(ELDER_MAUL),
        new Item(GHRAZI_RAPIER),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(AMULET_OF_TORTURE),
        new Item(ARMADYL_FROSTSWORD),
        new Item(VESTAS_CHAINBODY),
        new Item(VESTAS_PLATESKIRT),
        new Item(STATIUSS_PLATEBODY),
        new Item(STATIUSS_PLATELEGS),
        new Item(ZURIELS_ROBE_TOP),
        new Item(ZURIELS_ROBE_BOTTOM),
        new Item(MORRIGANS_LEATHER_BODY),
        new Item(MORRIGANS_LEATHER_CHAPS),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(STATIUSS_WARHAMMER),
        new Item(VESTAS_LONGSWORD),
        new Item(NEITIZNOT_FACEGUARD),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(DRAGON_CLAWS),
        new Item(ARMADYL_GODSWORD),
        new Item(IMBUED_HEART),
        new Item(DARK_BOW),
        new Item(DHAROKS_ARMOUR_SET),
        new Item(KARILS_ARMOUR_SET),
        new Item(AHRIMS_ARMOUR_SET),
        new Item(INFINITY_BOOTS),
        new Item(AMULET_OF_FURY),
        new Item(OCCULT_NECKLACE),
        new Item(RANGER_BOOTS),
        new Item(MAGES_BOOK),
        new Item(DRAGON_BOOTS),
        new Item(TOME_OF_FIRE)
        //new Item(ANCESTRAL_HAT),
        //new Item(ANCESTRAL_ROBE_TOP),
        //new Item(ANCESTRAL_ROBE_BOTTOM),
        //new Item(JUSTICIAR_FACEGUARD),
        //new Item(JUSTICIAR_CHESTGUARD),
        // new Item(JUSTICIAR_LEGGUARDS),
    ),
    LARRANS_KEY_I(AttributeKey.LARRANS_KEYS_TIER_ONE_USED, LogType.KEYS, "Larran's key tier I", new int[]{LARRANS_KEY_TIER_I}, AttributeKey.LARRANS_KEY_TIER_I_LOG_CLAIMED, new Item[]{new Item(LARRANS_KEY_TIER_I, 5)},
        new Item(CRYSTAL_HELM),
        new Item(CRYSTAL_BODY),
        new Item(CRYSTAL_LEGS),
        new Item(ELDER_MAUL),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(TOXIC_BLOWPIPE),
        new Item(TRIDENT_OF_THE_SWAMP),
        new Item(SERPENTINE_HELM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(STATIUSS_WARHAMMER),
        new Item(VESTAS_LONGSWORD),
        new Item(DRAGON_CLAWS),
        new Item(DRAGON_WARHAMMER),
        new Item(DINHS_BULWARK),
        new Item(ARMADYL_CHESTPLATE),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(ARMADYL_GODSWORD),
        new Item(BANDOS_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(ZAMORAK_GODSWORD),
        new Item(BLADE_OF_SAELDOR),
        new Item(ABYSSAL_BLUDGEON),
        new Item(ABYSSAL_DAGGER),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(DRAGON_CROSSBOW),
        new Item(ZAMORAKIAN_HASTA),
        new Item(BANDOS_CHESTPLATE),
        new Item(BANDOS_TASSETS),
        new Item(FREMENNIK_KILT),
        new Item(ItemIdentifiers.BLOOD_MONEY, 1000),
        new Item(ABYSSAL_TENTACLE),
        new Item(WARRIOR_RING_I),
        new Item(BERSERKER_RING_I),
        new Item(ARCHERS_RING_I),
        new Item(SEERS_RING_I),
        new Item(GRANITE_MAUL_24225),
        new Item(REGEN_BRACELET),
        new Item(RANGERS_TUNIC),
        new Item(OBSIDIAN_HELMET),
        new Item(OBSIDIAN_PLATEBODY),
        new Item(OBSIDIAN_PLATELEGS),
        new Item(DRAGON_THROWNAXE),
        new Item(DRAGON_JAVELIN),
        new Item(DRAGON_KNIFE),
        new Item(DRAGON_DART),
        new Item(ONYX_DRAGON_BOLTS_E),
        new Item(DRAGONSTONE_DRAGON_BOLTS_E),
        new Item(DIAMOND_DRAGON_BOLTS_E),
        new Item(OPAL_DRAGON_BOLTS_E)
    ),

    LARRANS_KEY_II(AttributeKey.LARRANS_KEYS_TIER_TWO_USED, LogType.KEYS, "Larran's key tier II", new int[]{LARRANS_KEY_TIER_II}, AttributeKey.LARRANS_KEY_TIER_II_LOG_CLAIMED, new Item[]{new Item(LARRANS_KEY_TIER_II, 5)},
        new Item(NEITIZNOT_FACEGUARD),
        new Item(ARMADYL_GODSWORD_OR),
        new Item(DRAGON_CLAWS_OR),
        new Item(ELDER_ICE_MAUL),
        new Item(CustomItemIdentifiers.BOW_OF_FAERDHINEN),
        new Item(CRYSTAL_HELM),
        new Item(CRYSTAL_BODY),
        new Item(CRYSTAL_LEGS),
        new Item(VOLATILE_ORB),
        new Item(HARMONISED_ORB),
        new Item(ELDRITCH_ORB),
        new Item(NIGHTMARE_STAFF),
        new Item(ELDER_MAUL),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(TOXIC_BLOWPIPE),
        new Item(TRIDENT_OF_THE_SWAMP),
        new Item(MAGMA_HELM),
        new Item(TANZANITE_HELM),
        new Item(SERPENTINE_HELM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(STATIUSS_WARHAMMER),
        new Item(VESTAS_LONGSWORD),
        new Item(DRAGON_CLAWS),
        new Item(DRAGON_WARHAMMER),
        new Item(DINHS_BULWARK),
        new Item(ARMADYL_CHESTPLATE),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(ARMADYL_GODSWORD),
        new Item(BANDOS_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(ZAMORAK_GODSWORD),
        new Item(BLADE_OF_SAELDOR),
        new Item(ABYSSAL_BLUDGEON),
        new Item(ABYSSAL_DAGGER),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(DRAGON_CROSSBOW),
        new Item(ZAMORAKIAN_HASTA),
        new Item(BANDOS_CHESTPLATE),
        new Item(BANDOS_TASSETS)
    ),

    LARRANS_KEY_III(AttributeKey.LARRANS_KEYS_TIER_THREE_USED, LogType.KEYS, "Larran's key tier III", new int[]{LARRANS_KEY_TIER_III}, AttributeKey.LARRANS_KEY_TIER_III_LOG_CLAIMED, new Item[]{new Item(LARRANS_KEY_TIER_III, 5)},
        new Item(CORRUPTED_CRYSTAL_HELM),
        new Item(CORRUPTED_CRYSTAL_BODY),
        new Item(CORRUPTED_CRYSTAL_LEGS),
        new Item(CORRUPTING_STONE),
        new Item(CustomItemIdentifiers.BOW_OF_FAERDHINEN_C),
        new Item(BLADE_OF_SAELDOR_8),
        new Item(ABYSSAL_TENTACLE_24948),
        new Item(NEITIZNOT_FACEGUARD),
        new Item(ELDER_ICE_MAUL),
        new Item(CustomItemIdentifiers.BOW_OF_FAERDHINEN),
        new Item(CRYSTAL_HELM),
        new Item(CRYSTAL_BODY),
        new Item(CRYSTAL_LEGS),
        new Item(VOLATILE_ORB),
        new Item(HARMONISED_ORB),
        new Item(ELDRITCH_ORB),
        new Item(ARMADYL_GODSWORD_OR),
        new Item(DRAGON_CLAWS_OR),
        new Item(ELDER_MAUL),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(TOXIC_BLOWPIPE),
        new Item(TRIDENT_OF_THE_SWAMP),
        new Item(SERPENTINE_HELM),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(STATIUSS_WARHAMMER),
        new Item(VESTAS_LONGSWORD),
        new Item(DRAGON_CLAWS),
        new Item(DRAGON_WARHAMMER)

    ),

    SLAYER_KEY(AttributeKey.SLAYER_KEYS_OPENED, LogType.KEYS, "Slayer key", new int[]{CustomItemIdentifiers.SLAYER_KEY}, AttributeKey.SLAYER_KEY_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.SLAYER_KEY, 5)},
        new Item(DIVINE_SUPER_COMBAT_POTION4 + 1, 10),
        new Item(STAMINA_POTION4 + 1, 10),
        new Item(ANTIVENOM4 + 1, 10),
        new Item(SUPER_ANTIFIRE_POTION4 + 1, 10),
        new Item(REGEN_BRACELET + 1, 1),
        new Item(WARRIOR_RING + 1, 1),
        new Item(ARCHERS_RING + 1, 1),
        new Item(BERSERKER_RING + 1, 1),
        new Item(SEERS_RING + 1, 1),
        new Item(ItemIdentifiers.BLOOD_MONEY, 1000),
        new Item(AMULET_OF_FURY),
        new Item(ABYSSAL_TENTACLE),
        new Item(RANGER_BOOTS),
        new Item(ROBIN_HOOD_HAT),
        new Item(DIVINE_SUPER_COMBAT_POTION4 + 1, 50),
        new Item(STAMINA_POTION4 + 1, 25),
        new Item(SUPER_ANTIFIRE_POTION4 + 1, 25),
        new Item(DRAGON_CROSSBOW),
        new Item(ARMADYL_HELMET),
        new Item(ABYSSAL_BLUDGEON),
        new Item(BANDOS_GODSWORD),
        new Item(ZAMORAK_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(DRAGONFIRE_SHIELD),
        new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX),
        new Item(ARMADYL_GODSWORD),
        new Item(BANDOS_TASSETS),
        new Item(BANDOS_CHESTPLATE),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(ARMADYL_CHESTPLATE),
        new Item(ARMADYL_CROSSBOW),
        new Item(SERPENTINE_HELM),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(TOXIC_BLOWPIPE),
        new Item(ETERNAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(PRIMORDIAL_BOOTS)
    ),

    REVENANTS(AttributeKey.REVENANTS_KILLED, LogType.OTHER, "Revenants", new int[]{NpcIdentifiers.REVENANT_IMP, NpcIdentifiers.REVENANT_CYCLOPS, NpcIdentifiers.REVENANT_DARK_BEAST, NpcIdentifiers.REVENANT_DEMON, NpcIdentifiers.REVENANT_DRAGON, NpcIdentifiers.REVENANT_GOBLIN, NpcIdentifiers.REVENANT_HELLHOUND, NpcIdentifiers.REVENANT_HOBGOBLIN, NpcIdentifiers.REVENANT_KNIGHT, NpcIdentifiers.REVENANT_ORK, NpcIdentifiers.REVENANT_PYREFIEND}, AttributeKey.REVENANTS_LOG_CLAIMED, new Item[]{new Item(CRAWS_BOW), new Item(VIGGORAS_CHAINMACE), new Item(THAMMARONS_SCEPTRE)},
        //Drops
        new Item(VESTAS_SPEAR), new Item(VESTAS_LONGSWORD), new Item(VESTAS_CHAINBODY), new Item(VESTAS_PLATESKIRT), new Item(STATIUSS_WARHAMMER), new Item(STATIUSS_FULL_HELM), new Item(STATIUSS_PLATEBODY), new Item(STATIUSS_PLATELEGS), new Item(ZURIELS_STAFF), new Item(ZURIELS_HOOD), new Item(ZURIELS_ROBE_TOP), new Item(ZURIELS_ROBE_BOTTOM), new Item(MORRIGANS_COIF), new Item(MORRIGANS_LEATHER_BODY), new Item(MORRIGANS_LEATHER_CHAPS), new Item(MORRIGANS_JAVELIN), new Item(MORRIGANS_THROWING_AXE), new Item(VIGGORAS_CHAINMACE), new Item(CRAWS_BOW), new Item(THAMMARONS_SCEPTRE), new Item(AMULET_OF_AVARICE), new Item(BRACELET_OF_ETHEREUM_UNCHARGED), new Item(ANCIENT_RELIC), new Item(ANCIENT_EFFIGY), new Item(ANCIENT_MEDALLION), new Item(ItemIdentifiers.ANCIENT_STATUETTE), new Item(ANCIENT_TOTEM), new Item(ANCIENT_EMBLEM), new Item(REVENANT_CAVE_TELEPORT), new Item(REVENANT_ETHER)
    ),

    ANCIENT_REVENANTS(AttributeKey.ANCIENT_REVENANTS_KILLED, LogType.OTHER, "Ancient Revenants", new int[]{ANCIENT_REVENANT_DARK_BEAST, ANCIENT_REVENANT_ORK, ANCIENT_REVENANT_CYCLOPS, ANCIENT_REVENANT_DRAGON, ANCIENT_REVENANT_KNIGHT}, AttributeKey.ANCIENT_REVENANTS_LOG_CLAIMED, new Item[]{new Item(ANCIENT_VESTAS_LONGSWORD), new Item(ANCIENT_STATIUSS_WARHAMMER)},
        //Drops
        new Item(VESTAS_SPEAR), new Item(VESTAS_LONGSWORD), new Item(VESTAS_CHAINBODY), new Item(VESTAS_PLATESKIRT), new Item(STATIUSS_WARHAMMER), new Item(STATIUSS_FULL_HELM), new Item(STATIUSS_PLATEBODY), new Item(STATIUSS_PLATELEGS), new Item(ZURIELS_STAFF), new Item(ZURIELS_HOOD), new Item(ZURIELS_ROBE_TOP), new Item(ZURIELS_ROBE_BOTTOM), new Item(MORRIGANS_COIF), new Item(MORRIGANS_LEATHER_BODY), new Item(MORRIGANS_LEATHER_CHAPS), new Item(MORRIGANS_JAVELIN), new Item(MORRIGANS_THROWING_AXE), new Item(VIGGORAS_CHAINMACE), new Item(CRAWS_BOW), new Item(THAMMARONS_SCEPTRE), new Item(AMULET_OF_AVARICE), new Item(DARK_ANCIENT_EMBLEM), new Item(DARK_ANCIENT_RELIC), new Item(DARK_ANCIENT_EFFIGY), new Item(DARK_ANCIENT_MEDALLION), new Item(DARK_ANCIENT_STATUETTE), new Item(DARK_ANCIENT_TOTEM), new Item(DARK_ANCIENT_EMBLEM), new Item(ANCIENT_VESTAS_LONGSWORD), new Item(ANCIENT_STATIUSS_WARHAMMER)
    ),

 //   ICE_DEMON(AttributeKey.ICE_DEMONS_KILLED, LogType.OTHER, "Ice demon", new int[]{CustomNpcIdentifiers.ICE_DEMON}, AttributeKey.ICE_DEMON_LOG_CLAIMED, new Item[]{new Item(PRESENT_13346, 5), new Item(WINTER_ITEM_CASKET)},
        //Drops
        //new Item(CustomItemIdentifiers.SNOWBIRD),
  //      new Item(GIANT_PRESENT), new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(FROST_WHIP), new Item(ABYSSAL_TENTACLE_24948), new Item(CustomItemIdentifiers.SNOWY_SLED), new Item(CustomItemIdentifiers.ICED_PARTYHAT), new Item(CustomItemIdentifiers.ICED_SANTA_HAT), new Item(CustomItemIdentifiers.ICED_HWEEN_MASK), new Item(UGLY_SANTA_HAT), new Item(WINTER_ITEM_CASKET), new Item(CustomItemIdentifiers.MYSTERY_TICKET), new Item(FROST_CLAWS), new Item(ARMADYL_FROSTSWORD), new Item(CustomItemIdentifiers.INFINITY_WINTER_BOOTS), new Item(FROST_IMBUED_CAPE), new Item(DRAGON_HUNTER_CROSSBOW_T), new Item(CustomItemIdentifiers.ELDER_ICE_MAUL)
 //   ),

    ELVARG(AttributeKey.ELVARGS_KILLED, LogType.OTHER, "Elvarg", new int[]{ELVARG_HARD}, AttributeKey.ELVARG_LOG_CLAIMED, new Item[]{new Item(PRESENT_13346, 5), new Item(WINTER_ITEM_CASKET)},
        //Drops
        new Item(CustomItemIdentifiers.ELVARG_JR),
        new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX), new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(LAVA_WHIP), new Item(ABYSSAL_TENTACLE_24948), new Item(DRAGON_HUNTER_CROSSBOW), new Item(DRAGON_CLAWS), new Item(UGLY_SANTA_HAT), new Item(TEN_DOLLAR_BOND), new Item(WINTER_ITEM_CASKET), new Item(EPIC_PET_BOX), new Item(DRAGON_HUNTER_CROSSBOW_T)
    ),

    LAVA_BEAST(AttributeKey.LAVA_BEASTS_KILLED, LogType.OTHER, "Lava beast", new int[]{NpcIdentifiers.LAVA_BEAST}, AttributeKey.LAVA_BEASTS_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(CustomItemIdentifiers.MOLTEN_KEY, 5)},
        //Drops
        new Item(CustomItemIdentifiers.LAVA_BABY),
        new Item(CustomItemIdentifiers.MOLTEN_KEY)
    ),

    EL_FUEGO(AttributeKey.EL_FUEGO_KILLED, LogType.OTHER, "El fuego", new int[]{CustomNpcIdentifiers.EL_FUEGO}, AttributeKey.EL_FUEGO_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 3), new Item(KEY_OF_BOXES, 1)},
        //Drops
        new Item(BABY_LAVA_DRAGON),
        new Item(DAGONHAI_HAT), new Item(DAGONHAI_ROBE_TOP), new Item(DAGONHAI_ROBE_BOTTOM), new Item(LAVA_WHIP), new Item(LAVA_PARTYHAT), new Item(LAVA_DHIDE_COIF), new Item(LAVA_DHIDE_BODY), new Item(LAVA_DHIDE_CHAPS), new Item(KEY_OF_BOXES), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(CustomItemIdentifiers.MOLTEN_MYSTERY_BOX), new Item(EPIC_PET_BOX), new Item(TEN_DOLLAR_BOND), new Item(MOLTEN_DEFENDER)
    ),

    DERANGED_ARCHAEOLOGIST(AttributeKey.DERANGED_ARCHAEOLOGIST_KILLED, LogType.OTHER, "Deranged Archaeologist", new int[]{NpcIdentifiers.DERANGED_ARCHAEOLOGIST}, AttributeKey.DERANGED_ARCHAEOLOGIST_LOG_CLAIMED, new Item[]{new Item(ENCHANTED_KEY_I, 10), new Item(ENCHANTED_KEY_II, 1)},
        new Item(CustomItemIdentifiers.DERANGED_ARCHAEOLOGIST),
        new Item(ENCHANTED_KEY_I), new Item(ENCHANTED_KEY_II)
    ),


    SLAYER(null, LogType.OTHER, "Slayer",
        //Npcs that drop these items
        new int[]{NpcIdentifiers.CRAWLING_HAND_448, NpcIdentifiers.CRAWLING_HAND_449, NpcIdentifiers.CRAWLING_HAND_450, NpcIdentifiers.CRAWLING_HAND_451, NpcIdentifiers.CRAWLING_HAND_452, NpcIdentifiers.CRAWLING_HAND_453, NpcIdentifiers.CRAWLING_HAND_454, NpcIdentifiers.CRAWLING_HAND_455, NpcIdentifiers.CRAWLING_HAND_456, NpcIdentifiers.CRAWLING_HAND_457, NpcIdentifiers.CRUSHING_HAND,
            NpcIdentifiers.COCKATRICE_419, NpcIdentifiers.COCKATRICE_420, NpcIdentifiers.COCKATHRICE, NpcIdentifiers.BASILISK_417, NpcIdentifiers.BASILISK_418, NpcIdentifiers.BASILISK_9283, NpcIdentifiers.BASILISK_9284, NpcIdentifiers.BASILISK_9285, NpcIdentifiers.BASILISK_9286, NpcIdentifiers.BASILISK_KNIGHT, NpcIdentifiers.BASILISK_SENTINEL, NpcIdentifiers.BASILISK_YOUNGLING, NpcIdentifiers.MONSTROUS_BASILISK, NpcIdentifiers.MONSTROUS_BASILISK_9287, NpcIdentifiers.MONSTROUS_BASILISK_9288,
            NpcIdentifiers.KURASK_410, NpcIdentifiers.KURASK_411, NpcIdentifiers.KING_KURASK, NpcIdentifiers.ABYSSAL_DEMON_415, NpcIdentifiers.ABYSSAL_DEMON_416, NpcIdentifiers.ABYSSAL_DEMON_7241, NpcIdentifiers.GREATER_ABYSSAL_DEMON, NpcIdentifiers.ABYSSAL_SIRE, NpcIdentifiers.ABYSSAL_SIRE_5887, NpcIdentifiers.ABYSSAL_SIRE_5888, NpcIdentifiers.ABYSSAL_SIRE_5889, NpcIdentifiers.ABYSSAL_SIRE_5890, NpcIdentifiers.ABYSSAL_SIRE_5891, NpcIdentifiers.ABYSSAL_SIRE_5908,
            NpcIdentifiers.GARGOYLE, NpcIdentifiers.GARGOYLE_1543, NpcIdentifiers.MARBLE_GARGOYLE_7408, NpcIdentifiers.TUROTH, NpcIdentifiers.TUROTH_427, NpcIdentifiers.TUROTH_428, NpcIdentifiers.TUROTH_429, NpcIdentifiers.TUROTH_430, NpcIdentifiers.TUROTH_431, NpcIdentifiers.TUROTH_432, NpcIdentifiers.CAVE_HORROR, NpcIdentifiers.CAVE_HORROR_1048, NpcIdentifiers.CAVE_HORROR_1049, NpcIdentifiers.CAVE_HORROR_1050, NpcIdentifiers.CAVE_HORROR_1051, NpcIdentifiers.CAVE_ABOMINATION,
            NpcIdentifiers.TALONED_WYVERN, NpcIdentifiers.SPITTING_WYVERN, NpcIdentifiers.LONGTAILED_WYVERN, NpcIdentifiers.ANCIENT_WYVERN, NpcIdentifiers.KING_BLACK_DRAGON, NpcIdentifiers.KING_BLACK_DRAGON_6502, NpcIdentifiers.BLACK_DRAGON, NpcIdentifiers.BLACK_DRAGON_253, NpcIdentifiers.BLACK_DRAGON_254, NpcIdentifiers.BLACK_DRAGON_255, NpcIdentifiers.BLACK_DRAGON_256, NpcIdentifiers.BLACK_DRAGON_257, NpcIdentifiers.BLACK_DRAGON_258, NpcIdentifiers.BLACK_DRAGON_259, NpcIdentifiers.BLACK_DRAGON_7861, NpcIdentifiers.BLACK_DRAGON_7862, NpcIdentifiers.BLACK_DRAGON_7863, NpcIdentifiers.BLACK_DRAGON_8084, NpcIdentifiers.BLACK_DRAGON_8085, NpcIdentifiers.BRUTAL_BLACK_DRAGON, NpcIdentifiers.BRUTAL_BLACK_DRAGON_8092, NpcIdentifiers.BRUTAL_BLACK_DRAGON_8092,
            NpcIdentifiers.VORKATH_8061, NpcIdentifiers.ADAMANT_DRAGON, NpcIdentifiers.ADAMANT_DRAGON_8090, NpcIdentifiers.RUNE_DRAGON, NpcIdentifiers.RUNE_DRAGON_8031, NpcIdentifiers.RUNE_DRAGON_8091, NpcIdentifiers.LAVA_DRAGON, NpcIdentifiers.MITHRIL_DRAGON, NpcIdentifiers.MITHRIL_DRAGON_8088, NpcIdentifiers.MITHRIL_DRAGON_8089, NpcIdentifiers.SKELETAL_WYVERN_466, NpcIdentifiers.SKELETAL_WYVERN_467, NpcIdentifiers.SKELETAL_WYVERN_468, NpcIdentifiers.SPIRITUAL_MAGE, NpcIdentifiers.SPIRITUAL_MAGE_2244, NpcIdentifiers.SPIRITUAL_MAGE_3161, NpcIdentifiers.SPIRITUAL_MAGE_3168,
            NpcIdentifiers.KRAKEN, NpcIdentifiers.DARK_BEAST, NpcIdentifiers.DARK_BEAST_7250, NpcIdentifiers.NIGHT_BEAST, NpcIdentifiers.SMOKE_DEVIL, NpcIdentifiers.SMOKE_DEVIL_6639, NpcIdentifiers.SMOKE_DEVIL_6655, NpcIdentifiers.SMOKE_DEVIL_8482, NpcIdentifiers.SMOKE_DEVIL_8483, NpcIdentifiers.NUCLEAR_SMOKE_DEVIL, NpcIdentifiers.THERMONUCLEAR_SMOKE_DEVIL, NpcIdentifiers.KALPHITE_QUEEN_6500, NpcIdentifiers.KALPHITE_QUEEN_6501, NpcIdentifiers.WYRM, NpcIdentifiers.WYRM_8611, NpcIdentifiers.DRAKE_8612, NpcIdentifiers.DRAKE_8613, NpcIdentifiers.HYDRA, NpcIdentifiers.ALCHEMICAL_HYDRA, NpcIdentifiers.ALCHEMICAL_HYDRA_8616, NpcIdentifiers.ALCHEMICAL_HYDRA_8617, NpcIdentifiers.ALCHEMICAL_HYDRA_8618, NpcIdentifiers.ALCHEMICAL_HYDRA_8619, NpcIdentifiers.ALCHEMICAL_HYDRA_8620, NpcIdentifiers.ALCHEMICAL_HYDRA_8621, NpcIdentifiers.ALCHEMICAL_HYDRA_8622, NpcIdentifiers.ALCHEMICAL_HYDRA_8634,
            NpcIdentifiers.BASILISK_KNIGHT, NpcIdentifiers.BASILISK_SENTINEL}, AttributeKey.SLAYER_LOG_CLAIMED, new Item[]{new Item(ABYSSAL_TENTACLE_24948), new Item(GRANITE_MAUL_24944)},
        //Drops
        new Item(CRAWLING_HAND_7975), new Item(COCKATRICE_HEAD), new Item(BASILISK_HEAD), new Item(KURASK_HEAD), new Item(ABYSSAL_HEAD), new Item(IMBUED_HEART), new Item(ETERNAL_GEM), new Item(DUST_BATTLESTAFF), new Item(MIST_BATTLESTAFF), new Item(ABYSSAL_WHIP), new Item(GRANITE_MAUL_24225), new Item(LEAFBLADED_SWORD), new Item(LEAFBLADED_BATTLEAXE), new Item(BLACK_MASK), new Item(GRANITE_LONGSWORD), new Item(WYVERN_VISAGE), new Item(DRACONIC_VISAGE),
        new Item(DRAGON_BOOTS), new Item(ABYSSAL_DAGGER), new Item(TRIDENT_OF_THE_SEAS), new Item(KRAKEN_TENTACLE), new Item(DARK_BOW), new Item(OCCULT_NECKLACE), new Item(DRAGON_CHAINBODY_3140), new Item(DRAGON_THROWNAXE), new Item(DRAGON_HARPOON), new Item(DRAGON_SWORD), new Item(DRAGON_KNIFE), new Item(DRAKES_TOOTH), new Item(DRAKES_CLAW), new Item(HYDRA_TAIL), new Item(HYDRAS_FANG), new Item(HYDRAS_EYE), new Item(HYDRAS_HEART), new Item(BASILISK_JAW)),
    CHAMBER_OF_XERIC(AttributeKey.CHAMBER_OF_XERIC_RUNS_CHALLENGE_COMPLETED, LogType.OTHER, "Chamber of Xeric", new int[]{RAIDS_KEY}, AttributeKey.CHAMBER_OF_XERIC_LOG_CLAIMED,
        new Item[]{new Item(CustomItemIdentifiers.RAIDS_MYSTERY_BOX, 3)},
        //Drops
       new Item(TWISTED_BOW), new Item(SANGUINE_ORNAMENT_KIT), new Item(ANCESTRAL_ROBE_BOTTOM), new Item(ANCESTRAL_ROBE_TOP), new Item(ANCESTRAL_HAT), new Item(ELDER_MAUL),
        new Item(DEXTEROUS_PRAYER_SCROLL), new Item(KODAI_WAND), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(DINHS_BULWARK), new Item(DRAGON_HUNTER_CROSSBOW), new Item(TWISTED_BUCKLER), new Item(ARCANE_PRAYER_SCROLL), new Item(DRAGON_CLAWS), new Item(25742), new Item(TWISTED_BOW_KIT), new Item(MASORI_MASK), new Item(MASORI_BODY),
        new Item(MASORI_CHAPS), new Item(TUMEKENS_SHADOW), new Item(MENAPHITE_ORNAMENT_KIT)
    ),
    THEATRE_OF_BLOOD(AttributeKey.THEATRE_OF_BLOOD_RUNS_COMPLETED, LogType.OTHER, "Theatre of Blood", new int[]{TOB_RAIDS_KEY}, AttributeKey.THEATRE_OF_BLOOD_LOG_CLAIMED, new Item[]{new Item(PET_MYSTERY_BOX,1), new Item(CustomItemIdentifiers.RAIDS_MYSTERY_BOX,3), new Item(CustomItemIdentifiers.MYSTERY_TICKET,2)},
        //Drops
        new Item(ItemIdentifiers.LIL_ZIK), new Item(SCYTHE_OF_VITUR), new Item(GHRAZI_RAPIER), new Item(SANGUINESTI_STAFF), new Item(JUSTICIAR_FACEGUARD), new Item(JUSTICIAR_CHESTGUARD), new Item(JUSTICIAR_LEGGUARDS), new Item(AVERNIC_DEFENDER)),

    CHAMBER_OF_SECRETS(AttributeKey.CHAMBER_OF_SECRET_RUNS_COMPLETED, LogType.OTHER, "Chamber Of Secrets", new int[]{RAIDS_KEY}, AttributeKey.CHAMBER_OF_SECRETS_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.RAIDS_MYSTERY_BOX, 3)},
        //Custom
        new Item(TOM_RIDDLE_DIARY), new Item(MARVOLO_GAUNTS_RING), new Item(CLOAK_OF_INVISIBILITY), new Item(ELDER_WAND_HANDLE), new Item(ELDER_WAND_STICK), new Item(SWORD_OF_GRYFFINDOR), new Item(TALONHAWK_CROSSBOW), new Item(SALAZAR_SLYTHERINS_LOCKET),  new Item(LIGHTBEARER), new Item(OSMUMTENS_FANG), new Item(27248), new Item(ELIDINIS_WARD)),
    ETHEREAL_REVENANTS(AttributeKey.ETHEREAL_REVENANTS_COMPLETED, LogType.BOSSES, "Ethereal Revenants", WildernessKeys.keyNpcs, AttributeKey.ETHEREAL_REVENANTS_LOG_CLAIMED, new Item[]{new Item(CustomItemIdentifiers.MYSTERY_CHEST, 3)},
        //Custom
        new Item(PET_MYSTERY_BOX), new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX), new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX), new Item(25744), new Item(CustomItemIdentifiers.BONDS_CASKET), new Item(EPIC_PET_BOX), new Item(CRAWS_BOW), new Item(VIGGORAS_CHAINMACE), new Item(THAMMARONS_SCEPTRE),  new Item(CRAWS_BOW_C), new Item(VIGGORAS_CHAINMACE_C), new Item(THAMMARONS_STAFF_C), new Item(CustomItemIdentifiers.MYSTERY_CHEST),new Item(CustomItemIdentifiers.MYSTERY_TICKET), new Item(CustomItemIdentifiers.RUINOUS_BOOK), new Item(CustomItemIdentifiers.GRANDMASTER_HEART), new Item(CustomItemIdentifiers.GRAND_KEY), new Item(CustomItemIdentifiers.KEY_OF_BOXES));
    ;

    private final AttributeKey attributeKey;
    private final LogType logType;
    private final String name;
    private final int[] key;
    private final AttributeKey rewardClaimed;
    private final Item[] reward;
    private final Item[] obtainables;

    Collection(AttributeKey attributeKey, LogType logType, String name, int[] key, AttributeKey rewardClaimed, Item[] reward, Item... obtainables) {
        this.attributeKey = attributeKey;
        this.logType = logType;
        this.name = name;
        this.key = key;
        this.rewardClaimed = rewardClaimed;
        this.reward = reward;
        this.obtainables = obtainables;
    }

    public AttributeKey getAttributeKey() {
        return attributeKey;
    }

    public String getName() {
        return name;
    }

    public int[] getKey() {
        return key;
    }

    public AttributeKey getRewardClaimedKey() {
        return rewardClaimed;
    }

    public Item[] getReward() {
        return reward;
    }

    public Item[] getObtainables() {
        return obtainables;
    }

    public LogType getLogType() {
        return logType;
    }

    /**
     * The amount of items we can obtain.
     */
    public int totalCollectables() {
        return obtainables.length;
    }

    /**
     * Gets all the data for a specific type.
     *
     * @param logType the log type that is being sorted at alphabetical order
     */
    public static List<Collection> getAsList(LogType logType) {
        return Arrays.stream(values()).filter(type -> type.getLogType() == logType).sorted(Comparator.comparing(Enum::name)).collect(Collectors.toList());
    }
}
