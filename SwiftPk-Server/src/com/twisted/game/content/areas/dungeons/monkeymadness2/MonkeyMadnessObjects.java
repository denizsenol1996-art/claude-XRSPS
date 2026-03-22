package com.twisted.game.content.areas.dungeons.monkeymadness2;

import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.ObjectIdentifiers.*;

public class MonkeyMadnessObjects extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            // Cave exit:
            if (obj.getId() == ROPE_28687) {
                Chain.bound(null).runFn(1, () -> player.animate(828)).then(1, () -> player.teleport(2026, 5611));
                return true;
            }

            // Cave entrance:
            if (obj.getId() == CAVERN_ENTRANCE_28686) {
                Chain.bound(null).runFn(1, () -> {
                    player.message("You enter the cavern beneath the crash site.");
                    player.message("Why would you want to go in there?");
                }).then(1, () -> {
                    player.teleport(2129, 5646);
                });
                return true;
            }

            // Gnome stronghold exit
            if (obj.getId() == OPENING_28656) {
                if (player.tile().y >= 5568) {
                    player.teleport(2435, 3519);
                } else {
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... parameters) {
                            send(DialogueType.OPTION, "Leave the Gnome Stronghold?", "Yes.", "No.");
                        }

                        @Override
                        public void select(int option) {
                            if (option == 1) {
                                player.teleport(1987, 5568);
                            } else if (option == 2) {
                                stop();
                            }
                        }
                    });
                }
                return true;
            }
        }
        return false;
    }
}
