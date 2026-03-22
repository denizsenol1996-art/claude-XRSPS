package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.xamphur;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.animations.Animation;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.droptables.ItemDrops;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.region.RegionManager;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import org.apache.commons.compress.utils.Lists;

import java.util.*;
import java.util.function.BooleanSupplier;

public class XamphurCombat extends CommonCombatMethod {

    int attackcount = 0;
    boolean initiated = false;
    List<GameObject> objects = new ArrayList<>();
    Set<Tile> tiles = new HashSet<>();
    List<Mob> corruption = new ArrayList<>();
    Map<Player, Integer> damageMap = new HashMap<>();

    @Override
    public void processArea(Mob entity, Mob target) {
        for (var targets : getPossibleTargets(this.mob)) {
            if (targets == null || targets.dead()) continue;
            if (targets.hasAttrib(AttributeKey.MARK_OF_DARKNESS)) continue;
            for (var tile : tiles) {
                if (tile == null) continue;
                if (tile.equals(targets.tile())) {
                    if (targets instanceof Player player) {
                        markOfDarkness(player);
                    }
                }
            }
        }
    }

    @Override
    public boolean prepareAttack(Mob entity, Mob target) {
        if (!this.initiated)
            return false;

        if (attackcount % 5 == 0) {
            attackcount = 0;
            resetAndShuffleMarks();
        }

        if (Utils.rollDie(2, 1)) {
            regularAttack();
        } else {
            handAttack();
        }

        attackcount++;
        return true;
    }

    final void regularAttack() {
        this.mob.animate(9064);
        for (var player : getPossibleTargets(this.mob)) {

            if (player == null || player.dead())
                continue;

            player.graphic(1915);
            target.hit(mob, Utils.random(1, 5));
        }
    }


    final void handAttack() {
        mob.animate(9065);
        for (var player : getPossibleTargets(this.mob)) {
            if (player == null || player.dead()) continue;
            Tile firstLocation = player.tile().copy();
            World.getWorld().tileGraphic(1919, firstLocation, 0, 15);
            Chain.bound(player).runFn(2, () -> {
                if (player.tile().equals(firstLocation)) {
                    target.hit(mob, Utils.random(1, 5));
                }
            });
        }
        Chain.noCtx().runFn(2, () -> this.mob.animate(Animation.DEFAULT_RESET_ANIMATION));
    }

    final void markOfDarkness(Player player) {
        if (player == null || player.dead()) return;
        if (player.hasAttrib(AttributeKey.MARK_OF_DARKNESS)) return;
        player.message("<col=a53fff>A Mark of Darkness has been placed upon you.</col>");
        player.graphic(1852);
        player.putAttrib(AttributeKey.MARK_OF_DARKNESS, true);
        int[] taskCount = new int[]{0};
        int[] hitCount = new int[]{0};
        corruption.add(player);
        BooleanSupplier cancel = () -> !corruption.contains(player);
        Chain.noCtxRepeat().cancelWhen(cancel).repeatingTask(1, task -> {
            if (taskCount[0] >= 50 || !player.hasAttrib(AttributeKey.MARK_OF_DARKNESS)) {
                removeFromList(player);
                player.message("<col=6800bf>Your Mark of Darkness has faded away.</col>");
                player.clearAttrib(AttributeKey.MARK_OF_DARKNESS);
                task.stop();
                return;
            }
            if (hitCount[0] >= 4) {
                hitCount[0] = 0;
                target.hit(mob, Utils.random(1, 5));
            }
            taskCount[0]++;
            hitCount[0]++;
        });
    }

    final void removeFromList(Mob player) {
        for (var p : Lists.newArrayList(corruption.iterator())) {
            if (!p.equals(player)) continue;
            corruption.remove(p);
        }
    }

    final void buildMarksOfDarknessTiles() {
        final Tile copy = this.mob.tile().copy();
        for (int index = 0; index < 16; index++) {
            var randomTile = World.getWorld().randomTileAround(copy, 6);
            if (randomTile == null) {
                continue;
            }
            if (randomTile.x < 0 || randomTile.y < 0) {
                continue;
            }
            if (!RegionManager.zarosBlock(randomTile) && randomTile.allowObjectPlacement()) {
                tiles.add(randomTile);
            }
        }
        for (var tile : tiles) objects.add(new GameObject(41881, tile));
    }

    final void resetAndShuffleMarks() {
        for (var object : objects) object.remove();
        objects.clear();
        tiles.clear();
        buildMarksOfDarknessTiles();
        for (var object : objects) object.spawn();
    }

    @Override
    public void postDamage(Hit hit) {
        incrementDamageMap(hit);
    }

    @Override
    public boolean customOnDeath(Hit hit) {
        if (this.mob instanceof Npc npc) {
            clearCorruptions();
            Chain.noCtx().runFn(3, () -> {
                computeDropTable(npc);
                clear(npc);
            });
        }
        return true;
    }

    final void incrementDamageMap(Hit hit) {
        var source = hit.getAttacker();
        var target = hit.getTarget();
        if (source instanceof Player player && target instanceof Npc) {
            damageMap.compute(player, (_, v) -> (v == null ? 0 : v) + hit.getDamage());
        }
    }

    final void computeDropTable(Npc npc) {
        for (var entry : damageMap.entrySet()) {
            var player = entry.getKey();
            if (player.tile().region() == 10317) {
                ItemDrops.rollTheDropTable(player, npc);
            }
        }
        damageMap.clear();
    }

    final void clearCorruptions() {
        for (var player : corruption) {
            if (!player.hasAttrib(AttributeKey.MARK_OF_DARKNESS)) continue;
            player.clearAttrib(AttributeKey.MARK_OF_DARKNESS);
        }
        tiles.clear();
    }

    final void clear(Npc npc) {
        for (var object : objects) object.remove();
        damageMap.clear();
        objects.clear();
        npc.remove();
    }

    @Override
    public int getAttackSpeed(Mob entity) {
        return 4;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 64;
    }
}
