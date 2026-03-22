package com.twisted.game.world.entity.combat.method.impl.npcs;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.NpcCombatInfo;
import com.twisted.util.chainedwork.Chain;

import java.util.Objects;

// mob.animate(132);range
//mob.animate(132);
// mob.animate(128);melee
public class VoteBossCombat extends CommonCombatMethod {//s23update

    private int attacks = 0;
    private boolean tornadoAttack = false;
    private int hp;
    private NpcCombatInfo combatInfo;

    public int hp() {
        return hp;
    }

    public void hp(int hp, int exceed) {
        this.hp = Math.min(maxHp() + exceed, hp);
    }

    public int maxHp() {
        return combatInfo == null ? 50 : combatInfo.stats.hitpoints;
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && World.getWorld().rollDie(2, 1)) {
            if (World.getWorld().rollDie(2, 1)) {
                meleeClawAttack(mob, target);
            } else {
                rangeAttack();
            }
        } else {
            var roll = World.getWorld().random(8);

            switch (roll) {
                case 2, 3 -> rangeAttack();
                default  -> magicAttack(mob,target);
                //
            }
        }
        return true;
    }

    private void meleeClawAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.forceChat("I take your prayer off!");
        mob.animate(8308);
        mob.face(null); // Stop facing the target
        Chain.bound(null).runFn(8, () -> {
            if (mob.isRegistered() && !mob.dead() && target != null && target.tile().inSqRadius(mob.tile(), 3)) {
                int first = World.getWorld().random(1, 30);
                int second = first / 2;
                target.hit(mob, first, 1);
                target.hit(mob, second, 1);
            }
        });
        mob.face(target.tile()); // Go back to facing the target.
    }


    private void magicAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.forceChat("I'm vote boss, who you?");
        mob.animate(8308);
        var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
        World.getWorld().getPlayers().filter(Objects::nonNull).filter(p -> p.tile().isWithinDistance(mob.tile())).forEach(p -> Chain.bound(null).runFn(3, () -> {
            if (target.tile().inSqRadius(p.tile(), 12)) {
                new Projectile(mob, p, 346, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
                p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                target.delayedGraphics(346, 60, delay + 1);
            }
        }));
    }

    private void rangeAttack() {
        if (mob.dead()) {
            return;
        }
        mob.animate(8308);
        mob.forceChat("You'll never defeat me..");
        mob.face(null); // Stop facing the target
        World.getWorld().getPlayers().filter(Objects::nonNull).filter(p -> p.tile().isWithinDistance(mob.tile())).forEach(p -> Chain.bound(null).runFn(2, () -> {
            if (mob.isRegistered() && !mob.dead() && p.tile().inSqRadius(mob.tile(), 12)) {
                int tileDist = mob.tile().transform(1, 1, 0).getChevDistance(p.tile());
                var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

                new Projectile(mob, p, 255, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 0).sendProjectile();

                p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
            }
        }));
        World.getWorld().getPlayers().filter(Objects::nonNull).filter(p -> p.tile().isWithinDistance(mob.tile())).forEach(p -> Chain.bound(null).runFn(3, () -> {
            if (mob.isRegistered() && !mob.dead() && p.tile().inSqRadius(mob.tile(), 12)) {
                int tileDist = mob.tile().transform(1, 1, 0).getChevDistance(p.tile());
                var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

                new Projectile(mob, p, 255, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 0).sendProjectile();

                p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
            }
        }));
        mob.face(target.tile());
    }




    @Override
    public int getAttackSpeed(Mob mob) {
        return tornadoAttack ? 8 : mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }
}
