package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class SkullCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(player.getSkullType() == SkullType.RED_SKULL) {
            return;
        }

        if (parts[0].startsWith("red")) {
            Skulling.assignSkullState(player, SkullType.RED_SKULL);
        } else {
            Skulling.assignSkullState(player, SkullType.WHITE_SKULL);
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
