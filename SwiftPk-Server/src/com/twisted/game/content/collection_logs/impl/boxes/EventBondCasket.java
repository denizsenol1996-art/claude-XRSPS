package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;

import static com.twisted.util.CustomItemIdentifiers.EVENT_BOND_CASKET;

public class EventBondCasket implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{new MysteryBoxItem(CustomItemIdentifiers.FIVE_DOLLAR_BOND, 5, -1), new MysteryBoxItem(CustomItemIdentifiers.TEN_DOLLAR_BOND, 10, -1), new MysteryBoxItem(CustomItemIdentifiers.TWENTY_DOLLAR_BOND, 100, -1), new MysteryBoxItem(CustomItemIdentifiers.FORTY_DOLLAR_BOND, 150, -1, true), new MysteryBoxItem(CustomItemIdentifiers.FIFTY_DOLLAR_BOND, 125, -1, true),new MysteryBoxItem(CustomItemIdentifiers.SEVENTY_FIVE_DOLLAR_BOND, 145, -1, true),new MysteryBoxItem(CustomItemIdentifiers.ONE_HUNDRED_DOLLAR_BOND, 500, -1, true)};
    }

    @Override
    public String name() {
        return "Bond Casket";
    }

    @Override
    public int id() {
        return EVENT_BOND_CASKET;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.BONDS_CASKET_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
