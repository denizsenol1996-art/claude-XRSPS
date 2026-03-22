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
import com.twisted.util.timers.TimerKey;

public class DragonThrownaxe extends CommonCombatMethod {

    private static final Animation ANIMATION = new Animation(7521, Priority.HIGH);
    private static final Graphic GRAPHIC = new Graphic(1317, 120);

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        final Player player = mob.getAsPlayer();

        player.animate(ANIMATION);
        player.performGraphic(GRAPHIC);

        new Projectile(player, target, 1318, 45, 65, 40, 33, 0).sendProjectile();

        CombatFactory.decrementAmmo(player);

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),0, CombatType.RANGED).checkAccuracy();
        hit.submit();
        CombatSpecial.drain(mob, CombatSpecial.DRAGON_THROWNAXE.getDrainAmount());

        player.getTimerRepository().register(TimerKey.THROWING_AXE_DELAY,1);
        player.getTimerRepository().register(TimerKey.COMBAT_ATTACK,1); // 1 tick delay before another normal melee
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed() + 1;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 4;
    }
}
