package com.twisted.game.world.entity.mob.npc.pets.insurance;

import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.twisted.util.ItemIdentifiers.BLOOD_MONEY;
import static com.twisted.util.NpcIdentifiers.PROBITA;

/**
 * This class handles insurance for pets.
 * @author Patrick van Elderen | January, 04, 2021, 16:50
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class PetInsurance {

    /**
     * The player instance of this class
     */
    private final Player player;

    public PetInsurance(Player player) {
        this.player = player;
    }

    private static final int PET_INSURANCE_WIDGET = 29000;
    private static final int PETS_INSURED_CONTAINER = 29011;
    private static final int PETS_RECLAIM_CONTAINER = 29013;
    private static final int INSURED_PETS_STRING = 29008;
    private static final int RECLAIMABLE_PETS_STRING = 29009;
    private static final int SCROLL_WIDGET_INSURED = 29010;
    private static final int SCROLL_WIDGET_RECLAIM = 29012;

    public static final int RECLAIM_FEE = 35_000;

    public void openInsuranceInterface() {
        if (player.getInsuredPets() == null || player.getInsuredPets().isEmpty()) {
            player.getPacketSender().sendString(INSURED_PETS_STRING, "None");
        } else {
            player.getPacketSender().sendString(INSURED_PETS_STRING, "");
        }

        ArrayList<Integer> reclaimablePets = getReclaimablePets();

        if (reclaimablePets == null || reclaimablePets.isEmpty()) {
            player.getPacketSender().sendString(RECLAIMABLE_PETS_STRING, "None");
        } else {
            player.getPacketSender().sendString(RECLAIMABLE_PETS_STRING, "");
        }

        if (player.getInsuredPets() != null && !player.getInsuredPets().isEmpty()) {
            Item[] insuredItems = new Item[player.getInsuredPets().size()];
            int index = 0;
            for (int id : player.getInsuredPets()) {
                insuredItems[index] = new Item(id);
                index++;
            }
            player.getPacketSender().setScrollPosition(SCROLL_WIDGET_INSURED, 0,(int) (Math.ceil((float)insuredItems.length / 7) + 1) * 42);
            player.getPacketSender().sendItemOnInterface(PETS_INSURED_CONTAINER, Arrays.asList(insuredItems));
        } else {
            player.getPacketSender().setScrollPosition(SCROLL_WIDGET_INSURED, 0,0);
            player.getPacketSender().sendItemOnInterface(PETS_INSURED_CONTAINER, Collections.emptyList());
        }

        if (reclaimablePets != null && !reclaimablePets.isEmpty()) {
            Item[] reclaimableItems = new Item[reclaimablePets.size()];
            int index = 0;
            for (int id : reclaimablePets) {
                reclaimableItems[index] = new Item(id);
                index++;
            }
            player.getPacketSender().sendItemOnInterface(PETS_RECLAIM_CONTAINER, Arrays.asList(reclaimableItems));
            player.getPacketSender().setScrollPosition(SCROLL_WIDGET_RECLAIM, 0,(int) (Math.ceil((float)reclaimableItems.length / 7) + 1) * 42);
        } else {
            player.getPacketSender().setScrollPosition(SCROLL_WIDGET_RECLAIM, 0,0);
            player.getPacketSender().sendItemOnInterface(PETS_RECLAIM_CONTAINER, Collections.emptyList());
        }
        player.getInterfaceManager().open(PET_INSURANCE_WIDGET);
    }

    public void reclaimPet(int id) {
        Pet pet = Pet.getPetByItem(id);
        if(pet == null) {
            return;
        }

        int npc = Pet.getBasePetNpcId(pet.npc);
        id = Pet.getByNpc(npc).item;

        if (getReclaimablePets().contains(id)) {
            boolean canClaimPet = false;
            int currencyInInventory = player.inventory().count(BLOOD_MONEY);
            if (currencyInInventory > 0) {
                if(currencyInInventory >= RECLAIM_FEE) {
                    canClaimPet = true;
                    player.inventory().remove(new Item(BLOOD_MONEY, RECLAIM_FEE),true);
                }
            }

            String currency = "bm";
            if(!canClaimPet) {
                player.message("<col=ca0d0d>You don't have enough "+currency+" to reclaim your pet!");
            } else if (player.inventory().getFreeSlots() == 0) {
                player.message("<col=ca0d0d>You don't have enough room in your inventory to reclaim your pet!");
            } else {
                player.message("You've reclaimed your pet!");
                player.inventory().add(new Item(id),true);
                openInsuranceInterface();
            }
        }
    }

    public ArrayList<Integer> getReclaimablePets() {
        ArrayList<Integer> reclaimablePets = new ArrayList<>();
        for (int id : player.getInsuredPets()) {
            boolean caught = false;
            ArrayList<Integer> itemAlternatives = Pet.getItemAlternatives(id);
            if(itemAlternatives == null) {
                return null;
            }
            for (int index : itemAlternatives) {
                Npc pet = player.pet();
                if (player.inventory().contains(index) || player.getBank().contains(index) || (pet != null && Pet.fromNpc(pet.id()).isPresent() && Pet.fromNpc(pet.id()).get().item == index)) {
                    caught = true;
                }
            }
            if (!caught) {
                reclaimablePets.add(id);
            }
        }

        return reclaimablePets;
    }

    public static boolean onContainerAction(Player player, Item item, int interfaceId, int option) {
        if(option == 1) {
            if(interfaceId == PETS_RECLAIM_CONTAINER) {
                player.getPetInsurance().reclaimPet(item.getId());
                return true;
            }
        }
        return false;
    }

    public static boolean handleNpc(Player player, Npc npc, int option) {
        if(option == 1) {
            if(npc.id() == PROBITA) {
                player.getDialogueManager().start(new ProbitaD());
                return true;
            }
        }
        if(option == 2) {
            if(npc.id() == PROBITA) {
                player.getPetInsurance().openInsuranceInterface();
                return true;
            }
        }
        return false;
    }
}
