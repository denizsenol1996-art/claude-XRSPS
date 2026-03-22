package com.twisted.game.world.entity.mob.npc.aggressive;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.AggressionCheck;

public class RaidsAgro implements AggressionCheck {

    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        return true;
    }
}
