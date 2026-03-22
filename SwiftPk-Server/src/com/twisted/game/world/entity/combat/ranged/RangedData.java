package com.twisted.game.world.entity.combat.ranged;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.weapon.FightType;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.entity.mob.player.rights.MemberRights;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.equipment.Equipment;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * A table of constants that hold data for all ranged ammo.
 * <p>
 * Edit: This is now purely only data.
 * Updated it and moved all methods to CombatFactory.
 *
 * @author Swiffy96
 * @author Professor Oak
 */
public class RangedData {

    private static int boltSpecialChance(MemberRights memberRights, boolean always_spec) {
        int percentage = 10;
        switch (memberRights) {
            case RUBY_MEMBER -> percentage = 1;
            case SAPPHIRE_MEMBER -> percentage = 2;
            case EMERALD_MEMBER -> percentage = 3;
            case DIAMOND_MEMBER -> percentage = 4;
            case DRAGONSTONE_MEMBER -> percentage = 5;
            case ONYX_MEMBER -> percentage = 6;
            case ZENYTE_MEMBER -> percentage = 7;
        }
        return always_spec ? 100 : percentage;
    }

    /**
     * A map of items and their respective interfaces.
     */
    private static final Map<Integer, RangedWeapon> rangedWeapons = new HashMap<>();

    public static int getBoltSpecialAttack(Player p, Mob target, int damage) {

        double boltSpecialMultiplier;

        boolean always_fire_special = false;

        if(target instanceof Npc npc) {
            if (npc.isCombatDummy()) {
                always_fire_special = true;
            }
        }

        //System.out.println("Using bolt spec "+always_fire_special);

        Item ammo = p.getEquipment().get(EquipSlot.AMMO);
        if(ammo != null) {
            switch (ammo.getId()) {
                case OPAL_BOLTS_E, OPAL_DRAGON_BOLTS_E -> {
                    boolean lucky_lightning = Utils.percentageChance(boltSpecialChance(p.getMemberRights(), always_fire_special));
                    if (lucky_lightning) {
                        int current_range_level = p.skills().level(Skills.RANGED);
                        target.performGraphic(new Graphic(749));
                        boltSpecialMultiplier = (current_range_level * 0.10); // Can max deal 25% extra damage.
                        damage += boltSpecialMultiplier;
                    }
                }
                case JADE_BOLTS_E, JADE_DRAGON_BOLTS_E -> {
                    boolean earths_fury = Utils.percentageChance(boltSpecialChance(p.getMemberRights(), always_fire_special));
                    if (earths_fury) {
                        boltSpecialMultiplier = 1.18; // Deals 18% extra damage.
                        damage *= boltSpecialMultiplier;
                        target.performGraphic(new Graphic(756));
                        if(target.isNpc()) {
                            Npc npc = (Npc) target;
                            if (!npc.isCombatDummy())
                                target.stun(10);
                        }
                    }
                }
                case PEARL_BOLTS_E, PEARL_DRAGON_BOLTS_E -> {
                    target.performGraphic(new Graphic(750));
                    boltSpecialMultiplier = 1.1;
                    damage *= boltSpecialMultiplier;
                }
                case TOPAZ_BOLTS_E, TOPAZ_DRAGON_BOLTS_E -> {
                    boolean down_to_earth = Utils.percentageChance(boltSpecialChance(p.getMemberRights(), always_fire_special));
                    if (down_to_earth && target.isPlayer()) {
                        Player t = target.getAsPlayer();
                        t.performGraphic(new Graphic(757));
                        t.skills().alterSkill(Skills.MAGIC, t.skills().level(Skills.MAGIC) - 1);
                        t.getPacketSender().sendMessage("Your Magic level has been reduced.");
                    }
                }
                case SAPPHIRE_BOLTS_E, SAPPHIRE_DRAGON_BOLTS_E -> {
                    boolean clear_mind = Utils.percentageChance(boltSpecialChance(p.getMemberRights(), always_fire_special));
                    if (clear_mind && target.isPlayer()) {
                        Player t = target.getAsPlayer();
                        t.performGraphic(new Graphic(751));
                        t.skills().alterSkill(Skills.PRAYER, -20);
                        t.getPacketSender().sendMessage("Your Prayer level has been leeched.");

                        p.skills().alterSkill(Skills.PRAYER, +20);
                        p.getPacketSender().sendMessage("Your enchanted bolts leech some Prayer points from your opponent..");
                    }
                }
                case EMERALD_BOLTS_E, EMERALD_DRAGON_BOLTS_E -> {
                    boolean magical_poison = Utils.percentageChance(boltSpecialChance(p.getMemberRights(), always_fire_special));
                    if (magical_poison) {
                        target.performGraphic(new Graphic(752));
                        target.poison(5);
                    }
                }
                case RUBY_BOLTS_E, RUBY_DRAGON_BOLTS_E -> {
                    boolean blood_forfeit = Utils.percentageChance(boltSpecialChance(p.getMemberRights(), always_fire_special));
                    if (blood_forfeit) {
                        int cap = 100;

                        target.performGraphic(new Graphic(754));

                        int selfDamage = (int) (p.skills().level(Skills.HITPOINTS) * 0.1);
                        if (selfDamage < p.skills().level(Skills.HITPOINTS)) {
                            int targetHP = target.hp();
                            damage += targetHP * 0.2;
                            if (damage > cap)
                                damage = cap;

                            p.hit(p, selfDamage, 0, null).setIsReflected().submit();
                        }
                    }
                }
                case DIAMOND_BOLTS_E, DIAMOND_DRAGON_BOLTS_E -> {
                    boolean armour_piercing = Utils.percentageChance(boltSpecialChance(p.getMemberRights(), always_fire_special));
                    if (armour_piercing) {
                        p.putAttrib(AttributeKey.ARMOUR_PIERCING, true);
                        target.performGraphic(new Graphic(758, 100));
                        boltSpecialMultiplier = 1.15; // Deals 15% extra damage.
                        damage *= boltSpecialMultiplier;
                    }
                }
                case DRAGONSTONE_BOLTS_E, DRAGONSTONE_DRAGON_BOLTS_E -> {
                    boolean dragons_breath = Utils.percentageChance(boltSpecialChance(p.getMemberRights(), always_fire_special));
                    boolean can_perform_dragons_breath = true;
                    if (dragons_breath) {
                        if (target.isPlayer()) {
                            Player t = target.getAsPlayer();
                            boolean potionEffect = (int) t.getAttribOr(AttributeKey.ANTIFIRE_POTION, 0) > 0;
                            can_perform_dragons_breath = !(potionEffect || Equipment.hasDragonProtectionGear(t));
                        }

                        if (can_perform_dragons_breath) {
                            target.performGraphic(new Graphic(756));
                            int current_range_level = p.skills().level(Skills.RANGED);
                            boltSpecialMultiplier = (current_range_level * 0.20); // 20 % extra damage
                            damage += boltSpecialMultiplier;
                        }
                    }
                }
                case ONYX_BOLTS_E, ONYX_DRAGON_BOLTS_E -> {
                    boolean life_leech = Utils.percentageChance(boltSpecialChance(p.getMemberRights(), always_fire_special));
                    if (life_leech) {
                        target.performGraphic(new Graphic(753));
                        boltSpecialMultiplier = 1.20; //20% extra damage
                        damage *= boltSpecialMultiplier;
                        int heal = (int) (damage * 0.25);

                        // Only heal the player if the person hasn't already got full hp.
                        if (p.hp() < 99) {
                            p.setHitpoints(p.hp() + heal);
                        }
                    }
                }
            }
        }

        //System.out.println("bolt spec dmg "+damage);
        return damage;
    }

    public enum RangedWeapon {

        LONGBOW(new int[]{ItemIdentifiers.LONGBOW}, RangedWeaponType.LONGBOW, true),
        SHORTBOW(new int[]{ItemIdentifiers.SHORTBOW}, RangedWeaponType.SHORTBOW, true),
        OAK_LONGBOW(new int[]{ItemIdentifiers.OAK_LONGBOW}, RangedWeaponType.LONGBOW, true),
        OAK_SHORTBOW(new int[]{ItemIdentifiers.OAK_SHORTBOW}, RangedWeaponType.SHORTBOW, true),
        WILLOW_LONGBOW(new int[]{ItemIdentifiers.WILLOW_LONGBOW}, RangedWeaponType.LONGBOW, true),
        WILLOW_SHORTBOW(new int[]{ItemIdentifiers.WILLOW_SHORTBOW}, RangedWeaponType.SHORTBOW, true),
        MAPLE_LONGBOW(new int[]{ItemIdentifiers.MAPLE_LONGBOW}, RangedWeaponType.LONGBOW, true),
        MAPLE_SHORTBOW(new int[]{ItemIdentifiers.MAPLE_SHORTBOW}, RangedWeaponType.SHORTBOW, true),
        YEW_LONGBOW(new int[]{ItemIdentifiers.YEW_LONGBOW}, RangedWeaponType.LONGBOW, true),
        YEW_SHORTBOW(new int[]{ItemIdentifiers.YEW_SHORTBOW}, RangedWeaponType.SHORTBOW, true),
        MAGIC_LONGBOW(new int[]{ItemIdentifiers.MAGIC_LONGBOW}, RangedWeaponType.LONGBOW, true),
        MAGIC_SHORTBOW(new int[]{ItemIdentifiers.MAGIC_SHORTBOW, ItemIdentifiers.MAGIC_SHORTBOW_I}, RangedWeaponType.SHORTBOW, true),
        _3RD_AGE_BOW(new int[] {ItemIdentifiers._3RD_AGE_BOW}, RangedWeaponType.SHORTBOW, true),
        DARK_BOW(new int[]{ItemIdentifiers.DARK_BOW}, RangedWeaponType.LONGBOW, true),
        TWISTED_BOW(new int[]{ItemIdentifiers.TWISTED_BOW, TWISTED_BOW_ORANGE, TWISTED_BOW_PURPLE, CustomItemIdentifiers.TWISTED_BOW_I, CustomItemIdentifiers.SANGUINE_TWISTED_BOW}, RangedWeaponType.TWISTED_BOW, true),
        ELEMENTAL_BOW(new int[] {CustomItemIdentifiers.ELEMENTAL_BOW}, RangedWeaponType.SHORTBOW, true),
        DONATION_BOW(new int[] {30095}, RangedWeaponType.SHORTBOW, true),
        DARKLORD_BOW(new int[] {CustomItemIdentifiers.DARKLORD_BOW}, RangedWeaponType.SHORTBOW, true),

        BRONZE_CROSSBOW(new int[]{ItemIdentifiers.BRONZE_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),
        IRON_CROSSBOW(new int[]{ItemIdentifiers.IRON_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),
        STEEL_CROSSBOW(new int[]{ItemIdentifiers.STEEL_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),
        MITHRIL_CROSSBOW(new int[]{ItemIdentifiers.MITHRIL_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),
        ADAMANT_CROSSBOW(new int[]{ItemIdentifiers.ADAMANT_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),
        RUNE_CROSSBOW(new int[]{ItemIdentifiers.RUNE_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),
        ARMADYL_CROSSBOW(new int[]{ItemIdentifiers.ARMADYL_CROSSBOW}, RangedWeaponType.ARMADYL_CROSSBOW, true),
        DRAGON_CROSSBOW(new int[] {ItemIdentifiers.DRAGON_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),
        DRAGON_HUNTER_CROSSBOW(new int[] {ItemIdentifiers.DRAGON_HUNTER_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),
        DRAGON_HUNTER_CROSSBOW_T(new int[] {CustomItemIdentifiers.DRAGON_HUNTER_CROSSBOW_T}, RangedWeaponType.CROSSBOWS, true),
        ZARYTE_CBOW(new int[]{26374}, RangedWeaponType.ZARYTE_CBOW, true),

        HUNTERS_CROSSBOW(new int[] {ItemIdentifiers.HUNTERS_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),
        KARILS_CROSSBOW(new int[]{ItemIdentifiers.KARILS_CROSSBOW}, RangedWeaponType.KARILS_CROSSBOW, true),
        TALONHAWK_CROSSBOW(new int[] {CustomItemIdentifiers.TALONHAWK_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),
        HAUNTED_CROSSBOW(new int[] {CustomItemIdentifiers.HAUNTED_CROSSBOW}, RangedWeaponType.CROSSBOWS, true),

        BRONZE_DART(new int[]{ItemIdentifiers.BRONZE_DART}, RangedWeaponType.DARTS, false),
        IRON_DART(new int[]{ItemIdentifiers.IRON_DART}, RangedWeaponType.DARTS, false),
        STEEL_DART(new int[]{ItemIdentifiers.STEEL_DART}, RangedWeaponType.DARTS, false),
        MITHRIL_DART(new int[]{ItemIdentifiers.MITHRIL_DART}, RangedWeaponType.DARTS, false),
        ADAMANT_DART(new int[]{ItemIdentifiers.ADAMANT_DART}, RangedWeaponType.DARTS, false),
        RUNE_DART(new int[]{ItemIdentifiers.RUNE_DART}, RangedWeaponType.DARTS, false),
        DRAGON_DART(new int[]{ItemIdentifiers.DRAGON_DART}, RangedWeaponType.DARTS, false),

        BRONZE_KNIFE(new int[]{ItemIdentifiers.BRONZE_KNIFE, BRONZE_KNIFEP, BRONZE_KNIFEP_5654}, RangedWeaponType.KNIVES, false),
        IRON_KNIFE(new int[]{ItemIdentifiers.IRON_KNIFE, IRON_KNIFEP, IRON_KNIFEP_5655}, RangedWeaponType.KNIVES, false),
        STEEL_KNIFE(new int[]{ItemIdentifiers.STEEL_KNIFE, STEEL_KNIFEP, STEEL_KNIFEP_5656}, RangedWeaponType.KNIVES, false),
        BLACK_KNIFE(new int[]{ItemIdentifiers.BLACK_KNIFE, MITHRIL_KNIFEP, MITHRIL_KNIFEP_5657}, RangedWeaponType.KNIVES, false),
        MITHRIL_KNIFE(new int[]{ItemIdentifiers.MITHRIL_KNIFE, BLACK_KNIFEP, BLACK_KNIFEP_5658}, RangedWeaponType.KNIVES, false),
        ADAMANT_KNIFE(new int[]{ItemIdentifiers.ADAMANT_KNIFE, ADAMANT_KNIFEP, ADAMANT_KNIFEP_5659}, RangedWeaponType.KNIVES, false),
        RUNE_KNIFE(new int[]{ItemIdentifiers.RUNE_KNIFE, RUNE_KNIFEP, RUNE_KNIFEP_5660, RUNE_KNIFEP_5667}, RangedWeaponType.KNIVES, false),
        DRAGON_KNIFE(new int[]{ItemIdentifiers.DRAGON_KNIFE, DRAGON_KNIFEP, DRAGON_KNIFEP_22808, DRAGON_KNIFEP_22810}, RangedWeaponType.KNIVES, false),

        VENATOR_BOW(new int[]{ItemIdentifiers.VENATOR_BOW}, RangedWeaponType.VENATOR_BOW, true),

        BRONZE_THROWNAXE(new int[]{ItemIdentifiers.BRONZE_THROWNAXE}, RangedWeaponType.THROWING_AXES, false),
        IRON_THROWNAXE(new int[]{ItemIdentifiers.IRON_THROWNAXE}, RangedWeaponType.KNIVES, false),
        STEEL_THROWNAXE(new int[]{ItemIdentifiers.STEEL_THROWNAXE}, RangedWeaponType.KNIVES, false),
        MITHRIL_THROWNAXE(new int[]{ItemIdentifiers.MITHRIL_THROWNAXE}, RangedWeaponType.KNIVES, false),
        ADAMANT_THROWNAXE(new int[]{ItemIdentifiers.ADAMANT_THROWNAXE}, RangedWeaponType.KNIVES, false),
        RUNE_THROWNAXE(new int[]{ItemIdentifiers.RUNE_THROWNAXE}, RangedWeaponType.KNIVES, false),
        DRAGON_THROWNAXE(new int[]{ItemIdentifiers.DRAGON_THROWNAXE}, RangedWeaponType.KNIVES, false),
        TOKTZ_XIL_UL(new int[]{TOKTZXILUL}, RangedWeaponType.THROWING_AXES, false),

        MORRIGANS_THROWING_AXE(new int[]{ItemIdentifiers.MORRIGANS_THROWING_AXE}, RangedWeaponType.THROWING_AXES, false),
        MORRIGANS_JAVALIN(new int[]{MORRIGANS_JAVELIN}, RangedWeaponType.THROWING_AXES, false),

        BALLISTA(new int[]{LIGHT_BALLISTA, HEAVY_BALLISTA}, RangedWeaponType.BALLISTA, true),

        TOXIC_BLOWPIPE(new int[]{ItemIdentifiers.TOXIC_BLOWPIPE, MAGMA_BLOWPIPE, HWEEN_BLOWPIPE}, RangedWeaponType.TOXIC_BLOWPIPE, false),
        
        CRAWS_BOW(new int[]{ItemIdentifiers.CRAWS_BOW_U, ItemIdentifiers.CRAWS_BOW, CRAWS_BOW_C, BEGINNER_CRAWS_BOW, HWEEN_CRAWS_BOW}, RangedWeaponType.CRAWS_BOW, false),

        BOW_OF_FAERDHINEN(new int[]{BOW_OF_FAERDHINEN_27187, CustomItemIdentifiers.BOW_OF_FAERDHINEN, BOW_OF_FAERDHINEN_C_25869,BOW_OF_FAERDHINEN_C_25884,BOW_OF_FAERDHINEN_C_25886,BOW_OF_FAERDHINEN_C_25888, BOW_OF_FAERDHINEN_C_25890,BOW_OF_FAERDHINEN_C_25892,BOW_OF_FAERDHINEN_C_25892,BOW_OF_FAERDHINEN_C_25896,BOW_OF_FAERDHINEN_C_25896, CustomItemIdentifiers.BOW_OF_FAERDHINEN, CustomItemIdentifiers.BOW_OF_FAERDHINEN_C}, RangedWeaponType.BOW_OF_FAERDHINEN, false),

        WEBWEAVER_BOW(new int[]{ItemIdentifiers.WEBWEAVER_BOW}, RangedWeaponType.CRAWS_BOW, false),

        CRYSTAL_BOW(new int[]{ItemIdentifiers.NEW_CRYSTAL_BOW, ItemIdentifiers.NEW_CRYSTAL_BOW_4213, ItemIdentifiers.CRYSTAL_BOW_FULL, ItemIdentifiers.CRYSTAL_BOW_910, ItemIdentifiers.CRYSTAL_BOW_810, ItemIdentifiers.CRYSTAL_BOW_710, ItemIdentifiers.CRYSTAL_BOW_610, ItemIdentifiers.CRYSTAL_BOW_510, ItemIdentifiers.CRYSTAL_BOW_410, ItemIdentifiers.CRYSTAL_BOW_310, ItemIdentifiers.CRYSTAL_BOW_210, ItemIdentifiers.CRYSTAL_BOW_110, ItemIdentifiers.NEW_CRYSTAL_BOW_I, ItemIdentifiers.CRYSTAL_BOW_FULL_I, ItemIdentifiers.CRYSTAL_BOW_910_I, ItemIdentifiers.CRYSTAL_BOW_810_I, ItemIdentifiers.CRYSTAL_BOW_710_I, ItemIdentifiers.CRYSTAL_BOW_610_I, ItemIdentifiers.CRYSTAL_BOW_510_I, ItemIdentifiers.CRYSTAL_BOW_410_I, ItemIdentifiers.CRYSTAL_BOW_310_I, ItemIdentifiers.CRYSTAL_BOW_210_I, ItemIdentifiers.CRYSTAL_BOW_110_I, ItemIdentifiers.NEW_CRYSTAL_BOW_16888, ItemIdentifiers.NEW_CRYSTAL_BOW_I_16889}, RangedWeaponType.CRYSTAL_BOW, false),

        CHINCHOMPA(new int[]{ItemIdentifiers.CHINCHOMPA_10033, RED_CHINCHOMPA_10034, ItemIdentifiers.BLACK_CHINCHOMPA}, RangedWeaponType.CHINCHOMPA, false),;

        static {
            for (RangedWeapon data : RangedWeapon.values()) {
                for (int i : data.getWeaponIds()) {
                    rangedWeapons.put(i, data);
                }
            }
        }

        private final int[] weaponIds;
        private final RangedWeaponType type;
        private final boolean factorInAmmoRangeStr;

        RangedWeapon(int[] weaponIds, RangedWeaponType type, boolean factorInAmmoRangeStr) {
            this.weaponIds = weaponIds;
            this.type = type;
            this.factorInAmmoRangeStr = factorInAmmoRangeStr;
        }

        public static RangedWeapon getFor(Player player) {
            Item weapon = player.getEquipment().get(EquipSlot.WEAPON);
            if(weapon == null) {
                player.message("This weapon isn't recognized as ranged weapon. Maybe I should report this.");
                return null;
            }
            //System.out.printf("%s item id set %s%n", player.getUsername(), weapon);
            return rangedWeapons.get(weapon.getId());
        }

        public int[] getWeaponIds() {
            return weaponIds;
        }

        public RangedWeaponType getType() {
            return type;
        }

        public boolean ignoreArrowsSlot() {
            return factorInAmmoRangeStr;
        }
    }

    public enum RangedWeaponType {
        DARTS(3, 5, FightType.THROWING_LONGRANGE, false),
        KNIVES(4, 6, FightType.THROWING_LONGRANGE, false),
        THROWING_AXES(4, 6, FightType.THROWING_LONGRANGE, false),
        TOXIC_BLOWPIPE(5, 7, FightType.THROWING_LONGRANGE, false),
        DORGESHUUN_CBOW(6, 8, FightType.BOLT_LONGRANGE, true),
        CROSSBOWS(7, 9, FightType.BOLT_LONGRANGE, true),
        SHORTBOW(7, 9, FightType.ARROW_LONGRANGE, true),
        ARMADYL_CROSSBOW(8, 10, FightType.BOLT_LONGRANGE, true),
        KARILS_CROSSBOW(8, 10, FightType.BOLT_LONGRANGE, true),
        SEERCULL_BOW(8,10, FightType.ARROW_LONGRANGE, false),
        BALLISTA(9, 10, FightType.THROWING_LONGRANGE, true),
        CHINCHOMPA(9, 10, FightType.THROWING_LONGRANGE, false),
        _3_AGE_BOW(9, 10, FightType.ARROW_LONGRANGE, false),
        ZARYTE_CBOW(8, 12, FightType.ARROW_LONGRANGE, false),

        CRAWS_BOW(9, 10, FightType.ARROW_LONGRANGE, false),
        BOW_OF_FAERDHINEN(9, 10, FightType.ARROW_LONGRANGE, false),
        LONGBOW(10, 10, FightType.ARROW_LONGRANGE, true),
        CRYSTAL_BOW(10, 10, FightType.ARROW_LONGRANGE, false),
        DARK_BOW(10, 10, FightType.ARROW_LONGRANGE, true),
        TWISTED_BOW(10, 10, FightType.ARROW_LONGRANGE, true),
        VENATOR_BOW(6, 6, FightType.ARROW_RAPID, true),

        //Custom not in OSRS
        ZARYTE_BOW(10, 10, FightType.ARROW_LONGRANGE, false),
        GRANDMASTER_BOW(9, 10, FightType.ARROW_LONGRANGE, false),
        HAND_CANNON(9, 9, FightType.ARROW_ACCURATE, true);

        private final FightType longRangeFightType;
        private final int defaultDistance;
        private final int longRangeDistance;
        private final boolean ammoRequired;

        RangedWeaponType(int defaultDistance, int longRangeDistance, FightType longRangeFightType, boolean ammoRequired) {
            this.defaultDistance = defaultDistance;
            this.longRangeDistance = longRangeDistance;
            this.longRangeFightType = longRangeFightType;
            this.ammoRequired = ammoRequired;
        }

        public int getDefaultDistance() {
            return defaultDistance;
        }
        
        public int getLongRangeDistance() {
            return longRangeDistance;
        }

        public FightType getLongRangeFightType() {
            return longRangeFightType;
        }

        public boolean isAmmoRequired() {
            return ammoRequired;
        }
    }

}
