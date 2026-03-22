package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

public class RaidsMysteryBox implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(ItemIdentifiers.TWISTED_BOW, 100, -1, true),
            new MysteryBoxItem(ItemIdentifiers.SCYTHE_OF_VITUR, 100, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.HOLY_GHRAZI_RAPIER, 70, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.TOM_RIDDLE_DIARY, 50, -1),
            new MysteryBoxItem(CustomItemIdentifiers.CLOAK_OF_INVISIBILITY, 50, -1),
            new MysteryBoxItem(CustomItemIdentifiers.SWORD_OF_GRYFFINDOR, 50, -1),
            new MysteryBoxItem(CustomItemIdentifiers.TWISTED_BOW_KIT, 5, -1),
            new MysteryBoxItem(CustomItemIdentifiers.SHADOW_INQUISITOR_ORNAMENT_KIT, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.SANGUINESTI_STAFF, 70, -1, true),
            new MysteryBoxItem(ItemIdentifiers.AVERNIC_DEFENDER, 10, -1),
            new MysteryBoxItem(ItemIdentifiers.ELDER_MAUL, 10, -1),
            new MysteryBoxItem(ItemIdentifiers.DRAGON_CLAWS, 10, -1),
            new MysteryBoxItem(ItemIdentifiers.GHRAZI_RAPIER, 10, -1),
            new MysteryBoxItem(ItemIdentifiers.ANCESTRAL_HAT, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.ANCESTRAL_ROBE_TOP, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.ANCESTRAL_ROBE_BOTTOM, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.KODAI_WAND, 25, -1)
        };
    }

    @Override
    public String name() {
        return "Raids Mystery Box";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.RAIDS_MYSTERY_BOX;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.RAIDS_MYSTERY_BOXES_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
