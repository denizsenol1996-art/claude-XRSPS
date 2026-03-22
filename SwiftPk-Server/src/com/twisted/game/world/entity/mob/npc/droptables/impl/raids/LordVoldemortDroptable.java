package com.twisted.game.world.entity.mob.npc.droptables.impl.raids;

import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.droptables.Droptable;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 27, 2021
 */
public class LordVoldemortDroptable implements Droptable {

    @Override
    public void reward(Npc npc, Player killer) {
        var party = killer.raidsParty;

        if (party != null) {
            if (party.getLeader().getRaids() != null)
                party.getLeader().getRaids().complete(party);
        }
    }
}
