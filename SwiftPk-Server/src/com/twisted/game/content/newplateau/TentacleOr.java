package com.twisted.game.content.newplateau;

import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.ItemIdentifiers.*;


public class TentacleOr extends PacketInteraction {//updatevoidor

    public static boolean REGULAR_DROP = false;



    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 2) {
            if (item.getId() == ABYSSAL_TENTACLE_OR) {
                player.message("Nothing interesting happens...");
                return true;
            }
            return false;
        }
        if (option == 4) {
            if (item.getId() == ABYSSAL_TENTACLE_OR) {
                if(REGULAR_DROP) {
                    return true;
                }
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.STATEMENT, "<col=7f0000>Warning!</col>", "Reverting the tentacle to a whip does NOT return the kraken tentacle, only the whip. Are you sure?");
                        setPhase(0);
                    }

                    @Override
                    protected void next() {
                        if (isPhase(0)) {
                            send(DialogueType.OPTION, "Are you sure you wish to do this?", "Yes, revert the tentacle to a abyssal whip.", "No, I'll keep my tentacle.");
                            setPhase(1);
                        }
                    }

                    @Override
                    protected void select(int option) {
                        if (isPhase(1)) {
                            if (option == 1) {
                                if (player.inventory().count(ABYSSAL_TENTACLE_OR) > 0) {
                                    // Apply inv change
                                    player.inventory().remove(new Item(ABYSSAL_TENTACLE_OR));
                                    player.inventory().add(new Item(ABYSSAL_WHIP));
                                    // Do some nice graphics
                                    player.animate(713);
                                }
                                stop();
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


}
