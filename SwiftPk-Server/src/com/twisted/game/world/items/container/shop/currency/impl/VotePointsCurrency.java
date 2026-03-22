package com.twisted.game.world.items.container.shop.currency.impl;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.QuestTab;
import com.twisted.game.world.items.container.shop.currency.Currency;

public final class VotePointsCurrency implements Currency {

    @Override
    public boolean tangible() {
        return false;
    }

    @Override
    public boolean takeCurrency(Player player, int amount) {
        int votePoints = player.getAttribOr(AttributeKey.VOTE_POINS, 0);
        if (votePoints >= amount) {
            player.putAttrib(AttributeKey.VOTE_POINS, votePoints - amount);
            player.getPacketSender().sendString(QuestTab.InfoTab.VOTE_POINTS.childId, QuestTab.InfoTab.INFO_TAB.get(QuestTab.InfoTab.VOTE_POINTS.childId).fetchLineData(player));
            return true;
        } else {
            player.message("You do not have enough vote points.");
            return false;
        }
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        //Empty can't receive currency from shops
    }

    @Override
    public int currencyAmount(Player player, int cost) {
        return player.getAttribOr(AttributeKey.VOTE_POINS, 0);
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return false;
    }

    @Override
    public String toString() {
        return "Vote points";
    }
}
