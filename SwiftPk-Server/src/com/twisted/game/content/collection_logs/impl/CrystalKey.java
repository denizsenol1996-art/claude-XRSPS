package com.twisted.game.content.collection_logs.impl;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.ItemIdentifiers;

import static com.twisted.util.ItemIdentifiers.*;

public class CrystalKey implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(DRAGONFIRE_SHIELD, 2, -1, true),
            new MysteryBoxItem(MASTER_WAND, 10, -1, true),
            new MysteryBoxItem(MAGES_BOOK, 10, -1, true),
            new MysteryBoxItem(AMULET_OF_FURY, 10, -1),
            new MysteryBoxItem(DARK_BOW, 10, -1),
            new MysteryBoxItem(STAMINA_POTION4 + 1, 25, 10),
            new MysteryBoxItem(DRAGON_BOOTS + 1, 25, -1),
            new MysteryBoxItem(DRAGON_DART, 25, 150),
            new MysteryBoxItem(DRAGON_KNIFE, 25, 100),
            new MysteryBoxItem(DRAGON_JAVELIN, 25, 100),
            new MysteryBoxItem(ANGLERFISH + 1, 25, 50),
            new MysteryBoxItem(GUTHIX_REST4 + 1, 25, 50),
            new MysteryBoxItem(RING_OF_RECOIL + 1, 25, 50)
        };
    }

    @Override
    public String name() {
        return "Crystal Key";
    }

    @Override
    public int id() {
        return ItemIdentifiers.CRYSTAL_KEY;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.CRYSTAL_KEYS_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.KEYS;
    }
}
