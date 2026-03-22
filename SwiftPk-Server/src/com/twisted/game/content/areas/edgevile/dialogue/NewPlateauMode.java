package com.twisted.game.content.areas.edgevile.dialogue;

import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.dialogue.Expression;
import com.twisted.game.world.entity.mob.player.GameMode;
import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.rights.PlayerRights;

import static com.twisted.util.NpcIdentifiers.IRON_MAN_TUTOR;

/**
 * Auth the plateau //faw
 */

public class NewPlateauMode extends Dialogue {
    @Override
    protected void start(Object... parameters) {
        send(DialogueType.NPC_STATEMENT, IRON_MAN_TUTOR, Expression.NODDING_THREE, "Are you sure you want to change your mode?");
        setPhase(0);
    }

    @Override
    protected void next() {
        if (isPhase(0)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes Please change it.", "Nevermind.");
            setPhase(1);
        } else if (isPhase(2)) {
            stop();
        }
    }
    /*@Override
    protected void start(Object... parameters) {
        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Change My Mode.", "Nevermind.");
        setPhase(0);
    }//check updates bro okay!

    @Override
    public void next() {
        if (isPhase(0)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes Change it.", "No thanks.");
            setPhase(2);
        } else if (isPhase(4)) {
            stop();
        }
    }*/

    @Override
    public void select(int option) {
        if (isPhase(1)) {
            if (option == 1) {
                if (player.ironMode() == IronMode.NONE && player.mode() != GameMode.INSTANT_PKER) {
                    player.getPacketSender().sendMessage("You need to be an iron man or instant pker to to use this ");
                    player.getPacketSender().closeDialogue();
                    return;
                }
                if (player.mode() == GameMode.INSTANT_PKER && option == 1) {//dzone3
                    GameMode accountType = player.mode(GameMode.TRAINED_ACCOUNT);
                    player.mode(accountType);
                    player.resetSkills();
                    player.ironMode(IronMode.NONE);
                    player.getPacketSender().closeDialogue();
                    player.getPacketSender().sendMessage("Please relog to take the effect ");

                } else {
                    player.setPlayerRights(PlayerRights.PLAYER);
                    player.ironMode(IronMode.NONE);
                    player.mode(GameMode.TRAINED_ACCOUNT);
                    player.getPacketSender().closeDialogue();
                    player.getPacketSender().sendMessage("You're not ironman anymore and can't change it again ");
                    player.getPacketSender().sendMessage("Please relog to take the effect ");

                }


            } else if (option == 2) {
                stop();
            }


        }
    }
}
