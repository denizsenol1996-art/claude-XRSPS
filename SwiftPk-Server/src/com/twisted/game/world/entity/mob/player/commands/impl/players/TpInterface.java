package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.position.areas.impl.WildernessArea;

public class TpInterface implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {//only teleport interface will be for diamond +

        //home and shop using ::home and ::shops for everyone it getting it from client
        //or you want them too for diamond +?
        if(!player.getMemberRights().isDiamondOrGreater(player) && !WildernessArea.inWilderness(player.tile())) {
            player.message("You need to become a Diamond to use that benefits or get out of wild.");
            return;

        } else {
            player.setCurrentTabIndex(4);
            player.getInterfaceManager().open(29050);
        }



    }

    @Override
    public boolean canUse(Player player) {//that is ctrl t
        return true;//later do update on that makeso it can be run
    }
}
