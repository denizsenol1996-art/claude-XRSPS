package com.twisted.game.world.entity.mob.npc.pets;

import com.twisted.game.content.consumables.potions.impl.OverloadPotion;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.pets.dialogue.NifflerD;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Color;
import com.twisted.util.Tuple;
import com.twisted.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Bart on 2/17/2016.
 */
public class PetAI {

    private static final Logger logger = LogManager.getLogger(PetAI.class);

    private static final boolean SMART_AI = true;

    public static boolean hasUnlocked(Player player, Pet pet) {
        if (pet.varbit == -1) {
            return false;
        }

        return player.isPetUnlocked(pet.varbit);
    }

    /**
     * note this deals with when the pet is following you on death, not for pet in inventory logic
     *
     * @param player
     * @param killer
     */
    public static void onDeath(Player player, Player killer) {
        Npc pet = player.pet();
        if (pet == null) {
            return;
        }
        boolean petCanTransfer = pet.petType().varbit == -1;

        // tradadable pets go to killer if skulled
        boolean transferPet = killer != null && petCanTransfer && killer.isRegistered() && killer != player && (player.getSkullType() == SkullType.RED_SKULL);

        if (transferPet) {
            Utils.sendDiscordInfoLog(player, "player "+player.getUsername()+" pet is redskull transferring "+pet.getMobName()+" to "+killer.getUsername(), "playerdeaths");
            //logger.info("player {} pet is redskull transferring {} to {}", player, pet.getMobName(), killer);
            // Always sent pet to either inventory or bank!
            killer.inventory().addOrBank(new Item(pet.petType().item));
            World.getWorld().unregisterNpc(pet);
            // clear old
            player.clearAttrib(AttributeKey.ACTIVE_PET_ITEM_ID);
            player.clearAttrib(AttributeKey.ACTIVE_PET);
            World.getWorld().unregisterNpc(pet);
        }
    }

    public static boolean onNpcOption1(Player player, Npc npc) {
        Npc pet = player.pet();
        Tuple<Integer, Player> ownerLink = npc.getAttribOr(AttributeKey.OWNING_PLAYER, new Tuple<>(-1, null).second());

        //Check if pet isn't null, check if the npc we're interacting with is indeed out pet.
        if (pet != null && npc.id() == pet.id()) {
            //Check if the pet hasn't despawned and that we are the pet owner.
            if (!pet.finished() && ownerLink.second() == player) {
                pickup(player);
                return true;
            }
        }
        return false;
    }

    public static boolean onNpcOption2(Player player, Npc npc) {
        Npc pet = player.pet();
        Tuple<Integer, Player> ownerLink = npc.getAttribOr(AttributeKey.OWNING_PLAYER, new Tuple<>(-1, null).second());

        //Check if pet isn't null, check if the npc we're interacting with is indeed out pet.
        if (pet != null && npc.id() == pet.id()) {
            //Check if the pet hasn't despawned and that we are the pet owner.
            if (!pet.finished() && ownerLink.second() == player) {

                //Niffler has custom option here
                if (pet.id() == Pet.NIFFLER.npc || pet.id() == Pet.ZIFFLER.npc) {
                    if (WildernessArea.inWilderness(player.tile())) {
                        player.message(Color.RED.wrap("You cannot bank items inside of the wilderness."));
                        return false;
                    }
                    player.getDialogueManager().start(new NifflerD());
                    return true;
                }

                //Pets with morph option
                if (pet.petType() == Pet.KALPHITE_PRINCESS || pet.petType() == Pet.KALPHITE_PRINCESS_2 || pet.petType() == Pet.VETION_JR_ORANGE || pet.petType() == Pet.VETION_JR_PURPLE || pet.petType() == Pet.TANZANITE_SNAKELING || pet.petType() == Pet.SNAKELING || pet.petType() == Pet.MAGMA_SNAKELING || pet.petType() == Pet.BABY_CHINCHOMPA_YELLOW
                    || pet.petType() == Pet.BABY_CHINCHOMPA_RED || pet.petType() == Pet.BABY_CHINCHOMPA_GREY || pet.petType() == Pet.BABY_CHINCHOMPA_BLACK || pet.petType() == Pet.IKKLE_HYDRA_GREEN || pet.petType() == Pet.IKKLE_HYDRA_BLUE || pet.petType() == Pet.IKKLE_HYDRA_RED || pet.petType() == Pet.IKKLE_HYDRA_BLACK) {
                    metamorph(player, npc);
                } else {
                    //Other pets have the pickup option
                    pickup(player);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean onNpcOption3(Player player, Npc npc) {
        Npc pet = player.pet();
        Tuple<Integer, Player> ownerLink = npc.getAttribOr(AttributeKey.OWNING_PLAYER, new Tuple<>(-1, null).second());

        //Check if pet isn't null, check if the npc we're interacting with is indeed out pet.
        if (pet != null && npc.id() == pet.id()) {
            //Check if the pet hasn't despawned and that we are the pet owner.
            if (!player.pet().finished() && ownerLink.second() == player) {

                //Checks passed pickup pet
                pickup(player);
            }
        }
        return false;
    }

    public static void spawnPet(Player player, Pet pet, boolean removeItem) {
        if (player.pet() != null && !player.pet().finished()) {
            if (player.inventory().getFreeSlots() < 1) {
                player.message("You need at least one free inventory slot.");
                return;
            }

            pickup(player); //Pickup old pet
            spawnPet(player, pet, true); //Spawn new pet
            return;
        }

        if (removeItem) {
            player.inventory().remove(new Item(pet.item), true);
        }

        player.message("You have set your pet down on the ground.");
        player.message("<img=505>" + Color.RED.tag() + "TIP:</col> You can also type " + Color.BLUE.tag() + "::claimpet</col> to pick up your pet.");
        player.animate(827);
        Npc petNpc = new Npc(pet.npc, player.tile().transform(0, 1));
        petNpc.walkRadius(-1); // Allow walking all across the map
        petNpc.putAttrib(AttributeKey.OWNING_PLAYER, new Tuple<>(player.getIndex(), player));
        petNpc.putAttrib(AttributeKey.PET_TYPE, pet);
        if (!player.hasPetOut(366) && pet.item == 24888) {
            OverloadPotion.apply(player);
        } else { //TODO remove effect if doppel was out

        }
        World.getWorld().registerNpc(petNpc);
        player.putAttrib(AttributeKey.ACTIVE_PET, petNpc);
        player.putAttrib(AttributeKey.ACTIVE_PET_ITEM_ID, pet.item);
        player.message("npcpetid:"+petNpc.getIndex());
        petNpc.getMovementQueue().follow(player);
        if (SMART_AI) {
            TaskManager.submit(new RegionalPetTask(petNpc));
        }
    }

    public static boolean onButton(Player player, int button) {
        if (button == 27668) {//Call follower
            callPet(player);
            return true;
        }
        return false;
    }

    private static void callPet(Player player) {
        if (!player.locked()) {
            if (player.pet() != null) {
                player.pet().teleport(player.tile());
                player.pet().getMovementQueue().follow(player);
                player.pet().graphic(333); // Custom, but otherwise they won't see it.
            } else {
                player.message("You do not have a follower.");
            }
        }
    }

    public static boolean onItemOption4(Player player, Item item) {
        for (Pet pet : Pet.values()) {
            if (item.getId() == pet.item) {
                spawnPet(player, pet, true);
                return true;
            }
        }
        return false;
    }

    public static void pickup(Player player) {
        Npc pet = player.pet();

        if (pet != null) {
            player.face(pet.tile());
            player.animate(827);
            Pet petType = pet.petType();
            if (petType == null) return;

            if (player.inventory().getFreeSlots() < 1) {
                player.message("You need at least one free inventory slot.");
                return;
            }

            player.inventory().add(new Item(petType.item), true);
            player.clearAttrib(AttributeKey.ACTIVE_PET_ITEM_ID);
            player.clearAttrib(AttributeKey.ACTIVE_PET);
            player.message("npcpetid:-1");
            World.getWorld().unregisterNpc(pet);
        }
    }

    public static void despawnOnLogout(Player player) {
        Npc pet = player.pet();

        if (pet != null) {
            player.face(pet.tile());
            Pet petType = pet.petType();
            if (petType == null)
                return;
            player.putAttrib(AttributeKey.ACTIVE_PET_ITEM_ID, petType.item);
            player.clearAttrib(AttributeKey.ACTIVE_PET);
            player.message("npcpetid:-1");
            World.getWorld().unregisterNpc(pet);
        }
    }

    public static void spawnOnLogin(Player player) {
        int petItemId = player.getAttribOr(AttributeKey.ACTIVE_PET_ITEM_ID, -1);
        if (petItemId != -1) {
            Pet petType = Pet.getPetByItem(petItemId);
            if (petType == null) return;
            spawnPet(player, petType, false);
        }
    }

    // Does not use pnpc, it fully changes the npc type and definition.
    public static void metamorph(Player player, Npc npc) {
        if (Pet.fromNpc(npc.id()).isEmpty()) {
            return;
        }
        var pet = Pet.fromNpc(npc.id()).get();

        if (pet.canMorph()) {
            var morphId = pet.morphId;
            // 1/10k chance that metamorph turns a chin into a golden chin.
            if ((pet == Pet.BABY_CHINCHOMPA_BLACK || pet == Pet.BABY_CHINCHOMPA_GREY || pet == Pet.BABY_CHINCHOMPA_RED) && World.getWorld().rollDie(10000, 1)) {
                morphId = Pet.BABY_CHINCHOMPA_YELLOW.npc;
            }

            npc.transmog(morphId);
            npc.putAttrib(AttributeKey.PET_TYPE, pet);
            player.putAttrib(AttributeKey.ACTIVE_PET, npc);
            player.putAttrib(AttributeKey.ACTIVE_PET_ITEM_ID, pet.item);
            player.message("npcpetid:"+npc.getIndex());
        }
    }

}
