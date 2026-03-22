package com.twisted.game.content.areas.edgevile.dialogue;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.dialogue.Expression;

/**
 * The general dialogue
 */
public class GeneralStoreDialogue extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.NPC_STATEMENT, player.getInteractingNpcId(), Expression.DEFAULT, "Can I help you at all?");
        setPhase(0);
    }

    @Override
    public void next() {
        if (isPhase(0)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes please. What are you selling?", "No thanks.");
            setPhase(1);
        }
    }

    @Override
    public void select(int option) {
        if (isPhase(1)) {
            switch (option) {
                case 1 ->
                    //Open general store
                    World.getWorld().shop(1).open(player);
                case 2 ->
                    //Cancel option
                    stop();
            }
        }
    }
}
