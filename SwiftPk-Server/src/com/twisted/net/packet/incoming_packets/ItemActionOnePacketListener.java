package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.content.items.RottenPotato;
import com.twisted.game.content.packet_actions.interactions.items.ItemActionOne;
import com.twisted.game.world.InterfaceConstants;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

import static com.twisted.util.ItemIdentifiers.ANTIQUE_LAMP_13148;
import static com.twisted.util.ItemIdentifiers.ROTTEN_POTATO;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class ItemActionOnePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int interfaceId = packet.readUnsignedShort();
        final int id = packet.readShort();
        final int slot = packet.readShort();

        player.debugMessage(String.format("First item action, interface: %d id: %d slot: %d", interfaceId, id, slot));

        if (slot < 0 || slot > 27)
            return;

        final Item item = player.inventory().get(slot);

        if (item != null && item.getId() == id) {

            if(item.getId() == ROTTEN_POTATO) {
                RottenPotato.onItemOption1(player);
                //System.out.println("Block because of Rotten Potato.");
                return;
            }
            if(id == ANTIQUE_LAMP_13148){//LAMP
                if(player.mode().isDarklord()){
                    player.message("DarkLord cannot use this lamp.");
                    return;
                }
                player.getInterfaceManager().open(2808);
                return;
            }
            if (player.locked() || player.dead()) {
                //System.out.println("Player locked or dead.");
                return;
            }

            if (player.busy()) {
                //System.out.println("Player is busy.");
                return;
            }

            if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
                player.getBankPin().openIfNot();
                //System.out.println("Bank pin.");
                return;
            }

            if(player.askForAccountPin()) {
                player.sendAccountPinMessage();
                return;
            }

            player.afkTimer.reset();

            if (interfaceId == InterfaceConstants.INVENTORY_INTERFACE) {
                player.stopActions(false);
                player.putAttrib(AttributeKey.ITEM_SLOT, slot);
                player.putAttrib(AttributeKey.FROM_ITEM, player.inventory().get(slot));
                player.putAttrib(AttributeKey.ITEM_ID, item.getId());

                ItemActionOne.click(player, item);
            }
        }
    }
}
