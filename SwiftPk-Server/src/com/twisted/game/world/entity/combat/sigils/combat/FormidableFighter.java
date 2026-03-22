package com.twisted.game.world.entity.combat.sigils.combat;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;

public class FormidableFighter extends AbstractSigil {

    @Override
    public void damageModification(Player player, Hit hit) {
        if (!attuned(player)) return;
        if (hit.isAccurate()) {
            if (World.getWorld().rollDie(20, 1)) {
                int damage = hit.getDamage();
                damage += 5;
                hit.setDamage(damage);
            }
        }
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.FORMIDABLE_FIGHTER);
    }

    @Override
    protected boolean activate(Player player) {
        return false;
    }

    @Override
    protected boolean validateCombatType(Player player) {
        return player.getCombat().getCombatType().equals(CombatType.MELEE);
    }
}
