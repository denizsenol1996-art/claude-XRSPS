package com.twisted.game.content.bank_pin.dialogue;

import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.dialogue.Expression;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class ChangeRecoveryDelayDialogue extends Dialogue {

    private final int npcId;

    public ChangeRecoveryDelayDialogue(int npcId) {
        this.npcId = npcId;
    }

    @Override
    protected void start(Object... parameters) {
        sendMainMenu();
    }

    @Override
    protected void next() {
        switch (getPhase()) {
            case 0:
                player.setEnterSyntax(new EnterSyntax() {
                    @Override
                    public void handleSyntax(Player player, long input) {
                        if (input < 3) {
                            sendInvalid("shorter than 3 days");
                        } else if (input > 30) {
                            sendInvalid("longer than 30 days");
                        } else {
                            player.getBankPin().changeRecoveryDays((int) input);
                            stop();
                        }
                    }
                });
                player.getPacketSender().sendEnterAmountPrompt("Enter your new recovery delay.");
                break;
            case 1:
                sendMainMenu();
                break;
        }
    }

    private void sendInvalid(String type) {
        send(DialogueType.NPC_STATEMENT, npcId, Expression.DEFAULT, "The recovery delay cannot be " + type + ".");
        setPhase(1);
    }

    private void sendMainMenu() {
        send(DialogueType.NPC_STATEMENT, npcId, Expression.DEFAULT, "How long would you like the recovery delay to be?");
        setPhase(0);
    }
}
