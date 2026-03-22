package com.twisted.game.content.seasonal_events.christmas;

import com.twisted.game.world.position.Tile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum SnowMonsterLocations {

    MAGE_BANK(new Tile(3091, 3962), "Mage Bank"),
    MAGE_BANK2(new Tile(3112, 3959), "Mage Bank"),
    MAGE_BANK3(new Tile(3086, 3951), "Mage Bank"),
    MAGE_BANK4(new Tile(3078, 3939), "Mage Bank"),
    MAGE_BANK5(new Tile(3123, 3953), "Mage Bank"),
    EASTS(new Tile(3346, 3674), "::Easts dragons"),
    EASTS2(new Tile(3334, 3668), "::Easts dragons"),
    EASTS3(new Tile(3347, 3661), "::Easts dragons"),
    EASTS4(new Tile(3355, 3679), "::Easts dragons"),
    EASTS5(new Tile(3351, 3690), "::Easts dragons"),
    WESTS(new Tile(2985, 3598), "::Wests dragons"),
    WESTS2(new Tile(2970, 3601), "::Wests dragons"),
    WESTS3(new Tile(2979, 3589), "::Wests dragons"),
    WESTS4(new Tile(2991, 3609), "::Wests dragons"),
    WESTS5(new Tile(2963, 3611), "::Wests dragons"),
    ROGUES_CASTLE(new Tile(3280, 3918), "Rogue's Castle (::50s)"),
    ROGUES_CASTLE2(new Tile(3301, 3926), "Rogue's Castle (::50s)"),
    ROGUES_CASTLE3(new Tile(3286, 3949), "Rogue's Castle (::50s)"),
    ROGUES_CASTLE4(new Tile(3305, 3942), "Rogue's Castle (::50s)"),
    ROGUES_CASTLE5(new Tile(3278, 3918), "Rogue's Castle (::50s)"),
    ICE_WARRIORS(new Tile(2958, 3872), "Ice warriors (::44s)"),
    ICE_WARRIORS2(new Tile(2972, 3884), "Ice warriors (::44s)"),
    ICE_WARRIORS3(new Tile(2948, 3844), "Ice warriors (::44s)"),
    ICE_WARRIORS4(new Tile(2957, 3841), "Ice warriors (::44s)"),
    ICE_WARRIORS5(new Tile(2956, 3873), "Ice warriors (::44s)"),
    DEMONIC_RUINS(new Tile(3289, 3876), "Demonic Ruins"),
    DEMONIC_RUINS2(new Tile(3297, 3896), "Demonic Ruins"),
    DEMONIC_RUINS3(new Tile(3303, 3873), "Demonic Ruins"),
    DEMONIC_RUINS4(new Tile(3302, 3893), "Demonic Ruins"),
    DEMONIC_RUINS5(new Tile(3275, 3885), "Demonic Ruins"),
    CHINS(new Tile(3129, 3792), "Black chinchompas (::chins)"),
    CHINS2(new Tile(3149, 3786), "Black chinchompas (::chins)"),
    CHINS3(new Tile(3138, 3783), "Black chinchompas (::chins)"),
    CHINS4(new Tile(3130, 3774), "Black chinchompas (::chins)"),
    CHINS5(new Tile(3133, 3797), "Black chinchompas (::chins)"),
    LAVA_MAZE(new Tile(3199, 3848), "Lava dragons"),
    LAVA_MAZE2(new Tile(3204, 3833), "Lava dragons"),
    LAVA_MAZE3(new Tile(3203, 3812), "Lava dragons"),
    LAVA_MAZE4(new Tile(3188, 3816), "Lava dragons"),
    LAVA_MAZE5(new Tile(3214, 3847), "Lava dragons"),
    GRAVES(new Tile(3156, 3670), "Zombie Graveyard (::graves)"),
    GRAVES2(new Tile(3174, 3686), "Zombie Graveyard (::graves)"),
    GRAVES3(new Tile(3171, 3664), "Zombie Graveyard (::graves)"),
    GRAVES4(new Tile(3161, 3677), "Zombie Graveyard (::graves)"),
    GRAVES5(new Tile(3184, 3671), "Zombie Graveyard (::graves)");

    public final Tile tile;
    public final String location;

    SnowMonsterLocations(Tile tile, String location) {
        this.tile = tile;
        this.location = location;
    }

    /**
     * Generate a random location from the enum
     * @return the location
     */
    public static SnowMonsterLocations generateLocation() {
        //Create a list
        List<SnowMonsterLocations> locations = Arrays.stream(SnowMonsterLocations.values()).collect(Collectors.toList());
        //Shuffle the list
        Collections.shuffle(locations);

        //Grab first found location
        SnowMonsterLocations location = locations.get(0);
        return location;
    }
}
