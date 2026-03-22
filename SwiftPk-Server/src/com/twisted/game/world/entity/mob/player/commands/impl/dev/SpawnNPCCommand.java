package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

public class SpawnNPCCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        boolean massSpawn = false;
        int amt = parts.length > 3 ? Integer.parseInt(parts[3]) : 1;
        for (int i = 0; i < amt; i++) {
            Npc npc = Npc.of(Integer.parseInt(parts[1]), player.tile()).respawns(false);
            World.getWorld().registerNpc(npc);
            if (parts.length > 2) {
                npc.setHitpoints(Integer.parseInt(parts[2]));
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
