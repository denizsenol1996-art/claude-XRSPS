package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class PNPCCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        int id = Integer.parseInt(parts[1]);
        if(id == -1) {
            player.looks().transmog(-1);
            player.looks().resetRender();
            player.message("You return to your human-like state.");
            return;
        }
        player.looks().transmog(id);
        //player.looks().renderData(World.getWorld().definitions().get(NpcDefinition.class, id).renderpairs());
        player.message("You transmog into the "+World.getWorld().definitions().get(NpcDefinition.class, id).name+ ".");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isAdminOrGreater(player));
    }

}
