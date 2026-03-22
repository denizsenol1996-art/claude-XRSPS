package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.chainedwork.Chain;

public class SoulReaperAxe extends CommonCombatMethod {
    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        final Player player = (Player) mob;
        final boolean hasBleed = target.<Boolean>getAttribOr(AttributeKey.BLEED, false);
        if (hasBleed) {
            player.message("Your opponent can only be affected by one bleed at a time.");
            return true;
        }
        player.animate(10173);
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        hit.postDamage(_ -> {
            final int[] tick = {0};
            target.putAttrib(AttributeKey.BLEED, true);
            Chain.noCtxRepeat().repeatingTask(2, bleed -> {
                if (tick[0] >= 5 || target.dead() || !target.isRegistered() || player.dead() || !player.isRegistered() || target.tile().distance(player.tile()) > 14) {
                    target.clearAttrib(AttributeKey.BLEED);
                    bleed.stop();
                    return;
                }
                target.hit(mob, World.getWorld().random(1, 10));
                tick[0]++;
            });
        });
        CombatSpecial.drain(mob, CombatSpecial.SOUL_REAPER_AXE.getDrainAmount());
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
