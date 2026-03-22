package com.twisted.game.content.instance.impl;

import com.twisted.game.content.EffectTimer;
import com.twisted.game.content.instance.InstancedAreaManager;
import com.twisted.game.content.instance.SingleInstancedArea;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.List;

public class KrakenInstance {

    /**
     * The kraken instance
     */
    private SingleInstancedArea instance;

    private final List<Npc> npcList = new ArrayList<>();
    public boolean playerHasLeft;

    public KrakenInstance() {
    }

    /**
     * Begin the kraken instance
     * @param player
     */
    public void enterKrakenInstance(Player player) {
        instance = (SingleInstancedArea) InstancedAreaManager.getSingleton().createSingleInstancedArea(player, new Area(2269, 10023, 2302, 10046));
        if (player != null && instance != null) {
            npcList.clear();
            player.teleport(new Tile(2280, 10022, instance.getzLevel()));

            //Create a Kraken instance, if there isn't one already spawning.
            startUp(player);
        }

        if(instance != null && player != null) {
            instance.setOnTeleport((p, t) -> {
                // so now we check if the target tile is inside or outside of the instance, if its out, we know we're leaving, if inside, we don't care
                if (t.getZ() != instance.getzLevel()) {
                    playerHasLeft = true;
                    player.getPacketSender().sendEffectTimer(0, EffectTimer.MONSTER_RESPAWN);
                    for (Npc npc : npcList) {
                        World.getWorld().unregisterNpc(npc);
                    }
                    npcList.clear();
                    player.putAttrib(AttributeKey.TENTACLES_DISTURBED, 0);
                }
            });
        }
    }

    /**
     * Spawn the kraken and whirlpools when entering the instance.
     * @param player
     */
    public void startUp(Player player) {
        if (player != null && instance != null) {
            onSpawn();
        }
    }

    private void onSpawn() {
        Npc kraken = new Npc(KrakenBoss.KRAKEN_WHIRLPOOL, new Tile(2278, 10034, instance.getzLevel()));
        npcList.add(kraken);
        Chain.bound(null).name("KrakenInstanceSpawnTask").runFn(1, () -> World.getWorld().registerNpc(kraken)).then(2, () -> {
            // Tile 4 tentacle whirlpools at the relevant tiles
            for (Tile tile : KrakenBoss.TENT_TILES) {
                Npc tentacle = new Npc(KrakenBoss.TENTACLE_WHIRLPOOL, new Tile(tile.getX(), tile.getY(), instance.getzLevel()));
                // tent Should respawn, if killed before boss is dead.
                World.getWorld().registerNpc(tentacle);
                npcList.add(tentacle);
                tentacle.putAttrib(AttributeKey.BOSS_OWNER, kraken);

                List<Npc> list = kraken.getAttribOr(AttributeKey.MINION_LIST, new ArrayList<Npc>());
                list.add(tentacle);
                kraken.putAttrib(AttributeKey.MINION_LIST, list);
            }
        });
    }

    /**
     * get the instance
     * @return the instance
     */
    public SingleInstancedArea getInstance() {
        return instance;
    }
}
