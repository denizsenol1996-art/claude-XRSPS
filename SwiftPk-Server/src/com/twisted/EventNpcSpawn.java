package com.twisted;

import com.twisted.game.task.Task;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.game.world.region.RegionManager;
import com.twisted.util.Utils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.twisted.util.CustomNpcIdentifiers.*;

@Slf4j
public class EventNpcSpawn extends Task {

    /**
     * The amount of time in game cycles (600ms) that the event pulses at
     */
    private static final int INTERVAL = Utils.toCyclesOrDefault(1, 1, TimeUnit.SECONDS);

    /**
     * A {@link java.util.Collection} of messages that are to be displayed
     */

    /**
     * The index or position in the list that we're currently at
     */
    final Set<Integer> REGIONS = Set.of
        (
            11832, 12088, 12344, 12600, 12856, 13112,
            13369, 13113, 12857, 1260, 12345, 12089,
            11833, 13370, 13114, 12850, 12602, 12346,
            12090, 11834, 11835, 12091, 12347, 12603,
            12859, 13115, 13371, 13372, 13116, 12860,
            12604, 12348, 12092, 11836, 13117, 12861,
            12605, 12349, 12093, 11837
        );

    final ObjectSet<Tile> tiles = new ObjectOpenHashSet<>();
    final AtomicInteger count = new AtomicInteger(0);
    public static int spawned;
    volatile boolean loaded = false;

    /**
     * Creates a new event to cycle through messages for the entirety of the runtime
     */
    public EventNpcSpawn() {
        super("EventNpcSpawn", INTERVAL);

    }

    @Override
    public void execute() {
        if (spawned >= 350)
            return;

        if (!loaded) {
            for (final int region : REGIONS) {
                int maxIterations = 1000;
                while (count.get() < 64 && maxIterations-- > 0) {
                    final Tile regionTile = Tile.regionToTile(region);
                    final Tile tile = World.getWorld().randomTileAround(regionTile, 32);
                    if (RegionManager.zarosBlock(tile) || ObjectManager.objWithTypeExists(10, tile)
                        || ObjectManager.objWithTypeExists(11, tile)
                        || (World.getWorld().floorAt(tile) & 0x4) != 0)
                        continue;

                    if (tiles.contains(tile))
                        continue;

                    if (tile.x > 3008 && tile.x < 3325 && WildernessArea.inWilderness(tile)) {
                        tiles.add(tile);
                        count.getAndIncrement();
                    }

                    if (count.get() >= 64) {
                        count.getAndSet(0);
                        break;
                    }
                }
            }

            loaded = true;
            log.info("Loaded {} Event Npc Tiles", tiles.size());
        }

        for (Tile tile : tiles) {
            if (spawned >= 350)
                break;

            Npc summerImp = new Npc(SUMMER_IMP, tile);
            summerImp.walkRadius(5);
            summerImp.spawn(false);
            spawned++;
        }
    }
}
