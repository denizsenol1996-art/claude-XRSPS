package com.twisted.game.content.items.mystery_box.impl;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.twisted.game.world.entity.mob.npc.pets.Pet.*;

public class PetMysteryBox extends PacketInteraction {

    public static final int PET_MYSTERY_BOX = 16456;

    private static final Pet[] unlockable = new Pet[]{
        //Custom
        BABY_SQUIRT,
        BABY_DARK_BEAST,
        BABY_ABYSSAL_DEMON,

        //OSRS
        ABYSSAL_ORPHAN,
        CALLISTO_CUB,
        HELLPUPPY,
        KALPHITE_PRINCESS,
        PET_CHAOS_ELEMENTAL,
        PET_DAGANNOTH_PRIME,
        PET_DAGANNOTH_REX,
        PET_DAGGANOTH_SUPREME,
        PET_DARK_CORE,
        PET_GENERAL_GRAARDOR,
        PET_KRIL_TSUTSAROTH,
        PET_KREEARRA,
        PET_ZILYANA,
        PET_KRAKEN,
        PET_PENANCE_QUEEN,
        PET_SMOKE_DEVIL,
        SNAKELING,
        PRINCE_BLACK_DRAGON,
        SCORPIAS_OFFSPRING,
        TZREK_JAD,
        TZKAL_ZUK,
        VENENATIS_SPIDERLING,
        VETION_JR_PURPLE,
        NOON,
        MIDNIGHT,
        SKOTOS,
        VORKI,
        OLMLET,
        BABY_CHINCHOMPA_GREY,
        BEAVER,
        HERON,
        ROCK_GOLEM,
        GIANT_SQUIRREL,
        TANGLEROOT,
        ROCKY,
        RIFT_GUARDIAN_FIRE,
        HERBI,
        BLOODHOUND,
        CHOMPY_CHICK
    };

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == PET_MYSTERY_BOX) {
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... options) {
                        send(DialogueType.ITEM_STATEMENT, new Item(PET_MYSTERY_BOX), "", "The box you are about to open will give you a random", "pet you haven't unlocked.");
                        setPhase(0);
                    }

                    @Override
                    public void next() {
                        if (isPhase(0)) {
                            send(DialogueType.OPTION, "The pet is untradable, the box is tradable.", "Open the box", "No thank you");
                            setPhase(1);
                        }
                    }

                    @Override
                    public void select(int option) {
                        if (getPhase() == 1) {
                            if (option == 1) {
                                if(!player.inventory().contains(PET_MYSTERY_BOX)) {
                                    stop();
                                    return;
                                }
                                stop();
                                unlockPet(player);
                            } else if (option == 2) {
                                stop();
                            }
                        }
                    }
                });
                return true;
            }
        }
        return false;
    }

    private static void unlockPet(Player player) {
        // Create a shuffled list to begin with
        List<Pet> list = new LinkedList<>(Arrays.asList(unlockable));
        Collections.shuffle(list);

        // Remove any that we've unlocked (or in inventory, or bank)
        for (Pet pet : Pet.values()) {
            if ((pet.varbit != -1 && player.isPetUnlocked(pet.varbit)) || player.inventory().contains(pet.item) || player.getBank().contains(pet.item)) {
                list.remove(pet);
            }
        }

        // If there's any left, remove the box and add a pet.
        if (list.size() != 0) {
            if (player.inventory().remove(new Item(PET_MYSTERY_BOX), true)) {
                Pet reward = list.get(0);
                player.inventory().add(new Item(reward.item), true);
                if (!player.isPetUnlocked(reward.varbit)) {
                    player.addUnlockedPet(reward.varbit);
                }
                String more = list.size() == 1 ? "no" : ("" + (list.size() - 1));
                String s = (list.size() == 2) ? "" : "s";
                player.message("Congratulations! You have unlocked the pet '" + new Item(reward.item).name() + "'!");
                player.message("You can unlock " + more + " more pet" + s + ".");
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... options) {
                        send(DialogueType.ITEM_STATEMENT, new Item(reward.item), "", "Congratulations! You have unlocked the pet " + new Item(reward.item).name() + "!", "You can unlock " + more + " pet" + s + ".");
                        setPhase(0);
                    }

                    @Override
                    public void next() {
                        if (isPhase(0)) {
                            stop();
                        }
                    }
                });
                //The user box test doesn't yell.
                if(player.getUsername().equalsIgnoreCase("Box test")) {
                    return;
                }
                World.getWorld().sendWorldMessage("<img=1081><col=844e0d>" + player.getUsername() + " unlocked a pet from a Pet Box: " + new Item(reward.item).name() + ".");
            }
        } else {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... options) {
                    send(DialogueType.ITEM_STATEMENT, new Item(PET_MYSTERY_BOX), "", "You have no more pets to unlock!", "You can hold your box until a new pet comes out,", "or sell it to other players.");
                    setPhase(0);
                }

                @Override
                public void next() {
                    if (isPhase(0)) {
                        stop();
                    }
                }
            });
        }
    }

    public AttributeKey key() {
        return AttributeKey.PET_MYSTERY_BOXES_OPENED;
    }
}
