package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

public class LegendaryMysteryBox implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(ItemIdentifiers.AMULET_OF_FURY, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.DRAGON_BOOTS, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.OCCULT_NECKLACE, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.LARRANS_KEY, 8, 3),
            new MysteryBoxItem(CustomItemIdentifiers.VOTE_TICKET, 8, 5),
            new MysteryBoxItem(ItemIdentifiers.PRIMORDIAL_BOOTS, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.PEGASIAN_BOOTS, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.ETERNAL_BOOTS, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.AMULET_OF_TORTURE, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.NECKLACE_OF_ANGUISH, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.TORMENTED_BRACELET, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.RING_OF_SUFFERING, 25, -1),
            new MysteryBoxItem(CustomItemIdentifiers.DONATOR_TICKET, 25, 100),
            new MysteryBoxItem(CustomItemIdentifiers.LARRANS_KEY_TIER_II, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.SPECTRAL_SPIRIT_SHIELD, 50, -1),
            new MysteryBoxItem(CustomItemIdentifiers.LARRANS_KEY_TIER_III, 50, -1),
            new MysteryBoxItem(ItemIdentifiers.STATIUSS_FULL_HELM, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.STATIUSS_PLATEBODY, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.STATIUSS_PLATELEGS, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.MORRIGANS_COIF, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.MORRIGANS_LEATHER_BODY, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.MORRIGANS_LEATHER_CHAPS, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.ZURIELS_HOOD, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.ZURIELS_ROBE_TOP, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.ZURIELS_ROBE_BOTTOM, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.VESTAS_CHAINBODY, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.VESTAS_PLATESKIRT, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.INQUISITORS_GREAT_HELM, 90, -1, true),
            new MysteryBoxItem(ItemIdentifiers.INQUISITORS_HAUBERK, 90, -1, true),
            new MysteryBoxItem(ItemIdentifiers.INQUISITORS_PLATESKIRT, 90, -1, true),
            new MysteryBoxItem(ItemIdentifiers.INQUISITORS_MACE, 90, -1, true),
            new MysteryBoxItem(ItemIdentifiers.ELDER_MAUL, 90, -1, true),
            new MysteryBoxItem(ItemIdentifiers.ARCANE_SPIRIT_SHIELD, 110, -1, true),
            new MysteryBoxItem(ItemIdentifiers.AVERNIC_DEFENDER, 110, -1, true),
            new MysteryBoxItem(ItemIdentifiers.INFERNAL_CAPE, 110, -1, true),
            new MysteryBoxItem(ItemIdentifiers.FEROCIOUS_GLOVES, 110, -1, true),
            new MysteryBoxItem(ItemIdentifiers.VOLATILE_NIGHTMARE_STAFF, 110, -1, true),
            new MysteryBoxItem(ItemIdentifiers.HARMONISED_NIGHTMARE_STAFF, 110, -1, true),
            new MysteryBoxItem(ItemIdentifiers.DEXTEROUS_PRAYER_SCROLL, 125, -1, true),
            new MysteryBoxItem(ItemIdentifiers.ARCANE_PRAYER_SCROLL, 110, -1, true),
            new MysteryBoxItem(ItemIdentifiers.SANGUINESTI_STAFF, 110, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.MYSTERY_TICKET, 140, -1, true)
        };
    }

    @Override
    public String name() {
        return "Legendary Mystery Box";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.LEGENDARY_MYSTERY_BOXES_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
