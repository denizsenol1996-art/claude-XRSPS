package com.twisted.game.content.newplateau;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.AggressionCheck;

public class KbdAgro implements AggressionCheck {
    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        if(mob.tile().region() == 11563){
            return false;
        }
        return true;
    }
}
