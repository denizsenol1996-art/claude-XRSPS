package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

public class WeaponMysteryBox implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(ItemIdentifiers.DRAGON_JAVELIN, 2, 250),
            new MysteryBoxItem(ItemIdentifiers.ABYSSAL_TENTACLE, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.DARK_BOW, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.GRANITE_MAUL_24225, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.MASTER_WAND, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.ZAMORAK_GODSWORD, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.SARADOMIN_GODSWORD, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.BANDOS_GODSWORD, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.STAFF_OF_BALANCE, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.STAFF_OF_THE_DEAD, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.STAFF_OF_LIGHT, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.DRAGON_CROSSBOW, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.LIGHT_BALLISTA, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.SARADOMINS_BLESSED_SWORD, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.BARRELCHEST_ANCHOR, 2, -1),
            new MysteryBoxItem(ItemIdentifiers.ZAMORAKIAN_SPEAR, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.ABYSSAL_DAGGER, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.ABYSSAL_DAGGER_P_13271, 15, -1),
            new MysteryBoxItem(ItemIdentifiers.BLADE_OF_SAELDOR, 70, -1, true),
            new MysteryBoxItem(ItemIdentifiers.ZAMORAKIAN_HASTA, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.HEAVY_BALLISTA, 25, -1),
            new MysteryBoxItem(ItemIdentifiers.VESTAS_SPEAR, 100, -1, true),
            new MysteryBoxItem(ItemIdentifiers.ZURIELS_STAFF, 100, -1, true),
            new MysteryBoxItem(ItemIdentifiers.DRAGON_WARHAMMER, 150, -1, true),
            new MysteryBoxItem(ItemIdentifiers.DRAGON_SWORD, 75, -1),
            new MysteryBoxItem(ItemIdentifiers.DRAGON_CLAWS, 75, -1),
            new MysteryBoxItem(ItemIdentifiers.ARMADYL_CROSSBOW, 75, -1),
            new MysteryBoxItem(ItemIdentifiers.ARMADYL_GODSWORD, 75, -1),
            new MysteryBoxItem(ItemIdentifiers.TOXIC_BLOWPIPE, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.TOXIC_STAFF_OF_THE_DEAD, 75, -1, true),
            new MysteryBoxItem(ItemIdentifiers.STATIUSS_WARHAMMER, 100, -1, true),
            new MysteryBoxItem(ItemIdentifiers.VESTAS_LONGSWORD, 150, -1, true),
            new MysteryBoxItem(ItemIdentifiers.INQUISITORS_MACE, 150, -1, true),
        };
    }

    @Override
    public String name() {
        return "Weapon Mystery Box";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.WEAPON_MYSTERY_BOX;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.WEAPON_MYSTERY_BOXES_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
