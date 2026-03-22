package com.twisted.game.world.entity.combat;

import lombok.Data;
@Data
public class BannedItem {
    private final int itemId;
    private final int equipSlot;
    public BannedItem(int itemId, int equipSlot) {
        this.itemId = itemId;
        this.equipSlot = equipSlot;
    }
}

