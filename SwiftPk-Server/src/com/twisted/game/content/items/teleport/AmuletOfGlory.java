package com.twisted.game.content.items.teleport;

import com.twisted.GameServer;
import com.twisted.game.content.teleport.TeleportType;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.CustomItemIdentifiers;

import java.util.stream.IntStream;

import static com.twisted.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen | December, 28, 2020, 13:48
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class AmuletOfGlory extends PacketInteraction {

    public static final int[] GLORY = new int[] {AMULET_OF_GLORY, AMULET_OF_GLORY1, AMULET_OF_GLORY2, AMULET_OF_GLORY3, AMULET_OF_GLORY4, AMULET_OF_GLORY5, AMULET_OF_GLORY6, AMULET_OF_ETERNAL_GLORY, CustomItemIdentifiers.DERANGED_MANIFESTO};

    private void teleport(Player player) {
        Tile tile = GameServer.properties().defaultTile;

        if (Teleports.canTeleport(player,true, TeleportType.ABOVE_20_WILD)) {
            Teleports.basicTeleport(player, tile);
            player.message("You have been teleported to home.");
        }
    }

    @Override
    public boolean handleEquipmentAction(Player player, Item item, int slot) {
        if(IntStream.of(GLORY).anyMatch(glory -> item.getId() == glory) && slot == EquipSlot.AMULET) {
            teleport(player);
            return true;
        }
        else if (IntStream.of(GLORY).anyMatch(glory -> item.getId() == glory) && slot == EquipSlot.SHIELD) {
            teleport(player);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 2) {
            if(IntStream.of(GLORY).anyMatch(glory -> item.getId() == glory)) {
                teleport(player);
                return true;
            }
        }
        return false;
    }
}
