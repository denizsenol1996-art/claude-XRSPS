package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.content.account.AccountSelection;
import com.twisted.game.content.packet_actions.interactions.buttons.Buttons;
import com.twisted.game.task.Task;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.prayer.default_prayer.DefaultPrayerData;
import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.net.packet.interaction.PacketInteractionManager;
import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;

import static com.twisted.util.ItemIdentifiers.ANTIQUE_LAMP_13148;

/**
 * This packet listener manages a button that the player has clicked upon.
 *
 * @author Gabriel Hannason
 */
public class ButtonClickPacketListener implements PacketListener {

    private static final Logger logger = LogManager.getLogger(ButtonClickPacketListener.class);

    public static final int FIRST_DIALOGUE_OPTION_OF_FIVE = 2494;
    public static final int SECOND_DIALOGUE_OPTION_OF_FIVE = 2495;
    public static final int THIRD_DIALOGUE_OPTION_OF_FIVE = 2496;
    public static final int FOURTH_DIALOGUE_OPTION_OF_FIVE = 2497;
    public static final int FIFTH_DIALOGUE_OPTION_OF_FIVE = 2498;
    public static final int FIRST_DIALOGUE_OPTION_OF_FOUR = 2482;
    public static final int SECOND_DIALOGUE_OPTION_OF_FOUR = 2483;
    public static final int THIRD_DIALOGUE_OPTION_OF_FOUR = 2484;
    public static final int FOURTH_DIALOGUE_OPTION_OF_FOUR = 2485;
    public static final int FIRST_DIALOGUE_OPTION_OF_THREE = 2471;
    public static final int SECOND_DIALOGUE_OPTION_OF_THREE = 2472;
    public static final int THIRD_DIALOGUE_OPTION_OF_THREE = 2473;
    public static final int FIRST_DIALOGUE_OPTION_OF_TWO = 2461;
    public static final int SECOND_DIALOGUE_OPTION_OF_TWO = 2462;
    public static final int LOGOUT = 2458;
    public static final int DUEL_LOAD_PREVIOUS_SETTINGS = 24492;

    public static final int[] ALL = new int[] {2494, 2495, 2496, 2497, 2498, 2482, 2483, 2484, 2485, 2471, 2472, 2473, 2461, 2462, 2458, 24492};

    public static void main(String[] args) {
        final Packet packet = new Packet(-1, Unpooled.copiedBuffer(new byte[]{(byte) 0, (byte) 0, (byte) 101, (byte) -9}));
        int r = packet.readInt();
        System.out.println("was "+ Arrays.toString(packet.getBuffer().array())+" -> "+r);
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int button = packet.readInt();
        parseButtonPacket(player, button);
    }

    public void parseButtonPacket(Player player, int button) {
        int select = player.getAttribOr(AttributeKey.SELECET, 0);
        if (player.dead()) {
            return;
        }

        if(player.askForAccountPin() && button != 2458) {//Allowed to logout
            player.sendAccountPinMessage();
            return;
        }
        if (button == 2812) {
            player.putAttrib(AttributeKey.SELECET, 1);
            player.message("You select attack");
        }
        if (button == 2813) {
            player.putAttrib(AttributeKey.SELECET, 2);
            player.message("You select str");
        }
        if (button == 2814) {
            player.putAttrib(AttributeKey.SELECET, 3);
            player.message("You select Range");
        }
        if (button == 2815) {
            player.putAttrib(AttributeKey.SELECET, 4);
            player.message("You select Magic");
        }
        if (button == 2816) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Defence");
            player.putAttrib(AttributeKey.SELECET, 5);

        }
        if (button == 2817) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Hitpoints");
            player.putAttrib(AttributeKey.SELECET, 6);

        }
        if (button == 2818) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Prayer");
            player.putAttrib(AttributeKey.SELECET, 7);

        }
        if (button == 2819) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Agility");
            player.putAttrib(AttributeKey.SELECET, 8);

        }
        if (button == 2820) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Herblore");
            player.putAttrib(AttributeKey.SELECET, 9);

        }
        if (button == 2821) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Thieving");
            player.putAttrib(AttributeKey.SELECET, 10);

        }
        if (button == 2822) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Crafting");
            player.putAttrib(AttributeKey.SELECET, 11);

        }
        if (button == 2823) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Runecraft");
            player.putAttrib(AttributeKey.SELECET, 12);

        }
        if (button == 12034) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Slayer");
            player.putAttrib(AttributeKey.SELECET, 13);

        }
        if (button == 13914) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Farming");
            player.putAttrib(AttributeKey.SELECET, 14);

        }
        if (button == 2824) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Mining");
            player.putAttrib(AttributeKey.SELECET, 15);

        }
        if (button == 2825) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Smiting");
            player.putAttrib(AttributeKey.SELECET, 16);

        }
        if (button == 2826) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Fishing");
            player.putAttrib(AttributeKey.SELECET, 17);

        }
        if (button == 2827) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Cooking");
            player.putAttrib(AttributeKey.SELECET, 18);

        }
        if (button == 2828) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select FireMaking");
            player.putAttrib(AttributeKey.SELECET, 19);

        }
        if (button == 2829) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Woodcutting");
            player.putAttrib(AttributeKey.SELECET, 20);

        }
        if (button == 2830) {
            //  player.antiqueItemResetSkillId = 4;
            player.message("You select Fletching");
            player.putAttrib(AttributeKey.SELECET, 21);

        }
        if (button == 2831 && player.getInventory().contains(ANTIQUE_LAMP_13148)) {
            int ironManXp = 12_500;
            int amount = 250_000;
            if (select == 1) {
                player.getInterfaceManager().close();
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.ATTACK, ironManXp);
                } else{
                    player.skills().addXp(Skills.ATTACK, amount);

                }

            } else if (select == 2) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.STRENGTH, ironManXp);
                } else{
                    player.skills().addXp(Skills.STRENGTH, amount);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 3) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.RANGED, ironManXp);
                } else{
                    player.skills().addXp(Skills.RANGED, amount);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 4) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.MAGIC, ironManXp);
                } else{
                    player.skills().addXp(Skills.MAGIC, amount);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 5) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.DEFENCE, ironManXp);
                } else{
                    player.skills().addXp(Skills.DEFENCE, amount);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 6) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.HITPOINTS, ironManXp);
                } else {
                    player.skills().addXp(Skills.HITPOINTS, amount);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 7) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.PRAYER, ironManXp);
                } else{
                    player.skills().addXp(Skills.PRAYER, 5000);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 8) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.AGILITY, ironManXp);
                } else{
                    player.skills().addXp(Skills.AGILITY, 8350);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 9) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.HERBLORE, ironManXp);
                } else{
                    player.skills().addXp(Skills.HERBLORE, 8350);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 10) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.THIEVING, ironManXp);
                } else{
                    player.skills().addXp(Skills.THIEVING, 7150);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 11) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.CRAFTING, ironManXp);
                } else{
                    player.skills().addXp(Skills.CRAFTING, 8350);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 12) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.RUNECRAFTING, ironManXp);
                } else{
                    player.skills().addXp(Skills.RUNECRAFTING, 5000);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 13) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.SLAYER, ironManXp);
                } else{
                    player.skills().addXp(Skills.SLAYER, 10000);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 14) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.FARMING, ironManXp);
                } else{
                    player.skills().addXp(Skills.FARMING, 4550);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 15) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.MINING, ironManXp);
                } else{
                    player.skills().addXp(Skills.MINING, 5000);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 16) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.SMITHING, ironManXp);
                } else{
                    player.skills().addXp(Skills.SMITHING, 4160);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 17) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.FISHING, ironManXp);
                } else{
                    player.skills().addXp(Skills.FISHING, 6250);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 18) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.COOKING, 12500);
                } else{
                    player.skills().addXp(Skills.COOKING, 8335);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 19) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.FIREMAKING, ironManXp);
                } else{
                    player.skills().addXp(Skills.FIREMAKING, 8350);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 20) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.WOODCUTTING, ironManXp);
                } else{
                    player.skills().addXp(Skills.WOODCUTTING, 8350);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            } else if (select == 21) {
                player.getInterfaceManager().close();
                player.putAttrib(AttributeKey.SELECET, 0);
                if(player.ironMode() != IronMode.NONE){
                    player.skills().addXp(Skills.FLETCHING, ironManXp);
                } else{
                    player.skills().addXp(Skills.FLETCHING, 8350);

                }
                player.getInventory().remove(new Item(ANTIQUE_LAMP_13148, 1), true);
            }else {
                player.message("Please reselect the skill you want.");
                return;
            }

        }

        player.afkTimer.reset();

        player.debugMessage("button=" + button);
        //System.out.println("button=" + button);

        if (button == DUEL_LOAD_PREVIOUS_SETTINGS) {
            if (!GameServer.properties().enableLoadLastDuelPreset) {
                player.message("That feature is currently disabled.");
                return;
            }
            player.getDueling().handleSavedConfig();
        }

        if (button == FIRST_DIALOGUE_OPTION_OF_FIVE || button == FIRST_DIALOGUE_OPTION_OF_FOUR
                || button == FIRST_DIALOGUE_OPTION_OF_THREE || button == FIRST_DIALOGUE_OPTION_OF_TWO) {
            if (player.getDialogueManager().isActive()) {
                if (player.getDialogueManager().select(1)) {
                    return;
                }
            }
        }

        if (button == SECOND_DIALOGUE_OPTION_OF_FIVE || button == SECOND_DIALOGUE_OPTION_OF_FOUR
                || button == SECOND_DIALOGUE_OPTION_OF_THREE || button == SECOND_DIALOGUE_OPTION_OF_TWO) {
            if (player.getDialogueManager().isActive()) {
                if (player.getDialogueManager().select(2)) {
                    return;
                }
            }
        }

        if (button == THIRD_DIALOGUE_OPTION_OF_FIVE || button == THIRD_DIALOGUE_OPTION_OF_FOUR
                || button == THIRD_DIALOGUE_OPTION_OF_THREE) {
            if (player.getDialogueManager().isActive()) {
                if (player.getDialogueManager().select(3)) {
                    return;
                }
            }
        }

        if (button == FOURTH_DIALOGUE_OPTION_OF_FIVE || button == FOURTH_DIALOGUE_OPTION_OF_FOUR) {
            if (player.getDialogueManager().isActive()) {
                if (player.getDialogueManager().select(4)) {
                    return;
                }
            }
        }

        if (button == FIFTH_DIALOGUE_OPTION_OF_FIVE) {
            if (player.getDialogueManager().isActive()) {
                if (player.getDialogueManager().select(5)) {
                    return;
                }
            }
        }

        //If the player accepts their appearance then they can continue making their account.
        if (player.<Boolean>getAttribOr(AttributeKey.NEW_ACCOUNT,false) && button == 3651) {
            if (GameServer.properties().pvpMode) {
                //Tutorial.start(player);
                AccountSelection.open(player);
            }
            return;
        }

        // Direct account selection handling
        if (button == 42402 || button == 42403 || button == 42423 || button == 42405 || button == 42406) {
            player.putAttrib(AttributeKey.GAME_MODE_SELECTED, button);
            AccountSelection.refreshOptions(player);
            return;
        }
        if (button == 42419) {
            new AccountSelection().confirm(player);
            return;
        }

        if(player.locked()) {
            // unique case: since prayers always 'activate' when clicked client side, we'll try to just wait until
            // we unlock and trigger the button so the client stays in sync.
            DefaultPrayerData defaultPrayerData = DefaultPrayerData.getActionButton().get(button);
            if (defaultPrayerData != null) {

                // store btn
                HashSet<Integer> clicks = player.<HashSet<Integer>>getAttribOr(AttributeKey.PRAYER_DELAYED_ACTIVATION_CLICKS, new HashSet<Integer>());
                clicks.add(button); // one task but you can spam different prayers. queue them all up until task is over.
                player.putAttrib(AttributeKey.PRAYER_DELAYED_ACTIVATION_CLICKS, clicks);

                // fetch task
                Task task = player.<Task>getAttribOr(AttributeKey.PRAYER_DELAYED_ACTIVATION_TASK, null);
                if (task == null) {

                    // build task logic
                    task = Task.repeatingTask(t -> {

                        // this is a long ass pause homie
                        if (t.tick > 10) {
                            t.stop();
                            player.clearAttrib(AttributeKey.PRAYER_DELAYED_ACTIVATION_TASK);
                            for (Integer click : clicks) {
                                DefaultPrayerData p1 = DefaultPrayerData.getActionButton().get(click);
                                if (p1 != null) // resync previous state
                                    player.getPacketSender().sendConfig(p1.getConfigId(), player.getPrayerActive()[p1.ordinal()] ? 1 : 0);
                            }
                            clicks.clear();
                            return;
                        }

                        // tele has finished or w.e was locking us
                        if (!player.locked()) {
                            t.stop();
                            player.clearAttrib(AttributeKey.PRAYER_DELAYED_ACTIVATION_TASK);
                            // now trigger we are unlocked
                            for (Integer click : clicks) {
                                parseButtonPacket(player, click);
                            }
                            clicks.clear();
                        }
                    });
                    player.putAttrib(AttributeKey.PRAYER_DELAYED_ACTIVATION_TASK, task);
                }
            }
            return;
        }

        if (PacketInteractionManager.checkButtonInteraction(player, button)) {
            return;
        }

        Buttons.handleButton(player, button);
    }
}
