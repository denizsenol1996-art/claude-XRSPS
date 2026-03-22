package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.nex;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.AggressionCheck;

public class NexAgro implements AggressionCheck {
    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        return true;
    }
}
