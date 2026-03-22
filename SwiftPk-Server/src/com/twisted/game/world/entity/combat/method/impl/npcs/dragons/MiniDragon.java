package com.twisted.game.world.entity.combat.method.impl.npcs.dragons;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatConstants;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.NpcIdentifiers;
import com.twisted.util.Utils;

import java.util.Objects;

import static com.twisted.util.CustomItemIdentifiers.HAUNTED_DRAGONFIRE_SHIELD;
//donet

/**
 * @author The Plateau
 * @date 6.13.2022
 */
public class MiniDragon extends CommonCombatMethod {

    boolean fire;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollDie(5, 4))
            basicAttack(mob, target);
        else if (!fire && Utils.rollDie(2, 1))
            meleeDragonfire(mob, target);
        else if (!fire && Utils.rollDie(1, 1))
            rangeAttack((Npc) mob, target);
        else
            magicAttack(((Npc) mob), target);

        return true;
    }

    private void rangeAttack(Npc npc, Mob target) {
        mob.animate(6722);
        npc.face(null); // Stop facing the target
        World.getWorld().getPlayers().filter(Objects::nonNull).filter(p -> p.tile().region() == 9369).forEach(p -> {
            if (target.tile().inSqRadius(p.tile(), 12)) {
                var delay = mob.getProjectileHitDelay(target);
                new Projectile(npc, p, 298, 32, mob.projectileSpeed(target), 30, 30, 0).sendProjectile();
                p.hit(npc, CombatFactory.calcDamageFromType(npc, p, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
            }
        });

        npc.face(target.tile()); // Go back to facing the target.
    }

    private void meleeDragonfire(Mob mob, Mob target) {
        fire = true;
        mob.animate(81);
        mob.graphic(1, 100, 0);
        if (target instanceof Player) {
            if (!CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
                return;
            }
            World.getWorld().getPlayers().filter(Objects::nonNull).filter(p -> p.tile().region() == 9369).forEach(p -> {
                double max = 50.0;
                int antifire_charges = p.getAttribOr(AttributeKey.ANTIFIRE_POTION, 0);
                boolean hasShield = CombatConstants.hasAntiFireShield(p);
                boolean hasPotion = antifire_charges > 0;
                boolean vorkiPetout = p.hasPetOut("Vorki");
                boolean petTamerI = p.<Boolean>getAttribOr(AttributeKey.ANTI_FIRE_RESISTANT, false);

                //System.out.println(vorkiPetout);
                //System.out.println(petTamerI);
                if (vorkiPetout && petTamerI) {
                    p.message("Your Vorki pet protects you completely from the heat of the mini donator boss breath!");
                    max = 0.0;
                }

                boolean memberEffect = p.getMemberRights().isDiamondOrGreater(p) && !WildernessArea.inWilderness(p.tile());
                if (max > 0 && p.<Boolean>getAttribOr(AttributeKey.SUPER_ANTIFIRE_POTION, false) || memberEffect) {
                    p.message("Your super antifire potion protects you completely from the heat of the mini donator boss breath!");
                    max = 0.0;
                }

                //Does our player have an anti-dragon shield?
                if (max > 0 && (p.getEquipment().hasAt(EquipSlot.SHIELD, 11283) || p.getEquipment().hasAt(EquipSlot.SHIELD, 11284) || p.getEquipment().hasAt(EquipSlot.SHIELD, HAUNTED_DRAGONFIRE_SHIELD) ||
                    p.getEquipment().hasAt(EquipSlot.SHIELD, 1540))) {
                    p.message("Your shield absorbs most of the dragon fire!");
                    max *= 0.3;
                }

                //Has our player recently consumed an antifire potion?
                if (max > 0 && antifire_charges > 0) {
                    p.message("Your potion protects you from the heat of the mini donator boss breath!");
                    max *= 0.3;
                }

                //Is our player using protect from magic?
                if (max > 0 && Prayers.usingPrayer(p, Prayers.PROTECT_FROM_MAGIC)) {
                    p.message("Your prayer absorbs most of the mini donator boss breath!");
                    max *= 0.6;
                }

                if (hasShield && hasPotion) {
                    max = 0.0;
                }
                int hit = Utils.random((int) max);
                p.hit(mob, hit, mob.getProjectileHitDelay(p), CombatType.MAGIC).submit();
                if (max == 50 && hit > 0) {
                    p.message("You are badly burned by the dragon fire!");
                }
            });
        }
    }

    private void basicAttack(Mob mob, Mob target) {
        World.getWorld().getPlayers().filter(Objects::nonNull).filter(p -> p.tile().region() == 9369).forEach(p -> {
            mob.forceChat("basic");
            fire = false;
            p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
            mob.animate(mob.attackAnimation());
        });
    }

    private void magicAttack(Npc npc, Mob target) {

        World.getWorld().getPlayers().filter(Objects::nonNull).filter(p -> p.tile().region() == 9369).forEach(p -> {
            fire = false;
            npc.animate(6722);
            if (npc.id() == NpcIdentifiers.MINI_DONATOR_DRAGON) {
                new Projectile(npc, p, 134, npc.projectileSpeed(target), 60, 10, 31, 0, 10, 16).sendProjectile();
            } else {
                System.err.println("Assigned brutal dragon script with no projectile, npc id " + npc.id());
            }

            p.hit(npc, CombatFactory.calcDamageFromType(npc, p, CombatType.MAGIC), npc.getProjectileHitDelay(target), CombatType.MAGIC).submit();
        });
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
