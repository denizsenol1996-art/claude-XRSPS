package com.twisted.game.world.entity.combat.sigils.combat;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

public class FeralFighter extends AbstractSigil {

    @Override
    public void processCombat(Player player, Mob target) {
        if (!attuned(player)) return;
        var delay = 12;
        switch (player.getMemberRights()) {
            case RUBY_MEMBER -> delay = 13;
            case SAPPHIRE_MEMBER -> delay = 14;
            case EMERALD_MEMBER -> delay = 15;
            case DIAMOND_MEMBER -> delay = 16;
            case DRAGONSTONE_MEMBER -> delay = 17;
            case ONYX_MEMBER -> delay = 18;
            case ZENYTE_MEMBER -> delay = 19;
        }
        if (!activate(player)) {
            if (Utils.rollDie(20, 1)) {
                player.animate(9158);
                player.graphic(1980);
                player.putAttrib(AttributeKey.FERAL_FIGHTER_ATTACKS_SPEED, player.getBaseAttackSpeed() - 1.2);
                Chain.noCtx().runFn(delay, () -> player.clearAttrib(AttributeKey.FERAL_FIGHTER_ATTACKS_SPEED));
            }
        }
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.FERAL_FIGHTER);
    }

    @Override
    protected boolean activate(Player player) {
        return player.hasAttrib(AttributeKey.FERAL_FIGHTER_ATTACKS_SPEED);
    }

    @Override
    protected boolean validateCombatType(Player player) {
        return player.getCombat().getCombatType().equals(CombatType.MELEE);
    }

}
