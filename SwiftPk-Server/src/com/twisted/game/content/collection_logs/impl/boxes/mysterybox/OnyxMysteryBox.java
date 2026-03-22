package com.twisted.game.content.collection_logs.impl.boxes.mysterybox;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

public class OnyxMysteryBox implements CollectionListener {

    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR, 25, -1),
            new MysteryBoxItem(CustomItemIdentifiers.SANGUINE_TWISTED_BOW, 25, -1),
            new MysteryBoxItem(CustomItemIdentifiers.HOLY_SANGUINESTI_STAFF, 2, -1),
            new MysteryBoxItem(CustomItemIdentifiers.DEMONIC_WINGS, 25, -1),
            new MysteryBoxItem(CustomItemIdentifiers.DRAGON_COMPANION, 40, -1),
            new MysteryBoxItem(ItemIdentifiers.ENCHANTED_SALAZAR_LOCKET, 2, -1),
            new MysteryBoxItem(CustomItemIdentifiers.E_TOTEMIC_HELMET, 40, -1),
            new MysteryBoxItem(CustomItemIdentifiers.E_TOTEMIC_PLATEBODY, 40, -1),
            new MysteryBoxItem(CustomItemIdentifiers.E_TOTEMIC_PLATELEGS, 40, -1),
            new MysteryBoxItem(ItemIdentifiers.SANGUINESTI_STAFF, 2, -1)
        };
    }

    @Override
    public String name() {
        return "Onyx Mystery Box";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.ONYX_MYSTERY_BOX;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.ONYX_MYSTERY_BOXES_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
