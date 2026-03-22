package com.twisted.game.content.collection_logs.impl;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;

import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.CRYSTAL_HELM;

public class LarransKeyII implements CollectionListener {

    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(NEITIZNOT_FACEGUARD, 20, -1),
            new MysteryBoxItem(ARMADYL_GODSWORD_OR, 5, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.DRAGON_CLAWS_OR, 5, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.ELDER_ICE_MAUL, 5, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.BOW_OF_FAERDHINEN, 2, -1, true),
            new MysteryBoxItem(CRYSTAL_HELM, 5, -1, true),
            new MysteryBoxItem(CRYSTAL_BODY, 5, -1, true),
            new MysteryBoxItem(CRYSTAL_LEGS, 5, -1, true),
            new MysteryBoxItem(VOLATILE_ORB, 5, -1, true),
            new MysteryBoxItem(HARMONISED_ORB, 5, -1, true),
            new MysteryBoxItem(ELDRITCH_ORB, 5, -1, true),
            new MysteryBoxItem(NIGHTMARE_STAFF, 10, -1, true),
            new MysteryBoxItem(ELDER_MAUL, 10, -1, true),
            new MysteryBoxItem(ARMADYL_GODSWORD, 30, -1),
            new MysteryBoxItem(DRAGON_CLAWS, 30, -1),
            new MysteryBoxItem(PRIMORDIAL_BOOTS, 30, -1),
            new MysteryBoxItem(PEGASIAN_BOOTS, 30, -1),
            new MysteryBoxItem(ETERNAL_BOOTS, 30, -1),
            new MysteryBoxItem(TOXIC_STAFF_OF_THE_DEAD, 30, -1),
            new MysteryBoxItem(TOXIC_BLOWPIPE, 30, -1),
            new MysteryBoxItem(TRIDENT_OF_THE_SWAMP, 30, -1),
            new MysteryBoxItem(MAGMA_HELM, 30, -1),
            new MysteryBoxItem(TANZANITE_HELM, 30, -1),
            new MysteryBoxItem(SERPENTINE_HELM, 40, -1),
            new MysteryBoxItem(AMULET_OF_TORTURE, 20, -1),
            new MysteryBoxItem(NECKLACE_OF_ANGUISH, 20, -1),
            new MysteryBoxItem(STATIUSS_WARHAMMER, 15, -1, true),
            new MysteryBoxItem(VESTAS_LONGSWORD, 15, -1, true),
            new MysteryBoxItem(DRAGON_WARHAMMER, 15, -1, true),
            new MysteryBoxItem(DINHS_BULWARK, 15, -1, true),
            new MysteryBoxItem(ARMADYL_CHESTPLATE, 40, -1),
            new MysteryBoxItem(ARMADYL_CHAINSKIRT, 40, -1),
            new MysteryBoxItem(BANDOS_GODSWORD, 60, -1),
            new MysteryBoxItem(SARADOMIN_GODSWORD, 60, -1),
            new MysteryBoxItem(ZAMORAK_GODSWORD, 60, -1),
            new MysteryBoxItem(BLADE_OF_SAELDOR, 5, -1, true),
            new MysteryBoxItem(ABYSSAL_BLUDGEON, 60, -1),
            new MysteryBoxItem(ABYSSAL_DAGGER, 80, -1),
            new MysteryBoxItem(DRAGON_SCIMITAR_OR, 90, -1),
            new MysteryBoxItem(DRAGON_CROSSBOW, 90, -1),
            new MysteryBoxItem(ZAMORAKIAN_HASTA, 40, -1),
            new MysteryBoxItem(BANDOS_CHESTPLATE, 40, -1),
            new MysteryBoxItem(BANDOS_TASSETS, 40, -1)
        };
    }

    @Override
    public String name() {
        return "Larrans Key(II)";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.LARRANS_KEY_TIER_II;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.LARRANS_KEYS_TIER_TWO_USED;
    }

    @Override
    public LogType logType() {
        return LogType.KEYS;
    }
}
