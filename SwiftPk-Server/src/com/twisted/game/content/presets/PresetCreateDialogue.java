package com.twisted.game.content.presets;

import com.twisted.game.content.syntax.impl.CreatePreset;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;

/**
 * The dialogue for creating a new preset
 *
 * @author Patrick van Elderen | dinsdag 21 mei 2019 (CEST) : 09:12
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 */
public class PresetCreateDialogue extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.STATEMENT, "That preset slot is empty. Create a new one in its place?");
        setPhase(0);
    }

    @Override
    public void next() {
        if (isPhase(0)) {
            send(DialogueType.OPTION, "Select option", "Yes", "Cancel");
            setPhase(1);
        }
    }

    @Override
    public void select(int option) {
        if (isPhase(1)) {
            setPhase(2);
            switch (option) {
                case 1 -> {
                    final int presetIndex = player.getPresetIndex();
                    if (player.getPresets()[presetIndex] == null) {
                        //Reset the dialogue, were sending a new interface after! So we don't wanne be stuck in this dialogue!
                        stop();
                        player.setEnterSyntax(new CreatePreset(presetIndex));
                        player.getPacketSender().sendEnterInputPrompt("Enter a name for your preset below.");
                    }
                }
                case 2 -> stop();
            }
        }
    }
}
