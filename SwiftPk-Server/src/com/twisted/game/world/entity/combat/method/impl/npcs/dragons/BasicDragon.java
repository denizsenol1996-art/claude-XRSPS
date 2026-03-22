package com.twisted.game.world.entity.combat.method.impl.npcs.dragons;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatConstants;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Utils;

import static com.twisted.util.CustomItemIdentifiers.HAUNTED_DRAGONFIRE_SHIELD;

public class BasicDragon extends CommonCombatMethod {

    boolean fire;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (!fire && Utils.rollDie(6, 1)) { // don't do dragon fire twice in a row
            breathFire(mob, target);
        } else {
            basicAttack(mob, target);
        }

        return true;
    }

    private void basicAttack(Mob mob, Mob target) {
        fire = false;
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    private void breathFire(Mob mob, Mob target) {
        fire = true;
        if(target instanceof Player) {
            Player player = (Player) target;
            double max = 50.0;
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

            boolean memberEffect = player.getMemberRights().isDiamondOrGreater(player) ;
            if (max > 0 && player.<Boolean>getAttribOr(AttributeKey.SUPER_ANTIFIRE_POTION, false) || memberEffect) {
                player.message("Your super antifire potion protects you completely from the heat of the dragon's breath!");
                max = 0.0;
            }

            //Does our player have an anti-dragon shield?
            if (max > 0 && (player.getEquipment().hasAt(EquipSlot.SHIELD, 11283) || player.getEquipment().hasAt(EquipSlot.SHIELD, 11284) || player.getEquipment().hasAt(EquipSlot.SHIELD, HAUNTED_DRAGONFIRE_SHIELD) || player.getEquipment().hasAt(EquipSlot.SHIELD, 1540))) {
                player.message("Your shield absorbs most of the dragon fire!");
                max *= 0.3;
            }

            //Has our player recently consumed an antifire potion?
            if (max > 0 && antifire_charges > 0) {
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
            player.hit(mob, hit, CombatType.MAGIC).submit();
            if (max == 50 && hit > 0) {
                player.message("You are badly burned by the dragon fire!");
            }
            mob.animate(81);
            mob.graphic(1, 100, 0);
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return 4;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1; //This may sound incorrect but 1 is the proper attack distance for combat following of basic dragons.
    }
}
