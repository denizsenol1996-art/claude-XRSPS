package com.twisted.game.world.entity.combat.method.impl.npcs.slayer.superiors;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

/**
 * The greater abyssal demon has a special attack, like other high levelled superior slayer monsters.
 * The greater abyssal demon will teleport around the player, hitting them quickly at an attack speed of 1 for the next four hits (one hit per teleport).
 * This special attack has 100% accuracy regardless of the player's defensive bonuses and is almost always guaranteed to hit 20 for each attack,
 * so it is advised to keep Protect from Melee active at all times while killing the greater abyssal demon.
 * <p>
 * Like all demons, the greater abyssal demon is weak against demonbane weapons.
 *
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * maart 31, 2020
 */
public class GreaterAbyssalDemon extends CommonCombatMethod {

    private static final byte[][] BASIC_OFFSETS = new byte[][]{{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (!mob.isNpc() || !target.isPlayer())
            return false;
        mob.animate(mob.attackAnimation());
        if (Utils.percentageChance(20)) {
            teleportAttack(mob, target);
            mob.getTimerRepository().register(TimerKey.COMBAT_ATTACK, 17);//Set attack timer to 17 ticks, because that's how long this attack lasts for.
        } else {
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
            if (Utils.random(4) == 0) {
                final byte[] offsets = BASIC_OFFSETS[Utils.random(3)];
                target.teleport(target.getX() + offsets[0], target.getY() + offsets[1],target.getZ());
            }
        }
        return true;
    }

    private void teleportAttack(Mob mob, Mob target) {

        final byte[] offsets = BASIC_OFFSETS[Utils.random(3)];

        if (mob.dead() || target.dead() || !target.tile().isWithinDistance(mob.tile(), 15)) {
            return;
        }

        Chain.bound(null).runFn(2, () -> {
            mob.teleport(new Tile(target.getX() + offsets[0], target.getY() + offsets[1], mob.getZ()));
        }).then(2, () -> { // First attack
            mob.graphic(409);
            mob.face(target.tile());
            mob.animate(mob.attackAnimation());
            target.hit(mob, Utils.random(31), CombatType.MELEE).checkAccuracy().submit();
        }).then(2, () -> {
            mob.teleport(new Tile(target.getX() + offsets[0], target.getY() + offsets[1], mob.getZ()));
        }).then(2, () -> {// Second attack
            mob.graphic(409);
            mob.face(target.tile());
            mob.animate(mob.attackAnimation());
            target.hit(mob, Utils.random(31), CombatType.MELEE).checkAccuracy().submit();
        }).then(2, () -> {
            mob.teleport(new Tile(target.getX() + offsets[0], target.getY() + offsets[1], mob.getZ()));
        }).then(2, () -> { // Third attack
            mob.graphic(409);
            mob.face(target.tile());
            mob.animate(mob.attackAnimation());
            target.hit(mob, Utils.random(31), CombatType.MELEE).checkAccuracy().submit();
        }).then(2, () -> {
            mob.teleport(new Tile(target.getX() + offsets[0], target.getY() + offsets[1], mob.getZ()));
        }).then(2, () -> {// Fourth attack
            mob.graphic(409);
            mob.face(target.tile());
            mob.animate(mob.attackAnimation());
            target.hit(mob, Utils.random(31), CombatType.MELEE).checkAccuracy().submit();
        });
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 2;
    }
}
