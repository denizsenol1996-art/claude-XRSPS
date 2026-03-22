package com.twisted.game.content.raids;

import com.twisted.game.content.instance.InstancedArea;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;

public interface RaidBuilder {
    void build(Player player, InstancedArea instance);
    int scale(Npc npc, Player player, boolean hardMode);

}
