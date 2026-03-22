package com.hazy;

import com.hazy.sign.SignLink;

import java.io.File;

/**
 * The main configuration for the Client
 *
 * @author Seven
 */
public final class ClientConstants {

    public static final boolean PVP_MODE = true;
    public static final boolean live = false;
    public static final String CLIENT_VERSION = "33";
    public static final int OSRS_DATA_VERSION = 212;
    public static final int OSRS_DATA_SUB_VERSION = 0;

    public static final boolean DEBUG_MODE = false;
    public static final boolean DEBUG_INTERFACES = false;

    /**
     * If we want to pre-populate the spawn tab with all item names, set this to true.//idk what happened was just on
     * We probably want this set to false.
     */
    public static final boolean SPAWN_TAB_DISPLAY_ALL_ITEMS_PRELOADED = false;

    public static final String MAPFUNCTION_SPRITE_FILE_NAME = "mapfunction_file_sprites";
    public static final boolean ENABLE_SWIFTFUP = true;

    /**
     * Used for change world button on login screen
     */
    public static boolean CAN_SWITCH_WORLD = false;

    /**
     * Used for toggle music button on login screen
     */
    public static boolean CAN_SWITCH_MUSIC = true;

    /**
     * Used to toggle sounds (other than the login screen).
     * Sounds seem quite buggy so we should keep this set to false.
     */
    public static boolean SOUNDS_ENABLED = false;

    /**
     * Used to toggle music for login screen at client level,
     * overriding the user's choice.
     */
    public static boolean LOGIN_MUSIC_ENABLED = true;

    /**
     * To enable restarting the login song on toggling the login song,
     * set this to true.
     */
    public static boolean ENABLE_RESTARTING_LOGIN_SONG = true;

    public static final String SPRITE_FILE_NAME = "main_file_sprites";

    /**
     * Allow walking over the wilderness ditch
     */
    public static final boolean WILDERNESS_DITCH_DISABLED = false;

    /**
     * If we want to hide the client version from players, set this to false.
     */
    public static final boolean DISPLAY_CLIENT_VERSION_IN_TITLE = true;

    /**
     * If we want to disable the hit predictor for all players, set this to false.
     */
    public static final boolean HIT_PREDICTOR_ENABLED = true;

    public static final File CACHE_DIR = new File(System.getProperty("user.home"), "." + "HazyV2");

    //Set this to true to check for unused interface ids, this slows down client loading.
    public static final boolean CHECK_UNUSED_INTERFACES = false;

    //Set this to true to check for duplicate interfaces ids, this might slow down client loading.
    public static final boolean CHECK_DUPLICATE_INTERFACES_IDS = false;

    //Set this to true to display the verbose client load time (the loading time at each step).
    public static final boolean DISPLAY_CLIENT_LOAD_TIME_VERBOSE = true;

    //Set this to true to display the simple client load time (the total time it takes to load the client).
    public static final boolean DISPLAY_CLIENT_LOAD_TIME = true;

    public static final String SERVER_ADDRESS = live ? "142.147.98.14" : "127.0.0.1";

    public static int SERVER_PORT = 43594;

    public static final int FILE_SERVER_PORT = 43595;

    public static final boolean DYNAMIC_MAP_CROSSHAIR = false;
    public static final boolean RASTERIZER3D_LOW_MEMORY = false;
    public static final boolean MAPREGION_LOW_MEMORY = true;
    public static final boolean SCENEGRAPH_LOW_MEMORY = false;
    public static final boolean CLIENT_LOW_MEMORY = true;
    public static final boolean OBJECT_DEFINITION_LOW_MEMORY = false;

    public static final String CACHE_NAME = ".HazyV2/";
    public static final String DATA_NAME = ".HazyV2-data/";

    public static boolean JAGCACHED_ENABLED = false;

    /**
     * Toggles a security feature called RSA to prevent packet sniffers
     */
    public static final boolean ENABLE_RSA = true;

    /**
     * A string which indicates the Client's name.
     */
    public static final String CLIENT_NAME = "TwistedScape";

    /**
     * Dumps map region images when new regions are loaded.
     */
    public static boolean DUMP_MAP_REGIONS = false;

    /**
     * A constant which is used for easy repacking data
     */
    public static boolean LOAD_OSRS_DATA_FROM_CACHE_DIR = false;

    /**
     * The directory to the osrs data
     */
    public static String DATA_DIR = SignLink.findCacheDir() + "/data/osrs/";

    /**
     * Displays debug messages on loginscreen and in-game
     */
    public static boolean CLIENT_DATA = false;

    public static boolean FORCE_OVERLAY_ABOVE_WIDGETS = false;

    public static boolean SHIFT_CLICK_TELEPORT = true;

    public static boolean RIGHT_CLICK_AUTOCAST = false;

    /**
     * Used to repack indexes Index 1 = Models Index 2 = Animations Index 3 =
     * Sounds/Music Index 4 = Maps
     */
    public static boolean repackIndexOne = false, repackIndexTwo = false, repackIndexThree = false,
        repackIndexFour = false;
    /**
     * npcBits can be changed to what your server's bits are set to.
     */
    public static final int NPC_BITS = 14;
    public static final int SHOW_MINIMAP = 0;
    public static final boolean OSRS_DATA = true;
    public final static int NO_EFFECT = 0;
    public final static int WAVE = 1;
    public final static int WAVE_2 = 2;
    public final static int SHAKE = 3;
    public final static int SCROLL = 4;
    public final static int SLIDE = 5;

    /**
     * Spawnable Items
     */
    public static final int[] PVP_ALLOWED_SPAWNS =
        {

        };

}

