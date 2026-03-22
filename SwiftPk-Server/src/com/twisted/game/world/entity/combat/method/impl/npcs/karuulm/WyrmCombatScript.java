package com.twisted.game.world.entity.combat.method.impl.npcs.karuulm;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;

import static com.twisted.game.world.entity.combat.CombatFactory.MELEE_COMBAT;

/**
 * The combat script for the wyrm.
 * @author Patrick van Elderen | December, 22, 2020, 14:16
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class WyrmCombatScript extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        var inMeleeDistance = CombatFactory.canReach(mob, MELEE_COMBAT, target);
        if (inMeleeDistance && World.getWorld().rollDie(2, 1))
            basicAttack(mob, target);
        else
            magicAttack(mob, target);

        return true;
    }

    private void basicAttack(Mob mob, Mob target) {
        mob.animate(8270);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
    }

    private void magicAttack(Mob mob, Mob target) {
        mob.animate(8271);
        new Projectile(mob.getCentrePosition(), target.tile(), 1634, 1, 125,30,36, 31,0,10,10).sendProjectile();

        int delay = mob.getProjectileHitDelay(target);
        int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC);
        target.hit(mob, hit, delay, CombatType.MAGIC).checkAccuracy().submit();
        if (hit > 0) {
            target.delayedGraphics(1635, 0, delay);
        }
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
