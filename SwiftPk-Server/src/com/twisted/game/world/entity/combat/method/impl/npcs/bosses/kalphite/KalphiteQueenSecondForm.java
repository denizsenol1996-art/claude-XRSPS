package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.kalphite;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 24, 2020
 */
public class KalphiteQueenSecondForm extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        int distance = getAttackDistance(target);
        boolean inDistance = target.boundaryBounds().within(mob.tile(), mob.getSize(), distance);
        if (inDistance) {
            if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollDie(4, 1)) {
                attack(((Npc)mob), ((Player)target), CombatType.MELEE);
            } else {
                int random = Utils.RANDOM_GEN.get().nextInt(100);
                attack(((Npc)mob), ((Player)target), random < 50 ? CombatType.MAGIC : CombatType.RANGED);
            }
        }
        return true;
    }

    private void attack(Npc npc, Player target, CombatType combatType) {

        int attackAnimation = KalphiteQueen.animation(npc.id(), combatType);

        npc.animate(attackAnimation);

        switch(combatType) {
            case MELEE:
                target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
                break;
            case RANGED:
                for(Player player : World.getWorld().getPlayers()) {
                    if(player != null && player.tile().inArea(KalphiteQueen.getArea())) {
                        new Projectile(npc, target, 473, 60, 41, 45, 30, 0, 10, 15).sendProjectile();
                        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
                    }
                }
                break;
            case MAGIC:
                npc.graphic(278);
                for(Player player : World.getWorld().getPlayers()) {
                    if (player != null && player.tile().inArea(KalphiteQueen.getArea())) {
                        new Projectile(npc, target, 280, 60, 41, 45, 30, 0, 10, 15).sendProjectile();
                        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().submit();

                        target.delayedGraphics(281,0,2);
                    }
                }
                break;
        }
        npc.getTimerRepository().register(TimerKey.COMBAT_ATTACK, 4);
    }

    public static void death(Npc npc) {
        Npc spawn = new Npc(6500, npc.tile());

        Chain.bound(null).runFn(spawn.combatInfo().respawntime, () -> {
            spawn.respawns(false);
            World.getWorld().registerNpc(spawn);
            npc.animate(6240);
            //here? not sure show ingame again
        });
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
