package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.vorkath;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;

public class ZombifiedSpawn extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return 4;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }
}
