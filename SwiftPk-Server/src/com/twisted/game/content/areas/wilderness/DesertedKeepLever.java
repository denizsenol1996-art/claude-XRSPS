package com.twisted.game.content.areas.wilderness;

import com.twisted.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.TickAndStop;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.timers.TimerKey;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 12, 2020
 */
public class DesertedKeepLever extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            if (obj.getId() == 1815) {
                player.faceObj(obj);

                //Check to see if the player is teleblocked
                if (player.getTimerRepository().has(TimerKey.TELEBLOCK) || player.getTimerRepository().has(TimerKey.SPECIAL_TELEBLOCK)) {
                    player.teleblockMessage();
                    return true;
                }

                if (WildernessKeyPlugin.hasKey(player) && WildernessArea.inWilderness(player.tile())) {
                    player.message("You cannot teleport outside the Wilderness with the Wilderness key.");
                    return true;
                }

                TaskManager.submit(new TickAndStop(1) {
                    @Override
                    public void executeAndStop() {
                        player.animate(2140);
                        player.message("You pull the lever...");
                    }
                });

                GameObject spawned = new GameObject(88, obj.tile(), obj.getType(), obj.getRotation());
                TaskManager.submit(new TickAndStop(1) {
                    @Override
                    public void executeAndStop() {
                        ObjectManager.addObj(spawned);
                    }
                });

                TaskManager.submit(new TickAndStop(5) {
                    @Override
                    public void executeAndStop() {
                        ObjectManager.removeObj(spawned);
                        ObjectManager.addObj(obj);
                    }
                });

                TaskManager.submit(new TickAndStop(2) {
                    @Override
                    public void executeAndStop() {
                        player.animate(714);
                        player.graphic(111);
                    }
                });

                TaskManager.submit(new TickAndStop(4) {
                    @Override
                    public void executeAndStop() {
                        Tile targetTile = new Tile(3092,3488);
                        player.teleport(targetTile);
                        player.animate(-1);
                        player.message("...And teleport out of the wilderness.");
                    }
                });
                return true;
            }
        }
        return false;
    }
}
