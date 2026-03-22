package com.twisted.game.content.consumables.potions.impl;

import com.twisted.game.content.EffectTimer;
import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Color;

/**
 * @author Patrick van Elderen | May, 24, 2021, 15:38
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class OverloadPotion {

    public static void apply(Player player) {
        player.putAttrib(AttributeKey.OVERLOAD_TASK_RUNNING, true);
        TaskManager.submit(new Task("OverloadTask", 1, false) {

            @Override
            protected void execute() {
                int ticks = player.getAttribOr(AttributeKey.OVERLOAD_POTION, 0);

                if (!player.isRegistered() || player.dead() || ticks == 0 || WildernessArea.inWilderness(player.tile())) {
                    stop();
                    player.skills().resetStats();
                    player.clearAttrib(AttributeKey.OVERLOAD_TASK_RUNNING);
                    player.message("Your overload effect wears off upon entering the wilderness.");
                    return;
                }


                if (ticks > 0) {
                    if (!player.getDueling().inDuel()) {
                        if (!player.dead()) {
                            if (ticks % 25 == 0) {
                                player.skills().overloadPlusBoost(Skills.ATTACK);
                                player.skills().overloadPlusBoost(Skills.STRENGTH);
                                player.skills().overloadPlusBoost(Skills.DEFENCE);
                                player.skills().overloadPlusBoost(Skills.RANGED);
                                player.skills().overloadPlusBoost(Skills.MAGIC);
                            } else {
                                return;
                            }
                        }
                    }

                    if (ticks == 0) {
                        player.message(Color.RED.tag() + "Your overload potion has expired.");
                        player.putAttrib(AttributeKey.OVERLOAD_POTION, 0);
                        player.clearAttrib(AttributeKey.OVERLOAD_TASK_RUNNING);
                        player.getPacketSender().sendEffectTimer(0, EffectTimer.OVERLOAD);
                        player.hp(Math.max(player.skills().level(Skills.HITPOINTS), 50), 20);
                        player.skills().replenishStats();
                        player.healPlayer();
                        stop();
                    } else {
                        player.putAttrib(AttributeKey.OVERLOAD_POTION, ticks);
                        if (ticks == 3) {
                            player.message(Color.RED.tag() + "Your overload potion is about to expire.");
                        }
                    }
                }
            }
        });
    }
}
