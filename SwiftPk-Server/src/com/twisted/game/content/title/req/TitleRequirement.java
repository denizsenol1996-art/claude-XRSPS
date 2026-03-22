package com.twisted.game.content.title.req;

import com.twisted.game.world.entity.mob.player.Player;

/**
 * Created by Kaleem on 25/03/2018.
 */
public abstract class TitleRequirement {

    private final String requirementName;

    public TitleRequirement(String requirementName) {
        this.requirementName = requirementName;
    }

    public abstract boolean satisfies(Player player);

    public String getRequirementName() {
        return requirementName;
    }
}
