package com.twisted.game.content.collection_logs;

import com.twisted.game.content.collection_logs.data.Collection;
import com.twisted.game.content.collection_logs.data.CollectionLogUtility;
import com.twisted.game.content.collection_logs.data.LogType;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.Color;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.twisted.game.content.collection_logs.data.CollectionLogUtility.*;

/**
 * @author PVE
 * @Since juli 15, 2020
 */
public class CollectionLog {

    public static final int RAIDS_KEY = 100_000;
    public static final int TOB_RAIDS_KEY = 100_002;

    private final Player player;

    public CollectionLog(Player player) {
        this.player = player;
    }

    public HashMap<Collection, ArrayList<Item>> collectionLog = new HashMap<>();

    public int totalObtained(Collection collection) {
        int obtained = 0;
        Item[] items = collection.getObtainables();
        var obtainedItems = collectionLog.get(collection);
        for (Item item : items) {
            if (obtainedItems != null) {
                int amount = obtainedItems.stream().filter(i -> i.getId() == item.getId()).findAny().orElse(new Item(0, 0)).getAmount();
                if (amount > 0)
                    obtained++;
            }
        }
        return obtained;
    }

    public void claimReward(Collection collection) {
        var logToCheck = player.<Collection>getAttribOr(AttributeKey.VIEWING_COLLECTION_LOG, null);
        String name = logToCheck.getRewardClaimedKey().toString();

        //Check if we're viewing the proper collection
        if (collection == logToCheck) {

            //The total collectables
            final int totalCollectables = collection.totalCollectables();

            //Check if we have collected every item on the log
            if (totalCollectables == totalObtained(logToCheck)) {
                var alreadyClaimed = player.<Boolean>getAttribOr(logToCheck.getRewardClaimedKey(), false);

                //Check if we've already claimed the reward
                if (player.isCollectionClaimed(name)) {
                    player.message(Color.RED.wrap("You have already claimed this collection log reward."));
                    return;
                }

                //Passed all checks claim the reward
                player.claimedCollectionLog.add(name);
                player.putAttrib(logToCheck.getRewardClaimedKey(), true);
                player.inventory().addOrBank(logToCheck.getReward());
                player.message(Color.PURPLE.wrap("You have collected the reward for completing " + Color.BLUE.wrap(logToCheck.getName()) + " " + Color.PURPLE.wrap("collection log.")));
            } else {
                //Does not meet the requirements
                player.message(Color.RED.wrap("This collection log is not yet complete."));
            }
        }
    }

    public void sendInterface(Collection collection) {
        int obtained = 0;
        final int obtainables = collection.totalCollectables();
        final int kills = player.getAttribOr(collection.getAttributeKey(), 0);
        Item[] items = collection.getObtainables();
        var obtainedItems = collectionLog.get(collection);
        for (Item item : items) {
            item.setAmount(0);
            if (obtainedItems != null) {
                int amount = obtainedItems.stream().filter(i -> i.getId() == item.getId()).findAny().orElse(new Item(0, 0)).getAmount();
                if (amount > 0)
                    obtained++;
                item.setAmount(amount);
            }
        }
        player.getPacketSender().sendScrollbarHeight(54150, (int) (Collection.getAsList(collection.getLogType()).size() * 15.1));
        player.getPacketSender().sendScrollbarHeight(SCROLL_BAR, items.length * 7 + 2);
        player.getPacketSender().sendString(NAME_STRING, "<col=" + Color.LIGHTORANGE.getColorValue() + ">" + collection.getName());
        player.getPacketSender().sendString(OBTAINED_STRING, "<col=" + Color.LIGHTORANGE.getColorValue() + ">Obtained: " + getColor(obtained, obtainables) + obtained + "/" + obtainables);
        String plural = collection.getLogType() == LogType.MYSTERY_BOX || collection.getLogType() == LogType.KEYS ? "Opened: " : "Kills: ";
        player.getPacketSender().sendString(KILLS_STRING, "<col=" + Color.LIGHTORANGE.getColorValue() + "> " + collection.getName() + " " + "" + plural + "<col=" + Color.WHITE.getColorValue() + ">" + kills);
        player.getPacketSender().sendItemOnInterface(ITEM_CONTAINER, items);
        player.getPacketSender().sendItemOnInterface(REWARD_ITEM_CONTAINER, collection.getReward());
        player.putAttrib(AttributeKey.VIEWING_COLLECTION_LOG, collection);
    }

    public void open(LogType logType) {
        final List<Collection> log = Collection.getAsList(logType);
        final int total = log.size();
        for (int index = 0; index < 60; index++) {
            //Clear old data first
            player.getPacketSender().sendString(NAMES_STARTING_LINE + index, "");
        }
        for (int index = 0; index < total; index++) {
            int obtained = 0;
            final int obtainables = log.get(index).totalCollectables();
            final int kills = player.getAttribOr(log.get(index).getAttributeKey(), 0);
            Item[] items = log.get(index).getObtainables();
            var obtainedItems = collectionLog.get(log.get(index));
            for (Item item : items) {
                item.setAmount(0);
                if (obtainedItems != null) {
                    int amount = obtainedItems.stream().filter(i -> i.getId() == item.getId()).findAny().orElse(new Item(0, 0)).getAmount();
                    if (amount > 0)
                        obtained++;
                    item.setAmount(amount);
                }
            }
            if(obtained >= obtainables) {
                player.getPacketSender().sendString((CollectionLogUtility.NAMES_STARTING_LINE) + index, "<col=" + Color.GREEN.getColorValue() + ">" + log.get(index).getName() + "</col>");
            } else {
                player.getPacketSender().sendString((CollectionLogUtility.NAMES_STARTING_LINE) + index, log.get(index).getName());
            }
        }
        sendInterface(log.get(0));
        player.putAttrib(AttributeKey.COLLECTION_LOG_OPEN, logType);
        player.getInterfaceManager().open(INTERFACE);
    }

    private String getColor(int obtained, int max) {
        return obtained == 0 ? "<col=" + Color.RED.getColorValue() + ">" : obtained >= max ? "<col=" + Color.GREEN.getColorValue() + ">" : "<col=" + Color.YELLOW.getColorValue() + ">";
    }

    private static final Object2ObjectMap<Collection, Item> unlockableItems = new Object2ObjectOpenHashMap<>();

    public Object2ObjectMap<Collection, Item> getUnlockableItems() {
        return unlockableItems;
    }

    static {
        for (Collection collection : Collection.values()) {
            for (Item item : collection.getObtainables()) {
                unlockableItems.put(collection, item);
            }
        }
    }

    public int unlocked(int id) {
        int[] unlocked = new int[] {-1};//default -1
        player.getCollectionLog().collectionLog.forEach((key, value) -> {
            if (unlocked[0] == 1) return; // continue@foreach
            Item[] items = key.getObtainables();
            for (Item item : items) {
                if (id == item.getId()) {
                    if (value.isEmpty()) {
                        unlocked[0] = 1; // cant wear
                        break;
                    }
                    int amount = value.stream().filter(i -> i.getId() == item.getId()).findAny().orElse(new Item(0, 0)).getAmount();
                    if (amount <= 0) {
                        unlocked[0] = 1; // cant wear
                        break;
                    }
                }
            }
        });

        return unlocked[0];
    }
}
