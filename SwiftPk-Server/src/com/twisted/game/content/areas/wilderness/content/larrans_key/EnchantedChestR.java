package com.twisted.game.content.areas.wilderness.content.larrans_key;

import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.collection_logs.listener.CollectionLogHandler;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.game.content.collection_logs.data.LogType.KEYS;
import static com.twisted.util.CustomItemIdentifiers.*;

public class EnchantedChestR extends PacketInteraction {

    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {//leave it like it was until i fix it ok?u can update now legend
        if (obj.getId() == 4121) {
            if (!WildernessArea.inDiamondZone(player.tile()) && player.inventory().contains(ENCHANTED_KEY_I)) {
                player.teleblock( 250, true);
                Skulling.assignSkullState(player, SkullType.WHITE_SKULL);
            }
            if (player.hasPetOut("Deranged archaeologist")) {
                Skulling.assignSkullState(player, SkullType.NO_SKULL);
            }
            if (player.inventory().contains(ENCHANTED_KEY_I)) {
                if (CollectionLogHandler.rollKeyReward(player, ENCHANTED_KEY_I, null, -1, -1)) {
                    return true;
                }
            } else {
                player.message("This enchanted chest wont budge, I think I need to find a key that fits.");
            }
            return true;
        }

        return false;
    }
    private static void open(Player player, int key) {
        if(!player.inventory().contains(key)) {
            return;
        }

        player.animate(536);
        player.lock();
        Chain.bound(player).runFn(1, () -> {
            player.inventory().remove(new Item(key, 1), true);
            int roll = Utils.percentageChance(player.extraItemRollChance()) ? 2 : 1;
            for (int i = 0; i < roll; i++) {
                Item reward = EnchantedChestRLootTable.rewardTables(key);

                if (reward == null)
                    return;

                //Collection logs
                KEYS.log(player, key, reward);
                player.inventory().addOrDrop(reward);
            }

            if (key == ENCHANTED_KEY_I) {
                int keysUsed = (Integer) player.getAttribOr(AttributeKey.ENCHANTED_KEYS_R_OPENED, 0) + 1;
                player.putAttrib(AttributeKey.ENCHANTED_KEYS_R_OPENED, keysUsed);
            }

            //Update achievements
            AchievementsManager.activate(player, Achievements.ENCHANTED_LOOTER_R_I, 1);
            AchievementsManager.activate(player, Achievements.ENCHANTED_LOOTER_R_II, 1);
            AchievementsManager.activate(player, Achievements.ENCHANTED_LOOTER_R_III, 1);
            player.unlock();
        });
    }
}
