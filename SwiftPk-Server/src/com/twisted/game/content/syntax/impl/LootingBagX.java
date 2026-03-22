package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import org.jetbrains.annotations.NotNull;

public class LootingBagX implements EnterSyntax {

    private final boolean bank_deposit;
    private final int item_id;
    private final int slot_id;

    public LootingBagX(int item_id, int slot_id, boolean deposit) {
        this.item_id = item_id;
        this.slot_id = slot_id;
        this.bank_deposit = deposit;
    }

    @Override
    public void handleSyntax(Player player, @NotNull String input) {
    }

    @Override
    public void handleSyntax(Player player, long input) {
        if (item_id < 0 || slot_id < 0 || input <= 0) {
            return;
        }
        if (!bank_deposit) {
            //If the looting bag has already deleted the item but somehow dialogue is still open. Make sure the looting bag still contains said item.
            if(!player.inventory().contains(item_id)) {
                return;
            }
            player.getLootingBag().deposit(new Item(item_id, (int) input), (int) input, null);
        } else {
            //If the looting bag has already deleted the item but somehow dialogue is still open. Make sure the looting bag still contains said item.
            if(!player.getLootingBag().contains(item_id)) {
                return;
            }

            if (input > player.getLootingBag().count(item_id))
                input = player.getLootingBag().count(item_id);

           // System.out.println("depositing "+input+" "+new Item(item_id, input).getName()+" to bank account");

           player.getLootingBag().withdrawBank(new Item(item_id, (int) input), slot_id);
        }
    }

}
