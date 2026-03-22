package com.twisted.game.content.collection_logs.impl;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;


public class SlayerKey implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{};
    }

    @Override
    public String name() {
        return "Slayer Key";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.SLAYER_KEY;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.SLAYER_KEYS_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.KEYS;
    }
}
