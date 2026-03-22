package com.twisted.game.world.entity.combat.sigils.combat;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;

public class Resistance extends AbstractSigil {

    @Override
    public void resistanceModification(Mob attacker, Mob target, Hit hit) {
        if (!(attacker instanceof Npc)) return;
        if (target instanceof Player player) {
            if (!attuned(player)) return;
            if (player.hasAttrib(AttributeKey.TITANIUM)) return;
            int damage = hit.getDamage();
            var reduced_value = damage - (damage * 0.25);
            hit.setDamage((int) reduced_value);
        }
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.RESISTANCE);
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
