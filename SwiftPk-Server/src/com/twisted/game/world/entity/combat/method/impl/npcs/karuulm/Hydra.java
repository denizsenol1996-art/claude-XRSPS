package com.twisted.game.world.entity.combat.method.impl.npcs.karuulm;

import com.twisted.game.world.entity.combat.method.impl.npcs.hydra.HydraAttacks;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Tile;

/**
 * @author Patrick van Elderen | December, 22, 2020, 17:58
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class Hydra extends Npc {

    public Hydra(int id, Tile tile) {
        super(id, tile);
    }

    /**
     * The amount of attacks left until it changes.
     */
    public int recordedAttacks = 3;

    /**
     * The hydra's current attack.
     */
    public HydraAttacks currentAttack = HydraAttacks.MAGIC;

    /**
     * The moment of the last hydra's poison pool attack.
     */
    public long lastPoisonPool = System.currentTimeMillis();

    @Override
    public void takeHit() {
        super.takeHit();

        if (System.currentTimeMillis() - lastPoisonPool > 60000L) {
            lastPoisonPool = System.currentTimeMillis();
        }
    }
}
