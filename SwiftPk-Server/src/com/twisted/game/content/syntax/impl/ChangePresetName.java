package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Utils;
import org.jetbrains.annotations.NotNull;


public class ChangePresetName implements EnterSyntax {

    private final int presetIndex;

    public ChangePresetName(final int presetIndex) {
        this.presetIndex = presetIndex;
    }

    @Override
    public void handleSyntax(Player player, @NotNull String input) {
        input = Utils.formatText(input);

        if (!Utils.isValidName(input)) {
            player.message("Invalid name for preset. Please enter characters only.");
            player.setCurrentPreset(null);
            player.getPresetManager().open();
            return;
        }

        if (player.getPresets()[presetIndex] != null) {

            player.getPresets()[presetIndex].setName(input);
            player.message("The presets name has been updated.");

            player.getPresetManager().open();
        }
    }

    @Override
    public void handleSyntax(Player player, long input) {
    }

}
