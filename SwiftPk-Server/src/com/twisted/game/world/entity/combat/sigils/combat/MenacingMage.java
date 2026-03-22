package com.twisted.game.world.entity.combat.sigils.combat;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import java.util.concurrent.atomic.AtomicInteger;

public class MenacingMage extends AbstractSigil {

    @Override
    public void processCombat(Player player, Mob target) {
        if (!attuned(player)) return;
        var damage = 2;
        switch (player.getMemberRights()) {
            case RUBY_MEMBER -> damage = 3;
            case SAPPHIRE_MEMBER -> damage = 4;
            case EMERALD_MEMBER -> damage = 5;
            case DIAMOND_MEMBER -> damage = 6;
            case DRAGONSTONE_MEMBER -> damage = 7;
            case ONYX_MEMBER -> damage = 8;
            case ZENYTE_MEMBER -> damage = 9;
        }
        if (!activate(player)) {
            if (Utils.rollDie(20, 1)) {
                player.animate(9158);
                player.graphic(1977);
                player.putAttrib(AttributeKey.MENACING_CURSE, true);
                AtomicInteger count = new AtomicInteger(6);
                int d = damage;
                Chain.noCtx().repeatingTask(1, curse -> {
                    count.getAndDecrement();
                    new Hit(player, target, d, 1, SplatType.DISEASE_HITSPLAT, CombatType.MAGIC).submit();
                    if (count.get() == 0) {
                        player.clearAttrib(AttributeKey.MENACING_CURSE);
                        curse.stop();
                    }
                });
            }
        }
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.MENACING_MAGE);
    }

    @Override
    protected boolean activate(Player player) {
        return player.hasAttrib(AttributeKey.MENACING_CURSE);
    }

    @Override
    protected boolean validateCombatType(Player player) {
        return player.getCombat().getCombatType().equals(CombatType.MAGIC);
    }

}
