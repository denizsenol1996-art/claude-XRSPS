package com.twisted.game.content.security;

import com.twisted.game.content.security.impl.ChangeAccountPin;
import com.twisted.game.content.security.impl.RemoveAccountPin;
import com.twisted.game.content.security.impl.SetupAccountPin;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.dialogue.Expression;

import static com.twisted.util.NpcIdentifiers.SECURITY_GUARD;

/**
 * @author Patrick van Elderen | April, 29, 2021, 18:18
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class SecurityAdvisorDialogue extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Set Account PIN", "Change Account PIN", "Remove Account PIN", "Nevermind");
        setPhase(0);
    }

    @Override
    protected void next() {
        if (isPhase(1)) {
            stop();
            player.setEnterSyntax(new SetupAccountPin());
            player.getPacketSender().sendEnterAmountPrompt("Fill in your account pin. (Exactly 5 digits)");
        }
        if (isPhase(2)) {
            stop();
        }
    }

    @Override
    protected void select(int option) {
        if (isPhase(0)) {
            if (option == 1) {
                var pin = player.<Integer>getAttribOr(AttributeKey.ACCOUNT_PIN, 0);
                String pinToString = Integer.toString(pin);
                if (pinToString.length() == 5) {
                    send(DialogueType.NPC_STATEMENT, SECURITY_GUARD, Expression.HAPPY, "You already have an account pin.");
                    setPhase(2);
                    return;
                }
                send(DialogueType.PLAYER_STATEMENT, Expression.ANNOYED, "I would like to setup an Account PIN.");
                setPhase(1);
            }
            if (option == 2) {
                stop();
                var pin = player.<Integer>getAttribOr(AttributeKey.ACCOUNT_PIN, 0);
                String pinToString = Integer.toString(pin);
                if (pinToString.length() != 5) {
                    send(DialogueType.NPC_STATEMENT, SECURITY_GUARD, Expression.HAPPY, "You have to setup an account pin first.");
                    setPhase(2);
                    return;
                }

                player.setEnterSyntax(new ChangeAccountPin());
                player.getPacketSender().sendEnterAmountPrompt("Confirm your pin first.");
            }
            if (option == 3) {
                var pin = player.<Integer>getAttribOr(AttributeKey.ACCOUNT_PIN, 0);
                String pinToString = Integer.toString(pin);
                if (pinToString.length() != 5) {
                    send(DialogueType.NPC_STATEMENT, SECURITY_GUARD, Expression.HAPPY, "You have to setup an account pin first.");
                    setPhase(2);
                    return;
                }
                stop();
                player.setEnterSyntax(new RemoveAccountPin());
                player.getPacketSender().sendEnterAmountPrompt("Confirm your pin in order to remove it.");
            }
            if (option == 4) {
                stop();
            }
        }
    }
}
