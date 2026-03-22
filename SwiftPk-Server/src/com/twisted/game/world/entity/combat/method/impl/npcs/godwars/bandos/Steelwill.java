package com.twisted.game.world.entity.combat.method.impl.npcs.godwars.bandos;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;

public class Steelwill extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(6154);
        mob.graphic(1216);
        new Projectile(mob, target, 1217, 30, 65, 25, 25, 0).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().postDamage(this::handleAfterHit).submit();
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }

    public void handleAfterHit(Hit hit) {
        hit.getTarget().graphic(hit.getDamage() > 0 ? 166 : 85);
    }
}
