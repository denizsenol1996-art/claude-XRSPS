package com.twisted.game.content.areas.wilderness.content.larrans_key;

import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.collection_logs.listener.CollectionLogHandler;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.game.content.collection_logs.data.LogType.KEYS;
import static com.twisted.util.CustomItemIdentifiers.*;

/**
 * @author Patrick van Elderen | February, 17, 2021, 14:17
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class LarransChest extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (obj.getId() == 34832) {
            if (player.skills().combatLevel() < 3 && !player.ironMode().ironman()) {
                player.message(Color.RED.wrap("You need to be at least level 126 to open this chest."));
                return true;
            }
            if (player.getInventory().contains(LARRANS_KEY_TIER_I)) {
                if (CollectionLogHandler.rollKeyReward(player, LARRANS_KEY_TIER_I, null, -1, -1)) {
                    return true;
                }
            } else if (player.inventory().contains(LARRANS_KEY_TIER_II)) {
                if (CollectionLogHandler.rollKeyReward(player, LARRANS_KEY_TIER_II, null, -1, -1)) {
                    return true;
                }
            } else if (player.inventory().contains(LARRANS_KEY_TIER_III)) {
                if (CollectionLogHandler.rollKeyReward(player, LARRANS_KEY_TIER_III, null, -1, -1)) {
                    return true;
                }
            } else {
                player.message("This Larran's big chest wont budge, I think I need to find a key that fits.");
            }
            return true;
        }
        return false;
    }

    private static void open(Player player, int key) {
        if (!player.inventory().contains(key)) {
            return;
        }

        player.animate(536);
        player.lock();
        Chain.bound(player).runFn(1, () -> {
            player.inventory().remove(new Item(key, 1), true);
            int roll = Utils.percentageChance(player.extraItemRollChance()) ? 2 : 1;
            for (int i = 0; i < roll; i++) {
                Item reward = LarransKeyLootTable.rewardTables(key);

                if (reward == null)
                    return;

                //Collection logs
                KEYS.log(player, key, reward);

                //Send a world message that someone opened the Larran's chest
                World.getWorld().sendWorldMessage("<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Larran's chest</col>]: " + "<col=1e44b3>" + player.getUsername() + " has just looted the Larran's chest with a Larran's key!");

                //When we receive a rare loot send a world message
                if (reward.getValue() >= 30_000) {
                    boolean amOverOne = reward.getAmount() > 1;
                    String amtString = amOverOne ? "x " + Utils.format(reward.getAmount()) + "" : Utils.getAOrAn(reward.name());
                    String msg = "<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Larran's chest</col>]: " + "<col=1e44b3>" + player.getUsername() + " has received " + amtString + " " + reward.unnote().name() + "!";
                    World.getWorld().sendWorldMessage(msg);
                }
                player.inventory().addOrDrop(reward);
            }

            //Give half a teleblock for tier I and a full for tier II and III when opening the Larran's chest.
            player.teleblock(key == LARRANS_KEY_TIER_I ? 250 : 500, true);

            if (key == LARRANS_KEY_TIER_I) {
                int keysUsed = (Integer) player.getAttribOr(AttributeKey.LARRANS_KEYS_TIER_ONE_USED, 0) + 1;
                player.putAttrib(AttributeKey.LARRANS_KEYS_TIER_ONE_USED, keysUsed);
            }

            if (key == LARRANS_KEY_TIER_II) {
                int keysUsed = (Integer) player.getAttribOr(AttributeKey.LARRANS_KEYS_TIER_TWO_USED, 0) + 1;
                player.putAttrib(AttributeKey.LARRANS_KEYS_TIER_TWO_USED, keysUsed);
            }

            //Tier III also gives out a redskull, high risk high reward!
            if (key == LARRANS_KEY_TIER_III) {
                Skulling.assignSkullState(player, SkullType.RED_SKULL);
                if (player.hasPetOut("Deranged archaeologist")) {
                    Skulling.assignSkullState(player, SkullType.NO_SKULL);
                }

                int keysUsed = (Integer) player.getAttribOr(AttributeKey.LARRANS_KEYS_TIER_THREE_USED, 0) + 1;
                player.putAttrib(AttributeKey.LARRANS_KEYS_TIER_THREE_USED, keysUsed);
            }

            //Update achievements
            AchievementsManager.activate(player, Achievements.LARRANS_LOOTER_I, 1);
            AchievementsManager.activate(player, Achievements.LARRANS_LOOTER_II, 1);
            AchievementsManager.activate(player, Achievements.LARRANS_LOOTER_III, 1);
            player.unlock();
        });
    }
}
