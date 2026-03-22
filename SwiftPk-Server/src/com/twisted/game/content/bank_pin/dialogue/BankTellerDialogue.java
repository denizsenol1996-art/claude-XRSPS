package com.twisted.game.content.bank_pin.dialogue;

import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.dialogue.Expression;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class BankTellerDialogue extends Dialogue {
    private int npcId;

    @Override
    protected void start(Object... parameters) {
        Npc npc = (Npc) parameters[0];
        npcId = npc.id();
        player.face(npc.tile());
        npc.face(player.tile());
        send(DialogueType.NPC_STATEMENT, npcId, Expression.DEFAULT, "Good day, how may I help you?");
        setPhase(0);
    }

    @Override
    protected void next() {
        if (isPhase(0)) {
            send(DialogueType.OPTION,
                "Select an option.",
                "I'd like to access my bank account, please.",
                "I'd like to check my PIN settings.");
            setPhase(1);
        }
    }

    @Override
    protected void select(int option) {
        if (isPhase(1)) {
            if (option == 1) {
                GroupIronman group = GroupIronman.getGroup(player.getUID());
                if (group == null) {
                    player.getBank().open();
                } else {
                    group.getGroupBank().open(player, group);
                }
            } else if (option == 2) {
                player.getBankPinSettings().open(npcId);
            }
        }
    }
}
