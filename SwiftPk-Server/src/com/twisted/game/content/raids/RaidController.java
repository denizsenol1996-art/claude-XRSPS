package com.twisted.game.content.raids;

import com.twisted.game.content.instance.InstancedArea;
import com.twisted.game.world.entity.mob.player.Player;

import java.util.List;

public class RaidController {

    private final List<RaidBuilder> npcs;

    public RaidController(List<RaidBuilder> npcs) {
        super();
        this.npcs = npcs;
    }

    public void build(Player player, InstancedArea instancedArea) {
        for (RaidBuilder r : npcs) {
            r.build(player, instancedArea);
        }
    }

}
