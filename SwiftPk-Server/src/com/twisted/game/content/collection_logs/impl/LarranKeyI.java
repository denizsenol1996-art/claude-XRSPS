package com.twisted.game.content.collection_logs.impl;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import static com.twisted.util.ItemIdentifiers.*;

public class LarranKeyI implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(OPAL_DRAGON_BOLTS_E, 90, 150),
            new MysteryBoxItem(DIAMOND_DRAGON_BOLTS_E, 90, 150),
            new MysteryBoxItem(DRAGONSTONE_DRAGON_BOLTS_E, 90, 150),
            new MysteryBoxItem(ONYX_DRAGON_BOLTS_E, 90, 150),
            new MysteryBoxItem(DRAGON_DART, 90, 150),
            new MysteryBoxItem(DRAGON_KNIFE, 90, 150),
            new MysteryBoxItem(DRAGON_JAVELIN, 90, 150),
            new MysteryBoxItem(DRAGON_THROWNAXE, 90, 150),
            new MysteryBoxItem(OBSIDIAN_PLATELEGS, 90, -1),
            new MysteryBoxItem(OBSIDIAN_PLATEBODY, 90, -1),
            new MysteryBoxItem(OBSIDIAN_HELMET, 90, -1),
            new MysteryBoxItem(RANGERS_TUNIC, 90, -1),
            new MysteryBoxItem(REGEN_BRACELET, 90, -1),
            new MysteryBoxItem(GRANITE_MAUL_24227, 90, -1),
            new MysteryBoxItem(SEERS_RING_I, 90, -1),
            new MysteryBoxItem(ARCHERS_RING_I, 90, -1),
            new MysteryBoxItem(BERSERKER_RING_I, 90, -1),
            new MysteryBoxItem(WARRIOR_RING_I, 90, -1),
            new MysteryBoxItem(ABYSSAL_TENTACLE, 90, -1),
            new MysteryBoxItem(BLOOD_MONEY, 90, 50_000),
            new MysteryBoxItem(FREMENNIK_KILT, 80, -1),
            new MysteryBoxItem(BANDOS_TASSETS, 20, -1),
            new MysteryBoxItem(BANDOS_CHESTPLATE, 20, -1),
            new MysteryBoxItem(ZAMORAKIAN_HASTA, 80, -1),
            new MysteryBoxItem(DRAGON_CROSSBOW, 70, -1),
            new MysteryBoxItem(ABYSSAL_DAGGER, 60, -1),
            new MysteryBoxItem(ABYSSAL_BLUDGEON, 50, -1),
            new MysteryBoxItem(BLADE_OF_SAELDOR, 5, -1),
            new MysteryBoxItem(ZAMORAK_GODSWORD, 15, -1),
            new MysteryBoxItem(SARADOMIN_GODSWORD, 15, -1),
            new MysteryBoxItem(BANDOS_GODSWORD, 15, -1),
            new MysteryBoxItem(ARMADYL_GODSWORD, 15, -1),
            new MysteryBoxItem(ARMADYL_CHAINSKIRT, 15, -1),
            new MysteryBoxItem(ARMADYL_CHESTPLATE, 15, -1),
            new MysteryBoxItem(DINHS_BULWARK, 5, -1, true),
            new MysteryBoxItem(DRAGON_WARHAMMER, 2, -1),
            new MysteryBoxItem(DRAGON_CLAWS, 10, -1, true),
            new MysteryBoxItem(VESTAS_LONGSWORD, 5, -1, true),
            new MysteryBoxItem(STATIUSS_WARHAMMER, 5, -1, true),
            new MysteryBoxItem(NECKLACE_OF_ANGUISH, 5, -1, true),
            new MysteryBoxItem(AMULET_OF_TORTURE, 5, -1, true),
            new MysteryBoxItem(SERPENTINE_HELM, 8, -1, true),
            new MysteryBoxItem(TRIDENT_OF_THE_SWAMP, 8, -1, true),
            new MysteryBoxItem(TOXIC_BLOWPIPE, 8, -1, true),
            new MysteryBoxItem(TOXIC_STAFF_OF_THE_DEAD, 8, -1, true),
            new MysteryBoxItem(ETERNAL_BOOTS, 8, -1, true),
            new MysteryBoxItem(PRIMORDIAL_BOOTS, 8, -1, true),
            new MysteryBoxItem(PEGASIAN_BOOTS, 8, -1, true),
            new MysteryBoxItem(ELDER_MAUL, 2, -1, true),
            new MysteryBoxItem(CRYSTAL_LEGS, 2, -1, true),
            new MysteryBoxItem(CRYSTAL_BODY, 2, -1, true),
            new MysteryBoxItem(CRYSTAL_HELM, 2, -1, true),
        };
    }

    @Override
    public String name() {
        return "Larrans Key(I)";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.LARRANS_KEY_TIER_I;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.LARRANS_KEYS_TIER_ONE_USED;
    }

    @Override
    public LogType logType() {
        return LogType.KEYS;
    }
}
