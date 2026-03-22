package com.twisted.game.world.entity.combat.method.impl.npcs.pestcontrol;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 05, 2021, 13:40
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Torcher extends CommonCombatMethod {

    private void magic(Npc npc, Mob target) {
        npc.animate(npc.attackAnimation());
        new Projectile(npc, target, 647, 50, 80, 50, 30, 0).sendProjectile();
        Chain.bound(target).name("TorcherMagicTask").runFn(2, () -> target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MAGIC), CombatType.MAGIC).checkAccuracy().submit());
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        Npc npc = (Npc) mob;
        magic(npc, target);

        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }
}
