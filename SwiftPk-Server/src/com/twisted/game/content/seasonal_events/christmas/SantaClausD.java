package com.twisted.game.content.seasonal_events.christmas;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.dialogue.Expression;
import com.twisted.game.world.items.Item;
import com.twisted.util.Color;
import com.twisted.util.Utils;

import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.NpcIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 12, 2021
 */
public class SantaClausD extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        if (player.getAttribOr(AttributeKey.FINISHED_HALLOWEEN_TEACHER_DIALOGUE, false)) {
            send(DialogueType.NPC_STATEMENT, SANTA_CLAUS, Expression.HAPPY, "Have you found any of the presents yet?");
            setPhase(10);
            return;
        }
        send(DialogueType.NPC_STATEMENT, WINTER_SOLDIER, Expression.SAD, "That good for nothing dragon killed Sir Capalot!");
        setPhase(0);
    }

    @Override
    protected void next() {
        if (isPhase(0)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "What are you talking about?", "Santa, you're the boss...whats going on?");
            setPhase(1);
        } else if (isPhase(3)) {
            send(DialogueType.PLAYER_STATEMENT, Expression.DEFAULT, "What can I do to help?");
            setPhase(4);
        } else if (isPhase(4)) {
            send(DialogueType.NPC_STATEMENT, SANTA_CLAUS, Expression.CALM_TALK, "To save christmas, you must find these demons!", "Please bring the presents back to me...", "I need to get back to the North Pole before Christmas!");
            setPhase(5);
        } else if (isPhase(5)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "I can help.", "No thanks find 'em yourself.");
            setPhase(6);
        } else if (isPhase(10)) {
            if (player.inventory().contains(GIANT_PRESENT)) {
                send(DialogueType.PLAYER_STATEMENT, Expression.DEFAULT, "I have found some of your presents!");
                setPhase(12);
            } else {
                send(DialogueType.PLAYER_STATEMENT, Expression.DEFAULT, "Sadly not yet, I will come back when I've found some!");
                setPhase(13);
            }
        } else if (isPhase(12)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Give Santa your presents.", "Keep them for yourself.");
            setPhase(12);
        } else if (isPhase(13)) {
            stop();
        }
    }

    @Override
    protected void select(int option) {
        if (isPhase(1)) {
            if (option == 1) {
                send(DialogueType.NPC_STATEMENT, WINTER_SOLDIER, Expression.SAD, "Santa's stuck here alone!", "There's an evil dragon...it killed my friend.", "There's also some sort of demons roaming around.");
                setPhase(13);
            }
            if (option == 2) {
                send(DialogueType.NPC_STATEMENT, SANTA_CLAUS, Expression.SLIGHTLY_SAD, "I got attacked by these creatures. They were covered in ice.", "They stole all the presents that we had ready.");
                setPhase(3);
            }
        }
        if (isPhase(6)) {
            if (option == 1) {
                send(DialogueType.ITEM_STATEMENT, new Item(SACK_OF_PRESENTS), "", "Santa hands you his sack to collect", "presents.");
                setPhase(13);
                player.inventory().addOrDrop(new Item(SACK_OF_PRESENTS));
                player.putAttrib(AttributeKey.FINISHED_HALLOWEEN_TEACHER_DIALOGUE,true);
            }
            if (option == 2) {
                stop();
            }
        }
        if (isPhase(12)) {
            if (option == 1) {
                int count = player.inventory().count(GIANT_PRESENT);
                player.inventory().remove(GIANT_PRESENT, count);
                var candies = player.<Integer>getAttribOr(AttributeKey.CANDIES_TRADED,0)+ count;
                player.putAttrib(AttributeKey.CANDIES_TRADED, candies);
                player.message(Color.PURPLE.wrap("You have now retrieved "+ Utils.formatNumber(candies) +" presents"));
                player.inventory().addOrBank(new Item(PRESENT_13346, count));
                String plural = count > 1 ? "presents" : "present";
                player.message(Color.PURPLE.wrap("Santa gave you "+ Utils.formatNumber(count)+ " " +plural+" in return."));
                stop();
            }
            if (option == 2) {
                stop();
            }
        }
    }
}
