package com.twisted.game.content;

import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.collection_logs.listener.CollectionLogHandler;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.Utils;

import static com.twisted.game.content.collection_logs.data.LogType.KEYS;
import static com.twisted.util.ItemIdentifiers.CRYSTAL_KEY;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 18, 2020
 */
public class CrystalKey extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == ItemIdentifiers.LOOP_HALF_OF_KEY && usedWith.getId() == ItemIdentifiers.TOOTH_HALF_OF_KEY) || (use.getId() == ItemIdentifiers.TOOTH_HALF_OF_KEY && usedWith.getId() == ItemIdentifiers.LOOP_HALF_OF_KEY)) {
            player.inventory().remove(new Item(ItemIdentifiers.LOOP_HALF_OF_KEY), true);
            player.inventory().remove(new Item(ItemIdentifiers.TOOTH_HALF_OF_KEY), true);
            player.inventory().add(new Item(CRYSTAL_KEY), true);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if (object.getId() == 172 && option == 1) {
            if (player.getInventory().contains(CRYSTAL_KEY)) {
                if (CollectionLogHandler.rollKeyReward(player, CRYSTAL_KEY, object, 172, 173)) {
                    return true;
                }
            }
            player.message("You need a crystal key to open this chest.");
            return true;
        }
        return false;
    }

    @Override
    public boolean handleItemOnObject(Player player, Item item, GameObject object) {
        if (object.getId() == 172) {
            if (player.getInventory().contains(CRYSTAL_KEY)) {
                if (CollectionLogHandler.rollKeyReward(player, CRYSTAL_KEY, object, 172, 173)) {
                    return true;
                }
            }
            player.message("You need a crystal key to open this chest.");
            return true;
        }
        return false;
    }

    private enum PvpRewards {
        FIRST(80, new Item[]{new Item(ItemIdentifiers.BLOOD_MONEY, 15_000)}),
        SECOND(60, new Item[]{new Item(CustomItemIdentifiers.BLOOD_MONEY_CASKET, 1)}),
        THIRD(50, new Item[]{new Item(ItemIdentifiers.DRAGONFIRE_SHIELD, 1)}),
        FOURTH(40, new Item[]{new Item(CustomItemIdentifiers.ARMOUR_MYSTERY_BOX, 1)}),
        FIFTH(30, new Item[]{new Item(CustomItemIdentifiers.WEAPON_MYSTERY_BOX, 1)}),
        SIXTH(20, new Item[]{new Item(CustomItemIdentifiers.DONATOR_MYSTERY_BOX, 1)}),
        EIGHT(10, new Item[]{new Item(ItemIdentifiers.BERSERKER_RING_I, 1)}),
        NINTH(8, new Item[]{new Item(ItemIdentifiers.SEERS_RING_I, 1)}),
        TENTH(7, new Item[]{new Item(ItemIdentifiers.ARCHERS_RING_I, 1)}),
        ELEVENTH(6, new Item[]{new Item(ItemIdentifiers.ARMADYL_CROSSBOW, 1)}),
        TWELFTH(5, new Item[]{new Item(ItemIdentifiers.TOXIC_STAFF_OF_THE_DEAD, 1)}),
        THIRTEENTH(2, new Item[]{new Item(ItemIdentifiers.ARMADYL_GODSWORD, 1)}),
        FOURTEENTH(1, new Item[]{new Item(ItemIdentifiers.DRAGON_CLAWS, 1)});

        private final int chance;
        private final Item[] rewards;

        PvpRewards(int chance, Item[] rewards) {
            this.chance = chance;
            this.rewards = rewards;
        }
    }

    private static void reward(Player player) {
        Item[] rewards = generateReward();

        Item drop = null;

        for (Item item : rewards) {
            player.getInventory().addOrDrop(item);
            KEYS.log(player, CRYSTAL_KEY, item);
            drop = item;
        }

        if (drop != null) {
            //The user box test doesn't yell.
            if (player.getUsername().equalsIgnoreCase("Box test")) {
                return;
            }
            System.out.println(drop.toString());
            World.getWorld().sendWorldMessage("<img=1875>" + player.getUsername() + " received <col=A30072>" + drop.name() + " </col>from the crystal key.");
        }

        int keysUsed = player.<Integer>getAttribOr(AttributeKey.CRYSTAL_KEYS_OPENED, 0) + 1;
        player.putAttrib(AttributeKey.CRYSTAL_KEYS_OPENED, keysUsed);

        player.message("You find some treasure in the chest!");
        AchievementsManager.activate(player, Achievements.CRYSTAL_LOOTER_I, 1);
        AchievementsManager.activate(player, Achievements.CRYSTAL_LOOTER_II, 1);
        AchievementsManager.activate(player, Achievements.CRYSTAL_LOOTER_III, 1);
    }

    private static Item[] generateReward() {
        int randomReward = Utils.random(80);
        Item[] rewardItem;
        for (PvpRewards reward : PvpRewards.values()) {
            if (reward.chance <= randomReward) {
                return reward.rewards;
            }
        }
        rewardItem = PvpRewards.FIRST.rewards;
        return rewardItem;
    }

}
