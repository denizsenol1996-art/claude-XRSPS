package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.areas.impl.WildernessArea;

public class POScommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.message(player.tile().toString());
        player.message("Region = "+player.tile().region());
        System.out.println(player.tile().toString());
        player.message(player.tile().toString()+" region: "+player.tile().region()+". wild: "+ WildernessArea.inWilderness(player.tile())+". Chunk: "+player.tile().chunk());
    }

    @Override
    public boolean canUse(Player player) {

        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
