package com.twisted.game.world.entity.combat.sigils.combat;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.formula.accuracy.AbstractAccuracy;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;

public class DeftStrikes extends AbstractSigil {
    @Override
    public double accuracyModification(Player player, Mob target, AbstractAccuracy accuracy) {
        if (!attuned(player)) return 0;
        var boost = 1.20;
        switch (player.getMemberRights()) {
            case RUBY_MEMBER -> boost = 1.21;
            case SAPPHIRE_MEMBER -> boost = 1.22;
            case EMERALD_MEMBER -> boost = 1.23;
            case DIAMOND_MEMBER -> boost = 1.24;
            case DRAGONSTONE_MEMBER -> boost = 1.25;
            case ONYX_MEMBER -> boost = 1.26;
            case ZENYTE_MEMBER -> boost = 1.27;
        }
        return boost;
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.DEFT_STRIKES);
    }

    @Override
    protected boolean activate(Player player) {
        return false;
    }

    @Override
    protected boolean validateCombatType(Player player) {
        return player.getCombat().getCombatType().equals(CombatType.RANGED) || player.getCombat().getCombatType().equals(CombatType.MELEE) || player.getCombat().getCombatType().equals(CombatType.MAGIC);
    }
}
