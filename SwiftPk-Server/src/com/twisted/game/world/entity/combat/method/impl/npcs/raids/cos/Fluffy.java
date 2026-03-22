package com.twisted.game.world.entity.combat.method.impl.npcs.raids.cos;

import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Area;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

/**
 * @author Patrick van Elderen | May, 12, 2021, 13:06
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Fluffy extends CommonCombatMethod {

    private void lighting(Player player) {
        mob.animate(4494);
        Party party = player.raidsParty;
        if(party == null) {
            return;
        }
        for (Player member : party.getMembers()) {
            if (member != null && member.getRaids() != null && member.getRaids().raiding(player) && member.tile().inArea(new Area(3138, 4558, 3144, 4564, member.raidsParty.getHeight()))) {
                final var tile1 = member.tile().copy();
                final var tile2 = member.tile().copy().transform(1, 0);
                final var tile3 = member.tile().copy().transform(1, 1);

                new Projectile(mob.getCentrePosition(), tile1, 731, 1, 125, 40, 25, 0, 0, 16, 96).sendProjectile();
                new Projectile(mob.getCentrePosition(), tile2, 731, 1, 125, 40, 25, 0, 0, 16, 96).sendProjectile();
                new Projectile(mob.getCentrePosition(), tile3, 731, 1, 125, 40, 25, 0, 0, 16, 96).sendProjectile();
                Chain.bound(null).runFn(5, () -> {
                    World.getWorld().tileGraphic(83, tile1, 0, 0);
                    World.getWorld().tileGraphic(83, tile2, 0, 0);
                    World.getWorld().tileGraphic(83, tile3, 0, 0);
                    if (member.tile().equals(tile1) || member.tile().equals(tile2) || member.tile().equals(tile3)) {
                        member.hit(mob, World.getWorld().random(1, 23), CombatType.MAGIC).submit();
                    }
                });
            }
        }
        mob.getTimerRepository().register(TimerKey.COMBAT_ATTACK, 6);
    }

    private void rangeAttack(Player player) {
        mob.face(null); // Stop facing the target
        mob.animate(4492);
        Party party = player.raidsParty;
        for (Player member : party.getMembers()) {
            if (member != null && member.getRaids() != null && member.getRaids().raiding(player) && member.tile().inArea(new Area(3138, 4558, 3144, 4564, member.raidsParty.getHeight()))) {
                var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
                var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

                new Projectile(mob, member, 182, 12 * tileDist, 25, 65, 31, 0, 15, 220).sendProjectile();
                member.hit(mob, CombatFactory.calcDamageFromType(mob, member, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
                member.delayedGraphics(183, 50, delay);
            }
        }

        mob.face(target.tile()); // Go back to facing the target.
    }

    private void magicAttack(Player player) {
        mob.face(null); // Stop facing the target
        mob.animate(4492);
        Party party = player.raidsParty;
        for (Player member : party.getMembers()) {
            if (member != null && member.getRaids() != null && member.getRaids().raiding(player) && member.tile().inArea(new Area(3138, 4558, 3144, 4564, member.raidsParty.getHeight()))) {
                var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
                var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
                new Projectile(mob, member, 1403, 12 * tileDist, 25, 65, 31, 0, 15, 220).sendProjectile();
                member.hit(mob, CombatFactory.calcDamageFromType(mob, member, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
            }
        }
        mob.face(target.tile()); // Go back to facing the target.
    }

    private void meleeAttack() {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    private boolean specialAttack = false;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (!mob.isNpc() || !target.isPlayer())
            return false;

        Player player = (Player) target;

        if (World.getWorld().rollDie(20, 1)) { //5% chance the npc sends lightning
            lighting(player);
            specialAttack = true;
        }

        // Determine if we're going to melee or mage
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            int chance = World.getWorld().random(6);
            if (chance == 1) {
                rangeAttack(player);
            } else if (chance == 2) {
                magicAttack(player);
            } else {
                meleeAttack();
            }
        } else {
            int chance = World.getWorld().random(3);
            if (chance == 1) {
                rangeAttack(player);
            } else {
                magicAttack(player);
            }
        }
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return specialAttack ? 10 : mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
