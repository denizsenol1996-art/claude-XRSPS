package com.twisted.game.world.entity.combat.method.impl.npcs.godwars.armadyl;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Area;
import com.twisted.util.Utils;

public class KreeArra extends CommonCombatMethod {

    public static boolean isMinion(Npc n) {
        return n.id() >= 3164 && n.id() <= 3163;
    }

    private static final Area ENCAMPMENT = new Area(2823, 5295, 2843, 5309);

    public static Area getENCAMPMENT() {
        return ENCAMPMENT;
    }

    private static Mob lastBossDamager = null;

    public static Mob getLastBossDamager() {
        return lastBossDamager;
    }

    public static void setLastBossDamager(Mob lastBossDamager) {
        KreeArra.lastBossDamager = lastBossDamager;
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        int roll = Utils.random(2);
        int melee_distance = mob.tile().distance(target.tile());
        boolean melee_range = melee_distance <= 1;
        if (melee_range && roll == 0) {
            mob.animate(6981);
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
        } else if (roll == 1) {
            mob.animate(6980);
            new Projectile(mob, target, 1200, 45, 65, 0, 0, 0).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().submit();
        } else {
            mob.animate(6980);
            new Projectile(mob, target, 1199, 45, 65, 0, 0, 0).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
        }

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
}
