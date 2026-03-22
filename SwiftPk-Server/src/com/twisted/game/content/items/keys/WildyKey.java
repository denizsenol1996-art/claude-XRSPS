package com.twisted.game.content.items.keys;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.Utils;

import static com.twisted.game.content.collection_logs.data.LogType.KEYS;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.ObjectIdentifiers.DEADMAN_CHEST_27290;

/**
 * The wilderness Key and its rewards.
 * @author Malefique
 * @Since december 01, 2020
 */
public class WildyKey extends PacketInteraction {

    private static final Item[] EXTREMELY_RARE = new Item[]{
        new Item(ANKOU_MASK), new Item(ANKOU_TOP), new Item(ANKOUS_LEGGINGS), new Item(ANKOU_GLOVES), new Item(ANKOU_SOCKS), new Item(NIGHTMARE_STAFF), new Item(VOLATILE_ORB), new Item(HARMONISED_ORB), new Item(ELDRITCH_ORB),
        new Item(PEGASIAN_BOOTS_OR), new Item(ETERNAL_BOOTS_OR)
    };

    private static final Item[] RARE = new Item[]{
        //new Item(ANCESTRAL_HAT), new Item(ANCESTRAL_ROBE_TOP), new Item(ANCESTRAL_ROBE_BOTTOM), new Item(JUSTICIAR_FACEGUARD), new Item(JUSTICIAR_CHESTGUARD), new Item(JUSTICIAR_LEGGUARDS), new Item(ELDER_MAUL), new Item(KODAI_WAND), new Item(GHRAZI_RAPIER),
        new Item(VESTAS_CHAINBODY), new Item(VESTAS_PLATESKIRT), new Item(AMULET_OF_TORTURE), new Item(NECKLACE_OF_ANGUISH), new Item(ARMADYL_FROSTSWORD), new Item(STATIUSS_PLATEBODY), new Item(STATIUSS_PLATELEGS),
        new Item(MORRIGANS_LEATHER_BODY), new Item(MORRIGANS_LEATHER_CHAPS), new Item(ZURIELS_ROBE_TOP), new Item(ZURIELS_ROBE_BOTTOM),new Item(ELDER_MAUL), new Item(GHRAZI_RAPIER), new Item(KODAI_WAND)
    };

    private static final Item[] UNCOMMON = new Item[]{
        new Item(TOXIC_STAFF_OF_THE_DEAD), new Item(STATIUSS_WARHAMMER), new Item(VESTAS_LONGSWORD), new Item(NEITIZNOT_FACEGUARD), new Item(PRIMORDIAL_BOOTS), new Item(PEGASIAN_BOOTS), new Item(ETERNAL_BOOTS), new Item(DRAGON_CLAWS), new Item(ARMADYL_GODSWORD), new Item(IMBUED_HEART)
    };

    private static final Item[] COMMON = new Item[]{
        new Item(DARK_BOW), new Item(DHAROKS_ARMOUR_SET), new Item(KARILS_ARMOUR_SET), new Item(AHRIMS_ARMOUR_SET), new Item(INFINITY_BOOTS), new Item(AMULET_OF_FURY), new Item(OCCULT_NECKLACE), new Item(RANGER_BOOTS), new Item(MAGES_BOOK), new Item(DRAGON_BOOTS), new Item(TOME_OF_FIRE)
    };

    public Item rollReward() {
        var roll = World.getWorld().random(100);
        //Reward rarity to base the server message on
        if (roll >= 95 && roll <= 100) {
            return Utils.randomElement(EXTREMELY_RARE);
        } else if (roll >= 75 && roll <= 94) {
            return Utils.randomElement(RARE);
        } else if (roll >= 35 && roll <= 74) {
            return Utils.randomElement(UNCOMMON);
        } else {
            return Utils.randomElement(COMMON);
        }
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        //Only perform actions if the object is a deadman chest and the option is 1.
        if (option == 1 && object.getId() == 27269 || object.getId() == DEADMAN_CHEST_27290) {
            if (player.skills().combatLevel() < 80) {
                player.message(Color.RED.wrap("You cannot loot this key without a combat level of 80 or above."));
                return false;
            }
            //Do stuff here
            player.faceObj(object);

            if (!player.inventory().contains(WILDERNESS_KEY)) {
                player.message("You need a wilderness key to open this chest.");
                return true;
            }

            //Important to lock the player during the action
            player.lock();

            Item rewardOne = rollReward();
            Item rewardTwo = rollReward();

            player.runFn(1, () -> {
                //Generate reward
                if(object.getId() == DEADMAN_CHEST_27290 && object.tile().equals(2337, 9904,1)) {
                    player.confirmDialogue(new String[]{"Are you sure you wish to open the chest?", "You will be " + Color.RED.wrap("red") + " skulled and teleblocked if you proceed."}, "", "proceed to open the chest.", "Nevermind.", () -> {
                        if(!player.inventory().contains(WILDERNESS_KEY)) {
                            return;
                        }
                        open(player, rewardOne, rewardTwo,true);
                    });
                    return;
                }

                open(player, rewardOne,null,false);
            });
            return true;
        }
        return false;
    }

    private void open(Player player, Item rewardOne, Item rewardTwo, boolean wilderness) {
        player.message("You unlock the chest with your key.");
        player.sound(51);
        player.animate(536);
        player.inventory().remove(new Item(WILDERNESS_KEY));

        if(wilderness) {
            player.teleblock(250,true);
            Skulling.assignSkullState(player, SkullType.RED_SKULL);
            if (player.hasPetOut("Deranged archaeologist")) {
                Skulling.assignSkullState(player, SkullType.NO_SKULL);
            }
        }

        int keysUsed = player.<Integer>getAttribOr(AttributeKey.WILDY_KEYS_OPENED, 0) + 1;
        player.putAttrib(AttributeKey.WILDY_KEYS_OPENED, keysUsed);

        if(rewardOne != null) {
            boolean amOverOne = rewardOne.getAmount() > 1;
            String amtString = amOverOne ? "x " + Utils.format(rewardOne.getAmount()) + "" : Utils.getAOrAn(rewardOne.name());
            String openedAt = wilderness ? "inside the member zone" : "at home";

            if(!player.getUsername().equalsIgnoreCase("Box test")) {
                String msg = "<img=506><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Wildy key</col>]</shad>: " + "<col=AD800F>" + player.getUsername() + " has received " + amtString + " " + rewardOne.name() + " " + openedAt + "!";
                World.getWorld().sendWorldMessage(msg);
            }

            //Check if item exists in collection log items
            KEYS.log(player, WILDERNESS_KEY, rewardOne);

            if(wilderness) {
                player.inventory().addOrDrop(rewardOne);
            } else {
                player.inventory().addOrBank(rewardOne);
            }
        }

        if(rewardTwo != null) {
            boolean amOverOne = rewardTwo.getAmount() > 1;
            String amtString = amOverOne ? "x " + Utils.format(rewardTwo.getAmount()) + "" : Utils.getAOrAn(rewardTwo.name());
            String openedAt = wilderness ? "inside the member zone" : "at home";

            //The user box test doesn't yell.
            if(!player.getUsername().equalsIgnoreCase("Box test")) {
                String msg = "<img=506><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Wildy key</col>]</shad>: " + "<col=AD800F>" + player.getUsername() + " has received " + amtString + " " + rewardTwo.name() + " " + openedAt + "!";
                World.getWorld().sendWorldMessage(msg);
            }

            //Check if item exists in collection log items
            KEYS.log(player, WILDERNESS_KEY, rewardTwo);

            if(wilderness) {
                player.inventory().addOrDrop(rewardTwo);
            } else {
                player.inventory().addOrBank(rewardTwo);
            }
        }

        //And unlock the player
        player.unlock();
    }
}
