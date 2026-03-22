package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.GameConstants;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.IronMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.entity.mob.player.rights.PlayerRights;
import com.twisted.game.world.items.Item;

import java.util.concurrent.TimeUnit;

import static com.twisted.util.ItemIdentifiers.BLOOD_MONEY;

public class YoutuberCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(player.ironMode() != IronMode.NONE) {
            return;
        }

        if (!player.getPlayerRights().isYoutuber(player)) {
            return;
        }

        long current = System.currentTimeMillis();
        long lastRedeem = player.getAttribOr(AttributeKey.YOUTUBER_BM_CLAIM, 0L);
        long days = TimeUnit.MILLISECONDS.toDays(current - lastRedeem);
        if (lastRedeem > 0 && days < 7) {
            player.message("<col=ca0d0d>You've already claimed this week's rewards. You may redeem again in another "+ (7 - days) +" days.");
            return;
        }

        int bmCount = player.getPlayerRights().equals(PlayerRights.GOLD_YOUTUBER) ? 1_000_000 : player.getPlayerRights().equals(PlayerRights.SILVER_YOUTUBER) ? 500_000 : 250_000;
        if (player.inventory().getFreeSlots() < 1) {
            player.message("<col=ca0d0d>Due to insufficient space in your inventory your BM reward has been placed in your bank.");
            player.getBank().depositFromNothing(new Item(BLOOD_MONEY, bmCount));
        } else {
            player.message("<col=ca0d0d>You've successfully claimed your BM reward. Thanks for playing "+ GameConstants.SERVER_NAME+"!");
            player.inventory().add(new Item(BLOOD_MONEY, bmCount));
        }

        player.putAttrib(AttributeKey.YOUTUBER_BM_CLAIM, current);
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
