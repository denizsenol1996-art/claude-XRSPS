package com.twisted.game.world.entity.combat.method.impl.npcs.misc.revenants;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.util.CustomNpcIdentifiers;

import java.security.SecureRandom;

/**
 * @author Patrick van Elderen | Zerikoth
 * <p>
 * Revenants use all three forms of attack.
 * Their attacks have very high if not 100% accuracy, and will often deal high damage.
 * They will react to a player's overhead prayers and defensive bonuses.
 * By default, all revenants attack with Magic, but can quickly adapt based on the player's defensive bonuses and prayers.
 */
public class Revenant extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {//bloodrevsupdate

        SecureRandom secureRandom = new SecureRandom();
        double chance = secureRandom.nextDouble();
        Npc npc = (Npc) mob;

        if (npc.hp() < npc.maxHp() / 2 && World.getWorld().rollDie(5, 1)&& npc.id() != CustomNpcIdentifiers.MENDING_REVENANT_ORK && npc.id() != CustomNpcIdentifiers.MENDING_REVENANT_KNIGHT) {
            if (chance < 0.5D) {
                npc.graphic(1221);
                npc.heal(npc.maxHp() / 3);
            }
        } else if (CombatFactory.canAttack(mob, CombatFactory.MELEE_COMBAT, target) && World.getWorld().random(2) == 1)
            meleeAttack(npc, target);
        else if (World.getWorld().rollDie(2, 1))
            rangedAttack(npc, target);
        else
            magicAttack(npc, target);

        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }

    private void meleeAttack(Npc npc, Mob target) {
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MAGIC), CombatType.MAGIC).checkAccuracy().submit();
        npc.animate(npc.attackAnimation());
    }

    private void rangedAttack(Npc npc, Mob target) {
        var tileDist = npc.tile().transform(1, 1, 0).distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
        npc.animate(npc.attackAnimation());
        new Projectile(npc, target, 206, 12 * tileDist, 31, 30, 31, 0, 15, 10).sendProjectile();
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
    }

    private void magicAttack(Npc npc, Mob target) {
        var tileDist = npc.tile().transform(1, 1, 0).distance(target.tile());
        var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
        npc.animate(npc.attackAnimation());
        new Projectile(npc, target, 1415, 12 * tileDist, 31, 43, 31, 0, 15, 10).sendProjectile();
        int damage = CombatFactory.calcDamageFromType(npc, target, CombatType.MAGIC);
        target.hit(npc, damage, delay, CombatType.MAGIC).checkAccuracy().submit();
        target.delayedGraphics(damage > 0 ? new Graphic(1454, 124, 0) : new Graphic(85, 124, 0), delay);
    }
}
