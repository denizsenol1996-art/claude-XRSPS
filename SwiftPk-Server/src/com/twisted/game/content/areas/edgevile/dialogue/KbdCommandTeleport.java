package com.twisted.game.content.areas.edgevile.dialogue;

import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.position.Tile;

/**
 * Auth the plateau
 */

public class KbdCommandTeleport extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Are sure you want to teleport?.<col=FF0000>(42 Wild)", "Nevermind.");
        setPhase(0);
    }

    @Override
    public void next() {
        if (isPhase(2)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes Teleport.", "No thanks.");
            setPhase(3);
        } else if (isPhase(4)) {
            stop();
        }
    }

    @Override
    public void select(int option) {
        if(isPhase(0)) {
            if (option == 1) {
                if (!Teleports.canTeleport(player, true, TeleportType.ABOVE_20_WILD)) {
                    stop();
                    return;
                }
                Teleports.basicTeleport(player, new Tile(3001,3849,0));
            } else if (option == 2) {
                stop();
            }

        }
    }

}

