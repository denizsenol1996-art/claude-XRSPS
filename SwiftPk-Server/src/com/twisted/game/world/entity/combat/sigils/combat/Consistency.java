package com.twisted.game.world.entity.combat.sigils.combat;


import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;

public class Consistency extends AbstractSigil {

    @Override
    public void damageModification(Player player, Hit hit) {
        if (!attuned(player)) return;
        hit.postDamage(h -> {
            int damage = h.getDamage() + 1;
            h.setAccurate(true);
            h.setDamage(damage);
        });
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.CONSISTENCY);
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
