package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.kalphite;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
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

import java.lang.ref.WeakReference;

import static com.twisted.util.NpcIdentifiers.KALPHITE_QUEEN_6501;

/**
 * Created by Jason MacKeigan on 2016-06-29.
 *
 * The purpose of this singleton is to represent the second form of the Kalphite Queen
 * npc.
 */
public class KalphiteQueenFirstForm extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        int distance = getAttackDistance(target);
        boolean inDistance = target.boundaryBounds().within(mob.tile(), mob.getSize(), distance);
        if (inDistance) {
            if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollDie(4, 1)) {
                attack(((Npc)mob), ((Player)target), CombatType.MELEE);
            } else {
                attack(((Npc)mob), ((Player)target), Utils.percentageChance(50) ? CombatType.MAGIC : CombatType.RANGED);
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

    public static void death(Npc form1) {
        form1.respawns(false); // ok order is fine
        form1.lock();
        var targ = form1.<WeakReference<Mob>>getAttribOr(AttributeKey.TARGET, new WeakReference<Mob>(null)).get();
        Chain.bound(null).runFn(4, () -> {
            form1.transmog(6501);
            form1.animate(6270);
            form1.graphic(1055);
        }).then(13, () -> {
            form1.combatInfo(World.getWorld().combatInfo(KALPHITE_QUEEN_6501));
            form1.def(World.getWorld().definitions().get(NpcDefinition.class, KALPHITE_QUEEN_6501));
            form1.heal(form1.maxHp());
            form1.unlock();
            if (targ != null) {
                form1.face(targ.tile());
                form1.getCombat().attack(targ);
                form1.cloneDamage(form1);
            }
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
