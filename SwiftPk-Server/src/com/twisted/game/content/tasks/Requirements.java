package com.twisted.game.content.tasks;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * A utility class for all the task requirements.
 * @author Patrick van Elderen | April, 08, 2021, 21:52
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class Requirements {

    public static boolean hasZulrahPet(Player player) {
        return player.hasPetOut("snakeling");
    }

    public static long bmRisk(Player player) {
        return player.getAttribOr(AttributeKey.RISKED_WEALTH, 0L);
    }

}
