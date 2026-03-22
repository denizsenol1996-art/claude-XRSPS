package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.util.ItemIdentifiers;

import java.lang.ref.WeakReference;

import static com.twisted.util.CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR;
import static com.twisted.util.CustomItemIdentifiers.SANGUINE_TWISTED_BOW;
import static com.twisted.util.ItemIdentifiers.TRIDENT_OF_THE_SEAS;
import static com.twisted.util.ItemIdentifiers.TRIDENT_OF_THE_SWAMP;

/**
 * @author PVE
 * @Since augustus 26, 2020
 */
public class AttackPlayerPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {


        player.afkTimer.reset();

        if (player.busy()) {
            return;
        }

        if (player.getUsername().equalsIgnoreCase("Box Test")) {
            player.message("You cannot attack this player.");
            return;
        }

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        //cmmented for the fix

        if (player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        player.stopActions(false);
        int index = packet.readLEShort();
        if (index > World.getWorld().getPlayers().capacity() || index < 0)
            return;

        if (player.locked() || player.dead()) {
            return;
        }

        final Player attacked = World.getWorld().getPlayers().get(index);
        if (player.getHostAddress().equalsIgnoreCase(attacked.getHostAddress()) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can't pk yourself on the same IP.");
            return;
        }
        if (player.tile().inArea(Tile.MB) && player.getCombat().getCombatType() != CombatType.MAGIC) {
            player.message("You can only use magic inside the arena!");
            player.getCombat().reset();
            return;
        }
        //XO1
        if (player.getEquipment().contains(SANGUINE_TWISTED_BOW)) {
            player.message("Sanguine Twisted Bow are not able to be use in pvp.");
            player.getCombat().reset();
            return;
        }
        if (player.getEquipment().contains(SANGUINE_SCYTHE_OF_VITUR)) {
            player.message("Sanguine Scythe of vitur are not able to be use in pvp.");
            player.getCombat().reset();
            return;
        }
        if (player.getEquipment().contains(TRIDENT_OF_THE_SEAS) || player.getEquipment().contains(TRIDENT_OF_THE_SWAMP)) {
            player.message("Cant be used in pvp.");
            player.getCombat().reset();
            return;
        }
        if (player.getEquipment().contains(ItemIdentifiers.TUMEKENS_SHADOW)) {
            player.message("You cannot use the Tumekens Shadow against players.");
            player.getCombat().reset();
            return;
        }

        if (attacked == null || attacked.dead() || attacked.equals(player)) {
            player.getMovementQueue().clear();
            return;
        }
        player.putAttrib(AttributeKey.INTERACTION_OPTION, 2);
        player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(attacked));
        player.getCombat().setCastSpell(null);
        player.getCombat().attack(attacked);
    }
}
