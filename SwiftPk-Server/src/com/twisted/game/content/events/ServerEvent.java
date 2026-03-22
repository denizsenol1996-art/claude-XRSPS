package com.twisted.game.content.events;

import com.twisted.game.world.World;
import com.twisted.util.Color;
import com.twisted.util.SecondsTimer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServerEvent {

    public String name;
    public SecondsTimer timer;

    public static int WELL_OF_GOODWILL_AMOUNT = 0;
    public static final int WELL_OF_GOODWILL_MAX = WOGWData.VALUES.length * 5_000_000;
    public static final List<ServerEvent> activeEvents = new ArrayList<>();

    public ServerEvent(String name) {
        this.name = name;
    }

    public void start() {
        this.timer = new SecondsTimer();
        this.timer.start((int) (6000 * 0.6));
    }

    public static void tick() {
        if (activeEvents.isEmpty()) {
            return;
        }

        for (final ServerEvent event : activeEvents) {
            if (event == null) continue;
            if (event.timer.expiredAfterBeingRun()) {
                World.getWorld().sendWorldMessage(Color.CYAN.wrap("<img=2014><shad=0> The Well Of Goodwill " + event.name + " Has Now Ended... </shad></img>"));
            }
        }

        removeCompletedEvents();
    }

    private static void removeCompletedEvents() {
        activeEvents.removeIf(event -> event.timer.completed);
    }

    public static boolean isActive(WOGWData wogwData) {
        return !ServerEvent.activeEvents.isEmpty() && ServerEvent.activeEvents.contains(wogwData.event);
    }

    public static boolean isDoubleVotes() {
        return isActive(WOGWData.DOUBLE_VOTES);
    }

    public static boolean isDoubleExperience() {
        return isActive(WOGWData.DOUBLE_EXP);
    }

    public static boolean isDoubleDrops() {
        return isActive(WOGWData.DOUBLE_DROPS);
    }

    public static boolean isDropRateBoost() {
        return isActive(WOGWData.DROP_RATE);
    }

    public static boolean isBloodMoneyBoost() {
        return isActive(WOGWData.BLOOD_MONEY_BOOST);
    }

    public static boolean isDoubleSlayerPoints() {
        return isActive(WOGWData.DOUBLE_SLAYER_POINTS);
    }

    public static void incrementWOGWTotal(int amount) {
        ServerEvent.WELL_OF_GOODWILL_AMOUNT += amount;
    }
}
