package com.twisted.game.world.entity.combat.method.impl.specials.melee;

import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.Player;

public class AbyssalWhip extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(1658);
        //todo it.player().world().spawnSound(it.player().tile(), 2713, 0, 10)
        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if (target.dead()) {
            return false;
        }

        target.graphic(341, 100, 0);
        if (target.isPlayer()) {
            Player t = (Player) target;
            Player player = (Player) mob;
            double target_cur_energy = t.getAttribOr(AttributeKey.RUN_ENERGY, 100.0);
            double player_cur_energy = player.getAttribOr(AttributeKey.RUN_ENERGY, 100.0);
            if (target_cur_energy > 0.0) {
                double drain = target_cur_energy / 10;
                if (drain > 0) {
                    t.setRunningEnergy((target_cur_energy - drain), true);
                    player.setRunningEnergy((player_cur_energy + drain), true);
                }
            }
        }
        CombatSpecial.drain(mob, CombatSpecial.ABYSSAL_WHIP.getDrainAmount());
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
