package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.masks.animations.Animation;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.NpcIdentifiers;

import java.security.SecureRandom;

public class VoidWaker extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob entity, Mob target) {
        SecureRandom randomGarunteedAccuracy = new SecureRandom();

        double maxHit = entity.getCombat().maximumMeleeHit();
        double minhit = maxHit * 0.5;
        double hitLogic = minhit + randomGarunteedAccuracy.nextInt((int) (maxHit * 1.5 + 1 - minhit));

        entity.animate(new Animation(1378));

        int damage = (int) Math.floor(hitLogic);
        if (damage > 76 && target instanceof Player) {
            damage = 76;
        }

        Hit hit = target.hit(entity, damage, 0, CombatType.MAGIC);

        if (target instanceof Npc) {
            if (target.getAsNpc().id() == NpcIdentifiers.CORPOREAL_BEAST) {
                hit = target.hit(entity, damage, 0, CombatType.MAGIC).checkAccuracy();
            }
        }

        hit.setAccurate(true);

        if (Prayers.usingPrayer(target, Prayers.PROTECT_FROM_MAGIC) || Prayers.usingPrayer(target, Prayers.DAMPEN_MAGIC)) {
            hit.setDamage(hit.getDamage() / 2);
        }

        hit.submit();

        target.performGraphic(new Graphic(2363, 0, 0));

        CombatSpecial.drain(entity, CombatSpecial.VOIDWAKER.getDrainAmount());
        return true;
    }

    @Override
    public int getAttackSpeed(Mob entity) {
        return entity.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob entity) {
        return 1;
    }
}
