package com.twisted.game.content.instance.impl;

import com.twisted.game.content.EffectTimer;
import com.twisted.game.content.instance.InstancedAreaManager;
import com.twisted.game.content.instance.SingleInstancedArea;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.method.impl.npcs.hydra.AlchemicalHydra;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.List;

public class AlchemicalHydraInstance {

    /**
     * The Alchemical hydra instance
     */
    private SingleInstancedArea instance;

    /**
     * get the instance
     *
     * @return the instance
     */
    public SingleInstancedArea getInstance() {
        return instance;
    }

    public List<Npc> npcList = new ArrayList<>();

    public AlchemicalHydraInstance() {

    }

    public static final Area ALCHEMICAL_HYDRA_AREA = new Area(1356, 10257, 1377, 10278);
    public static final Tile ENTRANCE_POINT = new Tile(1356, 10258);
    public static final Tile HYDRA_SPAWN_TILE = new Tile(1364, 10265);

    public void enterInstance(Player player) {
        instance = (SingleInstancedArea) InstancedAreaManager.getSingleton().createSingleInstancedArea(player, ALCHEMICAL_HYDRA_AREA);
        if (player != null && instance != null) {
            npcList.clear();
            player.teleport(ENTRANCE_POINT.transform(0, 0, instance.getzLevel()));

            //Create a Alchemical hydra instance, if there isn't one already spawning.
            var hydra = new AlchemicalHydra(HYDRA_SPAWN_TILE.transform(0, 0, instance.getzLevel()), player);
            hydra.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN,25);
            World.getWorld().registerNpc(hydra);
            npcList.add(hydra);
        }
        if(instance != null && player != null) {
            instance.setOnTeleport((p, t) -> {
                // so now we check if the target tile is inside or outside of the instance, if its out, we know we're leaving, if inside, we don't care
                if (t.getZ() != instance.getzLevel()) {
                    playerHasLeft = true;
                    player.getPacketSender().sendEffectTimer(0, EffectTimer.MONSTER_RESPAWN);
                }
            });
        }
    }

    public boolean playerHasLeft;

    public void clear() {
        for (Npc npc : npcList) {
            World.getWorld().unregisterNpc(npc);
        }
        npcList.clear();
    }

    public void death(Player killer) {
        int respawnTimer = 50;
        if (killer != null) {
            killer.getPacketSender().sendEffectTimer((int) Utils.ticksToSeconds(respawnTimer), EffectTimer.MONSTER_RESPAWN);

            Chain.bound(null).cancelWhen(() -> {
                return playerHasLeft; // cancels as expected
            }).runFn(respawnTimer, () -> {
                //Create a new Alchemical hydra instance
                if(instance == null) {
                    return;
                }
                var hydra = new AlchemicalHydra(HYDRA_SPAWN_TILE.transform(0, 0, instance.getzLevel()), killer);
                World.getWorld().registerNpc(hydra);
                npcList.add(hydra);
            });
        }
    }
}
