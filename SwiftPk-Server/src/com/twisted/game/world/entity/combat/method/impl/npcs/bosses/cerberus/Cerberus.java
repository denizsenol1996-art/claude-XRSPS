package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.cerberus;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.superiorbosses.Kerberos;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.util.CustomNpcIdentifiers;
import com.twisted.util.TickDelay;
import com.twisted.util.Utils;

/**
 * @author Patrick van Elderen | March, 10, 2021, 09:44
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class Cerberus extends CommonCombatMethod {

    private final TickDelay comboAttackCooldown = new TickDelay();
    private final TickDelay spreadLavaCooldown = new TickDelay();
    private boolean combatAttack = false;

    private void rangedAttack() {
        //mob.forceChat("RANGE");
        var tileDist = mob.tile().distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        new Projectile(mob, target, 1245, 20 * tileDist, 25, 65, 31, 0, 15, 220).sendProjectile();
        mob.animate(4492);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay + 1, CombatType.RANGED).checkAccuracy().submit();
        target.delayedGraphics(1244, 100, delay + 1);
    }

    private void magicAttack() {
        //mob.forceChat("MAGIC");
        var tileDist = mob.tile().distance(target.tile());
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        new Projectile(mob, target, 1242, 20 * tileDist, 25, 65, 31, 0, 15, 220).sendProjectile();
        mob.animate(4492);

        int hit = CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC);

        target.hit(mob, hit, delay + 1, CombatType.RANGED).checkAccuracy().submit();

        if (hit > 0) {
            target.delayedGraphics(1243, 100, delay + 1);
        }
    }

    private void meleeAttack(boolean animate) {
        if (animate)
            mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy().submit();
    }

    private void comboAttack() {
        mob.animate(4490);
        combatAttack = true;
        magicAttack();
        mob.runFn(2, () -> {
            if (mob == null || target.dead()) {
                return;
            }
            rangedAttack();
        }).then(2, () -> {
            combatAttack = true;
            if (!CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
                return;
            }
            meleeAttack(false);
        });
        comboAttackCooldown.delay(66);
    }

    private void spreadLava() {
        spreadLavaCooldown.delay(36);
        mob.animate(4493);
        mob.forceChat("Grrrrrrrrrr");
        Tile[] positions = {target.tile().copy(),
            new Tile(mob.getAbsX() + Utils.random(-4, mob.getSize() + 4), mob.getAbsY() + Utils.random(-4, mob.getSize() + 4), mob.getZ()),
            new Tile(mob.getAbsX() + Utils.random(-4, mob.getSize() + 4), mob.getAbsY() + Utils.random(-4, mob.getSize() + 4), mob.getZ())};
        for (Tile pos : positions) {
            new Projectile(mob.tile(), target.tile(), 1, 1247, 86, 25, 65, 0, 0, 15, 220).sendProjectile();
            mob.runFn(1, () -> {
                World.getWorld().tileGraphic(1246, new Tile(pos.getX(), pos.getY(), pos.getZ()), 0, 0);
            }).then(2, () -> {
                if (target == null)
                    return;
                if (target.tile().equals(pos)) {
                    target.hit(mob, 15);
                } else if (Utils.getDistance(target.tile(), pos) == 1) {
                    target.hit(mob, 7);
                } else if (Utils.getDistance(target.tile(), pos) <= 4) {
                    target.hit(mob, 10);
                }

            });
        }
    }

    @Override
    public void onDeath(Player killer, Npc npc) {
        comboAttackCooldown.reset();
        spreadLavaCooldown.reset();
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (!comboAttackCooldown.isDelayed()) {
            comboAttack();
        }
        if (mob.hp() <= 200 && !spreadLavaCooldown.isDelayed()) {
            spreadLava();
        }
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollPercent(25)) {
            meleeAttack(true);
        } else if (Utils.rollPercent(50)) {
            magicAttack();
        } else {
            rangedAttack();
        }
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return combatAttack ? 12 : mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }

    @Override
    public boolean rollSuperior(Npc npc) {
        if (World.getWorld().rollDie(50, 1)) {
            npc.transmog(CustomNpcIdentifiers.KERBEROS, true);
            npc.setCombatMethod(new Kerberos());
            return true;
        }
        return false;
    }
}
