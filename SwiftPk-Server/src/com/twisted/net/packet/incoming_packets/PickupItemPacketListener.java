package com.twisted.net.packet.incoming_packets;

import com.twisted.GameServer;
import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.DistancedActionTask;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.Packet;
import com.twisted.net.packet.PacketListener;
import com.twisted.util.Color;

import java.util.Optional;

import static com.twisted.util.CustomItemIdentifiers.ESCAPE_KEY;
import static com.twisted.util.CustomItemIdentifiers.WILDERNESS_KEY;

/**
 * This packet listener is used to pick up ground items
 * that exist in the world.
 *
 * @author Patrick van Elderen | 13 feb. 2019 : 12:57:23
 * @see <a href="https://www.rune-server.ee/members/_Patrick_/">Rune-Server profile</a>
 */
public class PickupItemPacketListener implements PacketListener {

    /**
     * Handles picking up the item
     *
     * @param player   The {@link Player} picking up the item
     * @param item       The item
     * @param tile The coordinates of the item
     */
    private void pickup(Player player, Item item, Tile tile) {
        Optional<GroundItem> groundItem = GroundItemHandler.getGroundItem(item.getId(), tile, player);
        if (groundItem.isPresent()) {
            GroundItemHandler.pickup(player, item.getId(), tile, false);
        }
    }

    /**
     * @param player   The {@link Player}
     * @param tile The coordinates of the spot
     * @return If the player is standing on this spot
     */
    private boolean onSpot(Player player, Tile tile) {
        return player.tile().isWithinDistance(tile,1);
    }

    public static void respawn(Item item, Tile position, int ticks) {
        TaskManager.submit(new Task("PickupItemPacketListener_respawn_task", ticks) {
            @Override
            protected void execute() {
                GroundItem next = new GroundItem(item, position.copy(), null);
                GroundItemHandler.createGroundItem(next);
                stop();
            }
        });
    }

    @Override
    public void handleMessage(final Player player, Packet packet) {
        final int y = packet.readLEShort();
        final int itemId = packet.readShort();
        final int x = packet.readLEShort();
        final Tile tile = new Tile(x, y, player.tile().getLevel());

        if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
            player.getBankPin().openIfNot();
            return;
        }

        if(player.askForAccountPin()) {
            player.sendAccountPinMessage();
            return;
        }

        if (player.busy()) {
            return;
        }

        // Make sure distance isn't way off..
        if (Math.abs(player.tile().getX() - x) > 25 || Math.abs(player.tile().getY() - y) > 25) {
            player.getMovementQueue().clear();
            return;
        }

        if (!player.locked() && !player.dead()) {
            boolean newAccount = player.getAttribOr(AttributeKey.NEW_ACCOUNT, false);
            if (newAccount) {
                player.message("You have to select your game mode before you can continue.");
                return;
            }

            if (GameServer.properties().debugMode && player.getPlayerRights().isDeveloperOrGreater(player)) {
                player.debugMessage(String.format("pickup item - Item: %d Location: %s", itemId, tile));
            }

            player.stopActions(false);
            player.skills().stopSkillable();
            player.getCombat().reset();

            player.putAttrib(AttributeKey.INTERACTED_GROUNDITEM, itemId);
            player.putAttrib(AttributeKey.INTERACTION_OPTION, 3);

            //Do actions...

            // If whoever froze you is out of viewport, you get unfrozen when interacting with items.
            CombatFactory.unfreezeWhenOutOfRange(player);

            if (onSpot(player, tile)) {
                if(itemId == WILDERNESS_KEY) {
                    if(player.skills().combatLevel() < 120) {
                        player.message(Color.RED.wrap("You need to be at least level 120 to pickup this key."));
                        return;
                    }
                }
                pickup(player, new Item(itemId), tile);
            } else {
                player.setDistancedTask(new DistancedActionTask() {

                    @Override
                    public void onReach() {
                        if(itemId == WILDERNESS_KEY) {
                            if(player.skills().combatLevel() < 120) {
                                player.message(Color.RED.wrap("You need to be at least level 120 to pickup this key."));
                                stop();
                                return;
                            }
                        }
                        pickup(player, new Item(itemId), tile);
                        stop();
                    }

                    @Override
                    public boolean reached() {
                        return onSpot(player, tile);
                    }
                });
            }

            if (onSpot(player, tile)) {
                if(itemId == ESCAPE_KEY) {
                    if(player.skills().combatLevel() < 120) {
                        player.message(Color.RED.wrap("You need to be at least level 120 to pickup this key."));
                        return;
                    }
                }
                pickup(player, new Item(itemId), tile);
            } else {
                player.setDistancedTask(new DistancedActionTask() {

                    @Override
                    public void onReach() {
                        if(itemId == ESCAPE_KEY) {
                            if(player.skills().combatLevel() < 120) {
                                player.message(Color.RED.wrap("You need to be at least level 120 to pickup this key."));
                                stop();
                                return;
                            }
                        }
                        pickup(player, new Item(itemId), tile);
                        stop();
                    }

                    @Override
                    public boolean reached() {
                        return onSpot(player, tile);
                    }
                });
            }

           /* GroundItem groundItem = new GroundItem(new Item(itemId), tile, player);
            player.getRouteFinder().routeGroundItem(groundItem, distance -> pickup(player, new Item(itemId), tile, distance));*/
        }
    }
}
