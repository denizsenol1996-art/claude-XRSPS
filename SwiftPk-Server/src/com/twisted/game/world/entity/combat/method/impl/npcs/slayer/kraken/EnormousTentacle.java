package com.twisted.game.world.entity.combat.method.impl.npcs.slayer.kraken;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.timers.TimerKey;

import java.lang.ref.WeakReference;

//TODO tents use a special combat mechanic where it does magic attacks with their range accuracy against the players magic defence
public class EnormousTentacle extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        new Projectile(mob, target, 162, 32,65, 30, 30, 0).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 1, CombatType.MAGIC).checkAccuracy().submit();
        target.graphic(163);

        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getAsNpc().getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 16;
    }

    public static void onHit(Player player, Npc npc) {
        // This hook is only relevent when we're in whirlpool form.
        if (npc.id() != KrakenBoss.TENTACLE_WHIRLPOOL || npc.transmog() == KrakenBoss.TENTACLE_NPCID || npc.hidden()) { // Not transformed yet
            return;
        }
        //We only want the NPC to transmog once
        if (npc.transmog() != KrakenBoss.TENTACLE_NPCID) {
            npc.transmog(KrakenBoss.TENTACLE_NPCID);
            npc.animate(3860);
            npc.getTimerRepository().extendOrRegister(TimerKey.COMBAT_ATTACK, 1);
            npc.combatInfo(World.getWorld().combatInfo(5535));
            npc.setCombatMethod(World.getWorld().combatInfo(KrakenBoss.TENTACLE_NPCID).scripts.newCombatInstance());
            npc.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(player));
        }
    }
}
