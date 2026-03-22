package com.twisted.game.world.entity.combat.sigils.misc;


import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;

public class Alchemaniac extends AbstractSigil {
    @Override
    public void onRemove(Player player) {
        player.clearAttrib(AttributeKey.ALCHEMANIAC_BOOST);
    }

    @Override
    public void processMisc(Player player) {
        if (!attuned(player)) return;
        player.putAttrib(AttributeKey.ALCHEMANIAC_BOOST, true);
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.SIGIL_OF_ALCHEMANIAC);
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
