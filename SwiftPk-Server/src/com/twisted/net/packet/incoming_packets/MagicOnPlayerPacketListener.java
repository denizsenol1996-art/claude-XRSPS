package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.magic.CombatSpell;
import com.twisted.game.world.entity.combat.magic.CombatSpells;
import com.twisted.game.world.entity.combat.magic.MagicClickSpells;
import com.twisted.game.world.entity.combat.magic.Spell;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

import java.lang.ref.WeakReference;

public class MagicOnPlayerPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int targetIndex = packet.readShortA();
        int spellId = packet.readLEShort();

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        if (player.locked()) {
            return;
        }

        Player other = World.getWorld().getPlayers().get(targetIndex);
        if (other == null) {
            player.message("Unable to find player.");
        } else {
            player.stopActions(false);

            if (!player.locked() && !player.dead()) {
                player.face(other.tile());
                if (!other.dead()) {
                    player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(other));
                    player.putAttrib(AttributeKey.INTERACTION_OPTION, 1);

                    //Do actions...
                    boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);
                    if (newAccount) {
                        player.message("You have to select your game mode before you can continue.");
                        return;
                    }

                    CombatSpell spell = CombatSpells.getCombatSpell(spellId);

                    if (spell == null) {
                        player.getMovementQueue().reset();
                        Spell clickSpell = MagicClickSpells.getMagicSpell(spellId);

                        if (clickSpell != null) {
                            MagicClickSpells.handleSpellOnPlayer(player, other, clickSpell);
                        }
                        return;
                    }

                    player.getCombat().setCastSpell(spell);
                    player.getCombat().attack(other);
                }
            }
        }
    }

}
