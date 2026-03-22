package com.twisted.game.content.areas.falador;

import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.dialogue.Expression;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.NpcIdentifiers;
import com.twisted.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * mei 06, 2020
 */
public class MiningGuild extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if (obj.getId() == 30364) {
                door(player, obj);
                return true;
            }
        }
        return false;
    }

    private void door(Player player, GameObject door) {
        if (player.skills().level(Skills.MINING) < 60) {
            DialogueManager.npcChat(player, Expression.HAPPY, NpcIdentifiers.GUARD_6561, "Sorry, but you need level 60 Mining to get in there.");
        } else {
            player.lock();

            if (player.tile().y >= 9757) {
                if (!player.tile().equals(door.tile().transform(0, 1, 0))) {
                    player.getMovementQueue().walkTo(door.tile().transform(0, 1, 0));
                }

                GameObject old = new GameObject(door.getId(), door.tile(), door.getType(), door.getRotation());
                GameObject spawned = new GameObject(door.getId(), new Tile(3046, 9757), door.getType(), 2);
                ObjectManager.removeObj(old);
                ObjectManager.addObj(spawned);
                Chain.bound(player).name("MiningGuildDoor1Task").runFn(2, () -> {
                    ObjectManager.removeObj(spawned);
                    ObjectManager.addObj(old);
                });

                player.getMovementQueue().interpolate(3046, 9756, MovementQueue.StepType.FORCED_WALK);
                Chain.bound(player).name("MiningGuildDoor2Task").runFn(1, player::unlock);
            } else {
                if (!player.tile().equals(door.tile().transform(0, 0, 0))) {
                    player.getMovementQueue().walkTo(door.tile().transform(0, 0, 0));
                }
                GameObject old = new GameObject(door.getId(), door.tile(), door.getType(), door.getRotation());
                GameObject spawned = new GameObject(door.getId(), new Tile(3046, 9757), door.getType(), 2);
                ObjectManager.removeObj(old);
                ObjectManager.addObj(spawned);
                Chain.bound(player).name("MiningGuildDoor3Task").runFn(2, () -> {
                    ObjectManager.removeObj(spawned);
                    ObjectManager.addObj(old);
                });
                player.getMovementQueue().interpolate(3046, 9757, MovementQueue.StepType.FORCED_WALK);
                Chain.bound(player).name("MiningGuildDoor4Task").runFn(1, player::unlock);
            }
        }
    }

}
