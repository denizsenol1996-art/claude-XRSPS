package com.twisted.game.world.entity.combat.method.impl.specials.range;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;

import static com.twisted.util.ItemIdentifiers.DRAGON_ARROW;

public class DarkBow extends CommonCombatMethod {

    private int endgfx;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        final Player player = mob.getAsPlayer();

        player.animate(426);

        Item ammo = player.getEquipment().get(EquipSlot.AMMO);

        var gfx = 1101;
        var gfx2 = 1102; //non drag arrow 2nd arrow has another graphic id
        endgfx = 1103; // small puff
        var min = 5;

        if (ammo != null && ammo.getId() == DRAGON_ARROW) {
            // dragon arrows
            gfx = 1099; // dragon spec
            gfx2 = 1099; // drag again
            endgfx = 1100; // large puff
            min = 8;
        }

        var dist = player.tile().distance(target.tile());
        var speed1 = 16 + (dist * 8);
        var speed2 = 25 + (dist * 10);
        var delay = (int) Math.round(Math.floor(32 / 30.0) + ((double)dist * (5 * 0.020) / 0.6));

        // Send 2 arrow projectiles
        new Projectile(player, target, gfx, 40, speed1, 40, 36, 0).sendProjectile();
        new Projectile(mob, target, gfx2, 40, speed2, 40, 36, 0).sendProjectile();

        // Decrement 2 arrows from ammunition
        CombatFactory.decrementAmmo(player);

        // Note: Dark bow first hit does have PID applied, but the delay varies (not always delay-1) depending on dist. It's custom.
        Hit hit1 = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy();

        // Minimum damages depending on arrow type
        if (hit1.getDamage() < min) {
            hit1.setDamage(min);
        }
        hit1.postDamage(this::handleAfterHit).submit();

        // Extra delay which the second arrow has
        var extraDelay = 2;
        if (dist <= 5)
            extraDelay -= 1;

        // The second hit is pid adjusted.
        Hit hit2 = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),extraDelay + delay, CombatType.RANGED).checkAccuracy();
        if (hit2.getDamage() < min) {
            hit2.setDamage(min);
        }
        hit2.postDamage(this::handleAfterHit).submit();

        CombatSpecial.drain(mob, CombatSpecial.DARK_BOW.getDrainAmount());
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed() + 1;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return CombatFactory.RANGED_COMBAT.getAttackDistance(mob);
    }

    public void handleAfterHit(Hit hit) {
        hit.getTarget().performGraphic(new Graphic(endgfx, 100));
    }
}
