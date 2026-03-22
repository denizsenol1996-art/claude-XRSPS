package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.content.syntax.impl.ChangeOtherPasswordSyntax;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Utils;

import java.util.Optional;

public class ChangeOtherPasswordCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {

        // Known exploit
        if (command.contains("\r") || command.contains("\n")) {
            return;
        }
        if (parts.length < 2) {
            player.message("Change password other usage: ::changepasswordother username");
            return;
        }
        String p2name = command.substring(parts[0].length() + 1);
        String player2 = Utils.formatText(p2name);
        Optional<Player> plr = World.getWorld().getPlayerByName(player2);
        player.setEnterSyntax(new ChangeOtherPasswordSyntax(plr, player2));
        player.getPacketSender().sendEnterInputPrompt("Enter a new password for " + player2 + ":");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }

}
