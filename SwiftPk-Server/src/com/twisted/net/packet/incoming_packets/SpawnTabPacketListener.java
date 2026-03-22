package com.twisted.net.packet.incoming_packets;

import com.twisted.fs.ItemDefinition;
import com.twisted.game.content.spawn_tab.SpawnTab;
import com.twisted.game.content.syntax.impl.SpawnX;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;

/**
 * This packet listener reads a item spawn request
 * from the spawn tab.
 * @author Professor Oak
 */
public class SpawnTabPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int item = packet.readInt();
        final boolean spawnX = packet.readByte() == 1;
        final boolean toBank = packet.readByte() == 1;

        if (player == null || player.dead() || player.locked()) {
            return;
        }

        if (player.<Boolean>getAttribOr(AttributeKey.NEW_ACCOUNT,false)) {
            player.message("You have to select your game mode before you can continue.");
            return;
        }

        player.afkTimer.reset();

        ItemDefinition def = World.getWorld().definitions().get(ItemDefinition.class, item);

        if (def == null) {
            player.message("This item is currently unavailable.");
            return;
        }
// i think we can just delete all in gameconstants pretty well
        //Check if player busy..
        if (player.busy()) {
            player.message("You cannot do that right now.");
            return;
        }

        if (!player.tile().homeRegion() && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("Spawning items is only allowed at home.");
            return;
        }

        boolean spawnable = def.pvpAllowed;

        if (!spawnable) {
            player.message("Feature disable.");
            return;
        }

        //Spawn item
        if (!spawnX) {
            SpawnTab.spawn(player, item, 1, toBank);
        } else {
            player.setEnterSyntax(new SpawnX(item, toBank));
            player.getPacketSender().sendEnterAmountPrompt("How many "+def.name+" would you like to spawn?");
        }
    }
}
