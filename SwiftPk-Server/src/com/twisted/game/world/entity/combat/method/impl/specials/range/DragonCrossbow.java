package com.twisted.game.world.entity.combat.method.impl.specials.range;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * The crossbow has a special attack, Annihilate, which drains 60% of the special attack bar and hits up to 9 enemies in a 3x3 area.
 * The primary target of Annihilate will take 20% extra damage, all other targets will take 20% less damage.
 * If used in a single-way combat area, the attack will still work but only hit one target with 20% extra damage.
 * It ignores enchanted bolt effects.
 */
public class DragonCrossbow extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob attacker, Mob target) {
        final Player player = attacker.getAsPlayer();

        player.animate(4230);

        new Projectile(attacker, target, 698, 50, 70, 44, 35, 0).sendProjectile();
        target.delayedGraphics(new Graphic(1466, 80), 3);

        //Decrement ammo by 1
        CombatFactory.decrementAmmo(player);

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),2, CombatType.RANGED).checkAccuracy();
        hit.submit();
        CombatSpecial.drain(attacker, CombatSpecial.DRAGON_CROSSBOW.getDrainAmount());
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
