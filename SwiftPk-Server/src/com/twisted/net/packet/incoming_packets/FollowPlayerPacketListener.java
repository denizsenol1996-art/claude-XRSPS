package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

import java.lang.ref.WeakReference;

/**
 * Handles the follow player packet listener Sets the player to follow when the
 * packet is executed
 *
 * @author Gabriel Hannason
 */
public class FollowPlayerPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int index = packet.readLEShort();
        if (index < 0 || index > World.getWorld().getPlayers().capacity())
            return;
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

        boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);

        if (newAccount) {
            player.message("You have to select your game mode before you can continue.");
            return;
        }

        Player other = World.getWorld().getPlayers().get(index);


        if (other == null) {
            player.message("Unable to find player.");
        } else {
            // Make sure it's not us.
            if (other.getIndex() == player.getIndex() || other.getUsername().equalsIgnoreCase(player.getUsername())) {
                return;
            }

            if (!player.dead()) {
                player.face(other.tile());

                if (!other.dead()) {
                    player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(other));
                    player.putAttrib(AttributeKey.INTERACTION_OPTION, 3);
                    //Do actions...
                    player.getCombat().reset();
                    player.skills().stopSkillable();
                    player.setEntityInteraction(other);
                    player.getMovementQueue().follow(other);
                }
            }
        }
    }
}
