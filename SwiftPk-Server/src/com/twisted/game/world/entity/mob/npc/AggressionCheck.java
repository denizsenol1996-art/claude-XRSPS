package com.twisted.game.world.entity.mob.npc;

import com.twisted.game.world.entity.Mob;

public interface AggressionCheck {

    boolean shouldAgro(Mob mob, Mob victim);

}
