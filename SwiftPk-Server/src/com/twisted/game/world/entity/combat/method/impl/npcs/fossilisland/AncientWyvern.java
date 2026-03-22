package com.twisted.game.world.entity.combat.method.impl.npcs.fossilisland;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.util.Utils;

public class AncientWyvern extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob wyvern, Mob target) {
        int roll = Utils.random(2);
        if(roll == 1) {
            doMagic(wyvern, target);
        } else {
            if (CombatFactory.canReach(wyvern, CombatFactory.MELEE_COMBAT, target)) {
                if (Utils.random(2) == 1) {
                    doMelee(wyvern, target);
                } else {
                    doTailWhip(wyvern, target);
                }
            } else {
                doMagic(wyvern, target);
            }
        }
        return true;
    }

    private void doMagic(Mob wyvern, Mob target) {
        wyvern.animate(7657);

        new Projectile(wyvern, target, 136, 25, 55, 90, 45, 0).sendProjectile();
        target.delayedGraphics(new Graphic(137, 80), 2);
        target.hit(wyvern, Utils.random(25), 2, CombatType.MAGIC).checkAccuracy().submit();
    }

    private void doTailWhip(Mob wyvern, Mob target) {
        wyvern.animate(wyvern.attackAnimation());
        target.hit(wyvern, Utils.random(wyvern.getAsNpc().combatInfo().maxhit), 1, CombatType.MELEE).checkAccuracy().submit();
    }

    private void doMelee(Mob wyvern, Mob target) {
        wyvern.animate(7658);
        target.hit(wyvern, Utils.random(wyvern.getAsNpc().combatInfo().maxhit), 1, CombatType.MELEE).checkAccuracy().submit();
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
