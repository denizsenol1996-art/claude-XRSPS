package com.twisted.game.content.skill.impl.agility.course;

import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.twisted.game.content.packet_actions.interactions.objects.Ladders;
import com.twisted.game.content.skill.impl.agility.UnlockAgilityPet;
import com.twisted.game.content.tasks.impl.Tasks;
import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.ForceMovementTask;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.player.ForceMovement;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.game.world.entity.AttributeKey.WILDY_COURSE_STATE;

/**
 * Created by Jak on 13/06/2016.
 */
public class WildernessCourse extends PacketInteraction {

    public final static int LOWER_GATE = 23555;
    public final static int UPPERGATE_EAST = 23552;
    public final static int UPPERGATE_WEST = 23554;
    private final static int PIPE = 23137;
    private final static int ROPESWING = 23132;
    private final static int LADDERDOWN = 14758;
    // ladder up is same as barb course
    private final static int STEPPINGSTONE = 23556;
    private final static int LOGBALANCE = 23542;
    private final static int ROCKS = 23640;

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            if (obj.getId() == LOWER_GATE) {
                if (player.skills().level(Skills.AGILITY) < 52) {
                    player.message("You do not have the required level to enter this course.");
                    return true;
                }
                lowergate(player, obj);
                return true;
            }
            if (obj.getId() == UPPERGATE_EAST) {
                uppergate(player, obj);
                return true;
            }

            if (obj.getId() == UPPERGATE_WEST) {
                uppergate(player, obj);
                return true;
            }

            if (obj.getId() == PIPE) {
                player.smartPathTo(obj.tile());
                Chain.bound(player).name("WildernessCourse1Task").waitForTile(new Tile(3004, 3937), () -> {
                    player.lockDelayDamage();
                    player.animate(749, 30);
                }).then(1, () -> {
                    player.teleport(3004, 3950);
                    var stage = player.<Integer>getAttribOr(WILDY_COURSE_STATE, 0) + 1;
                    player.putAttrib(WILDY_COURSE_STATE, stage);
                    player.skills().addXp(Skills.AGILITY, 12.5);
                    player.unlock();
                });
                return true;
            }

            if (obj.getId() == ROPESWING) {
                player.smartPathTo(new Tile(3005, 3951)); // turns out this tile is solid clipped.
                // when you get here it should do a run up but doesnt.
                player.waitForTile(new Tile(3005, 3951), () -> {
                    player.face(player.tile().transform(0, 10));
                    player.lockDelayDamage();
                    player.getPacketSender().sendObjectAnimation(obj, 54);// make rope pull back
                    player.animate(751); // swing

                    TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, 7), 30, 50, 0))); // move

                    // 2t later
                    Chain.bound(player).name("WildernessCourse4Task").runFn(1, () -> {
                        player.getPacketSender().sendObjectAnimation(obj, 55);
                        var stage = player.<Integer>getAttribOr(WILDY_COURSE_STATE, 0) + 1;
                        player.putAttrib(WILDY_COURSE_STATE, stage);
                        //System.out.println("stage is now: "+stage);
                        player.skills().addXp(Skills.AGILITY, 20.0);
                    }).then(2, player::unlock);
                });
                return true;
            }

            if (obj.getId() == LADDERDOWN) {
                Ladders.ladderDown(player, player.tile().transform(0, 6400), true);
                return true;
            }

            if (obj.getId() == STEPPINGSTONE) {
                player.smartPathTo(new Tile(3002, 3960));
                player.waitUntil(() -> player.tile().equals(3002, 3960), () -> {
                    player.getMovementQueue().interpolate(3002, 3960, MovementQueue.StepType.FORCED_WALK);
                    player.lockDelayDamage();
                }).then(2, () -> {
                    player.face(new Tile(0, player.tile().y));
                    //Increase stage when starting the obstacle
                    var stage = player.<Integer>getAttribOr(WILDY_COURSE_STATE, 0) + 1;
                    player.putAttrib(WILDY_COURSE_STATE, stage);
                    //System.out.println("stage is now: "+stage);

                    for (int i = 0; i < 6; i++) {
                        // add new Tasks @ instantly (0*3=0), 3 6, 9, 12
                        Chain.bound(player).name("WildernessCourse7Task").runFn(i * 3, () -> {
                            player.animate(741);
                            TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(-1, 0), 5, 35, 3)));
                        });
                    }
                    Chain.bound(player).name("WildernessCourse8Task").waitForTile(new Tile(2996, 3960), () -> {
                        player.skills().addXp(Skills.AGILITY, 20.0);
                        player.unlock();
                    });
                });
                return true;
            }

            if (obj.getId() == LOGBALANCE) {
                player.smartPathTo(new Tile(3002, 3945));
                player.waitForTile(new Tile(3002, 3945), () -> {

                    if (!player.tile().equals(3002, 3945)) { // Get in position
                        player.getMovementQueue().interpolate(3002, 3945, MovementQueue.StepType.FORCED_WALK);
                    }

                    Tile end = new Tile(2994, 3945);
                    Chain.bound(player).name("WildernessCourse9Task").runFn(1, () -> {
                        player.lockDelayDamage();
                        player.message("You walk carefully across the slippery log...");
                        player.getMovementQueue().clear();
                        player.agilityWalk(false);
                        player.getMovementQueue().interpolate(end, MovementQueue.StepType.FORCED_WALK);
                        player.looks().render(763, 762, 762, 762, 762, 762, -1);
                    });

                    Chain.bound(player).name("WildernessCourse10Task").waitForTile(end, () -> {
                        player.looks().resetRender();
                        var stage = player.<Integer>getAttribOr(WILDY_COURSE_STATE, 0) + 1;
                        player.putAttrib(WILDY_COURSE_STATE, stage);
                        //System.out.println("stage is now: "+stage);
                        player.skills().addXp(Skills.AGILITY, 20.0);
                        player.message("...You make it safely to the other side.");
                        player.agilityWalk(true);
                        player.unlock();
                    });
                });
                return true;
            }

            if (obj.getId() == ROCKS) {
                player.waitUntil(1, () -> player.tile().y == 3937, () -> {
                    if (!player.tile().equals(2995, 3937)) { // Get in position
                        player.getMovementQueue().interpolate(2995, 3937, MovementQueue.StepType.FORCED_WALK);
                        player.getMovementQueue().clear();
                    }

                    Chain.bound(player).name("WildernessCourse13Task").runFn(1, () -> {
                        player.lockDelayDamage();
                        player.animate(740);
                        TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, -4), 5, 80, 2))); // move
                    }).then(3, () -> {
                        player.teleport(new Tile(2995, 3934));
                        player.looks().resetRender();
                        player.animate(-1);
                        var stage = player.<Integer>getAttribOr(WILDY_COURSE_STATE, 0) + 1;
                        player.putAttrib(WILDY_COURSE_STATE, stage);
                        player.skills().addXp(Skills.AGILITY, 498.9);
                        //System.out.println("stage is now: "+stage);
                        if (stage == 5) {
                            player.getTaskMasterManager().increase(Tasks.WILDERNESS_COURSE);
                            DailyTaskManager.increase(DailyTasks.WILDY_AGLITY, player);

                            // Woo! A pet!
                            var odds = (int) (18000 * player.getMemberRights().petRateMultiplier());
                            if (World.getWorld().rollDie(odds, 1)) {
                                UnlockAgilityPet.unlockGiantSquirrel(player);
                            }

                            //Finding blood money while skilling..
                            if (WildernessArea.inWilderness(player.tile())) {
                                if (World.getWorld().rollDie(5, 1)) {
                                    if (player.inventory().add(new Item(13307, 5), true)) {
                                        player.message("You find 5 blood money coin on the ground.");
                                    }
                                }
                            }
                        }
                        player.putAttrib(WILDY_COURSE_STATE, 0);
                        player.unlock();
                    });
                });
                return true;
            }
        }
        return false;
    }

    // TODO make formula include exponential success growth for levels over requirement ending at 70
    public static boolean successful(Player player) {
        int req = 35;
        int endeffectiveness = 70;
        int gap = player.skills().xpLevel(Skills.AGILITY) - req;
        double failrate = 0.30;
        return Math.random() >= (failrate - ((failrate / (endeffectiveness - req)) * gap));
    }

    private static void uppergate(Player player, GameObject obj) {
        if (!player.tile().equals(obj.tile())) {
            player.getMovementQueue().walkTo(obj.tile());
        }

        var end = new Tile(2998, 3916);
        Chain.bound(player).waitForTile(obj.tile(), () -> {
            player.face(player.tile().transform(0, -1));

            Chain.bound(player).name("uppergate").runFn(1, () -> {
                // Walk away!
                player.lockDelayDamage();

                // Agility animation
                player.getMovementQueue().clear();

                player.agilityWalk(false);
                player.getMovementQueue().interpolate(end, MovementQueue.StepType.FORCED_WALK);
                player.looks().render(763, 762, 762, 762, 762, 762, -1);
                openDoubleDoors();
            }).then(13, () -> {
                openEntranceGate(player, obj);
            }).waitForTile(end, () -> {
                player.looks().resetRender();
                player.agilityWalk(true);
                player.unlock();
            });
        });
    }

    private static void lowergate(Player player, GameObject obj) {
        if (player.skills().xpLevel(Skills.AGILITY) < 52) {
            player.message("You need a Agility level of 52 to pass this gate.");
            return;
        }

        if (obj.interactAble() && obj.tile().equals(2998, 3917)) {
            player.lockDelayDamage();

            // Agility animation
            player.getMovementQueue().clear();
            var end = new Tile(2998, 3931);
            player.getMovementQueue().interpolate(end, MovementQueue.StepType.FORCED_WALK);
            player.agilityWalk(false);
            player.looks().render(763, 762, 762, 762, 762, 762, -1);

            // Handle the door
            openEntranceGate(player, obj);

            // Finish up agility part
            Chain.bound(player).name("lowergateTask").runFn(13, () -> {
                // The ending doors
                openDoubleDoors();
            }).waitForTile(end, () -> {
                player.looks().resetRender();
                player.agilityWalk(true);
                player.unlock();
            });
        }
    }

    private static void openEntranceGate(Player player, GameObject obj) {
        obj.interactAble(false);
        ObjectManager.removeObj(obj);
        var newobj = new GameObject(1548, new Tile(2997, 3917), obj.getType(), 2).interactAble(false);
        ObjectManager.addObj(newobj);
        Chain.bound(player).name("openEntranceGateTask").runFn(2, () -> {
            // Put the old door back
            ObjectManager.removeObj(newobj);
            ObjectManager.addObj(obj);
            obj.interactAble(true);
        });
    }

    private static void openDoubleDoors() {
        GameObject openDoor1 = new GameObject(23552, new Tile(2998, 3931, 0), 0, 3);
        GameObject rotateDoor1 = new GameObject(23552, new Tile(2998, 3931, 0), 0, 2);
        ObjectManager.replace(openDoor1, rotateDoor1, 3);

        GameObject openDoor2 = new GameObject(23554, new Tile(2997, 3931, 0), 0, 3);
        GameObject rotateDoor2 = new GameObject(23554, new Tile(2997, 3931, 0), 0, 4);
        ObjectManager.replace(openDoor2, rotateDoor2, 3);
    }
}
