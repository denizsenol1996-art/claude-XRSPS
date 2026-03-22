package com.twisted.game.content.areas;

import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.ObjectIdentifiers;

/**
 * Created by OUREVILSINS (SINS/Mike tython) on 2/2/2024.
 */

public class Dzone4Barrier extends PacketInteraction {
    private void walkX(Player player, boolean xUp) {
        Tile targTile = player.tile().transform(xUp ? +1 : -1, 0, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }
    private void walkX2(Player player, boolean xUp) {
        Tile targTile = player.tile().transform(xUp ? +2 : -1, 0, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }
    private void walkYD(Player player, boolean xDown) {
        Tile targTile = player.tile().transform(xDown ? +1 : -1, 0, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }
    private void walkDown(Player player, boolean xDown) {
        Tile targTile = player.tile().transform(xDown ? -1 : +1, 0, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }
    private void walkDown2(Player player, boolean xDown) {
        Tile targTile = player.tile().transform(xDown ? -1 : +2, 0, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }
    private void walkY(Player player, boolean yUp) {
        Tile targTile = player.tile().transform(0, yUp ? +1 : -1, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }
    private void walkYx(Player player, boolean yUp) {
        Tile targTile = player.tile().transform(0, yUp ? -1 : -1, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }
    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        //Kerberos barrier
        if (obj.getId() == ObjectIdentifiers.ENERGY_BARRIER) {
            if (player.tile().x == 2455 && (player.tile().y == 2720) ||
                player.tile().x == 2455 && (player.tile().y == 2721)) {
                walkX(player, true);
            } else if (player.tile().x == 2456 && (player.tile().y == 2720) ||
                player.tile().x == 2456 && (player.tile().y == 2721)) {
                walkDown(player, true);
            }
            //Cerberus Barrier
            if (player.tile().x == 2455 && (player.tile().y == 2695) ||
                player.tile().x == 2455 && (player.tile().y == 2696)) {
                walkX(player, true);
            } else if (player.tile().x == 2456 && (player.tile().y == 2695) ||
                player.tile().x == 2456 && (player.tile().y == 2696)) {
                walkDown(player, true);
            }
            //Elvarg Barrier
            if (player.tile().x == 2455 && (player.tile().y == 2708)) {
                walkDown(player, true);
            } else if (player.tile().x == 2454 && (player.tile().y == 2708)) {
                walkX(player, true);
            }
            //Nex Barrier
            if (player.tile().x == 2465 && (player.tile().y == 2695) ||
                player.tile().x == 2465 && (player.tile().y == 2696)) {
                walkX(player, true);
            } else if (player.tile().x == 2466 && (player.tile().y == 2695) ||
                player.tile().x == 2466 && (player.tile().y == 2696)) {
                walkDown(player, true);
            }
            //Seren Barrier
            if (player.tile().x == 2466 && (player.tile().y == 2708)) {
                walkX(player, true);
            } else if (player.tile().x == 2467 && (player.tile().y == 2708)) {
                walkDown(player, true);
            }
            //Nightmare Barrier
            if (player.tile().x == 2465 && (player.tile().y == 2720) ||
                player.tile().x == 2465 && (player.tile().y == 2721)) {
                walkX(player, true);
            } else if (player.tile().x == 2466 && (player.tile().y == 2720) ||
                player.tile().x == 2466 && (player.tile().y == 2721)) {
                walkDown(player, true);
            }
            //Mending Revs Barrier
            if (player.tile().x == 2460 && (player.tile().y == 2725) ||
                player.tile().x == 2461 && (player.tile().y == 2725)) {
                walkY(player, true);
            } else if (player.tile().x == 2460 && (player.tile().y == 2726) ||
                player.tile().x == 2461 && (player.tile().y == 2726)) {
                walkYx(player, true);
            }
            return true;
        }
        return false;
    }
}
