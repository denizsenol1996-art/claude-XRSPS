package com.twisted.game.content.seasonal_events.christmas;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static com.twisted.game.content.seasonal_events.rewards.UnlockEventRewards.UNLOCKED_ITEM_SLOT;
import static com.twisted.game.world.entity.AttributeKey.*;
import static com.twisted.util.CustomItemIdentifiers.SUMMER_TOKEN;
import static com.twisted.util.ObjectIdentifiers.BURNING_BRAZIER_29314;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 12, 2021
 */
public class WinterEvent extends PacketInteraction {

    public static final boolean ENABLED = false;

    private static final Logger logger = LogManager.getLogger(WinterEvent.class);

    @Override
    public boolean handleButtonInteraction(Player player, int button) {
        if (button == 73307) {
            player.optionsTitled("Exchange your 2,500 Summer tokens for a reward?", "Yes", "No", () -> {
                if (!player.inventory().contains(new Item(SUMMER_TOKEN, 2500))) {
                    return;
                }

                final List<AttributeKey> keyList = Arrays.asList(EVENT_REWARD_1_CLAIMED, EVENT_REWARD_2_CLAIMED, EVENT_REWARD_3_CLAIMED, EVENT_REWARD_4_CLAIMED, EVENT_REWARD_5_CLAIMED, EVENT_REWARD_6_CLAIMED, EVENT_REWARD_7_CLAIMED, EVENT_REWARD_8_CLAIMED, EVENT_REWARD_9_CLAIMED, EVENT_REWARD_10_CLAIMED, EVENT_REWARD_11_CLAIMED, EVENT_REWARD_12_CLAIMED, EVENT_REWARD_13_CLAIMED, EVENT_REWARD_14_CLAIMED, EVENT_REWARD_15_CLAIMED, EVENT_REWARD_16_CLAIMED, EVENT_REWARD_17_CLAIMED, EVENT_REWARD_18_CLAIMED, EVENT_REWARD_19_CLAIMED, EVENT_REWARD_20_CLAIMED, EVENT_REWARD_21_CLAIMED, EVENT_REWARD_22_CLAIMED, EVENT_REWARD_23_CLAIMED, EVENT_REWARD_24_CLAIMED, EVENT_REWARD_25_CLAIMED, EVENT_REWARD_26_CLAIMED, EVENT_REWARD_27_CLAIMED, EVENT_REWARD_28_CLAIMED, EVENT_REWARD_29_CLAIMED, EVENT_REWARD_30_CLAIMED, EVENT_REWARD_31_CLAIMED, EVENT_REWARD_32_CLAIMED, EVENT_REWARD_33_CLAIMED, EVENT_REWARD_34_CLAIMED, EVENT_REWARD_35_CLAIMED, EVENT_REWARD_36_CLAIMED, EVENT_REWARD_37_CLAIMED, EVENT_REWARD_38_CLAIMED, EVENT_REWARD_39_CLAIMED, EVENT_REWARD_40_CLAIMED, EVENT_REWARD_41_CLAIMED, EVENT_REWARD_42_CLAIMED, EVENT_REWARD_43_CLAIMED, EVENT_REWARD_44_CLAIMED);
                var unlockedAllRewards = keyList.stream().allMatch(key -> player.<Boolean>getAttribOr(key, false));
                if (unlockedAllRewards) {
                    player.message(Color.RED.wrap("You have already unlocked all rewards."));
                    return;
                }

                Item reward = player.getEventRewards().generateReward();
                if (reward == null) {
                    return;
                }

                System.out.println("Reward: " + reward.getId() + " Amount: " + reward.getAmount());
                if (player.inventory().contains(new Item(SUMMER_TOKEN, 2500))) {
                    player.getEventRewards().refreshItems();
                    player.getPacketSender().sendItemOnInterfaceSlot(UNLOCKED_ITEM_SLOT, reward.copy(),0);
                    player.getEventRewards().rollForReward(SUMMER_TOKEN, 2500, reward.copy());
                    System.out.println("Reward: " + reward.getId() + " Amount: " + reward.getAmount());
                } else {
                    player.message(Color.RED.wrap("You do not have enough Summer tokens to roll for a reward."));
                }
            });
            return true;
        }
        if (button == 73310) {
            player.getEventRewards().reset();
            return true;
        }
        return false;
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (option == 1) {
            if(object.getId() == BURNING_BRAZIER_29314) {
                player.getEventRewards().open();
                return true;
            }
        }
        return false;
    }
}
