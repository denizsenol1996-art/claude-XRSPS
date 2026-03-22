package com.twisted.game.world.entity.combat.formula.damage;

import com.twisted.game.world.entity.mob.player.Player;

public interface AbstractDamage {
    double getEquipmentBonus(final Player player);
    double getSlayerBonus(final Player player);
    default double getPrayerBonus(final Player player) {
        return 0;
    }
    default int getEffectiveLevel(final Player player) {
        return 0;
    }
    int getBaseDamage(final Player player);
    int getStyleBonus(final Player player);
    int getLevel(final Player player);
    int getMaxHit(final Player player);
}
