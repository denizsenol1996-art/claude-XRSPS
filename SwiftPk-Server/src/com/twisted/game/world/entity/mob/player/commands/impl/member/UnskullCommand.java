package com.twisted.game.world.entity.mob.player.commands.impl.member;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.areas.impl.WildernessArea;

public class UnskullCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        boolean member = player.getMemberRights().isRubyOrGreater(player);
        if(!member && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You need to be at least a Ruby member to use this command.");
            return;
        }
        if (CombatFactory.inCombat(player) || WildernessArea.inWilderness(player.tile())) {
            player.message("You can't do that right now.");
            return;
        }
        player.message("You have used the unskull command.");
        Skulling.unskull(player);
        player.clearAttrib(AttributeKey.PVP_WILDY_AGGRESSION_TRACKER);
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
