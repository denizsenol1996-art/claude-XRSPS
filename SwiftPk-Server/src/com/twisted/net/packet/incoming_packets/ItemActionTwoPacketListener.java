package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.content.items.RottenPotato;
import com.twisted.game.content.packet_actions.interactions.items.ItemActionTwo;
import com.twisted.game.world.InterfaceConstants;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

import static com.twisted.util.CustomItemIdentifiers.SANGUINE_TWISTED_BOW;
import static com.twisted.util.ItemIdentifiers.ROTTEN_POTATO;
import static com.twisted.util.ItemIdentifiers.TWISTED_BOW;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class ItemActionTwoPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int interfaceId = packet.readLEShortA();
        final int slot = packet.readLEShort();
        final int itemId = packet.readShortA();

        player.debugMessage(String.format("Second item action, interface: %d slot: %d itemId: %d", interfaceId, slot, itemId));

        if (slot < 0 || slot > 27) return;

        // Check if we used the item that we think we used.
        Item used = player.inventory().get(slot);
        if (used != null && used.getId() == itemId) {
            if(used.getId() == ROTTEN_POTATO) {
                RottenPotato.onItemOption2(player);
                return;
            }
            if(itemId == SANGUINE_TWISTED_BOW) {//updateox
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION, "Un-Attach?", "Yes, Un-Attach Sang Twisted bow.", "No, not right now.");
                        setPhase(0);
                    }

                    @Override
                    protected void select(int option) {
                        if(isPhase(0)) {
                            if(option == 1) {
                                if(!player.inventory().containsAll(SANGUINE_TWISTED_BOW)) {
                                    stop();
                                    return;
                                }
                                player.inventory().remove(new Item(SANGUINE_TWISTED_BOW), true);
                                player.inventory().add(new Item(25744), true);
                                player.inventory().add(new Item(TWISTED_BOW), true);
                                player.message("You Un-attached Sang Twisted bow and received Twisted bow, sang Kit.");
                                stop();
                            } else if(option == 2) {
                                stop();
                            }
                        }
                    }
                });
            }

            // Not possible when locked
            if (player.locked() || player.dead() || !player.inventory().hasAt(slot)) {
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
            player.putAttrib(AttributeKey.ITEM_ID, used.getId());

            if (interfaceId == InterfaceConstants.INVENTORY_INTERFACE) {
                ItemActionTwo.click(player, used);
            }
        }
    }
}
