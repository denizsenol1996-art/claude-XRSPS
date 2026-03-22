package com.twisted.net.packet.incoming_packets;

import com.twisted.game.world.entity.mob.Flag;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppearanceChangePacketListener implements PacketListener {

    private static final Logger logger = LogManager.getLogger(AppearanceChangePacketListener.class);

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.dead()) {
            return;
        }

        player.afkTimer.reset();

        try {

            final boolean gender = packet.readUnsignedByte() == 1;
            final int head = packet.readUnsignedByte();
            final int jaw = packet.readUnsignedByte();
            final int torso = packet.readUnsignedByte();
            final int arms = packet.readUnsignedByte();
            final int hands = packet.readUnsignedByte();
            final int legs = packet.readUnsignedByte();
            final int feet = packet.readUnsignedByte();
            final int hairColor = packet.readUnsignedByte();
            final int torsoColor = packet.readUnsignedByte();
            final int legsColor = packet.readUnsignedByte();
            final int feetColor = packet.readUnsignedByte();
            final int skinColor = packet.readUnsignedByte();

            if(!gender){
                player.message("head: "+head);
                if(head > 9 || head < 0){
                    player.message("This hairstyle is disabled.");
                    return;
                }

            }

            if (skinColor == 10 && !player.getMemberRights().isRubyOrGreater(player)) {
                player.message("You need to be a Ruby Member to use this skin!");
                return;
            }

            if (skinColor == 11 && !player.getMemberRights().isSaphireOrGreater(player)) {
                player.message("You need to be a Sapphire member to use this skin!");
                return;
            }

            if (skinColor == 12 && !player.getMemberRights().isEmeraldOrGreater(player)) {
                player.message("You need to be a Emerald member to use this skin!");
                return;
            }

            if (skinColor == 13 && !player.getMemberRights().isDiamondOrGreater(player)) {
                player.message("You need to be a Diamond member to use this skin!");
                return;
            }

            if (skinColor == 14 && !player.getMemberRights().isDragonstoneOrGreater(player)) {
                player.message("You need to be a Dragonstone member to use this skin!");
                return;
            }

            if (skinColor == 15 && !player.getMemberRights().isOnyxOrGreater(player)) {
                player.message("You need to be a Onyx member to use this skin!");
                return;
            }

            if (skinColor == 16 && !player.getMemberRights().isZenyteOrGreater(player)) {
                player.message("You need to be a Zentye member to use this skin!");
                return;
            }

            player.looks().female(gender);
            player.looks().looks(new int[] {head, jaw, torso, arms, hands, legs, feet});
            player.looks().colors(new int[] {hairColor, torsoColor, legsColor, feetColor, skinColor});
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            player.stopActions(true);
            player.getInterfaceManager().close();
        } catch(Exception e) {
            logger.catching(e);
        }
    }

}
