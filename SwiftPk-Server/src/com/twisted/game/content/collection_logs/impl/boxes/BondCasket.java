package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;

import static com.twisted.util.CustomItemIdentifiers.BONDS_CASKET;

public class BondCasket implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{new MysteryBoxItem(CustomItemIdentifiers.FIVE_DOLLAR_BOND, 5, -1), new MysteryBoxItem(CustomItemIdentifiers.TEN_DOLLAR_BOND, 5, -1), new MysteryBoxItem(CustomItemIdentifiers.TWENTY_DOLLAR_BOND, 10, -1), new MysteryBoxItem(CustomItemIdentifiers.FORTY_DOLLAR_BOND, 20, -1, true),new MysteryBoxItem(CustomItemIdentifiers.FIFTY_DOLLAR_BOND, 50, -1, true),new MysteryBoxItem(CustomItemIdentifiers.SEVENTY_FIVE_DOLLAR_BOND, 120, -1, true),new MysteryBoxItem(CustomItemIdentifiers.ONE_HUNDRED_DOLLAR_BOND, 500, -1, true)};
    }

    @Override
    public String name() {
        return "Bond Casket";
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public int id() {
        return BONDS_CASKET;
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
