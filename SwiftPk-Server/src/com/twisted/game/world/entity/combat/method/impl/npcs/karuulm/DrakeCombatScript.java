package com.twisted.game.world.entity.combat.method.impl.npcs.karuulm;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.game.world.entity.combat.CombatFactory.MELEE_COMBAT;

/**
 * @author Patrick van Elderen | December, 22, 2020, 14:57
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class DrakeCombatScript extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        Npc npc = (Npc) mob;

        if (npc instanceof Drake) {
            Drake drake = (Drake) npc;
            drake.recordedAttacks--;

            if (drake.recordedAttacks == 0) {
                volcanicBreath(drake, target);
                drake.recordedAttacks = 7;
            } else {
                regularAttack(drake, target);
            }
        }

        return true;
    }

    /**
     * Sends the drakes's ranged or melee attack.
     */
    private void regularAttack(Npc drake, Mob target) {
        var meleeAttack = CombatFactory.canReach(drake, MELEE_COMBAT, target);

        if (meleeAttack && World.getWorld().rollDie(2,1)) {
            drake.animate(8275);
            target.hit(drake, CombatFactory.calcDamageFromType(drake, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
        } else {
            drake.animate(8276);
            new Projectile(drake, target,1636, 68, 40, 25, 31,0,16,96).sendProjectile();
            target.hit(drake, CombatFactory.calcDamageFromType(drake, target, CombatType.RANGED), 2, CombatType.RANGED).checkAccuracy().submit();
        }
    }

    /**
     * Sends the volcanic breath special attack.
     */
    private void volcanicBreath(Npc drake, Mob target) {
        drake.animate(8276);
        final var tile = target.tile().copy();
        new Projectile(drake.getCentrePosition(), tile, 1637, 1, 125, 40, 25, 0,0,16,96).sendProjectile();
        Chain.bound(null).runFn(5, () -> {
            World.getWorld().tileGraphic(1638, tile, 0, 0);
            if (target.tile().equals(tile)) {
                for (int hits = 0; hits < 4; hits++) {
                    Chain.bound(null).name("drake_special_attack_task").runFn(hits, () -> {
                        target.hit(drake, World.getWorld().random(6, 8));
                    });
                }
            }
        });
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
