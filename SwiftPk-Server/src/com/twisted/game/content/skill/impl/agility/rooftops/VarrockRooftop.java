package com.twisted.game.content.skill.impl.agility.rooftops;

import com.twisted.game.content.skill.impl.agility.MarksOfGrace;
import com.twisted.game.content.skill.impl.agility.UnlockAgilityPet;
import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.ForceMovementTask;
import com.twisted.game.world.World;
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
 * juni 13, 2020
 */
public class VarrockRooftop extends PacketInteraction {

    private static final List<Tile> MARK_SPOTS = Arrays.asList(
        new Tile(3214, 3417, 3),
        new Tile(3202, 3417, 3),
        new Tile(3194, 3416, 1),
        new Tile(3194, 3404, 3),
        new Tile(3196, 3394, 3),
        new Tile(3205, 3395, 3),
        new Tile(3226, 3402, 3),
        new Tile(3236, 3407, 3)
    );

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        // Wall climb
        if (obj.getId() == ROUGH_WALL_14412) {
            if (player.skills().xpLevel(Skills.AGILITY) < 30) {
                player.message("You need an Agility level of 30 to attempt this.");
            } else {
                player.lock();
                player.face(player.tile().transform(-1, 0));
                Chain.bound(player).name("VarrockWallclimbTask").runFn(1, () -> player.animate(828, 15)).then(2, () -> {
                    player.teleport(3220, 3414, 3);
                    player.animate(2585);
                }).then(2, () -> {
                    player.teleport(3219, 3414, 3);
                    player.animate(-1);
                    player.skills().addXp(Skills.AGILITY, 12.0);
                    player.unlock();
                    MarksOfGrace.trySpawn(player, MARK_SPOTS, 40, 30);
                });
            }
            return true;
        }

        // Clothes line
        if (obj.getId() == CLOTHES_LINE) {
            player.lockNoDamage();
            Chain.bound(player).name("VarrockClotheslineTask").runFn(1, () -> {
                player.sound(2461);
                player.animate(741);
                TaskManager.submit(new ForceMovementTask(player, 0, new ForceMovement(player.tile().clone(), new Tile(-2, 0), 15, 30, Direction.WEST.toInteger())));
            }).then(1, () -> {
                player.sound(2461, 15);
                player.animate(741, 15);
                TaskManager.submit(new ForceMovementTask(player, 0, new ForceMovement(player.tile().clone(), new Tile(-2, 0), 30, 45, Direction.WEST.toInteger())));
            }).then(1, () -> {
                player.sound(2461);
                player.animate(741);
                TaskManager.submit(new ForceMovementTask(player, 0, new ForceMovement(player.tile().clone(), new Tile(-2, 0), 15, 30, Direction.WEST.toInteger())));
            }).then(1, () -> {
                player.skills().addXp(Skills.AGILITY, 21.0);
                player.unlock();

                MarksOfGrace.trySpawn(player, MARK_SPOTS, 40, 30);
            });
            return true;
        }

        // Jump down
        if (obj.getId() == GAP_14414) {
            player.lock();
            player.face(player.tile().transform(-1, 0));
            Chain.bound(player).name("VarrockJumpdownTask").runFn(1, () -> {
                player.sound(2462, 15);
                player.animate(2586, 15);
            }).then(1, () -> {
                player.teleport(3197, 3416, 1);
                player.animate(2588);
            }).then(1, () -> {
                player.animate(-1);
                player.skills().addXp(Skills.AGILITY, 17.0);
                player.unlock();

                MarksOfGrace.trySpawn(player, MARK_SPOTS, 40, 30);
            });
            return true;
        }

        // Swing wall
        if (obj.getId() == WALL_14832) {
            Tile startPos = obj.tile().transform(3, 1);
            //player.smartPathTo(startPos, obj.getSize());
            player.smartPathTo(startPos);
            player.waitForTile(startPos, () -> {}).then(() -> {
                //Run towards start
                player.getMovementQueue().interpolate(3194, 3416, MovementQueue.StepType.FORCED_WALK);
            }).name("VarrockSwingwall1Task")
                .waitForTile(new Tile(3194, 3416), () -> {
                    //Reached start
                    player.lock();
                    player.face(player.tile().transform(-1, 0));
                }).then(1, () -> {
                //Animate running
                player.animate(1995, 15);
                //Runs towards jump spot
                TaskManager.submit(new ForceMovementTask(player, 0, new ForceMovement(player.tile().clone(), new Tile(-1, 0), 15, 45, Direction.WEST.toInteger())));

                //Jump spot reached
            }).waitForTile(new Tile(3193, 3416, 1), () -> {
                player.sound(2468, 20);
                player.animate(1124);
                TaskManager.submit(new ForceMovementTask(player, 0, new ForceMovement(player.tile().clone(), new Tile(-3, -2), 25, 30, Direction.WEST.toInteger())));
            }).then(1, () -> {
                //Do hang animations and other stuff
                for (int i = 0; i < 5; i++) {
                    Chain.bound(player).name("VarrockSwingwall2Task").runFn(i * 2, () -> {
                        player.sound(2459, 35);
                        player.animate(1122);
                        TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, -1), 34, 52, Direction.WEST.toInteger())));
                    });
                    player.face(player.tile().transform(0, -1));
                }

                //End of wall position
            }).waitForTile(new Tile(3190, 3409, 1), () -> {
                //Start doing side walk
                player.agilityWalk(false);
                player.looks().render(757, 757, 756, 756, 756, 756, -1);
                player.animate(753);
                player.getMovementQueue().interpolate(3190, 3407, MovementQueue.StepType.FORCED_WALK);
                //Reached the spot jump up the roof
            }).waitForTile(new Tile(3190, 3407, 1), () -> {
                player.agilityWalk(true);
                player.looks().resetRender();
                player.animate(741);
                TaskManager.submit(new ForceMovementTask(player, 0, new ForceMovement(player.tile().clone(), new Tile(2, -1), 5, 30, Direction.EAST.toInteger())));
                //Delay movement by two ticks, so we can finish the lovely force movement mask.
                Chain.bound(player).name("VarrockSwingwall3Task").runFn(2, () -> {
                    player.teleport(3192, 3406, 3);
                    player.skills().addXp(Skills.AGILITY, 25.0);
                    player.unlock();

                    MarksOfGrace.trySpawn(player, MARK_SPOTS, 40, 30);
                });
            });
            return true;
        }

        // Jump gap
        if (obj.getId() == GAP_14833) {
            player.lock();
            player.face(player.tile().transform(0, -1));
            Chain.bound(player).name("VarrockJumpgap1Task").runFn(1, () -> {
                player.sound(2468, 20);
                player.animate(2583, 10);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, -3), 25, 30, Direction.SOUTH.toInteger())));
            }).then(1, () -> {
                player.teleport(3193, 3399, 3);
                player.animate(2585);
            }).then(1, () -> TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, -1), 17, 26, Direction.SOUTH.toInteger())))).then(1, () -> {
                player.teleport(3193, 3398, 3);
                player.skills().addXp(Skills.AGILITY, 9.0);
                player.unlock();

                MarksOfGrace.trySpawn(player, MARK_SPOTS, 40, 30);
            });
            return true;
        }

        // Jump gap
        if (obj.getId() == GAP_14834) {
            player.lock();
            player.face(player.tile().transform(+1, 0));
            Chain.bound(player).name("VarrockJumpgap2Task").waitForTile(new Tile(3208, 3397, 3), () -> {
                player.animate(1995);
                player.sound(2461, 15);
            }).then(1, () -> {
                player.animate(4789, 15);
                TaskManager.submit(new ForceMovementTask(player, 0, new ForceMovement(player.tile().clone(), new Tile(7, 2), 20, 50, Direction.EAST.toInteger())));
            }).then(1, () -> player.teleport(3215, 3399, 3)).then(1, () -> {
                player.sound(2468);
                player.animate(2583);
                player.face(new Tile(3222, 3399, 3));
                player.teleport(3217, 3399, 3);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(2, 0), 5, 10, Direction.EAST.toInteger())));
            }).then(1, () -> {
                player.face(new Tile(3222, 3399, 3));
                player.teleport(3217, 3399, 3);

                player.animate(2585);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(1, 0), 18, 27, Direction.EAST.toInteger())));
                player.face(new Tile(3222, 3399, 3));
            }).then(1, () -> {
                player.face(new Tile(3222, 3399, 3));
                player.animate(-1);
                player.teleport(3218, 3399, 3);
                player.skills().addXp(Skills.AGILITY, 22.0);
                player.unlock();

                MarksOfGrace.trySpawn(player, MARK_SPOTS, 40, 30);
            });
            return true;
        }

        // Jump another gap
        if (obj.getId() == GAP_14835) {
            Tile startPos = obj.tile().transform(-1, 0);
            player.smartPathTo(startPos);
            player.waitForTile(startPos, () -> {}).then(() -> {
                player.lock();
                player.sound(2462, 15);
                player.animate(2586, 15);
            }).then(1, () -> {
                player.teleport(3236, 3403, 3);
                player.animate(2588);
            }).then(1, () -> {
                player.animate(-1);
                player.skills().addXp(Skills.AGILITY, 4.0);
                player.unlock();

                MarksOfGrace.trySpawn(player, MARK_SPOTS, 40, 30);
            });
            return true;
        }

        // Jump up
        if (obj.getId() == LEDGE_14836) {
            Tile startPos = obj.tile().transform(0, -1);
            player.smartPathTo(startPos);
            player.waitForTile(startPos, () -> {
                player.lock();
                player.face(player.tile().transform(0, 1));
            })
                .name("VarrockJumpgap4Task").then(1, () -> {
                player.sound(1936);
                player.animate(1603);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, 2), 8, 50, Direction.NORTH.toInteger())));
            }).then(1, () -> {
                player.teleport(3236, 3410, 3);
                player.skills().addXp(Skills.AGILITY, 3.0);
                player.unlock();

                MarksOfGrace.trySpawn(player, MARK_SPOTS, 40, 30);
            });
            return true;
        }

        // Jump down from the roof
        if (obj.getId() == EDGE) {
            Tile startPos = obj.tile().transform(0, -1);
            player.smartPathTo(startPos);
            player.waitForTile(startPos, () -> {}).then(() -> {
                player.lock();
                player.face(player.tile().transform(0, 1));
            })
                .name("VarrockEdgeTask").then(1, () -> {
                player.animate(741);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, 1), 15, 30, Direction.NORTH.toInteger())));
            }).then(1, () -> player.teleport(3236, 3416, 3)).then(1, () -> {
                player.sound(2462, 15);
                player.animate(2586, 15);
            }).then(1, () -> {
                player.teleport(3236, 3417, 0);
                player.animate(2588);
                player.skills().addXp(Skills.AGILITY, 125.0);
                player.unlock();
                MarksOfGrace.trySpawn(player, MARK_SPOTS, 40, 30);

                // Woo! A pet!
                var odds = (int) (26000 * player.getMemberRights().petRateMultiplier());
                if (World.getWorld().rollDie(odds, 1)) {
                    UnlockAgilityPet.unlockGiantSquirrel(player);
                }
            });
            return true;
        }
        return false;
    }

}
