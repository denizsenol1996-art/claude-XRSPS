package com.twisted.game.content.collection_logs.impl;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

public class EnchantedKeysI implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(DOUBLE_DROPS_LAMP, 20, -1),
            new MysteryBoxItem(TOME_OF_FIRE, 40, -1),
            new MysteryBoxItem(IMBUEMENT_SCROLL, 50, -1),
            new MysteryBoxItem(MASTER_WAND, 80, -1),
            new MysteryBoxItem(SEERS_RING, 80, -1),
            new MysteryBoxItem(MAGES_BOOK, 80, -1),
            new MysteryBoxItem(OCCULT_NECKLACE, 80, -1),
            new MysteryBoxItem(BLESSED_SPIRIT_SHIELD, 90, -1),
            new MysteryBoxItem(BLOOD_MONEY_CASKET, 60, -1),
            new MysteryBoxItem(ENCHANTED_BONES, 90, 25)
        };
    }

    @Override
    public String name() {
        return "Enchanted Key(I)";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.ENCHANTED_KEY_I;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.ENCHANTED_KEYS_R_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.KEYS;
    }
}
