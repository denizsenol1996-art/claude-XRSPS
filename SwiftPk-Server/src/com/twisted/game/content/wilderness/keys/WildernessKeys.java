package com.twisted.game.content.wilderness.keys;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.Utils;

import java.util.Objects;

/**
 * @Author: Origin
 * @Date: 2/22/24
 */
public class WildernessKeys extends PacketInteraction {
    public static void rollWildernessKey(Player player, Npc npc) {
        if (WildernessArea.inWilderness(npc.tile())) {
            if (Utils.rollDie(85, 1)) {
                Item item = new Item(298, 1);
                GroundItem groundItem = new GroundItem(item, npc.tile(), player);
                GroundItemHandler.createGroundItem(groundItem);
                player.message(Color.PURPLE.wrap("<img=2010>You've received a Wilderness Key drop!"));
            }
        }
    }

    public static final int[] keyNpcs = new int[]{16012, 16013, 16014, 16015, 16016};
    public int getRandomNpc() {
        int randomIndex = World.getWorld().random().nextInt(keyNpcs.length);
        return keyNpcs[randomIndex];
    }

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (item.getId() == 298) {
            if (option == 1) {
                Tile digTile = new Tile(3028, 3915, 0);
                KeyNpc npc = new KeyNpc(getRandomNpc(), new Tile(player.tile().getX(), player.tile().getY(), player.tile().getZ()), player);
                boolean playerOnDigTile = Objects.equals(player.tile(), digTile);
                boolean playerInRange = player.tile().inSqRadius(digTile, 8);
                boolean clickDelay = player.getClickDelay().elapsed(500);
                if (!playerOnDigTile && playerInRange && clickDelay) {
                    player.getClickDelay().reset();
                    player.getPacketSender().sendPositionalHint(digTile, 2);
                    player.message(Color.BLUE.wrap("Stand on the marked tile, and activate your key."));
                } else player.message("Please wait before doing this again.");
                if (clickDelay) {
                    player.waitForTile(digTile, () -> {
                        if (player.tile().equals(3028, 3915, 0) && player.getInventory().contains(ItemIdentifiers.KEY_298)) {
                            player.getClickDelay().reset();
                            player.getInventory().remove(ItemIdentifiers.KEY_298);
                            player.getPacketSender().sendEntityHintRemoval(true);
                            player.getCombat().setTarget(npc);
                            player.getPacketSender().sendEntityHint(npc);
                            World.getWorld().registerNpc(npc);
                        }
                    });
                }
                return true;
            }
        }
        return false;
    }
}
