package com.twisted.game.world.entity.combat.sigils.combat;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;
public class Ninja extends AbstractSigil {
    @Override
    public void onRemove(Player player) {
        player.clearAttrib(AttributeKey.NINJA);
    }

    @Override
    public void processMisc(Player player) {
        if (!attuned(player)) return;
        player.putAttrib(AttributeKey.NINJA, true);
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.SIGIL_OF_THE_NINJA);
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
