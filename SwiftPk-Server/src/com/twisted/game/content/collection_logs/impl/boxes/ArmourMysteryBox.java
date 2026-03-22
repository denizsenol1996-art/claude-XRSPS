package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

public class ArmourMysteryBox implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(ItemIdentifiers.INQUISITORS_GREAT_HELM, 150, -1, true),
            new MysteryBoxItem(ItemIdentifiers.INQUISITORS_HAUBERK, 150, -1, true),
            new MysteryBoxItem(ItemIdentifiers.INQUISITORS_PLATESKIRT, 150, -1, true),
            new MysteryBoxItem(ItemIdentifiers.DARK_INFINITY_HAT, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.DARK_INFINITY_TOP, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.DARK_INFINITY_BOTTOMS, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.LIGHT_INFINITY_HAT, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.LIGHT_INFINITY_TOP, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.LIGHT_INFINITY_BOTTOMS, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.ARMADYL_CHESTPLATE, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.ARMADYL_PLATESKIRT, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.BANDOS_CHESTPLATE, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.BANDOS_TASSETS, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.ARMADYL_HELMET, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.BOOTS_OF_BRIMSTONE, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.DAGONHAI_HAT, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.DAGONHAI_ROBE_TOP, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.DAGONHAI_ROBE_BOTTOM, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.DRAGON_FULL_HELM, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.DRAGON_PLATEBODY, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.GUTHANS_ARMOUR_SET, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.VERACS_ARMOUR_SET, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.TORAGS_ARMOUR_SET, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.KARILS_ARMOUR_SET, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.DHAROKS_ARMOUR_SET, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.DRAGON_DEFENDER, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.FIGHTER_TORSO, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.BLESSED_SPIRIT_SHIELD, 1, -1),
            new MysteryBoxItem(ItemIdentifiers.ROBIN_HOOD_HAT, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.RANGER_BOOTS, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.DRAGON_BOOTS, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.INFINITY_BOOTS, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.INFINITY_HAT, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.INFINITY_TOP, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.INFINITY_BOTTOMS, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.MAGES_BOOK, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.OBSIDIAN_HELMET, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.OBSIDIAN_PLATEBODY, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.OBSIDIAN_PLATELEGS, 2, -1),

        };
    }

    @Override
    public String name() {
        return "Amour Mystery Box";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.ARMOUR_MYSTERY_BOX;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.ARMOUR_MYSTERY_BOXES_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
