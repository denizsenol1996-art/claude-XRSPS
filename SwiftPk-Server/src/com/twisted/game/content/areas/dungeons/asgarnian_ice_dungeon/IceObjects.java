package com.twisted.game.content.areas.dungeons.asgarnian_ice_dungeon;

import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.TickAndStop;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;

import static com.twisted.util.ObjectIdentifiers.*;

public class IceObjects extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == TRAPDOOR_1738) {
                // Trapdoor down
                if (obj.tile().equals(3008, 3150)) {
                    TaskManager.submit(new TickAndStop(1) {
                        @Override
                        public void executeAndStop() {
                            player.teleport(new Tile(3007, 9550));
                        }
                    });
                }
                return true;
            }
            if(obj.getId() == ICY_CAVERN) {
                TaskManager.submit(new TickAndStop(1) {
                    @Override
                    public void executeAndStop() {
                        player.teleport(new Tile(player.tile().x, 9562));
                    }
                });
                return true;
            }
            if(obj.getId() == ICY_CAVERN_10596) {
                TaskManager.submit(new TickAndStop(1) {
                    @Override
                    public void executeAndStop() {
                        player.teleport(new Tile(player.tile().x, 9555));
                    }
                });
                return true;
            }
        }
        return false;
    }
}
