package com.twisted.game.world.entity.combat.sigils.misc;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;

public class Stamina extends AbstractSigil {
    @Override
    public void onRemove(Player player) {
        player.getPacketSender().sendStamina(false);
        player.clearAttrib(AttributeKey.STAMINA_POTION_TICKS);
    }

    @Override
    public void processMisc(Player player) {
        if (!attuned(player)) return;
        player.getPacketSender().sendStamina(true);
        player.putAttrib(AttributeKey.STAMINA_POTION_TICKS, Integer.MAX_VALUE);
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.SIGIL_OF_STAMINA);
    }

    @Override
    protected boolean activate(Player player) {
        return false;
    }

    @Override
    protected boolean validateCombatType(Player player) {
        return false;
    }
}
