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

public class RuthlessRanger extends AbstractSigil {

    @Override
    public void processCombat(Player player, Mob target) {
        if (!attuned(player)) return;
        var damage = 1;
        switch (player.getMemberRights()) {
            case RUBY_MEMBER -> damage = 2;
            case SAPPHIRE_MEMBER -> damage = 3;
            case EMERALD_MEMBER -> damage = 4;
            case DIAMOND_MEMBER -> damage = 5;
            case DRAGONSTONE_MEMBER -> damage = 6;
            case ONYX_MEMBER -> damage = 7;
            case ZENYTE_MEMBER -> damage = 8;
        }
        if (!activate(player)) {
            if (Utils.rollDie(10, 1)) {
                player.animate(9158);
                player.graphic(1981);
                player.putAttrib(AttributeKey.RUTHLESS_CRIPPLE, true);
                AtomicInteger count = new AtomicInteger(6);
                int d = damage;
                Chain.noCtx().repeatingTask(1, cripple -> {
                    count.getAndDecrement();
                    new Hit(player, target, d, 1, SplatType.DISEASE_HITSPLAT, CombatType.RANGED).submit();
                    if (count.get() == 0) {
                        player.clearAttrib(AttributeKey.RUTHLESS_CRIPPLE);
                        cripple.stop();
                    }
                });

            }
        }
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.RUTHLESS_RANGER);
    }

    @Override
    protected boolean activate(Player player) {
        return player.hasAttrib(AttributeKey.RUTHLESS_CRIPPLE);
    }

    @Override
    protected boolean validateCombatType(Player player) {
        return player.getCombat().getCombatType().equals(CombatType.RANGED);
    }

}
