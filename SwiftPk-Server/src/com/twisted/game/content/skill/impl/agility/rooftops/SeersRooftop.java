package com.twisted.game.content.skill.impl.agility.rooftops;

import com.twisted.game.content.skill.impl.agility.MarksOfGrace;
import com.twisted.game.content.skill.impl.agility.UnlockAgilityPet;
import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.ForceMovementTask;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.player.ForceMovement;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.chainedwork.Chain;

import java.util.Arrays;
import java.util.List;

import static com.twisted.util.ObjectIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * juni 14, 2020
 */
public class SeersRooftop extends PacketInteraction {

    private static final List<Tile> MARK_SPOTS = Arrays.asList(new Tile(2726, 3492, 3), new Tile(2728, 3495, 3), new Tile(2707, 3493, 2), new Tile(2708, 3489, 2), new Tile(2712, 3481, 2), new Tile(2710, 3478, 2), new Tile(2710, 3472, 3), new Tile(2702, 3474, 3), new Tile(2698, 3462, 2));

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        // Wall climb
        if(obj.getId() == WALL_14927) {
            if (player.skills().xpLevel(Skills.AGILITY) < 60) {
                player.message("You need an Agility level of 60 to attempt this.");
            } else {
                player.lock();
                player.face(player.tile().transform(0, +1));
                Chain.bound(player).name("SeersRooftopWallClimbTask").runFn(1, () -> player.animate(737, 15)).then(2, () -> {
                    player.teleport(2729, 3488, 1);
                    player.animate(1118);
                }).then(2, () -> {
                    player.teleport(2729, 3491, 3);
                    player.animate(-1);
                    player.skills().addXp(Skills.AGILITY, 45.0);
                    player.unlock();
                    player.putAttrib(AttributeKey.SEERS_ROOFTOP_COURSE_STATE,1);
                    MarksOfGrace.trySpawn(player, MARK_SPOTS, 60, 20);
                });
            }
            return true;
        }

        // Rooftop jumping
        if(obj.getId() == GAP_14928) {
            player.lock();
            Chain.bound(player).name("SeersRooftopJumpTask").runFn(1, () -> {
                player.sound(2462, 15);
                player.animate(2586, 15);
            }).then(1, () -> {
                player.teleport(2719, 3495, 2);
                player.animate(2588);
            }).then(1, () -> {
                player.sound(2462, 15);
                player.animate(2586, 15);
            }).then(1, () -> {
                player.teleport(2713, 3494, 2);
                player.animate(2588);
            }).then(1, () -> {
                player.skills().addXp(Skills.AGILITY, 20.0);
                player.unlock();
                player.putAttrib(AttributeKey.SEERS_ROOFTOP_COURSE_STATE,2);
                MarksOfGrace.trySpawn(player, MARK_SPOTS, 60, 20);
            });
            return true;
        }

        // Tightrope
        if(obj.getId() == TIGHTROPE_14932) {
            Chain.bound(player).name("SeersRooftopTightrope").runFn(1, () -> {
                player.lock();
                player.getMovementQueue().clear();
                //player.sound(2495, 0, 6);
                player.getMovementQueue().interpolate(2710, 3481, MovementQueue.StepType.FORCED_WALK);
            }).then(1, () -> {
                player.agilityWalk(false);
                player.looks().render(763, 762, 762, 762, 762, 762, -1);
            }).waitForTile(new Tile(2710, 3481), () -> {
                player.agilityWalk(true);
                player.looks().resetRender();
                player.skills().addXp(Skills.AGILITY, 20.0);
                player.unlock();
                player.putAttrib(AttributeKey.SEERS_ROOFTOP_COURSE_STATE,3);
                MarksOfGrace.trySpawn(player, MARK_SPOTS, 60, 20);
            });
            return true;
        }

        // Gap jump
        if(obj.getId() == GAP_14929) {
            player.lock();
            Chain.bound(player).name("SeersRooftopGapJump1Task").runFn(1, () -> player.face(player.tile().transform(0 , -1))).then(1, () -> TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, -3), 25, 30, Direction.SOUTH.toInteger())))).then(1, () -> player.animate(2585)).then(1, () -> {
                player.teleport(2710, 3474, 3);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, -2), 17, 26, Direction.SOUTH.toInteger())));
            }).then(1, () -> {
                player.teleport(2710, 3472, 3);
                player.skills().addXp(Skills.AGILITY, 35.0);
                player.unlock();
                MarksOfGrace.trySpawn(player, MARK_SPOTS, 60, 20);
                player.putAttrib(AttributeKey.SEERS_ROOFTOP_COURSE_STATE,4);
            });
            return true;
        }

        // Small gap jump
        if(obj.getId() == GAP_14930) {
            player.waitUntil(1, () -> !player.getMovementQueue().isMoving(), () -> {
                player.lock();
                Chain.bound(player).name("SeersRooftopGapJump2Task").runFn(1, () -> {
                    player.face(player.tile().transform(+1, 0));
                    player.animate(2586, 15);
                    player.sound(2462, 15);
                }).then(1, () -> {
                    player.animate(2588);
                    player.teleport(2702, 3465, 2);
                }).then(1, () -> {
                    player.animate(-1);
                    player.skills().addXp(Skills.AGILITY, 15.0);
                    player.unlock();
                    player.putAttrib(AttributeKey.SEERS_ROOFTOP_COURSE_STATE,5);
                    MarksOfGrace.trySpawn(player, MARK_SPOTS, 60, 20);
                });
            });
            return true;
        }

        // Final gap jump down
        if(obj.getId() == EDGE_14931) {
            player.lock();
            player.face(2703, 3461);
            Chain.bound(player).name("SeersRooftopGapJump3Task").runFn(1, () -> {
                player.animate(2462, 15);
                player.sound(2586, 15);
            }).then(1, () -> {
                player.teleport(2704, 3464, 0);
                player.animate(2588);
            }).then(1, () -> {
                player.animate(-1);

                int stage = player.getAttribOr(AttributeKey.SEERS_ROOFTOP_COURSE_STATE, 0);
                if (stage == 5) {
                    player.putAttrib(AttributeKey.SEERS_ROOFTOP_COURSE_STATE, 0);
                    player.skills().addXp(Skills.AGILITY, 435.0);

                    // Woo! A pet!
                    var odds = (int) (13000 * player.getMemberRights().petRateMultiplier());
                    if (World.getWorld().rollDie(odds, 1)) {
                        UnlockAgilityPet.unlockGiantSquirrel(player);
                    }
                } else {
                    player.skills().addXp(Skills.AGILITY, 15.0);
                }
                player.unlock();
                MarksOfGrace.trySpawn(player, MARK_SPOTS, 60, 20);
            });
            return true;
        }
        return false;
    }

}
