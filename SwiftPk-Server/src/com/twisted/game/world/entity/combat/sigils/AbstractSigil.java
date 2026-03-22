package com.twisted.game.world.entity.combat.sigils;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.formula.accuracy.AbstractAccuracy;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.mob.player.Player;

public abstract class AbstractSigil {
    public void onRemove(Player player){}
    public void processMisc(Player player){}
    public void processCombat(Player player, Mob target){}
    public void damageModification(Player player, Hit hit) {}
    public void skillModification(Player player) {}
    public void resistanceModification(Mob attacker, Mob target, Hit entity) {}
    public double accuracyModification(Player player, Mob target, AbstractAccuracy accuracy) {return 0;}
    public double equipmentModification(Player player){return 0;}
    protected abstract boolean attuned(Player player);
    protected abstract boolean activate(Player player);
    protected abstract boolean validateCombatType(Player player);
}
