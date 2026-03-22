package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Utils;

import java.util.HashMap;

/**
 * @author PVE
 * @Since augustus 19, 2020
 */
public class DebugNpcsCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        HashMap<String, Integer> counts = new HashMap<>();
        World.getWorld().getNpcs().forEach(npc -> {
            Integer count = counts.getOrDefault(npc.spawnStack, 0);
            counts.put(npc.spawnStack, (count + 1));
        });
        Utils.sortByValue(counts, true);

        System.out.println("NPC STACKS:");
        counts.forEach((stack, count) -> System.out.println("    " + stack + ": " + count));
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
