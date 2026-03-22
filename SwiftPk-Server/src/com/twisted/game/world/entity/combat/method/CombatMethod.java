package com.twisted.game.world.entity.combat.method;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.hit.Hit;

/**
 * Represents a combat method.
 */
public interface CombatMethod {

    boolean prepareAttack(Mob mob, Mob target);
    int getAttackSpeed(Mob mob);
    int getAttackDistance(Mob mob);
    default void process(Mob mob) {

    }

    default void processArea(Mob mob, Mob target) {

    }
    default boolean customOnDeath(Hit hit) {
        return false;
    }

    default boolean customOnDeath(Mob mob) {
        return false;
    }
    default boolean canMultiAttackInSingleZones() { return false; }
    default double getDamageReduction() {
        return 1.0;
    }
}
