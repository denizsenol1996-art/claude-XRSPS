package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.content.tradingpost.TradingPost;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

/**
 * @author Patrick van Elderen | May, 12, 2021, 14:04
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class SaveAllTPCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        TradingPost.save();
        player.requestLogout();
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }
}
