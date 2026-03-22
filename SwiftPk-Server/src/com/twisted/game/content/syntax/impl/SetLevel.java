package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;

public class SetLevel implements EnterSyntax {

    private final int skill;

    public SetLevel(int skill) {
        this.skill = skill;
    }

    @Override
    public void handleSyntax(Player player, String input) {

    }

    @Override
    public void handleSyntax(Player player, long input) {
        if (input <= 0 || input > 99) {
            player.message("Invalid syntax. Please enter a level in the range of 1-99.");
            return;
        }
        player.skills().clickSkillToChangeLevel(skill, (int) input);
    }
}
