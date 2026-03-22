package com.twisted.game.world.entity.combat.method.impl.specials.range;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.weapon.FightStyle;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.timers.TimerKey;

/**
 * Morrigan's throwing axe has a special attack, Hamstring, that consumes 50% of the player's special attack energy and deals between 20% and 120% of the user's max hit.
 * <p>
 * Against players, it will also increase the rate in which the target's run energy is drained by sixfold.
 * When this occurs, the target will receive a message in their chatbox stating "You've been hamstrung! For the next minute, your run energy will drain 6x faster."
 */
public class MorrigansThrowingAxe extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(929);
        mob.graphic(1626, 100, 0);

        //Fire projectile
        new Projectile(mob, target, 1625, 50, 70, 44, 35, 3).sendProjectile();

        int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED);
        target.hit(mob, hit, 1, CombatType.RANGED).checkAccuracy().postDamage(this::handleAfterHit).submit();
        CombatSpecial.drain(mob, CombatSpecial.MORRIGANS_THROWING_AXE.getDrainAmount());
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return mob.getCombat().getFightType().getStyle().equals(FightStyle.DEFENSIVE) ? 6 : 4;
    }

    public void handleAfterHit(Hit hit) {
        if(hit.getTarget() instanceof Player) {
            Player player = (Player) hit.getTarget();
            player.getTimerRepository().register(TimerKey.HAMSTRUNG, 100);
            player.message("You've been hamstrung! For the next minute, your run energy will drain 6x faster.");
        }
    }
}
