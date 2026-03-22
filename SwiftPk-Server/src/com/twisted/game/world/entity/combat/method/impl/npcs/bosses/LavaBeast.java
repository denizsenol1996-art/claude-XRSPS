package com.twisted.game.world.entity.combat.method.impl.npcs.bosses;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;

import java.security.SecureRandom;

public class LavaBeast extends CommonCombatMethod {

        @Override
    public boolean prepareAttack(Mob mob, Mob target) {
            int roll = new SecureRandom().nextInt(5);

            //10% chance that the boss skulls you!
            if (World.getWorld().rollDie(10, 1)) {
                Skulling.assignSkullState(((Player) target), SkullType.RED_SKULL);
                target.message("The "+mob.getMobName()+" has redskulled you, be careful!");
            }

            Npc npc = (Npc) mob;

        mob.animate(7678);
        new Projectile(mob, target, 1403, 60, 20, 30, 30, 0,10,14).sendProjectile();
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 1, CombatType.MAGIC).checkAccuracy().submit();
            return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 15;
    }
}
