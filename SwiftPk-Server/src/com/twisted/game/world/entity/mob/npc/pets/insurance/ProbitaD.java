package com.twisted.game.world.entity.mob.npc.pets.insurance;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.dialogue.Expression;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.items.Item;
import com.twisted.util.Utils;

import static com.twisted.util.ItemIdentifiers.BLOOD_MONEY;
import static com.twisted.util.NpcIdentifiers.PROBITA;

/**
 * Probita runs the pet insurance bureau in East Ardougne in the small building next to Aemad's Adventuring Supplies.
 * She will insure a player's pet at a one-time cost of 35m bm per pet.
 * If a player loses their pet, and it is insured by Probita, they may reclaim the pet for 35m bm.
 * @author Patrick van Elderen | January, 04, 2021, 17:54
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class ProbitaD extends Dialogue {

    private int petId;

    private final int INSURANCE_FEE = 35_000;

    private boolean canInsure(int id) {
        if(Pet.fromNpc(id).isEmpty()) {
            return false;
        }
        return Pet.fromNpc(id).get().varbit != -1;
    }

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.NPC_STATEMENT, PROBITA, Expression.DEFAULT, "Welcome to the pet insurance bureau.", "How can I help you?");
        setPhase(0);
    }

    @Override
    protected void next() {
        if (getPhase() == 0) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Tell me about pet insurance.", "I've lost a pet. Have you got it?", "I have a pet that I'd like to insure.", "What pets have I insured?", "Maybe another time.");
            setPhase(1);
        } else if (getPhase() == 2) {
            send(DialogueType.NPC_STATEMENT, PROBITA, Expression.DEFAULT, "My insurance fee is "+ Utils.formatNumber(INSURANCE_FEE)+" bm. Once you've paid", "that, the pet's insured forever, and you can reclaim it", "here unlimited times for a reclamation fee of "+ Utils.formatNumber(PetInsurance.RECLAIM_FEE)+"", " bm whenever you lose your pet upon death.");
            setPhase(3);
        } else if (getPhase() == 3) {
            stop();
        } else if (getPhase() == 4) {
            Npc pet = player.pet();
            if (pet != null) {
                petId = Pet.getBasePetNpcId(pet.id());
                int pet_item = Pet.fromNpc(petId).get().item;

                if(!canInsure(petId)) {
                    send(DialogueType.NPC_STATEMENT, PROBITA, Expression.CALM_TALK, "You can't insure your " + pet.def().name + ".");
                    setPhase(3);
                    return;
                }

                if (player.getInsuredPets().contains(pet_item)) {
                    send(DialogueType.NPC_STATEMENT, PROBITA, Expression.CALM_TALK, "You already insured your " + pet.def().name + ".");
                    setPhase(3);
                } else if (Pet.fromNpc(petId).isPresent()) {
                    send(DialogueType.NPC_STATEMENT, PROBITA, Expression.CALM_TALK, "My insurance fee is "+ Utils.formatNumber(INSURANCE_FEE)+" bm. Once you've paid", "that, the pet's insured forever, and you can reclaim it", "here unlimited times for a reclamation fee of "+ Utils.formatNumber(INSURANCE_FEE)+" ", "bm whenever you lose your pet upon death.");
                    setPhase(5);
                }
            } else {
                send(DialogueType.NPC_STATEMENT, PROBITA, Expression.CALM_TALK, "You don't have any pet that is following you", "at the moment.");
                setPhase(3);
            }
        } else if (getPhase() == 5) {
            Npc pet = player.pet();
            if(pet == null) {
                return;
            }
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Insure your " + pet.def().name + " for "+ Utils.formatNumber(INSURANCE_FEE)+" bm.", "Maybe another time.");
            setPhase(6);
        }
    }

    @Override
    protected void select(int option) {
        if (getPhase() == 1) {
            switch (option) {
                case 1 -> {
                    send(DialogueType.PLAYER_STATEMENT, Expression.DEFAULT, "Tell me about pet insurance.");
                    setPhase(2);
                }
                case 2, 4 -> {
                    stop();
                    player.getPetInsurance().openInsuranceInterface();
                }
                case 3 -> {
                    send(DialogueType.PLAYER_STATEMENT, Expression.DEFAULT, "I have a pet that I'd like to insure.");
                    setPhase(4);
                }
                case 5 -> stop();
            }
        } else if (getPhase() == 6) {
            switch (option) {
                case 1 -> {
                    boolean canClaimPet = false;
                    int bmInInventory = player.inventory().count(BLOOD_MONEY);
                    if (bmInInventory > 0) {
                        if (bmInInventory >= INSURANCE_FEE) {
                            canClaimPet = true;
                            player.inventory().remove(new Item(BLOOD_MONEY, INSURANCE_FEE), true);
                        }
                    }

                    if (canClaimPet) {
                        NpcDefinition npcDefinition = World.getWorld().definitions().get(NpcDefinition.class, petId);
                        player.addInsuredPet(Pet.fromNpc(petId).get().item);
                        send(DialogueType.ITEM_STATEMENT, new Item(Pet.fromNpc(petId).get().item), "", "Your " + npcDefinition.name + " is now insured.", "You can reclaim it from Probita if you ever lose it.");
                    } else {
                        send(DialogueType.NPC_STATEMENT, PROBITA, Expression.CALM_TALK, "You don't have enough bm to do this.");
                    }
                    setPhase(3);
                }
                case 2 -> stop();
            }
        }
    }
}
