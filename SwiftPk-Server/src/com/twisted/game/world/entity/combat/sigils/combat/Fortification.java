package com.twisted.game.world.entity.combat.sigils.combat;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.formula.accuracy.AbstractAccuracy;
import com.twisted.game.world.entity.combat.sigils.AbstractSigil;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.container.equipment.EquipmentBonuses;

public class Fortification extends AbstractSigil {

    @Override
        public double accuracyModification(Player player, Mob target, AbstractAccuracy accuracy) { //TODO not accuracy
        if (!attuned(player)) return 0;
        EquipmentBonuses attackerBonus = player.getBonuses().totalBonuses(player, World.getWorld().equipmentInfo());
        attackerBonus.stab += 30;
        attackerBonus.slash += 30;
        attackerBonus.crush += 30;
        return 30;
    }

    @Override
    protected boolean attuned(Player player) {
        return player.hasAttrib(AttributeKey.SIGIL_OF_FORTIFICATION);
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
