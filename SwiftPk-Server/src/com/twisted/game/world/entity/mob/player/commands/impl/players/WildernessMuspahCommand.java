package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.Tile;

public class WildernessMuspahCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes <col=FF0000>(80 Wild/Teleblock)", "No.");
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
                if (isPhase(0)) {
                    if (option == 1) {
                        Teleports.basicTeleport(player, new Tile(3155, 3977, 0));
                        player.teleblock(250, true);
                    } else if (option == 2) {
                        stop();
                    }

                }
            }
        });
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
