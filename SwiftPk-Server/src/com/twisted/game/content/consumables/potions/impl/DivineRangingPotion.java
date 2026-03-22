package com.twisted.game.content.consumables.potions.impl;

import com.twisted.game.content.EffectTimer;
import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.Color;
import com.twisted.util.Utils;

/**
 * @author Patrick van Elderen | November, 28, 2020, 13:19
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class DivineRangingPotion {

    public static void onLogin(Player me) {
        setTimer(me);
    }

    public static void setTimer(Player player) {
        player.putAttrib(AttributeKey.DIVINE_RANGING_POTION_TASK_RUNNING, true);
        TaskManager.submit(new Task("DivineRangingPotionTask", 1, false) {

            @Override
            protected void execute() {
                int ticks = player.<Integer>getAttribOr(AttributeKey.DIVINE_RANGING_POTION_TICKS, 0);
                boolean potionEffectActive = player.getAttribOr(AttributeKey.DIVINE_RANGING_POTION_EFFECT_ACTIVE, false);

                if(!player.isRegistered() || player.dead() || ticks == 0) {
                    stop();
                    player.clearAttrib(AttributeKey.DIVINE_RANGING_POTION_TASK_RUNNING);
                    return;
                }
                if (!player.getDueling().inDuel()) {//up02
                    if (!player.dead()) {
                        if (ticks % 25 == 0) {
                            double rangeChange = 4 + (player.skills().xpLevel(Skills.RANGED) * 10 / 100.0);
                            player.skills().alterSkill(Skills.RANGED, (int) rangeChange);
                        }
                    }

                }

                if(potionEffectActive) {
                    player.getPacketSender().sendEffectTimer((int) Utils.ticksToSeconds(ticks), EffectTimer.DIVINE_RANGING_POTION);
                    ticks--;
                    player.putAttrib(AttributeKey.DIVINE_RANGING_POTION_TICKS, ticks--);
                    if(ticks == 0) {
                        player.putAttrib(AttributeKey.DIVINE_RANGING_POTION_TASK_RUNNING, false);
                        player.putAttrib(AttributeKey.DIVINE_RANGING_POTION_EFFECT_ACTIVE, false);
                        player.putAttrib(AttributeKey.DIVINE_RANGING_POTION_TICKS, 0);
                        player.message(Color.RED.tag() + "Your divine ranging potion has expired.");
                        stop();
                    }
                }
            }
        });
    }
}
