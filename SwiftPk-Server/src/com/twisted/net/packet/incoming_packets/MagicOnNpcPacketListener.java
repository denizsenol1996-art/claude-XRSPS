package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.magic.CombatSpell;
import com.twisted.game.world.entity.combat.magic.CombatSpells;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.util.Color;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.CustomNpcIdentifiers;
import com.twisted.util.Tuple;

import java.lang.ref.WeakReference;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class MagicOnNpcPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int targetIndex = packet.readLEShortA();
        int spellId = packet.readShortA();

        if (targetIndex < 0 || spellId < 0 || targetIndex > World.getWorld().getNpcs().capacity()) {
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

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        Npc other = World.getWorld().getNpcs().get(targetIndex);
        if (other == null) {
            player.message("Unable to find npc.");
        } else {
            if (!player.locked() && !player.dead()) {
                player.stopActions(false);
                player.face(other.tile());
                if (!other.dead()) {
                    player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(other));
                    player.putAttrib(AttributeKey.INTERACTION_OPTION, 2);

                    if (other.combatInfo() == null) {
                        player.message("Without combat attributes this monster is unattackable.");
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

                    CombatSpell spellSelected = CombatSpells.getCombatSpell(spellId);

                    if (spellSelected == null) {
                        player.getMovementQueue().clear();
                        return;
                    }
                    other.getMovementQueue().setBlockMovement(true);
                    player.face(other.tile());

                    //These always overwrite it I tried so many things
                    player.getCombat().setCastSpell(spellSelected);
                    player.getCombat().attack(other);
                    other.getMovementQueue().setBlockMovement(false);
                }
            }
        }
    }
}
