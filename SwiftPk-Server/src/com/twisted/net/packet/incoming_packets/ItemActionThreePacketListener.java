package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.content.items.RottenPotato;
import com.twisted.game.content.packet_actions.interactions.items.ItemActionThree;
import com.twisted.game.world.InterfaceConstants;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.util.CustomItemIdentifiers;

import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class ItemActionThreePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int itemId = packet.readShortA();
        final int slot = packet.readLEShortA();
        final int interfaceId = packet.readLEShortA();

        player.debugMessage(String.format("Third item action, itemId: %d slot: %d interfaceId: %d", itemId, slot, interfaceId));

        if (slot < 0 || slot > 27)
            return;
        Item item = player.inventory().get(slot);
        if (item != null && item.getId() == itemId) {

            if(item.getId() == ROTTEN_POTATO) {
                RottenPotato.onItemOption3(player);
                return;
            }
            if(itemId == SANGUINE_SCYTHE_OF_VITUR){//updateox
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION, "Un-Attach?", "Yes, Un-Attach Sang scythe of vitur.", "No, not right now.");
                        setPhase(0);
                    }

                    @Override
                    protected void select(int option) {
                        if(isPhase(0)) {
                            if(option == 1) {
                                if(!player.inventory().containsAll(SANGUINE_SCYTHE_OF_VITUR)) {
                                    stop();
                                    return;
                                }
                                player.inventory().remove(new Item(SANGUINE_SCYTHE_OF_VITUR), true);
                                player.inventory().add(new Item(25744), true);
                                player.inventory().add(new Item(SCYTHE_OF_VITUR), true);
                                player.message("You Un-attached Sang scythe of vitur and received scythe of vitur, sang Kit.");
                                stop();
                            } else if(option == 2) {
                                stop();
                            }
                        }
                    }
                });
            }
            if(itemId == 30183){//updatet3
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION, "Un-Attach?", "Yes, Un-Attach Twisted bow i.", "No, not right now.");
                        setPhase(0);
                    }

                    @Override
                    protected void select(int option) {
                        if(isPhase(0)) {
                            if(option == 1) {
                                if(!player.inventory().containsAll(TWISTED_BOW_I)) {
                                    stop();
                                    return;
                                }
                                player.inventory().remove(new Item(TWISTED_BOW_I), true);
                                player.inventory().add(new Item(29103), true);
                                player.inventory().add(new Item(20997), true);

                                player.message("You Un-attached twisted bow i and received Twisted bow, Twisted bow Kit I");
                                stop();
                            } else if(option == 2) {
                                stop();
                            }
                        }
                    }
                });
            }
            if(itemId == 25736){
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION, "Un-Attach?", "Yes, Un-Attach Holy Scythe.", "No, not right now.");
                        setPhase(0);
                    }

                    @Override
                    protected void select(int option) {
                        if(isPhase(0)) {
                            if(option == 1) {
                                if(!player.inventory().containsAll(CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR)) {
                                    stop();
                                    return;
                                }
                                player.inventory().remove(new Item(CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR), true);
                                player.inventory().add(new Item(SCYTHE_OF_VITUR), true);
                                player.inventory().add(new Item(HOLY_ORNAMENT_KIT), true);

                                player.message("You Un-attached Holy Scythe and received Scythe of Vitur , Scythe Kit ");
                                stop();
                            } else if(option == 2) {
                                stop();
                            }
                        }
                    }
                });
            }
            if (player.locked() || player.dead()) {
                return;
            }

            if (player.busy()) {
                return;
            }

            if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
                player.getBankPin().openIfNot();
                return;
            }

            if(player.askForAccountPin()) {
                player.sendAccountPinMessage();
                return;
            }

            player.afkTimer.reset();

            player.stopActions(false);
            player.putAttrib(AttributeKey.ITEM_SLOT, slot);
            player.putAttrib(AttributeKey.FROM_ITEM, player.inventory().get(slot));
            player.putAttrib(AttributeKey.ITEM_ID, item.getId());

            if (interfaceId == InterfaceConstants.INVENTORY_INTERFACE) {
                ItemActionThree.click(player, item);
            }
        }
    }
}
