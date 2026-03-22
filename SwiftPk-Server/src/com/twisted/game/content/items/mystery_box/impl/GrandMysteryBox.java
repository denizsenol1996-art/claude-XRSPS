package com.twisted.game.content.items.mystery_box.impl;

import com.twisted.GameServer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.Utils;

import java.util.Arrays;

import static com.twisted.game.content.items.mystery_box.impl.DonatorMysteryBox.DONATOR_MYSTERY_BOX;
import static com.twisted.util.CustomItemIdentifiers.EPIC_PET_BOX;
import static com.twisted.util.CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date februari 08, 2020 13:01
 */
public class GrandMysteryBox extends PacketInteraction {

    public static final int GRAND_MYSTERY_BOX = 16455;

    /**
     * This option gives 2 legendary mystery boxes
     * This is the base reward
     */
    private static final Item[] OPTION_ONE = {new Item(LEGENDARY_MYSTERY_BOX), new Item(LEGENDARY_MYSTERY_BOX)};

    /**
     * This option gives 2 legendary mystery box and 2 donator mystery box
     * There is a 45% chance that you get this option.
     */
    private static final Item[] OPTION_TWO = {new Item(LEGENDARY_MYSTERY_BOX), new Item(LEGENDARY_MYSTERY_BOX), new Item(DONATOR_MYSTERY_BOX), new Item(DONATOR_MYSTERY_BOX)};

    /**
     * This option gives you 1 mystery ticket
     * There is a 30% chance that you get this option.
     */
    private static final Item[] OPTION_THREE = {new Item(CustomItemIdentifiers.MYSTERY_TICKET, 1)};

    /**
     * This option gives you 1 epic pet box
     * There is a 20% chance that you get this option.
     */
    private static final Item[] OPTION_FOUR = {new Item(EPIC_PET_BOX)};

    /**
     * This option gives you a mystery chest promo I
     * There is a 5% chance that you get this option.
     */
    private static final Item[] OPTION_FIVE = {new Item(CustomItemIdentifiers.MYSTERY_CHEST, 1)};

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == GRAND_MYSTERY_BOX) {
                open(player);
                return true;
            }
        }
        return false;
    }

    /**
     * The grand mystery box opens three mystery boxes
     */
    private void open(Player player) {
        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.STATEMENT, "<col=FF0000>Guarenteed Reward</col> 2 Legendary donator boxes.", "<col=FF0000>45% chance to get</col> 2 Legendary and 2 donator mystery boxes.", "<col=FF0000>30% chance to get</col> 1 Mystery ticket", "<col=FF0000>20% chance to get</col> 1 Epic pet mystery box.", "<col=FF0000>EXTREME RARE (5%): 1 Mystery chest.</col>");
                setPhase(1);
            }

            @Override
            protected void next() {
                if(isPhase(1)) {
                    send(DialogueType.OPTION, "Open the Grand Mystery Box?", "Yes.", "No.");
                    setPhase(2);
                }
            }

            @Override
            protected void select(int option) {
                if(isPhase(2)) {
                    if(option == 1) {
                        if(!player.inventory().contains(GRAND_MYSTERY_BOX)) {
                            stop();
                            return;
                        }
                        reward(player);
                    }
                    stop();
                }
            }
        });
    }
    //update19
    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item with) {
        if (use.getId() == 2955 && with.getId() == GRAND_MYSTERY_BOX) {

            if (player.getInventory().getFreeSlots() < 6) {
                player.message("You need at least 6 free inventory slots.");
                return true;
            }

            player.inventory().remove(new Item(2955), true);
            player.inventory().remove(new Item(GRAND_MYSTERY_BOX), true);
            int rolls = 2;
            for (int i = 0; i < rolls; i++) {
                rewardUseOnKey(player); // roll twice before giving out the rewards
            }
            return true;
        }
        return false;
    }
    public static void rewardUseOnKey(Player player) {
        int avail = player.inventory().getFreeSlots();


        if (avail > 5) {
            if (Utils.rollDie(GameServer.properties().nerfDropRateBoxes ? 20 : 15, 1)) {
                player.inventory().addAll(OPTION_FIVE);
                //The user box test doesn't yell.
                if (player.getUsername().equalsIgnoreCase("Box test")) {
                    return;
                }
                Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " received " + Arrays.toString(OPTION_FIVE) + " from a mystery box.", "box_and_tickets");
                String worldMessage = "<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Grand Mystery Box</col>]:<col=1e44b3> " + player.getUsername() + "" + Color.HOTPINK.tag() + " just received a Mystery chest promo I" + Color.BLACK.tag() + "!";
                World.getWorld().sendWorldMessage(worldMessage);
            } else if (Utils.rollDie(5, 1)) {
                Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " received " + Arrays.toString(OPTION_FOUR) + " from a mystery box.", "box_and_tickets");
                String worldMessage = "<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Grand Mystery Box</col>]:<col=1e44b3> " + player.getUsername() + "" + Color.HOTPINK.tag() + " just received a Epic Pet Mystery Box" + Color.BLACK.tag() + "!";
                World.getWorld().sendWorldMessage(worldMessage);
                player.inventory().addAll(OPTION_FOUR);
            } else if (Utils.rollDie(10, 3)) {
                Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " received " + Arrays.toString(OPTION_THREE) + " from a mystery box.", "box_and_tickets");
                player.inventory().addAll(OPTION_THREE);
            } else if (Utils.rollDie(100, 45)) {
                Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " received " + Arrays.toString(OPTION_TWO) + " from a mystery box.", "box_and_tickets");
                player.inventory().addAll(OPTION_TWO);
            } else {
                Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " received " + Arrays.toString(OPTION_ONE) + " from a mystery box.", "box_and_tickets");
                player.inventory().addAll(OPTION_ONE);
            }
        }
    }

    public static void reward(Player player) {
        if (!player.inventory().contains(GRAND_MYSTERY_BOX))
            return;

        int avail = player.inventory().getFreeSlots();

        if(avail > 5) {
            player.inventory().remove(new Item(GRAND_MYSTERY_BOX),true);

            if (Utils.rollDie(GameServer.properties().nerfDropRateBoxes ? 20 : 15, 1)) {
                player.inventory().addAll(OPTION_FIVE);
                //The user box test doesn't yell.
                if(player.getUsername().equalsIgnoreCase("Box test")) {
                    return;
                }
                Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " received "+ Arrays.toString(OPTION_FIVE) +" from a mystery box.", "box_and_tickets");
                String worldMessage = "<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Grand Mystery Box</col>]:<col=1e44b3> " + player.getUsername() + ""+Color.HOTPINK.tag()+" just received a Mystery chest promo I" + Color.BLACK.tag() + "!";
                World.getWorld().sendWorldMessage(worldMessage);
            } else if (Utils.rollDie(5, 1)) {
                Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " received "+ Arrays.toString(OPTION_FOUR) +" from a mystery box.", "box_and_tickets");
                String worldMessage = "<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Grand Mystery Box</col>]:<col=1e44b3> " + player.getUsername() + ""+Color.HOTPINK.tag()+" just received a Epic Pet Mystery Box" + Color.BLACK.tag() + "!";
                World.getWorld().sendWorldMessage(worldMessage);
                player.inventory().addAll(OPTION_FOUR);
            } else if (Utils.rollDie(10, 3)) {
                Utils.sendDiscordInfoLog(player,"Player " + player.getUsername() + " received "+ Arrays.toString(OPTION_THREE) +" from a mystery box.", "box_and_tickets");
                player.inventory().addAll(OPTION_THREE);
            } else if (Utils.rollDie(100, 45)) {
                Utils.sendDiscordInfoLog(player,"Player " + player.getUsername() + " received "+ Arrays.toString(OPTION_TWO) +" from a mystery box.", "box_and_tickets");
                player.inventory().addAll(OPTION_TWO);
            } else {
                Utils.sendDiscordInfoLog(player,"Player " + player.getUsername() + " received "+ Arrays.toString(OPTION_ONE) +" from a mystery box.", "box_and_tickets");
                player.inventory().addAll(OPTION_ONE);
            }
        } else {
            player.message("You need at least 6 free inventory slots.");
        }
    }

}
