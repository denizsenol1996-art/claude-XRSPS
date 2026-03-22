package com.twisted.game.world.entity.combat.method.impl.specials.range;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.weapon.FightStyle;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.Player;

/**
 * The dragon knife has a special attack, Duality, which consumes 25% of the player's special attack energy.
 * It causes the player to throw two dragon knives at once, with each knife having their own accuracy and damage rolls.
 * This special attack is similar to dragon dagger's, albeit without an extra increase in accuracy and damage.
 */
public class DragonKnife extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        final Player player = mob.getAsPlayer();

        boolean poisonKnive = player.getEquipment().containsAny(22806, 22808, 22810);
        player.animate(poisonKnive ? 8292 : 8291);

        // Get proper projectile id
        int projectileId = poisonKnive ? 1629 : 699;

        // Send projectiles
        new Projectile(player, target, projectileId, 40, 70, 43, 31, 0).sendProjectile();

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),0, CombatType.RANGED).checkAccuracy();
        hit.submit();

        Hit hi2 = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), target.isNpc() ? 1 : 2, CombatType.RANGED).checkAccuracy();
        hi2.submit();
        CombatSpecial.drain(mob, CombatSpecial.DRAGON_KNIFE.getDrainAmount());
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return mob.getCombat().getFightType().getStyle().equals(FightStyle.DEFENSIVE) ? 6 : 4;
    }
}
