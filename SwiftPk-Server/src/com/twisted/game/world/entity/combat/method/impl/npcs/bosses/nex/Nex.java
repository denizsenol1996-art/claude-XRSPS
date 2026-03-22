package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.nex;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;

import com.twisted.game.world.entity.mob.npc.NpcCombatInfo;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Nex extends CommonCombatMethod {

    private int attacks = 0;


    private NpcDefinition def;
    private int hp;


    public static int getNexHP;
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
        if (target == null) return false;
        if (mob == null) return false;

        getNexHP = mob.hp();
        if (mob.distanceToPoint(target.getX(), target.getY()) >= 10) {
            mob.teleport(target.tile());
            return false;
        }
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            meleeClawAttack(mob, target);
        }
        var roll = World.getWorld().random(10);

        switch (roll) {//smokePhaseAttack
            case 0, 1 -> rangedAttack(mob, target);
            case 2, 3 -> smokePhaseAttack(mob, target);
            case 4, 5 -> shadowPhaseAttack(mob, target);
            case 6, 7 -> bloodPhaseAttack(mob, target);
            case 8, 9 -> zarosPhaseAttack(mob, target);
            case 10 -> icePhaseAttack(mob, target);

        }
        return true;
    }

    private void rangedAttack(Mob mob, Mob target) {
        mob.forceChat("Watch out");
        var tileDist = mob.tile().distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

        if (target.tile().region() != 9770) {
            World.getWorld().getPlayers().filter(p -> p.tile().region() == 11601).forEach(p -> Chain.bound(null).runFn(3, () -> {
                if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                    new Projectile(mob, p, 1245, 20 * tileDist, 25, 65, 31, 0, 15, 220).sendProjectile();
                    mob.animate(9179);
                    p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.RANGED), delay + 1, CombatType.RANGED).checkAccuracy().submit();
                    p.delayedGraphics(1244, 100, delay + 1);
                }
            }));
        } else {
            new Projectile(mob, target, 1245, 20 * tileDist, 25, 65, 31, 0, 15, 220).sendProjectile();
            mob.animate(9179);
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay + 1, CombatType.RANGED).checkAccuracy().submit();
            target.delayedGraphics(1244, 100, delay + 1);
        }

    }

    private void smokePhaseAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.forceChat("Fill my soul with smoke!");
        mob.animate(9179);
        var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 14)) / 30);
        if (target.tile().region() != 9770) {
            World.getWorld().getPlayers().filter(p -> p.tile().region() == 11601).forEach(p -> Chain.bound(null).runFn(3, () -> {
                if (p.tile().inArea(2909, 5187, 2940, 5219)) {
                    if (p != null && target.tile().inSqRadius(p.tile(), 80)) {
                        new Projectile(mob, p, 1997, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
                        p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                        target.delayedGraphics(1998, 60, delay + 1);
                    }
                }
            }));
        } else {
            new Projectile(mob, target, 1997, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
            target.delayedGraphics(1998, 60, delay + 1);
        }
    }

    private void shadowPhaseAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.forceChat("Darken My Shadow");
        mob.animate(9179);
        var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 14)) / 30);
        if (target.tile().region() != 9770) {
            World.getWorld().getPlayers().filter(p -> p.tile().region() == 11601).forEach(p -> Chain.bound(null).runFn(3, () -> {
                if (p.tile().inArea(2909, 5187, 2940, 5219)) {
                    if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                        new Projectile(mob, p, 1999, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
                        p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                        target.delayedGraphics(1999, 60, delay + 1);
                    }
                }
            }));
        }
    }

    private void bloodPhaseAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.forceChat("Flood my lungs with blood!");
        mob.animate(9179);
        var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 14)) / 30);
        if (target.tile().region() != 9770) {
            World.getWorld().getPlayers().filter(p -> p.tile().region() == 11601).forEach(p -> Chain.bound(null).runFn(3, () -> {
                if (p.tile().inArea(2909, 5187, 2940, 5219)) {
                    if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                        new Projectile(mob, p, 2002, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
                        p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                        target.delayedGraphics(2002, 60, delay + 1);
                    }
                }
            }));
        } else {
            new Projectile(mob, target, 2002, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
            target.delayedGraphics(2002, 60, delay + 1);
        }
    }

    private void icePhaseAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.forceChat("Infuse me with the power of Ice!");
        mob.animate(9179);
        var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 14)) / 30);
        if (target.tile().region() != 9770) {
            World.getWorld().getPlayers().forEach(p -> Chain.bound(null).runFn(3, () -> {
                if (p.tile().inArea(2909, 5187, 2940, 5219)) {
                    if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                        new Projectile(mob, p, 2006, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
                        p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                        target.delayedGraphics(2005, 60, delay + 1);
                        mob.freeze(15, target);
                    }
                }
            }));
        } else {
            new Projectile(mob, target, 2006, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
            target.delayedGraphics(2005, 60, delay + 1);
            mob.freeze(15, target);
        }
    }

    private void zarosPhaseAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.forceChat("NOW, THE POWER OF ZAROS!");
        mob.animate(9179);
        var tileDist = mob.tile().transform(1, 1, 0).getChevDistance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 14)) / 30);
        if (target.tile().region() != 9770) {
            World.getWorld().getPlayers().filter(p -> p.tile().region() == 11601).forEach(p -> Chain.bound(null).runFn(3, () -> {
                if (p.tile().inArea(2909, 5187, 2940, 5219)) {
                    if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                        new Projectile(mob, p, 2007, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
                        p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                        target.delayedGraphics(2008, 60, delay + 1);
                    }
                }
            }));
        } else {
            new Projectile(mob, target, 2007, mob.projectileSpeed(target), mob.getProjectileHitDelay(target), 50, 43, 0, 14, 5).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
            target.delayedGraphics(2008, 60, delay + 1);
        }
    }

    private void meleeClawAttack(Mob mob, Mob target) {
        if (mob.dead()) {
            return;
        }
        mob.animate(9180);
        mob.face(null); // Stop facing the target
        if (target.tile().region() != 9770) {
            Chain.bound(null).runFn(1, () -> {
                if (mob.isRegistered() && !mob.dead() && target != null && target.tile().inSqRadius(mob.tile(), 4)) {
                    int first = World.getWorld().random(1, 30);
                    int second = first / 2;
                    target.hit(mob, first, 1);
                }
            });
        } else {
            int first = World.getWorld().random(1, 30);
            int second = first / 2;
            target.hit(mob, first, 1);
        }
        mob.face(target.tile()); // Go back to facing the target.
    }


    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 80;
    }

    public static final Area NEX_AREA = new Area(2910, 5189, 2939, 5217);


    public static boolean inNexArea(Tile tile) {
        return tile.inArea(NEX_AREA);
    }

    @Override
    public ArrayList<Mob> getPossibleTargets(Mob mob) {
        if (inNexArea(mob.tile())) {
            return Arrays.stream(mob.closePlayers(64)).filter(NEX_AREA::contains).collect(Collectors.toCollection(ArrayList::new));
        }
        return null;
    }
}

