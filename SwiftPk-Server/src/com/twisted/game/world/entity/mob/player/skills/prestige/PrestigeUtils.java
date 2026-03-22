package com.twisted.game.world.entity.mob.player.skills.prestige;

import com.twisted.util.ItemIdentifiers;

/**
 * A utility class that contains all the locked equipment that requires a certain prestige level to equip.
 * <p>
 * The first index of the array is the item id, and the second index is the prestige level required to equip the item.
 * Each line will need a new array with the item id and prestige level.
 * Example:
 *     {
 *     {ItemIdentifiers.ABYSSAL_WHIP, 1},
 *     {ItemIdentifiers.ABYSSAL_TENTACLE, 2},
 *     {ItemIdentifiers.ABYSSAL_BLUDGEON, 3}
 *     }
 * <p>
 *     In this example, the Abyssal Whip requires 1 prestige level to equip, the Abyssal Tentacle requires 2 prestige levels to equip, and the Abyssal Bludgeon requires 3 prestige levels to equip.
 * <p>
 *     The item id can be found in the {@link ItemIdentifiers} class.
 * <p>
 *     The prestige level can be any integer value 1-10.
 *
 */
public class PrestigeUtils {
    public static final int[][] LOCKED_EQUIPMENT =
        {
            {ItemIdentifiers.ABYSSAL_WHIP, 1}
        };
}
