package com.twisted.game.world.items.container.shop.currency.impl;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.QuestTab;
import com.twisted.game.world.items.container.shop.currency.Currency;

import static com.twisted.game.world.entity.mob.player.QuestTab.InfoTab.SLAYER_POINTS;

public final class SlayerPointsCurrency implements Currency {

    @Override
    public boolean tangible() {
        return false;
    }

    @Override
    public boolean takeCurrency(Player player, int amount) {
        int slayerRewardPoints = player.getAttribOr(AttributeKey.SLAYER_REWARD_POINTS, 0);
        if (slayerRewardPoints >= amount) {
            player.putAttrib(AttributeKey.SLAYER_REWARD_POINTS, slayerRewardPoints - amount);
            player.getPacketSender().sendString(SLAYER_POINTS.childId, QuestTab.InfoTab.INFO_TAB.get(SLAYER_POINTS.childId).fetchLineData(player));
            return true;
        } else {
            player.message("You do not have enough slayer points.");
            return false;
        }
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        //Empty can't receive currency from shops
    }

    @Override
    public int currencyAmount(Player player, int cost) {
        return player.getAttribOr(AttributeKey.SLAYER_REWARD_POINTS, 0);
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return false;
    }
}
