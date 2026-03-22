package com.twisted.game.world.entity.combat.method.impl.npcs.hydra;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.npc.Npc;

/**
 * @author Patrick van Elderen | March, 20, 2021, 18:41
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class AlchemicalHydraAttackScript extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        Npc mobAsNpc = (Npc) mob;

        if (mobAsNpc instanceof AlchemicalHydra) {
            AlchemicalHydra npc = (AlchemicalHydra) mobAsNpc;

            npc.recordedAttacks--;
            var nextAttackType = npc.getNextAttack();

            if (nextAttackType == HydraAttacks.RANGED || nextAttackType == HydraAttacks.MAGIC) {
                npc.currentAttack = nextAttackType;
            }

            nextAttackType.executeAttack(npc, target.getAsPlayer());
            npc.getCombat().delayAttack(5);
        }
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 5;
    }
}
