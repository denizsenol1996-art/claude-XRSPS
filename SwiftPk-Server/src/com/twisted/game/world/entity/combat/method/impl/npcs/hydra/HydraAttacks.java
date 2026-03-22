package com.twisted.game.world.entity.combat.method.impl.npcs.hydra;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import java.util.ArrayList;

import static com.twisted.game.world.entity.combat.method.impl.npcs.hydra.HydraChamber.*;

/**
 * The Npc extension for the hydras.
 *
 * @author Gabriel || Wolfsdarker
 */
public enum HydraAttacks {

    RANGED(null) {
        @Override
        public void executeAttack(AlchemicalHydra hydra, Mob target) {
            hydra.animate(hydra.getAttackAnim());
            fireProjectileToEntity(hydra, target, 1663, 50);
            var tileDist = hydra.tile().transform(3, 3, 0).distance(target.tile());
            var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

            Chain.bound(null).runFn(delay, () -> {
                target.hit(hydra, CombatFactory.calcDamageFromType(hydra, target, CombatType.MAGIC), CombatType.RANGED).checkAccuracy().submit();
            });
        }
    },

    /**
     * The default magic attack.
     */
    MAGIC(null) {
        @Override
        public void executeAttack(AlchemicalHydra hydra, Mob target) {
            hydra.animate(hydra.getAttackAnim());
            fireProjectileToEntity(hydra, target, 1662, 50);
            fireProjectileToEntity(hydra, target, 1662, 60);

            var tileDist = hydra.tile().transform(3, 3, 0).distance(target.tile());
            var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

            Chain.bound(null).runFn(delay, () -> {
                target.hit(hydra, hydra.isEnraged() ? 26 : CombatFactory.calcDamageFromType(hydra, target, CombatType.RANGED), CombatType.MAGIC).checkAccuracy().submit();
            });
        }
    },

    /**
     * The poison pool for the first phase.
     */
    POISON(HydraPhase.GREEN) {
        @Override
        public void executeAttack(AlchemicalHydra hydra, Mob target) {
            var poolAmount = Utils.random(4, 5);
            var pools = getPoolTiles(hydra.baseLocation, target.tile(), poolAmount);
            hydra.animate(8234);

            for (Tile pool : pools) {
                fireProjectileToLocation(hydra, pool, 1644, 50, 90, 0, 5, 55, 0);
            }

            Chain.bound(null).runFn(3, () -> {
                for (Tile pool : pools) {
                    World.getWorld().tileGraphic(1645, pool, 0, 0);
                }
            }).then(1, () -> {
                for (Tile pool : pools) {
                    var graphicId = getPoolGraphic(hydra, pool);
                    World.getWorld().tileGraphic(graphicId, pool, 0, 0);
                }
            }).repeatingTask(1, t -> {
                for (Tile pool : pools) {
                    if (pool == target.tile()) {
                        target.hit(hydra, Utils.random(12), SplatType.POISON_HITSPLAT);
                    }
                }
                if (t.getRunDuration() == 10) {
                    t.stop();
                }
            });
        }
    },

    /**
     * The lightning strike for the second phase.
     */
    LIGHTNING(HydraPhase.BLUE) {
        @Override
        public void executeAttack(AlchemicalHydra hydra, Mob target) {
            hydra.animate(8241);
            Tile base = hydra.baseLocation;

            Tile centralLightningSpot = new Tile(39, 14, 0);
            Tile central = base.transform(centralLightningSpot.x, centralLightningSpot.y);
            ArrayList<Tile> spots = new ArrayList<>(lightningSpots);
            fireProjectileToLocation(hydra, central, 1664, 50, 80, 0, 0, 0, 0);
            int[] ticker = new int[1];
            Chain.bound(null).runFn(2, () -> {
                World.getWorld().tileGraphic(1664, central, 0, 0);
            }).then(1, () -> {
                for (Tile spot : spots) {
                    fireProjectileToLocation(central, base.transform(spot.x, spot.y), 1665, 0, 40, 0, 5, 55, 0);
                }
            }).repeatingTask(1, t -> { //
                if (ticker[0] == 10) {
                    t.stop();
                    return;
                }
                for (Tile spot : spots) {
                    World.getWorld().tileGraphic(1666, base.transform(spot.x, spot.y), 0, 0);
                }
                ArrayList<Tile> newSpots = new ArrayList<>();
                for (Tile spot : new ArrayList<>(spots)) {
                    final Tile curSpot = base.transform(spot.x, spot.y);
                    if (curSpot.equals(target.tile())) {
                        target.hit(hydra, Utils.random(20), SplatType.POISON_HITSPLAT);
                        if (target.isPlayer()) {
                            target.message("<col=ff0000>The electricity temporarily paralyzes you!");
                            target.stun(8);
                        }
                    } else {
                        final Direction direction = Direction.getDirection(curSpot, target.tile());
                        Tile newSpot = spot.transform(direction.x, direction.y);

                        newSpots.add(newSpot);

                    }
                }
                // visual debug
                /*ArrayList<GroundItem> markers = new ArrayList<>(1);
                for (Tile step : newSpots) {
                    GroundItem marker = new GroundItem(new Item(ItemIdentifiers.VIAL, 1), new Tile(base.transform(step.x, step.y).x,
                        base.transform(step.x, step.y).y, hydra.getZ()), null);
                    GroundItemHandler.createGroundItem(marker);
                    markers.add(marker);
                }
                Task.runOnceTask(1, c -> {
                    markers.forEach(GroundItemHandler::sendRemoveGroundItem);
                });*/
                spots.clear();
                spots.addAll(newSpots);
                ticker[0]++;
            }); // looks fine actually
        }
    },

    /**
     * The wall of fire for the third phase.
     */
    FIRE_WALL(HydraPhase.RED) {
        @Override
        public void executeAttack(AlchemicalHydra hydra, Mob target) {
            //hydra.lockNoAttack();
            hydra.face(null);
            Tile base = hydra.baseLocation;
            hydra.walkAndWait(base.transform(hydraSpawnLoc.x, hydraSpawnLoc.y), () -> {
                FireWallSpots fireWallSpots = getWallFireSpots(target.tile(), base);
                ArrayList<Tile> spotOffsets = fireWallSpots.spots;
                ArrayList<Tile> spots = new ArrayList<>();
                int[] ticker = new int[2];
                Chain.bound(null).runFn(1, () -> {
                    Tile faceSpot = base.transform((spotOffsets.get(0).x + spotOffsets.get(1).x) / 2, (spotOffsets.get(0).y + spotOffsets.get(1).y) / 2);
                    hydra.face(faceSpot);
                    hydra.animate(hydra.getAttackAnim());
                    for (int i = 0; i <= 1; i++) {
                        for (Tile spot : fireWallSpots.projectileSpots) {
                            for (int x = fireWallSpots.projectileSpots.get(i * 2).x; x < fireWallSpots.projectileSpots.get(i * 2 + 1).x; x++) {
                                for (int y = fireWallSpots.projectileSpots.get(i * 2).y; y < fireWallSpots.projectileSpots.get(i * 2 + 1).y; y++) {
                                    fireProjectileToLocation(hydra, base.transform(x, y), 1667, 45, 55, 15, 15, 55, 0);
                                }
                            }
                        }
                    }
                }).runFn(1, () -> {
                    for (Tile spot : spots) {
                        if (target.tile() == spot) {
                            target.hit(hydra, Utils.random(20),0);
                        }
                    }
                }).runFn(1, () -> {
                    for (int i = 0; i <= 1; i++) {
                        for (Tile spot : spots) {
                            if (target.tile() == spot) {
                                target.hit(hydra, Utils.random(20),0);
                            }
                        }

                        fireWallSpots.spawnFireWall(base, i, spots);
                    }
                }).repeatingTask(1, t -> {
                    if (t.getRunDuration() == 2) {
                        t.stop();
                        return;
                    }
                    for (Tile spot : spots) {
                        if (target.tile() == spot) {
                            target.hit(hydra, Utils.random(20),0);
                        }
                    }

                    hydra.face(target.tile());
                    Tile startMovingFire = new Tile(Utils.random(spotOffsets.get(0).x, spotOffsets.get(1).x), Utils.random(spotOffsets.get(0).y, Utils.random(spotOffsets.get(1).y)));
                    hydra.animate(hydra.getAttackAnim());
                    fireProjectileToLocation(hydra, base.transform(startMovingFire.x, startMovingFire.y), 1667, 45, 50, 0, 5, 55, 0);
                    for (Tile spot : spots) {
                        if (target.tile() == spot) {
                            target.hit(hydra, Utils.random(20),0);
                        }
                    }

                    spots.add(base.transform(startMovingFire.x, startMovingFire.y, 0));
                    for (int i = 0; i < fireWallDuration; i++) {
                        for (Tile spot : spots) {
                            if (target.tile() == spot) {
                                target.hit(hydra, Utils.random(20),0);
                            }
                        }
                        if (i < 16) {
                            if (base.transform(startMovingFire.x, startMovingFire.y) == target.tile()) {
                                target.hit(hydra, Utils.random(20),0);
                            } else {
                                startMovingFire = getMoveLocation(base, startMovingFire, target.tile());
                                spots.add(startMovingFire);
                                World.getWorld().tileGraphic(1668, base.transform(startMovingFire.x, startMovingFire.y), 0, 0);
                            }
                        } else if (i == 16) {
                            hydra.unlock();
                        }
                    }
                });
            });
        }
    },

    /**
     * The poison pool for the final and enraged version.
     */
    ENRAGED_POISON(HydraPhase.ENRAGED) {
        @Override
        public void executeAttack(AlchemicalHydra hydra, Mob target) {
            var poolAmount = Utils.randomFloat() < 0.6 ? 1 : Utils.random(4, 5);
            var pools = getPoolTiles(hydra.baseLocation, target.tile(), poolAmount);
            hydra.animate(8234);

            for (Tile pool : pools) {
                fireProjectileToLocation(hydra, pool, 1644, 50, 90, 0, 5, 55, 0);
            }

            Chain.bound(null).runFn(3, () -> {
                for (Tile pool : pools) {
                    World.getWorld().tileGraphic(1645, pool, 0, 0);
                }
            }).then(1, () -> {
                for (Tile pool : pools) {
                    var graphicId = getPoolGraphic(hydra, pool);
                    World.getWorld().tileGraphic(graphicId, pool, 0, 0);
                }
            }).repeatingTask(1, t -> {
                for (Tile pool : pools) {
                    if (pool == target.tile()) {
                        target.hit(hydra, Utils.random(12), SplatType.POISON_HITSPLAT);
                    }
                }
                if (t.getRunDuration() == 10) {
                    t.stop();
                }
            });
        }
    };

    public HydraPhase phaseRequired;

    HydraAttacks(HydraPhase phaseRequired) {
        this.phaseRequired = phaseRequired;
    }

    /**
     * Executes the hydra's attack.
     */
    public abstract void executeAttack(AlchemicalHydra hydra, Mob target);

    /**
     * Fires a projectile from the hydra to the target.
     */
    void fireProjectileToEntity(AlchemicalHydra hydra, Mob target, int projectile, int delay) {
        var center = Utils.getCenterLocation(hydra);
        var dir = Direction.of(target.tile().x - center.x, target.tile().y - center.y);
        var from = center.transform(dir.x * 2, dir.y * 2);
        var speed = Utils.getSpeedModifier(from, target.tile());
        new Projectile(hydra, target, projectile, speed, delay, 55, 25, 0, 15, 15).sendProjectile();
    }

    /**
     * Fires a projectile from the hydra to a tile.
     */
    void fireProjectileToLocation(AlchemicalHydra hydra, Tile tile, int projectile, int delay, int projectileSpeed, int angle, int stepness, int startHeight, int endHeight) {
        var center = Utils.getCenterLocation(hydra);
        var dir = Direction.of(tile.x - center.x, tile.y - center.y);
        var from = center.transform(dir.x * 2, dir.y * 2);
        new Projectile(from, tile, hydra.getProjectileLockonIndex(), projectile, projectileSpeed, delay, startHeight, endHeight, 0, angle, stepness).sendProjectile();
    }

    /**
     * Fires a projectile from a tile to another.
     */
    void fireProjectileToLocation(Tile from, Tile to, int projectile, int delay, int projectileSpeed, int angle, int stepness, int startHeight, int endHeight) {
        new Projectile(from, to, 0, projectile, projectileSpeed, delay, startHeight, endHeight, 0, angle, stepness).sendProjectile();
    }
}
