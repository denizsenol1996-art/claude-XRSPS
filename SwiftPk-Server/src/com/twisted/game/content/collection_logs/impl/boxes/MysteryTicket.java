package com.twisted.game.content.collection_logs.impl.boxes;

import com.twisted.game.content.collection_logs.listener.CollectionListener;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.world.entity.AttributeKey;

import static com.twisted.game.content.collection_logs.data.LogType.MYSTERY_BOX;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.OCCULT_NECKLACE_OR;

public class MysteryTicket implements CollectionListener {
    @Override
    public MysteryBoxItem[] rewards() {
        return new MysteryBoxItem[]{new MysteryBoxItem(ARMADYL_GODSWORD_OR, 15, -1), new MysteryBoxItem(DRAGON_CLAWS_OR, 15, -1), new MysteryBoxItem(TOXIC_STAFF_OF_THE_DEAD_C, 20, -1), new MysteryBoxItem(RING_OF_PRECISION, 20, -1), new MysteryBoxItem(RING_OF_SORCERY, 20, -1), new MysteryBoxItem(RING_OF_MANHUNTING, 20, -1), new MysteryBoxItem(ANCIENT_FACEGAURD, 35, -1), new MysteryBoxItem(AMULET_OF_TORTURE_OR, 35, -1), new MysteryBoxItem(NECKLACE_OF_ANGUISH_OR, 35, -1), new MysteryBoxItem(OCCULT_NECKLACE_OR, 12, -1), new MysteryBoxItem(PEGASIAN_BOOTS_OR, 35, -1), new MysteryBoxItem(PRIMORDIAL_BOOTS_OR, 35, -1), new MysteryBoxItem(ETERNAL_BOOTS_OR, 35, -1), new MysteryBoxItem(LEGENDARY_MYSTERY_BOX, 5, -1), new MysteryBoxItem(EPIC_PET_BOX, 50, -1), new MysteryBoxItem(MYSTERY_CHEST, 75, -1, true), new MysteryBoxItem(CRAWS_BOW_C, 75, -1, true), new MysteryBoxItem(THAMMARONS_STAFF_C, 75, -1, true), new MysteryBoxItem(VIGGORAS_CHAINMACE_C, 75, -1, true)};
    }

    @Override
    public String name() {
        return "Mystery Ticket";
    }

    @Override
    public int id() {
        return MYSTERY_TICKET;
    }

    @Override
    public boolean isItem(int id) {
        return id == this.id();
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.MYSTERY_TICKETS_OPENED;
    }

    @Override
    public LogType logType() {
        return MYSTERY_BOX;
    }
}
