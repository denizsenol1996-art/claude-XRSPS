package com.twisted.game.content.seasonal_events.rewards;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.items.Item;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import lombok.Getter;

import static com.twisted.game.world.entity.AttributeKey.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 14, 2021
 */
public enum EventRewards {

    EVENT_REWARD_1(new Item(ItemIdentifiers.STAFF_OF_THE_DEAD, 1), 50, EVENT_REWARD_1_CLAIMED),
    EVENT_REWARD_2(new Item(ItemIdentifiers.ARMADYL_CROSSBOW, 1), 50, EVENT_REWARD_2_CLAIMED),
    EVENT_REWARD_3(new Item(CustomItemIdentifiers.DOUBLE_DROPS_LAMP, 1), 15, EVENT_REWARD_3_CLAIMED),
    EVENT_REWARD_4(new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX, 1), 25, EVENT_REWARD_4_CLAIMED),
    EVENT_REWARD_5(new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX, 1), 25, EVENT_REWARD_5_CLAIMED),
    EVENT_REWARD_6(new Item(CustomItemIdentifiers.CORRUPTED_RANGER_GAUNTLETS, 1), 200, EVENT_REWARD_6_CLAIMED),
    EVENT_REWARD_7(new Item(ItemIdentifiers.TORMENTED_BRACELET_OR, 1), 200, EVENT_REWARD_7_CLAIMED),
    EVENT_REWARD_8(new Item(ItemIdentifiers.FEROCIOUS_GLOVES, 1), 200, EVENT_REWARD_8_CLAIMED),
    EVENT_REWARD_9(new Item(ItemIdentifiers.PRIMORDIAL_BOOTS, 1), 35, EVENT_REWARD_9_CLAIMED),
    EVENT_REWARD_10(new Item(ItemIdentifiers.PEGASIAN_BOOTS, 1), 35, EVENT_REWARD_10_CLAIMED),
    EVENT_REWARD_11(new Item(ItemIdentifiers.ETERNAL_BOOTS, 1), 35, EVENT_REWARD_11_CLAIMED),
    EVENT_REWARD_12(new Item(CustomItemIdentifiers.LAVA_WHIP, 1), 50, EVENT_REWARD_12_CLAIMED),
    EVENT_REWARD_13(new Item(ItemIdentifiers.KODAI_WAND, 1), 120, EVENT_REWARD_13_CLAIMED),
    EVENT_REWARD_14(new Item(ItemIdentifiers.IMBUED_HEART, 1), 115, EVENT_REWARD_14_CLAIMED),
    EVENT_REWARD_15(new Item(16167, 1), 110, EVENT_REWARD_15_CLAIMED),
    EVENT_REWARD_16(new Item(CustomItemIdentifiers.RING_OF_DIVINATION, 1), 115, EVENT_REWARD_16_CLAIMED),
    EVENT_REWARD_17(new Item(ItemIdentifiers.CHRISTMAS_CRACKER, 1), 125, EVENT_REWARD_17_CLAIMED),
    EVENT_REWARD_18(new Item(CustomItemIdentifiers.FROST_WHIP, 1), 50, EVENT_REWARD_18_CLAIMED),
    EVENT_REWARD_19(new Item(ItemIdentifiers.AVERNIC_DEFENDER, 1), 110, EVENT_REWARD_19_CLAIMED),
    EVENT_REWARD_20(new Item(ItemIdentifiers.ZAMORAK_GODSWORD, 1), 50, EVENT_REWARD_20_CLAIMED),
    EVENT_REWARD_21(new Item(ItemIdentifiers.BANDOS_GODSWORD, 1), 50, EVENT_REWARD_21_CLAIMED),
    EVENT_REWARD_22(new Item(ItemIdentifiers.ARMADYL_GODSWORD, 1), 75, EVENT_REWARD_22_CLAIMED),
    EVENT_REWARD_23(new Item(32131, 1), 150, EVENT_REWARD_23_CLAIMED),
    EVENT_REWARD_24(new Item(32133, 1), 150, EVENT_REWARD_24_CLAIMED),
    EVENT_REWARD_25(new Item(CustomItemIdentifiers.PET_MYSTERY_BOX, 1), 150, EVENT_REWARD_25_CLAIMED),
    EVENT_REWARD_26(new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET, 1), 80, EVENT_REWARD_26_CLAIMED),
    EVENT_REWARD_27(new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET, 1), 80, EVENT_REWARD_27_CLAIMED),
    EVENT_REWARD_28(new Item(CustomItemIdentifiers.DONATOR_TICKET, 100), 50, EVENT_REWARD_28_CLAIMED),
    EVENT_REWARD_29(new Item(CustomItemIdentifiers.LARRANS_KEY_TIER_I, 1), 35, EVENT_REWARD_29_CLAIMED),
    EVENT_REWARD_30(new Item(CustomItemIdentifiers.LARRANS_KEY_TIER_II, 1), 35, EVENT_REWARD_30_CLAIMED),
    EVENT_REWARD_31(new Item(CustomItemIdentifiers.LARRANS_KEY_TIER_III, 1), 80, EVENT_REWARD_31_CLAIMED),
    EVENT_REWARD_32(new Item(ItemIdentifiers.BLOOD_MONEY, 2_000_000), 150, EVENT_REWARD_32_CLAIMED),
    EVENT_REWARD_33(new Item(CustomItemIdentifiers.REVENANT_MYSTER_BOX, 1), 150, EVENT_REWARD_33_CLAIMED),
    EVENT_REWARD_34(new Item(28313, 1), 150, EVENT_REWARD_34_CLAIMED),
    EVENT_REWARD_35(new Item(28310, 1), 150, EVENT_REWARD_35_CLAIMED),
    EVENT_REWARD_36(new Item(28316, 1), 150, EVENT_REWARD_36_CLAIMED),
    EVENT_REWARD_37(new Item(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 1), 85, EVENT_REWARD_37_CLAIMED),
    EVENT_REWARD_38(new Item(CustomItemIdentifiers.MYSTERY_TICKET, 1), 85, EVENT_REWARD_38_CLAIMED),
    EVENT_REWARD_39(new Item(CustomItemIdentifiers.KEY_OF_BOXES, 1), 85, EVENT_REWARD_39_CLAIMED),
    EVENT_REWARD_40(new Item(CustomItemIdentifiers.GRAND_KEY, 1), 160, EVENT_REWARD_40_CLAIMED),
    EVENT_REWARD_41(new Item(ItemIdentifiers.AMULET_OF_BLOOD_FURY, 1), 15, EVENT_REWARD_41_CLAIMED),
    EVENT_REWARD_42(new Item(ItemIdentifiers.PK_TICKET, 1), 100, EVENT_REWARD_42_CLAIMED),
    EVENT_REWARD_43(new Item(CustomItemIdentifiers.MYSTERY_CHEST, 1), 200, EVENT_REWARD_43_CLAIMED),
    EVENT_REWARD_44(new Item(ItemIdentifiers.BLOOD_MONEY, 2_000_000), 150, EVENT_REWARD_44_CLAIMED)//Contains a random cosmetic
    ;

    @Getter public final Item reward;
    @Getter public final int chance;
    @Getter public final AttributeKey key;
    public static final EventRewards[] VALUES = EventRewards.values();

    EventRewards(Item reward, int chance, AttributeKey key) {
        this.reward = reward;
        this.chance = chance;
        this.key = key;
    }
}
