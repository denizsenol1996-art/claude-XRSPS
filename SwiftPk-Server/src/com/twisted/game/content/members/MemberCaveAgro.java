package com.twisted.game.content.members;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.AggressionCheck;

public class MemberCaveAgro implements AggressionCheck {

    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        if(mob.tile().region() == 11563){//dzone3
            return false;
        }
        return !mob.tile().memberCave();
    }
}
