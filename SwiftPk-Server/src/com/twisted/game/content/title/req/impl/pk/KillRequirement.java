package com.twisted.game.content.title.req.impl.pk;

import com.twisted.game.content.title.req.TitleRequirement;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * Created by Kaleem on 25/03/2018.
 */
public class KillRequirement extends TitleRequirement {

    private final int kills;

    public KillRequirement(int kills) {
        super("Get " + kills + " kills");
        this.kills = kills;
    }

    @Override
    public boolean satisfies(Player player) {
        int kills = player.getAttribOr(AttributeKey.PLAYER_KILLS, 0);
        return kills >= this.kills;
    }
}
