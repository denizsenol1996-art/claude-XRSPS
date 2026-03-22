package com.twisted.game.world.entity.combat.method.impl.npcs.godwars.armadyl;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.AggressionCheck;

import java.util.Arrays;
import java.util.List;

/**
 * @author Patrick van Elderen | April, 29, 2021, 14:16
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ArmadylAgro implements AggressionCheck {

    private final List<Integer> ARMADYL_PROTECTION_EQUIPMENT = Arrays.asList(11785, 11802, 11826, 11828, 11830, 12253,
        12255, 12257, 12259, 12261, 12263, 12470, 12472, 12474, 12476, 12478, 12506, 12508,
        12510, 12512, 12610, 19930);

    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        for(int armour : ARMADYL_PROTECTION_EQUIPMENT) {
            if(mob.isPlayer()) {
                if(mob.getAsPlayer().getEquipment().contains(armour))
                    return false;
            }
        }
        return true;
    }
}
