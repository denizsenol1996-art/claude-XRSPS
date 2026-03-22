package com.twisted.game.content.teleport.world_teleport_manager;

import com.twisted.game.content.areas.edgevile.dialogue.SkillingAreaHuntingExpertDialogue;
import com.twisted.game.content.areas.wilderness.content.boss_event.WildernessBossEvent;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.position.Tile;
import com.twisted.util.chainedwork.Chain;

import java.util.*;
import java.util.stream.Collectors;

import static com.twisted.game.content.teleport.world_teleport_manager.TeleportInterface.TeleportData.*;

/**
 * @author Patrick van Elderen | February, 20, 2021, 21:08
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class TeleportInterface {

    private final Player player;

    public TeleportInterface(Player player) {
        this.player = player;
    }

    private static final int FAVORITES_TAB = 1;
    private static final int RECENT_TAB = 2;
    private static final int PVP_TAB = 3;
    private static final int PVM_TAB = 4;
    private static final int BOSSING_TAB = 5;
    private static final int MINIGAMES_TAB = 6;
    private static final int OTHER_TAB = 7;

    public enum TeleportData implements TeleportActions {
        // for now.
        BANDIT_CAMP(new Tile(3034, 3690), 1001, " Bandit Camp", PVP_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        CHAOS_TEMPLE(new Tile(3235, 3643), 1000, "Chaos Temple", PVP_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        DEMONIC_RUINS(new Tile(3287, 3884), 999, "Demonic Ruins", PVP_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        EAST_DRAGONS(new Tile(3343, 3664), 998, "East Dragons", PVP_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        GRAVEYARD(new Tile(3161, 3670), 997, "  Graveyard", PVP_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        MAGEBANK_WILD(new Tile(3091, 3957), 995, "   Magebank", PVP_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        REV_CAVES(new Tile(3127, 3832), 994, "   Rev Caves", PVP_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        THE_GATE(new Tile(3225, 3903), 993, "    The Gate", PVP_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        WEST_DRAGONS(new Tile(2978, 3598), 992, "West Dragons", PVP_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        BLACK_CHINS(new Tile(3149, 3779), 1101, "  Black Chins", PVP_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        COWS(new Tile(3261, 3271), 1008, "        Cows", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        DAGANNOTHS(new Tile(2443, 10146, 0), 1007, " Dagannoths", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        EXPERIMENTS(new Tile(3556, 9944), 1006, " Experiments", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        LIZARDMEN(new Tile(1453, 3694, 0), 1005, "  Lizardmen", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        ROCK_CRABS(new Tile(2706, 3713), 1004, "  Rock Crabs", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        SKELE_WYVERNS(new Tile(3056, 9562, 0), 1003, "Skele Wyverns", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        YAKS(new Tile(2323, 3800), 1002, "        Yaks", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        SMOKE_DEVILS(new Tile(2404, 9417, 0), 1043, "Smoke Devils", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        SLAYER_TOWER(new Tile(3420, 3535, 0), 1092, " Slayer Tower", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        BRIMHAVEN_DUNGEON(new Tile(2709, 9564, 0), 1095, "Brimhaven Du", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        TAVERLEY_DUNGEON(new Tile(2884, 9799, 0), 1094, " Taverley Du", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        CATACOMBS(new Tile(1666, 10048, 0), 1090, " Catacombs", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        SAND_CRABS(new Tile(1728, 3463, 0), 1158, "  Sand Crabs", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        FIRE_GIANTS(new Tile(2568, 9892, 0), 1159, "  Fire Giants", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        SLAYER_STRONGHOLD(new Tile(2431, 3421, 0), 1160, "Slayer Strong", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        RELLEKKA_SLAYER(new Tile(2803, 9998, 0), 1161, "Rellekka Du", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        DARK_BEAST(new Tile(2023, 4635, 0), 1162, "  Dark Beast", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        KALPHITE_LAIR(new Tile(3485, 9509, 2), 1163, "Kalphite Lair", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        ANCIENT_CAVERN(new Tile(1768, 5366, 1), 1190, "Ancient Cave", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        WYVERNS(new Tile(3609, 10278, 0), 1009, "Wyvern Cave", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        KARUULM_SLAYER_DUNGEON(new Tile(1311, 3812, 0), 1448, "Karuulm Du", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        LITHKREN(new Tile(3565, 3998, 0), 1449, "    Lithkren", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        LUMBRIDGE_SWAMP_CAVE(new Tile(3169, 9570, 0), 1814, "Lumbridge Sw", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        BRINE_RAT_CAVE(new Tile(2692, 10124, 0), 1818, "Brine Rat Cav", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        MOSS_LE_HARMLESS_CAVE(new Tile(3826, 9425, 0), 1820, "Mos Le'Harm", PVM_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        CALLISTO(new Tile(3307, 3837), 1031, "  Callisto", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        CERBERUS(new Tile(1315, 1251), 1030, "   Cerberus", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        CHAOS_FAN(new Tile(2992, 3851), 1029, "   Chaos Fan", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        CORP_BEAST(new Tile(2969, 4382, 2), 1028, "  Corp Beast", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        CRAZY_ARCH(new Tile(2976, 3694), 1027, "  Crazy Arch", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        DAGG_KINGS(new Tile(1912, 4367), 1026, "  Dagg Kings", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        DEMON_GORILLAS(new Tile(2128, 5647), 1025, "Demon Gorillas", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        GWD(null, 1024, "         GWD", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },//faw
        KBD(new Tile(3005, 3848), 1023, "         KBD", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        KRAKEN(new Tile(2280, 10016), 1021, "     Kraken", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        SHAMAN(new Tile(1420, 3715), 1020, "     Shaman", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        THERMO(new Tile(2379, 9452), 1018, "     Thermo", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        VENENATIS(new Tile(3319, 3745), 1017, "   Venenatis", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        VETION(new Tile(3239, 3783), 1016, "      Vet'ion", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        SCORPIA(new Tile(3232, 3950), 1042, "      Scorpia", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        CHAOS_ELEMENTAL(new Tile(3269, 3927), 1044, "   Chaos Ele", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        ZULRAH(new Tile(2204, 3056), 1015, "      Zulrah", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        VORKATH(new Tile(2273, 4049), 1104, "   Vorkath", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        WORLD_BOSS(null, 1019, "   World boss", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        KQ(new Tile(3507, 9494,2), 1022, "          KQ", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        GIANT_MOLE(new Tile(1752, 5234), 1447, "Giant Mole", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        ALCHEMICAL_HYDRA(new Tile(1354, 10258), 1758, "Alchy Hydra", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        BARRELCHEST(new Tile(3287, 3884), 1795, "Barrelchest", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return true;
            }
        },
        CORRUPTED_NECHRYARCH(new Tile(1885, 3865), 1812, "Corrupted Ne", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        RAIDS_AREA(new Tile(1245, 3561), 1819, "Raids area", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        NEX(new Tile(2914, 5203), 1872, "Nex", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        THEJUSTICIARGUY(new Tile(2455, 5031), 1903, "thejusticiarguy", BOSSING_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        BARROWS(new Tile(3565, 3306), 1014, "     Barrows", MINIGAMES_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        FIGHT_CAVE(new Tile(2440, 5172), 1012, "    Fight Cave", MINIGAMES_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        MAGEBANK(new Tile(2540, 4716), 1011, "   Magebank", MINIGAMES_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        WARRIORS_GUILD(new Tile(2879, 3546), 1808, "Warriors Guild", MINIGAMES_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        PEST_CONTROL(new Tile(2662, 2647), 1804, "Pest Control", MINIGAMES_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        PORT_PISCARILIUS(new Tile(1815, 3690), 1093, "Port Piscaril", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        GNOME_AGILITY(new Tile(2474, 3438), 1096, "Gnome Agility", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        BARB_AGILITY(new Tile(2552, 3563), 1097, " Barb Agility", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        FARMING_AREA(new Tile(2831, 3453), 1116, " Farming Area", OTHER_TAB) {//up10
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        CATHERBY(new Tile(2809, 3439), 1450, " Catherby", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        KARAMJA(new Tile(2918, 3176), 1816, " Karamja", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        LUNAR_ISLE(new Tile(2108, 3914), 1817, "Lunar Isle", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        TZHAAR_CITY(new Tile(2451, 5152), 1806, "Tzhaar City", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        EDGEVILE(new Tile(3086, 3490), 1798, "Edgevile", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        LUMBRIDGE(new Tile(3222, 3218), 1815, "Lumbridge", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        VARROCK(new Tile(3210, 3424), 1807, "Varrock", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        FALADOR(new Tile(2964, 3378), 1799, "Falador", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        CAMELOT(new Tile(2757, 3477), 1796, "Camelot", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        ARDOUGNE(new Tile(2662, 3305), 1794, "Ardougne", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        CANIFIS(new Tile(3495, 3486), 1797, "Canifis", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        KELDAGRIM(new Tile(2843, 10204), 1802, "Keldagrim", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        YANILLE(new Tile(2544, 3092), 1810, "Yanille", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        FISHING_AREAS(null, 1801, "Fishing Areas", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        MINING_AREAS(null, 1803, "Mining Areas", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        WOODCUTTING_AREAS(null, 1809, "Woodcutting", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        HUNTER_AREAS(null, 1101, "Hunter Areas", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        },
        SMITHING_ANVIL(new Tile(3189, 3425), 1805, "Smithing Anvil", OTHER_TAB) {
            @Override
            public int paymentAmount() {
                return 0;
            }

            @Override
            public boolean dangerous() {
                return false;
            }
        };

        TeleportData(Tile tile, int spriteID, String teleportName, int tabIndex) {
            this.tile = tile;
            this.spriteID = spriteID;
            this.teleportName = teleportName;
            this.tabIndex = tabIndex;
        }

        private final Tile tile;
        private final int spriteID;
        private final String teleportName;
        private final int tabIndex;

    }

    private final TeleportData[] VALUES = TeleportData.values();

    public Optional<TeleportData> findTeleport(int tabIndex, int teleportIndex) {
        int index = 0;
        for (TeleportData teleport : VALUES) {
            if (teleport.tabIndex == tabIndex) {
                if (index == teleportIndex) {
                    return Optional.of(teleport);
                }
                index++;
            }
        }
        return Optional.empty();
    }

    public static Tile getLocation(TeleportData value) {
        return value.tile;
    }

    // this method doesn't really need an explanation
    public boolean handleButton(int id, int optionIndex) {
        if (!(id >= 29095 && id <= 29125)) {
            return false;
        }
        int index = -1;
        //System.out.println("button id: " + id);
        index = id - 29095;
        //System.out.println("teleport index" + index);

        if (optionIndex == 1) { // add favorite
            if (player.getFavorites().size() >= 20) {
                player.message("You can only have 20 favourite teleports.");
                return false;
            }
            TeleportData teleport = VALUES[index];
            player.debugMessage("Found teleport " + teleport);
            //System.out.println("teleport tabindex" + teleport.tabIndex);
            //System.out.println("player tabindex" + player.getCurrentTabIndex());
            Optional<TeleportData> possibleTeleport = findTeleport(player.getCurrentTabIndex(), index);
            if (possibleTeleport.isPresent()) {
                teleport = possibleTeleport.get();
                player.debugMessage("Found teleport now" + teleport);
            }
            player.getFavorites().add(teleport);
            player.debugMessage("Added teleport: " + teleport.toString() + " to favorites");
            player.debugMessage("Teleport data now after adding: " + teleport);

        } else if (optionIndex == 2) {
            player.getFavorites().remove(index);
            player.debugMessage("Teleport data now after removing: " + player.getFavorites());
            displayFavorites();
        }

        if (optionIndex == 0) {
            int currentTab = player.getCurrentTabIndex();
            if (currentTab == RECENT_TAB && index < player.getRecentTeleports().size()) {
                TeleportData recent = player.getRecentTeleports().get(index);
                teleport(recent);
                return true;
            }

            if (currentTab == FAVORITES_TAB && index < player.getFavorites().size()) {
                TeleportData favorite = player.getFavorites().get(index);
                teleport(favorite);
                return true;
            }

            if (index < VALUES.length) {
                teleport(index);
            }
        }

        if (optionIndex == 0 && index >= VALUES.length) {
            //If this condition is true, it probably means we need to add the teleport to the TeleportData enum.
            System.err.println("index: " + index + " vs VALUES.length: " + VALUES.length);
            System.err.println("Clicked a button (probably a Teleport) that doesn't seem to exist in the TeleportData enum");
        }

        return true;
    }

    // handles the teleporting
    public void teleport(int index) {
        TeleportData chosen = VALUES[index];
        int currentTabIndex = player.getCurrentTabIndex();
        Optional<TeleportData> possibleTeleport = findTeleport(currentTabIndex, index);
        if (possibleTeleport.isPresent()) {
            chosen = possibleTeleport.get();
        }
        Tile destination = getDestination(chosen, currentTabIndex);
        player.debugMessage("Found teleport " + chosen);
        //System.out.println("Destination is: " + destination + " for " + chosen + " for tab index " + currentTabIndex);
        TeleportData[] special_teleport = {WORLD_BOSS, GWD, FISHING_AREAS, MINING_AREAS, WOODCUTTING_AREAS, HUNTER_AREAS};
        TeleportData teleport = chosen;
        if (destination == null && Arrays.stream(special_teleport).noneMatch(teleportData -> teleportData == teleport)) { // it can only be null when it doesn't exist in the tab or enum
            player.message("Fella u clicked on teleport that quite literally doesn't exist");
            return;
        }
        //Technically a LinkedHashSet (with overriding equals / hashcode?) is faster than a List but this is probably fine.
        if (!player.getRecentTeleports().contains(chosen)) {
            //If we have too many recent teleports, remove the last one.
            if (player.getRecentTeleports().size() >= 16) {
                player.getRecentTeleports().remove(15);
            }
            player.getRecentTeleports().add(0, chosen);
        } else {
            player.getRecentTeleports().remove(chosen);
            //If we have too many recent teleports, remove the last one.
            if (player.getRecentTeleports().size() >= 16) {
                player.getRecentTeleports().remove(15);
            }
            player.getRecentTeleports().add(0, chosen);
        }
        if (!handleCustomTeleport(chosen)) {
            //Dangerous teleports
            if (chosen.dangerous()) {
                TeleportData finalChosen = chosen;
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.STATEMENT, "This location is dangerous, would you like to proceed?");
                        setPhase(0);
                    }

                    @Override
                    protected void next() {
                        if (isPhase(0)) {
                            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No.");
                            setPhase(1);
                        }
                    }

                    @Override
                    protected void select(int option) {
                        if (isPhase(1)) {
                            if (option == 1) {
                                if (finalChosen.paymentAmount() > 0) {
                                    takePaymentAndTeleport(finalChosen);
                                } else {
                                    if(destination != null && finalChosen.dangerous() && !Teleports.pkTeleportOk(player, destination)) {
                                        stop();
                                        return;
                                    }

                                    if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                                        stop();
                                        return;
                                    }
                                    Teleports.basicTeleport(player, destination);
                                }
                            } else if (option == 2) {
                                stop();
                            }
                        }
                    }
                });
            } else {
                //Not dangerous, check if it costs BM to teleport
                if (chosen.paymentAmount() > 0) {
                    takePaymentAndTeleport(chosen);
                } else {
                    if(destination != null && chosen.dangerous() && !Teleports.pkTeleportOk(player, destination)) {
                        return;
                    }

                    if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                        return;
                    }
                    Teleports.basicTeleport(player, destination);
                }
            }
        }
    }

    public void takePaymentAndTeleport(TeleportData teleportData) {
        player.costBMAction(teleportData.paymentAmount(), "This teleport costs " + teleportData.paymentAmount() + " BM would you like to proceed?", () -> {
            if(teleportData.dangerous() && !Teleports.pkTeleportOk(player, teleportData.tile)) {
                return;
            }

            if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                return;
            }
            Teleports.basicTeleport(player, teleportData.tile);
        });
    }

    public void teleport(TeleportData chosen) { // instead of making a method that gets the enum index via iterating, this is a way better option.
        //System.out.println("This one");
        int currentTabIndex = player.getCurrentTabIndex();
        Tile destination = getDestination(chosen, currentTabIndex);
        player.debugMessage("Found teleport " + chosen);
        //System.out.println("Destination is: " + destination + " for " + chosen + " for tab index " + currentTabIndex);
        if (destination == null && chosen != WORLD_BOSS) { // it can only be null when it doesn't exist in the tab or enum
            player.message("Fella u clicked on teleport that quite literally doesn't exist");
            return;
        }
        //Technically a LinkedHashSet (with overriding equals / hashcode?) is faster than a List but this is probably fine.
        if (!player.getRecentTeleports().contains(chosen)) {
            //If we have too many recent teleports, remove the last one.
            if (player.getRecentTeleports().size() >= 16) {
                player.getRecentTeleports().remove(15);
            }
            player.getRecentTeleports().add(0, chosen);
        } else {
            player.getRecentTeleports().remove(chosen);
            //If we have too many recent teleports, remove the last one.
            if (player.getRecentTeleports().size() >= 16) {
                player.getRecentTeleports().remove(15);
            }
            player.getRecentTeleports().add(0, chosen);
        }
        if (!handleCustomTeleport(chosen)) {
            //Dangerous teleports
            if (chosen.dangerous()) {
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.STATEMENT, "This location is dangerous, would you like to proceed?");
                        setPhase(0);
                    }

                    @Override
                    protected void next() {
                        if (isPhase(0)) {
                            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No.");
                            setPhase(1);
                        }
                    }

                    @Override
                    protected void select(int option) {
                        if (isPhase(1)) {
                            if (option == 1) {
                                if (chosen.paymentAmount() > 0) {
                                    takePaymentAndTeleport(chosen);
                                } else {
                                    if(chosen.dangerous() && !Teleports.pkTeleportOk(player, chosen.tile)) {
                                        stop();
                                        return;
                                    }

                                    if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                                        stop();
                                        return;
                                    }
                                    Teleports.basicTeleport(player, destination);
                                }
                            } else if (option == 2) {
                                stop();
                            }
                        }
                    }
                });
            } else {
                //Not dangerous, check if it costs BM to teleport
                if (chosen.paymentAmount() > 0) {
                    takePaymentAndTeleport(chosen);
                } else {
                    if(chosen.dangerous() && !Teleports.pkTeleportOk(player, chosen.tile)) {
                        return;
                    }

                    if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                        return;
                    }
                    Teleports.basicTeleport(player, destination);
                }
            }
        }
    }

    // gets the destination for tab, returns null if the teleport doesn't exist in the tab.
    private Tile getDestination(TeleportData teleport, int currentTabIndex) {
        return teleport.tabIndex == currentTabIndex || isFavoriteOrRecent(currentTabIndex) ? teleport.tile : null;
    }

    private boolean isFavoriteOrRecent(int currentTab) {
        return currentTab == RECENT_TAB || currentTab == FAVORITES_TAB;
    }

    // sends a packet to the client to update the recent teleports
    public void displayRecent() {
        int length = player.getRecentTeleports().size();
        int[] sprites = new int[length];
        String[] names = new String[length];
        List<TeleportData> recentTeleports = player.getRecentTeleports();
        int index = 0;
        for (TeleportData teleport : recentTeleports) {
            if (teleport == null) {
                length--;
                continue;
            }
            sprites[index] = teleport.spriteID;
            names[index] = teleport.teleportName;
            index++;
        }
        player.getPacketSender().sendTeleportData(length, true, sprites, names);
    }

    // same thing but with favorites
    public void displayFavorites() {
        List<TeleportData> favorites = player.getFavorites();
        int length = player.getFavorites().size();

        int[] sprites = new int[length];
        String[] names = new String[length];
        List<Integer> toRemove = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            TeleportData favorite = favorites.get(i);
            if (favorite == null) {
                toRemove.add(i);
                continue;
            }
            sprites[i] = favorite.spriteID;
            names[i] = favorite.teleportName;
        }
        if (!toRemove.isEmpty()) {
            for (int remove : toRemove) {
                favorites.remove(remove);
            }
            length = player.getFavorites().size();
        }

        player.getPacketSender().sendTeleportData(length, false, sprites, names);
    }

    /**
     * Sorts the values in descending order and returns the map as a linked hashmap
     * No longer used but keep it for now.
     */
    private Map<TeleportData, Integer> getSortedMap(Map<TeleportData, Integer> map) {
        return map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public boolean handleCustomTeleport(TeleportData chosen) {
        if (chosen == TeleportData.BARROWS) {
            Chain.bound(null).runFn(2, () -> {
                GroundItemHandler.createGroundItem(new GroundItem(new Item(952, 1), new Tile(3565, 3305), player));
            });
            return false;
        }

        if (chosen == TeleportData.REV_CAVES) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.STATEMENT, "This location is dangerous, would you like to proceed?");
                    setPhase(0);
                }

                @Override
                protected void next() {
                    if (isPhase(0)) {
                        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No.");
                        setPhase(1);
                    }
                }

                @Override
                protected void select(int option) {
                    if (isPhase(1)) {
                        if (option == 1) {
                            if(chosen.dangerous() && !Teleports.pkTeleportOk(player, chosen.tile)) {
                                stop();
                                return;
                            }
                            if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                                stop();
                                return;
                            }
                            Teleports.basicTeleport(player, player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.REVENANT_TELEPORT) ? new Tile(3244, 10145, 0) : chosen.tile);
                        } else if (option == 2) {
                            stop();
                        }
                    }
                }
            });
            return true;
        }

        if (chosen == WORLD_BOSS) {
            if (!player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.WORLD_BOSS_TELEPORT)) {
                player.message("You do not meet the requirements to use this teleport.");
                return true;
            }
            if (WildernessBossEvent.getINSTANCE().getActiveNpc().isPresent() && WildernessBossEvent.currentSpawnPos != null) {
                Tile tile = WildernessBossEvent.currentSpawnPos;
                if(chosen.dangerous() && !Teleports.pkTeleportOk(player, tile)) {
                    return true;
                }
                if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                    return true;
                }
                Teleports.basicTeleport(player, tile);
            } else {
                player.message("The world boss recently died and will respawn shortly.");
            }
            return true;
        }
        if (chosen == GWD) {
            if (!player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.GODWARS_ENTRY)) {
                player.message("You need to unlock this teleport first.");
                return true;
            }
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Armadyl", "Bandos", "Saradomin", "Zamorak");
                    setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if (option == 1) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(2841, 5291, 2));
                    } else if (option == 2) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(2860, 5354, 2));
                    } else if (option == 3) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(2911, 5267, 0));
                    } else if (option == 4) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(2925, 5336, 2));
                    }
                }
            });
            //Don't handle tp code.
            return true;
        }

        if (chosen == FISHING_AREAS) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Catherby Fishing Area", "Fishing Guild", "Nevermind");
                    setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if (option == 1) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(2835,3433));
                    } else if (option == 2) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(2594, 3415));
                    } else if (option == 3) {
                        stop();
                    }
                }
            });
            //Don't handle tp code.
            return true;
        }

        if (chosen == MINING_AREAS) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Essence Mining", "Varrock Mining Area", "Desert Mining Area", "Mining Guild", "Nevermind");
                    setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if (option == 1) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(2911, 4830));
                    } else if (option == 2) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(3284,3365));
                    } else if (option == 3) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(3300,3300));
                    } else if (option == 4) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(3050, 9762));
                    } else if (option == 5) {
                        stop();
                    }
                }
            });
            //Don't handle tp code.
            return true;
        }

        if (chosen == WOODCUTTING_AREAS) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Camelot Trees", "Woodcutting Guild", "Nevermind");
                    setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if (option == 1) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(2726, 3473));
                    } else if (option == 2) {
                        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
                            stop();
                            return;
                        }
                        Teleports.basicTeleport(player, new Tile(1663, 3506));
                    } else if (option == 3) {
                        stop();
                    }
                }
            });
            //Don't handle tp code.
            return true;
        }

        if (chosen == HUNTER_AREAS) {
            player.getDialogueManager().start(new SkillingAreaHuntingExpertDialogue());
            //Don't handle tp code.
            return true;
        }
        return false;
    }

}
