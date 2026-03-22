package com.twisted.game.world.entity.combat.formula;

import com.twisted.game.content.skill.impl.slayer.Slayer;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.magic.CombatSpell;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.container.equipment.EquipmentBonuses;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.NpcIdentifiers;
import com.twisted.util.timers.TimerKey;
import org.apache.commons.lang.ArrayUtils;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.HOLY_SANGUINESTI_STAFF;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since November 06, 2021
 */
public class MagicMaxHit {

    public static int maxHit(Player player, boolean includeNpcMax) {
        boolean lilZikPet = player.hasPetOut("Lil' Zik");
        boolean lilMaidenPet = player.hasPetOut("Lil' Maiden");
        boolean lilBloatPet = player.hasPetOut("Lil' Bloat");
        boolean lilNyloPet = player.hasPetOut("Lil' Nylo");
        boolean lilSotPet = player.hasPetOut("Lil' Sot");
        boolean lilXarpPet = player.hasPetOut("Lil' Xarp");
        boolean tobPet = lilZikPet
            || lilMaidenPet || lilBloatPet || lilNyloPet || lilSotPet || lilXarpPet;
        int baseMaxHit = 0;
        CombatSpell spell = player.getCombat().getCastSpell() != null ? player.getCombat().getCastSpell() : player.getCombat().getAutoCastSpell();
        if (spell != null) {
            EquipmentBonuses b = player.getBonuses().totalBonuses(player, World.getWorld().equipmentInfo());
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            Mob target = player.getCombat().getTarget();
            String spell_name = spell.name();
            int level = player.skills().level(Skills.MAGIC);

            //Find the base maximum damage a spell can deal.
            int spell_maxhit = spell.baseMaxHit();

            //• Slayer dart
            if (spell_name.equals("Magic Dart")) {
                spell_maxhit = (int) (10 + Math.floor(level / 10D));
            }

            //• Trident of the seas
            if (spell_name.equals("Trident of the seas")) {
                spell_maxhit = 28;
                //spell_maxhit = (int) Math.round((Math.max(spell_maxhit, spell_maxhit + (Math.max(0, level - 75)) / 3)) * (1 + (b.magestr / 100.0)));
            }

            //• Trident of the swamp
            if (spell_name.equals("Trident of the swamp")) {
                spell_maxhit = 31;
                //spell_maxhit = (int) Math.round((Math.max(spell_maxhit, spell_maxhit + (Math.max(0, level - 75)) / 3)) * (1 + (b.magestr / 100.0)));
            }

            if (player.getEquipment().hasAt(EquipSlot.WEAPON, TUMEKENS_SHADOW) && target != null && target.isNpc()) {
                spell_maxhit = 48;
                spell_maxhit = (int) Math.round((Math.max(spell_maxhit, spell_maxhit + (Math.max(0, level - 75)) / 5)) * (1 + (b.magestr / 100.0)));
            }
            //• God spells (level 60) in combination with Charge (level 80): the base max hit is 30.
            if (spell_name.equals("Saradomin Strike") || spell_name.equals("Claws of Guthix") || spell_name.equals("Flames of Zamorak")) {
                if (player.getTimerRepository().has(TimerKey.CHARGE_SPELL)) {
                    spell_maxhit = 30;
                }
            }

            if (spell_name.toLowerCase().contains("fire") && hasTomeOfFire) {
                spell_maxhit *= 1.50;
            }

            double multiplier = 1 + ((b.magestr > 0 ? b.magestr : 1.0) / 100);

            if (FormulaUtils.hasThammaronSceptre(player) && target != null && target.isNpc() && includeNpcMax && WildernessArea.inWilderness(player.tile())) {
                multiplier += 0.25;
            }

            if (target instanceof Npc npc
                && ArrayUtils.contains(FormulaUtils.isRevenant(), npc.id())
                && player.getEquipment().contains(AMULET_OF_AVARICE)) {
                multiplier += 0.20;
            }

            if (player.getEquipment().hasAt(EquipSlot.WEAPON, THAMMARONS_STAFF_C) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.25;
            }

            if (Prayers.usingPrayer(player, Prayers.AUGURY)) {//upos
                multiplier += 0.25;
            }

            if (player.getEquipment().hasAt(EquipSlot.HEAD, TURQUOISE_SLAYER_HELMET_I)) {
                multiplier += 0.10;
            }

            if (target != null && target.isNpc() && includeNpcMax) {
                Npc npc = target.getAsNpc();
                if (npc.id() == NpcIdentifiers.COMBAT_DUMMY) {
                    if (player.getEquipment().hasAt(EquipSlot.HEAD, 30326)) {
                        multiplier *= 1.2;
                    } else {
                        multiplier *= 1.1667;
                    }
                }
                //without combat dummy and undead dummy vs normal monster
                if (npc.id() != NpcIdentifiers.COMBAT_DUMMY && npc.id() != NpcIdentifiers.UNDEAD_COMBAT_DUMMY) {
                    if (player.getEquipment().hasAt(EquipSlot.HEAD, 30326)) {
                        multiplier *= 1.2;//with e helm on normal monster

                    } else {
                        multiplier *= 1.1667;//without e helm on normal monster and give bonus to another slayer helm

                    }
                }
                if (npc.id() == NpcIdentifiers.UNDEAD_COMBAT_DUMMY) {//on undead dummy slayer helm i will be boosted
                    if (player.getEquipment().hasAt(EquipSlot.HEAD, TWISTED_SLAYER_HELMET_I)) {
                        multiplier *= 1.4;

                    }
                }
                //Slayer helms on tasks
                if (Slayer.creatureMatches(player, npc.id())) {
                    if (player.getEquipment().hasAt(EquipSlot.HEAD, TWISTED_SLAYER_HELMET_I)) {
                        multiplier *= 1.4;//alone boost with slayer helm i
                    } else {
                        multiplier *= 1.1667;//anoher slayer helms

                    }
                }
            }

            /**
             * When wearing the clock of invisibility with an elder wand you get a 10% damage boost vs npcs
             */
            boolean elderWand = player.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND);
            boolean cloakOfInvisibility = player.getEquipment().hasAt(EquipSlot.CAPE, CLOAK_OF_INVISIBILITY);
            boolean wearingBoth = elderWand && cloakOfInvisibility;
            if (target != null && target.isNpc() && wearingBoth) {
                multiplier += 0.10;
            }
            if (player.getEquipment().hasAt(EquipSlot.WEAPON, MENDING_WAND)) {//bloodrevsupdate
                multiplier += 0.05;
            }

            // #Custom slayer effects
            var weakSpot = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.WEAK_SPOT);
            if (weakSpot && target != null && target.isNpc()) {
                if (Slayer.creatureMatches(player, target.getAsNpc().id())) {
                    multiplier += 0.10;
                }
            }

            int weapon = player.getEquipment().get(3) == null ? -1 : player.getEquipment().get(3).getId();
            if (spell_name.equals("Volatile spell")) {
                int baseLevel = level;
                if (baseLevel > 99)
                    baseLevel = 99;
                double levelTimes = 0.67;
                multiplier -= 0.15;
                spell_maxhit = (int) (baseLevel * levelTimes);
            }

            // #Custom armour multipliers
            if (player.getEquipment().hasAt(EquipSlot.HEAD, DARK_SAGE_HAT) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.05;//5% damage boost
            }

            if (player.getEquipment().hasAt(EquipSlot.BODY, DARK_SAGE_ROBE_TOP) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.10;//10% damage boost
            }

            if (player.getEquipment().hasAt(EquipSlot.LEGS, DARK_SAGE_ROBE_BOTTOM) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.10;//10.0% damage boost
            }
            if (player.getEquipment().hasAt(EquipSlot.HEAD, E_TOTEMIC_HELMET) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.10;//+10% Reduce damage
            }
            if (player.getEquipment().hasAt(EquipSlot.LEGS, E_TOTEMIC_PLATELEGS) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.15;//+15% Reduce damage
            }
            if (player.getEquipment().hasAt(EquipSlot.BODY, E_TOTEMIC_PLATEBODY) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.15;//+15% Reduce damage
            }
            int maxHit = (int) Math.round(spell_maxhit * multiplier);

            // #Custom Armour effects
            if (player.getEquipment().hasAt(EquipSlot.AMULET, OCCULT_NECKLACE_OR) || player.getEquipment().hasAt(EquipSlot.HANDS, TORMENTED_BRACELET_OR)) {
                maxHit += 1;
            }

            if (player.getEquipment().hasAt(EquipSlot.RING, MARVOLO_GAUNTS_RING) && target != null && target.isNpc()) {
                maxHit += 5;
            }

            //# Pet effects
            if (player.hasPetOut("Olmlet") && target != null && target.isNpc() && includeNpcMax) {
                var increaseBy = 5;
                if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                    maxHit += increaseBy;
                }
                if (player.getRaids() != null && player.getRaids().raiding(player))
                    maxHit += increaseBy;
            }

            if (tobPet && target != null && target.isNpc() && includeNpcMax) {
                var increaseBy = 5;
                if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                    maxHit += increaseBy;
                }
                if (player.getRaids() != null && player.getRaids().raiding(player))
                    maxHit += increaseBy;
            }


            if (player.hasPetOut("Skeleton hellhound pet")) {
                var increaseBy = target != null && target.isNpc() && includeNpcMax ? 2 : 1;
                maxHit += increaseBy;
            }

            if (player.hasPetOut("Baby lava dragon pet")) {
                var increaseBy = target != null && target.isNpc() && includeNpcMax ? 5 : 1;
                maxHit += increaseBy;
            }

            if (player.hasPetOut("Mini necromancer pet")) {
                var increaseBy = target != null && target.isNpc() && includeNpcMax ? 3 : 1;
                maxHit += increaseBy;
            }

            if (player.hasPetOut("Nagini pet")) {
                var increaseBy = target != null && target.isNpc() && includeNpcMax ? 10 : 1;
                maxHit += increaseBy;
            }
            if (player.hasPetOut("Youngllef pet")) {
                maxHit += 1;
            }

            if (player.hasPetOut("Corrupted Youngllef pet")) {
                maxHit += 2;
            }

            if (player.hasPetOut(366)) {
                maxHit += 2;
            }

            if (player.hasPetOut("Little Nightmare")) {
                maxHit += 1;
            }
            if (player.hasPetOut("Lava beast pet")) {
                maxHit += 1;
            }
            if (player.hasPetOut("Snowbird") && spell_name.equals("Ice barrage")) {
                maxHit += 2;
            }
            if (spell_name.contains("barrage") || spell_name.contains("blitz") || spell_name.contains("Ice")) {//updatevoidor
                maxHit -= 5;

            }
            if (FormulaUtils.voidMagic(player)) {//updatevoidor
                maxHit += 2;
            }
            if (FormulaUtils.wearingEliteVoid(player) || FormulaUtils.voidCustomMelee(player)) {//updatevoidor
                maxHit += 4;

            }
            if (FormulaUtils.wearingEliteVoidOr(player)) {//updatevoidor
                maxHit += 8;
            }

            if (target instanceof Npc && player.getEquipment().contains(THAMMARONS_STAFF_C)) {
                maxHit += 20;
            }

            if (player.hasPetOut("Baby Elvarg") && spell_name.equals("Fire surge")) {
                maxHit += 2;
            }

            //After all modifiers spell max hits
            if (spell_name.equals("Petrificus Totalus") && target != null && target.isPlayer()) {
                maxHit = 40;
            }

            if (spell_name.equals("Cruciatus Curse") && target != null && target.isPlayer()) {
                maxHit = 41;
            }

            if (spell_name.equals("Expelliarmus") && target != null && target.isPlayer()) {
                maxHit = 50;
            }

            if (spell_name.equals("Sectumsempra") && target != null && target.isPlayer()) {
                maxHit = 55;
            }

            if (spell_name.equals("Avada Kedavra") && target != null && target.isPlayer()) {
                if (target.isNpc()) {
                    maxHit = 82;

                }
            }

            if (spell_name.equals("Sanguinesti spell")) {
                boolean holy_staff = weapon == HOLY_SANGUINESTI_STAFF;
                if (holy_staff) {
                    // System.out.println("i mean it works");
                    maxHit += 10;
                }
            }
            return maxHit;
        }
        return baseMaxHit;
    }
}
