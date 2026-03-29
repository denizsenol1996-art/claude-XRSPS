package com.twisted.game.world.entity.mob.player.commands.impl.dev;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
public class ObjectCommand implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        int id = Integer.parseInt(parts[1]);
        int type = parts.length > 2 ? Integer.parseInt(parts[2]) : 10;
        int rotation = parts.length > 3 ? Integer.parseInt(parts[3]) : 0;
        ObjectManager.addObj(new GameObject(id, player.tile().copy(), type, rotation));
    }
    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }
}