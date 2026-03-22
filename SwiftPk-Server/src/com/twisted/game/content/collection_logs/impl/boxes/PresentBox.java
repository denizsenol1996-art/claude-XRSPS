package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

public class PresentBox implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(CustomItemIdentifiers.SNOWBIRD, 80, -1),
            new MysteryBoxItem(CustomItemIdentifiers.ELDER_ICE_MAUL, 100, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.DRAGON_HUNTER_CROSSBOW_T, 90, -1),
            new MysteryBoxItem(CustomItemIdentifiers.FROST_IMBUED_CAPE, 50, -1),
            new MysteryBoxItem(CustomItemIdentifiers.INFINITY_WINTER_BOOTS, 15, -1),
            new MysteryBoxItem(CustomItemIdentifiers.MYSTERY_TICKET, 120, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX , 80, -1),
            new MysteryBoxItem(CustomItemIdentifiers.SNOWY_SLED, 2, -1),
            new MysteryBoxItem(CustomItemIdentifiers.UGLY_SANTA_HAT, 2, -1),
            new MysteryBoxItem(CustomItemIdentifiers.ICED_SANTA_HAT, 30, -1),
            new MysteryBoxItem(ItemIdentifiers.WISE_OLD_MANS_SANTA_HAT, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.ELDER_MAUL , 90, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.FROST_CLAWS, 30, -1),
            new MysteryBoxItem(CustomItemIdentifiers.ARMADYL_FROSTSWORD, 30, -1),
            new MysteryBoxItem(ItemIdentifiers.BLACK_SANTA_HAT, 10, -1),
            new MysteryBoxItem(ItemIdentifiers.INVERTED_SANTA_HAT, 10, -1),
            new MysteryBoxItem(ItemIdentifiers.SANTA_HAT , 10, -1),
            new MysteryBoxItem(ItemIdentifiers.CHRISTMAS_CRACKER, 120, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.ABYSSAL_TENTACLE_24948, 2, -1),
            new MysteryBoxItem(CustomItemIdentifiers.FROST_WHIP, 2, -1),
            new MysteryBoxItem(CustomItemIdentifiers.DONATOR_MYSTERY_BOX, 2, -1)
        };
    }
    @Override
    public String name() {
        return "Present Mystery Box";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.PRESENT_MYSTERY_BOX;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.PRESENTS_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
