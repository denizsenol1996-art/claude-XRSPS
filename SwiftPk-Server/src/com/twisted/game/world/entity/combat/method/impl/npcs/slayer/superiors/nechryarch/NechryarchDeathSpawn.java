package com.twisted.game.world.entity.combat.method.impl.npcs.slayer.superiors.nechryarch;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Tile;

import java.util.List;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 02, 2020
 */
public class NechryarchDeathSpawn extends Npc {

    public NechryarchDeathSpawn(Npc nechryarch, Mob target, int id, Tile tile, int walkRadius) {
        super(id, tile);
        this.putAttrib(AttributeKey.BOSS_OWNER, nechryarch);
        this.setTile(tile);
        this.walkRadius(walkRadius);
        this.respawns(false);
        this.getCombat().attack(target);
    }

    public static void death(Npc npc) {
        Npc nechryarch = npc.getAttribOr(AttributeKey.BOSS_OWNER, null);
        if (nechryarch != null) {
            //Check for any minions.
            List<Npc> minList = nechryarch.getAttribOr(AttributeKey.MINION_LIST, null);
            if (minList != null) {
                minList.remove(npc);
                if (minList.size() == 0) {
                    nechryarch.putAttrib(AttributeKey.DEATH_SPAWNS_SPAWNED, false);
                }
            }
        }
    }
}
