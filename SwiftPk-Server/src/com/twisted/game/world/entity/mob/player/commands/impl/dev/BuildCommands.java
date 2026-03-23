package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.MapObjects;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import java.util.List;

public class BuildCommands implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        String sub = parts[0].toLowerCase();
        int px = player.tile().getX(), py = player.tile().getY(), pz = player.tile().getZ();
        switch (sub) {
            case "floor" -> {
                int id = Integer.parseInt(parts[1]);
                ObjectManager.addObj(new GameObject(id, player.tile().copy(), 22, 0));
                player.message("Floor " + id + " placed.");
            }
            case "fill" -> {
                int id = Integer.parseInt(parts[1]);
                int r = parts.length > 2 ? Integer.parseInt(parts[2]) : 3;
                int c = 0;
                for (int x = px-r; x <= px+r; x++)
                    for (int y = py-r; y <= py+r; y++) {
                        ObjectManager.addObj(new GameObject(id, new Tile(x,y,pz), 22, 0)); c++;
                    }
                player.message("Filled " + c + " tiles.");
            }
            case "fillobject" -> {
                int id = Integer.parseInt(parts[1]);
                int r = parts.length > 2 ? Integer.parseInt(parts[2]) : 3;
                int gap = parts.length > 3 ? Integer.parseInt(parts[3]) : 2;
                int c = 0;
                for (int x = px-r; x <= px+r; x+=gap)
                    for (int y = py-r; y <= py+r; y+=gap) {
                        ObjectManager.addObj(new GameObject(id, new Tile(x,y,pz), 10, 0)); c++;
                    }
                player.message("Placed " + c + " objects.");
            }
            case "cleararea" -> {
                int r = parts.length > 1 ? Integer.parseInt(parts[1]) : 5;
                int c = 0;
                for (int x = px-r; x <= px+r; x++)
                    for (int y = py-r; y <= py+r; y++) {
                        long hash = MapObjects.getHash(x, y, pz);
                        List<GameObject> objs = MapObjects.mapObjects.get(hash);
                        if (objs != null) {
                            for (GameObject g : objs.toArray(new GameObject[0])) {
                                g.remove();
                                c++;
                            }
                        }
                    }
                player.message("Cleared " + c + " objects.");
            }
            case "wallline" -> {
                int id = Integer.parseInt(parts[1]);
                int len = parts.length > 2 ? Integer.parseInt(parts[2]) : 5;
                String dir = parts.length > 3 ? parts[3] : "e";
                int dx=0,dy=0;
                switch(dir){case "n"->dy=1;case "s"->dy=-1;case "e"->dx=1;case "w"->dx=-1;}
                for (int i=0;i<len;i++)
                    ObjectManager.addObj(new GameObject(id, new Tile(px+dx*i,py+dy*i,pz), 10, 0));
                player.message("Wall line: " + len + " " + dir);
            }
            case "objtype" -> {
                int id = Integer.parseInt(parts[1]);
                int type = parts.length > 2 ? Integer.parseInt(parts[2]) : 10;
                int face = parts.length > 3 ? Integer.parseInt(parts[3]) : 0;
                ObjectManager.addObj(new GameObject(id, player.tile().copy(), type, face));
                player.message("Placed type=" + type + " face=" + face);
            }
            default -> {
                player.message("::floor [id] | ::fill [id] [radius] | ::cleararea [radius]");
                player.message("::fillobject [id] [r] [gap] | ::wallline [id] [len] [n/s/e/w]");
                player.message("::objtype [id] [type] [face]");
            }
        }
    }
    @Override public boolean canUse(Player player) { return player.getPlayerRights().isDeveloperOrGreater(player); }
}
