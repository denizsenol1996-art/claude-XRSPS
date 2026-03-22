package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.GameConstants;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class VoteCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL(GameConstants.VOTE_URL);
        player.message("Opening " + GameConstants.VOTE_URL + " in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
