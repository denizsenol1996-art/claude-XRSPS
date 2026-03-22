package com.twisted.game.world.entity.combat.method.impl.npcs;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
public class Porazdir extends CommonCombatMethod {
    @Override
    public boolean prepareAttack(Mob mob, Mob target) {

        int distance = getAttackDistance(target);
        boolean inDistance = target.boundaryBounds().within(mob.tile(), mob.getSize(), distance);
        if (inDistance) {
            if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
                meleeAttack((Npc) mob, target);
            } else {
                rangedAttack((Npc) mob, target);
            }
        }
        return true;
    }
    int RANGE_ATTACK_ANIMATION = 7841;
    int MELEE_ATTACK_ANIMATION = 7840;
    private void meleeAttack(Npc npc, Mob target) {
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
        npc.animate(MELEE_ATTACK_ANIMATION);
    }

    private void rangedAttack(Npc npc, Mob target) {
        var tileDist = npc.tile().transform(1, 1, 0).distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

        mob.animate(RANGE_ATTACK_ANIMATION);
        new Projectile(npc, target, 294, 12 * tileDist, 31, 30, 31, 0, 15, 10).sendProjectile();
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
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
