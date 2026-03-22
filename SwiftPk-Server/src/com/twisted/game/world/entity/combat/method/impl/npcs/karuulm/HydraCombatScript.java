package com.twisted.game.world.entity.combat.method.impl.npcs.karuulm;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.hydra.HydraAttacks;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.route.routes.ProjectileRoute;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Patrick van Elderen | December, 22, 2020, 22:49
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class HydraCombatScript extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        Npc npc = (Npc) mob;

        if (npc instanceof Hydra) {
            Hydra hydra = (Hydra) npc;

            if (System.currentTimeMillis() - hydra.lastPoisonPool >= 30000L) {
                sendPoisonAttack(hydra, target);
                hydra.lastPoisonPool = System.currentTimeMillis();
            } else {
                regularAttack(hydra, target);
            }
        }

        return true;
    }

    /**
     * Sends the hydra's ranged or magical attack.
     */
    private void regularAttack(Hydra hydra, Mob target) {
        hydra.animate(hydraAttacks.get(Utils.random(hydraAttacks.size() - 1)));

        hydra.recordedAttacks--;

        if (hydra.recordedAttacks == 0) {
            hydra.currentAttack = hydra.currentAttack == HydraAttacks.MAGIC ? HydraAttacks.RANGED : HydraAttacks.MAGIC;
            hydra.recordedAttacks = 3;
        }

        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

        sendProjectile(hydra, target, hydra.currentAttack == HydraAttacks.MAGIC ? 1663 : 1662);

        Chain.bound(null).runFn(delay, () -> target.hit(hydra, CombatFactory.calcDamageFromType(hydra, target, hydra.currentAttack == HydraAttacks.MAGIC ? CombatType.MAGIC : CombatType.RANGED), hydra.currentAttack == HydraAttacks.MAGIC ? CombatType.MAGIC : CombatType.RANGED).checkAccuracy().submit());
    }

    /**
     * Sends the poison pool attack.
     */
    private void sendPoisonAttack(Hydra hydra, Mob target) {
        hydra.animate(8263);
        List<Tile> targets = new LinkedList<>();
        targets.add(target.tile().copy());
        Area hydraBounds = hydra.bounds();
        List<Tile> positions = target.tile().area(3, pos -> !pos.inArea(hydraBounds) && ProjectileRoute.allow(hydra, pos));
        for (int i = 0; i < 2; i++)
            targets.add(Utils.randomElement(positions));
        targets.forEach(pos -> hydra.runFn(1, () -> {
            new Projectile(hydra.getCentrePosition(), pos, 1644, 1, 54,25,35,0,0,16,64).sendProjectile();
            Direction dir = Direction.getDirection(Utils.getClosestTile(hydra, pos), pos);

            World.getWorld().tileGraphic(1645, new Tile(pos.getX(), pos.getY()), pos.getZ(),40);
            World.getWorld().tileGraphic(POISON_POOLS[dir.ordinal()], new Tile(pos.getX(), pos.getY()), pos.getZ(),40);
            Chain.bound(hydra).runFn(3, () -> {
                for (int i = 0; i < 15; i++) {
                    if (target.tile().equals(pos)) {
                        target.hit(hydra, World.getWorld().random(1, 4), SplatType.POISON_HITSPLAT);
                    }
                    Chain.bound(hydra).runFn(2, () -> {
                        //Just ticking
                    });
                }
            });
        }));
    }

    public static final int[] POISON_POOLS = { // indexed by direction as in Direction class
        1658,
        1659,
        1660,
        1657,
        1661,
        1656,
        1655,
        1654,
    };

    /**
     * Fires a projectile from the hydra to a tile.
     */
    private void fireProjectileToLocation(Npc hydra, Tile tile) {
        var center = Utils.getCenterLocation(hydra);
        var dir = Direction.of(tile.x - center.x, tile.y - center.y);
        var from = center.transform(dir.x * 2, dir.y * 2);
        new Projectile(from, tile, 1644, 1, 90, 50, 55, 0, 0, 0, 5).sendProjectile();
    }

    /**
     * Sends the projectile from the hydra to the target.
     */
    private void sendProjectile(Npc npc, Mob target, int projectile) {
        var speed = Utils.getSpeedModifier(npc.tile(), target.tile());
        new Projectile(npc, target, projectile, speed, 30, 35, 30, 0, 10, 10).sendProjectile();
    }

    /**
     * The hydra's attack animations.
     */
    private final List<Integer> hydraAttacks = List.of(8261, 8262, 8263);

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
