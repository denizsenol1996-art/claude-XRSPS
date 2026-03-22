package com.twisted.net.packet.incoming_packets;

import com.twisted.game.content.clan.ClanManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.util.Color;

public class InputFieldPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int component = packet.readInt();
        final String context = packet.readString();

        if (component < 0) {
            return;
        }

        player.debugMessage("[InputField] - Text: " + context + " Component: " + component);
        
        switch (component) {
            /* Clan Chat */
            case 47828:
                ClanManager.kickMember(player, context);
                break;
            case 47830:
                if (World.getWorld().getPlayerByName(context).isPresent()) {
                    Player other = World.getWorld().getPlayerByName(context).get();
                    player.setClanPromote(other.getUsername());
                    player.message("You are now promoting "+ Color.RED.wrap(other.getUsername())+"</col>.");
                }
                break;
            case 47843:
                ClanManager.changeSlogan(player, context);
                break;
            case 47845:
                int amount = context.length() == 0 ? 0 : Integer.parseInt(context);
                ClanManager.setMemberLimit(player, amount);
                break;
        }
    }

}
