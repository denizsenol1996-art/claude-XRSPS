package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.container.ItemContainer;
import org.jetbrains.annotations.NotNull;

public class GambleX implements EnterSyntax {

    private final boolean deposit;
    private final int item_id;
    private final int slot_id;

    public GambleX(int item_id, int slot_id, boolean deposit) {
        this.item_id = item_id;
        this.slot_id = slot_id;
        this.deposit = deposit;
    }

    @Override
    public void handleSyntax(Player player, @NotNull String input) {
    }

    @Override
    public void handleSyntax(Player player, long input) {
        if (item_id < 0 || slot_id < 0 || input <= 0) {
            return;
        }

        ItemContainer to = deposit ? player.getGamblingSession().getContainer() : player.inventory();
        ItemContainer from = deposit ? player.inventory() : player.getGamblingSession().getContainer();

        player.getGamblingSession().handleItem(item_id, (int) input, slot_id, from, to);
    }

}
