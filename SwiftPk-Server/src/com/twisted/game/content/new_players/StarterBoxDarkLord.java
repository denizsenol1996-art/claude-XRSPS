package com.twisted.game.content.new_players;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.FileUtil;
import com.twisted.util.Utils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StarterBoxDarkLord extends PacketInteraction {//sbox

    private static final Logger starterBoxLogs = LogManager.getLogger("StarterBoxDarkLordLogs");
    private static final Level STARTER_BOX_LOGS;

    static {
        STARTER_BOX_LOGS = Level.getLevel("STARTER_BOX_DARK_LORD");
    }

    public static boolean STARTER_BOX_ENABLED = true;


    public static Set<String> starterBoxClaimedIP = new HashSet<>(), starterBoxClaimedMAC = new HashSet<>();

    private static final String directory = "./data/saves/starterBoxDarkLordClaimed.txt";

    public static void init() {
        starterMysteryBoxClaimed(directory);
    }

    public static void starterMysteryBoxClaimed(String directory) {
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(directory))) {
                String data;
                while ((data = in.readLine()) != null) {
                    starterBoxClaimedIP.add(data);
                    starterBoxClaimedMAC.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void claimStarterBox(Player player) {
        var IP = player.getHostAddress();
        var MAC = player.<String>getAttribOr(AttributeKey.MAC_ADDRESS, "invalid");
        var starterBoxClaimed = player.<Boolean>getAttribOr(AttributeKey.STARTER_BOX_DARK_LORD_CLAIMED, false);
        var fileAlreadyContainsAddress = FileUtil.claimed(IP, MAC, directory);

        //Check if the player doesn't have a spoofed mac address
        if (IP.isEmpty() || MAC.isEmpty() || MAC.equalsIgnoreCase("invalid")) {
            return; // No valid mac address
        }

        //Check if the player has already claimed the box
        if (starterBoxClaimed || fileAlreadyContainsAddress) {
            return; // Already claimed
        }

        if (!STARTER_BOX_ENABLED) {
            return; // System disabled
        }

        //Add the player address to the file
        FileUtil.addAddressToClaimedList(IP, MAC, starterBoxClaimedIP, starterBoxClaimedMAC, directory);

        //Mark as claimed
        player.putAttrib(AttributeKey.STARTER_BOX_DARK_LORD_CLAIMED, true);
        player.inventory().addOrBank(new Item(CustomItemIdentifiers.STARTER_BOX_DARK_LORD));
        Utils.sendDiscordInfoLog(player, player.getUsername() + " received a starter box Dark Lord.", "starter_box_received");
        starterBoxLogs.log(STARTER_BOX_LOGS, player.getUsername() + " received a starter box.");
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 1) {
            if (item.getId() == CustomItemIdentifiers.STARTER_BOX_DARK_LORD) {
                if (!player.inventory().contains(CustomItemIdentifiers.STARTER_BOX_DARK_LORD)) {
                    return true;
                }

                player.getDialogueManager().start(new StarterBoxDig());
                return true;
            }
        }
        return false;
    }

    private class StarterBoxDig extends Dialogue {
        @Override
        protected void start(Object... parameters) {
            send(DialogueType.OPTION, "Please select your darklord weapon","Darklord sword", "Darklord staff", "Darklord bow");
            setPhase(0);
        }

        @Override
        protected void next() {
            if (isPhase(1)) {
                stop();
            }
        }

        @Override
        protected void select(int option) {
            if (isPhase(0)) {
                if (option == 1) {
                    player.getInventory().addOrBank(new Item(CustomItemIdentifiers.DARKLORD_SWORD));
                    player.inventory().remove(CustomItemIdentifiers.STARTER_BOX_DARK_LORD);
                    stop();
                } else if (option == 2) {
                    player.getInventory().addOrBank(new Item(CustomItemIdentifiers.DARKLORD_STAFF));
                    player.inventory().remove(CustomItemIdentifiers.STARTER_BOX_DARK_LORD);
                    stop();
                } else if (option == 3) {
                    player.getInventory().addOrBank(new Item(CustomItemIdentifiers.DARKLORD_BOW));
                    player.inventory().remove(CustomItemIdentifiers.STARTER_BOX_DARK_LORD);
                    stop();
                }
            }

        }
    }
}

