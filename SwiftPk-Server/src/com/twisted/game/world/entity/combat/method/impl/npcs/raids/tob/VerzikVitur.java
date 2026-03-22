package com.twisted.game.world.entity.combat.method.impl.npcs.raids.tob;

import com.twisted.game.task.Task;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.twisted.util.ItemIdentifiers.DAWNBRINGER;
import static com.twisted.util.NpcIdentifiers.*;
import static com.twisted.util.ObjectIdentifiers.TREASURE_ROOM;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since January 07, 2022
 */
public class VerzikVitur extends CommonCombatMethod {

    private static final int OUT_OF_CHAIR = 8111;
    private static final int CHAIR_ATTACK = 8109;
    private static final Tile SPIDER_SPAWN = new Tile(3171, 4315);
    private static final Area ARENA = new Area(3154, 4303, 3182, 4322);
    private int bombCount = 0;
    private int electricCount = 0;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        set(mob, target);
        List<Mob> targets = getPossibleTargets(mob);
        final Tile tile = target.tile();
        if (mob.getAsNpc().id() == VERZIK_VITUR_8370) {
            mob.animate(CHAIR_ATTACK);
            for (Mob t : targets) {
                if (t == null || t.getAsPlayer().dead() || !t.tile().isWithinDistance(mob.tile(), 32) || !t.tile().inArea(ARENA)) {
                    continue;
                }
                final Tile t_tile = t.tile();
                Projectile projectile = new Projectile(mob.tile().center(mob.getSize()).transform(-1, 0), target.tile(), -1, 1580, 220, 0, 100, 0, 0);
                int dmg = Prayers.usingPrayer(t, Prayers.PROTECT_FROM_MAGIC) ? World.getWorld().random(1, 60) : World.getWorld().random(1, 137);
                handleDodgableAttack(mob, t, projectile, null, dmg, 7, new Task("VerzikViturPrepareAttackTask1", 1) {
                    int count = 0;

                    @Override
                    public void execute() {
                        count++;
                        if (count == 8) {
                            World.getWorld().tileGraphic2(1582, t_tile, 0, 0);
                            Stream<Mob> ts = targets.stream().filter(n -> !n.equals(t) && n.tile().withinDistanceIgnoreHeight(t_tile, 1));
                            int dmg = Prayers.usingPrayer(t, Prayers.PROTECT_FROM_MAGIC) ? World.getWorld().random(1, 60) : World.getWorld().random(1, 137);
                            ts.forEach(t -> t.hit(mob, dmg, 0, CombatType.MAGIC).setAccurate(false).checkAccuracy().submit());
                            stop();
                        }
                    }
                });
            }
        }
        if (mob.getAsNpc().id() == VERZIK_VITUR_8372) {
            mob.animate(8114);
            if (bombCount < 4) {
                for (Mob t : targets) {
                    if (t == null || t.getAsPlayer().dead() || !t.tile().isWithinDistance(mob.tile(), 32) || !t.tile().inArea(ARENA)) {
                        continue;
                    }
                    final Tile t_tile = t.tile();
                    Projectile projectile = new Projectile(mob.tile(), t.tile(), -1, 1583, 130, 0, 100, 0, 0);
                    handleDodgableAttack(mob, t, projectile, null, World.getWorld().random(1, 60), 5, new Task("VerzikViturPrepareAttackTask2", 1) {
                        int count = 0;

                        @Override
                        public void execute() {
                            count++;
                            if (count == 5) {
                                World.getWorld().tileGraphic2(1584, t_tile, 0, 0);
                                Stream<Mob> ts = targets.stream().filter(n -> !n.equals(t) && n.tile().withinDistanceIgnoreHeight(t_tile, 1));
                                ts.forEach(t -> t.hit(mob, World.getWorld().random(1, 60), 0, CombatType.MAGIC).setAccurate(false).checkAccuracy().submit());
                                stop();
                            }
                        }
                    });
                }
                bombCount++;
            } else {
                if (electricCount < 2) {
                    for (Mob t : targets) {
                        if (t == null || t.getAsPlayer().dead() || !t.tile().isWithinDistance(mob.tile(), 32) || !t.tile().inArea(ARENA)) {
                            continue;
                        }
                        mob.hit(t, target.hp() < 2 ? 1 : 20, 3, CombatType.MAGIC).submit();
                    }
                    electricCount++;
                    bombCount = 0;
                } else {
                    Projectile projectile = new Projectile(mob.tile(), target.tile(), -1, 1586, 130, 0, 100, 0, 0);
                    handleDodgableAttack(mob, target, projectile, null, World.getWorld().random(1, 60), 5, new Task("VerzikViturPrepareAttackTask3", 1) {
                        int count = 0;
                        Npc healer;
                        Npc bomber;

                        @Override
                        public void execute() {
                            count++;
                            if (count == 5 && healer == null && bomber == null) {
                                healer = new Npc(NYLOCAS_ATHANATOS, tile).spawn(false);
                                healer.animate(8079);
                                healer.face(mob.tile());
                                bomber = new Npc(NYLOCAS_MATOMENOS_8385, SPIDER_SPAWN).spawn(false);
                                bomber.getCombat().setTarget(target);
                                bomber.animate(8098);
                            }
                            if (count >= 5 && healer != null) {
                                if (healer.hp() < healer.maxHp()) {
                                    healer.hit(healer, healer.hp());
                                }
                            }
                            if (count >= 15) {
                                if (bomber != null)
                                    bomber.hit(healer, healer.hp());
                                if (healer != null)
                                    healer.hit(healer, healer.hp());
                                stop();
                            }
                            if ((count % 2 == 0) && healer != null && !healer.dead()) {
                                Projectile projectile = new Projectile(mob, target, 1578, 0, 100, 50, 25, 0);
                                projectile.sendProjectile();
                                mob.hit(mob, 6, SplatType.NPC_HEALING_HITSPLAT);
                            }
                        }
                    });
                    electricCount = 0;
                }
            }
        }
        if (mob.getAsNpc().id() == VERZIK_VITUR_8374) {
            int random = World.getWorld().random(0, 2);
            if (random == 0) {
                if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
                    meleeAttack(mob, target);
                }
            } else if (random == 1) {
                mob.animate(8124);
                for (Mob t : targets) {
                    if (t == null || t.getAsPlayer().dead() || !t.tile().inArea(ARENA)) {
                        continue;
                    }
                    Projectile projectile = new Projectile(mob, target, 1580, 0, 220, 100, 0, 0);
                    projectile.sendProjectile();
                    t.hit(mob, World.getWorld().random(1, 40), 7, CombatType.MAGIC).checkAccuracy().submit();
                }
            } else if (random == 2) {
                mob.animate(8125);
                for (Mob t : targets) {
                    if (t == null || t.getAsPlayer().dead() || !t.tile().inArea(ARENA)) {
                        continue;
                    }
                    var delay = mob.getProjectileHitDelay(t);
                    Projectile projectile = new Projectile(mob, target, 1560, 0, mob.projectileSpeed(t), 25, 30, 0);
                    projectile.sendProjectile();
                    t.hit(mob, World.getWorld().random(1, 40), delay, CombatType.RANGED).checkAccuracy().submit();
                }
            }
        }
        return true;
    }

    private void meleeAttack(Mob mob, Mob target) {
        mob.animate(8123);
        target.hit(mob, World.getWorld().random(1, 40), 0, CombatType.MELEE).submit();
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return mob.isNpc() && mob.getAsNpc().id() == VERZIK_VITUR_8374 ? 1 : 32;
    }

    @Override
    public void onHit(Mob attacker, Mob verzik, Hit hit) {
        if (verzik.isNpc()) {
            if (verzik.getAsNpc().id() == VERZIK_VITUR_8370) {
                hit.setSplatType(SplatType.VERZIK_SHIELD_HITSPLAT);
            }
            if (verzik.getAsNpc().id() == VERZIK_VITUR_8371 || verzik.getAsNpc().id() == VERZIK_VITUR_8375) {
                hit.setDamage(0);
                hit.setSplatType(SplatType.BLOCK_HITSPLAT);
            }
        }
    }

    @Override
    public boolean customOnDeath(Hit hit) {
        if (hit.getTarget().isNpc()) {
            List<Mob> targets = getPossibleTargets(hit.getTarget());
            Mob target = Utils.randomElement(targets);
            System.out.println("test");
            return transform(hit.getTarget(), ((Player) target));
        }
        return true;
    }

    @Override
    public void process(Mob mob) {
        if (mob.getCombat().getTarget() == null) {
            List<Mob> targets = getPossibleTargets(mob);
            Mob newTarg = Utils.randomElement(targets);
            mob.getCombat().setTarget(newTarg);
        }
        //System.out.println(target);
    }


    private boolean transform(Mob mob, Player player) {
        var party = player.raidsParty;
        if (party == null) {
            return false;
        }
        if (mob.getAsNpc().id() == VERZIK_VITUR_8370) {
            List<Mob> targets = getPossibleTargets(mob);
            mob.getAsNpc().canAttack(false);
            mob.animate(OUT_OF_CHAIR);
            mob.resetFaceTile();
            targets.forEach(t -> {
                t.getAsPlayer().removeAll(new Item(DAWNBRINGER));
                t.getAsPlayer().getCombat().reset();
            });

       /*     CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                    System.out.println("chain work");
                    mob.getAsNpc().transmog(VERZIK_VITUR_8371);
                    mob.getAsNpc().def(World.getWorld().definitions().get(NpcDefinition.class, VERZIK_VITUR_8371));
                    mob.heal(mob.maxHp());
                    mob.getAsNpc().animate(-1);
                    mob.getAsNpc().canAttack(true);
                    return true; // CompletableFuture requires a return value
                }).thenApply(conditionMet -> {
                    mob.setEntityInteraction(null);
                    mob.noRetaliation(true);
                    mob.getCombat().setTarget(null);
                    mob.getTimers().cancel(TimerKey.FROZEN);
                    Tile targetTile = new Tile(3167, 4311, mob.tile().level);
                    mob.smartPathTo(targetTile.getX(), targetTile.getY());
                    return true;
                })
                .thenAccept(conditionMet -> {
                    GameObject gameObject = new GameObject(VERZIKS_THRONE_32737, new Tile(3167, 4324, mob.tile().level), 10, 0);
                    party.objects.add(gameObject);
                    gameObject.spawn();
                    mob.getAsNpc().completelyLockedFromMoving(true);
                    mob.getAsNpc().transmog(VERZIK_VITUR_8372);
                    mob.getAsNpc().def(World.getWorld().definitions().get(NpcDefinition.class, VERZIK_VITUR_8372));
                });
            future.join(); // Wait for the chain to complete
            return true;
        } else if (mob.getAsNpc().id() == VERZIK_VITUR_8372) {
            mob.getCombat().reset();
            mob.getAsNpc().canAttack(false);
            mob.animate(8119);
            mob.getAsNpc().transmog(VERZIK_VITUR_8374);
            mob.getAsNpc().def(World.getWorld().definitions().get(NpcDefinition.class, VERZIK_VITUR_8374));
            mob.heal(mob.maxHp());

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                mob.animate(-1);
                mob.forceChat("Behold my true nature!");
                mob.getAsNpc().canAttack(true);
                mob.getAsNpc().completelyLockedFromMoving(false);
                mob.getAsNpc().cantMoveUnderCombat(false);
                mob.getAsNpc().combatInfo().aggressive = true;
                mob.getAsNpc().combatInfo().aggroradius = 25;
                mob.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 25);
            });

            future.join(); // Wait for the chain to complete
            return true;*/
       // } else if (mob.getAsNpc().id() == VERZIK_VITUR_8374) {
            /*mob.getAsNpc().canAttack(false);
            mob.getAsNpc().transmog(VERZIK_VITUR_8375);
*/
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                World.getWorld().unregisterNpc(mob.getAsNpc());
            }).thenRun(() -> {
                GameObject gameObject = new GameObject(TREASURE_ROOM, new Tile(3167, 4324, mob.tile().level), 10, 0);
                party.objects.add(gameObject);
                gameObject.spawn();

                List<Player> players = party.getMembers();
                players.forEach(p -> {
                    p.message("You have defeated Verzik Vitur!");
                    p.getCombat().reset();
                });

                if (party != null) {
                    if (party.getLeader().getRaids() != null) {
                        party.getLeader().getRaids().complete(party);
                    }
                }
            });

            future.join(); // Wait for the chain to complete
            return true;
        }

        return false;
    }
}
