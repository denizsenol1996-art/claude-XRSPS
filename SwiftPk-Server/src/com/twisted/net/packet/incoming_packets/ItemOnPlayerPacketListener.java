package com.twisted.net.packet.incoming_packets;

import com.twisted.game.content.items.RottenPotato;
import com.twisted.game.content.skill.impl.slayer.slayer_partner.SlayerPartner;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.net.packet.interaction.PacketInteractionManager;
import com.twisted.util.ItemIdentifiers;

import java.lang.ref.WeakReference;

/**
 * @author PVE
 * @Since augustus 24, 2020
 */
public class ItemOnPlayerPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int interfaceId = packet.readUnsignedShortA();
        int playerIdx = packet.readUnsignedShort();
        int itemId = packet.readUnsignedShort();
        int slot = packet.readLEShort();

        if (player.locked()) {
            return;
        }

        player.debugMessage(String.format("itemId %d on player from %d pid %d inv-slot %d.", itemId, interfaceId, playerIdx, slot));

        Player other = World.getWorld().getPlayers().get(playerIdx);

        if (other == null) {
            player.message("Unable to find player.");
        } else {

            if(itemId == 5733 && RottenPotato.onItemOnMob(player, other)) {
                return;
            }

            player.stopActions(false);

            // Verify item
            Item item = player.inventory().get(slot);
            if (item == null || item.getId() != itemId) {
                return;
            }

            if (!player.locked() && !player.dead()) {
                player.face(other.tile());
                if (!other.dead()) {
                    player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(other));
                    player.putAttrib(AttributeKey.INTERACTION_OPTION, -1); // Special action
                    player.putAttrib(AttributeKey.ITEM_SLOT, slot);
                    player.putAttrib(AttributeKey.ITEM_ID, item.getId());
                    player.putAttrib(AttributeKey.FROM_ITEM, item);

                    //Do actions...

                    if (PacketInteractionManager.checkItemOnPlayerInteraction(player, item, other)) {
                        return;
                    }

                    if(item.getId() == ItemIdentifiers.ENCHANTED_GEM) {
                        SlayerPartner.invite(player, other, false);
                        return;
                    }
                }
            }
        }
    }
}
