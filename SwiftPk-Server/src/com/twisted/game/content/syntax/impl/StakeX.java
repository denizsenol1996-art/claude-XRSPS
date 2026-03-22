package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.container.ItemContainer;

public class StakeX implements EnterSyntax {

    private boolean deposit;
    private int item_id;
    private int slot_id;

    public StakeX(int item_id, int slot_id, boolean deposit) {
        this.item_id = item_id;
        this.slot_id = slot_id;
        this.deposit = deposit;
    }

    @Override
    public void handleSyntax(Player player, String input) {
    }

    @Override
    public void handleSyntax(Player player, long input) {
        if (item_id < 0 || slot_id < 0 || input <= 0) {
            return;
        }

        ItemContainer to = deposit ? player.getDueling().getContainer() : player.inventory();
        ItemContainer from = deposit ? player.inventory() : player.getDueling().getContainer();

        player.getDueling().handleItem(item_id, (int) input, slot_id, from, to);
    }

}
