package com.twisted.game.world.entity.combat.method.impl.npcs.bosses;

import com.twisted.game.task.Task;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

import java.security.SecureRandom;

public class LizardShaman extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        var random = new SecureRandom().nextInt(5);
        Npc npc = (Npc) mob;
        switch (random) {
            case 1 -> jump_attack(npc, target);
            case 2 -> cast_spawn_destructive_minions(npc, target);
            case 3 -> green_acidic_attack(npc, target);
            default -> {
                if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target))
                    primary_melee_attack(npc, target);
                else primate_ranged_attack(npc, target);
            }
        }
        return true;
    }

    private void cast_spawn_destructive_minions(Npc npc, Mob target) {
        npc.animate(7157);
        npc.getTimerRepository().register(TimerKey.COMBAT_ATTACK, 5);
        Task.runOnceTask(3, t -> {
            for (int i = 1; i < 7; i += 2) {
                spawn_destructive_minions(target, i);
            }
        });
    }

    private void spawn_destructive_minions(Mob target, int explodeTick) {
        Npc spawn = new Npc(6768, new Tile(target.tile().x + Utils.random(2), target.tile().y + Utils.random(2)));
        spawn.respawns(false);
        spawn.noRetaliation(true);
        spawn.combatInfo(World.getWorld().combatInfo(6768));
        spawn.getCombat().setTarget(target);
        spawn.getMovementQueue().follow(target);
        World.getWorld().registerNpc(spawn);

        Chain.runGlobal(explodeTick, () -> {
            spawn.animate(7159);
            spawn.stopActions(true);
        }).then(2, () -> {
            spawn.hidden(true);
            World.getWorld().tileGraphic(1295, spawn.tile(), 1, 0);

            World.getWorld().getPlayers().forEach(p -> {
                if (p.tile().inSqRadius(spawn.tile(), 1))
                    p.hit(spawn, Utils.random(10));
            });
        }).then(2, () -> World.getWorld().unregisterNpc(spawn));
    }

    private void primary_melee_attack(Npc npc, Mob target) {
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        npc.animate(npc.attackAnimation());
        npc.getTimerRepository().register(TimerKey.COMBAT_ATTACK, npc.combatInfo().attackspeed);
    }

    private void primate_ranged_attack(Npc npc, Mob target) {
        int tileDist = npc.tile().transform(1, 1, 0).distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

        npc.animate(7193);
        new Projectile(npc, target, 1291, 120, 12 * tileDist, 120, 43, 0, 14, 5).sendProjectile();
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
        npc.getTimerRepository().register(TimerKey.COMBAT_ATTACK, npc.combatInfo().attackspeed);
    }

    private void jump_attack(Npc npc, Mob target) {
        var jump_destination = target.tile();

        npc.animate(7152);
        npc.lockNoDamage();
        Chain.bound(null).name("LizardShamanJumpAttackTask").runFn(3, () -> {
            npc.hidden(true);// removes from client view
            npc.teleport(jump_destination);// just sets new location, doesn't do any npc updating changes (npc doesn't support TELEPORT like players do)
        }).then(2, () -> {
            npc.animate(6946);
            npc.hidden(false);
            npc.face(target.tile());
            npc.unlock();
            npc.getCombat().attack(target);
            if (target.tile().inSqRadius(jump_destination, 1))
                target.hit(npc, World.getWorld().random(25));
        });
    }

    private void green_acidic_attack(Npc npc, Mob target) {
        var green_acidic_orb = new Tile(target.tile().x, target.tile().y);
        var green_acidic_orb_distance = npc.tile().distance(green_acidic_orb);
        var green_acidic_orb_delay = Math.max(2, (900 + green_acidic_orb_distance * 12) / 15);
        var green_acidic_orb_hit_delay = Math.max(1, (30 + green_acidic_orb_distance * 12) / 18);

        npc.animate(7193);
        new Projectile(npc.tile().transform(1, 1, 0), green_acidic_orb, 1293, 0, green_acidic_orb_delay, 12 * green_acidic_orb_distance, 90, 0, 0, 25, 10).sendProjectile();
        World.getWorld().tileGraphic(1294, green_acidic_orb, 1, 24 * green_acidic_orb_hit_delay);

        World.getWorld().getPlayers().forEach(p -> {
            //When projectile hits the ground check if we're in range
            Chain.runGlobal(4, () -> {
                if (p.tile().inSqRadius(green_acidic_orb, 1)) {
                    target.hit(npc, Utils.random(30), CombatType.MAGIC).submit();
                    if (Utils.rollDie(2, 1)) {
                        p.poison(World.getWorld().random(30));
                    }
                }
            });
        });

        npc.getTimerRepository().register(TimerKey.COMBAT_ATTACK, npc.combatInfo().attackspeed);
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
