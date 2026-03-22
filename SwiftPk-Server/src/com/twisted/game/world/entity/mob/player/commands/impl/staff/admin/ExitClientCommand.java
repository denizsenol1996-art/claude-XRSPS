package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Utils;

import java.util.Optional;

public class ExitClientCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        final String player2 = Utils.formatText(command.substring(parts[0].length() + 1));
        Optional<Player> plr = World.getWorld().getPlayerByName(player2);
        if (plr.isEmpty()) {
            player.message("Player " + player2 + " is not online.");
            return;
        }
        if (CombatFactory.inCombat(plr.get())) {
            player.message("Player " + player2 + " is in combat!");
            return;
        }
        plr.get().getPacketSender().sendExit();
        player.message("Closed " + player2 + "'s client.");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }

}
