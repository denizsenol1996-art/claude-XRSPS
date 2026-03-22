package com.twisted.game.world.entity.combat.method.impl.npcs.dragons;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.AggressionCheck;

public class MiniDragonAgro implements AggressionCheck {
    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        return false;
    }
}
