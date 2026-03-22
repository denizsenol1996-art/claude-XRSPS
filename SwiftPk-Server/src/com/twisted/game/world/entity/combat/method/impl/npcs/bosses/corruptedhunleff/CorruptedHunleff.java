package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.corruptedhunleff;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.CombatMethod;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Tile;

import static com.twisted.game.world.entity.combat.method.impl.npcs.bosses.corruptedhunleff.CorruptedHunleff.Phase.*;

public class CorruptedHunleff extends Npc {

    public CorruptedHunleff.CombatAI getCombatAI() {
        return combatAI;
    }

    private final CorruptedHunleff.CombatAI combatAI;
    private CorruptedHunleff.Phase phase;

    public CorruptedHunleff(int id, Tile tile) {
        super(id, tile);
        this.phase = CorruptedHunleff.Phase.forId(id);
        this.combatAI = new CorruptedHunleff.CombatAI(this);
        this.setCombatMethod(combatAI);
    }

    enum Phase {
        MELEE(9035, CombatType.MELEE, new CorruptedHunleffCombatStrategy()),
        RANGED(9036, CombatType.RANGED, new CorruptedHunleffCombatStrategy()),
        MAGIC(9037, CombatType.MAGIC, new CorruptedHunleffCombatStrategy());

        public final int npcId;
        public final CombatMethod method;
        public final CombatType type;

        Phase(int npcId, CombatType type, CombatMethod method) {
            this.npcId = npcId;
            this.type = type;
            this.method = method;
        }

        public static CorruptedHunleff.Phase forId(int id) {
            for (CorruptedHunleff.Phase phase : values()) {
                if (phase.npcId == id)
                    return phase;
            }
            return null;
        }
    }

    public static class CombatAI extends CommonCombatMethod {

        private final CorruptedHunleff corruptedHunleff;
        private CombatMethod currentMethod;
        private int attacksCounter;

        public CombatAI(CorruptedHunleff corruptedHunleff) {
            this.corruptedHunleff = corruptedHunleff;
            currentMethod = corruptedHunleff.phase.method;
        }

        void updatePhase(CorruptedHunleff.Phase phase) {
            corruptedHunleff.phase = phase;
            currentMethod = phase.method;
            corruptedHunleff.transmog(phase.npcId);
            corruptedHunleff.getCombat().delayAttack(1);
            target.putAttrib(AttributeKey.HUNLESS_PREVIOUS_STYLE, phase);
        }

        public void attacksDone() {
            attacksCounter += 1;
            //System.out.println("attks "+attacksCounter);
            if (attacksCounter >= 4) {
                CorruptedHunleff.Phase old = target.getAttribOr(AttributeKey.HUNLESS_PREVIOUS_STYLE, MELEE);
                //System.out.println("previous phase: "+old);
                if(old == MELEE) {
                    updatePhase(Phase.RANGED);
                } else if(old == RANGED) {
                    updatePhase(MAGIC);
                } else if(old == MAGIC) {
                    updatePhase(MELEE);
                }
                attacksCounter = 0;
            }
        }

        @Override
        public boolean prepareAttack(Mob mob, Mob target) {
            attacksDone();
            currentMethod.prepareAttack(mob, target);
            return true;
        }

        @Override
        public int getAttackSpeed(Mob mob) {
            return currentMethod.getAttackSpeed(mob);
        }

        @Override
        public int getAttackDistance(Mob mob) {
            return currentMethod.getAttackDistance(mob);
        }
    }
}
