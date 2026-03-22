package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author PVE
 * @Since augustus 08, 2020
 */
public class SkeletalWyvern extends CommonCombatMethod {

    private enum AttackStyle {
        MELEE, RANGED, ICE_BREATH
    }

    private AttackStyle attackStyle = AttackStyle.MELEE;
    private final List<Integer> SHIELDS = Arrays.asList(ANCIENT_WYVERN_SHIELD, ANCIENT_WYVERN_SHIELD_21634, DRAGONFIRE_WARD, DRAGONFIRE_WARD_22003, MIND_SHIELD, ELEMENTAL_SHIELD, DRAGONFIRE_SHIELD, DRAGONFIRE_SHIELD_11284);
    private final int[] DRAIN = { Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGED, Skills.MAGIC};

    private void basicAttack(Mob mob, Mob target) {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    private void jumpAttack(Mob mob, Mob target) {
        mob.animate(2989);
        mob.graphic(499);
        new Projectile(mob, target, 500, 50, mob.projectileSpeed(target), 15, 31, 0).sendProjectile();
        int delay = mob.getProjectileHitDelay(target);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
    }

    private void rangedAttack(Mob mob, Mob target) {
        mob.animate(2985);
        new Projectile(mob, target, 500, 50, mob.projectileSpeed(target), 15, 31, 0).sendProjectile();
        int delay = mob.getProjectileHitDelay(target);
        target.delayedGraphics(502, 0, delay);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
    }

    private void iceBreath(Mob mob, Mob target) {
        mob.animate(2988);
        mob.graphic(502);
        int maxDamage = 60;
        if (SHIELDS.contains(target.getAsPlayer().getEquipment().getId(EquipSlot.SHIELD))) {
            maxDamage = 10;
        } else {
            for (int skill : DRAIN) {
                target.getAsPlayer().skills().alterSkill(skill, -9);
            }
            target.getAsPlayer().message("The wyvern's ice breath drains your stats!");
        }
        if (Utils.rollDie(3, 1))
            mob.freeze(3, target);
        target.hit(mob, World.getWorld().random(maxDamage), CombatType.MAGIC).submit();
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollDie(3, 2)) {
            attackStyle = AttackStyle.MELEE;
            basicAttack(mob, target);
        } else if (Utils.rollDie(5, 1)) {
            attackStyle = AttackStyle.ICE_BREATH;
            iceBreath(mob, target);
        } else if (Utils.rollDie(2, 1)) {
            attackStyle = AttackStyle.RANGED;
            rangedAttack(mob, target);
        } else {
            attackStyle = AttackStyle.RANGED;
            jumpAttack(mob, target);
        }

        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return attackStyle == AttackStyle.MELEE ? 1 : attackStyle == AttackStyle.RANGED ? 6 : 5;
    }
}
