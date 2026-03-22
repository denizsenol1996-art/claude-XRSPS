package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.content.interfaces.BonusesInterface;
import com.twisted.game.content.mechanics.Transmogrify;
import com.twisted.game.content.mechanics.item_simulator.ItemSimulatorUtility;
import com.twisted.game.world.InterfaceConstants;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.equipment.EquipmentInfo;
import com.twisted.game.world.items.container.looting_bag.LootingBag;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.net.packet.interaction.PacketInteractionManager;
import com.twisted.util.ItemIdentifiers;

/**
 * This packet listener manages the equip action a player
 * executes when wielding or equipping an item.
 *
 * @author relex lawl
 */

public class EquipPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int id = packet.readShort();
        int slot = packet.readShortA();
        int interfaceId = packet.readShortA();

        boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);

        if (newAccount) {
            player.message("You have to select your game mode before you can continue.");
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

        if (slot < 0 || slot > 27)
            return;
        Item item = player.inventory().get(slot);
        if (item != null && item.getId() == id && !player.locked() && !player.dead()) {
            if(player.getInterfaceManager().isInterfaceOpen(ItemSimulatorUtility.WIDGET_ID)) {
                player.message("Close this interface before trying to equip your "+item.unnote().name()+".");
                return;
            }

            //Close all other interfaces except for the {@code Equipment.EQUIPMENT_SCREEN_INTERFACE_ID} one..
            if (!player.getInterfaceManager().isClear() && !player.getInterfaceManager().isInterfaceOpen(InterfaceConstants.EQUIPMENT_SCREEN_INTERFACE_ID)) {
                player.getInterfaceManager().close(false);
            }

            //ibag
            if (item.getId() == ItemIdentifiers.LOOTING_BAG || item.getId() == LootingBag.OPEN_LOOTING_BAG || item.getId() == 30099 || item.getId() == LootingBag.OPEN_LOOTING_BAG_I) {
                player.getLootingBag().open();
                return;
            }

            if (interfaceId == InterfaceConstants.INVENTORY_INTERFACE) {
                player.debugMessage("Equip ItemId=" + id + " Slot=" + slot + " InterfaceId=" + interfaceId);

                //Stop skilling..
                player.skills().stopSkillable();

                Transmogrify.onItemEquip(player, item);

                if(PacketInteractionManager.onEquipItem(player, item)) {
                    return;
                }

                EquipmentInfo info = World.getWorld().equipmentInfo();
                if(info != null) {
                    player.getEquipment().equip(slot);
                    BonusesInterface.sendBonuses(player);
                }
            }
        }
    }
}
