package com.twisted.game.content.areas.dungeons.kraken_cove;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenInstanceD;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.ObjectIdentifiers.CREVICE_537;
import static com.twisted.util.ObjectIdentifiers.CREVICE_538;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 26, 2020
 */
public class KrakenCove extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if (obj.getId() == CREVICE_537) {
                if (CombatFactory.inCombat(player)) {
                    DialogueManager.sendStatement(player, "You can't go in here when under attack.");
                    player.message("You can't go in here when under attack.");
                } else {
                    player.teleport(new Tile(2280, 10022));
                }
                return true;
            } else if (obj.getId() == CREVICE_538) {
                if (player.getKrakenInstance() != null && player.getKrakenInstance().getInstance() != null) {
                    player.getDialogueManager().start(new Dialogue() {
                        @Override
                        protected void start(Object... parameters) {
                            send(DialogueType.OPTION,"Leave the instance? You cannot return.", "Yes, I want to leave.", "No, I'm staying for now.");
                            setPhase(0);
                        }

                        @Override
                        protected void select(int option) {
                            if(option == 1) {
                                player.teleport(new Tile(2280, 10016));
                                player.clearInstance(); // exit
                                stop();
                            } else if(option == 2) {
                                stop();
                            }
                        }
                    });
                } else {
                    player.teleport(new Tile(2280, 10016));
                    player.putAttrib(AttributeKey.TENTACLES_DISTURBED, 0);
                }
                return true;
            }
        } else if(option == 2) {
            if (obj.getId() == CREVICE_537) {
                player.getDialogueManager().start(new KrakenInstanceD());
                return true;
            }
        } else if(option == 3) {// Look inside
            if (obj.getId() == CREVICE_537) {
                int count = 0;
                for (Player p : World.getWorld().getPlayers()) {
                    if (p != null && p.tile().inArea(2269, 10023, 2302, 10046))
                        count++;
                    String strEnd = count == 1 ? "" : "s";
                    String isAre = count == 1 ? "is" : "are";
                    DialogueManager.sendStatement(player, "There " + isAre + " currently " + count + " player" + strEnd + " in the cave.");
                }
                return true;
            }
        }
        return false;
    }
}
