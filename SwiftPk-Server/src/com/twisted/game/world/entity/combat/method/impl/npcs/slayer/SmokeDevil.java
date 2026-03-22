package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;

public class SmokeDevil extends CommonCombatMethod {

    private boolean smokeAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        Player player = (Player) target;
        new Projectile(mob, target, 643, mob.projectileSpeed(target), 15, 65, 31, 0, 0, 0).sendProjectile();
        int delay = mob.getProjectileHitDelay(target);
        if (player.getEquipment().getId(EquipSlot.HEAD) != 4164 && !player.getEquipment().wearingSlayerHelm()) {
            target.hit(mob, 18, delay, CombatType.MAGIC).submit();
            player.message("<col=ff0000>The devil's smoke blinds and damages you!");
            player.message("<col=ff0000>A facemask can protect you from this attack.");
            return true;
        }
        return false;
    }

    private void magicAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());
        new Projectile(mob, target, 643, mob.projectileSpeed(target), 15, 65, 31, 0, 0, 0).sendProjectile();
        int delay = mob.getProjectileHitDelay(target);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().postDamage(this::handleAfterHit).submit();
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (smokeAttack(mob, target))
            return true;
        magicAttack(mob, target);
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }

    public void handleAfterHit(Hit hit) {
        hit.getTarget().graphic(643);
    }
}
