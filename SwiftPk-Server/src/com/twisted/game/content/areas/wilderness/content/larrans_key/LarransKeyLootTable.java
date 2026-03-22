package com.twisted.game.content.areas.wilderness.content.larrans_key;

import com.twisted.game.world.items.Item;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.CustomItemIdentifiers.DRAGON_CLAWS_OR;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | February, 17, 2021, 14:19
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class LarransKeyLootTable {

    private static final List<Item> COMMON_TABLE_TIER_I = Arrays.asList(
        new Item(BLOOD_MONEY, 1500),
        new Item(DRAGON_DART, 25),
        new Item(DRAGON_KNIFE, 15),
        new Item(DRAGON_JAVELIN, 25),
        new Item(DRAGON_THROWNAXE, 25),
        new Item(ANTIVENOM4+1, 5),
        new Item(GUTHIX_REST4+1, 5),
        new Item(OBSIDIAN_HELMET, 1),
        new Item(OBSIDIAN_PLATEBODY, 1),
        new Item(OBSIDIAN_PLATELEGS, 1),
        new Item(RANGERS_TUNIC, 1),
        new Item(REGEN_BRACELET, 1),
        new Item(GRANITE_MAUL_24225, 1),
        new Item(BERSERKER_RING_I, 1),
        new Item(ARCHERS_RING_I, 1),
        new Item(SEERS_RING_I, 1),
        new Item(WARRIOR_RING_I, 1),
        new Item(OPAL_DRAGON_BOLTS_E, 25),
        new Item(DIAMOND_DRAGON_BOLTS_E, 25),
        new Item(DRAGONSTONE_DRAGON_BOLTS_E, 25),
        new Item(ONYX_DRAGON_BOLTS_E, 25),
        new Item(ABYSSAL_TENTACLE)
    );

    private static final List<Item> UNCOMMON_TABLE_TIER_I = Arrays.asList(
        new Item(BLOOD_MONEY, 25000),
        new Item(FREMENNIK_KILT),
        new Item(BANDOS_CHESTPLATE),
        new Item(BANDOS_TASSETS),
        new Item(ARMADYL_CHESTPLATE),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(ABYSSAL_BLUDGEON),
        new Item(BLADE_OF_SAELDOR),
        new Item(BANDOS_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(ZAMORAK_GODSWORD)
    );

    private static final List<Item> RARE_TABLE_TIER_I = Arrays.asList(
        new Item(DRAGON_WARHAMMER),
        new Item(STATIUSS_WARHAMMER),
        new Item(VESTAS_LONGSWORD),
        new Item(TRIDENT_OF_THE_SWAMP),
        new Item(DINHS_BULWARK),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(ARMADYL_CHESTPLATE),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(TOXIC_BLOWPIPE),
        new Item(SERPENTINE_HELM),
        new Item(MAGMA_HELM),
        new Item(TANZANITE_HELM)
    );

    private static final List<Item> EXTREMELY_RARE_TABLE_TIER_I = Arrays.asList(
        new Item(ELDER_MAUL),
        new Item(ARMADYL_GODSWORD),
        new Item(DRAGON_CLAWS),
        new Item(CRYSTAL_HELM),
        new Item(CRYSTAL_BODY),
        new Item(CRYSTAL_LEGS)
    );

    private static final List<Item> COMMON_TABLE_TIER_II = Arrays.asList(
        new Item(BLOOD_MONEY, 25000),
        new Item(FREMENNIK_KILT),
        new Item(BANDOS_CHESTPLATE),
        new Item(BANDOS_TASSETS),
        new Item(ZAMORAKIAN_HASTA),
        new Item(DRAGON_CROSSBOW),
        new Item(DRAGON_SCIMITAR_OR),
        new Item(ABYSSAL_DAGGER),
        new Item(ABYSSAL_BLUDGEON),
        new Item(ARMADYL_CHESTPLATE),
        new Item(ARMADYL_CHAINSKIRT),
        new Item(BLADE_OF_SAELDOR),
        new Item(BANDOS_GODSWORD),
        new Item(SARADOMIN_GODSWORD),
        new Item(ZAMORAK_GODSWORD)
    );

    private static final List<Item> UNCOMMON_TABLE_TIER_II = Arrays.asList(
        new Item(DRAGON_WARHAMMER),
        new Item(STATIUSS_WARHAMMER),
        new Item(VESTAS_LONGSWORD),
        new Item(TRIDENT_OF_THE_SWAMP),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(TOXIC_BLOWPIPE),
        new Item(SERPENTINE_HELM),
        new Item(MAGMA_HELM),
        new Item(TANZANITE_HELM)
    );

    private static final List<Item> RARE_TABLE_TIER_II = Arrays.asList(
        new Item(VOLATILE_ORB),
        new Item(HARMONISED_ORB),
        new Item(ELDRITCH_ORB),
        new Item(NIGHTMARE_STAFF),
        new Item(ARMADYL_GODSWORD),
        new Item(DRAGON_CLAWS),
        new Item(ELDER_MAUL),
        new Item(CRYSTAL_HELM),
        new Item(CRYSTAL_BODY),
        new Item(CRYSTAL_LEGS)
    );

    private static final List<Item> EXTREMELY_RARE_TABLE_TIER_II = Arrays.asList(

        new Item(NEITIZNOT_FACEGUARD),
        new Item(ARMADYL_GODSWORD_OR),
        new Item(DRAGON_CLAWS_OR),
        new Item(ELDER_ICE_MAUL),
        new Item(CustomItemIdentifiers.BOW_OF_FAERDHINEN)
    );

    private static final List<Item> COMMON_TABLE_TIER_III = Arrays.asList(
        new Item(STATIUSS_WARHAMMER),
        new Item(VESTAS_LONGSWORD),
        new Item(TRIDENT_OF_THE_SWAMP),
        new Item(PRIMORDIAL_BOOTS),
        new Item(PEGASIAN_BOOTS),
        new Item(ETERNAL_BOOTS),
        new Item(AMULET_OF_TORTURE),
        new Item(NECKLACE_OF_ANGUISH),
        new Item(TOXIC_STAFF_OF_THE_DEAD),
        new Item(TOXIC_BLOWPIPE),
        new Item(SERPENTINE_HELM),
        new Item(MAGMA_HELM),
        new Item(TANZANITE_HELM)
    );

    private static final List<Item> UNCOMMON_TABLE_TIER_III = Arrays.asList(
        new Item(VOLATILE_ORB),
        new Item(HARMONISED_ORB),
        new Item(ELDRITCH_ORB),
        new Item(HARMONISED_ORB),
        new Item(NIGHTMARE_STAFF),
        new Item(ELDER_MAUL),
        new Item(CRYSTAL_HELM),
        new Item(CRYSTAL_BODY),
        new Item(CRYSTAL_LEGS)
    );

    private static final List<Item> RARE_TABLE_TIER_III = Arrays.asList(
        new Item(NEITIZNOT_FACEGUARD),
        new Item(ARMADYL_GODSWORD_OR),
        new Item(DRAGON_CLAWS_OR),
        new Item(ELDER_ICE_MAUL),
        new Item(CustomItemIdentifiers.BOW_OF_FAERDHINEN)
    );

    private static final List<Item> EXTREMELY_RARE_TABLE_TIER_III = Arrays.asList(
        new Item(CORRUPTED_CRYSTAL_HELM),
        new Item(CORRUPTED_CRYSTAL_BODY),
        new Item(CORRUPTED_CRYSTAL_LEGS),
        new Item(CORRUPTING_STONE),
        new Item(CustomItemIdentifiers.BOW_OF_FAERDHINEN_C),
        new Item(BLADE_OF_SAELDOR_8),
        new Item(ABYSSAL_TENTACLE_24948)
    );


    public static Item rewardTables(int key) {
        List<Item> items = null;
        if(key == LARRANS_KEY_TIER_I) {
            if(Utils.rollDie(150, 1)) {
                items = EXTREMELY_RARE_TABLE_TIER_I;
            } else if (Utils.rollDie(30, 1)) {
                items = RARE_TABLE_TIER_I;
            } else if (Utils.rollDie(5, 1)) {
                items = UNCOMMON_TABLE_TIER_I;
            } else {
                items = COMMON_TABLE_TIER_I;
            }
        } else if(key == LARRANS_KEY_TIER_II) {
            if(Utils.rollDie(125, 1)) {
                items = EXTREMELY_RARE_TABLE_TIER_II;
            } else if (Utils.rollDie(20, 1)) {
                items = RARE_TABLE_TIER_II;
            } else if (Utils.rollDie(2, 1)) {
                items = UNCOMMON_TABLE_TIER_II;
            } else {
                items = COMMON_TABLE_TIER_II;
            }
        } else if(key == LARRANS_KEY_TIER_III) {
            if(Utils.rollDie(100, 1)) {
                items = EXTREMELY_RARE_TABLE_TIER_III;
            } else if (Utils.rollDie(15, 1)) {
                items = RARE_TABLE_TIER_III;
            } else if (Utils.rollDie(1, 1)) {
                items = UNCOMMON_TABLE_TIER_III;
            } else {
                items = COMMON_TABLE_TIER_III;
            }
        }
        return Utils.randomElement(items);
    }
}
