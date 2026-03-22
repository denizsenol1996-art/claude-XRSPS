package com.twisted.game.world.entity.mob.npc.droptables.impl;

import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.droptables.Droptable;
import com.twisted.game.world.entity.mob.player.Player;

import static com.twisted.game.world.entity.AttributeKey.GWD_BANDOS_KC;

/**
 * @author Patrick van Elderen | April, 29, 2021, 14:23
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class GWDBandosMinion implements Droptable {

    @Override
    public void reward(Npc npc, Player killer) {
        var current = killer.<Integer>getAttribOr(GWD_BANDOS_KC,0) + 1;
        if (current < 2000) {
            killer.putAttrib(GWD_BANDOS_KC, current);
        }
    }
}
