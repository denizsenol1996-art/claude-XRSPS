package com.twisted.game.world.entity.combat.sigils;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.formula.accuracy.AbstractAccuracy;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.mob.player.Player;

public interface SigilListener {
    void processResistance(Mob attacker, Mob target, Hit hit);
    void processDamage(Player player, Hit hit);
    void process(Player player, Mob target);
    double processAccuracy(Player player, Mob target, AbstractAccuracy accuracy);
    void HandleLogin(Player player);
}
