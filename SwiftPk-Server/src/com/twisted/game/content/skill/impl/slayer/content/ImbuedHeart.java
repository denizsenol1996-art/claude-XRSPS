package com.twisted.game.content.skill.impl.slayer.content;

import com.twisted.game.content.EffectTimer;
import com.twisted.game.content.consumables.potions.impl.OverloadPotion;
import com.twisted.game.content.duel.DuelRule;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.Utils;
import com.twisted.util.timers.TimerKey;

import static com.twisted.game.world.entity.AttributeKey.OVERLOAD_POTION;

public class ImbuedHeart extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        return switch (item.getId()) {
            case ItemIdentifiers.IMBUED_HEART -> {
                if (option == 1) {
                    activate(player, Skills.MAGIC);
                    yield true;
                }
                yield false;
            }
            case CustomItemIdentifiers.IMBUED_HEART_MELEE -> {
                if (option == 1) {
                    activate(player, Skills.ATTACK);
                    activate(player, Skills.STRENGTH);
                    activate(player, Skills.DEFENCE);
                    yield true;
                }
                yield false;
            }
            case CustomItemIdentifiers.IMBUED_HEART_RANGE -> {
                if (option == 1) {
                    activate(player, Skills.RANGED);
                    yield true;
                }
                yield false;
            }
            case CustomItemIdentifiers.IMBUED_HEART_OVERLOAD -> {
                if (option == 1) {
                    player.putAttrib(OVERLOAD_POTION, 500);
                    player.getSkills().overloadPlusBoost(Skills.ATTACK);
                    player.getSkills().overloadPlusBoost(Skills.STRENGTH);
                    player.getSkills().overloadPlusBoost(Skills.DEFENCE);
                    player.getSkills().overloadPlusBoost(Skills.RANGED);
                    player.getSkills().overloadPlusBoost(Skills.MAGIC);
                    OverloadPotion.apply(player);
                    int timer = (int) Utils.ticksToSeconds(500);
                    player.getPacketSender().sendEffectTimer(timer, EffectTimer.OVERLOAD);
                    yield true;
                }

                yield false;
            }
            default -> false;
        };
    }

    public static void activate(Player player, int skill) {
        if (player.getTimerRepository().has(TimerKey.IMBUED_HEART_COOLDOWN)) {
            int ticks = player.getTimerRepository().left(TimerKey.IMBUED_HEART_COOLDOWN);

            if (ticks >= 100) {
                int minutes = ticks / 100;
                player.message("The heart is still drained of its power. Judging by how it feels, it will be ready in around "+minutes+" minutes.");
            } else {
                int seconds = ticks / 10 * 6;
                player.message("The heart is still drained of its power. Judging by how it feels, it will be ready in around "+seconds+" seconds.");
            }
        } else {
            if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_POTIONS.ordinal()]) {
                player.message("Stat-boosting items are disabled for this duel.");
            } else {
                player.message("<col="+Color.RED.getColorValue()+">Your imbued heart has regained its magical power.");
                player.graphic(1316, 0, 30);
                player.getTimerRepository().register(TimerKey.IMBUED_HEART_COOLDOWN, 700);
                int seconds = 700 / 10 * 6;
                player.getPacketSender().sendEffectTimer(seconds, EffectTimer.IMBUED_HEART);
                int boost = 1 + (player.getSkills().xpLevel(skill) / 10);
                if (player.getSkills().levels()[skill] == player.getSkills().xpLevel(skill)) {
                    player.getSkills().alterSkill(skill, boost);
                }
            }
        }
    }
}
