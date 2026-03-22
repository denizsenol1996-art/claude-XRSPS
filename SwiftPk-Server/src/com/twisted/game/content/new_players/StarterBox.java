package com.twisted.game.content.new_players;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;
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

import static com.twisted.util.CustomItemIdentifiers.DONATOR_MYSTERY_BOX;
import static com.twisted.util.CustomItemIdentifiers.STARTER_BOX;
import static com.twisted.util.ItemIdentifiers.*;

public class StarterBox extends PacketInteraction {

    private static final Logger starterBoxLogs = LogManager.getLogger("StarterBoxLogs");
    private static final Level STARTER_BOX_LOGS;

    static {
        STARTER_BOX_LOGS = Level.getLevel("STARTER_BOX");
    }

    public static boolean STARTER_BOX_ENABLED = true;

    private static final List<Item> STARTER_SETUP = Arrays.asList(new Item(DHAROKS_ARMOUR_SET), new Item(ABYSSAL_TENTACLE), new Item(GRANITE_MAUL_24225), new Item(DRAGON_BOOTS), new Item(AMULET_OF_FURY), new Item(DONATOR_MYSTERY_BOX));

    public static Set<String> starterBoxClaimedIP = new HashSet<>(), starterBoxClaimedMAC = new HashSet<>();

    private static final String directory = "./data/saves/starterBoxClaimed.txt";

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
        var MAC = player.<String>getAttribOr(AttributeKey.MAC_ADDRESS,"invalid");
        var starterBoxClaimed = player.<Boolean>getAttribOr(AttributeKey.STARTER_BOX_CLAIMED,false);
        var fileAlreadyContainsAddress = FileUtil.claimed(IP, MAC, directory);

        //Check if the player doesn't have a spoofed mac address
        if(IP.isEmpty() || MAC.isEmpty() || MAC.equalsIgnoreCase("invalid")) {
            return; // No valid mac address
        }

        //Check if the player has already claimed the box
        if(starterBoxClaimed || fileAlreadyContainsAddress) {
            return; // Already claimed
        }

        if(!STARTER_BOX_ENABLED) {
            return; // System disabled
        }

        //Add the player address to the file
        FileUtil.addAddressToClaimedList(IP, MAC, starterBoxClaimedIP, starterBoxClaimedMAC, directory);

        //Mark as claimed
        player.putAttrib(AttributeKey.STARTER_BOX_CLAIMED,true);
        player.inventory().addOrBank(new Item(STARTER_BOX));
        Utils.sendDiscordInfoLog(player, player.getUsername() + " received a starter box.", "starter_box_received");
        starterBoxLogs.log(STARTER_BOX_LOGS,player.getUsername() + " received a starter box.");
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == STARTER_BOX) {
                if(!player.inventory().contains(STARTER_BOX)) {
                    return true;
                }

                player.inventory().remove(STARTER_BOX);
                player.inventory().addOrBank(STARTER_SETUP);
                return true;
            }
        }
        return false;
    }
}
