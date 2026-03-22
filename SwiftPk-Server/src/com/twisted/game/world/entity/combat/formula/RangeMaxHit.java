package com.twisted.game.world.entity.combat.formula;

import com.twisted.game.content.skill.impl.slayer.Slayer;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.weapon.FightStyle;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.equipment.EquipmentBonuses;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.NpcIdentifiers;
import org.apache.commons.lang.ArrayUtils;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since November 06, 2021
 */
public class RangeMaxHit {

    public static double getBaseDamage(Player player, boolean factorInAmmoRangeStr) {
        EquipmentBonuses bonuses = player.getBonuses().totalBonuses(player, World.getWorld().equipmentInfo(), !factorInAmmoRangeStr);
        return (1.3 + (getEffectiveRanged(player) / 10) + (bonuses.rangestr / 80D) + (getEffectiveRanged(player) * bonuses.rangestr / 640));
    }

    public static int getRangedlevel(Player player) {
        return player.skills().level(Skills.RANGED);
    }

    public static double getEffectiveRanged(Player player) {
        return Math.floor(((getRangedlevel(player)) * getPrayerBonus(player)) * getOtherBonus(player, true)) + getStyleBonus(player);
    }

    public static double getPrayerBonus(Player player) {
        double prayerBonus = 1;
        if (Prayers.usingPrayer(player, Prayers.SHARP_EYE)) {
            prayerBonus = 1.05;
        } else if (Prayers.usingPrayer(player, Prayers.HAWK_EYE)) {
            prayerBonus = 1.10;
        } else if (Prayers.usingPrayer(player, Prayers.EAGLE_EYE)) {
            prayerBonus = 1.15;
        } else if (Prayers.usingPrayer(player, Prayers.RIGOUR)) {
            prayerBonus = 1.23;
        }

        var isMSB = player.getEquipment().hasAt(EquipSlot.WEAPON, MAGIC_SHORTBOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, MAGIC_SHORTBOW_I);
        var ignoreMSBBonus = isMSB && player.isSpecialActivated();

        if (ignoreMSBBonus) {
            prayerBonus = 1.0;
        }

        return prayerBonus;
    }

    public static int getStyleBonus(Player player) {
        FightStyle style = player.getCombat().getFightType().getStyle();
        return style.equals(FightStyle.ACCURATE) ? 3 : 0;
    }

    public static double getOtherBonus(Player player, boolean includeNpcMax) {
        double otherBonus = 1;


        Mob target = player.getCombat().getTarget();
        if (target != null) {

            otherBonus = getPetBonus(player, includeNpcMax, target, otherBonus);

            Item weapon = player.getEquipment().get(EquipSlot.WEAPON);

            if (weapon != null) {
                otherBonus = getWeaponBonus(player, includeNpcMax, weapon, target, otherBonus);
            }

            otherBonus = getEquipmentBonus(player, includeNpcMax, target, otherBonus);
        }

        return otherBonus;
    }

    private static double getEquipmentBonus(final Player player, final boolean includeNpcMax, final Mob target, double otherBonus) {
        if (player.getEquipment().hasAt(EquipSlot.HEAD, 30326) && target.isNpc() && includeNpcMax) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus += 0.15;

            } else {
                if (target.isNpc() && target.getAsNpc().id() != NpcIdentifiers.UNDEAD_COMBAT_DUMMY) {
                    if (!Slayer.creatureMatches(player, target.getAsNpc().id())) {
                        otherBonus += 0.15;
                    }
                }
            }

        }

        if (player.getEquipment().hasAt(EquipSlot.HEAD, TWISTED_SLAYER_HELMET_I) && target.isNpc() && includeNpcMax) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.UNDEAD_COMBAT_DUMMY) {
                otherBonus += 0.40;
            } else {
                if (Slayer.creatureMatches(player, target.getAsNpc().id())) {
                    otherBonus += 0.40;

                }
            }
        }

        if (FormulaUtils.voidRanger(player)) {
            otherBonus *= 1.10;
        }

        if (FormulaUtils.eliteVoidEquipmentRanged(player) || FormulaUtils.eliteTrimmedVoidEquipmentBaseRanged(player)) {
            if (target instanceof Npc) {
                otherBonus *= 1.28;
            } else {
                otherBonus *= 1.15;
            }
        }
        if (FormulaUtils.wearingEliteVoidOr(player)) {
            if (target instanceof Npc) {
                otherBonus *= 1.30;
            } else {
                otherBonus *= 1.18;
            }
        }
        if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULET)) {
            if (target instanceof Npc) {
                otherBonus *= 1.15;
            }
        }

        if (target instanceof Npc npc
            && ArrayUtils.contains(FormulaUtils.isRevenant(), npc.id())
            && player.getEquipment().contains(AMULET_OF_AVARICE)) {
            otherBonus *= 1.20;
        } else if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETI) || player.getEquipment().contains(SALVE_AMULET_E) || player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETEI)) {
            if (target instanceof Npc) {
                otherBonus *= 1.20;
            }
        }

        if (target instanceof Npc && player.getEquipment().contains(BLOOD_MASORI_HELM)) {
            otherBonus *= 1.116;
        }

        if (target instanceof Npc && player.getEquipment().contains(BLOOD_MASORI_PLATEBODY)) {
            otherBonus *= 1.116;
        }

        if (target instanceof Npc && player.getEquipment().contains(BLOOD_MASORI_PLATELEGS)) {
            otherBonus *= 1.116;
        }


        if (player.getEquipment().hasAt(EquipSlot.HEAD, CRYSTAL_HELM)) {
            otherBonus *= 1.025;//2.5% damage boost
        }

        if (player.getEquipment().hasAt(EquipSlot.BODY, CRYSTAL_BODY)) {
            otherBonus *= 1.075;//7.5% damage boost
        }

        if (player.getEquipment().hasAt(EquipSlot.LEGS, CRYSTAL_LEGS)) {
            otherBonus *= 1.05;//5.0% damage boost
        }

        if (player.getEquipment().hasAt(EquipSlot.HEAD, CORRUPTED_CRYSTAL_HELM)) {
            otherBonus *= 1.100;
        }

        if (player.getEquipment().hasAt(EquipSlot.BODY, CORRUPTED_CRYSTAL_BODY)) {
            otherBonus *= 1.125;
        }

        if (player.getEquipment().hasAt(EquipSlot.HEAD, SARKIS_DARK_COIF) && target.isNpc() && includeNpcMax) {
            otherBonus -= 0.05;//-5% Reduce damage
        }

        if (player.getEquipment().hasAt(EquipSlot.LEGS, SARKIS_DARK_LEGS) && target.isNpc() && includeNpcMax) {
            otherBonus -= 0.05;//-5% Reduce damage
        }

        if (player.getEquipment().hasAt(EquipSlot.BODY, SARKIS_DARK_BODY) && target.isNpc() && includeNpcMax) {
            otherBonus -= 0.05;//-5% Reduce damage
        }

        if (player.getEquipment().hasAt(EquipSlot.HEAD, E_TOTEMIC_HELMET) && target.isNpc() && includeNpcMax) {
            otherBonus += 0.10;//+10% Reduce damage
        }

        if (player.getEquipment().hasAt(EquipSlot.LEGS, E_TOTEMIC_PLATELEGS) && target.isNpc() && includeNpcMax) {
            otherBonus += 0.15;//+15% Reduce damage
        }

        if (player.getEquipment().hasAt(EquipSlot.BODY, E_TOTEMIC_PLATEBODY) && target.isNpc() && includeNpcMax) {
            otherBonus += 0.15;//+15% Reduce damage
        }

        if (player.getEquipment().hasAt(EquipSlot.LEGS, CORRUPTED_CRYSTAL_LEGS)) {
            otherBonus *= 1.125;
        }

        var weakSpot = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.WEAK_SPOT);

        if (weakSpot && target.isNpc()) {
            if (Slayer.creatureMatches(player, target.getAsNpc().id())) {
                otherBonus += 0.10;
            }
        }
        return otherBonus;
    }

    private static double getWeaponBonus(final Player player, final boolean includeNpcMax, final Item weapon, final Mob target, double otherBonus) {
        if (target != null) {
            if (((weapon.getId() == TWISTED_BOW || weapon.getId() == TWISTED_BOW_I) && target.isNpc() && includeNpcMax)) {
                int magicLevel = 0;

                if (((Npc) target).combatInfo() != null && ((Npc) target).combatInfo().stats != null)
                    magicLevel = ((Npc) target).combatInfo().stats.magic;

                double damage = 250D + (((10 * 3 * magicLevel) / 10D) - 14) - ((Math.floor((3 * magicLevel / 10D) - 140)) * 2);
                damage /= 100;
                damage = Math.min(250D, damage);
                otherBonus *= Math.min(2D, 1D + damage);
            }

            if (player.getEquipment().hasAt(EquipSlot.WEAPON, SANGUINE_TWISTED_BOW) && target.isNpc() && includeNpcMax) {//s23update
                otherBonus += 0.80;
            }

            if (player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_CROSSBOW) && includeNpcMax) {
                if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY || FormulaUtils.isDragon(target)) {
                    otherBonus *= 1.25;
                } else {
                    otherBonus *= 1.30;
                }
            }

            if (FormulaUtils.hasRangedWildernessWeapon(player) && target.isNpc() && includeNpcMax && WildernessArea.inWilderness(player.tile())) {
                otherBonus *= 1.50;
            }

            if ((player.getEquipment().hasAt(EquipSlot.WEAPON, CRAWS_BOW_C) || player.getEquipment().hasAt(EquipSlot.WEAPON, WEBWEAVER_BOW)) && target.isNpc() && includeNpcMax) {
                if (player.getEquipment().hasAt(EquipSlot.WEAPON, WEBWEAVER_BOW)) {
                    otherBonus *= 1.70;
                } else {
                    otherBonus *= 1.65;
                }
            }
        }
        return otherBonus;
    }

    private static double getPetBonus(Player player, boolean includeNpcMax, Mob target, double otherBonus) {
        if (player.hasPetOut("Baby Aragog")) {
            var percentage = target.isNpc() && includeNpcMax ? 0.10 : 0.05;
            otherBonus += percentage;
        }

        if (player.hasPetOut("Baby Aragog")) {
            var percentage = target.isNpc() && includeNpcMax ? 0.10 : 0.05;
            otherBonus += percentage;
        }

        if (player.hasPetOut("Skeleton hellhound pet")) {
            otherBonus *= 1.05;
        }

        if (player.hasPetOut("Olmlet") && target.isNpc()) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY && includeNpcMax) {
                otherBonus *= 1.10;
            }
            if (player.getRaids() != null && player.getRaids().raiding(player))
                otherBonus *= 1.10;
        }

        if (player.hasPetOut("Lil' Zik") ||
            player.hasPetOut("Lil' Maiden") ||
            player.hasPetOut("Lil' Bloat") ||
            player.hasPetOut("Lil' Nylo") ||
            player.hasPetOut("Lil' Sot") ||
            player.hasPetOut("Lil' Xarp") && target.isNpc()) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY && includeNpcMax) {
                otherBonus *= 1.10;
            }
            if (player.getRaids() != null && player.getRaids().raiding(player))
                otherBonus *= 1.10;
        }
        return otherBonus;
    }

    public static int maxHit(Player player, Mob target, boolean factorInAmmoRangeStr, boolean includeNpcMax) {
        int maxHit;
        maxHit = !factorInAmmoRangeStr ? (int) Math.floor(getBaseDamage(player, false)) : (int) Math.floor(getBaseDamage(player, true));

        double specialMultiplier = player.getCombatSpecial() == null ? 0 : player.getCombatSpecial().getSpecialMultiplier();

        if (player.isSpecialActivated()) {
            maxHit = (int) (specialMultiplier * maxHit);
        }

        if (player.hasPetOut("Little Nightmare") || player.hasPetOut("Youngllef pet") || player.hasPetOut("Corrupted Youngllef pet")) {
            maxHit += 1;
        }

        if (player.hasPetOut(366)) {
            maxHit += 2;
        }

        if (player.getEquipment().hasAt(EquipSlot.AMULET, NECKLACE_OF_ANGUISH_OR)) {
            maxHit += 1;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, MAGMA_BLOWPIPE)) {
            maxHit += 3;
        }

        if (player.getEquipment().contains(TWISTED_BOW_I) && target instanceof Npc) {
            maxHit += 6;
        }

        return maxHit;
    }
}
