package com.twisted.game.world.entity.combat.method.impl.specials.range;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;

import static com.twisted.util.ItemIdentifiers.TOXIC_BLOWPIPE;

public class ToxicBlowpipeSpecialAttack extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        final Player player = mob.getAsPlayer();

        player.animate(player.getEquipment().hasAt(EquipSlot.WEAPON, TOXIC_BLOWPIPE) ? 5061 : 11901);

        // Send projectiles
        new Projectile(player, target, 1043, 32, 65, 35, 36, 0).sendProjectile();

        CombatFactory.decrementAmmo(player);

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),2, CombatType.RANGED).checkAccuracy();
        hit.submit();

        if (hit.getDamage() > 0) {
            player.heal(hit.getDamage() / 2);
            boolean venom = Venom.attempt(player, target, CombatType.RANGED, true);
            if (venom)
                target.venom(player);
        }
        CombatSpecial.drain(mob, CombatSpecial.TOXIC_BLOWPIPE.getDrainAmount());
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        int delay = mob.getBaseAttackSpeed();
        if (mob.isNpc())
            return delay - 1;
        return delay;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return CombatFactory.RANGED_COMBAT.getAttackDistance(mob);
    }
}
