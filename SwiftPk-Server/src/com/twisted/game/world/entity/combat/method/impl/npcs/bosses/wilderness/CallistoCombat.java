package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.superiorbosses.Artio;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.route.routes.ProjectileRoute;
import com.twisted.util.NpcIdentifiers;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import lombok.NonNull;

import java.util.ArrayList;

import static com.twisted.util.CustomNpcIdentifiers.ARTIO;

public class CallistoCombat extends CommonCombatMethod {

    private int roarCount = 0;
    private int trapState = 0;
    public boolean performingAnimation = false;
    private final ArrayList<Tile> allActiveTraps = new ArrayList<>();
    private final ArrayList<GameObject> allActiveTrapObjects = new ArrayList<>();
    private final Area ARTIO_AREA = new Area(1750, 11532, 1770, 11554);

    @Override
    public int getAttackSpeed(@NonNull final Mob entity) {
        return entity.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 5;
    }

    @Override
    public boolean customOnDeath(Hit hit) {
        roarCount = 0;
        trapState = 0;
        performingAnimation = false;
        for (var o : allActiveTrapObjects) {
            Chain.noCtx().runFn(3, () -> o.animate(9999)).then(2, o::remove);
        }
        return true;
    }

    @Override
    public void process(Mob entity) {
        var bear = NpcIdentifiers.CALLISTO;
        var npc = (Npc) entity;
        if (npc.getId() == bear) return;
        for (var t : getPossibleTargets(npc)) {
            if (t == null) continue;
            var currentX = t.tile().getX();
            var currentY = t.tile().getY();
            var previousX = t.getPreviousTile().getX();
            var previousY = t.getPreviousTile().getY();
            var middleX = (currentX + previousX) / 2;
            var middleY = (currentY + previousY) / 2;
            for (var o : allActiveTrapObjects) {
                if (o.tile().equals(middleX, middleY) && !t.tile().equals(o.tile())) {
                    Chain.noCtx().runFn(1, () -> o.animate(9999)).then(1, () -> {
                        o.remove();
                        allActiveTrapObjects.remove(o);
                    });
                }
            }
            allActiveTrapObjects.stream().filter(o -> o.tile().equals(t.tile())).findFirst().ifPresent(o ->
                Chain.noCtx().runFn(1, () -> o.animate(9999)).then(1, () -> {
                t.hit(entity, Utils.random(1, 15), 1);
                t.stun(3);
                o.remove();
                allActiveTrapObjects.remove(o);
            }));
        }
    }

    @Override
    public boolean prepareAttack(@NonNull final Mob entity, @NonNull final Mob target) {
        if (performingAnimation) return false;
        if (Utils.percentageChance(50)) {
            if (!ProjectileRoute.allow(entity, target)) return false;
            rangeAttack(entity);
        } else if (Utils.percentageChance(35)) {
            if (!ProjectileRoute.allow(entity, target)) return false;
            magicAttack(entity);
        } else {
            if (!withinDistance(1)) return false;
            meleeAttack(entity, target);
        }
        trapState++;
        double hpPercentage = ((double) entity.hp() / entity.maxHp());
        if (hpPercentage <= .66 && trapState == Utils.random(1, 2) || hpPercentage <= .66 && roarCount == 0)
            bearTraps(entity);
        if (hpPercentage <= .33 && trapState == Utils.random(1, 2) || hpPercentage <= .33 && roarCount == 1)
            bearTraps(entity);
        if (trapState == 2) trapState = 0;
        return true;
    }

    @Override
    public void doFollowLogic() {
        if (performingAnimation) return;
        follow(1);
    }

    private void meleeAttack(@NonNull final Mob entity, @NonNull final Mob target) {
        if (!withinDistance(2) || performingAnimation) return;
        entity.animate(10012);
        Hit hit = Hit.builder(entity, target, CombatFactory.calcDamageFromType(entity, target, CombatType.MELEE), 3, CombatType.MELEE).checkAccuracy();
        hit.submit();
    }

    private void rangeAttack(@NonNull final Mob entity) {
        if (!withinDistance(10) || performingAnimation) return;
        entity.animate(10013);
        entity.graphic(2349);
        for (var t : getPossibleTargets(entity)) {
            int tileDist = entity.tile().distance(t.tile());
            int duration = (25 + 10 + (10 * tileDist));
            Projectile p = new Projectile(entity, t, 2350, duration, 25, 20, 20, 0, 5, 10);
            final int delay = (int) (p.getSpeed() / 30D);
            p.sendProjectile();
            var dmg = CombatFactory.calcDamageFromType(entity, t, CombatType.RANGED);
            Hit hit = Hit.builder(entity, t, dmg, delay, CombatType.RANGED).checkAccuracy();
            hit.submit();
            t.graphic(2351, 0, p.getSpeed());
        }
    }

    private void magicAttack(@NonNull final Mob entity) {
        if (!withinDistance(10) || performingAnimation) return;
        entity.animate(10014);
        for (var t : getPossibleTargets(entity)) {
            int tileDist = entity.tile().distance(t.tile());
            int duration = (55 + 10 + (10 * tileDist));
            Projectile p = new Projectile(entity, t, 133, duration, 55, 50, 31, 0, 5, 10);
            final int delay = (int) (p.getSpeed() / 30D);
            p.sendProjectile();
            Hit hit = Hit.builder(entity, t, CombatFactory.calcDamageFromType(entity, t, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy();
            hit.submit();
            if (hit.isAccurate()) {
                t.graphic(134, 50, p.getSpeed());
            }
        }
    }

    private void bearTraps(@NonNull final Mob entity) {
        if (roarCount == 0) {
            roarCount++;
            performingAnimation = true;
            Chain.noCtx().runFn(1, () -> {
                entity.animate(10015);
                entity.graphic(2352);
            }).then(4, () -> {
                entity.unlock();
                performingAnimation = false;
            });
        } else if (roarCount == 1) {
            roarCount++;
            performingAnimation = true;
            Chain.noCtx().runFn(1, () -> {
                entity.animate(10015);
            }).then(3, () -> {
                performingAnimation = false;
            });
        }
        int spawned = 5;
        int attempts = 50;

        ArrayList<Tile> newTraps = new ArrayList<>();

        while (spawned-- > 0 && attempts-- > 0) {
            var t = this.mob.tile().area(5).randomTile();
            if (!t.allowObjectPlacement()) continue;
            if (allActiveTraps.contains(t)) {
                spawned++;
                continue;
            }
            newTraps.add(t);
        }

        allActiveTraps.addAll(newTraps);

        for (Tile newTrap : newTraps) {
            if (MovementQueue.dumbReachable(newTrap.getX(), newTrap.getY(), entity.tile())) {
                World.getWorld().tileGraphic(2343, newTrap, 0, 0);
                Chain.noCtx().runFn(1, () -> {
                    GameObject o = newTrap.object(47146).spawn();
                    allActiveTrapObjects.add(o);
                    o.animate(9998);
                    Chain.noCtx().runFn(20, () -> {
                        o.remove();
                        allActiveTrapObjects.remove(o);
                        allActiveTraps.remove(newTrap);
                    });
                });
            }
        }
    }

    @Override
    public boolean rollSuperior(Npc npc) {
        if (World.getWorld().rollDie(25, 1)) {
            npc.transmog(ARTIO, true);
            npc.setCombatMethod(new Artio());
            return true;
        }
        return false;
    }
}
