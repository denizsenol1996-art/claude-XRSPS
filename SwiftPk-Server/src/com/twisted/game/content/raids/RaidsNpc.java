package com.twisted.game.content.raids;

import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Tile;

/**
 * An NPC with raids attributes.
 *
 * @author Patrick van Elderen | May, 10, 2021, 16:16
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class RaidsNpc extends Npc {

    public static final double BONUS_HP_PER_PLAYER = 0.40; // %increased hp for each players beyond the first
    public RaidsNpc(int id, Tile tile, boolean theatre) {
        super(id, tile);
        this.respawns(false);
        this.combatInfo().aggroradius = 15;
        this.walkRadius(15);
    }
    public RaidsNpc(int id, Tile tile, int partySize) {
        super(id, tile);
        this.respawns(false);
        this.combatInfo().aggroradius = 15;
        this.walkRadius(15);
        this.setHitpoints((int) (this.hp() * (1 + (BONUS_HP_PER_PLAYER * (partySize - 1)))));
    }
}
