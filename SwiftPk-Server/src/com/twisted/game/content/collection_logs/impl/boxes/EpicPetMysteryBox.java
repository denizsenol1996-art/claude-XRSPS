package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

public class EpicPetMysteryBox implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{
            new MysteryBoxItem(CustomItemIdentifiers.SNOWBIRD, 5, -1),
            new MysteryBoxItem(CustomItemIdentifiers.ELVARG_JR, 5, -1),
            new MysteryBoxItem(ItemIdentifiers.JALNIBREK, 8, -1),
            new MysteryBoxItem(ItemIdentifiers.LITTLE_NIGHTMARE, 60, -1),
            new MysteryBoxItem(CustomItemIdentifiers.RING_OF_ELYSIAN, 5, -1),
            new MysteryBoxItem(CustomItemIdentifiers.BLOOD_MONEY_PET, 5, -1),
            new MysteryBoxItem(CustomItemIdentifiers.KERBEROS_PET, 15, -1),
            new MysteryBoxItem(CustomItemIdentifiers.SKORPIOS_PET, 15, -1),
            new MysteryBoxItem(CustomItemIdentifiers.ARACHNE_PET, 15, -1),
            new MysteryBoxItem(CustomItemIdentifiers.ARTIO_PET, 15, -1),
            new MysteryBoxItem(CustomItemIdentifiers.JAWA_PET, 140, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.DEMENTOR_PET, 80, -1),
            new MysteryBoxItem(CustomItemIdentifiers.FENRIR_GREYBACK_JR, 60, -1),
            new MysteryBoxItem(CustomItemIdentifiers.FLUFFY_JR, 140, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.ANCIENT_KING_BLACK_DRAGON_PET, 35, -1),
            new MysteryBoxItem(CustomItemIdentifiers.ANCIENT_CHAOS_ELEMENTAL_PET, 35, -1),
            new MysteryBoxItem(CustomItemIdentifiers.ANCIENT_BARRELCHEST_PET, 35, -1),
            new MysteryBoxItem(CustomItemIdentifiers.NIFFLER, 140, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.BARRELCHEST_PET, 35, -1),
            new MysteryBoxItem(CustomItemIdentifiers.WAMPA, 20, -1),
            new MysteryBoxItem(CustomItemIdentifiers.BABY_ARAGOG, 20, -1),
            new MysteryBoxItem(CustomItemIdentifiers.FOUNDER_IMP, 160, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.CENTAUR_MALE, 90, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.CENTAUR_FEMALE, 90, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.BABY_LAVA_DRAGON, 25, -1),
            new MysteryBoxItem(CustomItemIdentifiers.JALTOK_JAD, 25, -1),
            new MysteryBoxItem(CustomItemIdentifiers.MINI_NECROMANCER, 35, -1),
            new MysteryBoxItem(CustomItemIdentifiers.PET_CORRUPTED_NECHRYARCH, 35, -1),
            new MysteryBoxItem(ItemIdentifiers.TZREKZUK, 35, -1),
            new MysteryBoxItem(CustomItemIdentifiers.GRIM_REAPER_PET, 100, -1, true),
            new MysteryBoxItem(CustomItemIdentifiers.GENIE_PET, 100, -1),
            new MysteryBoxItem(CustomItemIdentifiers.DHAROK_PET, 25, -1),
            new MysteryBoxItem(CustomItemIdentifiers.PET_ZOMBIES_CHAMPION, 10, -1),
            new MysteryBoxItem(CustomItemIdentifiers.BABY_ABYSSAL_DEMON, 5, -1),
            new MysteryBoxItem(CustomItemIdentifiers.BABY_DARK_BEAST_EGG, 5, -1),
            new MysteryBoxItem(CustomItemIdentifiers.BABY_SQUIRT, 80, -1, true)};
    }

    @Override
    public String name() {
        return "Epic Pet Mystery Box";
    }

    @Override
    public int id() {
        return CustomItemIdentifiers.EPIC_PET_BOX;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.EPIC_PET_MYSTERY_BOXES_OPENED;
    }

    @Override
    public LogType logType() {
        return LogType.MYSTERY_BOX;
    }
}
