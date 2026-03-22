package com.twisted.game.content.skill.impl.slayer.content;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.Gargoyle;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.NpcIdentifiers;

import java.util.Arrays;
import java.util.List;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * mei 15, 2020
 */
public class RockHammer {

    private static final List<Integer> GARGOYLES = Arrays.asList(NpcIdentifiers.GARGOYLE);

    public static boolean onItemOnNpc(Player player, Npc npc) {
        for (int GARGOYLES : GARGOYLES) {
            if (npc.id() == GARGOYLES) {
                int item = player.getAttrib(AttributeKey.ITEM_ID);

                if (item == ItemIdentifiers.ROCK_HAMMER) {
                    Gargoyle.smash(player, npc, true);
                }
                return true;
            }
        }
        return false;
    }
}
