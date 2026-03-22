package com.twisted.game.world.entity.combat.formula.accuracy;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Color;

public interface AbstractAccuracy {
    Mob attacker();

    Mob defender();

    CombatType getCombatType();

    float modifier();

    int getEquipmentBonusForAttacker();

    int getEquipmentBonusForDefender();

    int getOffensiveSkillLevelAttacker();

    int getDefensiveSKillLevelDefender();

    double getPrayerBonusAttacker();

    double getPrayerBonusDefender();

    int getEffectiveLevel();

    int getOffensiveStyleBonus(Player player);

    int getDefensiveStyleBonus();

    default double getBoost() {
        if (this.getCombatType() == null) return 0.0D;
        if (this.getCombatType().equals(CombatType.MELEE)) return 0.25D;
        else if (this.getCombatType().equals(CombatType.MAGIC)) return 0.50D;
        else if (this.getCombatType().equals(CombatType.RANGED)) return 0.90D;
        else return 0.0D;
    }

    default boolean success(double selectedChance) {
        double chance;
        double specialMultiplier = 0.0D;

        if (this.attacker() instanceof Player player && player.getCombatSpecial() != null && player.isSpecialActivated()) {
            specialMultiplier = getSpecialMultiplier(player);
        }

        int attackRoll = this.getAttackRoll();

        if (specialMultiplier > 0.0D) {
            attackRoll *= specialMultiplier;
        }

        int defenceRoll = this.getDefenceRoll();

        if (this.getBoost() > 0.0D && this.defender() instanceof Npc) {
            defenceRoll *= getBoost();
        }

        if (attackRoll > defenceRoll) chance = 1D - (defenceRoll + 2D) / (2D * (attackRoll + 1D));
        else chance = attackRoll / (2D * (defenceRoll + 1D));

      //  if (this.attacker() instanceof Player player) {
           // if (Hit.isDebugAccuracy() && PlayerRights.OWNER.isOwner(player)) {
                sendDebugPrints(chance, attackRoll, defenceRoll);
           // }
       // }

        return chance > selectedChance;
    }

    default int getAttackRoll() {
        double modification = this.modifier();
        double prayerBonus = this.getPrayerBonusAttacker();
        int attackLevel = this.getOffensiveSkillLevelAttacker();
        attackLevel *= prayerBonus;
        if (this.attacker() instanceof Player player) {
            attackLevel += this.getOffensiveStyleBonus(player);
        }
        attackLevel += 8;
        int effectiveAttack = attackLevel + this.getEffectiveLevel();
        int equipmentBonus = this.getEquipmentBonusForAttacker();
        int attackRoll = effectiveAttack * (equipmentBonus + 64);
        if (modification > 0.0D) attackRoll *= modification;
        return attackRoll;
    }

    default int getDefenceRoll() {
        int effectiveDefence;
        double prayerBonus = this.getPrayerBonusDefender();
        int defenceLevel = this.getDefensiveSKillLevelDefender();
        defenceLevel *= prayerBonus;
        int equipmentBonus = this.getEquipmentBonusForDefender();
        if (this.defender() instanceof Player) {
            effectiveDefence = defenceLevel;
            effectiveDefence += getDefensiveStyleBonus();
        } else effectiveDefence = defenceLevel + 9;
        return effectiveDefence * (equipmentBonus + 64);
    }

    default double getSpecialMultiplier(Player player) {
        double specialMultiplier = player.getCombatSpecial().getAccuracyMultiplier();
        if (player.hasPetOut("Artio pet")) specialMultiplier += 0.20D;
        if (player.hasPetOut("Jal-Nib-Rek pet")) specialMultiplier += 0.10D;
        return specialMultiplier;
    }

    private void sendDebugPrints(double chance, int attackRoll, int defenceRoll) {
        this.attacker().message("*<shad=0>[" + Color.PURPLE.wrap("" + this.getCombatType()) + "]</shad>" + " Attack Roll: [" + attackRoll + "]");
        this.attacker().message("*<shad=0>[" + Color.PURPLE.wrap("" + this.getCombatType()) + "]</shad>" + " Defence Roll: [" + defenceRoll + "]");
        this.attacker().message("*<shad=0>[" + Color.PURPLE.wrap("" + this.getCombatType()) + "]</shad>" + " Chance To Hit: [" + String.format("%.2f%%", chance * 100) + "]");
    }

}
