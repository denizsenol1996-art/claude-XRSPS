package com.twisted.game.world.entity.combat.method.impl.npcs.waterbirth;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;

/**
 * @author Patrick van Elderen | March, 04, 2021, 17:06
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class Wallasalki extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

        new Projectile(mob, target, 136, 15, 12 * tileDist, 30, 31, 0).sendProjectile();

        int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC);
        target.hit(mob, hit, delay, CombatType.MAGIC).checkAccuracy().submit();

        if (hit > 0)
            target.delayedGraphics(137, 124, 2);
        else
            target.delayedGraphics(85, 124, 2);

        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }
}
