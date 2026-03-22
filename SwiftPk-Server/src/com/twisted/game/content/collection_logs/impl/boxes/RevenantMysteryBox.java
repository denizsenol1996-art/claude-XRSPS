package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

public class RevenantMysteryBox implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(ItemIdentifiers.VIGGORAS_CHAINMACE, 50, -1, true),
            new MysteryBoxItem(ItemIdentifiers.CRAWS_BOW, 50, -1, true),
            new MysteryBoxItem(ItemIdentifiers.THAMMARONS_SCEPTRE, 50, -1, true),
            new MysteryBoxItem(ItemIdentifiers.AMULET_OF_AVARICE, 50, -1),
            new MysteryBoxItem(ItemIdentifiers.STATIUSS_FULL_HELM, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.STATIUSS_PLATEBODY, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.STATIUSS_PLATELEGS, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.STATIUSS_WARHAMMER , 25, -1),
            new MysteryBoxItem(ItemIdentifiers.VESTAS_PLATESKIRT, 10, -1),
            new MysteryBoxItem(ItemIdentifiers.VESTAS_CHAINBODY, 10, -1),
            new MysteryBoxItem(ItemIdentifiers.VESTAS_LONGSWORD, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.MORRIGANS_COIF, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.MORRIGANS_LEATHER_BODY, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.MORRIGANS_LEATHER_CHAPS, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.ZURIELS_HOOD, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.ZURIELS_ROBE_TOP, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.ZURIELS_ROBE_BOTTOM, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.ZURIELS_STAFF, 2, -1)};
    }

    @Override
    public String name() {
        return "Revenant Mystery Box";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.REVENANT_MYSTER_BOX;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.REVENANT_MYSTERY_BOXES_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
