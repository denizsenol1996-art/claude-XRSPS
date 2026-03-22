package com.twisted.game.content.collection_logs.impl;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

public class WildernessKey implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(TOME_OF_FIRE, 50, -1),
            new MysteryBoxItem(DRAGON_BOOTS, 50, -1),
            new MysteryBoxItem(MAGES_BOOK, 50, -1),
            new MysteryBoxItem(RANGER_BOOTS, 50, -1),
            new MysteryBoxItem(OCCULT_NECKLACE, 50, -1),
            new MysteryBoxItem(AMULET_OF_FURY, 50, -1),
            new MysteryBoxItem(INFINITY_BOOTS, 50, -1),
            new MysteryBoxItem(AHRIMS_ARMOUR_SET, 50, -1),
            new MysteryBoxItem(KARILS_ARMOUR_SET, 50, -1),
            new MysteryBoxItem(DHAROKS_ARMOUR_SET, 50, -1),
            new MysteryBoxItem(DARK_BOW, 25, -1),
            new MysteryBoxItem(IMBUED_HEART, 25, -1),
            new MysteryBoxItem(ARMADYL_GODSWORD, 25, -1),
            new MysteryBoxItem(DRAGON_CLAWS, 15, -1),
            new MysteryBoxItem(ETERNAL_BOOTS, 15, -1),
            new MysteryBoxItem(PEGASIAN_BOOTS, 15, -1),
            new MysteryBoxItem(PRIMORDIAL_BOOTS, 15, -1),
            new MysteryBoxItem(NEITIZNOT_FACEGUARD, 15, -1),
            new MysteryBoxItem(VESTAS_LONGSWORD, 5, -1, true),
            new MysteryBoxItem(STATIUSS_WARHAMMER, 5, -1, true),
            new MysteryBoxItem(TOXIC_STAFF_OF_THE_DEAD, 15, -1),
            new MysteryBoxItem(MORRIGANS_LEATHER_CHAPS, 5, -1, true),
            new MysteryBoxItem(MORRIGANS_LEATHER_BODY, 5, -1, true),
            new MysteryBoxItem(ZURIELS_ROBE_BOTTOM, 5, -1, true),
            new MysteryBoxItem(ZURIELS_ROBE_TOP, 5, -1, true),
            new MysteryBoxItem(STATIUSS_PLATELEGS, 5, -1, true),
            new MysteryBoxItem(STATIUSS_PLATEBODY, 5, -1, true),
            new MysteryBoxItem(VESTAS_PLATESKIRT, 5, -1, true),
            new MysteryBoxItem(VESTAS_CHAINBODY, 5, -1, true),
            new MysteryBoxItem(ARMADYL_FROSTSWORD, 4, -1, true),
            new MysteryBoxItem(AMULET_OF_TORTURE, 4, -1, true),
            new MysteryBoxItem(NECKLACE_OF_ANGUISH, 4, -1, true),
            new MysteryBoxItem(GHRAZI_RAPIER, 3, -1, true),
            new MysteryBoxItem(ELDER_MAUL, 3, -1, true),
            new MysteryBoxItem(KODAI_WAND, 3, -1, true),
            new MysteryBoxItem(ETERNAL_BOOTS_OR, 3, -1, true),
            new MysteryBoxItem(PEGASIAN_BOOTS_OR, 3, -1, true),
            new MysteryBoxItem(PRIMORDIAL_BOOTS_OR, 3, -1, true),
            new MysteryBoxItem(ELDRITCH_ORB, 2, -1, true),
            new MysteryBoxItem(VOLATILE_ORB, 2, -1, true),
            new MysteryBoxItem(HARMONISED_ORB, 2, -1, true)
        };
    }

    @Override
    public String name() {
        return "Wilderness Key";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.WILDERNESS_KEY;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.WILDY_KEYS_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.KEYS;
    }
}
