package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

public class MoltenMysteryBox implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{new MysteryBoxItem(ItemIdentifiers.LAVA_DRAGON_MASK, 5, -1), new MysteryBoxItem(CustomItemIdentifiers.LAVA_WHIP, 5, -1), new MysteryBoxItem(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX, 20, -1), new MysteryBoxItem(CustomItemIdentifiers.LAVA_PARTYHAT, 20, -1), new MysteryBoxItem(CustomItemIdentifiers.MOLTEN_PARTYHAT, 50, -1), new MysteryBoxItem(CustomItemIdentifiers.LAVA_DHIDE_COIF, 50, -1), new MysteryBoxItem(CustomItemIdentifiers.LAVA_DHIDE_BODY, 50, -1), new MysteryBoxItem(CustomItemIdentifiers.LAVA_DHIDE_CHAPS, 50, -1), new MysteryBoxItem(CustomItemIdentifiers.MOLTEN_DEFENDER, 60, -1), new MysteryBoxItem(ItemIdentifiers.INFERNAL_CAPE, 100, -1, true), new MysteryBoxItem(CustomItemIdentifiers.MYSTERY_TICKET, 150, -1, true)};
    }

    @Override
    public String name() {
        return "Molten Mystery Box";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.MOLTEN_MYSTERY_BOX;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.MOLTEN_MYSTERY_BOXES_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
