package com.twisted.game.content.areas.riskzone;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.route.StepType;
import com.twisted.net.packet.interaction.PacketInteraction;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

import static com.twisted.util.ObjectIdentifiers.*;

public class RiskFightArea extends PacketInteraction {

    public static final Area NH_AREA = new Area(3071, 3466, 3080, 3473);

    //public static final Area ONE_V_ONE_1 = new Area(3111, 3505, 3116, 3509);
    //public static final Area ONE_V_ONE_2 = new Area(3103, 3513, 3110, 3517);
    //public static final Area ONE_V_ONE_3 = new Area(3111, 3513, 3116, 3517);
    private void walkDown(Player player, boolean xDown) {
        Tile targTile = player.tile().transform(xDown ? -1 : +1, 0, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }

    private void walk(Player player, GameObject obj) {
        player.runFn(1, () -> {
            int x = player.getAbsX();
            int y = player.getAbsY();

            switch (obj.getRotation()) {
                case 1:
                case 3:
                    if (y < obj.tile().y) {
                        System.out.println("we here 3");
                        y += 1;
                    } else {
                        System.out.println("we here 4");
                        y -= 1;
                    }
                default:
                    break;
            }
            player.lock();
            player.stepAbs(x, y, StepType.FORCE_WALK);
            final int finalY = y;
            player.waitForTile(new Tile(x, finalY), player::unlock);
        });
    }

    private void walkX(Player player, boolean xUp) {
        Tile targTile = player.tile().transform(xUp ? +1 : -1, 0, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }

    private void walkYx(Player player, boolean yUp) {
        Tile targTile = player.tile().transform(0, yUp ? -1 : -1, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }

    private void walkY(Player player, boolean yUp) {
        Tile targTile = player.tile().transform(0, yUp ? +1 : -1, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            var risk = player.<Long>getAttribOr(AttributeKey.RISKED_WEALTH, 0L);
            if (obj.getId() == BELL_21394) {
                if (player.getTimerRepository().has(TimerKey.RISK_FIGHT_BELL)) {
                    player.message(Color.RED.wrap("You can ring the bell again in " + player.getTimerRepository().asMinutesAndSecondsLeft(TimerKey.RISK_FIGHT_BELL) + "."));
                } else {
                    player.getTimerRepository().register(TimerKey.RISK_FIGHT_BELL, 300);
                    World.getWorld().sendWorldMessage("<img=162>" + Color.BLUE.wrap(player.getUsername()) + Color.RAID_PURPLE.wrap(" has just rung the bell at the risk zone and is looking for a fight!"));
                }
                return true;
            }
            if (obj.getId() == MAGICAL_BARRIER_31808) {
                walk(player, obj);
                return true;
            }
            if (obj.getId() == ENERGY_BARRIER_4470) {//Dzone3 right
                if (obj.tile().equals(2911, 2776)
                    || obj.tile().equals(2911, 2777)
                    || obj.tile().equals(2911, 2768)
                    || obj.tile().equals(2911, 2759)
                    || obj.tile().equals(2911, 2760)) {
                    if (player.tile().x == 2910) {
                        player.message(Color.RED.wrap("Thank you for supporting the server."));
                        player.message(Color.RED.wrap("You sense that the barrier has a magical force that won't let you back out."));
                        walkX(player, true);
                    } else if (player.tile().x == 2911) {
                        player.message(Color.RED.wrap("Thank you for supporting the server."));
                        walkDown(player, true);
                    }
                    /*if (player.tile().x == 2911) {
                        walkDown(player, true);
                    }*/
                    return true;

                }
            }
            if (obj.getId() == ENERGY_BARRIER_4470) {//Dzone3 left
                if (obj.tile().equals(2906, 2759)
                    || obj.tile().equals(2906, 2760)
                    || obj.tile().equals(2906, 2768)
                    || obj.tile().equals(2906, 2776)
                    || obj.tile().equals(2906, 2777)) {
                  /*  if (player.tile().x == 2906) {
                        walkX(player, true);
                    }*/
                    if (player.tile().x == 2907) {
                        walkDown(player, true);
                        player.message(Color.RED.wrap("Thank you for supporting the server."));
                        player.message(Color.RED.wrap("You sense that the barrier has a magical force that won't let you back out."));

                    } else if (player.tile().x == 2906) {
                        player.message(Color.RED.wrap("Thank you for supporting the server."));
                        walkX(player, true);
                    }
                    return true;

                }
            }
            if (obj.getId() == ENERGY_BARRIER_4470) {//Dzone3 enter
                if (obj.tile().equals(2908, 2779)
                    || obj.tile().equals(2909, 2779)) {
                    if (player.tile().y == 2780) {
                        player.message(Color.RED.wrap("Thank you for supporting the server."));
                        walkYx(player, true);
                    }
                   if (player.tile().y == 2779) {
                        walkY(player, true);
                    }
                    return true;

                }
            }
            if (obj.getId() == ENERGY_BARRIER_4470) {//Dzone2 Revs
                if (obj.tile().equals(2460, 2800)
                    || obj.tile().equals(2461, 2800)) {
                    if (player.tile().y == 2799) {
                        player.message(Color.RED.wrap("Thank you for supporting the server."));
                        walkY(player, true);
                    }
                    if (player.tile().y == 2800) {
                        walkYx(player, true);
                    }
                    return true;

                }
            }
            if (obj.getId() == ENERGY_BARRIER_4470) {//Dzone2 right side
                if (obj.tile().equals(2468, 2801)
                    || obj.tile().equals(2468, 2802)
                    || obj.tile().equals(2468, 2783)
                    || obj.tile().equals(2468, 2784)
                    || obj.tile().equals(2468, 2767)
                    || obj.tile().equals(2468, 2768)) {
                    if (player.tile().x == 2467) {
                        player.message(Color.RED.wrap("Thank you for supporting the server."));
                        walkX(player, true);
                    }
                    if (player.tile().x == 2468) {
                        walkDown(player, true);
                    }
                    return true;

                }
            }
            if (obj.getId() == ENERGY_BARRIER_4470) {//Dzone2 left side
                if (obj.tile().equals(2453, 2802)
                    || obj.tile().equals(2453, 2803)
                    || obj.tile().equals(2453, 2787)
                    || obj.tile().equals(2453, 2788)
                    || obj.tile().equals(2453, 2768)
                    || obj.tile().equals(2453, 2769)) {
                    if (player.tile().x == 2454) {
                        player.message(Color.RED.wrap("Thank you for supporting the server."));
                        walkDown(player, true);

                    }
                    if (player.tile().x == 2453) {
                        walkX(player, true);
                    }
                    return true;
                }
            }

            if (obj.getId() == ENERGY_BARRIER_4470) {
                if (obj.tile().equals(3043, 3789, 0) || obj.tile().equals(3044, 3789, 0)) {
                    if (player.tile().y == 3789) {
                        player.message(Color.RED.wrap("You sense that the barrier has a magical force that won't let you back out."));
                        walkY(player, true);
                    } else if (player.tile().y == 3790) {
                        player.message(Color.RED.wrap("You sense that the barrier has a magical force that won't let you back out."));
                        walkY(player, false);
                    }
                    return true;
                }
            }


            if (obj.getId() == ENERGY_BARRIER_4470) {
                //500k risk area
                if (obj.tile().equals(3106, 3512, 0) || obj.tile().equals(3107, 3512, 0)) {
                    player.getRisk().update(); // make sure our wealth is up to date.
                    if (risk <= 500_000) {
                        player.message(Color.RED.wrap("You need to risk at least 500K blood money to enter this risk zone."));
                        return true;
                    }
                    if (player.tile().y == 3512) {
                        if (!player.tile().equals(obj.tile().transform(0, 0, 0))) {
                            player.getMovementQueue().walkTo(obj.tile().transform(0, 0, 0));
                        }
                        Chain.bound(player).name("RiskFightArenaTask").waitForTile(obj.tile().transform(0, 0, 0), () -> {
                            Skulling.assignSkullState(player, SkullType.RED_SKULL);
                            player.forceChat("I am currently risking " + Utils.formatNumber(risk) + " BM!");
                            Tile targTile = player.tile().transform(0, +1, 0);
                            player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
                            Chain.bound(player).name("RiskFightArenaTask2").waitForTile(targTile, player::unlock);
                        });
                    } else if (player.tile().y == 3513) {
                        Skulling.unskull(player);
                        walkY(player, false);
                    }
                    return true;
                }
                //250k risk area
                if (obj.tile().equals(3110, 3508, 0) || obj.tile().equals(3110, 3507, 0)) {
                    player.getRisk().update(); // make sure our wealth is up to date.
                    if (risk <= 250_000) {
                        player.message(Color.RED.wrap("You need to risk at least 250K blood money to enter this risk zone."));
                        return true;
                    }
                    if (player.tile().x == 3110 && (player.tile().y == 3507 || player.tile().y == 3508)) {
                        Skulling.assignSkullState(player, SkullType.RED_SKULL);
                        player.forceChat("I am currently risking " + Utils.formatNumber(risk) + " BM!");
                        walkX(player, true);
                    } else if (player.tile().x == 3111 && (player.tile().y == 3507 || player.tile().y == 3508)) {
                        Skulling.unskull(player);
                        walkX(player, false);
                    }
                    return true;
                }
                //1M risk area
                if (obj.tile().equals(3112, 3512, 0) || obj.tile().equals(3113, 3512, 0)) {
                    player.getRisk().update(); // make sure our wealth is up to date.
                    if (risk <= 1_000_000) {
                        player.message(Color.RED.wrap("You need to risk at least 1M blood money to enter this risk zone."));
                        return true;
                    }
                    if (player.tile().y == 3512 && (player.tile().x == 3112 || player.tile().x == 3113)) {
                        Skulling.assignSkullState(player, SkullType.RED_SKULL);
                        player.forceChat("I am currently risking " + Utils.formatNumber(risk) + " BM!");
                        walkY(player, true);
                    } else if (player.tile().y == 3513 && (player.tile().x == 3112 || player.tile().x == 3113)) {
                        Skulling.unskull(player);
                        walkY(player, false);
                    }
                    return true;
                }
                //No restrictions here
                if (obj.tile().equals(3076, 3473, 0) || obj.tile().equals(3075, 3473, 0) || obj.tile().equals(3117, 3510, 0)) {
                    if (player.tile().y == 3474) {
                        Skulling.assignSkullState(player, SkullType.RED_SKULL);
                        player.forceChat("I am currently risking " + Utils.formatNumber(risk) + " BM!");
                        walkY(player, false);
                    } else if (player.tile().y == 3473) {
                        if (player.tile().y <= 3473) {
                            if (!player.tile().equals(obj.tile().transform(0, 0, 0))) {
                                player.getMovementQueue().walkTo(obj.tile().transform(0, 0, 0));
                            }
                            Chain.bound(player).name("RiskFightArenaTask").waitForTile(obj.tile().transform(0, 0, 0), () -> {
                                Skulling.unskull(player);
                                Tile targTile = player.tile().transform(0, 1, 0);
                                player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
                                Chain.bound(player).name("RiskFightArenaTask2").waitForTile(targTile, player::unlock);
                            });
                        }
                    }
                    return true;
                }
                return true;
            }
        }
        return false;
    }
}
