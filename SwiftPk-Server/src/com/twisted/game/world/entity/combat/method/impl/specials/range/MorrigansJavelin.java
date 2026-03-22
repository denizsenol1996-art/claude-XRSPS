package com.twisted.game.world.entity.combat.method.impl.specials.range;

import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.weapon.FightStyle;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * Morrigan's javelin has a special attack, Phantom Strike, that consumes 50% of the player's special attack energy and applies a damage over time effect to the opponent after the special attack is performed.
 * This special attack has no effect on NPCs.
 *
 * Every 3 ticks (1.8 seconds) after the special attack is performed, the opponent will take an additional 5 hitpoints of damage until the same damage dealt by the special attack has been delivered.
 * When this occurs, the target will receive a message in their chatbox stating "You start to bleed as a result of the javelin strike."
 * Subsequent damage will result in the chatbox stating "You continue to bleed as a result of the javelin strike."
 */
public class MorrigansJavelin extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(806);
        mob.graphic(1621);

        //Fire projectile
        new Projectile(mob, target, 1622, 30, 60, 40, 36, 0).sendProjectile();

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),0, CombatType.RANGED).checkAccuracy();
        hit.submit();

        if(target instanceof Player) {
            Player playerTarget = (Player) target;
            playerTarget.message("You start to bleed as a result of the javelin strike.");
            playerTarget.hit(target, 5, CombatType.RANGED).submit();

            TaskManager.submit(new Task("Phantom Strike Task", 3) {
                final int damage = hit.getDamage();
                int damageDealt = 0;
                @Override
                protected void execute() {
                    if(playerTarget.dead() || !playerTarget.isRegistered()) {
                        this.stop();
                    }

                    if (damage - damageDealt >= 5) {
                        Hit hit = playerTarget.hit(mob, 5,0, CombatType.RANGED).setAccurate(true);
                        hit.submit();
                        playerTarget.message("You continue to bleed as a result of the javelin strike.");
                        damageDealt += 5;
                    } else {
                        int left = damage - damageDealt;
                        Hit hit = playerTarget.hit(mob, 5,0, CombatType.RANGED).setAccurate(true);
                        hit.submit();
                        playerTarget.message("You continue to bleed as a result of the javelin strike.");
                        damageDealt += left;
                    }

                    if (damageDealt >= damage) {
                        this.stop();
                    }
                }
            });
        }
        CombatSpecial.drain(mob, CombatSpecial.MORRIGANS_JAVALIN.getDrainAmount());
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
}
