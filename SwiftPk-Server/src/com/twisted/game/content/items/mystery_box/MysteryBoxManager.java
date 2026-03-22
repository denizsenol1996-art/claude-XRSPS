package com.twisted.game.content.items.mystery_box;

import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import java.util.*;

import static com.twisted.game.content.collection_logs.data.LogType.MYSTERY_BOX;
import static com.twisted.game.world.entity.AttributeKey.MBOX_REWARDS_VISIBLE;
import static com.twisted.game.world.entity.AttributeKey.TOTAL_RARES_FROM_MYSTERY_BOX;
import static com.twisted.util.CustomItemIdentifiers.KEY_OF_BOXES;

/**
 * This class represents functionality for the mystery box spinner.
 *
 * @author Shadowrs/Jak tardisfan121@gmail.com
 * @author Patrick van Elderen | November, 21, 2020, 19:51
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class MysteryBoxManager {

    public static final int MAIN_INTERFACE_ID = 71000;
    private static final int MYSTERY_BOX_TYPE_STRING_ID = 71009;
    private static final int POSSIBLE_REWARDS_CONTAINER_ID = 71202;
    private static final int REWARD_STRIP_CONTAINER_ID = 71200;
    private static final int SPINS_LEFT_ID = 71015;
    private static final int TOTAL_RARES_WON_ID = 71017;
    private static final int TOTAL_MYSTERY_BOXES_OPENED = 71019;

    private final Player player;
    private int count;
    private boolean isSpinning = false;
    public Item reward;
    public MysteryBox box;
    public boolean broadcast = false;

    public boolean isSpinning() {
        return isSpinning;
    }

    public MysteryBoxManager(Player player) {
        this.player = player;
    }

    public boolean onItemonItem(Item use, Item with) {
        Optional<MysteryBox> mBox = MysteryBox.getMysteryBox(with.getId());

        if (mBox.isEmpty()) {
            return false;
        }

        box = mBox.get();

        if (use.getId() == KEY_OF_BOXES && with.getId() == box.mysteryBoxId()) {
            player.inventory().remove(new Item(KEY_OF_BOXES), true);
            player.inventory().remove(new Item(box.mysteryBoxId(), 1), true);
            int rolls = 2;
            for (int i = 0; i < rolls; i++) {
                MboxItem mboxItem = box.rollReward().copy();
                reward = mboxItem; // we roll here! and its shows visually later
                broadcast = mboxItem.broadcastItem;
                reward();
            }
            return true;
        }
        return false;
    }

    public boolean open(Item item) {
        Optional<MysteryBox> mBox = MysteryBox.getMysteryBox(item.getId());

        if (mBox.isEmpty()) {
            return false;
        }

        if (WildernessArea.inWilderness(player.tile())) {
            DialogueManager.sendStatement(player, "You can not open a mystery box while in the wilderness!");
            return true;
        }

        if (CombatFactory.inCombat(player)) {
            DialogueManager.sendStatement(player, "You can not open a mystery box while in combat!");
            return true;
        }

        if (player.busy() && !player.getInterfaceManager().isInterfaceOpen(MAIN_INTERFACE_ID)) {
            DialogueManager.sendStatement(player, "You can not open a mystery box right now!");
            return true;
        }

        player.getInterfaceManager().open(MAIN_INTERFACE_ID);

        box = mBox.get();

        //Update strings
        count = player.inventory().count(box.mysteryBoxId());
        player.getPacketSender().sendString(SPINS_LEFT_ID, Integer.toString(count));
        player.getPacketSender().sendString(TOTAL_RARES_WON_ID, Integer.toString(player.getAttribOr(TOTAL_RARES_FROM_MYSTERY_BOX, 0)));
        player.getPacketSender().sendString(TOTAL_MYSTERY_BOXES_OPENED, Integer.toString(player.getAttribOr(box.key(), 0)));

        //Load up all possible rewards
        Item[] allPossibleRewards = mBox.get().allPossibleRewards();

        //Before we shuffle write them in the possible rewards container first
        player.getPacketSender().sendItemOnInterface(POSSIBLE_REWARDS_CONTAINER_ID, allPossibleRewards);

        //When the mystery box has below 50 items duplicate the items.
        int sizing = 1;
        int size = allPossibleRewards.length;
        if (size < 50) {
            ArrayList<MboxItem> mboxItems = new ArrayList<>(Arrays.asList(mBox.get().allPossibleRewards()));
            // start with normal list, expand again and again until we hit >50 items
            for (int i = 0; i < sizing; i++) {
                mboxItems.addAll(Arrays.asList(mBox.get().allPossibleRewards()));
            }
            allPossibleRewards = mboxItems.toArray(new MboxItem[0]);
        }

        player.putAttrib(MBOX_REWARDS_VISIBLE, new ArrayList<>(Arrays.asList(allPossibleRewards)));

        //Get all rewards here
        List<Item> rewards = player.getAttribOr(MBOX_REWARDS_VISIBLE, new ArrayList<>());

        //Now we can shuffle
        Collections.shuffle(rewards);

        player.getPacketSender().sendItemOnInterface(REWARD_STRIP_CONTAINER_ID, Collections.singletonList(new Item(-1, -1)));
        player.getPacketSender().sendItemOnInterface(REWARD_STRIP_CONTAINER_ID, rewards);
        player.getPacketSender().sendString(MYSTERY_BOX_TYPE_STRING_ID, mBox.get().name());
        return true;
    }

    public void spin() {
        if (box == null) {
            player.getInterfaceManager().close();
            return;
        }

        if (isSpinning) {
            player.message("You must wait until the current spin is finished.");
            return;
        }

        if (player.inventory().count(box.mysteryBoxId()) == 0) {
            DialogueManager.sendStatement(player, "You do not have any " + box.name() + "!", "To spin a different mystery box level click on the item first!");
            return;
        }

        if (!player.getInterfaceManager().isInterfaceOpen(MAIN_INTERFACE_ID)) {
            return;
        }

        player.inventory().remove(new Item(box.mysteryBoxId(),1));

        MboxItem mboxItem = box.rollReward().copy();
        reward = mboxItem; // we roll here! and its shows visually later
        broadcast = mboxItem.broadcastItem;
        player.getPacketSender().sendItemOnInterfaceSlot(REWARD_STRIP_CONTAINER_ID, reward.getId(), reward.getAmount(), 41);

        isSpinning = true;
        player.getPacketSender().mysteryBoxSpinner();
        player.lock();

        //Reward
        Chain.bound(null).cancelWhen(() -> !isSpinning).runFn(9, this::reward);
    }

    public void onClose(boolean spinning) {
        if (spinning) {
            reward();
        }
        isSpinning = false;
    }

    public void reward() {
        //Set new count calculate current count and remove 1
        this.count = this.count - 1;
        player.getPacketSender().sendString(SPINS_LEFT_ID, Integer.toString(count));

        //System.out.println("reward: "+reward.toShortString());

        //Add the reward in the correct slot
        player.inventory().addOrBank(reward);

        //System.out.println("Reward from mbox: "+reward.toShortString());

        //Increase achievements
        AchievementsManager.activate(player, Achievements.WHATS_IN_THE_BOX_I, 1);
        AchievementsManager.activate(player, Achievements.WHATS_IN_THE_BOX_II, 1);
        AchievementsManager.activate(player, Achievements.WHATS_IN_THE_BOX_III, 1);

        //Increase total mystery boxes opened
        int mysteryBoxesOpened = (Integer) player.getAttribOr(box.key(), 0) + 1;
        player.putAttrib(box.key(), mysteryBoxesOpened);
        player.getPacketSender().sendString(TOTAL_MYSTERY_BOXES_OPENED, Integer.toString(player.getAttribOr(box.key(), 0)));

        //The reward amount
        int amount = reward.getAmount();
        String plural = amount > 1 ? "x" + amount : "x1";

        player.message("<shad=0>" + Color.ORANGE_2.tag() + "Congratulations, you have received " + plural + " " + reward.unnote().name() + "!</shad>");

        boolean sendWorldMessage = broadcast && !player.getUsername().equalsIgnoreCase("Box test");
        if (sendWorldMessage) {
            String worldMessage = "<img=505><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">" + box.name() + "</col>]</shad>:<col=AD800F> " + player.getUsername() + " received " + plural + " <shad=0>" + reward.unnote().name() + "</shad>!";
            World.getWorld().sendWorldMessage(worldMessage);

            int raresReceived = (Integer) player.getAttribOr(TOTAL_RARES_FROM_MYSTERY_BOX, 0) + 1;
            player.putAttrib(TOTAL_RARES_FROM_MYSTERY_BOX, raresReceived);
            player.getPacketSender().sendString(TOTAL_RARES_WON_ID, Integer.toString(player.getAttribOr(TOTAL_RARES_FROM_MYSTERY_BOX, 0)));
        }

        //When the reward isn't null increase the collection logs
        if (reward != null) {
            MYSTERY_BOX.log(player, box.mysteryBoxId(), reward);
            Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " received "+plural+" "+reward.unnote().name()+" from a "+box.name()+".", "box_and_tickets");
        }

        //Done spinning lets unlock
        isSpinning = false;
        player.unlock();
    }

    public boolean onButton(int button) {
        if (button == 71011) {
            player.getPacketSender().sendURL("#");
            return true;
        }

        if (button == 71003) {
            player.getMysteryBox().spin();
            return true;
        }

        if (button == 71020) {
            player.getInterfaceManager().close();
            return true;
        }

        if (button == 71021) {
            if (player.inventory().contains(box.mysteryBoxId())) {
                player.inventory().remove(new Item(box.mysteryBoxId(),1), true);
                MboxItem mboxItem = box.rollReward().copy();
                reward = mboxItem;
                broadcast = mboxItem.broadcastItem;
                reward();
            } else {
                player.message("You have no more " + box.name() + " remaining.");
            }
            return true;
        }
        return false;
    }
}
