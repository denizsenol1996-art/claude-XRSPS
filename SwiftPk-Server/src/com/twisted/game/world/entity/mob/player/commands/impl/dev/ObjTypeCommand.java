package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.object.GameObject;

public class ObjTypeCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        int type = Integer.parseInt(parts[1]);
        player.getPacketSender().sendObject(new GameObject(Integer.parseInt(parts[1]), player.tile().copy(), type));
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
