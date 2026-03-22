package com.twisted.game.content.items.mystery_box.impl;

import com.twisted.game.content.items.mystery_box.MboxItem;
import com.twisted.game.content.items.mystery_box.MysteryBox;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

public class DonatorMysteryBox extends MysteryBox {

    public static final int DONATOR_MYSTERY_BOX = 30185;

    @Override
    protected String name() {
        return "Donator mystery box";
    }

    @Override
    public int mysteryBoxId() {
        return DONATOR_MYSTERY_BOX;
    }

    private static final int EXTREME_ROLL = 70;
    private static final int RARE_ROLL = 20;
    private static final int UNCOMMON_ROLL = 10;

    private static final MboxItem[] EXTREMELY_RARE = new MboxItem[]{
        new MboxItem(_3RD_AGE_DRUIDIC_CLOAK).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_DRUIDIC_STAFF).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_DRUIDIC_ROBE_TOP).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_DRUIDIC_ROBE_BOTTOMS).broadcastWorldMessage(true),
        new MboxItem(RAINBOW_PARTYHAT).broadcastWorldMessage(true),
        new MboxItem(BLACK_PARTYHAT).broadcastWorldMessage(true),
        new MboxItem(BLACK_SANTA_HAT).broadcastWorldMessage(true),
        new MboxItem(BLACK_HWEEN_MASK).broadcastWorldMessage(true),
        new MboxItem(INVERTED_SANTA_HAT).broadcastWorldMessage(true),
        new MboxItem(PARTYHAT__SPECS).broadcastWorldMessage(true),
        new MboxItem(ANTISANTA_MASK).broadcastWorldMessage(true),
        new MboxItem(ANTISANTA_JACKET).broadcastWorldMessage(true),
        new MboxItem(ANTISANTA_PANTALOONS).broadcastWorldMessage(true),
        new MboxItem(ANTISANTA_GLOVES).broadcastWorldMessage(true),
        new MboxItem(ANTISANTA_BOOTS).broadcastWorldMessage(true),
        new MboxItem(BLACK_ANKOU_MASK).broadcastWorldMessage(true),//UPDATET1
        new MboxItem(BLACK_ANKOU_GLOVES).broadcastWorldMessage(true),
        new MboxItem(BLACK_ANKOU_TOP).broadcastWorldMessage(true),
        new MboxItem(BLACK_ANKOU_SOCKS).broadcastWorldMessage(true),
        new MboxItem(BLACK_ANKOUS_LEGGINGS).broadcastWorldMessage(true),
        new MboxItem(YELLOW_PARTYHAT).broadcastWorldMessage(true),
        new MboxItem(RED_PARTYHAT).broadcastWorldMessage(true),
        new MboxItem(BLUE_PARTYHAT).broadcastWorldMessage(true),
        new MboxItem(GREEN_PARTYHAT).broadcastWorldMessage(true),
        new MboxItem(WHITE_PARTYHAT).broadcastWorldMessage(true),
        new MboxItem(PURPLE_PARTYHAT).broadcastWorldMessage(true)
    };

    private static final MboxItem[] RARE = new MboxItem[]{
        new MboxItem(_3RD_AGE_LONGSWORD).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_BOW).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_WAND).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_PICKAXE).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_FULL_HELMET).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_PLATEBODY).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_PLATELEGS).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_KITESHIELD).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_MAGE_HAT).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_ROBE_TOP).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_ROBE).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_RANGE_COIF).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_RANGE_TOP).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_RANGE_LEGS).broadcastWorldMessage(true),
        new MboxItem(_3RD_AGE_VAMBRACES).broadcastWorldMessage(true),
        new MboxItem(FANCY_BOOTS).broadcastWorldMessage(true),
        new MboxItem(FIGHTING_BOOTS).broadcastWorldMessage(true),

    };

    private static final MboxItem[] UNCOMMON = new MboxItem[]{
        new MboxItem(GREEN_HALLOWEEN_MASK),
        new MboxItem(RED_HALLOWEEN_MASK),
        new MboxItem(BLUE_HALLOWEEN_MASK),
        new MboxItem(SANTA_HAT),
        new MboxItem(4084),
        new MboxItem(EASTER_BASKET),
        new MboxItem(CAPE_OF_SKULLS),
    };

    private static final MboxItem[] COMMON = new MboxItem[]{
        new MboxItem(SANTA_MASK),
        new MboxItem(SANTA_JACKET),
        new MboxItem(SANTA_PANTALOONS),
        new MboxItem(SANTA_GLOVES),
        new MboxItem(SANTA_BOOTS),
        new MboxItem(FLIPPERS),
        new MboxItem(GOLDEN_CHEFS_HAT),
        new MboxItem(GOLDEN_APRON),
        new MboxItem(BUCKET_HELM_G),
        new MboxItem(GILDED_DHIDE_VAMBRACES),
        new MboxItem(GILDED_DHIDE_BODY),
        new MboxItem(GILDED_DHIDE_CHAPS),
        new MboxItem(GILDED_2H_SWORD),
        new MboxItem(GILDED_SCIMITAR),
        new MboxItem(GILDED_AXE),
        new MboxItem(GILDED_PICKAXE),
        new MboxItem(GILDED_HASTA),
        new MboxItem(GILDED_SQ_SHIELD),
        new MboxItem(SCYTHE),
        new MboxItem(GNOME_SCARF),
        new MboxItem(RAINBOW_SCARF),
        new MboxItem(HEAVY_CASKET),
        new MboxItem(HAM_JOINT),
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
