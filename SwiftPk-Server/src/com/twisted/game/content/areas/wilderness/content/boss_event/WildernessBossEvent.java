package com.twisted.game.content.areas.wilderness.content.boss_event;

import com.twisted.game.content.announcements.ServerAnnouncements;
import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.droptables.ScalarLootTable;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.twisted.game.content.collection_logs.data.LogType.BOSSES;
import static com.twisted.util.CustomNpcIdentifiers.BRUTAL_LAVA_DRAGON_FLYING;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.NpcIdentifiers.*;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 03, 2020
 */
public class WildernessBossEvent {

    private static final Logger logger = LogManager.getLogger(WildernessBossEvent.class);

    private static final WildernessBossEvent INSTANCE = new WildernessBossEvent();

    public static WildernessBossEvent getINSTANCE() {
        return INSTANCE;
    }

    public Optional<Npc> getActiveNpc() {
        return activeNpc;
    }

    public BossEvent getActiveEvent() {
        return activeEvent;
    }

    /**
     * An array of possible boss spawns. Chosen at random when a boss spawns.
     */
    private static final Tile[] POSSIBLE_SPAWNS = {
        /*  new Tile(3167, 3757),//level 30 wild
          new Tile(3166, 3832),//level 40 wild
          new Tile(3073, 3687),//level 21 wild
          new Tile(3194,3951),//level 54 wild
          new Tile(2963,3819),//level 38 wild*/
        new Tile(3077, 3687),//outside bandit camp
        new Tile(2966, 3403)//outside falador entrance

    };

    public static Tile currentSpawnPos;

    /**
     * The interval at which server-wide Wilderness events occur.
     * Whilst in production mode every hour otherwise every 30 seconds.
     */
    public static final int BOSS_EVENT_INTERVAL = 6000;

    /**
     * The active event being ran in the Wilderness.
     */
    private BossEvent activeEvent = BossEvent.NOTHING;

    /**
     * The rotation of events, executed in sequence.
     */
    private static final BossEvent[] EVENT_ROTATION = {BossEvent.ZOMBIES_CHAMPION, BossEvent.CORRUPTED_HUNLEFF, BossEvent.TEKTON};

    public static boolean ANNOUNCE_5_MIN_TIMER = false;

    /**
     * The NPC reference for the active event.
     */
    private Optional<Npc> activeNpc = Optional.empty();

    public void bossDeath(Mob mob) {
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to the world boss!"));
            // Only people nearby are rewarded. This is to avoid people 'poking' the boss to do some damage
            // without really risking being there.
            if (mob.tile().isWithinDistance(player.tile(), 10) && hits.getDamage() >= 1) {
                Npc npc = null;
                if (activeNpc.isPresent()) {
                    npc = activeNpc.get();
                }

                if (npc == null) {
                    return;
                }

                //Always drops
                if (npc.id() == SKOTIZO || npc.id() == TEKTON_7542) {
                    GroundItemHandler.createGroundItem(new GroundItem(new Item(ASHES), npc.tile(), player));
                }

                if (npc.id() == ZOMBIES_CHAMPION) {
                    GroundItemHandler.createGroundItem(new GroundItem(new Item(BIG_BONES), npc.tile(), player));
                }

                if (npc.id() == BRUTAL_LAVA_DRAGON_FLYING) {
                    GroundItemHandler.createGroundItem(new GroundItem(new Item(LAVA_DRAGON_BONES), npc.tile(), player));
                }

                //Always drop random BM
                GroundItemHandler.createGroundItem(new GroundItem(new Item(BLOOD_MONEY, World.getWorld().random(10_000, 15_000)), npc.tile(), player));

                //Always log kill timers
                player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);

                //Always increase kill counts
                player.getBossKillLog().addKill(npc);

                DailyTaskManager.increase(DailyTasks.WORLD_BOSS, player);

                //Random drop from the table
                ScalarLootTable table = ScalarLootTable.forNPC(npc.id());
                if (table != null) {
                    Item reward = table.randomItem(World.getWorld().random());
                    if (reward != null) {
                        player.message("You received a drop roll from the table for dealing more then 100 damage!");
                        //System.out.println("Drop roll for "+player.getUsername()+" for killing world boss "+npc.def().name);

                        // bosses, find npc ID, find item ID
                        BOSSES.log(player, npc.id(), reward);

                        //Niffler doesn't loot world boss loot
                        GroundItemHandler.createGroundItem(new GroundItem(reward, npc.tile(), player));
                        ServerAnnouncements.tryBroadcastDrop(player, npc, reward);

                        Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " got drop item " + reward.toString(), "npcdrops");
                    }
                }
            }
        });

        //Dissmiss broadcast when boss has been killed.
        World.getWorld().clearBroadcast();
        World.getWorld().sendWorldMessage("<col=6a1a18><img=1877> " + activeEvent.description + " has been killed. It will respawn shortly.");
    }

    public LocalDateTime last = LocalDateTime.now().minus((long) (BOSS_EVENT_INTERVAL * 0.6d), ChronoUnit.SECONDS);
    public LocalDateTime next = LocalDateTime.now().plus((long) (BOSS_EVENT_INTERVAL * 0.6d), ChronoUnit.SECONDS);

    public static void onServerStart() {
        // every 60 mins
        TaskManager.submit(new WildernessBossEventTask());
    }

    public String timeTill(boolean displaySeconds) {
        LocalDateTime now = LocalDateTime.now();
        long difference = now.until(next, ChronoUnit.SECONDS);
        if (difference < 60 && displaySeconds) {
            return difference + " seconds";
        }
        difference = now.until(next, ChronoUnit.MINUTES);
        if (difference <= 2) {
            return 1 + difference + " minutes";
        } else if (difference <= 59) {
            return difference + " minutes";
        } else {
            return (difference / 60) + "h " + difference % 60 + "min";
        }
    }

    boolean nextIsGrim;
    int lastEvent = 0;

    public void startBossEvent() {
        // First despawn the npc if existing
        terminateActiveEvent(true);

        if (nextIsGrim) {
            nextIsGrim = false;
            activeEvent = BossEvent.GRIM;
        } else {
            if (++lastEvent > EVENT_ROTATION.length - 1) // reset when its at the end
                lastEvent = 0;
            activeEvent = EVENT_ROTATION[lastEvent];
            nextIsGrim = true;
        }

        // Only if it's an actual boss we spawn an NPC.
        if (activeEvent != BossEvent.NOTHING) {
            last = LocalDateTime.now();
            next = LocalDateTime.now().plus((long) (BOSS_EVENT_INTERVAL * 0.6d), ChronoUnit.SECONDS);
            // see you can see constructors with ctrl+shift+space
            Tile tile = POSSIBLE_SPAWNS[new SecureRandom().nextInt(POSSIBLE_SPAWNS.length)];
            currentSpawnPos = tile;
            ANNOUNCE_5_MIN_TIMER = false;

            Npc boss = new Npc(activeEvent.npc, tile);
            boss.respawns(false);
            boss.walkRadius(1);
            boss.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 1);
            World.getWorld().registerNpc(boss);

            this.activeNpc = Optional.of(boss);

            if (WildernessArea.inWilderness(boss.tile())) {
                World.getWorld().sendWorldMessage("<col=6a1a18><img=1877> " + activeEvent.description + " has been spotted " + activeEvent.spawnLocation(boss.tile()) + " in level " + WildernessArea.wildernessLevel(boss.tile()) + " Wild!");
                World.getWorld().sendWorldMessage("<col=6a1a18>It despawns in 60 minutes. Hurry!");

                // Broadcast it
                World.getWorld().sendBroadcast("<img=1877>" + activeEvent.description + " has been spotted " + activeEvent.spawnLocation(boss.tile()) + " in level " + WildernessArea.wildernessLevel(boss.tile()) + " Wild!");


            } else {
                World.getWorld().sendWorldMessage("<col=6a1a18><img=1877> " + activeEvent.description + " has been spotted " + activeEvent.spawnLocation(boss.tile()) + "!");
                World.getWorld().sendWorldMessage("<col=6a1a18>It despawns in 60 minutes. Hurry!");

                // Broadcast it
                World.getWorld().sendBroadcast("<img=1100>" + activeEvent.description + " has been spotted " + activeEvent.spawnLocation(boss.tile()) + "!");
            }
        }

    }

    public void terminateActiveEvent(boolean force) {
        if (activeEvent != BossEvent.NOTHING) {
            boolean despawned = false;
            for (Npc n : World.getWorld().getNpcs()) {
                if (n != null && n.id() == activeEvent.npc && (n.hp() > 0 || force)) {
                    n.stopActions(true);
                    World.getWorld().unregisterNpc(n);
                    despawned = true;
                }
            }
            ANNOUNCE_5_MIN_TIMER = false;
            this.activeNpc = Optional.empty();

            if (despawned) {
                currentSpawnPos = null; // reset current pos
                logger.info("The wilderness event boss has been despawned");
                World.getWorld().sendBroadcast(activeEvent.description + " has despawned");
            }
        }
    }
}
