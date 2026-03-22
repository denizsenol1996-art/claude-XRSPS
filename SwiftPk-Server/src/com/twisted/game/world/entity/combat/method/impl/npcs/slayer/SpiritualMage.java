package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;

import static com.twisted.util.NpcIdentifiers.*;

/**
 * @author Patrick van Elderen | January, 08, 2021, 08:48
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class SpiritualMage extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        // Attack the player
        mob.animate(mob.attackAnimation());
        int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC);
        target.hit(mob, hit, CombatType.MAGIC).checkAccuracy().submit();

        // Does NOT splash when miss!
        if(target instanceof Player) {
            Player playerTarget = (Player) target;
            Npc npc = (Npc) mob;
            if (hit > 0) {
                playerTarget.performGraphic(get_graphic(npc.id())); // Cannot protect from this.
            } else {
                playerTarget.performGraphic(new Graphic(85,90,0)); // Cannot protect from this.
            }
        }
        return true;
    }

    private Graphic get_graphic(int npc) {
        return switch (npc) {
            case SPIRITUAL_MAGE_3161 -> new Graphic(78, 0, 0);
            case BATTLE_MAGE -> new Graphic(78, 0, 0);
            case SPIRITUAL_MAGE -> new Graphic(76, 128, 0);
            case SARADOMIN_PRIEST -> new Graphic(76, 128, 0);
            case BATTLE_MAGE_1611 -> new Graphic(76, 128, 0);
            default -> new Graphic(77, 96, 0);
        };
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 7;
    }
}
