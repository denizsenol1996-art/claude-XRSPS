package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;

public class PriceCheckX implements EnterSyntax {

    private boolean deposit;
    private int id;
    private int slot_id;

    public PriceCheckX(int id, int slot_id, boolean deposit) {
        this.id = id;
        this.slot_id = slot_id;
        this.deposit = deposit;
    }

    @Override
    public void handleSyntax(Player player, String input) {
    }

    @Override
    public void handleSyntax(Player player, long input) {
        if (id < 0 || slot_id < 0 || input <= 0) {
            return;
        }
        if (deposit) {
            player.getPriceChecker().deposit(slot_id, (int) input);
        } else {
           player.getPriceChecker().withdraw(id, (int) input);
        }
    }

}
