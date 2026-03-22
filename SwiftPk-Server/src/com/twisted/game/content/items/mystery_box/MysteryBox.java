package com.twisted.game.content.items.mystery_box;

import com.twisted.game.content.items.mystery_box.impl.*;
import com.twisted.game.world.entity.AttributeKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract system for mystery boxes.
 *
 * @author Patrick van Elderen | 27-8-2018 : 13:58
 * @see <a href="https://www.rune-server.ee/members/_Patrick_/">Rune-Server
 * profile</a>
 */
public abstract class MysteryBox {

    /**
     * Map of all the mystery boxes.
     */
    private static final Map<Integer, MysteryBox> MYSTERY_BOXES = new HashMap<>();

    /**
     * Handles loading the mystery boxes.
     */
    public static void load() {
        MysteryBox MYSTERY_BOX = new RegularMysteryBox();
        MysteryBox DONATOR_MYSTERY_BOX = new DonatorMysteryBox();
        MysteryBox VOTEBOSS_MYSTERY_BOX = new VoteBossMysteryBox();
        MysteryBox HALLOWEEN_MYSTERY_BOX = new HalloweenMysteryBox();

        MYSTERY_BOXES.put(MYSTERY_BOX.mysteryBoxId(), MYSTERY_BOX);
        MYSTERY_BOXES.put(VOTEBOSS_MYSTERY_BOX.mysteryBoxId(), VOTEBOSS_MYSTERY_BOX);
        MYSTERY_BOXES.put(DONATOR_MYSTERY_BOX.mysteryBoxId(), DONATOR_MYSTERY_BOX);
        MYSTERY_BOXES.put(HALLOWEEN_MYSTERY_BOX.mysteryBoxId(), HALLOWEEN_MYSTERY_BOX);
    }

    /** Handles getting the mystery box. */
    public static Optional<MysteryBox> getMysteryBox(int item) {
        return MYSTERY_BOXES.containsKey(item) ? Optional.of(MYSTERY_BOXES.get(item)) : Optional.empty();
    }

    /** The name of the mystery box. */
    protected abstract String name();

    /** The item identification of the mystery box. */
    protected abstract int mysteryBoxId();

    /**
     * Roll chances and return a reward
     */
    public abstract MboxItem rollReward();

    /**
     * Collect mutliple tiers of rares/common arrays into one array
     */
    public abstract MboxItem[] allPossibleRewards();

    public abstract AttributeKey key();
}
