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
import com.twisted.util.ItemIdentifiers;
import lombok.Getter;

import java.util.stream.IntStream;

import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.*;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.SALVE_AMULET_E;

public final class MagicAccuracy implements AbstractAccuracy {

    @Getter public float modifier;
    @Getter Mob attacker, defender;
    CombatType combatType;

    public MagicAccuracy(Mob attacker, Mob defender, CombatType combatType) {
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
        return this.attacker() instanceof Player ? this.attacker().getBonuses().totalBonuses(this.attacker(), World.getWorld().equipmentInfo()).mage : this.attacker().getAsNpc().getCombatInfo().getBonuses().magic;
    }

    @Override
    public int getEquipmentBonusForDefender() {
        if (this.defender() instanceof Player player) return player.getBonuses().totalBonuses(player, World.getWorld().equipmentInfo()).magedef;
        else if (this.defender() instanceof Npc npc) return npc.getCombatInfo().getBonuses().magicdefence;
        return 0;
    }

    @Override
    public int getOffensiveSkillLevelAttacker() {
        return this.attacker() instanceof Npc npc && npc.getCombatInfo() != null ? npc.getCombatInfo().getStats().magic : this.attacker().skills().level(Skills.MAGIC);
    }

    @Override
    public int getDefensiveSKillLevelDefender() {
        return this.defender() instanceof Npc npc && npc.getCombatInfo() != null ? npc.getCombatInfo().getStats().magic : this.defender().skills().level(Skills.MAGIC);
    }

    @Override
    public double getPrayerBonusAttacker() {
        double prayerBonus = 1D;
        if (this.attacker() instanceof Player) {
            if (Prayers.usingPrayer(this.attacker(), MYSTIC_WILL)) prayerBonus *= 1.05D;
            else if (Prayers.usingPrayer(this.attacker(), MYSTIC_LORE)) prayerBonus *= 1.10D;
            else if (Prayers.usingPrayer(this.attacker(), MYSTIC_MIGHT)) prayerBonus *= 1.15D;
            else if (Prayers.usingPrayer(this.attacker(), AUGURY)) prayerBonus *= 1.25D;
            else if (Prayers.usingPrayer(this.attacker(), INTENSIFY)) prayerBonus *= 1.40D;
        }
        return prayerBonus;
    }

    @Override
    public double getPrayerBonusDefender() {
        double prayerBonus = 1D;
        if (this.defender() instanceof Player) if (Prayers.usingPrayer(this.defender(), AUGURY)) prayerBonus *= 1.25D;
        return prayerBonus;
    }

    @Override
    public int getEffectiveLevel() {
        int effectiveLevel = 0;
        if (this.attacker() instanceof Player player) {
            boolean tobPets = player.hasPetOut("Lil' Zik", "Lil' Maiden", "Lil' Bloat", "Lil' Nylo", "Lil' Sot", "Lil' Xarp");
            final Item helm = player.getEquipment().get(EquipSlot.HEAD);
            effectiveLevel = this.getOffensiveStyleBonus(player);
            if (this.defender() instanceof Npc npc) {
                if (helm != null && Slayer.creatureMatches(player, npc.id())) {
                    if (player.getEquipment().wearingSlayerHelm() || (IntStream.range(8901, 8921).anyMatch(id -> id == helm.getId()))) {
                        effectiveLevel *= 1.125D;
                    }
                }

                if (player.tile().memberCave()) {
                    effectiveLevel *= 1.10D;
                }

                if (npc.id() == 6593) {
                    effectiveLevel *= 1.50D;
                }

                if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULET)) {
                    effectiveLevel *= 1.15D;
                }

                if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETI) || player.getEquipment().contains(SALVE_AMULET_E) || player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETEI)) {
                    effectiveLevel *= 1.20D;
                }

                if (FormulaUtils.hasThammaronSceptre(player)) {
                    effectiveLevel *= 1.50D;
                }

                if (player.getEquipment().hasAt(EquipSlot.WEAPON, THAMMARONS_STAFF_C)) {
                    effectiveLevel *= 1.50D;
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

                if (player.getEquipment().contains(DARK_SAGE_HAT)) {
                    effectiveLevel *= 1.05D;
                }

                if (player.getEquipment().contains(DARK_SAGE_ROBE_TOP)) {
                    effectiveLevel *= 1.05D;
                }

                if (player.getEquipment().contains(DARK_SAGE_ROBE_BOTTOM)) {
                    effectiveLevel *= 1.05D;
                }
            }

            if (player.hasPetOut("Baby Elvarg", "Snowbird", "Kerberos pet", "Little Nightmare", "Skeleton hellhound pet")) {
                effectiveLevel *= 1.05D;
            }

            if (player.hasPetOut("Deranged archaeologist", "Nagini pet", "Baby Squirt", "Baby Lava Dragon")) {
                effectiveLevel *= 1.10D;
            }

            if (player.hasPetOut("Youngllef pet")) {
                effectiveLevel *= 1.075D;
            }

            if (player.hasPetOut("Corrupted Youngllef pet")) {
                effectiveLevel *= 1.125D;
            }

            if (player.hasPetOut("Kaal-Ket-Jor jr")) {
                effectiveLevel *= 1.15D;
            }

            if (player.hasPetOut(366)) {
                effectiveLevel *= 1.10D;
            }

            if (player.getEquipment().containsAny(11998, 12000)) {
                effectiveLevel *= 1.10D;
            }

            if (player.getEquipment().hasAt(EquipSlot.HEAD, ANCESTRAL_HAT_I)) {
                effectiveLevel *= 1.10D;
            }

            if (FormulaUtils.wearingEliteVoidOr(player)) {
                effectiveLevel *= 1.10D;
            }

            if ((FormulaUtils.voidMagic(player) || FormulaUtils.voidCustomMagic(player))) {
                effectiveLevel *= 1.30;
            }

            if (player.getEquipment().hasAt(EquipSlot.BODY, ANCESTRAL_ROBE_TOP_I)) {
                effectiveLevel *= 1.10D;
            }

            if (player.getEquipment().hasAt(EquipSlot.LEGS, ANCESTRAL_ROBE_BOTTOM_I)) {
                effectiveLevel *= 1.10D;
            }
        } else {
            effectiveLevel = getOffensiveSkillLevelAttacker() + 9;
        }
        return effectiveLevel;
    }

    @Override
    public int getDefenceRoll() {
        double prayer = this.getPrayerBonusDefender();
        int defenceLevel = (int) Math.floor(this.getDefensiveSKillLevelDefender() * prayer);
        int bonus = this.getEquipmentBonusForDefender();
        int effectiveDefence = 0;
        int effectiveMagic = this.getOffensiveSkillLevelAttacker();
        if (this.defender() instanceof Npc) effectiveDefence = defenceLevel + 9;
        else if (this.defender() instanceof Player) {
            bonus += this.getDefensiveStyleBonus();
            effectiveMagic *= 0.7D;
            effectiveMagic += defenceLevel;
            effectiveMagic *= 0.3D;
            effectiveMagic += 8;
            effectiveDefence += effectiveMagic;
        }
        return effectiveDefence * (bonus + 64);
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
