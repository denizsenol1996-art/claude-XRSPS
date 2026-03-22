package com.twisted.game.content.syntax.impl;

import com.twisted.game.content.clan.ClanManager;
import com.twisted.game.content.syntax.EnterSyntax;
import com.twisted.game.world.entity.mob.player.Player;

public class JoinClanChat implements EnterSyntax {

    @Override
    public void handleSyntax(Player player, String input) {
        ClanManager.join(player, input);
    }

    @Override
    public void handleSyntax(Player player, long input) {

    }
}
