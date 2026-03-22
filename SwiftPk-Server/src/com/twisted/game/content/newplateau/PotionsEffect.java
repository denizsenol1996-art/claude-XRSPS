package com.twisted.game.content.newplateau;

import com.twisted.game.content.EffectTimer;
import com.twisted.game.content.consumables.potions.impl.DivineRangingPotion;
import com.twisted.game.content.consumables.potions.impl.DivineSuperCombatPotion;
import com.twisted.game.content.consumables.potions.impl.OverloadPotion;
import com.twisted.game.content.duel.DuelRule;
import com.twisted.game.content.mechanics.Poison;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.AreaConstants;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

import static com.twisted.game.world.entity.AttributeKey.OVERLOAD_POTION;

public class PotionsEffect extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        int eatAnim;

        if (item.getId() == 29111) {
            if (player.getAttribOr(AttributeKey.DIVINE_SUPER_COMBAT_POTION_TASK_RUNNING, false)) {
                player.message(Color.RED.wrap("The effect is still running"));
                return true;
            }
            if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_POTIONS.ordinal()]) {
                player.message("Drinks are disabled for this duel.");
                return true;
            }
            if (WildernessArea.inWilderness(player.tile())) {
                player.message("This potions are disabled in wilderness.");
                return true;
            }
            if (player.getTimerRepository().has(TimerKey.POTION))
                return true;

            int curStr = player.skills().xpLevel(Skills.STRENGTH);
            int curAtk = player.skills().xpLevel(Skills.ATTACK);
            int curDef = player.skills().xpLevel(Skills.DEFENCE);
            player.skills().alterSkill(Skills.ATTACK, (int) ((curAtk * 0.1) + 10));
            player.skills().alterSkill(Skills.STRENGTH, (int) ((curStr * 0.1) + 10));
            player.skills().alterSkill(Skills.DEFENCE, (int) ((curDef * 0.1) + 10));
            if (player.getEquipment().contains(4084)) eatAnim = 1469;
            else eatAnim = 829;
            player.putAttrib(AttributeKey.DIVINE_SUPER_COMBAT_POTION_TASK_RUNNING, true);
            player.putAttrib(AttributeKey.DIVINE_SUPER_COMBAT_POTION_EFFECT_ACTIVE, true);
            player.putAttrib(AttributeKey.DIVINE_SUPER_COMBAT_POTION_TICKS, 500);
            DivineSuperCombatPotion.setTimer(player);
            player.getTimerRepository().register(TimerKey.POTION, 3);
            player.animate(eatAnim);
            player.graphic(560);
            return true;
        }


        if (item.getId() == 29112) {
            if (player.getAttribOr(AttributeKey.DIVINE_RANGING_POTION_TASK_RUNNING, false)) {
                player.message(Color.RED.wrap("The effect is still running"));
                return true;
            }
            if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_POTIONS.ordinal()]) {
                player.message("Drinks are disabled for this duel.");
                return true;
            }
            if (WildernessArea.inWilderness(player.tile())) {
                player.message("This potions are disabled in wilderness.");
                return true;
            }


            double rangeChange = 4 + (player.skills().xpLevel(Skills.RANGED) * 10 / 100.0);
            player.skills().alterSkill(Skills.RANGED, (int) rangeChange);
            player.putAttrib(AttributeKey.DIVINE_RANGING_POTION_TASK_RUNNING, true);
            player.putAttrib(AttributeKey.DIVINE_RANGING_POTION_EFFECT_ACTIVE, true);
            player.putAttrib(AttributeKey.DIVINE_RANGING_POTION_TICKS, 500);
            player.getPacketSender().sendEffectTimer((int) Utils.ticksToSeconds(500), EffectTimer.DIVINE_RANGING_POTION);
            DivineRangingPotion.setTimer(player);
            if (player.getEquipment().contains(4084))
                eatAnim = 1469;
            else
                eatAnim = 829;
            player.animate(eatAnim);
            player.graphic(560);
            return true;
        }
        if (item.getId() == 29113) {
            if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_POTIONS.ordinal()]) {
                player.message("Drinks are disabled for this duel.");
                return true;
            }
            if (WildernessArea.inWilderness(player.tile())) {
                player.message("This potions are disabled in wilderness.");
                return true;
            }

            if (player.getTimerRepository().has(TimerKey.POTION)) return true;

            player.animate(829);
            player.getTimerRepository().register(TimerKey.POTION, 3);
            double heal = (int) ((player.skills().xpLevel(Skills.HITPOINTS) * 0.15) + 2);
            double def = (int) ((player.skills().xpLevel(Skills.DEFENCE) * 0.2) + 2);
            int increase = player.getEquipment().hpIncrease();
            player.heal((int) heal, increase > 0 ? increase : 16);
            player.skills().alterSkill(Skills.DEFENCE, (int) def);

            int[] ids = new int[]{Skills.ATTACK, Skills.STRENGTH, Skills.MAGIC, Skills.RANGED};

            for (int i : ids) {
                int lvl = player.skills().xpLevel(i);
                player.skills().alterSkill(i, (int) -(lvl * 0.1) + 2);
            }
            return true;
        }
        if (item.getId() == 29114 && !WildernessArea.inWilderness(player.tile())) {
            if (player.getTimerRepository().has(TimerKey.POTION)) return true;
            player.animate(829);
            player.getTimerRepository().register(TimerKey.POTION, 3);
            for (int i = 0; i < Skills.SKILL_COUNT; i++) {
                if (i != Skills.HITPOINTS) {
                    int current_flat = player.skills().xpLevel(i);
                    double restorable = (int) (current_flat * 0.25 + 8);
                    if (i == Skills.PRAYER) {
                        if (player.inventory().contains(6714) && player.getEquipment().wearingMaxCape()) { // Max cape holy wrench effect
                            if (current_flat > 25 && current_flat <= 85) {
                                restorable += 1;
                            } else if (current_flat > 85) {
                                restorable += 2;
                            }
                        }
                    }
                    player.skills().replenishSkill(i, (int) restorable);
                }
            }
            if (Venom.venomed(player)) {
                Venom.cure(1, player);
            } else {
                Poison.cureAndImmune(player, 24);
                player.message("It grants you immunity from poison for six minutes.");
            }
            return true;

        }
        if (item.getId() == 29115) {
            if (player.<Boolean>getAttribOr(AttributeKey.OVERLOAD_TASK_RUNNING, false)) {
                player.message(Color.RED.wrap("The effect is still running")
                );
                return true;
            }


            if (!player.tile().inRaid1(player) && !player.tile().inRaid2(player) && !player.tile().inArea(AreaConstants.THEATRE)) {
                player.message("You're not able to use overload outside raid");
                return true;
            }
            for (int i = 0; i < 5; i++) {
                if (player.getEquipment().contains(4084))
                    eatAnim = 1469;
                else
                    eatAnim = 829;
                player.animate(eatAnim);
                Chain.bound(null).name("overloadTask").runFn(i * 2, () -> {
                    player.animate(3170);
                    player.graphic(560);

                });
            }
            player.putAttrib(OVERLOAD_POTION, 500);
            player.skills().overloadPlusBoost(Skills.ATTACK);
            player.skills().overloadPlusBoost(Skills.STRENGTH);
            player.skills().overloadPlusBoost(Skills.DEFENCE);
            player.skills().overloadPlusBoost(Skills.RANGED);
            player.skills().overloadPlusBoost(Skills.MAGIC);
            OverloadPotion.apply(player);
            player.getPacketSender().sendEffectTimer((int) Utils.ticksToSeconds(500), EffectTimer.OVERLOAD);

            return true;
        }

        return false;
    }
}
