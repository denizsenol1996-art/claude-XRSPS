package com.twisted.game.world.entity.combat.method.impl.npcs.godwars.bandos;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.AggressionCheck;

import java.util.Arrays;
import java.util.List;

/**
 * @author Patrick van Elderen | April, 29, 2021, 14:16
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class BandosAgro implements AggressionCheck {

    private final List<Integer> BANDOS_PROTECTION_EQUIPMENT = Arrays.asList(11061, 11804, 11832, 11834,
        11836, 12265, 12267, 12269, 12271, 12273, 12275, 12480, 12482, 12484,
        12486, 12488, 12498, 12500, 12502, 12504, 12608, 19924);

    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        for(int armour : BANDOS_PROTECTION_EQUIPMENT) {
            if(mob.isPlayer()) {
                if(mob.getAsPlayer().getEquipment().contains(armour))
                    return false;
            }
        }
        return true;
    }
}
