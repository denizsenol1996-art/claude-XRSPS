package com.twisted.net.packet.incoming_packets;

import com.twisted.game.content.items.RottenPotato;
import com.twisted.game.content.skill.impl.slayer.content.BagOfSalt;
import com.twisted.game.content.skill.impl.slayer.content.FungicideSpray;
import com.twisted.game.content.skill.impl.slayer.content.IceCooler;
import com.twisted.game.content.skill.impl.slayer.content.RockHammer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.route.routes.TargetRoute;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.net.packet.interaction.PacketInteractionManager;

import java.lang.ref.WeakReference;

/**
 * @author PVE
 * @Since augustus 24, 2020
 */
public class ItemOnNpcPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int itemId = packet.readShortA();
        final int npcIdx = packet.readShortA();
        final int itemSlot = packet.readLEShort();

        Npc npc = World.getWorld().getNpcs().get(npcIdx);
        if (npc == null) {
            return;
        }

        // priority code
        if(itemId == 5733 && RottenPotato.onItemOnMob(player, npc)) {
            return;
        }

        if ( player.locked() || player.dead()) {
            return;
        }

        if(itemId == 1735 && npc.getMobName().equalsIgnoreCase("Sheep")){
            player.animate(893);
            player.getInventory().add(1737, 1);
            npc.forceChat("Get your hand off me :(");//update3

        }
        player.stopActions(true);
        player.setEntityInteraction(npc);

        if (npc.dead()) {
            return;
        }

        Item item = player.inventory().get(itemSlot);
        if (item == null || item.getId() != itemId)
            return;

        // Store attribs
        player.putAttrib(AttributeKey.ITEM_SLOT, itemSlot);
        player.putAttrib(AttributeKey.INTERACTION_OPTION, -1); // secret key
        player.putAttrib(AttributeKey.ITEM_ID, itemId);
        player.putAttrib(AttributeKey.FROM_ITEM, item);
        player.putAttrib(AttributeKey.TARGET, new WeakReference<>(npc));

        //Do actions below
        TargetRoute.set(player, npc, () -> {
            player.face(npc.tile());

            if (PacketInteractionManager.checkItemOnNpcInteraction(player, item, npc)) {
                return;
            }

            if (RockHammer.onItemOnNpc(player, npc)) {
                return;
            }

            if(IceCooler.onItemOnNpc(player, npc)) {
                return;
            }

            if (BagOfSalt.onItemOnNpc(player, npc)) {
                return;
            }

            if (FungicideSpray.onItemOnNpc(player, npc)) {
                return;
            }

            player.message("Nothing interesting happens...");
        });
    }
}
