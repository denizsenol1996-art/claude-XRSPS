package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness.vetion;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.util.Utils;

import java.util.List;

/**
 * @author Patrick van Elderen | Zerikoth | PVE
 * @date maart 19, 2020 16:52
 */
public class VetionMinion extends Npc {

    public VetionMinion(Npc vetion, Mob target) {
        super(6613, vetion.tile());
        this.putAttrib(AttributeKey.BOSS_OWNER, vetion);
        this.setTile(vetion.tile().transform(Utils.random(3), -1));
        this.walkRadius(8);
        this.respawns(false);
        this.getCombat().attack(target);
    }

    public static void death(Npc npc) {
        Npc vetion = npc.getAttribOr(AttributeKey.BOSS_OWNER, null);
        if (vetion != null) {
            //Check for any minions.
            List<Npc> minList = vetion.getAttribOr(AttributeKey.MINION_LIST, null);
            if (minList != null) {
                minList.remove(npc);
                // All minions dead? Enable damage on vetion again
                if (minList.size() == 0) {
                    vetion.putAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED, false);
                }
            }
        }
    }
}
