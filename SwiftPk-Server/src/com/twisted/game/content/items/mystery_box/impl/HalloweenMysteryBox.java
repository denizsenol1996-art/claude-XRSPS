package com.twisted.game.content.items.mystery_box.impl;

import com.twisted.game.content.items.mystery_box.MboxItem;
import com.twisted.game.content.items.mystery_box.MysteryBox;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 13, 2021
 */
public class HalloweenMysteryBox extends MysteryBox {

    public static final int HALLOWEEN_MYSTERY_BOX = 30186;

    @Override
    protected String name() {
        return "H'ween mystery box";
    }

    @Override
    public int mysteryBoxId() {
        return HALLOWEEN_MYSTERY_BOX;
    }

    private static final int EXTREME_ROLL = 28;
    private static final int RARE_ROLL = 14;
    private static final int UNCOMMON_ROLL = 7;

    private static final MboxItem[] EXTREMELY_RARE = new MboxItem[]{
        new MboxItem(STATIUSS_PLATEBODY).broadcastWorldMessage(true),
        new MboxItem(STATIUSS_PLATELEGS).broadcastWorldMessage(true),
        new MboxItem(MORRIGANS_LEATHER_BODY).broadcastWorldMessage(true),
        new MboxItem(MORRIGANS_LEATHER_CHAPS).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_LONGSWORD).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_BOW).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_WAND).broadcastWorldMessage(true),
    };

    private static final MboxItem[] RARE = new MboxItem[]{
        new MboxItem(MORRIGANS_COIF).broadcastWorldMessage(true),
        new MboxItem(ZURIELS_ROBE_TOP).broadcastWorldMessage(true),
        new MboxItem(ZURIELS_ROBE_BOTTOM).broadcastWorldMessage(true),
        new MboxItem(VESTAS_SPEAR).broadcastWorldMessage(true),
        new MboxItem(STATIUSS_FULL_HELM).broadcastWorldMessage(true),
        new MboxItem(STATIUSS_WARHAMMER).broadcastWorldMessage(true),
        new MboxItem(TOXIC_BLOWPIPE),
        new MboxItem(NEITIZNOT_FACEGUARD),
        new MboxItem(TOXIC_STAFF_OF_THE_DEAD),
    };

    private static final MboxItem[] UNCOMMON = new MboxItem[]{
        new MboxItem(HEAVY_BALLISTA),
        new MboxItem(ARMADYL_CROSSBOW),
        new MboxItem(ARMADYL_GODSWORD),
        new MboxItem(BRIMSTONE_RING),
        new MboxItem(TOXIC_STAFF_OF_THE_DEAD),
        new MboxItem(BANDOS_CHESTPLATE),
        new MboxItem(BANDOS_TASSETS),
        new MboxItem(DRAGON_WARHAMMER),
    };

    private static final MboxItem[] COMMON = new MboxItem[]{
        new MboxItem(BANDOS_GODSWORD),
        new MboxItem(SARADOMIN_GODSWORD),
        new MboxItem(ZAMORAK_GODSWORD),
        new MboxItem(ARMADYL_CHAINSKIRT),
        new MboxItem(ARMADYL_CHESTPLATE),
        new MboxItem(MORRIGANS_JAVELIN, 75),
        new MboxItem(MORRIGANS_THROWING_AXE, 75),
        new MboxItem(DRAGONFIRE_WARD),
        new MboxItem(SERPENTINE_HELM),
        new MboxItem(ANCIENT_WYVERN_SHIELD),
        new MboxItem(DRAGONFIRE_SHIELD),
        new MboxItem(BLOOD_MONEY, 25000),
    };

    private MboxItem[] allRewardsCached;

    public MboxItem[] allPossibleRewards() {
        if (allRewardsCached == null) {
            ArrayList<MboxItem> mboxItems = new ArrayList<>();
            mboxItems.addAll(Arrays.asList(EXTREMELY_RARE));
            mboxItems.addAll(Arrays.asList(RARE));
            mboxItems.addAll(Arrays.asList(UNCOMMON));
            mboxItems.addAll(Arrays.asList(COMMON));
            allRewardsCached = mboxItems.toArray(new MboxItem[0]);
        }
        return allRewardsCached;
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.DONATOR_MYSTERY_BOXES_OPENED;
    }

    @Override
    public MboxItem rollReward() {
        if (Utils.rollDie(EXTREME_ROLL, 1)) {
            return Utils.randomElement(EXTREMELY_RARE);
        } else if (Utils.rollDie(RARE_ROLL, 1)) {
            return Utils.randomElement(RARE);
        } else if (Utils.rollDie(UNCOMMON_ROLL, 1)) {
            return Utils.randomElement(UNCOMMON);
        } else {
            return Utils.randomElement(COMMON);
        }
    }
}
