package com.twisted.game.content.bank_pin.dialogue;

import com.twisted.game.content.bank_pin.BankPin;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;

/**
 * @author lare96 <http://github.com/lare96>
 */
public class IncorrectBankPinDialogue extends Dialogue {

    private final BankPin bankPin;
    private final Runnable action;

    public IncorrectBankPinDialogue(BankPin bankPin, Runnable action) {
        this.bankPin = bankPin;
        this.action = action;
    }

    @Override
    protected void start(Object... parameters) {
        setPhase(0);
        send(DialogueType.STATEMENT, "<col=ca0d0d>That number was incorrect.", "Please try again.");
    }

    @Override
    protected void next() {
        if (getPhase() == 0) {
            action.run();
        }
    }
}
