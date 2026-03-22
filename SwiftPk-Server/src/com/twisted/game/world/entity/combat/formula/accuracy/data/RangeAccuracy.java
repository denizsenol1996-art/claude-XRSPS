package com.twisted.game.world.entity.combat.formula.accuracy.data;

import com.twisted.game.content.skill.impl.slayer.Slayer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.formula.FormulaUtils;
import com.twisted.game.world.entity.combat.formula.accuracy.AbstractAccuracy;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.weapon.FightStyle;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import lombok.Getter;

import java.util.stream.IntStream;

import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.*;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

public class RangeAccuracy implements AbstractAccuracy {
    @Getter public float modifier;
    @Getter Mob attacker, defender;
    CombatType combatType;

    public RangeAccuracy(Mob attacker, Mob defender, CombatType combatType) {
        this.attacker = attacker;
        this.defender = defender;
        this.combatType = combatType;
    }

    @Override
    public Mob attacker() {
        return this.attacker;
    }

    @Override
    public Mob defender() {
        return this.defender;
    }

    @Override
    public float modifier() {
        return this.attacker() instanceof Player player ? (float) player.sigil.processAccuracy(player, this.defender(), this) : 0;
    }

    @Override
    public int getEquipmentBonusForAttacker() {
        return this.attacker instanceof Player ? this.attacker.getBonuses().totalBonuses(attacker, World.getWorld().equipmentInfo()).range : this.attacker.getAsNpc().combatInfo().bonuses.ranged;
    }

    @Override
    public int getEquipmentBonusForDefender() {
        return this.defender instanceof Player ? this.defender.getBonuses().totalBonuses(this.defender, World.getWorld().equipmentInfo()).rangedef : this.defender.getAsNpc().combatInfo() != null ? this.defender.getAsNpc().combatInfo().bonuses.rangeddefence : 0;
    }

    @Override
    public int getOffensiveSkillLevelAttacker() {
        return this.attacker instanceof Npc npc && npc.combatInfo() != null && npc.combatInfo().stats != null ? npc.combatInfo().stats.ranged : this.attacker.skills().level(Skills.RANGED);
    }

    @Override
    public int getDefensiveSKillLevelDefender() {
        return this.defender instanceof Npc npc && npc.combatInfo() != null && npc.combatInfo().stats != null ? npc.combatInfo().stats.ranged : this.defender.skills().level(Skills.DEFENCE);
    }

    @Override
    public double getPrayerBonusAttacker() {
        double prayerBonus = 1D;
        if (this.attacker instanceof Player) {
            if (Prayers.usingPrayer(this.attacker, SHARP_EYE)) prayerBonus *= 1.05D; // 5% range level boost
            else if (Prayers.usingPrayer(this.attacker, HAWK_EYE)) prayerBonus *= 1.10D; // 10% range level boost
            else if (Prayers.usingPrayer(this.attacker, EAGLE_EYE)) prayerBonus *= 1.15D; // 15% range level boost
            else if (Prayers.usingPrayer(this.attacker, RIGOUR)) prayerBonus *= 1.20D; // 20% range level boost
            else if (Prayers.usingPrayer(this.attacker, INTENSIFY)) prayerBonus *= 1.40D;
        }
        return prayerBonus;
    }

    @Override
    public double getPrayerBonusDefender() {
        double prayerBonus = 1D;
        if (defender instanceof Player) if (Prayers.usingPrayer(attacker, RIGOUR)) prayerBonus *= 1.25D;
        return prayerBonus;
    }

    @Override
    public int getEffectiveLevel() {
        int effectiveLevel = 0;
        double specialMultiplier;
        if (this.attacker instanceof Player player) {
            boolean tobPets = player.hasPetOut("Lil' Zik", "Lil' Maiden", "Lil' Bloat", "Lil' Nylo", "Lil' Sot", "Lil' Xarp");
            final Item helm = player.getEquipment().get(EquipSlot.HEAD);

            if (this.defender instanceof Npc npc) {
                if (helm != null && Slayer.creatureMatches(player, npc.id())) {
                    if (player.getEquipment().wearingSlayerHelm() || (IntStream.range(8901, 8921).anyMatch(id -> id == helm.getId()))) {
                        effectiveLevel *= 1.125D;
                    }
                }

                if (player.tile().memberCave()) {
                    effectiveLevel *= 1.10D;
                }

                if (player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_CROSSBOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, CustomItemIdentifiers.DRAGON_HUNTER_CROSSBOW_T) || player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_LANCE)) {
                    if (npc.def() != null && npc.def().name != null && FormulaUtils.isDragon(npc)) {
                        effectiveLevel *= 1.30D;
                    }
                }

                boolean ancientKingBlackDragonPet = player.hasPetOut("Ancient king black dragon");
                if (ancientKingBlackDragonPet) {
                    if (npc.def() != null && npc.def().name != null && FormulaUtils.isDragon(npc)) {
                        effectiveLevel *= 1.25D;
                    }
                }

                if (player.pet() != null && player.hasPetOut("Pet zombies champion") && npc.isWorldBoss()) {
                    effectiveLevel *= 1.10D;
                }

                if (player.pet() != null && player.hasPetOut("Olmlet") && player.getRaids() != null && player.getRaids().raiding(player)) {
                    effectiveLevel *= 1.10D;
                }

                if (player.pet() != null && tobPets && player.getRaids() != null && player.getRaids().raiding(player)) {
                    effectiveLevel *= 1.10D;
                }

                if (FormulaUtils.hasCrawsBow(player)) {
                    effectiveLevel *= 1.50D;
                }

                if (player.getEquipment().contains(TWISTED_BOW_I)) {
                    effectiveLevel *= 1.30D;
                }

                if (player.getEquipment().contains(SANGUINE_TWISTED_BOW)) {
                    effectiveLevel *= 1.25D;
                }


                if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULET)) {
                    effectiveLevel *= 1.15D;
                }

                if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETI) || player.getEquipment().contains(SALVE_AMULET_E) || player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETEI)) {
                    effectiveLevel *= 1.2D;
                }

                if (player.getEquipment().contains(SARKIS_DARK_COIF)) {
                    effectiveLevel *= 1.05D;
                }

                if (player.getEquipment().contains(SARKIS_DARK_BODY)) {
                    effectiveLevel *= 1.05D;
                }

                if (player.getEquipment().contains(SARKIS_DARK_LEGS)) {
                    effectiveLevel *= 1.05D;
                }
            }

            if (player.hasPetOut("Nagini pet")) {
                effectiveLevel *= 1.10D;
            }

            if (player.hasPetOut("Kerberos pet")) {
                effectiveLevel *= 1.05D;
            }

            if (player.hasPetOut(366)) {
                effectiveLevel *= 1.10D;
            }

            if (player.hasPetOut("Skeleton hellhound pet")) {
                effectiveLevel *= 1.05D;
            }

            if (player.hasPetOut("Youngllef pet")) {
                effectiveLevel *= 1.075D;
            }

            if (player.hasPetOut("Corrupted Youngllef pet")) {
                effectiveLevel *= 1.125D;
            }

            if (player.hasPetOut("Little Nightmare")) {
                effectiveLevel *= 1.05D;
            }

            if (player.hasPetOut("Memory of seren")) {
                effectiveLevel *= 1.15D;
            }

            if (player.hasPetOut("Baby Squirt")) {
                effectiveLevel *= 1.10D;
            }

            if (player.hasPetOut("Baby Aragog")) {
                effectiveLevel *= 1.15D;
            }

            if (player.hasPetOut("Kaal-Ket-Jor jr")) {
                effectiveLevel *= 1.15D;
            }

            if (player.getEquipment().hasAt(EquipSlot.HEAD, CRYSTAL_HELM)) {
                effectiveLevel *= 1.05D;
            }

            if (player.getEquipment().hasAt(EquipSlot.BODY, CRYSTAL_BODY)) {
                effectiveLevel *= 1.15D;
            }

            if (player.getEquipment().hasAt(EquipSlot.LEGS, CRYSTAL_LEGS)) {
                effectiveLevel *= 1.10D;
            }

            if (player.getCombatSpecial() != null && player.isSpecialActivated()) {
                specialMultiplier = player.getCombatSpecial().getAccuracyMultiplier();
                effectiveLevel *= specialMultiplier;
            }

            effectiveLevel += this.getOffensiveStyleBonus(player);

            if (FormulaUtils.wearingEliteVoidOr(player)) {
                effectiveLevel *= 1.10D;
            }
        }
        return effectiveLevel;
    }

    @Override
    public int getOffensiveStyleBonus(Player player) {
        var style = player.getCombat().getFightType().getStyle();
        return style.equals(FightStyle.ACCURATE) ? 3 : 0;
    }

    @Override
    public int getDefensiveStyleBonus() {
        var style = this.defender().getCombat().getFightType().getStyle();
        return style.equals(FightStyle.DEFENSIVE) ? 3 : style.equals(FightStyle.CONTROLLED) ? 1 : 0;
    }

    @Override
    public CombatType getCombatType() {
        return this.combatType;
    }
}
