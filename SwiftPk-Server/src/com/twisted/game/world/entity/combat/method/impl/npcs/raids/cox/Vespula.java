package com.twisted.game.world.entity.combat.method.impl.npcs.raids.cox;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.container.equipment.Equipment;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.route.Direction;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.NpcIdentifiers.VESPULA;
import static com.twisted.util.NpcIdentifiers.VESPULA_7532;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 30, 2021
 */
public class Vespula extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (mob.isNpc()) {
            Npc npc = mob.getAsNpc();
            if (npc.id() != VESPULA || !withinDistance(1) || World.getWorld().random(5) > 3)
                rangeAttack(npc, target);
            else
                meleeAttack(npc, target);
        }
        return true;
    }

    private void rangeAttack(Npc npc, Mob target) {
        if (target.isPlayer()) {
            Player player = target.getAsPlayer();
            npc.animate(mob.attackAnimation());
            var tileDist = npc.tile().transform(1, 1, 0).distance(target.tile());
            var delay = Math.max(1, (50 + (tileDist * 12)) / 30);

            npc.face(null); // Stop facing the target
            //Target all raids party members
            if (player.raidsParty != null) {
                for (Player p : player.raidsParty.getMembers()) {
                    if (p != null && p.getRaids() != null && p.getRaids().raiding(p) && p.tile().inArea(new Area(3298, 5287, 3325, 5309, p.raidsParty.getHeight()))) {
                        if (npc.id() == VESPULA) {
                            new Projectile(npc, target, 1486, 20, 12 * tileDist, 70, 43, 0).sendProjectile();
                        } else {
                            new Projectile(npc, target, 1486, 20, 12 * tileDist, 40, 43, 0).sendProjectile();
                        }
                        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().postDamage(this::handleAfterHit).submit();

                        //echo projectile
                        Direction echoDir = World.getWorld().random(Direction.values());
                        Tile echoTile = target.tile().copy().transform(echoDir.deltaX, echoDir.deltaY, target.tile().level);

                        if (npc.id() == VESPULA)
                            new Projectile(mob.tile().transform(1, 1, 0), echoTile, 1, 1486, 100, 30, 70, 0, 0).sendProjectile();
                        else
                            new Projectile(mob.tile().transform(1, 1, 0), echoTile, 1, 1486, 100, 30, 40, 0, 0).sendProjectile();

                        Chain.bound(null).runFn(4, () -> {
                            if (p.isAt(echoTile)) {
                                target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().postDamage(this::handleAfterHit).submit();
                            }
                        });
                    }
                }
            }
        }
    }

    private void meleeAttack(Npc npc, Mob target) {
        npc.animate(7454);
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
    }

    public static void onHit(Npc npc, int damage) {
        if (npc.hp() > npc.maxHp() / 5 && (npc.hp() - damage) <= npc.maxHp() / 5) {
            // land
            npc.transmog(VESPULA_7532);
            npc.heal(npc.maxHp());
            npc.combatInfo(World.getWorld().combatInfo(VESPULA_7532));
            npc.setCombatMethod(World.getWorld().combatInfo(VESPULA_7532).scripts.newCombatInstance());
            npc.animate(7457);
            Chain.bound(null).runFn(50, () -> {
                if (!npc.dead()) {
                    npc.transmog(VESPULA);
                    npc.combatInfo(World.getWorld().combatInfo(VESPULA_7532));
                    npc.setCombatMethod(World.getWorld().combatInfo(VESPULA_7532).scripts.newCombatInstance());
                    npc.animate(7452);
                    npc.heal(npc.maxHp());
                }
            });
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }

    public void handleAfterHit(Hit hit) {
        Mob attacker = hit.getAttacker();
        Mob target = hit.getTarget();
        if (World.getWorld().rollDie(5,1)) {
            if (!Equipment.venomHelm(target)) { // Serp helm stops poison.
                target.hit(attacker, 20, SplatType.POISON_HITSPLAT);
            }
        }
    }
}
