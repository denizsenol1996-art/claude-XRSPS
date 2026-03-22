package com.twisted.game.world.entity.mob.npc.pets;

import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.ItemIdentifiers;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Zerikoth
 * @Since oktober 10, 2020
 */
public class PetPaint {

    private static final int BLACK_PAINT = ItemIdentifiers.DESERT_PAINTING;
    private static final int WHITE_PAINT = ItemIdentifiers.ISAFDAR_PAINTING;

    /**
     * Paints the players pet white or black making it untradeable.
     * @param player The player painting the pet.
     * @param use The paint or pet item.
     * @param with The pet item or paint in reverse.
     */
    public static boolean paintPet(Player player, Item use, Item with) {
        //Paint general graardor pet
        if ((use.getId() == BLACK_PAINT || with.getId() == BLACK_PAINT) && (use.getId() == PET_GENERAL_GRAARDOR || with.getId() == PET_GENERAL_GRAARDOR)) {
            //Safety
            if(!player.inventory().containsAll(BLACK_PAINT, PET_GENERAL_GRAARDOR)) {
                return false;
            }

            player.inventory().remove(BLACK_PAINT);
            player.inventory().remove(PET_GENERAL_GRAARDOR);
            player.inventory().add(new Item(Pet.PET_GENERAL_GRAARDOR_BLACK.item));
            return true;
        }

        //Paint K'ril tsutsaroth pet
        if ((use.getId() == BLACK_PAINT || with.getId() == BLACK_PAINT) && (use.getId() == PET_KRIL_TSUTSAROTH || with.getId() == PET_KRIL_TSUTSAROTH)) {
            //Safety
            if(!player.inventory().containsAll(BLACK_PAINT, PET_KRIL_TSUTSAROTH)) {
                return false;
            }

            player.inventory().remove(BLACK_PAINT);
            player.inventory().remove(PET_KRIL_TSUTSAROTH);
            player.inventory().add(new Item(Pet.PET_KRIL_TSUTSAROTH_BLACK.item));
            return true;
        }

        //Paint Commander Zilyana pet
        if ((use.getId() == WHITE_PAINT || with.getId() == WHITE_PAINT) && (use.getId() == PET_ZILYANA || with.getId() == PET_ZILYANA)) {
            //Safety
            if(!player.inventory().containsAll(WHITE_PAINT, PET_ZILYANA)) {
                return false;
            }

            player.inventory().remove(WHITE_PAINT);
            player.inventory().remove(PET_ZILYANA);
            player.inventory().add(new Item(Pet.PET_ZILYANA_WHITE.item));
            return true;
        }

        //Paint Kree'Arra pet
        if ((use.getId() == WHITE_PAINT || with.getId() == WHITE_PAINT) && (use.getId() == PET_KREEARRA || with.getId() == PET_KREEARRA)) {
            //Safety
            if(!player.inventory().containsAll(WHITE_PAINT, PET_KREEARRA)) {
                return false;
            }

            player.inventory().remove(WHITE_PAINT);
            player.inventory().remove(PET_KREEARRA);
            player.inventory().add(new Item(Pet.PET_KREEARRA_WHITE.item));
            return true;
        }
        return false;
    }

    public static void wipePaint(Player player, Item item) {
        if(Pet.PET_GENERAL_GRAARDOR_BLACK.item == item.getId()) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, "Are you sure you wish to wipe the paint?", "Yes.", "No.");
                    setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if(isPhase(0)) {
                        if(option == 1) {
                            if (!player.inventory().contains(Pet.PET_GENERAL_GRAARDOR_BLACK.item)) {
                                stop();
                                return;
                            }
                            //Remove painted version
                            player.inventory().remove(Pet.PET_GENERAL_GRAARDOR_BLACK.item);
                            //Add normal version
                            player.inventory().add(new Item(Pet.PET_GENERAL_GRAARDOR.item));
                            stop();
                        } else if(option == 2) {
                            stop();
                        }
                    }
                }
            });
        }

        if(Pet.PET_KRIL_TSUTSAROTH_BLACK.item == item.getId()) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, "Are you sure you wish to wipe the paint?", "Yes.", "No.");
                    setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if(isPhase(0)) {
                        if(option == 1) {
                            if (!player.inventory().contains(Pet.PET_KRIL_TSUTSAROTH_BLACK.item)) {
                                stop();
                                return;
                            }
                            //Remove painted version
                            player.inventory().remove(Pet.PET_KRIL_TSUTSAROTH_BLACK.item);
                            //Add normal version
                            player.inventory().add(new Item(Pet.PET_KRIL_TSUTSAROTH.item));
                            stop();
                        } else if(option == 2) {
                            stop();
                        }
                    }
                }
            });
        }

        if(Pet.PET_ZILYANA_WHITE.item == item.getId()) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, "Are you sure you wish to wipe the paint?", "Yes.", "No.");
                    setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if(isPhase(0)) {
                        if(option == 1) {
                            if (!player.inventory().contains(Pet.PET_ZILYANA_WHITE.item)) {
                                stop();
                                return;
                            }
                            //Remove painted version
                            player.inventory().remove(Pet.PET_ZILYANA_WHITE.item);
                            //Add normal version
                            player.inventory().add(new Item(Pet.PET_ZILYANA.item));
                            stop();
                        } else if(option == 2) {
                            stop();
                        }
                    }
                }
            });
        }

        if(Pet.PET_KREEARRA_WHITE.item == item.getId()) {
            player.getDialogueManager().start(new Dialogue() {
                @Override
                protected void start(Object... parameters) {
                    send(DialogueType.OPTION, "Are you sure you wish to wipe the paint?", "Yes.", "No.");
                    setPhase(0);
                }

                @Override
                protected void select(int option) {
                    if(isPhase(0)) {
                        if(option == 1) {
                            if (!player.inventory().contains(Pet.PET_KREEARRA_WHITE.item)) {
                                stop();
                                return;
                            }
                            //Remove painted version
                            player.inventory().remove(Pet.PET_KREEARRA_WHITE.item);
                            //Add normal version
                            player.inventory().add(new Item(Pet.PET_KREEARRA.item));
                            stop();
                        } else if(option == 2) {
                            stop();
                        }
                    }
                }
            });
        }
    }
}
