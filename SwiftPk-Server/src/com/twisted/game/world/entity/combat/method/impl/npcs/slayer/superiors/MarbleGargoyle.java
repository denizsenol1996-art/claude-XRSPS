package com.twisted.game.world.entity.combat.method.impl.npcs.slayer.superiors;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

/**
 * In combat, the marble gargoyle will occasionally launch a grey ball towards the player.
 * If hit by the projectile, it will inflict up to 38 damage and immobilise the player for a few seconds. The message box states "You have been trapped in stone!" when this occurs.
 * Players are able to avoid this attack by moving at least one tile away from their original position once the projectile is launched.
 *
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * maart 31, 2020
 */
public class MarbleGargoyle extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (Utils.random(3) == 0) {
            mob.animate(7815);
            stoneAttack(mob, target);
            mob.getTimerRepository().register(TimerKey.COMBAT_ATTACK, 6);
        } else if (Utils.random(1) == 0 || !CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            mob.animate(7814);
            new Projectile(mob, target, 276, 70, 35, 50, 30, 0, 10,5).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
        } else {
            mob.animate(7814);
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        }
        return true;
    }

    private void stoneAttack(Mob mob, Mob target) {
        Tile tile = target.tile();
        new Projectile(mob, target,1453, 75, 30, 50, 30, 0, 10,5).sendProjectile();
        Chain.bound(null).runFn(3, () -> {
            if (target.tile().equals(tile)) {
                target.hit(mob, Utils.random(38));
                target.stun(3);
                if (target instanceof Player) {
                    target.message("You have been trapped in stone!");
                }
            }
        });
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 7;
    }
}
