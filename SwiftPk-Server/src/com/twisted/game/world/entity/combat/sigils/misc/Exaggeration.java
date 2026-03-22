package com.twisted.game.world.entity.combat.sigils.misc;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;

public class Exaggeration extends AbstractSigil {
    @Override
    public void onRemove(Player player) {
        player.clearAttrib(AttributeKey.EXAGGERATION_BOOST);
        player.getSkills().resetStats();
    }

    @Override
    public void processMisc(Player player) {
        if (!attuned(player)) return;
        player.putAttrib(AttributeKey.EXAGGERATION_BOOST, true);
        for (int index = 0; index < 23; index++) {
            if (index > 6) player.getSkills().alterSkill(index, 5);
        }
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.SIGIL_OF_EXAGGERATION);
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
