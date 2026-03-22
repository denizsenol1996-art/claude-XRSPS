package com.twisted.game.content.raids.chamber_of_xeric;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.loot.LootItem;
import com.twisted.game.world.items.loot.LootTable;

import static com.twisted.game.content.collection_logs.CollectionLog.RAIDS_KEY;
import static com.twisted.game.content.collection_logs.data.LogType.BOSSES;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.CustomItemIdentifiers.IMBUEMENT_SCROLL;
import static com.twisted.util.ItemIdentifiers.*;

public class ChamberLootTable {

    public static Item rollRegular() {
        return regularTable.rollItem();
    }

    public static Item rollUnique() {
        return uniqueTable.rollItem();
    }

    public static Item rollUltraRare() {
        return uniqueTable.rollItem();
    }

    static final LootTable uniqueTable = new LootTable()
        .addTable(1,
            new LootItem(TWISTED_BUCKLER, 1, 8),
            new LootItem(ARCANE_PRAYER_SCROLL, 1, 7),
            new LootItem(DEXTEROUS_PRAYER_SCROLL, 1, 7),
            new LootItem(DRAGON_HUNTER_CROSSBOW, 1, 7),
            new LootItem(DRAGON_CLAWS, 1, 6),
            new LootItem(DINHS_BULWARK, 1, 6),
            new LootItem(MASORI_MASK, 1, 6),
            new LootItem(ANCESTRAL_HAT, 1, 6),
            new LootItem(KODAI_WAND, 1, 6),
            new LootItem(ELDER_MAUL, 1, 6),
            new LootItem(LEGENDARY_MYSTERY_BOX, 1, 6),
            new LootItem(ANCESTRAL_ROBE_TOP, 1, 5),
            new LootItem(ANCESTRAL_ROBE_BOTTOM, 1, 5),
            new LootItem(MASORI_BODY, 1, 5),
            new LootItem(MASORI_CHAPS, 1, 5),
            new LootItem(TUMEKENS_SHADOW, 1, 4),
            new LootItem(MENAPHITE_ORNAMENT_KIT, 1, 3),
            new LootItem(TWISTED_BOW_KIT, 1, 3),
            new LootItem(HOLY_ORNAMENT_KIT, 1, 3),
            new LootItem(25744, 1, 2),
            new LootItem(TWISTED_BOW, 1, 1)
        );

    private static final LootTable regularTable = new LootTable()
        .addTable(1,

            new LootItem(ARMADYL_GODSWORD, 1, 1),
            new LootItem(ARMADYL_CROSSBOW, 1, 1),
            new LootItem(DONATOR_MYSTERY_BOX, 1, 1),
            new LootItem(WEAPON_MYSTERY_BOX, World.getWorld().random(1, 3), 3),
            new LootItem(BLOOD_MONEY, World.getWorld().random(15000, 35000), 6),
            new LootItem(DRAGON_THROWNAXE, World.getWorld().random(125, 250), 5),
            new LootItem(DRAGON_KNIFE, World.getWorld().random(125, 250), 5),
            new LootItem(BANDOS_GODSWORD, 1, 4),
            new LootItem(ZAMORAK_GODSWORD, 1, 4),
            new LootItem(SARADOMIN_GODSWORD, 1, 4),
            new LootItem(ARMOUR_MYSTERY_BOX, World.getWorld().random(1, 7), 3),
            new LootItem(DRAGONFIRE_SHIELD, 1, 3),
            new LootItem(ABYSSAL_DAGGER_P_13271, 1, 5),
            new LootItem(IMBUEMENT_SCROLL, 5, 4),
            new LootItem(TORN_PRAYER_SCROLL, 1, 3)
        );

    public static void unlockOlmlet(Player player) {
        BOSSES.log(player, RAIDS_KEY, new Item(OLMLET));
    }

}

