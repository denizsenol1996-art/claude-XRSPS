package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.entity.mob.updating.Tinting;

public class TintingCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        byte hue = 0;
        byte sat = 6;
        byte lum = 28;
        byte opac = 112;

        Tinting tinting = new Tinting((short) 0, (short) 0, hue, sat, lum, opac);
        player.setTinting(tinting, player);
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}

