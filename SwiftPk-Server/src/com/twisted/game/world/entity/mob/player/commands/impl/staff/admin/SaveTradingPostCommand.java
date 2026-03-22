package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.content.tradingpost.TradingPost;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class SaveTradingPostCommand implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {

        try {
            TradingPost.save();
            TradingPost.saveRecentSales();
            player.getPacketSender().sendMessage("Saving tradepost listings..");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
