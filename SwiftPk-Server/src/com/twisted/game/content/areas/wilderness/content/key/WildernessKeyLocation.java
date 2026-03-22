package com.twisted.game.content.areas.wilderness.content.key;

import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Kaleem on 15/11/2017.
 */
public enum WildernessKeyLocation {

    //Single area
    MINING_AREA("Mining area", new Tile(3087,3761)),
    CHAOS_ELEMENTAL("Wildy agility entrance", new Tile(2998, 3907)),
    OBELISK("44s Obelisk", new Tile(2980, 3860)),
    FORGOTTEN_CEMETERY("The forgotten cemetery", new Tile(2976, 3751)),

    //Multi area
    VOLCANO("Wilderness Volcano", new Tile(3350, 3917)),
    CALLISTO("Callisto", new Tile(3326, 3852)),
    SCORPIA("Lair of Scorpia", new Tile(3231, 3945)),
    ;

    private static final WildernessKeyLocation[] VALUES = values();

    public static WildernessKeyLocation findRandom() {
        return VALUES[Utils.RANDOM_GEN.get().nextInt(VALUES.length)];
    }

    private final Tile tile;
    private final String description;

    WildernessKeyLocation(String description, Tile tile) {
        this.description = checkNotNull(description, "description");
        this.tile = checkNotNull(tile, "tile");
    }

    public Tile tile() {
        return tile;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

}
