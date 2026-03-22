package com.twisted.game.content.items.mystery_box.impl;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.FileUtil;
import com.twisted.util.Utils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.twisted.util.CustomItemIdentifiers.CLAN_BOX;
import static com.twisted.util.ItemIdentifiers.*;

public class ClanBox extends PacketInteraction {

    private static final Logger clanBoxLogs = LogManager.getLogger("ClanBoxLogs");
    private static final Level CLAN_BOX_LOGS;

    static {
        CLAN_BOX_LOGS = Level.getLevel("CLAN_BOX");
    }

    public static Set<String> clanBoxClaimedIP = new HashSet<>(), clanBoxClaimedMAC = new HashSet<>();

    private static final String directory = "./data/saves/clanBoxOpened.txt";

    public static void init() {
        clanBoxClaimed(directory);
    }

    public static void clanBoxClaimed(String directory) {
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(directory))) {
                String data;
                while ((data = in.readLine()) != null) {
                    clanBoxClaimedIP.add(data);
                    clanBoxClaimedMAC.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == CLAN_BOX) {
                open(player);
                return true;
            }
        }
        return false;
    }

    private final List<Item> ITEMS = Arrays.asList(new Item(DRAGON_CROSSBOW), new Item(ARMADYL_GODSWORD), new Item(STAFF_OF_LIGHT), new Item(STAFF_OF_LIGHT), new Item(AHRIMS_ARMOUR_SET), new Item(AHRIMS_ARMOUR_SET), new Item(KARILS_LEATHERTOP),new Item(KARILS_LEATHERTOP), new Item(VERACS_PLATESKIRT), new Item(VERACS_PLATESKIRT), new Item(DRAGONSTONE_DRAGON_BOLTS_E,2000), new Item(BERSERKER_RING), new Item(BERSERKER_RING), new Item(INFINITY_BOOTS), new Item(INFINITY_BOOTS), new Item(AMULET_OF_FURY), new Item(AMULET_OF_FURY), new Item(BLOOD_MONEY, 50_000));

    private void open(Player player) {
        if(player.inventory().contains(CLAN_BOX)) {
            var IP = player.getHostAddress();
            var MAC = player.<String>getAttribOr(AttributeKey.MAC_ADDRESS,"invalid");
            var clanBoxOpened = player.<Boolean>getAttribOr(AttributeKey.CLAN_BOX_OPENED,false);
            var fileAlreadyContainsAddress = FileUtil.claimed(IP, MAC, directory);

            //Check if the player doesn't have a spoofed mac address
            if(IP.isEmpty() || MAC.isEmpty() || MAC.equalsIgnoreCase("invalid")) {
                player.message(Color.RED.wrap("You are not on a valid IP or MAC address. You cannot open this box."));
                return; // No valid mac address
            }

            //Check if the player has already claimed the box
            if(clanBoxOpened || fileAlreadyContainsAddress) {
                player.message(Color.RED.wrap("You can only open one clan box."));
                return; // Already opened one
            }

            //Add the player address to the file
            FileUtil.addAddressToClaimedList(IP, MAC, clanBoxClaimedIP, clanBoxClaimedMAC, directory);

            player.inventory().remove(CLAN_BOX);
            player.inventory().addOrBank(ITEMS);

            Utils.sendDiscordInfoLog(player, player.getUsername() + " opened a clan box.", "clan_box_opened");
            clanBoxLogs.log(CLAN_BOX_LOGS,player.getUsername() + " opened a clan box.");

            //Mark as opened
            player.putAttrib(AttributeKey.CLAN_BOX_OPENED,true);
        }
    }
}
