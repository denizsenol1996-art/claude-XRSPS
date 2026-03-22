package com.twisted.game.world.entity.combat.method.impl.npcs.raids.cox;

import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

import java.util.Arrays;
import java.util.List;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 29, 2021
 */
public class VasaNistirio extends CommonCombatMethod {

    private boolean specialAttack = false;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        mob.animate(mob.attackAnimation());

        if (target.isPlayer()) {
            Player player = target.getAsPlayer();
            if(!mob.getTimerRepository().has(TimerKey.VASA_TELEPORT_ATTACK)) {
                specialAttack = true;
                mob.getTimerRepository().register(TimerKey.VASA_TELEPORT_ATTACK,100);
                teleportAttack(mob, player);
            } else {
                rangeAttack(mob, player);
            }
        }
        return true;
    }

    private void rangeAttack(Mob mob, Player target) {
        Party party = target.raidsParty;
        if (party == null) {
            return;
        }

        for (Player member : party.getMembers()) {
            if (member != null && member.getRaids() != null && member.getRaids().raiding(member) && member.tile().inArea(new Area(3298, 5282, 3399, 5308, member.raidsParty.getHeight()))) {
                final var tile = target.tile().copy();
                new Projectile(mob.tile().transform(1, 1, 0), tile, 1, 1329, 125, 30, 175, 6, 0).sendProjectile();

                Chain.bound(null).runFn(6, () -> {
                    World.getWorld().tileGraphic(1330, tile, 5, 0);
                    if (target.tile().equals(tile)) {
                        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 0, CombatType.RANGED).checkAccuracy().submit();
                    }
                });
            }
        }
    }

    private void teleportAttack(Mob mob, Player target) {
        //Vasa doesn't take damage during this phase
        mob.lockNoDamage();
        Chain.bound(null).runFn(1, () -> {
            target.graphic(1296,100,0);
        }).then(2, () -> {
            target.lock();
            target.teleport(mob.tile().x - 1, mob.tile().y, mob.tile().level);
        }).then(1, () -> {
            //According to the video the teleport attack is always in the middle
            final List<Tile> tiles = Arrays.asList(
                new Tile(3308, 5293, mob.tile().level),
                new Tile(3308, 5295, mob.tile().level),
                new Tile(3308, 5298, mob.tile().level),
                new Tile(3311, 5293, mob.tile().level),
                new Tile(3311, 5295, mob.tile().level),
                new Tile(3311, 5298, mob.tile().level),
                new Tile(3313, 5292, mob.tile().level),
                new Tile(3313, 5295, mob.tile().level),
                new Tile(3313, 5297, mob.tile().level)
            );

            for (Tile tile : tiles) {
                new Projectile(mob.tile().transform(1, 1, 0), tile, 1, 1327, 165, 30, 200, 6, 0).sendProjectile();

                Chain.bound(null).runFn(6, () -> {
                    World.getWorld().tileGraphic(1328, tile, 5, 0);
                    if (target.tile().isWithinDistance(tile,3)) {
                        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 0, CombatType.MAGIC).checkAccuracy().submit();
                    }
                    target.unlock();
                    mob.unlock();// Attack is over, unlock vasa
                });
            }
        });
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return specialAttack ? 10 : mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }

}
