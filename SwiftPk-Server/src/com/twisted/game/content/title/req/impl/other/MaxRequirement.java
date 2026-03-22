package com.twisted.game.content.title.req.impl.other;

import com.twisted.game.content.items.equipment.max_cape.MaxCape;
import com.twisted.game.content.title.req.TitleRequirement;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * Created by Kaleem on 25/03/2018.
 */
public class MaxRequirement extends TitleRequirement {

    public MaxRequirement() {
        super("Reach level 99 in all stats<br>");
    }

    @Override
    public boolean satisfies(Player player) {
        return MaxCape.hasTotalLevel(player);
    }

}
