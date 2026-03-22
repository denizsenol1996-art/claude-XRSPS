package com.twisted.game.content.instance.impl;

import com.twisted.game.content.instance.InstancedAreaManager;
import com.twisted.game.content.instance.SingleInstancedArea;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.zulrah.Zulrah;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.util.NpcIdentifiers;
import com.twisted.util.Tuple;
import com.twisted.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick van Elderen | February, 10, 2021, 10:27
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class ZulrahInstance {

    /**
     * The Zulrah instance
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

    public ZulrahInstance() {

    }

    private static final Tile ZULRAH_PLAYER_START_TILE = new Tile(2268, 3069);
    private static final Area ZULRAH_AREA = new Area(2251, 3058, 2281, 3088);

    public void enterInstance(Player player, boolean teleport) {
        instance = (SingleInstancedArea) InstancedAreaManager.getSingleton().createSingleInstancedArea(player, ZULRAH_AREA);
        if (player != null && instance != null) {
            npcList.clear();
            player.lock();
            //Npc zulrah = new Npc(NpcIdentifiers.ZULRAH, ZULRAH_PLAYER_START_TILE.transform(-2, 3, instance.getzLevel()));
            Npc zulrah = new Npc(NpcIdentifiers.ZULRAH, ZULRAH_PLAYER_START_TILE.transform(-2, 3, instance.getzLevel()));
            npcList.add(zulrah);
            Chain.bound(null).name("ZulAndraBoatTask").runFn(teleport ? 1 : 9, () -> {
                player.getMovementQueue().clear();
                player.teleport(ZULRAH_PLAYER_START_TILE.x, ZULRAH_PLAYER_START_TILE.y, instance.getzLevel());
                player.unlock();

                zulrah.respawns(false);
                zulrah.putAttrib(AttributeKey.OWNING_PLAYER, new Tuple<>(player.getIndex(), player));
                zulrah.face(null);
                zulrah.noRetaliation(true);
                zulrah.combatInfo().aggressive = false;
            }).then(1, () -> player.message("Welcome to Zulrah's shrine.")).then(1, () -> {
                World.getWorld().registerNpc(zulrah);
                zulrah.face(zulrah.tile().transform(0, -10, 0));
                zulrah.animate(5073);
                Zulrah.startZulrahBattle(zulrah, player);
            });
        }
    }

    public void clear() {
        for (Npc npc : npcList) {
            World.getWorld().unregisterNpc(npc);
        }
        npcList.clear();
    }
}
