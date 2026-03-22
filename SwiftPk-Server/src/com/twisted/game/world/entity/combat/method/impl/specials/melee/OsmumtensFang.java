package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;

public class OsmumtensFang extends CommonCombatMethod {
    public static boolean first = false;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        //first = false;
     /*   final Player player = (Player) mob;
        double multiper = player.getCombatSpecial().getSpecialMultiplier();*/
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy();
       // int maxHit = mob.getCombat().maximumMeleeHit();
        mob.animate(1378);
        mob.graphic(2128, 20, 0);
        hit.setDamage(World.getWorld().random(9, 60));
        hit.submit();
       /* mob.animate(1378);
        mob.graphic(2128, 130, 0);
        hit.submit();
        if (hit.getDamage() == maxHit) {
            target.graphic(310);
            mob.animate(1378);
            hit.setDamage(World.getWorld().random(9,60));
            //  first = false;
        }*/
        CombatSpecial.drain(mob, CombatSpecial.OSMUMTENS_FANG.getDrainAmount());
        return true;
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
