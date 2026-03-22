package com.twisted.game.world.entity.combat.method.impl.npcs.bosses;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.region.RegionManager;
import com.twisted.game.world.route.StepType;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import org.apache.commons.compress.utils.Lists;

import java.util.*;

public class VardorvisCombat extends CommonCombatMethod {

    boolean throwingAxes = false;
    final Tile center = Tile.of(1128, 3417);
    List<Npc> axes = new ArrayList<>();
    List<Npc> tendrils = new ArrayList<>();

    int attackCount = 0;

    @Override
    public boolean prepareAttack(Mob entity, Mob target) {
        this.melee();
        this.sequenceSpecialAttack();
        attackCount++;
        return true;
    }

    private void sequenceSpecialAttack() {
        if (attackCount >= 2 && attackCount < 5) {
            this.sendSnowPits();
        } else if (attackCount >= 8) {
            this.throwAxes();
            this.attackCount = 0;
        }
    }

    @Override
    public void process(Mob target) {
        if (this.mob == null)
            return;

        for (final Mob e : this.getPossibleTargets(this.mob)) {
            for (final Npc npc : axes) {
                if (e instanceof Player player) {
                    if (player.tile().isWithinDistance(npc.getCentrePosition(), 1)) {
                        int damage = World.getWorld().random(15, 30);
                        if (Prayers.usingPrayer(player, Prayers.PROTECT_FROM_MELEE)) {
                            damage /= 2;
                        }
                        new Hit(mob, player, damage, 1, CombatType.MELEE).setAccurate(true).submit();
                    }
                }
            }
        }
    }

    public void melee() {
        mob.animate(10340);
        new Hit(mob, target, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy().submit();
    }

    public void sendSnowPits() {
        mob.animate(10341);

        final List<Tile> tiles = new ArrayList<>();

        for (final var entity : this.getPossibleTargets(this.mob)) {
            final Tile targetTile = entity.tile().copy();
            tiles.add(targetTile);
            while (tiles.size() < World.getWorld().random(2, 4)) {
                final Tile tile = World.getWorld().randomTileAround(targetTile, 3);
                if (RegionManager.zarosBlock(tile) || tiles.contains(tile))
                    continue;

                tiles.add(tile);
            }

            Map<Tile, Graphic> snowPits = new HashMap<>();
            for (Tile value : tiles) {
                snowPits.put(value, new Graphic(2510, 0, World.getWorld().random(3, 9)));
            }

            for (final var entry : snowPits.entrySet()) {
                final Graphic graphic = entry.getValue();
                final Tile tile = entry.getKey();
                World.getWorld().tileGraphic(graphic.id(), tile, 0, graphic.delay());
                Chain.noCtx().runFn(3, () -> {
                    World.getWorld().tileGraphic(2335, tile, 0, 0);
                    World.getWorld().tileGraphic(2325, tile, 0, 0);
                    int damage = World.getWorld().random(15, 30);
                    if (Prayers.usingPrayer(entity, Prayers.PROTECT_FROM_MELEE)) {
                        damage /= 2;
                    }
                    if (entity.tile().equals(tile)) {
                        new Hit(mob, entity, damage, 1, CombatType.MELEE).checkAccuracy().submit();
                    }
                });
            }
        }
    }

    public void throwAxes() {
        if (throwingAxes)
            return;

        throwingAxes = true;

        final int randomSize = World.getWorld().random(1, 3);
        final Set<Tile> tiles = new HashSet<>();


        final Tile centerPosition = this.mob.getCentrePosition();

        final Tile[] corners =
            {
                Tile.of(centerPosition.getX() - 8, centerPosition.getY()),
                Tile.of(centerPosition.getX() + 8, centerPosition.getY()),
                Tile.of(centerPosition.getX(), centerPosition.getY() - 8),
                Tile.of(centerPosition.getX(), centerPosition.getY() + 8),
                Tile.of(centerPosition.getX() + 5, centerPosition.getY() + 5),
                Tile.of(centerPosition.getX() - 5, centerPosition.getY() - 5)
            };

        for (int index = 0; index < randomSize; index++) {
            tiles.add(Utils.randomElement(corners));
        }

        for (final Tile tile : tiles) {
            Npc tendril = new Npc(12225, tile).spawn(false);
            tendril.face(center);
            tendril.animate(10364);
            tendrils.add(tendril);
        }

        for (final Npc tendril : tendrils) {
            tendril.animate(10365);
            Chain.noCtx().runFn(2, () -> {
                tendril.animate(10336);
                tendril.transmog(12229, false);
            }).then(1, tendril::remove);

            Chain.noCtx().runFn(2, () -> {
                Npc axe = new Npc(12227, tendril.tile()).spawn(false);
                axes.add(axe);

                final var possibleTargets = this.getPossibleTargets(this.mob);
                final Mob randomTarget = Utils.randomElement(possibleTargets);
                int[] ticks = {0};
                Chain.noCtxRepeat().repeatingTask(1, follow -> {
                    if (ticks[0] >= 15) {
                        for (final Npc npc : Lists.newArrayList(tendrils.iterator())) {
                            npc.remove();
                        }
                        for (final Npc npc : Lists.newArrayList(axes.iterator())) {
                            npc.remove();
                        }
                        tendrils.clear();
                        axes.clear();
                        throwingAxes = false;
                        follow.stop();
                        return;
                    }
                    axe.getMovementQueue().clear();
                    axe.stepAbs(randomTarget.getCentrePosition().getX(), randomTarget.getCentrePosition().getY(), StepType.FORCE_WALK);
                    ticks[0]++;
                });
            });
        }
    }

    @Override
    public int getAttackSpeed(Mob entity) {
        return 5;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }

}
