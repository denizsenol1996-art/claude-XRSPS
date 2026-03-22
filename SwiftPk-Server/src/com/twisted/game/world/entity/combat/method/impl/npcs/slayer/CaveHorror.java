package com.twisted.game.world.entity.combat.method.impl.npcs.slayer;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Utils;

/**
 * @author PVE
 * @Since augustus 06, 2020
 */
public class CaveHorror extends CommonCombatMethod {

    private void basicAttack(Mob mob, Mob target) {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        Player player = (Player) target;
        if (player.getEquipment().getId(EquipSlot.AMULET) != 8923) {
            mob.animate(4237);
            player.hit(mob, Utils.random(target.maxHp() / 10), CombatType.MELEE).submit();
            player.message("<col=ff0000>The cave horror's scream rips through you!");
            player.message("<col=ff0000>A witchwood icon can protect you from this attack.");
        } else
            basicAttack(mob, target);

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
