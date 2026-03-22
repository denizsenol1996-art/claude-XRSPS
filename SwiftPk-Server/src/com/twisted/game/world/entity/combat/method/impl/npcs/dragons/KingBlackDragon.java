package com.twisted.game.world.entity.combat.method.impl.npcs.dragons;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatConstants;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.Utils;

import java.util.Arrays;

public class KingBlackDragon extends CommonCombatMethod {

    private enum FireType {
        FIRE, FREEZE, SHOCK, POISON
    }

    private static final int[] SHOCK_STATS = {
        Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGED, Skills.MAGIC
    };

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollDie(4, 1))
            basicAttack(mob, target);
        else {
            if (Utils.rollDie(2, 1))
                fire(mob, target, FireType.FIRE, 0);
            else switch (Utils.random(3)) {
                case 1 -> {
                    fire(mob, target, FireType.FREEZE, 10);
                    if (Utils.rollDie(3, 1))
                        mob.freeze(3, target);
                }
                case 2 -> {
                    fire(mob, target, FireType.SHOCK, 12);
                    if (target != null && Utils.rollDie(3, 1))
                        Arrays.stream(SHOCK_STATS).forEach(skill -> target.skills().alterSkill(skill, -2));
                }
                case 3 -> {
                    fire(mob, target, FireType.POISON, 10);
                    if (Utils.rollDie(3, 1))
                        target.poison(8);
                }
            }
        }

        return true;
    }

    private void basicAttack(Mob mob, Mob target) {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    private void fire(Mob mob, Mob target, FireType fireType, int minMaxDamage) {
        mob.animate(81);
        switch (fireType) {
            case FIRE -> new Projectile(mob, target, 393, mob.projectileSpeed(target), 51, 43, 31, 0, 15, 250).sendProjectile();
            case POISON -> new Projectile(mob, target, 394, mob.projectileSpeed(target), 51, 43, 31, 0, 15, 250).sendProjectile();
            case SHOCK -> new Projectile(mob, target, 395, mob.projectileSpeed(target), 51, 43, 31, 0, 15, 250).sendProjectile();
            case FREEZE -> new Projectile(mob, target, 396, mob.projectileSpeed(target), 51, 43, 31, 0, 15, 250).sendProjectile();
        }

        if(target instanceof Player) {
            Player player = (Player) target;
            double max = Math.max(65, minMaxDamage);
            int antifire_charges = player.getAttribOr(AttributeKey.ANTIFIRE_POTION, 0);
            boolean hasShield = CombatConstants.hasAntiFireShield(player);
            boolean hasPotion = antifire_charges > 0;

            boolean vorkiPetout = player.hasPetOut("Vorki");
            boolean petTamerI = player.<Boolean>getAttribOr(AttributeKey.ANTI_FIRE_RESISTANT,false);

            //System.out.println(vorkiPetout);
            //System.out.println(petTamerI);
            if(vorkiPetout && petTamerI) {
                player.message("Your Vorki pet protects you completely from the heat of the dragon's breath!");
                max = 0.0;
            }

            boolean memberEffect = player.getMemberRights().isDiamondOrGreater(player);
            if (max > 0 && player.<Boolean>getAttribOr(AttributeKey.SUPER_ANTIFIRE_POTION, false) || memberEffect) {
                player.message("Your super antifire potion protects you completely from the heat of the dragon's breath!");
                max = 0.0;
            }

            //Does our player have an anti-dragon shield?
            if (max > 0 && hasShield) {
                player.message("Your shield absorbs most of the dragon fire!");
                max *= 0.3;
            }

            //Has our player recently consumed an antifire potion?
            if (max > 0 && hasPotion) {
                player.message("Your potion protects you from the heat of the dragon's breath!");
                max *= 0.3;
            }

            //Is our player using protect from magic?
            if (max > 0 && Prayers.usingPrayer(player, Prayers.PROTECT_FROM_MAGIC)) {
                player.message("Your prayer absorbs most of the dragon's breath!");
                max *= 0.6;
            }

            if (hasShield && hasPotion) {
                max = 0.0;
            }

            int hit = Utils.random((int) max);
            player.hit(mob, hit, 2, CombatType.MAGIC).submit();
            if (max == 65 && hit > 0) {
                player.message("You are badly burned by the dragon fire!");
            }
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return 4;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }

}
