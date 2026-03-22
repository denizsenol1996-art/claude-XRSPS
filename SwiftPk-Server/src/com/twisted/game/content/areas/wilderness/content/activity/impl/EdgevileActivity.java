package com.twisted.game.content.areas.wilderness.content.activity.impl;

import com.twisted.game.content.areas.wilderness.content.activity.WildernessActivity;
import com.twisted.game.content.areas.wilderness.content.activity.WildernessLocations;
import com.twisted.game.world.entity.mob.player.Player;

import java.util.concurrent.TimeUnit;

/**
 * @author Zerikoth
 * @Since september 24, 2020
 */
public class EdgevileActivity extends WildernessActivity {

    private WildernessLocations wildernessLocation;

    @Override
    public String description() {
        return "Edgevile Pking";
    }

    @Override
    public String announcement() {
        return "Edgevile Pking is the new wilderness activity for one hour!";
    }

    @Override
    public void onCreate() {
        wildernessLocation = WildernessLocations.EDGEVILLE;
    }

    @Override
    public void process() {
    }

    @Override
    public void onFinish() {
        wildernessLocation = null;
    }

    @Override
    public long activityTime() {
        return TimeUnit.MINUTES.toMillis(60);
    }

    @Override
    public boolean canReward(Player player) {
        return wildernessLocation.isInArea(player);
    }
}
