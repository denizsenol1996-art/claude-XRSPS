package com.twisted.game.content.packet_actions.interactions.objects;

import com.twisted.GameServer;
import com.twisted.game.content.skill.impl.smithing.Bar;
import com.twisted.game.content.skill.impl.smithing.EquipmentMaking;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.content.tradingpost.TradingPost;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.ironman.GroupIronman;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteractionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.twisted.util.ObjectIdentifiers.OPEN_CHEST_3194;
import static org.apache.logging.log4j.util.Unbox.box;

public class ObjectActions {

    private static final Logger logger = LogManager.getLogger(ObjectActions.class);

    /**
     * This method determines all the clicking actions for an npc.
     *
     * @param player      The player
     * @param object      The object
     * @param clickAction The menu action
     */
    public static void handleAction(Player player, GameObject object, int clickAction) {
        //Get object definition
        if (object.definition() == null) {
            logger.error("ObjectDefinition for object {} is null for player " + player.toString() + ".", box(object.getId()));
            return;
        }

        final String name = object.definition().name;

        final boolean bank = object.getId() == OPEN_CHEST_3194 || name.equalsIgnoreCase("Bank booth") || name.equalsIgnoreCase("Bank chest") || name.equalsIgnoreCase("Grand Exchange booth");
        if (clickAction == 1) {
            if (name.equalsIgnoreCase("anvil")) {
                if (object.tile().equals(2794, 2793)) {
                    player.smartPathTo(object.tile());
                    player.waitUntil(1, () -> !player.getMovementQueue().isMoving(), () -> EquipmentMaking.openInterface(player));
                }
                else if (object.tile().equals(3343, 9652)) {
                    player.smartPathTo(object.tile());
                    player.waitUntil(1, () -> !player.getMovementQueue().isMoving(), () -> EquipmentMaking.openInterface(player));
                }
                else
                    EquipmentMaking.openInterface(player);
                return;
            }

            if (player.getController() != null) {
                if (player.getController().handleObjectClick(player, object, 1)) {
                    return;
                }
            }

            if (object.getId() == 10556) {
                Tile tile = GameServer.properties().defaultTile;
                Teleports.basicTeleport(player, tile);
                return;
            }

            if (object.getId() == 26738) {
                if (player.getParticipatingTournament() != null) {
                    TournamentManager.leaveTourny(player, false, true);
                    return;
                }
            }

            GroupIronman group = GroupIronman.getGroup(player.getUID());
            if (bank) {
                if (group == null) {
                    player.getBank().open();
                } else {
                    group.getGroupBank().open(player, group);
                }
                return;
            }

            if (name.equalsIgnoreCase("furnace")) {
                for (Bar bar : Bar.values()) {
                    player.getPacketSender().sendInterfaceModel(bar.getFrame(),150, bar.getBar());
                }
                player.getPacketSender().sendChatboxInterface(2400);
                return;
            }

            if (name.equalsIgnoreCase("anvil")) {
                EquipmentMaking.openInterface(player);
                return;
            }

            if (PacketInteractionManager.checkObjectInteraction(player, object, 1)) {
                return;
            }

            if (player.farming().handleObjectInteraction(object.getId(), object.getX(), object.getY(),1)) {
                return;
            }
        }

        if (clickAction == 2) {

            // Areas
            if (player.getController() != null) {
                if (player.getController().handleObjectClick(player, object, 2)) {
                    //System.out.println("Area click object option 2.");
                    return;
                }
            }

            //System.out.println(name);
            if (bank) {
                GroupIronman group = GroupIronman.getGroup(player.getUID());
                //System.out.println("object option 2, opening bank.");
                if (group == null) {
                    player.getBank().open();
                } else {
                    group.getGroupBank().open(player, group);
                }
                return;
            }

            if (name.equalsIgnoreCase("furnace")) {
                //System.out.println("object option 2, furnace action.");
                for (Bar bar : Bar.values()) {
                    player.getPacketSender().sendInterfaceModel(bar.getFrame(), 150, bar.getBar());
                }
                player.getPacketSender().sendChatboxInterface(2400);
                return;
            }

            if (player.farming().handleObjectInteraction(object.getId(), object.getX(), object.getY(),2)) {
                //System.out.println("object option 2, farming old system.");
                return;
            }

            if (PacketInteractionManager.checkObjectInteraction(player, object, 2)) {
                //System.out.println("object option 2, checkObjectInteraction.");
                return;
            }
            return;
        }

        if (clickAction == 3) {

            if (name.equalsIgnoreCase("Grand Exchange booth")) {
                TradingPost.open(player);
                return;
            }

            if (player.getController() != null) {
                if (player.getController().handleObjectClick(player, object, 3)) {
                    return;
                }
            }

            if (PacketInteractionManager.checkObjectInteraction(player, object, 3)) {
                return;
            }
            return;
        }

        if (clickAction == 4) {
            if (player.getController() != null) {
                if (player.getController().handleObjectClick(player, object, 4)) {
                    return;
                }
            }
            if (PacketInteractionManager.checkObjectInteraction(player, object, 4)) {
                return;
            }

            return;
        }
    }
}
