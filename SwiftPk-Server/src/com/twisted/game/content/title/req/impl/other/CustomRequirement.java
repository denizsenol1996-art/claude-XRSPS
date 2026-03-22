package com.twisted.game.content.title.req.impl.other;

import com.twisted.game.content.title.req.TitleRequirement;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * @author Patrick van Elderen | January, 20, 2021, 13:04
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class CustomRequirement extends TitleRequirement {

    public CustomRequirement() {
        super("Must be at least an<br>Emerald member");
    }

    @Override
    public boolean satisfies(Player player) {
        return player.getMemberRights().isEmeraldOrGreater(player);
    }

}
