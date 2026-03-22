package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.presets.PresetManager;
import com.twisted.game.content.presets.Presetable;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.Utils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;


public class CreatePreset implements EnterSyntax {

    private static final Logger presetLogs = LogManager.getLogger("PresetLogs");
    private static final Level PRESET;

    static {
        PRESET = Level.getLevel("PRESET");
    }

    private final int presetIndex;

    public CreatePreset(final int presetIndex) {
        this.presetIndex = presetIndex;
    }

    @Override
    public void handleSyntax(Player player, @NotNull String input) {
        input = Utils.formatText(input);

        if (!Utils.isValidName(input)) {
            player.message("Invalid name for preset.");
            player.setCurrentPreset(null);
            player.getPresetManager().open();
            return;
        }

        if (player.getPresets()[presetIndex] == null) {

            //Get stats..
            int[] stats = new int[7];
            for (int i = 0; i < stats.length; i++) {
                stats[i] = player.skills().xpLevel(i);
            }

            Item[] inventory = player.inventory().copyValidItemsArray();
            Item[] equipment = player.getEquipment().copyValidItemsArray();
            for (Item t : Utils.concat(inventory, equipment)) {
                if (Arrays.stream(PresetManager.ILLEGAL_ITEMS).anyMatch(id -> t.getId() == id)) {
                    player.message("You cannot create a preset with the following item: "+ t.definition(World.getWorld()).name);
                    return;
                }
                
                if (t.noted()) {
                    player.message("You cannot create presets which contain noted items.");
                    return;
                }
            }
            presetLogs.log(PRESET, "Player: "+ player.getUsername() + " successfully made a preset with the following items -> equipment: " + Arrays.toString(equipment)+" inventory: "+ Arrays.toString(inventory));

            player.getPresets()[presetIndex] = new Presetable(input, presetIndex, inventory, equipment, stats, player.getSpellbook(), false, player.getRunePouch().stream().filter(Objects::nonNull).map(Item::copy).toArray(Item[]::new));
            player.setCurrentPreset(player.getPresets()[presetIndex]);
            player.getPresetManager().open();
        }
    }

    @Override
    public void handleSyntax(Player player, long input) {
    }

}

