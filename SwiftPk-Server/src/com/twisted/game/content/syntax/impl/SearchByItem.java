package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.DropsDisplay;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juli 7, 2020
 */
public class SearchByItem implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
        DropsDisplay.search(player, input, DropsDisplay.Type.ITEM);
    }

    @Override
    public void handleSyntax(Player player, long input) {

    }
}
