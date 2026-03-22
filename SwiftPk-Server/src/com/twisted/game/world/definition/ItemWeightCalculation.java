package com.twisted.game.world.definition;

import java.util.HashMap;
import java.util.Map;

public class ItemWeightCalculation {
    private final int id;
    private final int value;

    public static final Map<Integer, ItemWeightCalculation> definitions = new HashMap<>();
    public ItemWeightCalculation(int id, int value) {
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
