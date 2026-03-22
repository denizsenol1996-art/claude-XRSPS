package com.twisted.game.world.entity.combat.method.impl.npcs.godwars;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.armadyl.KreeArra;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.bandos.Graardor;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.saradomin.Zilyana;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.zamorak.Kril;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.NpcDeath;
import com.twisted.game.world.position.Area;

import java.util.*;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date februari 15, 2020 13:54
 */
public class GwdLogic {

    private static final List<Integer> gwdBoss = Arrays.asList(2215, 3162, 2205, 3129);

    // Any type of gwd boss - just in the graaardor file srry xxx
    public static boolean isBoss(int npc) {
        return gwdBoss.contains(npc);
    }

    public static void onRespawn(Npc boss) {
        List<Npc> minionList = boss.getAttribOr(AttributeKey.MINION_LIST, null);
        if (minionList == null) return;

        minionList.forEach(NpcDeath::respawn);
    }

    public static void onServerStart() {
        Optional<Npc> boss = Optional.empty();
        Area[] areas =  {KreeArra.getENCAMPMENT(), Zilyana.getENCAMPMENT(), Kril.getENCAMPMENT(), Graardor.getBandosArea()};

        for (Area a : areas) {
            // Identify the boss
            for(Npc n : World.getWorld().getNpcs()) {
                if (n == null)
                    continue;
                if(n.tile().inArea(a)) {
                    if (GwdLogic.isBoss(n.id())) {// Located boss.
                        boss = Optional.of(n);
                    }
                }
            }
            if (boss.isPresent()) {
                // Now identify minions.
                Npc boss1 = boss.get();
                ArrayList<Npc> minionList = new ArrayList<>();
                for(Npc n : World.getWorld().getNpcs()) {
                    if (n == null)
                        continue;
                    if (n.tile().inArea(a) && !GwdLogic.isBoss(n.id())) {
                        minionList.add(n);
                    }
                }

                if (!minionList.isEmpty())
                    boss1.putAttrib(AttributeKey.MINION_LIST, minionList);
            } else {
                System.out.println("boss missing from gwd... wat");
            }
            boss = Optional.empty();
        }
    }
}
