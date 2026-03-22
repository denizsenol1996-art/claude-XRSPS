package com.hazy.entity;

import com.hazy.cache.def.ItemDefinition;
import com.hazy.entity.model.Model;

public final class Item extends Renderable {

    public Item() {
    }

    public Item(int id, int amount) {
        this.id = id;
        this.quantity = amount;
    }

    public final Model get_rotated_model() {
        ItemDefinition itemDef = ItemDefinition.get(id);
        return itemDef.get_model(quantity);
    }

    public ItemDefinition getDefinition() {
        return ItemDefinition.get(id);
    }

    public int id;
    public int x;
    public int y;
    public int quantity;
}
