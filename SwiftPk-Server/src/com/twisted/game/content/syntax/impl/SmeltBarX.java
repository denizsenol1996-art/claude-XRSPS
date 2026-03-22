package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.skill.impl.smithing.Bar;
import com.twisted.game.content.skill.impl.smithing.Smelting;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * @author Patrick van Elderen | November, 15, 2020, 12:13
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class SmeltBarX implements EnterSyntax {

    /**
     * The bar to smelt.
     */
    private final Bar bar;

    public SmeltBarX(Bar bar) {
        this.bar = bar;
    }

    @Override
    public void handleSyntax(Player player, String input) {
    }

    @Override
    public void handleSyntax(Player player, long input) {
        if (input <= 0 || input > Integer.MAX_VALUE) {
            return;
        }
        player.skills().startSkillable(new Smelting(bar, (int) input));
    }
}
