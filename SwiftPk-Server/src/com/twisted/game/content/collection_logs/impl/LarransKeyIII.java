package com.twisted.game.content.collection_logs.impl;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;

import static com.twisted.util.ItemIdentifiers.*;

public class LarransKeyIII implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(CustomItemIdentifiers.CORRUPTED_CRYSTAL_BODY, 2, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.CORRUPTED_CRYSTAL_LEGS, 2, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.CORRUPTED_CRYSTAL_HELM, 2, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.CORRUPTING_STONE, 5, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.BOW_OF_FAERDHINEN_C, 1, -1, true),
            new MysteryBoxItem(25881, 5, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.ABYSSAL_TENTACLE_24948, 25, -1, true),
            new MysteryBoxItem(NEITIZNOT_FACEGUARD, 25, -1, true),
            new MysteryBoxItem(ARMADYL_GODSWORD_OR, 10, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.DRAGON_CLAWS_OR, 10, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.ELDER_ICE_MAUL, 10, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.BOW_OF_FAERDHINEN, 10, -1, true),
            new MysteryBoxItem(CRYSTAL_HELM, 10, -1, true),
            new MysteryBoxItem(CRYSTAL_BODY, 10, -1, true),
            new MysteryBoxItem(CRYSTAL_LEGS, 10, -1, true),
            new MysteryBoxItem(VOLATILE_ORB, 15, -1, true),
            new MysteryBoxItem(HARMONISED_ORB, 15, -1, true),
            new MysteryBoxItem(ELDRITCH_ORB, 15, -1, true),
            new MysteryBoxItem(ELDER_MAUL, 25, -1),
            new MysteryBoxItem(PRIMORDIAL_BOOTS, 30, -1),
            new MysteryBoxItem(PEGASIAN_BOOTS, 30, -1),
            new MysteryBoxItem(ETERNAL_BOOTS, 30, -1),
            new MysteryBoxItem(TOXIC_STAFF_OF_THE_DEAD, 30, -1),
            new MysteryBoxItem(TOXIC_BLOWPIPE, 30, -1),
            new MysteryBoxItem(TRIDENT_OF_THE_SWAMP, 30, -1),
            new MysteryBoxItem(SERPENTINE_HELM, 30, -1),
            new MysteryBoxItem(AMULET_OF_TORTURE, 20, -1),
            new MysteryBoxItem(NECKLACE_OF_ANGUISH, 20, -1),
            new MysteryBoxItem(STATIUSS_WARHAMMER, 15, -1, true),
            new MysteryBoxItem(VESTAS_LONGSWORD, 15, -1, true),
            new MysteryBoxItem(DRAGON_WARHAMMER, 15, -1, true),
            new MysteryBoxItem(DRAGON_CLAWS, 60, -1, true)
        };
    }

    @Override
    public String name() {
        return "Larrans Key(III)";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.LARRANS_KEY_TIER_III;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.LARRANS_KEYS_TIER_THREE_USED;
    }

    @Override
    public LogType logType() {
        return LogType.KEYS;
    }
}
