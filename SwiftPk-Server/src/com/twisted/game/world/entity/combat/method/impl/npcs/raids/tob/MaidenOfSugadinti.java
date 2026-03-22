package com.twisted.game.world.entity.combat.method.impl.npcs.raids.tob;

import com.twisted.game.task.Task;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import lombok.Getter;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Handles Maiden of Sugadinti combat.
 * @author ReverendDread
 *
 * Apr 7, 2019
 */
public class MaidenOfSugadinti extends CommonCombatMethod {

    private static final int NORMAL_ATTACK = 8092;
    private static final int BLOOD_SPAWN = 8367;
    private static final int BLOOD_SPLASH = 1576;
    private static final int BLOOD_SPLAT = 1579;

    private int phase = 0;

    private static final Tile[] SPIDER_SPAWNS = {
        //North
        new Tile(3173, 4457),
        new Tile(3177, 4457),
        new Tile(3181, 4457),
        new Tile(3185, 4457),
        new Tile(3186, 4455),
        //South
        new Tile(3186, 4437),
        new Tile(3185, 4435),
        new Tile(3182, 4435),
        new Tile(3178, 4435),
        new Tile(3174, 4435)
    };

    /**
     * Handles spawning random blood spawns.
     */
    private void bloodSpawns(final Mob mob, final Mob target) {
        final Tile tile = target.tile();
        Task task = new Task("MaidenOfSugadintiTask", 1, target, false) {
            int cycle = 0;
            final CopyOnWriteArraySet<BloodSplat> splats = new CopyOnWriteArraySet<>();
            Npc spawn;
            @Override
            public void execute() {
                cycle++;
                if (cycle == 2) {
                    cycle = 0;
                    if (spawn == null) {
                        spawn = new Npc(BLOOD_SPAWN, tile).spawn(false);
                        spawn.canAttack(false);
                        spawn.completelyLockedFromMoving(true);
                        World.getWorld().tileGraphic2(BLOOD_SPLASH, tile, 0, 0);
                    }
                    if (spawn.dead() || mob.dead()) {
                        stop();
                        return;
                    }
                    boolean exists = splats.stream().anyMatch((splat) -> splat.tile.equals(spawn.tile()));
                    if (target.isPlayer() && !exists) {
                        World.getWorld().tileGraphic2(BLOOD_SPLAT, spawn.tile(), 0, 0);
                        splats.add(new BloodSplat(spawn.tile()));
                    }
                    for (BloodSplat splat : splats) {
                        if (splat.age <= 8 && splat.getTile().equals((target.tile()))) {
                            target.hit(mob, World.getWorld().random(5, 15));
                        } else {
                            splats.remove(splat);
                        }
                        splat.age++;
                    }
                }
            }
        };

        Projectile projectile = new Projectile(mob, target, 1578, 0, 100, 50, 25, 0);
        handleDodgableAttack(mob, target, projectile, new Graphic(1576), World.getWorld().random(5, 25),4, task);
    }

    /**
     * Gets the closest player to the npc.
     */
    private Player getClosestPlayer(Mob mob) {
        Player target = null;
        for (Player player : World.getWorld().getPlayers()) {
            if (player == null || player.dead() || player.looks().hidden() || !player.tile().isWithinDistance(mob.tile(), 32))
                continue;
            if (target == null || (target.tile().getDistance(mob.tile()) > player.tile().getDistance(mob.tile()))) {
                target = player;
            }
        }
        return target;
    }

    @Getter
    protected static class BloodSplat {

        public BloodSplat(Tile tile) {
            this.tile = tile;
        }

        private final Tile tile;
        private int age;

    }

    @Override
    public void process(Mob mob) {
        Player closest =  getClosestPlayer(mob);
        if(closest == null) return;
        mob.getCombat().setTarget(closest); //Sets npcs target to the closest player to it, should never be null.
        if (target == null) return;
        mob.getCombat().setTarget(getClosestPlayer(mob)); //Sets npcs target to the closest player to it, should never be null.
        double percent = mob.getHealthPercentage();
        if (percent <= 0.7D && phase == 0 || percent <= 0.5D && phase == 1 || percent <= 0.3D && phase == 2) {
            phase++;
        }
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(NORMAL_ATTACK);

        if (World.getWorld().random(10) == 0) {
            bloodSpawns(mob, target);
        } else {
            var tileDist = mob.tile().transform(1, 1, 0).distance(target.tile());
            var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

            new Projectile(mob, target, 1577, 20, 12 * tileDist, 50, 25, 0).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
        }
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 24;
    }

    @Override
    public void onDeath(Player killer,Npc npc) {
            World.getWorld().getNpcs().forEachInRegion(12613, n -> {
                if (n.tile().getZ() == killer.tile().getZ()) {
                    if (n.isRegistered() && !n.dead() && !n.isPet()) {
                        n.remove();
                    }
                }
            });
        }
    }

