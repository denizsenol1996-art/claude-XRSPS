package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Tile;
import com.twisted.util.NpcIdentifiers;

public class Scorpia extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            if (World.getWorld().rollDie(4, 1)) {
                target.poison(20);
            }

            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
            mob.animate(mob.attackAnimation());
        }
        return true;
    }

    private void summon_guardian(Npc scorpia) {
        var guardian = new Npc(NpcIdentifiers.SCORPIAS_GUARDIAN, new Tile(scorpia.tile().x + World.getWorld().random(2), scorpia.tile().y + World.getWorld().random(2)));
        guardian.respawns(false);
        guardian.noRetaliation(true);
        World.getWorld().registerNpc(guardian);
        guardian.setEntityInteraction(scorpia);

        // Execute script
        ScorpiaGuardian.heal(scorpia, guardian);
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }
}
