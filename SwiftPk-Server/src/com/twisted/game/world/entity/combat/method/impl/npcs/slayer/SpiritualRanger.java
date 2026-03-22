package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;

public class SpiritualRanger extends CommonCombatMethod {

    private int getProjectileHeight(int npc) {
        return npc == 2211 ? 45 : 30;
    }

    private int getDelay(int npc) {
        return npc == 2242 ? 20 : 40;
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (mob.isNpc()) {
            mob.animate(mob.attackAnimation());
            Npc npc = (Npc) mob;
            var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
            var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

            new Projectile(mob, target, 1192, getDelay(npc.id()), 5 * npc.tile().distance(target.tile()), getProjectileHeight(npc.id()), 33, 0).sendProjectile();

            int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED);
            target.hit(mob, hit,1, CombatType.RANGED).checkAccuracy().submit();
        }
        return true;
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
