package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;

public class RunePouchX implements EnterSyntax {

    private int item_id;
    private int slot;
    private boolean deposit;

    public RunePouchX(int item_id, int slot, boolean deposit) {
        this.item_id = item_id;
        this.slot = slot;
        this.deposit = deposit;
    }

    @Override
    public void handleSyntax(Player player, String input) {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleSyntax(Player player, long input) {
        if (item_id < 0 || slot < 0 || input <= 0) {
            return;
        }
        if(deposit) {
            player.getRunePouch().deposit(new Item(item_id, (int) input));
        } else {
            player.getRunePouch().withdraw(item_id, (int) input);
        }
    }
}
