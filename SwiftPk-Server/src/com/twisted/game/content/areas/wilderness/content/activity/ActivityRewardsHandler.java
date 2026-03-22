package com.twisted.game.content.areas.wilderness.content.activity;

import com.twisted.game.content.items.mystery_box.MboxItem;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.Color;
import com.twisted.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * The class which represents functionality for the wilderness activity's rewards.
 * @author Zerikoth
 * @Since september 24, 2020
 */
public class ActivityRewardsHandler {

    private static final List<Item> COMMON = Arrays.asList(
        new MboxItem(DRAGON_CROSSBOW),
        new MboxItem(DRAGON_HUNTER_CROSSBOW),
        new MboxItem(BANDOS_GODSWORD),
        new MboxItem(SARADOMIN_GODSWORD),
        new MboxItem(ZAMORAK_GODSWORD),
        new MboxItem(LIGHT_BALLISTA),
        new MboxItem(STAFF_OF_THE_DEAD),
        new MboxItem(SUPER_COMBAT_POTION4+1, 50),
        new MboxItem(ANGLERFISH+1,100),
        new MboxItem(ANTIVENOM4+1,100),
        new MboxItem(ETERNAL_BOOTS),
        new MboxItem(PEGASIAN_BOOTS),
        new MboxItem(PRIMORDIAL_BOOTS),
        new MboxItem(STAFF_OF_THE_DEAD),
        new MboxItem(DRAGONFIRE_SHIELD),
        new MboxItem(ARMADYL_HELMET),
        new MboxItem(ARMADYL_CHESTPLATE),
        new MboxItem(ARMADYL_CHAINSKIRT),
        new MboxItem(BANDOS_CHESTPLATE),
        new MboxItem(BANDOS_TASSETS),
        new MboxItem(DRAGONFIRE_WARD),
        new MboxItem(SERPENTINE_HELM),
        new MboxItem(ANCIENT_WYVERN_SHIELD),
        new MboxItem(SPECTRAL_SPIRIT_SHIELD),
        new MboxItem(ARMADYL_CROSSBOW),
        new MboxItem(DRAGON_HUNTER_CROSSBOW)
    );

    private static final List<Item> UNCOMMON = Arrays.asList(
        new MboxItem(ARMADYL_GODSWORD),
        new MboxItem(HEAVY_BALLISTA),
        new MboxItem(DRAGON_CLAWS),
        new MboxItem(SERPENTINE_HELM),
        new MboxItem(TOXIC_STAFF_OF_THE_DEAD),
        new MboxItem(ARCANE_SPIRIT_SHIELD),
        new MboxItem(DINHS_BULWARK),
        new MboxItem(ARCANE_SPIRIT_SHIELD),
        new MboxItem(NEITIZNOT_FACEGUARD),
        new MboxItem(MORRIGANS_COIF),
        new MboxItem(MORRIGANS_LEATHER_BODY),
        new MboxItem(MORRIGANS_LEATHER_CHAPS),
        new MboxItem(ZURIELS_HOOD),
        new MboxItem(ZURIELS_ROBE_TOP),
        new MboxItem(ZURIELS_ROBE_BOTTOM),
        new MboxItem(VESTAS_SPEAR),
        new MboxItem(VESTAS_LONGSWORD),
        new MboxItem(TOXIC_BLOWPIPE),
        new MboxItem(DRAGON_WARHAMMER)
    );

    private static final List<Item> RARE = Arrays.asList(
        new MboxItem(GHRAZI_RAPIER),
        new MboxItem(ELDER_MAUL),
        new MboxItem(SANGUINESTI_STAFF),
        new MboxItem(ELYSIAN_SPIRIT_SHIELD),
        new MboxItem(STATIUSS_FULL_HELM),
        new MboxItem(STATIUSS_PLATEBODY),
        new MboxItem(STATIUSS_PLATELEGS),
        new MboxItem(VESTAS_CHAINBODY),
        new MboxItem(VESTAS_PLATESKIRT),
        new MboxItem(ANCESTRAL_HAT),
        new MboxItem(ANCESTRAL_ROBE_TOP),
        new MboxItem(ANCESTRAL_ROBE_BOTTOM),
        new MboxItem(NIGHTMARE_STAFF),
        new MboxItem(INQUISITORS_GREAT_HELM),
        new MboxItem(INQUISITORS_HAUBERK),
        new MboxItem(INQUISITORS_PLATESKIRT)
    );

    public static void rollForCasket(Player player) {
        if(Utils.rollDie(45, 1)) {
            player.inventory().addOrDrop(new Item(ACTIVITY_CASKET_3));
            //The user box test doesn't yell.
            if(player.getUsername().equalsIgnoreCase("Box test")) {
                return;
            }
            World.getWorld().sendWorldMessage("<img=1875>" + player.getUsername() + " " + "received "+Color.HOTPINK.tag()+" 1x Activity casket</col>.");
        }
    }

    public static void open(Player player) {
        if(!player.inventory().contains(ACTIVITY_CASKET_3))
            return;

        player.inventory().remove(new Item(ACTIVITY_CASKET_3), true);

        List<Item> items;
        if (Utils.rollDie(20, 1)) {
            items = RARE;
        } else if (Utils.rollDie(10, 1)) {
            items = UNCOMMON;
        } else {
            items = COMMON;
        }

        Item item = Utils.randomElement(items);
        player.inventory().addOrDrop(item);

        boolean amOverOne = item.getAmount() > 1;
        String amtString = amOverOne ? "" + Utils.format(item.getAmount()) + "x" : Utils.getAOrAn(item.name());

        player.message("You open the casket and find...");
        player.message("And find... "+Color.HOTPINK.tag()+""+amtString+" "+item.unnote().name()+"</col>.");
        if (items == RARE) {
            //The user box test doesn't yell.
            if(player.getUsername().equalsIgnoreCase("Box test")) {
                return;
            }
            String msg = "<img=1875><shad=0>" + player.getUsername() + " has received "+Color.HOTPINK.tag()+"" + amtString + " " + item.name() + "</col> from a Activity casket!";
            World.getWorld().sendWorldMessage(msg);
        }
    }
}
