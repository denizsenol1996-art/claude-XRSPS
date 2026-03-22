package com.twisted.game.world.entity.combat.formula;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.container.ItemContainer;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;

import java.util.Arrays;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.BOW_OF_FAERDHINEN;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.NpcIdentifiers.*;

/**
 * This is a utility class for the combat max hits.
 *
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since November 06, 2021
 */
public class FormulaUtils {

    /**
     * Checks if the Npc is a demon
     *
     * @param target The npc
     * @return true if the npc is in fact a demon, false otherwise.
     */
    public static boolean isDemon(Mob target) {
        if (target.isNpc()) {
            Npc npc = target.getAsNpc();
            NpcDefinition def = npc.def();
            String name = "";
            if (def != null) {
                name = def.name;
            }
            return name.equalsIgnoreCase("Imp") || name.equalsIgnoreCase("Imp Champion") || name.equalsIgnoreCase("Lesser demon") || name.equalsIgnoreCase("Lesser Demon Champion") || name.equalsIgnoreCase("Greater demon") || name.equalsIgnoreCase("Black demon") || name.equalsIgnoreCase("Abyssal demon") || name.equalsIgnoreCase("Greater abyssal demon") || name.equalsIgnoreCase("Ice demon") || name.equalsIgnoreCase("Bloodveld") || name.equalsIgnoreCase("Insatiable Bloodveld") || name.equalsIgnoreCase("Mutated Bloodveld") || name.equalsIgnoreCase("Insatiable Mutated Bloodveld") || name.equalsIgnoreCase("Demonic gorilla") || name.equalsIgnoreCase("hellhound") || name.equalsIgnoreCase("Skeleton Hellhound") || name.equalsIgnoreCase("Greater Skeleton Hellhound") || name.equalsIgnoreCase("Nechryael") || name.equalsIgnoreCase("Death spawn") || name.equalsIgnoreCase("Greater Nechryael") || name.equalsIgnoreCase("Nechryarch") || name.equalsIgnoreCase("Chaotic death spawn");
        }
        return false;
    }

    public static int[] isRevenant() {
        return new int[]{
            REVENANT_HELLHOUND, REVENANT_IMP,
            REVENANT_KNIGHT, REVENANT_HOBGOBLIN,
            REVENANT_GOBLIN, REVENANT_IMP,
            REVENANT_DRAGON, REVENANT_DARK_BEAST,
            REVENANT_DEMON, REVENANT_ORK,
            REVENANT_PYREFIEND, REVENANT_CYCLOPS
        };
    }

    public static boolean isUndead(Mob target) {
        if (target.isNpc()) {
            Npc npc = target.getAsNpc();
            NpcDefinition def = npc.def();
            String name = "";
            if (def != null) {
                name = def.name;
            }
            return name.equalsIgnoreCase("Aberrant spectre") || name.equalsIgnoreCase("Ankou") || name.equalsIgnoreCase("Banshee") || name.equalsIgnoreCase("Crawling Hand") || name.equalsIgnoreCase("Ghast") || name.equalsIgnoreCase("Ghost") || name.equalsIgnoreCase("Mummy") || name.contains("revenant") || name.equalsIgnoreCase("Shade") || name.equalsIgnoreCase("Skeleton") || name.equalsIgnoreCase("Skogre") || name.equalsIgnoreCase("Summoned Zombie") || name.equalsIgnoreCase("Tortured soul") || name.equalsIgnoreCase("Undead chicken") || name.equalsIgnoreCase("Undead cow") || name.equalsIgnoreCase("Undead one") || name.equalsIgnoreCase("Zogre") || name.equalsIgnoreCase("Zombified Spawn") || name.equalsIgnoreCase("Zombie") || name.equalsIgnoreCase("Zombie rat") || name.equalsIgnoreCase("Pestilent Bloat") || name.equalsIgnoreCase("Vet'ion") || name.equalsIgnoreCase("Ahrim the Blighted") || name.equalsIgnoreCase("Dharok the Wretched") || name.equalsIgnoreCase("Guthan the Infested") || name.equalsIgnoreCase("Karil the Tainted") || name.equalsIgnoreCase("Torag the Corrupted") || name.equalsIgnoreCase("Verac the Defiled") || name.equalsIgnoreCase("Pestilent Bloat") || name.equalsIgnoreCase("Mi-Gor") || name.equalsIgnoreCase("Treus Dayth") || name.equalsIgnoreCase("Nazastarool") || name.equalsIgnoreCase("Slash Bash") || name.equalsIgnoreCase("Ulfric") || name.equalsIgnoreCase("Vorkath");
        }
        return false;
    }

    /**
     * Checks if the Npc is a dragon.
     *
     * @param target The mob
     * @return returns true if the npc is a dragon, false otherwise.
     */
    public static boolean isDragon(Mob target) {
        if (target.isNpc()) {
            Npc npc = target.getAsNpc();
            NpcDefinition def = npc.def();
            String name = "";
            if (def != null) {
                name = def.name;
            }
            boolean exceptions = name.contains("Elvarg") || name.contains("Revenant dragon");
            return name.contains("Hungarian horntail") || name.contains("Wyvern") || name.contains("Basilisk (Right claw)") || name.contains("Basilisk (Left claw)") || name.contains("Basilisk") || name.contains("Great Olm") || name.contains("Wyrm") || name.contains("Drake") || name.contains("Hydra") || name.contains("Vorkath") || name.contains("Galvek") || name.contains("dragon") || name.contains("Dragon") && !exceptions;
        }
        return false;
    }

    public static boolean obbyArmour(Player player) {
        ItemContainer eq = player.getEquipment();
        return ((eq.hasAt(EquipSlot.HEAD, 21298) && eq.hasAt(EquipSlot.BODY, 21301) && eq.hasAt(EquipSlot.LEGS, 21304)));
    }

    public static boolean hasViggorasChainMace(Player player) {
        return ((player.getEquipment().hasAt(EquipSlot.WEAPON, VIGGORAS_CHAINMACE) || player.getEquipment().hasAt(EquipSlot.WEAPON, BEGINNER_CHAINMACE) || player.getEquipment().hasAt(EquipSlot.WEAPON, HWEEN_CHAINMACE)) && WildernessArea.inWilderness(player.tile()));
    }

    public static boolean hasThammaronSceptre(Player player) {
        ItemContainer eq = player.getEquipment();
        return (eq.hasAt(EquipSlot.WEAPON, 22555) && (WildernessArea.inWilderness(player.tile())));
    }

    public static boolean hasBowOfFaerdhenin(Player player) {
        return player.getEquipment().containsAny(BOW_OF_FAERDHINEN, BOW_OF_FAERDHINEN_27187, BOW_OF_FAERDHINEN_C_25869, BOW_OF_FAERDHINEN_C_25884, BOW_OF_FAERDHINEN_C_25886, BOW_OF_FAERDHINEN_C_25888, BOW_OF_FAERDHINEN_C_25890, BOW_OF_FAERDHINEN_C_25892, BOW_OF_FAERDHINEN_C_25892, BOW_OF_FAERDHINEN_C_25896, BOW_OF_FAERDHINEN_C_25896, CustomItemIdentifiers.BOW_OF_FAERDHINEN, CustomItemIdentifiers.BOW_OF_FAERDHINEN_C);
    }

    public static boolean eliteVoidEquipmentBaseMagic(Player player) {
        return player.getEquipment().containsAll(ELITE_VOID_TOP, ELITE_VOID_ROBE, VOID_KNIGHT_GLOVES, VOID_MAGE_HELM);
    }

    public static boolean regularVoidEquipmentBaseRanged(Player player) {
        return player.getEquipment().containsAll(VOID_KNIGHT_GLOVES, VOID_KNIGHT_ROBE, VOID_KNIGHT_TOP, VOID_RANGER_HELM);
    }

    public static boolean eliteTrimmedVoidEquipmentBaseRanged(Player player) {
        return player.getEquipment().containsAll(ELITE_VOID_TOP_LOR, ELITE_VOID_ROBE_LOR, VOID_KNIGHT_GLOVES_LOR, VOID_RANGER_HELM_LOR);
    }

    public static boolean eliteVoidEquipmentMelee(Player player) {
        return player.getEquipment().containsAll(ELITE_VOID_TOP, ELITE_VOID_ROBE, VOID_KNIGHT_GLOVES, VOID_MELEE_HELM);
    }

    public static boolean eliteVoidEquipmentRanged(Player player) {
        return player.getEquipment().containsAll(ELITE_VOID_TOP, ELITE_VOID_ROBE, VOID_KNIGHT_GLOVES, VOID_RANGER_HELM);
    }

    public static boolean hasCrawsBow(Player player) {
        return ((player.getEquipment().hasAt(EquipSlot.WEAPON, CRAWS_BOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, BEGINNER_CRAWS_BOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, WEBWEAVER_BOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, HWEEN_CRAWS_BOW)) || player.getEquipment().hasAt(EquipSlot.WEAPON, CRAWS_BOW_C) && WildernessArea.inWilderness(player.tile()));
    }

    public static boolean hasAmuletOfAvarice(Player player) {
        ItemContainer eq = player.getEquipment();
        return (eq.hasAt(EquipSlot.WEAPON, 22557) && WildernessArea.inWilderness(player.tile()));
    }

    public static boolean berserkerNecklace(Player player) {
        return player.getEquipment().hasAt(EquipSlot.AMULET, BERSERKER_NECKLACE) || player.getEquipment().hasAt(EquipSlot.AMULET, BERSERKER_NECKLACE_OR);
    }

    public static boolean hasObbyWeapon(Player player) {
        ItemContainer eq = player.getEquipment();
        int[] weaponry = new int[]{6528, 6523, 6525};
        return ((eq.hasAt(EquipSlot.WEAPON, weaponry[0]) || (eq.hasAt(EquipSlot.WEAPON, weaponry[1]) || (eq.hasAt(EquipSlot.WEAPON, weaponry[2])))));
    }

    public static boolean voidBase(Player player) {
        return ((player.getEquipment().hasAt(EquipSlot.BODY, 8839) && player.getEquipment().hasAt(EquipSlot.LEGS, 8840)) || (player.getEquipment().hasAt(EquipSlot.BODY, 13072) && player.getEquipment().hasAt(EquipSlot.LEGS, 13073))) && player.getEquipment().hasAt(EquipSlot.HANDS, 8842);
    }

    public static boolean eliteTrimmedVoidEquipmentBaseMelee(Player player) {
        return player.getEquipment().containsAll(ELITE_VOID_TOP_LOR, ELITE_VOID_ROBE_LOR, VOID_KNIGHT_GLOVES_LOR, VOID_MELEE_HELM_LOR);
    }

    public static boolean hasSlayerHelmet(Player player) {
        return player.getEquipment().containsAny(SLAYER_HELMET, TWISTED_SLAYER_HELMET, GREEN_SLAYER_HELMET, RED_SLAYER_HELMET, BLACK_SLAYER_HELMET, PURPLE_SLAYER_HELMET, TURQUOISE_SLAYER_HELMET, HYDRA_SLAYER_HELMET, VAMPYRIC_SLAYER_HELMET, TZKAL_SLAYER_HELMET);
    }

    public static boolean hasSlayerHelmetImbued(Player player) {
        return player.getEquipment().containsAny(SLAYER_HELMET_I, ItemIdentifiers.TWISTED_SLAYER_HELMET_I, GREEN_SLAYER_HELMET_I, RED_SLAYER_HELMET_I, BLACK_SLAYER_HELMET_I, PURPLE_SLAYER_HELMET_I, TURQUOISE_SLAYER_HELMET_I, HYDRA_SLAYER_HELMET_I, VAMPYRIC_SLAYER_HELMET_I, TZKAL_SLAYER_HELMET_I);
    }

    public static boolean wearingFullDharok(Player player) {
        return player.getEquipment().containsAll(DHAROKS_HELM, DHAROKS_GREATAXE, DHAROKS_PLATEBODY, DHAROKS_PLATELEGS);
    }

    public static boolean hasMeleeWildernessWeapon(Player player) {
        return player.getEquipment().containsAny(VIGGORAS_CHAINMACE, VIGGORAS_CHAINMACE_C, URSINE_CHAINMACE);
    }

    public static boolean hasRangedWildernessWeapon(Player player) {
        return player.getEquipment().containsAny(CRAWS_BOW);
    }

    public static boolean hasSalveAmulet(Player player) {
        return player.getEquipment().contains(SALVE_AMULET);
    }

    public static boolean eliteTrimmedVoidEquipmentBaseMagic(Player player) {
        return player.getEquipment().containsAll(ELITE_VOID_TOP_LOR, ELITE_VOID_ROBE_LOR, VOID_KNIGHT_GLOVES_LOR, VOID_MAGE_HELM_LOR);
    }

    public static boolean hasSalveAmuletI(Player player) {
        return player.getEquipment().containsAny(SALVE_AMULETI, SALVE_AMULETI_25250, SALVE_AMULETI_26763);
    }

    public static boolean hasSalveAmuletE(Player player) {
        return player.getEquipment().contains(SALVE_AMULET_E);
    }

    public static boolean hasSalveAmuletEI(Player player) {
        return player.getEquipment().containsAny(SALVE_AMULETEI, SALVE_AMULETEI_25278, SALVE_AMULETEI_26782);
    }

    public static boolean hasMagicWildernessWeapon(Player player) {
        return player.getEquipment().containsAny(THAMMARONS_SCEPTRE, THAMMARONS_STAFF_C, ACCURSED_SCEPTRE_A);
    }

    public static boolean voidCustomBase(Player player) {
        return (player.getEquipment().hasAt(EquipSlot.BODY, ELITE_VOID_TOP_24943) && player.getEquipment().hasAt(EquipSlot.LEGS, ELITE_VOID_ROBE_24942) && player.getEquipment().hasAt(EquipSlot.HANDS, VOID_KNIGHT_GLOVES_24938));
    }

    public static boolean voidCustomRanger(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, VOID_RANGER_HELM_24939) && voidCustomBase(player);
    }

    public static boolean voidCustomMelee(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, VOID_MELEE_HELM_24941) && voidCustomBase(player);
    }

    public static boolean voidCustomMagic(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, VOID_MAGE_HELM_24940) && voidCustomBase(player);
    }

    public static boolean voidRanger(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, 11664) && voidBase(player);
    }

    public static boolean voidMelee(Player player) {
        return player.getEquipment().hasAt(EquipSlot.HEAD, 11665) && voidBase(player);
    }

    public static boolean voidMagic(Player p) {//updatevoidor
        return (p.getEquipment().contains(11663)) && p.getEquipment().hasAt(EquipSlot.BODY, VOID_KNIGHT_TOP) && p.getEquipment().hasAt(EquipSlot.LEGS, VOID_KNIGHT_ROBE) && p.getEquipment().hasAt(EquipSlot.HANDS, 8842);
    }

    public static boolean wearingEliteVoid(Player p) {
        return (p.getEquipment().contains(11665) || p.getEquipment().contains(11664) || p.getEquipment().contains(11663)) && p.getEquipment().hasAt(EquipSlot.BODY, 13072) && p.getEquipment().hasAt(EquipSlot.LEGS, 13073) && p.getEquipment().hasAt(EquipSlot.HANDS, 8842);
    }

    public static boolean wearingEliteVoidOr(Player p) {//updatevoidor
        return (p.getEquipment().contains(26473) || p.getEquipment().contains(26475) || p.getEquipment().contains(26477)) && p.getEquipment().hasAt(EquipSlot.BODY, 26469) && p.getEquipment().hasAt(EquipSlot.LEGS, 26471) && p.getEquipment().hasAt(EquipSlot.HANDS, 26467);
    }

    private static final int[] BLACK_MASK = new int[]{BLACK_MASK_1, BLACK_MASK_2, BLACK_MASK_3, BLACK_MASK_4, BLACK_MASK_5, BLACK_MASK_6, BLACK_MASK_7, BLACK_MASK_8, BLACK_MASK_9, BLACK_MASK_10};
    private static final int[] BLACK_MASK_IMBUED = new int[]{BLACK_MASK_1_I, BLACK_MASK_2_I, BLACK_MASK_3_I, BLACK_MASK_4_I, BLACK_MASK_5_I, BLACK_MASK_6_I, BLACK_MASK_7_I, BLACK_MASK_8_I, BLACK_MASK_9_I, BLACK_MASK_10_I};

    public static boolean wearingBlackMask(Player player) {
        return Arrays.stream(BLACK_MASK).anyMatch(mask -> player.getEquipment().hasAt(EquipSlot.HEAD, mask));
    }

    public static boolean wearingBlackMaskImbued(Player player) {
        return Arrays.stream(BLACK_MASK_IMBUED).anyMatch(mask -> player.getEquipment().hasAt(EquipSlot.HEAD, mask));
    }

    private static final int[] TWISTED_SLAYER_HELMET_I = new int[]{ItemIdentifiers.TWISTED_SLAYER_HELMET_I, TWISTED_SLAYER_HELMET_I_KBD_HEADS, TWISTED_SLAYER_HELMET_I_INFERNAL_CAPE, TWISTED_SLAYER_HELMET_I_VAMP_DUST, TWISTED_SLAYER_HELMET_I_JAD};

    public static boolean wearingTwistedSlayerHelmetI(Player player) {
        return Arrays.stream(TWISTED_SLAYER_HELMET_I).anyMatch(mask -> player.getEquipment().hasAt(EquipSlot.HEAD, mask));
    }

}
