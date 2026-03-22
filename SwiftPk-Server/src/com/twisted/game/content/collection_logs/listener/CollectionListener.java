package com.twisted.game.content.collection_logs.listener;

import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.content.collection_logs.MysteryBoxItem;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Color;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface CollectionListener {
    @NotNull MysteryBoxItem[] rewards();
    String name();
    int id();
    boolean isItem(int id);
    AttributeKey key();
    LogType logType();
    default void openKey(Player player, GameObject object, int oldId, int newId) {
        player.lock();
        player.animate(536);
        player.getInventory().remove(this.id());
        int increment = player.<Integer>getAttribOr(this.key(), 0) + 1;
        Item reward = null;
        reward = getSelectedItem(reward);
        Chain.bound(null).runFn(1, () -> {
            if (object != null) {
                GameObject oldObject = new GameObject(oldId, object.tile(), object.getType(), object.getRotation());
                GameObject newObject = new GameObject(newId, object.tile(), object.getType(), object.getRotation());
                ObjectManager.replace(oldObject, newObject, 2);
            }
            player.unlock();
        });
        boolean isRare = false;
        for (var i : rewards()) {
            if (i.isRare && i.id == reward.getId()) {
                isRare = true;
                break;
            }
        }
        if (!WildernessArea.inWilderness(player.tile())) player.getInventory().addOrBank(reward);
        else player.getInventory().addOrDrop(reward);
        this.logType().log(player, this.id(), reward);
        String name = reward.name();
        if (reward.noted()) name = reward.unnote().name();
        if (isRare) {
            World.getWorld().sendWorldMessage("<img=505><shad=0>[<col=" + Color.YELLOW.getColorValue() + ">" + this.name() + "</col>]</shad>:<col=AD800F> " + "<shad=0>" + Color.ADAMANTITE.wrap(player.getUsername() + " received a ") + "</shad>" + "<shad=0>" + Color.RAID_PURPLE.wrap(name + "!") + "</shad>");
            Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " received a " + name + " from a " + this.name() + ".", "box_and_tickets");
        }
        player.putAttrib(this.key(), increment);
    }

    @NotNull
    private Item getSelectedItem(Item reward) {
        boolean rewardFound = false;
        int totalRarity = 0;
        for (var i : rewards()) totalRarity += i.rarity;
        int randomNumber = World.getWorld().random().nextInt(totalRarity);
        int cumulativeRarity = 0;
        while (!rewardFound) {
            for (var i : rewards()) {
                cumulativeRarity += i.rarity;
                if (randomNumber < cumulativeRarity) {
                    if (i.amount == -1) reward = new Item(i.id);
                    else reward = new Item(i.id, World.getWorld().random(i.amount / 2, i.amount));
                    rewardFound = true;
                    break;
                }
            }
        }
        return reward;
    }

    default void openBox(Player player, boolean isGrandKey, boolean isKeyOfBoxes) {
        Item reward;
        player.getInventory().remove(this.id());
        int increment = player.<Integer>getAttribOr(this.key(), 0) + 1;
        List<Item> rewards = new ArrayList<>();
        int rolls = isGrandKey || isKeyOfBoxes ? 2 : 1;
        if (isGrandKey) player.getInventory().remove(CustomItemIdentifiers.GRAND_KEY);
        if (isKeyOfBoxes) player.getInventory().remove(CustomItemIdentifiers.KEY_OF_BOXES);
        boolean isRare = false;
        for (int rollCount = 0; rollCount < rolls; rollCount++) {
            boolean rewardFound = false;
            while (!rewardFound) {
                for (var i : rewards()) {
                    if (World.getWorld().rollDie(i.rarity, 1)) {
                        if (i.amount == -1) reward = new Item(i.id);
                        else reward = new Item(i.id, World.getWorld().random(i.amount / 2, i.amount));
                        rewards.add(reward);
                        if (i.isRare) isRare = true;
                        rewardFound = true;
                        break;
                    }
                }
            }
        }
        player.putAttrib(this.key(), increment);
        for (Item rolledReward : rewards) {
            if (!WildernessArea.inWilderness(player.tile())) player.getInventory().addOrBank(rolledReward);
            else player.getInventory().addOrDrop(rolledReward);
            this.logType().log(player, this.id(), rolledReward);
            if (isRare) {
                World.getWorld().sendWorldMessage("<img=505><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">" + this.name() + "</col>]</shad>:<col=AD800F> " + player.getUsername() + " received a <shad=0>" + rolledReward.name() + "</shad>!");
                Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " received a " + rolledReward.name() + " from a " + this.name() + ".", "box_and_tickets");
            }
        }
    }
}
