package com.twisted.game.world.entity.combat.formula;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.content.skill.impl.slayer.Slayer;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.weapon.AttackType;
import com.twisted.game.world.entity.combat.weapon.FightStyle;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.container.equipment.EquipmentBonuses;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.NpcIdentifiers;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Origin
 * @Since January 16 2022
 */
public class MeleeMaxHit {

    /**
     * The max hit
     *
     * @param player        The player performing the hit
     * @param includeNpcMax The npc is a PvP combat dummy
     * @return return the max hit based on the given calculations
     */

    public static int maxHit(Player player, boolean includeNpcMax) {

        double specialMultiplier = player.getCombatSpecial() == null ? 1 : player.getCombatSpecial().getSpecialMultiplier();
        /**
         * Max Hit
         *
         */

        int maxHit = (int) Math.floor(getBaseDamage(player) * slayerPerkBonus(player));

        if (player.isSpecialActivated()) {
            maxHit = (int) (maxHit * specialMultiplier);
        }


        if (player.hasPetOut("Lava beast pet") || player.hasPetOut(23818) || player.hasPetOut(24988) || player.hasPetOut("Youngllef pet") || player.hasPetOut("Corrupted nechryarch pet")) {
            maxHit += 1;
        }

        if (player.hasPetOut("Corrupted Youngllef pet") || player.hasPetOut(366)) {
            maxHit += 2;
        }

        if (player.getEquipment().hasAt(EquipSlot.AMULET, AMULET_OF_TORTURE_OR) || player.getEquipment().hasAt(EquipSlot.AMULET, AMULET_OF_FURY_OR) || player.getEquipment().hasAt(EquipSlot.AMULET, BERSERKER_NECKLACE_OR)) {
            maxHit += 1;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR)) {
            maxHit += 20;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, ETHEREAL_SCYTHE)) {
            maxHit += 30;
        }

        if (player.getEquipment().contains(CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR)) {
            maxHit += 9;
        }

        List<Integer> increaseMaxHitbyOne = new ArrayList<>(List.of(GRANITE_MAUL_12848, ARMADYL_GODSWORD_OR, BANDOS_GODSWORD_OR, SARADOMIN_GODSWORD_OR, ZAMORAK_GODSWORD_OR, DRAGON_CLAWS_OR));
        if (increaseMaxHitbyOne.stream().anyMatch(w -> player.getEquipment().hasAt(EquipSlot.WEAPON, w))) {
            maxHit += 1;
        }

        return (int) Math.floor(maxHit);
    }

    public static int getBaseDamage(Player player) {
        return (int) (Math.floor(0.5 + (getEffectiveStrength(player)) * (getStrengthBonus(player) + 64) + 320) / 640.0);
    }

    public static int getStrengthBonus(Player player) {
        EquipmentBonuses bonuses = player.getBonuses().totalBonuses(player, World.getWorld().equipmentInfo());
        return bonuses.str;
    }

    public static int getStrengthLevel(Player player) {
        return player.skills().level(Skills.STRENGTH);
    }

    private static double getPrayerBonus(Player player) {
        /**
         * Prayer Bonus
         *
         */
        double prayerBonus = 1;
        if (Prayers.usingPrayer(player, Prayers.BURST_OF_STRENGTH)) {
            prayerBonus *= 1.05;
        } else if (Prayers.usingPrayer(player, Prayers.SUPERHUMAN_STRENGTH)) {
            prayerBonus *= 1.10;
        } else if (Prayers.usingPrayer(player, Prayers.ULTIMATE_STRENGTH)) {
            prayerBonus *= 1.15;
        } else if (Prayers.usingPrayer(player, Prayers.CHIVALRY)) {
            prayerBonus *= 1.18;
        } else if (Prayers.usingPrayer(player, Prayers.PIETY)) {
            prayerBonus *= 1.23;
        }
        return prayerBonus;
    }

    public static int getStyleBonus(Player player) {
        FightStyle style = player.getCombat().getFightType().getStyle();
        return style.equals(FightStyle.AGGRESSIVE) ? 3 : style.equals(FightStyle.ACCURATE) ? 1 : 0;
    }

    public static double slayerPerkBonus(Player player) {
        Mob target = player.getCombat().getTarget();

        double slayerPerkBonus = 1.0;

        var weakSpot = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.WEAK_SPOT);
        if (weakSpot && target != null && target.isNpc()) {
            if (Slayer.creatureMatches(player, target.getAsNpc().id())) {
                slayerPerkBonus *= 1.10;
            }
        }
        return slayerPerkBonus;
    }

    public static double getPetBonus(Player player, boolean includeNpcMax) {
        double petBonus = 1;
        Mob target = player.getCombat().getTarget();
        /**
         * PetDefinitions bonuses
         *
         */

        return petBonus;
    }

    public static double getOtherBonus(Player player, boolean includeNpcMax) {
        boolean lilZikPet = player.hasPetOut("Lil' Zik");
        boolean lilMaidenPet = player.hasPetOut("Lil' Maiden");
        boolean lilBloatPet = player.hasPetOut("Lil' Bloat");
        boolean lilNyloPet = player.hasPetOut("Lil' Nylo");
        boolean lilSotPet = player.hasPetOut("Lil' Sot");
        boolean lilXarpPet = player.hasPetOut("Lil' Xarp");
        boolean tobPet = lilZikPet
            || lilMaidenPet || lilBloatPet || lilNyloPet || lilSotPet || lilXarpPet;

        double otherBonus = 1;

        Mob target = player.getCombat().getTarget();

        /**
         * Other bonuses
         *
         */
        if (FormulaUtils.voidMelee(player)) {//wupdate
            otherBonus *= 1.10;
        }

        if (FormulaUtils.wearingEliteVoid(player) || FormulaUtils.voidCustomMelee(player)) {
            if (target instanceof Npc) {
                otherBonus *= 1.28;
            } else {
                otherBonus *= 1.15;
            }
        }

        if (FormulaUtils.wearingEliteVoidOr(player)) {//updatevoidor
            if (target instanceof Npc) {
                otherBonus *= 1.30;
            } else {
                otherBonus *= 1.18;
            }
        }

        if (FormulaUtils.eliteTrimmedVoidEquipmentBaseMelee(player)) {
            otherBonus *= 1.125;
        }

        if (FormulaUtils.wearingFullDharok(player)) {
            int hitpoints = player.hp();
            double max = player.maxHp();
            double mult = Math.max(0, ((max - (double) hitpoints) / max) * 100D) + 100D;
            otherBonus *= (mult / 100);
        }

        var wearingAnyBlackMask = FormulaUtils.wearingBlackMask(player) || FormulaUtils.wearingBlackMaskImbued(player) || player.getEquipment().wearingSlayerHelm();
        if (wearingAnyBlackMask && target != null && target.isNpc() && includeNpcMax) {
            Npc npc = target.getAsNpc();
            if (npc.id() == NpcIdentifiers.COMBAT_DUMMY) {
                if (player.getEquipment().hasAt(EquipSlot.HEAD, 30326)) {
                    otherBonus *= 1.3;//with e helm on combat dummy
                } else {
                    otherBonus *= 1.1667;//without e helm on combat dummy

                }
            }
            //without combat dummy and undead dummy vs normal monster
            if (npc.id() != NpcIdentifiers.COMBAT_DUMMY && npc.id() != NpcIdentifiers.UNDEAD_COMBAT_DUMMY) {
                if (player.getEquipment().hasAt(EquipSlot.HEAD, 30326)) {
                    otherBonus *= 1.3;//with e helm on normal monster

                } else {
                    otherBonus *= 1.1667;//without e helm on normal monster and give bonus to another slayer helm

                }
            }
            if (npc.id() == NpcIdentifiers.UNDEAD_COMBAT_DUMMY) {//on undead dummy slayer helm i will be boosted
                if (player.getEquipment().hasAt(EquipSlot.HEAD, TWISTED_SLAYER_HELMET_I)) {
                    otherBonus *= 1.5;

                }
            }
            //Slayer helms on tasks
            if (Slayer.creatureMatches(player, npc.id())) {
                if (player.getEquipment().hasAt(EquipSlot.HEAD, TWISTED_SLAYER_HELMET_I)) {
                    otherBonus *= 1.5;//alone boost with slayer helm i
                } else {
                    otherBonus *= 1.1667;//anoher slayer helms

                }
            }
        }

        if (target instanceof Npc npc
            && ArrayUtils.contains(FormulaUtils.isRevenant(), npc.id())
            && player.getEquipment().contains(AMULET_OF_AVARICE)) {
            otherBonus *= 1.20;
        } else if (player.getEquipment().hasAt(EquipSlot.AMULET, SALVE_AMULETEI) && !wearingAnyBlackMask && target != null && includeNpcMax) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.20;
            }

            if (FormulaUtils.isUndead(target)) {
                otherBonus *= 1.20;
            }
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, RED_SLAYER_HELMET_I) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.10;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, ARCLIGHT) && target != null && includeNpcMax) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.70;
            }

            if (FormulaUtils.isDemon(target)) {
                otherBonus *= 1.70;
            }
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, DARKLIGHT) && target != null && includeNpcMax) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.60;
            }

            if (FormulaUtils.isDemon(target)) {
                otherBonus *= 1.60;
            }
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, DARKLIGHT) && target != null && includeNpcMax) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.175;
            }

            if (target.isNpc() && target.getAsNpc().def() != null && (target.getAsNpc().def().name.equalsIgnoreCase("Kurask") || target.getAsNpc().def().name.equalsIgnoreCase("Turoth"))) {
                otherBonus *= 1.175;
            }
        }

        if (FormulaUtils.obbyArmour(player) && FormulaUtils.hasObbyWeapon(player)) {
            otherBonus *= 1.10;
        }

        if (FormulaUtils.berserkerNecklace(player) && FormulaUtils.hasObbyWeapon(player)) {
            otherBonus *= 1.10;
        }

        if (player.getCombat().getFightType().getAttackType() == AttackType.CRUSH) {
            if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM) || player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK) || player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                otherBonus *= 1.05;
            }
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_LANCE) && target != null && includeNpcMax) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY || FormulaUtils.isDragon(target)) {
                otherBonus *= 1.20;
            }

            if (FormulaUtils.isDragon(target)) {
                otherBonus *= 1.20;
            }
        }

        //• Gadderhammer: 1.25 or 2.0 vs shades
        if (player.getEquipment().hasAt(EquipSlot.WEAPON, GADDERHAMMER)) {
            if (target != null && target.isNpc()) {
                Npc npc = target.getAsNpc();
                NpcDefinition def = npc.def();
                var isShade = def != null && def.name.equalsIgnoreCase("Shade");
                otherBonus *= isShade ? 1.25 : 2.00;
            }
        }

        if (FormulaUtils.hasMeleeWildernessWeapon(player) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.50;
        }

        if (player.getEquipment().hasAt(EquipSlot.HEAD, TOTEMIC_HELMET) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.05;//5% damage boost
        }
        if (player.getEquipment().hasAt(EquipSlot.LEGS, TOTEMIC_PLATELEGS) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.10;//10.0% damage boost
        }
        if (player.getEquipment().hasAt(EquipSlot.BODY, TOTEMIC_PLATEBODY) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.15;//15 % damage boost
        }
        if (player.getEquipment().hasAt(EquipSlot.HEAD, E_TOTEMIC_HELMET) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus += 0.10;//+10% Reduce damage
        }
        if (player.getEquipment().hasAt(EquipSlot.LEGS, E_TOTEMIC_PLATELEGS) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus += 0.15;//+15% Reduce damage
        }
        if (player.getEquipment().hasAt(EquipSlot.BODY, E_TOTEMIC_PLATEBODY) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus += 0.15;//+15% Reduce damage
        }


        if (player.getEquipment().hasAt(EquipSlot.WEAPON, VIGGORAS_CHAINMACE) && target != null && target.isNpc() && includeNpcMax && WildernessArea.inWilderness(player.tile())) {
            if (WildernessArea.inWilderness(player.tile())) {
                otherBonus += 0.50;
            } else {
                otherBonus -= 0.55;
            }
        }

        if (((player.getEquipment().hasAt(EquipSlot.WEAPON, VIGGORAS_CHAINMACE_C) || player.getEquipment().hasAt(EquipSlot.WEAPON, URSINE_CHAINMACE)) && target != null && target.isNpc() && includeNpcMax)) {
            if (player.getEquipment().hasAt(EquipSlot.WEAPON, URSINE_CHAINMACE)) {
                otherBonus *= 1.60;
            } else {
                otherBonus *= 1.45;
            }
        }
        if (player.getEquipment().hasAt(EquipSlot.WEAPON, CustomItemIdentifiers.SWORD_OF_GRYFFINDOR) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.25;
        }
        if (player.getEquipment().hasAt(EquipSlot.WEAPON, 30103) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.25;//dzone3
        }

        /**
         * Pet bonuses
         *
         */

        boolean ancientKingBlackDragonPet = player.hasPetOut("Ancient king black dragon");
        if (ancientKingBlackDragonPet && target != null && target.isNpc() && includeNpcMax) {
            Npc npc = (Npc) target;
            if (npc.def() != null && npc.def().name != null && FormulaUtils.isDragon(npc)) {
                otherBonus *= 1.25;
            }
        }

        if (player.hasPetOut("Skeleton hellhound pet") && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.05;
        }

        if (player.hasPetOut("Olmlet") && target != null && target.isNpc() && includeNpcMax) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.10;
            }

            if (player.getRaids() != null && player.getRaids().raiding(player))
                otherBonus *= 1.10;
        }
        if (tobPet && target != null && target.isNpc() && includeNpcMax) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.10;
            }

            if (player.getRaids() != null && player.getRaids().raiding(player))
                otherBonus *= 1.10;
        }

        if (player.hasPetOut("Pet zombies champion") && target != null && target.isNpc() && target.getAsNpc().isWorldBoss() && includeNpcMax) {
            otherBonus *= 1.10;
        }

        return otherBonus;
    }

    public static int getEffectiveStrength(Player player) {
        return (int) (Math.floor(((((getStrengthLevel(player)) * getPrayerBonus(player)) + getStyleBonus(player)) + 8) * getOtherBonus(player, true)) * getPetBonus(player, true));
    }
}
