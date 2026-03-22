package com.twisted.game.world.entity.combat.method.impl.npcs.misc;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.AggressionCheck;

public class DemonAgro implements AggressionCheck {
    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        return true;
    }
}
