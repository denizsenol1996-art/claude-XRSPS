package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.ItemIdentifiers;

import static com.twisted.util.CustomItemIdentifiers.*;

public class MysteryChest implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{new MysteryBoxItem(TWISTED_BOW_I, 150, -1, true), new MysteryBoxItem(HOLY_SCYTHE_OF_VITUR, 150, -1, true), new MysteryBoxItem(HOLY_SANGUINESTI_STAFF, 150, -1, true), new MysteryBoxItem(ItemIdentifiers.ELYSIAN_SPIRIT_SHIELD, 150, -1, true), new MysteryBoxItem(ANCESTRAL_HAT_I, 110, -1, true), new MysteryBoxItem(ANCESTRAL_ROBE_TOP_I, 110, -1, true), new MysteryBoxItem(ANCESTRAL_ROBE_BOTTOM_I, 110, -1, true), new MysteryBoxItem(SALAZAR_SLYTHERINS_LOCKET, 110, -1, true), new MysteryBoxItem(ENCHANTED_BOOTS, 110, -1, true), new MysteryBoxItem(CLOAK_OF_INVISIBILITY, 60, -1), new MysteryBoxItem(TOM_RIDDLE_DIARY, 60, -1), new MysteryBoxItem(DARK_ARMADYL_HELMET, 25, -1), new MysteryBoxItem(DARK_ARMADYL_CHESTPLATE, 25, -1), new MysteryBoxItem(DARK_ARMADYL_CHAINSKIRT, 25, -1), new MysteryBoxItem(DARK_BANDOS_CHESTPLATE, 25, -1), new MysteryBoxItem(DARK_BANDOS_TASSETS, 25, -1), new MysteryBoxItem(ELDER_WAND_HANDLE, 70, -1), new MysteryBoxItem(ELDER_WAND_STICK, 70, -1), new MysteryBoxItem(SWORD_OF_GRYFFINDOR, 55, -1), new MysteryBoxItem(TALONHAWK_CROSSBOW, 35, -1), new MysteryBoxItem(25881, 25, -1), new MysteryBoxItem(16169, 60, -1), new MysteryBoxItem(SHADOW_INQUISITOR_ORNAMENT_KIT, 20, -1), new MysteryBoxItem(SHADOW_GREAT_HELM, 65, -1), new MysteryBoxItem(SHADOW_HAUBERK, 65, -1), new MysteryBoxItem(SHADOW_PLATESKIRT, 65, -1)};
    }

    @Override
    public String name() {
        return "Mystery Chest";
    }

    @Override
    public int id() {
        return MYSTERY_CHEST;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.MYSTERY_CHESTS_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
