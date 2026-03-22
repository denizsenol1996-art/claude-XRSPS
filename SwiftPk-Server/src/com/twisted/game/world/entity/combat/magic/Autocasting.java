package com.twisted.game.world.entity.combat.magic;

import com.twisted.game.content.interfaces.BonusesInterface;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.Combat;
import com.twisted.game.world.entity.combat.weapon.FightType;
import com.twisted.game.world.entity.combat.weapon.WeaponInterfaces;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

public class Autocasting {

    public static HashMap<Integer, CombatSpells> LEGACY_AUTOCAST_SPELLS = new HashMap<>();

    static {
        // Modern
        LEGACY_AUTOCAST_SPELLS.put(1830, CombatSpells.WIND_STRIKE);
        LEGACY_AUTOCAST_SPELLS.put(1831, CombatSpells.WATER_STRIKE);
        LEGACY_AUTOCAST_SPELLS.put(1832, CombatSpells.EARTH_STRIKE);
        LEGACY_AUTOCAST_SPELLS.put(1833, CombatSpells.FIRE_STRIKE);
        LEGACY_AUTOCAST_SPELLS.put(1834, CombatSpells.WIND_BOLT);
        LEGACY_AUTOCAST_SPELLS.put(1835, CombatSpells.WATER_BOLT);
        LEGACY_AUTOCAST_SPELLS.put(1836, CombatSpells.EARTH_BOLT);
        LEGACY_AUTOCAST_SPELLS.put(1837, CombatSpells.FIRE_BOLT);
        LEGACY_AUTOCAST_SPELLS.put(1838, CombatSpells.WIND_BLAST);
        LEGACY_AUTOCAST_SPELLS.put(1839, CombatSpells.WATER_BLAST);
        LEGACY_AUTOCAST_SPELLS.put(1840, CombatSpells.EARTH_BLAST);
        LEGACY_AUTOCAST_SPELLS.put(1841, CombatSpells.FIRE_BLAST);
        LEGACY_AUTOCAST_SPELLS.put(1842, CombatSpells.WIND_WAVE);
        LEGACY_AUTOCAST_SPELLS.put(1843, CombatSpells.WATER_WAVE);
        LEGACY_AUTOCAST_SPELLS.put(1844, CombatSpells.EARTH_WAVE);
        LEGACY_AUTOCAST_SPELLS.put(1845, CombatSpells.FIRE_WAVE);
        LEGACY_AUTOCAST_SPELLS.put(50050, CombatSpells.AIR_SURGE);
        LEGACY_AUTOCAST_SPELLS.put(50070, CombatSpells.WATER_SURGE);
        LEGACY_AUTOCAST_SPELLS.put(50090, CombatSpells.EARTH_SURGE);
        LEGACY_AUTOCAST_SPELLS.put(50110, CombatSpells.FIRE_SURGE);

        // Ancients
        LEGACY_AUTOCAST_SPELLS.put(13189, CombatSpells.SMOKE_RUSH);
        LEGACY_AUTOCAST_SPELLS.put(13241, CombatSpells.SHADOW_RUSH);
        LEGACY_AUTOCAST_SPELLS.put(13247, CombatSpells.BLOOD_RUSH);
        LEGACY_AUTOCAST_SPELLS.put(6162, CombatSpells.ICE_RUSH);
        LEGACY_AUTOCAST_SPELLS.put(13215, CombatSpells.SMOKE_BURST);
        LEGACY_AUTOCAST_SPELLS.put(13267, CombatSpells.SHADOW_BURST);
        LEGACY_AUTOCAST_SPELLS.put(13167, CombatSpells.BLOOD_BURST);
        LEGACY_AUTOCAST_SPELLS.put(13125, CombatSpells.ICE_BURST);
        LEGACY_AUTOCAST_SPELLS.put(13202, CombatSpells.SMOKE_BLITZ);
        LEGACY_AUTOCAST_SPELLS.put(13254, CombatSpells.SHADOW_BLITZ);
        LEGACY_AUTOCAST_SPELLS.put(13158, CombatSpells.BLOOD_BLITZ);
        LEGACY_AUTOCAST_SPELLS.put(13114, CombatSpells.ICE_BLITZ);
        LEGACY_AUTOCAST_SPELLS.put(13228, CombatSpells.SMOKE_BARRAGE);
        LEGACY_AUTOCAST_SPELLS.put(13280, CombatSpells.SHADOW_BARRAGE);
        LEGACY_AUTOCAST_SPELLS.put(13178, CombatSpells.BLOOD_BARRAGE);
        LEGACY_AUTOCAST_SPELLS.put(13136, CombatSpells.ICE_BARRAGE);
    }

    public static final Set<Integer> ANCIENT_SPELL_AUTOCAST_STAFFS = Set.of(KODAI_WAND, KODAI_WAND_23626, MASTER_WAND,
        ANCIENT_STAFF, NIGHTMARE_STAFF, VOLATILE_NIGHTMARE_STAFF, ELDRITCH_NIGHTMARE_STAFF, 30091, TOXIC_STAFF_OF_THE_DEAD, MENDING_WAND, TOXIC_STAFF_OF_THE_DEAD_C, ELDER_WAND, ELDER_WAND_RAIDS, STAFF_OF_THE_DEAD, STAFF_OF_LIGHT, THAMMARONS_STAFF_C, ANATHEMATIC_WAND);//bloodrevsupdate

    public static List<CombatSpell> POWERED_STAFF_SPELLS = List.of(
        CombatSpells.SANGUINESTI_STAFF.getSpell(),
        CombatSpells.DAWNBRINGER.getSpell(),
        CombatSpells.TUMEKENS_SPELL.getSpell(),
        CombatSpells.THAMMARON_SCEPTRE.getSpell(),
        CombatSpells.TRIDENT_OF_THE_SWAMP.getSpell(),
        CombatSpells.TRIDENT_OF_THE_SEAS.getSpell(),
        CombatSpells.VOLATILE_NIGHTMARE_STAFF.getSpell(),
        CombatSpells.ELDRITCH_NIGHTMARE_STAFF.getSpell(),
        CombatSpells.CRUCIATUS_CURSE.getSpell(),
        CombatSpells.PETRIFICUS_TOTALUS.getSpell(),
        CombatSpells.AVADA_KEDAVRA.getSpell(),
        CombatSpells.EXPELLIARMUS.getSpell(),
        CombatSpells.SECTUMSEMPRA.getSpell()
    );

    public static boolean toggleAutocast(final Player player, int actionButtonId) {
        final CombatSpell cbSpell = CombatSpells.getCombatSpell(actionButtonId);
        if (cbSpell == null) {
            return false;
        }

        CombatSpell spellBeingCast = player.getCombat().getCastSpell();

        if (cbSpell.levelRequired() > player.skills().level(Skills.MAGIC)) {
            player.message("You need a Magic level of at least %d to cast this spell.", cbSpell.levelRequired());
            setAutocast(player, null);
            return false;
        }

        if (spellBeingCast != null && spellBeingCast == cbSpell && Arrays.stream(Combat.ELDER_WAND_SPELLS).noneMatch(combatSpell -> combatSpell == cbSpell)) {
            // Player is already autocasting this spell. Turn it off.
            setAutocast(player, null);
            //System.out.println("Player is already auto casting this spell. Turn it off.");
        } else {
            // Set the new autocast spell
            //System.out.println("Set the new auto cast spell");
            setAutocast(player, cbSpell);
        }
        return true;
    }

    public static boolean handleLegacyAutocast(Player player, int button) {
        if (LEGACY_AUTOCAST_SPELLS.containsKey(button)) {
            setAutocast(player, LEGACY_AUTOCAST_SPELLS.get(button).getSpell());
            WeaponInterfaces.updateWeaponInterface(player);
            return true;
        }

        if (button == 2004 || button == 6161) {
            setAutocast(player, null);
            WeaponInterfaces.updateWeaponInterface(player);
            return true;
        }
        return false;
    }

    public static void setAutocast(Player player, CombatSpell spell) {
        if (spell == null) {
            player.putAttrib(AttributeKey.AUTOCAST_SELECTED, false);
            player.getPacketSender().sendAutocastId(-1).sendConfig(108, 0).setDefensiveAutocastState(0);
            if (player.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND) || player.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND_RAIDS)) {
                player.getCombat().setCastSpell(CombatSpells.CRUCIATUS_CURSE.getSpell());
            }
            //System.out.println("no spell");
            //Use this code for testing when we don't know where the autocast spell is being set to null from -Ken
            //throw new RuntimeException("no spell");
        } else {
            if (player.<Boolean>getAttribOr(AttributeKey.DEFENSIVE_AUTOCAST, false)) {
                player.getPacketSender().sendAutocastId(spell.spellId()).sendConfig(108, 0).setDefensiveAutocastState(1);
            } else {
                player.getPacketSender().sendAutocastId(spell.spellId()).sendConfig(108, 1).setDefensiveAutocastState(0);
            }
            //System.out.println("setting spell");
            player.putAttrib(AttributeKey.AUTOCAST_SELECTED, true);
        }
        player.getCombat().setAutoCastSpell(spell);
        BonusesInterface.sendBonuses(player);
        updateConfigsOnAutocast(player, spell != null);
    }

    private static final List<FightType> STAFF_FIGHT_TYPES = List.of(
        FightType.STAFF_BASH,
        FightType.STAFF_FOCUS,
        FightType.STAFF_POUND
    );

    private static void updateConfigsOnAutocast(final Player player, boolean autocast) {
        if (autocast) {
            for (final FightType type : STAFF_FIGHT_TYPES) {
                player.getPacketSender().sendConfig(type.getParentId(), 3);
            }
        }
    }
}
