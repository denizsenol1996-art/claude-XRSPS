package com.twisted.game.content.newplateau;

import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.ForceMovementTask;
import com.twisted.game.world.entity.mob.player.ForceMovement;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.route.Direction;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.ObjectIdentifiers;
import com.twisted.util.chainedwork.Chain;

public class RootJump extends PacketInteraction {


    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if(object.getId() == ObjectIdentifiers.ROOT_26720 || object.getId() ==  ObjectIdentifiers.ROOT_26721){
            Direction dir = player.getAbsY() > object.tile().y ? Direction.EAST : Direction.WEST;
            Direction dir2 = player.getAbsY() > object.tile().y ? Direction.WEST : Direction.EAST;
            Chain.bound(null).runFn(1, () -> {
                player.animate(3067);
                if(player.getX() == 1590 && player.getY() == 9900 || player.getX() == 1590 && player.getY() == 9899){
                    TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(dir.deltaX * 2, dir.deltaY * 2), 25, 65, dir.faceValue)));
                } else {
                    TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(dir2.deltaX * 2, dir2.deltaY * 2), 25, 65, dir2.faceValue)));

                }
            }).then(3, player::unlock);

            return true;

        }

        return false;
    }
}
