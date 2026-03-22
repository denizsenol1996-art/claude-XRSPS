package com.twisted.game.world.entity.combat.sigils.combat;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.formula.accuracy.AbstractAccuracy;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;

public class Precision extends AbstractSigil {

    @Override
        public double accuracyModification(Player player, Mob target, AbstractAccuracy accuracy) {
        if (!attuned(player)) return 0;
        return 1.50D;
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.PRECISION);
    }

    @Override
    protected boolean activate(Player player) {
        return false;
    }

    @Override
    protected boolean validateCombatType(Player player) {
        return player.getCombat().getCombatType().equals(CombatType.MELEE) || player.getCombat().getCombatType().equals(CombatType.MAGIC) || player.getCombat().getCombatType().equals(CombatType.RANGED);
    }
}
