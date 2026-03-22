package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.chainedwork.Chain;

public class UrsineMace extends CommonCombatMethod {
    @Override
    public boolean prepareAttack(Mob entity, Mob target) {
        entity.animate(9963);
        entity.performGraphic(new Graphic(2341, 100, 0));
        entity.performGraphic(new Graphic(2342, 100, 0));

        Hit hit = target.hit(entity, CombatFactory.calcDamageFromType(entity, target, CombatType.MELEE),2, CombatType.MELEE).checkAccuracy();

        if (hit.isAccurate()) {
            if (target.isPlayer()) {
                target.freeze(6, target);
                target.skills().setLevel(Skills.AGILITY, target.skills().level(Skills.AGILITY) - 20);
                Chain.bound(null).runFn(2, () -> {
                    for (int index = 0; index < 4; index++) {
                        Chain.bound(null).name("ursinebleed").runFn(index * 2, () -> {
                            Hit bleed = target.hit(entity, 4, 1, CombatType.MELEE).checkAccuracy();
                            bleed.submit();
                        });
                    }
                });
            } else {
                if (hit.isAccurate() && target.isNpc()) {
                    Chain.bound(null).runFn(2, () -> {
                        for (int index = 0; index < 4; index++) {
                            Chain.bound(null).name("ursinebleed").runFn(index * 2, () -> {
                                Hit bleed = target.hit(entity, 9, 1, CombatType.MELEE).checkAccuracy();
                                bleed.submit();
                            });
                        }
                    });
                }
            }
        }

        CombatSpecial.drain(entity, CombatSpecial.URSINE_CHAINMACE.getDrainAmount());
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
