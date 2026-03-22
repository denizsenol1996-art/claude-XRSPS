package com.twisted.game.content.collection_logs.impl;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;

import static com.twisted.util.CustomItemIdentifiers.*;

public class EnchantedKeyII implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(MYSTERY_TICKET, 1, -1, true),
            new MysteryBoxItem(ANATHEMA_WARD, 15, -1, true),
            new MysteryBoxItem(ENCHANTED_KEY_I, 15, -1),
            new MysteryBoxItem(DERANGED_MANIFESTO, 15, -1, true),
            new MysteryBoxItem(RING_OF_DIVINATION, 15, -1, true),
            new MysteryBoxItem(ANATHEMATIC_STONE, 15, -1)
        };
    }

    @Override
    public String name() {
        return "Enchanted Key(II)";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.ENCHANTED_KEY_II;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.ENCHANTED_KEYS_P_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.KEYS;
    }
}
