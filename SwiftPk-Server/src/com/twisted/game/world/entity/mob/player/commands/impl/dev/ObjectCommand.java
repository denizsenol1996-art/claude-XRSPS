package com.twisted.game.world.entity.mob.player.commands.impl.dev;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
public class ObjectCommand implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        try {
            int id = Integer.parseInt(parts[1]);
            int type = parts.length > 2 ? Integer.parseInt(parts[2]) : 10;
            int rotation = parts.length > 3 ? Integer.parseInt(parts[3]) : 0;
            player.message("Spawning object " + id + " type=" + type + " rot=" + rotation);
            GameObject obj = new GameObject(id, player.tile().copy(), type, rotation);
            player.message("Object created, adding...");
            ObjectManager.addObj(obj);
            player.message("Object " + id + " spawned!");
        } catch (Exception e) {
            player.message("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }
}
