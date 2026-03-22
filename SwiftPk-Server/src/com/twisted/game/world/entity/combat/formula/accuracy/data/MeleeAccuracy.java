package com.twisted.game.world.entity.combat.formula.accuracy.data;

import com.twisted.game.content.skill.impl.slayer.Slayer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.formula.FormulaUtils;
import com.twisted.game.world.entity.combat.formula.accuracy.AbstractAccuracy;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.weapon.AttackType;
import com.twisted.game.world.entity.combat.weapon.FightStyle;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.equipment.EquipmentBonuses;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import lombok.Getter;

import java.util.stream.IntStream;

import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.*;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.NpcIdentifiers.CORPOREAL_BEAST;

/**
 * @Author Origin
 */
public class MeleeAccuracy implements AbstractAccuracy {
    @Getter public float modifier;
    @Getter Mob attacker, defender;
    CombatType combatType;

    public MeleeAccuracy(Mob attacker, Mob defender, CombatType combatType) {
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
        int bonus = 0;
        if (this.attacker() instanceof Player) {
            EquipmentBonuses attackerBonus = this.attacker.getBonuses().totalBonuses(this.attacker, World.getWorld().equipmentInfo());
            final AttackType type = this.attacker.getCombat().getFightType().getAttackType();
            switch (type) {
                case STAB -> bonus = attackerBonus.stab;
                case CRUSH -> bonus = attackerBonus.crush;
                case SLASH -> bonus = attackerBonus.slash;
            }
        } else if (this.attacker instanceof Npc npc) {
            bonus = npc.combatInfo().bonuses.attack;
        }
        return bonus;
    }

    @Override
    public int getEquipmentBonusForDefender() {
        int bonus = 0;
        AttackType type;
        if (this.defender() instanceof Npc npc) {
            var stats = npc.combatInfo().bonuses;
            type = this.attacker instanceof Player player ? player.getCombat().getFightType().getAttackType() : npc.getCombat().getFightType().getAttackType();
            if (type != null) {
                if (npc.combatInfo() != null) {
                    if (npc.combatInfo().stats != null) {
                        switch (type) {
                            case STAB -> bonus = stats.stabdefence;
                            case CRUSH -> bonus = stats.crushdefence;
                            case SLASH -> bonus = stats.slashdefence;
                        }
                    }
                }
            }
        } else if (this.defender instanceof Player) {
            var stats = this.defender.getBonuses().totalBonuses(this.defender, World.getWorld().equipmentInfo());
            type = this.defender.getCombat().getFightType().getAttackType();
            if (type != null) {
                switch (type) {
                    case STAB -> bonus = stats.stabdef;
                    case CRUSH -> bonus = stats.crushdef;
                    case SLASH -> bonus = stats.slashdef;
                }
            }
        }
        return bonus;
    }

    @Override
    public int getOffensiveSkillLevelAttacker() {
        return this.attacker instanceof Npc npc && npc.combatInfo().stats != null ? npc.combatInfo().stats.attack : this.attacker.skills().level(Skills.ATTACK);
    }

    @Override
    public int getDefensiveSKillLevelDefender() {
        return this.defender instanceof Npc npc && npc.combatInfo().stats != null ? npc.combatInfo().stats.defence : this.defender.skills().level(Skills.DEFENCE);
    }

    @Override
    public double getPrayerBonusAttacker() {
        double prayerBonus = 1D;
        if (this.attacker instanceof Player) {
            if (Prayers.usingPrayer(this.attacker, CLARITY_OF_THOUGHT)) prayerBonus *= 1.05D; // 5% attack level boost
            else if (Prayers.usingPrayer(this.attacker, IMPROVED_REFLEXES)) prayerBonus *= 1.10D; // 10% attack level boost
            else if (Prayers.usingPrayer(this.attacker, INCREDIBLE_REFLEXES)) prayerBonus *= 1.15D; // 15% attack level boost
            else if (Prayers.usingPrayer(this.attacker, CHIVALRY)) prayerBonus *= 1.15D; // 15% attack level boost
            else if (Prayers.usingPrayer(this.attacker, PIETY)) prayerBonus *= 1.20D; // 20% attack level boost
            else if (Prayers.usingPrayer(this.attacker, INTENSIFY)) prayerBonus *= 1.40D;
        }
        return prayerBonus;
    }

    @Override
    public double getPrayerBonusDefender() {
        double prayerBonus = 1F;
        if (this.attacker instanceof Player) {
            if (Prayers.usingPrayer(this.defender, THICK_SKIN)) prayerBonus *= 1.05D; // 5% def level boost
            else if (Prayers.usingPrayer(this.defender, ROCK_SKIN)) prayerBonus *= 1.10D; // 10% def level boost
            else if (Prayers.usingPrayer(this.defender, STEEL_SKIN)) prayerBonus *= 1.15D; // 15% def level boost
            else if (Prayers.usingPrayer(this.defender, CHIVALRY)) prayerBonus *= 1.20D; // 20% def level boost
            else if (Prayers.usingPrayer(this.defender, PIETY)) prayerBonus *= 1.25D; // 25% def level boost
        }
        return prayerBonus;
    }

    @Override
    public int getEffectiveLevel() {
        int effectiveLevel = 0;
        if (this.attacker instanceof Player player) {
            final Item helm = player.getEquipment().get(EquipSlot.HEAD);
            final Item weapon = player.getEquipment().get(EquipSlot.WEAPON);
            boolean tobPets = player.hasPetOut("Lil' Zik", "Lil' Maiden", "Lil' Bloat", "Lil' Nylo", "Lil' Sot", "Lil' Xarp");
            if (this.defender instanceof Npc npc) {
                AttackType attackType = player.getCombat().getFightType().getAttackType();
                if (helm != null && Slayer.creatureMatches(player, npc.id())) {
                    if (player.getEquipment().wearingSlayerHelm() || (IntStream.range(8901, 8921).anyMatch(id -> id == helm.getId()))) {
                        effectiveLevel *= 1.125D;
                    }
                }

                if (npc.id() == CORPOREAL_BEAST) {
                    if (weapon != null && player.getEquipment().corpbeastArmour(weapon) && attackType != null && attackType.equals(AttackType.STAB)) {
                        effectiveLevel *= 1.50D;
                    }
                }

                if (player.pet() != null && tobPets && player.getRaids() != null && player.getRaids().raiding(player)) {
                    effectiveLevel *= 1.10D;
                }

                if (FormulaUtils.hasViggorasChainMace(player) || player.getEquipment().hasAt(EquipSlot.WEAPON, VIGGORAS_CHAINMACE_C)) {
                    effectiveLevel *= 1.5D;
                }

                if (player.pet() != null && player.hasPetOut("Pet zombies champion") && npc.isWorldBoss()) {
                    effectiveLevel *= 1.10D;
                }

                if (player.pet() != null && player.hasPetOut("Olmlet") && player.getRaids() != null && player.getRaids().raiding(player)) {
                    effectiveLevel *= 1.10D;
                }

                if (player.tile().memberCave()) {
                    effectiveLevel *= 1.10D;
                }

                if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULET)) {
                    effectiveLevel *= 1.15D;
                }

                if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETI) || player.getEquipment().contains(SALVE_AMULET_E) || player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETEI)) {
                    effectiveLevel *= 1.2D;
                }

                boolean ancientKingBlackDragonPet = player.hasPetOut("Ancient king black dragon");
                if (ancientKingBlackDragonPet) {
                    if (npc.def() != null && npc.def().name != null && FormulaUtils.isDragon(npc)) {
                        effectiveLevel *= 1.25D;
                    }
                }

                if (npc.def() != null && npc.def().name != null && FormulaUtils.isDemon(npc)) {
                    if (player.getEquipment().hasAt(EquipSlot.WEAPON, ARCLIGHT)) {
                        effectiveLevel *= 1.70D;
                    }
                }
            }

            if (player.getEquipment().hasAt(EquipSlot.SHIELD, MOLTEN_DEFENDER)) {
                effectiveLevel *= 1.1D;
            }

            if (player.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_MAUL_21205)) {
                effectiveLevel *= 1.10D;
            }

            if (player.getCombat().getFightType().getAttackType() == AttackType.CRUSH) {
                if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM)) {
                    effectiveLevel *= 1.01D;//1% accuracy boost
                }

                if (player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK)) {
                    effectiveLevel *= 1.01D;//1% accuracy boost
                }

                if (player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                    effectiveLevel *= 1.01D;//1% accuracy boost
                }

                if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM) || player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK) || player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                    effectiveLevel *= 1.02D;//2% accuracy boost
                }
            }

            if (player.getEquipment().hasAt(EquipSlot.WEAPON, CustomItemIdentifiers.SWORD_OF_GRYFFINDOR)) {
                effectiveLevel *= 1.25D;
            }


            if (player.getEquipment().hasAt(EquipSlot.WEAPON, 30103)) {//dzone3
                effectiveLevel *= 1.25D;
            }

            if (player.getEquipment().hasAt(EquipSlot.HEAD, E_TOTEMIC_HELMET) || player.getEquipment().hasAt(EquipSlot.HEAD, TOTEMIC_HELMET) || player.getEquipment().hasAt(EquipSlot.HEAD, DARK_SAGE_HAT) || player.getEquipment().hasAt(EquipSlot.HEAD, SARKIS_DARK_COIF)) {
                effectiveLevel *= 1.05D;//5% accuracy boost
            }

            if (player.getEquipment().hasAt(EquipSlot.BODY, E_TOTEMIC_PLATEBODY) || player.getEquipment().hasAt(EquipSlot.BODY, TOTEMIC_PLATEBODY) || player.getEquipment().hasAt(EquipSlot.BODY, DARK_SAGE_ROBE_TOP) || player.getEquipment().hasAt(EquipSlot.BODY, SARKIS_DARK_BODY)) {
                effectiveLevel *= 1.10D;//10% damage boost
            }

            if (player.getEquipment().hasAt(EquipSlot.LEGS, E_TOTEMIC_PLATELEGS) || player.getEquipment().hasAt(EquipSlot.LEGS, TOTEMIC_PLATELEGS) || player.getEquipment().hasAt(EquipSlot.LEGS, DARK_SAGE_ROBE_BOTTOM) || player.getEquipment().hasAt(EquipSlot.LEGS, SARKIS_DARK_LEGS)) {
                effectiveLevel *= 1.10D;//10.0% damage boost
            }

            if (FormulaUtils.obbyArmour(player) && FormulaUtils.hasObbyWeapon(player)) {
                effectiveLevel *= 1.10D;
            }

            if (player.hasPetOut("Kaal-Ket-Jor jr")) {
                effectiveLevel *= 1.15D;
            }

            if (player.hasPetOut("Corrupted nechryarch pet")) {
                effectiveLevel *= 1.15D;
            }

            if (player.hasPetOut("Baby Squirt")) {
                effectiveLevel *= 1.10D;
            }

            if (player.hasPetOut(366)) {
                effectiveLevel *= 1.10D;
            }

            if (player.hasPetOut("Ancient barrelchest")) {
                effectiveLevel *= 1.20D;
            }

            if (player.hasPetOut("Baby Barrelchest")) {
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

            if (player.hasPetOut("Kerberos pet")) {
                effectiveLevel *= 1.05D;
            }

            if (player.hasPetOut("Nagini pet")) {
                effectiveLevel *= 1.10D;
            }

            if (player.hasPetOut("Dharok the Wretched") && player.getEquipment().hasAt(EquipSlot.WEAPON, DHAROKS_GREATAXE)) {
                effectiveLevel *= 1.10D;
            }

            if (player.getEquipment().contains(CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR)) {
                effectiveLevel *= 1.30D;
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
