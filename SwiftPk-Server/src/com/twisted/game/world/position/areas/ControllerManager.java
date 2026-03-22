package com.twisted.game.world.position.areas;

import com.twisted.game.content.gambling.GamblingArea;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.*;

import java.util.ArrayList;
import java.util.List;

public class ControllerManager {

    private static final List<Controller> CONTROLLERS = new ArrayList<>();

    static {
        CONTROLLERS.add(new DuelArenaArea());
        CONTROLLERS.add(new WildernessArea());
        CONTROLLERS.add(new TournamentArea());
        CONTROLLERS.add(new FightCaveArea());
        CONTROLLERS.add(new GamblingArea());
        CONTROLLERS.add(new COXArea());
        CONTROLLERS.add(new TOBArea());
        CONTROLLERS.add(new ChamberOfSecretsRaidArea());
    }

    /**
     * Processes areas for the given mob.
     */
    public static void process(Mob mob) {
        Tile tile = mob.tile();
        Controller controller = mob.getController();

        if (controller != null) {
            //We only want to check using the abstract area or using the area manager, not both,
            //since wilderness does not have the correct coordinates in the constructor, and
            //wilderness also uses custom code for determining wilderness "level".
            if ((!controller.useInsideCheck() && !inside(tile, controller)) || (controller.useInsideCheck() && !controller.inside(mob))) {
                //System.out.println(mob.getMobName() + " leaving " + controller + " located at " + mob.tile());
                controller.leave(mob);
                controller = null;
            }
        }

        if (controller == null) {
            controller = get(tile);
            if (controller == null) {//fallback
                for (Controller area : CONTROLLERS) {
                    if (area.useInsideCheck() && area.inside(mob)) {
                        controller = area;
                        break;
                    }
                }
            }
            if (controller != null) {
                //System.out.println(mob.getMobName() + " entering " + controller + " located at " + mob.tile());
                controller.enter(mob);
            }
        }

        // Handle processing..
        if (controller != null) {
            controller.process(mob);
        }
        if (mob.isPlayer())
            //System.out.println(mob.tile()+" "+controller);

        // Update area..
        mob.setController(controller);
    }

    /**
     * Checks if a {@link Mob} can attack another one.
     */
    public static boolean canAttack(Mob attacker, Mob target) {
        if (attacker.getController() != null) {
            return attacker.getController().canAttack(attacker, target);
        }

        // Don't allow PvP by default
        if (attacker.isPlayer() && target.isPlayer()) {
            // bypass
            return target.getAsPlayer().getTournamentOpponent() == attacker;
        }

        return true;
    }

    /**
     * Checks if a position is inside of an area's boundaries.
     */
    // there are always more reasons that just position to check if a mob is in a area, so i'd have another check that takes the mob as an argument
    public static boolean inside(Tile tile, Controller controller) {
        for (Area area : controller.getAreas()) {
            if (area.contains(tile)) {
                return true;
            }
        }
        return false;
    }

    public static Controller get(Tile tile) {
        for (Controller controller : CONTROLLERS) {
            if (inside(tile, controller)) {
                return controller;
            }
        }
        return null;
    }
}
