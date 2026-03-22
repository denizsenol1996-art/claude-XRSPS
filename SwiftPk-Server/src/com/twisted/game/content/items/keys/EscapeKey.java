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
import static com.twisted.util.ObjectIdentifiers.REWARD_CHEST_36087;

public class EscapeKey extends PacketInteraction {
    private static final Item[] EXTREMELY_RARE = new Item[]{
        new Item(ANCESTRAL_HAT), new Item(ANCESTRAL_ROBE_TOP), new Item(ANCESTRAL_ROBE_BOTTOM), new Item(EPIC_PET_BOX), new Item(GRAND_MYSTERY_BOX), new Item(TEN_DOLLAR_BOND)
    };

    private static final Item[] RARE = new Item[]{
        new Item(LARRANS_KEY_TIER_III), new Item(KEY_OF_BOXES), new Item(LEGENDARY_MYSTERY_BOX), new Item(MOLTEN_MYSTERY_BOX), new Item(MYSTERY_TICKET), new Item(FIVE_DOLLAR_BOND), new Item(VESTAS_CHAINBODY), new Item(VESTAS_PLATESKIRT),
    };

    private static final Item[] UNCOMMON = new Item[]{
        new Item(LARRANS_KEY_TIER_II), new Item(ZENYTE_MYSTERY_BOX), new Item(REVENANT_MYSTER_BOX), new Item(DONATOR_MYSTERY_BOX),  new Item(TOXIC_STAFF_OF_THE_DEAD),  new Item(ARMADYL_CROSSBOW),  new Item(DRAGON_CLAWS), new Item(PRIMORDIAL_BOOTS), new Item(PEGASIAN_BOOTS), new Item(ETERNAL_BOOTS)
    };

    private static final Item[] COMMON = new Item[]{
        new Item(LARRANS_KEY_TIER_I), new Item(ARMOUR_MYSTERY_BOX),  new Item(WEAPON_MYSTERY_BOX),  new Item(ANTIQUE_EMBLEM_TIER_1),
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
        //Only perform actions if the object is a escape key chest and the option is 1.
        if (option == 1 && object.getId() == 36087 || object.getId() == REWARD_CHEST_36087) {
            //Do stuff here
            player.faceObj(object);

            if (!player.inventory().contains(ESCAPE_KEY)) {
                player.message("You need an escape key to open this chest.");
                return true;
            }

            //Important to lock the player during the action
            player.lock();

            Item rewardOne = rollReward();
            Item rewardTwo = rollReward();

            player.runFn(1, () -> {
                //Generate reward
                if(object.getId() == REWARD_CHEST_36087 && object.tile().equals(2337, 9904,1)) {
                    player.confirmDialogue(new String[]{"Are you sure you wish to open the chest?", "You will be " + Color.RED.wrap("red") + " skulled and teleblocked if you proceed."}, "", "proceed to open the chest.", "Nevermind.", () -> {
                        if(!player.inventory().contains(ESCAPE_KEY)) {
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
        player.inventory().remove(new Item(ESCAPE_KEY));

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
            String openedAt = wilderness ? "inside the Escape Zone" : "inside the Escape Zone";

            if(!player.getUsername().equalsIgnoreCase("Box test")) {
                String msg = "<img=506><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Escape key</col>]</shad>: " + "<col=AD800F>" + player.getUsername() + " has received " + amtString + " " + rewardOne.name() + " " + openedAt + "!";
                World.getWorld().sendWorldMessage(msg);
            }

            //Check if item exists in collection log items
            KEYS.log(player, ESCAPE_KEY, rewardOne);

            if(wilderness) {
                player.inventory().addOrDrop(rewardOne);
            } else {
                player.inventory().addOrBank(rewardOne);
            }
        }

        if(rewardTwo != null) {
            boolean amOverOne = rewardTwo.getAmount() > 1;
            String amtString = amOverOne ? "x " + Utils.format(rewardTwo.getAmount()) + "" : Utils.getAOrAn(rewardTwo.name());
            String openedAt = wilderness ? "inside the Escape Zone" : "inside the Escape Zone";

            //The user box test doesn't yell.
            if(!player.getUsername().equalsIgnoreCase("Box test")) {
                String msg = "<img=506><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Escape key</col>]</shad>: " + "<col=AD800F>" + player.getUsername() + " has received " + amtString + " " + rewardTwo.name() + " " + openedAt + "!";
                World.getWorld().sendWorldMessage(msg);
            }

            //Check if item exists in collection log items
            KEYS.log(player, ESCAPE_KEY, rewardTwo);

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
