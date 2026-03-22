package com.twisted.net.packet.incoming_packets;

import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

/**
 * This packet listener handles the action when pressing
 * a special attack bar.
 * @author Professor Oak
 */

public class SpecialAttackPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int specialBarButton = packet.readInt();

        if (player == null || player.dead()) {
            return;
        }
        player.afkTimer.reset();

        if(CombatSpecial.specialAttackButton(player, specialBarButton)) {
            return;
        }
        CombatSpecial.activate(player);
    }
}
