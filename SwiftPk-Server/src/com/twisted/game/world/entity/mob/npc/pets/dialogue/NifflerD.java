package com.twisted.game.world.entity.mob.npc.pets.dialogue;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * @author Patrick van Elderen | November, 18, 2020, 16:28
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class NifflerD extends Dialogue {

    private static final Logger nifflerLogs = LogManager.getLogger("NifflerLogs");
    private static final Level NIFFLER_LOGS;

    static {
        NIFFLER_LOGS = Level.getLevel("NIFFLER_LOGS");
    }

    @Override
    protected void start(Object... parameters) {
        if(WildernessArea.inWilderness(player.tile())) {
            player.message("You can't take out items inside of the wilderness.");
            stop();
            return;
        }
        send(DialogueType.OPTION, "Retrieve items from pouch?", "Yes.", "No.");
        setPhase(0);
    }

    @Override
    protected void select(int option) {
        if(isPhase(0)) {
            if(option == 1) {
                var items = player.<ArrayList<Item>>getAttribOr(AttributeKey.NIFFLER_ITEMS_STORED, new ArrayList<Item>());
                if(items.isEmpty()) {
                    player.message("The Niffler has no items in his pouch.");
                    stop();
                    return;
                }
                nifflerLogs.log(NIFFLER_LOGS, "Player " + player.getUsername() + "'s niffler bank all the items: " + items);
                player.inventory().addOrBank(items);
                items.clear();
            }
            stop();
        }
    }
}
