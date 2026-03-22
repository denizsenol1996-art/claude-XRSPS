package com.twisted.game.content.title.req.impl.other;

import com.twisted.game.content.title.req.TitleRequirement;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * Created by Kaleem on 25/03/2018.
 */
public class AllAchievementsRequirement extends TitleRequirement {

    public AllAchievementsRequirement() {
        super("Complete all <br>achievements");
    }

    @Override
    public boolean satisfies(Player player) {
        return player.completedAllAchievements();
    }
}
