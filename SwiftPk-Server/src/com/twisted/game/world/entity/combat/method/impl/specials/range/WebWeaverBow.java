package com.twisted.game.world.entity.combat.method.impl.specials.range;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.ranged.RangedData;
import com.twisted.game.world.entity.masks.animations.Animation;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import java.security.SecureRandom;

public class WebWeaverBow extends CommonCombatMethod {
    @Override
    public boolean prepareAttack(Mob entity, Mob target) {
        SecureRandom secureRandom = new SecureRandom();

        int delay = (int) (Math.floor(3 + entity.tile().getManHattanDist(entity.tile(), target.tile()) / 6D));

        RangedData.RangedWeapon rangeWeapon = entity.getCombat().getRangedWeapon();
        boolean ignoreArrows = rangeWeapon != null && rangeWeapon.ignoreArrowsSlot();

        double maxHit = entity.getCombat().maximumRangedHit(ignoreArrows);
        double hitLogic = (maxHit * (secureRandom.nextDouble() * 0.4));

        boolean chanceToPoison = Utils.securedRandomChance(0.35D);

        entity.animate(new Animation(9964));
        entity.performGraphic(new Graphic(2354, 100, 0));

        if (chanceToPoison) {
            target.poison(4);
        }

        Hit hit = target.hit(entity, (int) hitLogic,  delay, CombatType.RANGED).checkAccuracy();
        hit.submit();

        Chain.bound(null).runFn(delay, () -> target.performGraphic(new Graphic(2355, 0, delay))).then(0, () -> {
            for (int i = 0; i < 3; i++) {
                Chain.bound(null).runFn(1, hit::submit);
            }
        });

        CombatSpecial.drain(entity, CombatSpecial.WEBWEAVER_BOW.getDrainAmount());
        return true;
    }

    @Override
    public int getAttackSpeed(Mob entity) {
        return entity.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob entity) {
        return 6;
    }
}
