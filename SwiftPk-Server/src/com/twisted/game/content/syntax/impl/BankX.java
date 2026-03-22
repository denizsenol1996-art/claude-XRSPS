package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;

public class BankX implements EnterSyntax {

    private boolean deposit;
    private int item_id;
    private int slot_id;

    public BankX(int item_id, int slot_id, boolean deposit) {
        this.item_id = item_id;
        this.slot_id = slot_id;
        this.deposit = deposit;
    }

    @Override
    public void handleSyntax(Player player, String input) {

    }

    @Override
    public void handleSyntax(Player player, long input) {
        if (item_id < 0 || slot_id < 0 || input <= 0) return;
        GroupIronman group = GroupIronman.getGroup(player.getUID());
        if (deposit) {
            if (group == null) {
                player.getBank().deposit(slot_id, (int) input);
            } else {
                group.getGroupBank().deposit(slot_id, (int) input);
                group.update();
            }
        } else {
            if (group == null) {
                if (player.getBank().quantityX) {
                    player.getBank().currentQuantityX = (int) input;
                }
            } else {
                if (group.getGroupBank().quantityX) {
                    group.getGroupBank().currentQuantityX = (int) input;
                }
            }
            if (group == null) {
                player.getBank().withdraw(item_id, slot_id, (int) input);
            } else {
                group.getGroupBank().withdraw(item_id, slot_id, (int) input);
                group.update();
            }
        }
    }

}
