package com.twisted.game.world.entity.combat.method.impl.specials.melee;


import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.updating.Tinting;
import com.twisted.util.chainedwork.Chain;

public class AncientGodsword extends CommonCombatMethod {
    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        short delay = 0;
        short duration = 240;
        byte hue = 0;
        byte sat = 6;
        byte lum = 28;
        byte opac = 108;
        final Player player = (Player) mob;
        player.animate(9171);
        player.graphic(1996);
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        if (hit.isAccurate()) {
            if (target instanceof Player) {
                target.getAsPlayer().setTinting(new Tinting(delay, duration, hue, sat, lum, opac), (Player) target);
            }
            Chain.bound(null).runFn(8, () -> {
                if (mob.tile().isWithinDistance(target.tile(), 5)) {
                    target.hit(mob, 25, 1);
                    target.graphic(2003);
                    mob.message("You heal from the ancient godswords special.");
                    mob.heal(25);
                    if (target.isPlayer()) {
                        target.message("You took damage");
                    }

                } else {
                    if (target.isPlayer()) {
                        target.message("You took no damage");
                    }
                }
            });
        }

        CombatSpecial.drain(mob, CombatSpecial.ANCIENT_GODSWORD.getDrainAmount());
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
