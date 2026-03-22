package com.twisted.game.world.entity.combat.method.impl.specials.range;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.animations.Animation;
import com.twisted.game.world.entity.masks.animations.Priority;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.player.Player;

public class MagicShortbow extends CommonCombatMethod {

    private static final Animation ANIMATION = new Animation(1074, Priority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(250, 100);

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        final Player player = mob.getAsPlayer();

        player.animate(ANIMATION);
        player.performGraphic(GRAPHIC);

        //Send 2 arrow projectiles
        new Projectile(player, target, 249, 40, 70, 43, 31, 0).sendProjectile();
        new Projectile(player, target, 249, 33, 74, 48, 31, 0).sendProjectile();

        //Remove 2 arrows from ammo
        CombatFactory.decrementAmmo(player);
        CombatFactory.decrementAmmo(player);

        Hit hit1 = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),3, CombatType.RANGED).checkAccuracy();
        hit1.submit();

        Hit hit2 = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),2, CombatType.RANGED).checkAccuracy();
        hit2.submit();
        CombatSpecial.drain(mob, CombatSpecial.MAGIC_SHORTBOW.getDrainAmount());
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed() + 1;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
