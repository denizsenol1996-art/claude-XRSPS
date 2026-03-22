package com.twisted.game.world.definition;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * Represents the blood money value for certain items.
 * These values specifically apply to the PVP world.
 *
 * @author PVE | Zerikoth
 */
public class BloodMoneyPrices {

    public static final Int2ObjectMap<BloodMoneyPrices> definitions = new Int2ObjectOpenHashMap<>();

    private final int id;
    private final int value;

    public BloodMoneyPrices(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public int id() {
        return id;
    }

    public int value() {
        return value;
    }
}

