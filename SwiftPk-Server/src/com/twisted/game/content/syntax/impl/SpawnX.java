package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.spawn_tab.SpawnTab;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Patrick van Elderen | May, 29, 2021, 03:15
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class SpawnX implements EnterSyntax {

    private final int item_id;
    private final boolean toBank;

    public SpawnX(int item_id, boolean toBank) {
        this.item_id = item_id;
        this.toBank = toBank;
    }

    @Override
    public void handleSyntax(Player player, @NotNull String input) {
    }

    @Override
    public void handleSyntax(Player player, long input) {
        if (input <= 0 || input > Integer.MAX_VALUE) {
            player.message("You can't spawn a negative amount or any more than "+Integer.MAX_VALUE+" of a item.");
            return;
        }
        SpawnTab.spawn(player, item_id, (int) input, toBank);
    }
}
