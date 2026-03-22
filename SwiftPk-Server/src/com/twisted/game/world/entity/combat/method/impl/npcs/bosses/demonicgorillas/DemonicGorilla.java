package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.demonicgorillas;

import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.CombatMethod;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;

/**
 * @author Patrick van Elderen | March, 13, 2021, 22:08
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class DemonicGorilla extends Npc {

    public CombatAI getCombatAI() {
        return combatAI;
    }

    private final CombatAI combatAI;
    private Phase phase;

    public DemonicGorilla(int id, Tile tile) {
        super(id, tile);
        this.phase = Phase.forId(id);
        this.combatAI = new CombatAI(this);
        this.setCombatMethod(combatAI);
    }

    @Override
    public void onHit(Hit hit) {
        if (hp() - hit.getDamage() > 0) {
            combatAI.onDamageTaken(hit);
        }
    }

    enum Phase {
        MELEE(7144, CombatType.MELEE, new DemonicGorillaMeleeStrategy()),
        RANGED(7145, CombatType.RANGED, new DemonicGorillaRangedStrategy()),
        MAGIC(7146, CombatType.MAGIC, new DemonicGorillaMagicStrategy());

        public final int npcId;
        public final CombatMethod method;
        public final CombatType type;

        Phase(int npcId, CombatType type, CombatMethod method) {
            this.npcId = npcId;
            this.type = type;
            this.method = method;
        }

        public static Phase forType(CombatType type) {
            for (Phase phase : values()) {
                if (phase.type == type)
                    return phase;
            }
            return MELEE;
        }

        public static Phase nextPhase(Phase old) {
            return values()[old.ordinal() + 1 % values().length - 1];
        }

        public static Phase forId(int id) {
            for (Phase phase : values()) {
                if (phase.npcId == id)
                    return phase;
            }
            return null;
        }
    }

    public static class CombatAI extends CommonCombatMethod {

        private final DemonicGorilla demonic;
        private CombatMethod currentMethod;
        private int missCounter;
        private int damageCounter;

        public CombatAI(DemonicGorilla demonic) {
            this.demonic = demonic;
            currentMethod = demonic.phase.method;
        }

        void updatePhase(Phase phase) {
            demonic.phase = phase;
            currentMethod = phase.method;
            demonic.transmog(phase.npcId);
            demonic.getCombat().delayAttack(1);
        }

        public void onDamageTaken(Hit hit) {
            damageCounter += hit.getDamage();
            if (damageCounter >= 50) {
                updatePhase(Phase.forType(hit.getCombatType()));
                damageCounter = 0;
            }
        }

        @Override
        public boolean prepareAttack(Mob mob, Mob target) {
            if (demonic.phase == Phase.MAGIC || demonic.phase == Phase.RANGED) {
                if (Utils.random(4) == 1) {
                    boulderToss(mob, target);
                    mob.getCombat().delayAttack(4);
                    return false;
                }
            }
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

        public void handleAfterHit(Hit hit) {
            if (hit.getDamage() == 0) {
                missCounter++;
                if (missCounter == 4) {
                    updatePhase(Phase.nextPhase(demonic.phase));
                    missCounter = 0;
                }
            }
        }

        private void boulderToss(Mob mob, Mob target) {
            Tile boulderTile = target.tile().clone();
            mob.animate(7228);
            new Projectile(mob.tile().transform(1, 1,0), boulderTile, 1, 856, 200, 30, 200, 6, 0).sendProjectile();

            TaskManager.submit(new Task("boulderTossTask",8) {
                @Override
                protected void execute() {
                    if (target.tile().inSqRadius(boulderTile, 1))
                        target.hit(mob, (int) Math.ceil(target.maxHp() * 0.33));
                    World.getWorld().tileGraphic(305, boulderTile, 5, 0);
                    stop();
                }
            });
        }
    }
}
