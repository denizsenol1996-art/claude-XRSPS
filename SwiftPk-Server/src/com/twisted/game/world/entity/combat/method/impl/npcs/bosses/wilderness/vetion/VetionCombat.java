package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness.vetion;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.route.StepType;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;
import com.google.common.collect.Lists;

import java.util.*;

public class VetionCombat extends CommonCombatMethod {
    boolean hasAttacked = false;
    Set<Tile> usedTiles = new HashSet<>();
    Set<Tile> tiles = new HashSet<>(12);
    private final List<String> VETION_QUOTES = Arrays.asList("Dodge this!",
        "Sit still you rat!",
        "Die, rodent!", "I will end you!",
        "You can't escape!",
        "Filthy whelps!", "Time to die, mortal!", "You call that a weapon?!", "Now i've got you!");

    @Override
    public void preDefend(Hit hit) {
        Player player = (Player) target;
        Npc vetion = (Npc) mob;
        if (player != null) {
            if (houndList.size() == 0) {
                vetion.message("My hounds! I'll make you pay for that!");
            }
            if (!hasAttacked) {
                vetion.getCombat().setTarget(player);
            }
        }
    }

    @Override
    public boolean prepareAttack(Mob entity, Mob target) {
        entity.face(null);
        entity.setEntityInteraction(null);
        entity.stopActions(true);
        if (entity.hp() <= 125 && !entity.hasAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED)) {
            spawnHellhounds((Npc) entity, target);
        }

        if (Utils.percentageChance(50)) {
            magicSwordRaise();
        } else if (Utils.percentageChance(50)) {
            magicSwordSlash();
        } else {
            if (withinDistance(5)) {
                doShieldBash();
            }
        }

        return true;
    }

    @Override
    public int getAttackSpeed(Mob entity) {
        return 6;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }

    @Override
    public void doFollowLogic() {
        Npc vetion = (Npc) mob;
        vetion.face(null);
        vetion.setEntityInteraction(null);
        if (hasAttacked) {
            var t = new Tile(target.tile().getX(), target.tile().getY()).transform(1, 1);
            vetion.stepAbs(t.getX(), t.getY(), StepType.NORMAL);
            hasAttacked = false;
        }
    }

    void magicSwordRaise() {
        Npc vetion = (Npc) mob;
        for (var player : getPossibleTargets(vetion)) {
            if (player == null) continue;
            List<Tile> tileList = new ArrayList<>();
            var targetTile = player.tile();
            tileList.add(targetTile);
            usedTiles.add(targetTile);
            for (int tileIndex = 0; tileIndex < 4; tileIndex++) {
                var tilesAround = World.getWorld().randomTileAround(targetTile, 4);
                if (tilesAround.getZ() != player.getZ()) continue;
                if (usedTiles.contains(tilesAround)) continue;
                usedTiles.add(tilesAround);
                if (tileList.contains(tilesAround)) continue;
                tileList.add(tilesAround);
            }
            for (Tile tile : tileList) {
                if (tile.inSqRadius(vetion.tile(), 2)) continue;
                System.out.println(tile);
                Chain.noCtx()
                    .runFn(1, () -> {
                        vetion.forceChat(Utils.randomElement(VETION_QUOTES));
                        //vetion.getMovementQueue().clear();
                    })
                    .then(1, () -> {
                        vetion.animate(9969);
                        vetion.graphic(vetion.id() == 6612 ? 2345 : 2344, 50, 0);
                        World.getWorld().tileGraphic(vetion.id() == 6612 ? 2347 : 2346, tile, 0, 0);
                    })
                    .then(3, () -> {
                        if (!player.tile().equals(tile) && !player.tile().inSqRadius(tile, 1)) {
                            return;
                        }
                        if (!player.dead() && player.isRegistered() && !vetion.dead() && player.tile().equals(tile)) {
                            new Hit(vetion, player, Utils.random(15, 30), 0, CombatType.MAGIC).checkAccuracy().submit();
                        } else if (player.tile().nextTo(tile)) {
                            new Hit(vetion, player, (Utils.random(15, 30) / 2), 0, CombatType.MAGIC).checkAccuracy().submit();
                        }
                    })
                    .then(2, () -> {
                        vetion.unlock();
                        vetion.getCombat().setTarget(player);
                        vetion.face(null);
                        hasAttacked = true;
                        usedTiles.clear();
                        tiles.clear();
                    });
            }
        }
    }

    void magicSwordSlash() {
        Npc vetion = (Npc) mob;
        for (var player : getPossibleTargets(vetion)) {
            if (player == null) continue;
            List<Tile> tileList = new ArrayList<>();
            var targetTile = player.tile();
            tileList.add(targetTile);
            usedTiles.add(targetTile);
            for (int tileIndex = 0; tileIndex < 4; tileIndex++) {
                var tilesAround = World.getWorld().randomTileAround(targetTile, 4);
                if (tilesAround.getZ() != target.getZ()) continue;
                if (usedTiles.contains(tilesAround)) continue;
                usedTiles.add(tilesAround);
                if (tileList.contains(tilesAround)) continue;
                tileList.add(tilesAround);
            }
            tileList
                .stream()
                .filter(tile -> World.getWorld().clipAt(tile.x, tile.y, tile.level) == 0)
                .filter(tile -> !tile.equals(vetion.tile()))
                .forEach(tile ->
                    Chain.noCtx().runFn(1, () -> {
                            vetion.forceChat(Utils.randomElement(VETION_QUOTES));
                            vetion.lockMovement();
                            vetion.getMovementQueue().clear();
                        })
                        .then(1, () -> {
                            vetion.animate(9972);
                            vetion.graphic(2348);
                            World.getWorld().tileGraphic(vetion.id() == 6612 ? 2347 : 2346, tile, 0, 0);
                        })
                        .then(3, () -> {
                            if (!player.tile().equals(tile) && !player.tile().inSqRadius(tile, 1)) {
                                return;
                            }
                            if (!player.dead() && player.isRegistered() && !vetion.dead() && player.tile().equals(tile)) {
                                new Hit(vetion, player, Utils.random(15, 30), 0, CombatType.MAGIC).checkAccuracy().submit();
                            } else if (player.tile().nextTo(tile)) {
                                new Hit(vetion, player, (Utils.random(15, 30) / 2), 0, CombatType.MAGIC).checkAccuracy().submit();
                            }
                        })
                        .then(2, () -> {
                            vetion.unlock();
                            vetion.getCombat().setTarget(player);
                            vetion.face(null);
                            hasAttacked = true;
                            usedTiles.clear();
                            tiles.clear();
                        }));
        }
    }

    private void doShieldBash() {
        Npc vetion = (Npc) mob;
        var randomTarget = Utils.randomElement(getPossibleTargets(vetion));
        for (Mob target : getPossibleTargets(vetion)) {
            if (target instanceof Player player) {
                Chain.noCtx().runFn(1, () -> {
                    vetion.forceChat(Utils.randomElement(VETION_QUOTES));
                    vetion.face(player.tile());
                    vetion.lockMovement();
                    vetion.getMovementQueue().clear();
                    var dir = Direction.resolveForLargeNpc(player.tile(), vetion);
                    spawnShieldInDir(this, vetion.tile(), dir);
                }).then(2, () -> vetion.animate(9974)).then(1, () -> {
                    for (var t : tiles) {
                        if (t.equals(player.tile())) {
                            if (!player.dead() && !vetion.dead()) {
                                new Hit(vetion, player, Utils.random(15, 30), 0, CombatType.MELEE).checkAccuracy().submit();
                            }
                        }
                    }
                }).then(2, () -> {
                    vetion.unlock();
                    vetion.getCombat().setTarget(randomTarget);
                    vetion.face(null);
                    hasAttacked = true;
                    tiles.clear();
                });
            }
        }
    }

    List<Npc> houndList = Lists.newArrayList();

    private void spawnHellhounds(Npc vetion, Mob target) {
        for (int index = 0; index < 2; index++) {
            var tilesAround = World.getWorld().randomTileAround(vetion.tile(), 4);
            if (!MovementQueue.dumbReachable(tilesAround.x, tilesAround.y, vetion.tile())) continue;
            VetionMinion minion = new VetionMinion(vetion, target);
            houndList.add(minion);
            minion.spawn(false);
            minion.face(target.tile());
            minion.getCombat().setTarget(target);
        }

        vetion.animate(9976);
        vetion.forceChat(vetion.id() == 6611 ? "Gah! Hounds! get them!" : "HOUNDS! DISPOSE OF THESE TRESSPASSERS!");

        vetion.putAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED, true);
        vetion.putAttrib(AttributeKey.MINION_LIST, houndList);
    }

    @Override
    public void onRespawn(Npc npc) {
        npc.spawnDirection(Direction.SOUTH.toInteger());
        npc.getCombat().setTarget(null);
        npc.canAttack(false);
        npc.transmog(6611, false);
        Chain.noCtx().runFn(1, () -> {
            npc.canAttack(true);
            npc.getCombat().setTarget(target);
        });
    }

    @Override
    public boolean customOnDeath(Hit hit) {
        if (mob instanceof Npc npc) {
            if (npc.id() == 6611) {
                Chain.noCtx().runFn(1, () -> {
                    npc.lockNoDamage();
                    npc.canAttack(false);
                    npc.message("Now.. DO IT AGAIN!!!");
                    npc.transmog(6612, true);
                    npc.animate(9979);
                    npc.getTimerRepository().register(TimerKey.VETION_REBORN_TIMER, 500);
                    npc.putAttrib(AttributeKey.VETION_REBORN_ACTIVE, true);
                }).then(1, () -> {
                    npc.unlock();
                    npc.canAttack(true);
                });
            } else if (npc.id() == 6612) {
                for (var m : Lists.newArrayList(houndList)) {
                    if (m == null) continue;
                    houndList.remove(m);
                    m.die();
                }
                houndList.clear();
                npc.getTimerRepository().cancel(TimerKey.VETION_REBORN_TIMER);
                npc.putAttrib(AttributeKey.VETION_REBORN_ACTIVE, false);
                npc.clearAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED);
            }
        }
        return true;
    }

    @Override
    public boolean canMultiAttackInSingleZones() {
        return true;
    }


    public static void spawnShieldInDir(VetionCombat vetionCombat, Tile origin, Direction dir) {
        var PATTERNS = new int[][][]{ // player is [NESW] of vetion
            // NESW
            new int[][]{
                new int[]{-2, 4}, new int[]{-1, 4}, new int[]{0, 4}, new int[]{1, 4}, new int[]{2, 4}, new int[]{3, 4}, new int[]{4, 4}
                , new int[]{-1, 3}, new int[]{0, 3}, new int[]{1, 3}, new int[]{2, 3}, new int[]{3, 3}
            },
            new int[][]{
                new int[]{4, 4}, new int[]{4, 3}, new int[]{4, 2}, new int[]{4, 1}, new int[]{4, 0}, new int[]{4, -1}, new int[]{4, -2}
                , new int[]{3, 3}, new int[]{3, 2}, new int[]{3, 1}, new int[]{3, 0}, new int[]{3, -1}
            },
            new int[][]{
                new int[]{-2, -2}, new int[]{-1, -2}, new int[]{0, -2}, new int[]{1, -2}, new int[]{2, -2}, new int[]{3, -2}, new int[]{4, -2}
                , new int[]{-1, -1}, new int[]{0, -1}, new int[]{1, -1}, new int[]{2, -1}, new int[]{3, -1}
            },
            new int[][]{
                new int[]{-2, -2}, new int[]{-2, -1}, new int[]{-2, 0}, new int[]{-2, 1}, new int[]{-2, 2}, new int[]{-2, 3}, new int[]{-2, 4}
                , new int[]{-1, -1}, new int[]{-1, 0}, new int[]{-1, 1}, new int[]{-1, 2}, new int[]{-1, 3}
            }
        };
        if (dir.ordinal() <= 3) {
            int[][] pattern = PATTERNS[dir.ordinal()];
            if (pattern.length == 0)
                return;
            for (int[] offset : pattern) {
                if (offset == null || offset.length == 0)
                    break;
                var pos = origin.transform(offset[0], offset[1]);
                vetionCombat.tiles.add(pos); // you were missing side ones
                World.getWorld().tileGraphic(1446, pos, 0, 0);
                World.getWorld().tileGraphic(2184, pos, 0, 90);
                //World.getWorld().tileGraphic(2349, pos, 0, 30);
            }
        } else if (dir.ordinal() >= 4 && dir.ordinal() <= 7) {
            Area[][] PATTERNS2 = new Area[][]{
                new Area[]{new Area(-2, 1, -1, 4), new Area(-2, 3, 1, 4),},
                new Area[]{new Area(3, 1, 4, 4), new Area(1, 3, 4, 4),},
                new Area[]{new Area(1, -2, 4, -1), new Area(3, -2, 4, 1),},
                new Area[]{new Area(-2, -2, -1, 1), new Area(-2, -2, 1, -1),},
            };
            Area[] pattern = PATTERNS2[dir.ordinal() - 4];
            if (pattern.length == 0) return;
            for (Area area : pattern) {
                area.forEachPos(t -> {
                    var pos = origin.transform(t.x, t.y);
                    vetionCombat.tiles.add(pos);
                });
            }
            for (Tile tile : vetionCombat.tiles) {
                World.getWorld().tileGraphic(2236, tile, 0, 0);
                World.getWorld().tileGraphic(2184, tile, 0, 90);
            }
        }
    }
}

