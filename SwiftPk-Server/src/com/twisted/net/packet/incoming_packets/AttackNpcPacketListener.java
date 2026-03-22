package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.util.*;

import java.lang.ref.WeakReference;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class AttackNpcPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        double totalAmountPaid = player.getAttribOr(AttributeKey.TOTAL_PAYMENT_AMOUNT, 0D);
        int index = packet.readShortA();
        if (index < 0 || index > World.getWorld().getNpcs().capacity())
            return;
        final Npc other = World.getWorld().getNpcs().get(index);

        if (other == null) {
            return;
        }

        if (player == null || player.dead()) {
            return;
        }

        player.afkTimer.reset();

        if (player.busy()) {
            return;
        }

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if (player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }
        //update4.27
//donet
        if (other.id() == 13391) {
            if (totalAmountPaid < 50) {
                player.getCombat().reset();
                player.message("You need total $50 donator to attack this npc!");
                return;
            }
        }
        if (other.id() == 9430 && player.getY() < 3981) {
            player.getPacketSender().sendMessage("Safe spot is not allowed here stay on way of nightmare");
            player.getCombat().reset();
            return;
        }

        if (!player.locked() && !player.dead()) {
            player.stopActions(false);

            if (!other.dead()) {
                if (other.combatInfo() == null) {
                    player.message("Without combat attributes this monster is unattackable. [Npc Id: " + other.getId() + "] ");
                    return;
                }

                if (!player.getEquipment().contains(CustomItemIdentifiers.SUMMER_SOAKER) && other.id() == CustomNpcIdentifiers.SUMMER_IMP) {
                    player.message(Color.RED.wrap("You can only damage a Summer Imp while using a Summer Soaker."));
                    player.getCombat().reset();
                    return;
                }

                // See if it's exclusively owned
                Tuple<Integer, Player> ownerLink = other.getAttribOr(AttributeKey.OWNING_PLAYER, new Tuple<>(-1, null));
                if (ownerLink.first() != null && ownerLink.first() >= 0 && ownerLink.first() != player.getIndex()) {
                    player.message("They don't seem interested in fighting you.");
                    player.getCombat().reset();
                    return;
                }

                other.getMovementQueue().setBlockMovement(true);
                player.putAttrib(AttributeKey.INTERACTION_OPTION, 2);
                player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(other));


                /*if (player.pet() != null && player.hasPetOut(12302)) {
                    player.pet().getCombat().attack(other);
                }*/
                player.getCombat().attack(other);
                //CombatFactory.debug(player, "Executed attack in AttackNPC packet", other, true);
                other.getMovementQueue().setBlockMovement(false);
            }
        }
    }

}
