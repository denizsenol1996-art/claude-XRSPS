package com.twisted.game.content.instance.impl;

import com.twisted.game.content.instance.InstancedAreaManager;
import com.twisted.game.content.instance.SingleInstancedArea;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.vorkath.VorkathState;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;

import java.util.ArrayList;
import java.util.List;

import static com.twisted.util.NpcIdentifiers.VORKATH_8059;

/**
 * @author Patrick van Elderen | February, 11, 2021, 09:01
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class VorkathInstance {

    /**
     * The Vorkath instance
     */
    private SingleInstancedArea instance;

    /**
     * get the instance
     * @return the instance
     */
    public SingleInstancedArea getInstance() {
        return instance;
    }

    public List<Npc> npcList = new ArrayList<>();

    public VorkathInstance() {

    }

    public Npc sleepingVorkath;
    public Npc vorkath;

    public static final Tile ENTRANCE_POINT = new Tile(2272, 4054);
    public static final Area VORKATH_AREA = new Area(2260, 4054, 2286, 4077);

    public void enterInstance(Player player) {
        instance = (SingleInstancedArea) InstancedAreaManager.getSingleton().createSingleInstancedArea(player, VORKATH_AREA);
        if (player != null && instance != null) {
            npcList.clear();
            player.teleport(ENTRANCE_POINT.transform(0, 0, instance.getzLevel()));

            //Create a Vorkath instance
            //sleepingVorkath = new Npc(VORKATH_8059, ENTRANCE_POINT.transform(-3, 9, instance.getzLevel()));
            sleepingVorkath = new Npc(VORKATH_8059, ENTRANCE_POINT.transform(-3, 9, instance.getzLevel()));

            Npc vorkath = sleepingVorkath;
            vorkath.getMovementQueue().setBlockMovement(true);
            World.getWorld().registerNpc(vorkath);
            npcList.add(vorkath);

            //Just to make sure when entering the area reset vorkath's state
            player.setVorkathState(VorkathState.SLEEPING);
        }
    }

    public void clear(Player player) {
        for (Npc npc : npcList) {
            World.getWorld().unregisterNpc(npc);
        }
        npcList.clear();

        //Reset state upon leaving
        player.setVorkathState(VorkathState.SLEEPING);
    }
}
