package com.twisted.game.world;

import com.twisted.game.content.events.ServerEvent;
import com.twisted.game.world.entity.mob.npc.droptables.ItemRepository;
import com.twisted.game.world.entity.mob.npc.droptables.NpcDropRepository;
import com.twisted.game.world.items.Item;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.twisted.GameServer;
import com.twisted.fs.DefinitionRepository;
import com.twisted.fs.NpcDefinition;
import com.twisted.game.GameConstants;
import com.twisted.game.TimesCycle;
import com.twisted.game.content.areas.burthope.warriors_guild.dialogue.Shanomi;
import com.twisted.game.content.minigames.MinigameManager;
import com.twisted.game.content.skill.impl.fishing.Fishing;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.NodeType;
import com.twisted.game.world.entity.combat.bountyhunter.BountyHunter;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss;
import com.twisted.game.world.entity.masks.updating.NPCUpdating;
import com.twisted.game.world.entity.masks.updating.PlayerUpdating;
import com.twisted.game.world.entity.mob.MobList;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.NpcCombatInfo;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.PlayerPerformanceTracker;
import com.twisted.game.world.entity.mob.sync.GameSyncExecutor;
import com.twisted.game.world.items.ItemWeight;
import com.twisted.game.world.items.container.equipment.EquipmentInfo;
import com.twisted.game.world.items.container.shop.Shop;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.OwnedObject;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.region.Flags;
import com.twisted.game.world.region.Region;
import com.twisted.game.world.region.RegionManager;
import com.twisted.util.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import kotlin.ranges.IntRange;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.twisted.util.NpcIdentifiers.SHANOMI;

/**
 * Represents the world, processing it and its mobs.
 *
 * @author Professor Oak
 * @author lare96
 */
public class World {

    @Getter
    @Setter
    private static Thread worldThread;

    public static boolean isWorldThread() {
        return Thread.currentThread() == worldThread;
    }

    public static boolean TRIPLE_SUMMER_TOKEN_DROPS = false;

    public static boolean SYNCMODE1 = false;
    public final Tile HOME = Tile.of(2028, 3577, 0);
    public final Tile EDGEHOME = Tile.of(3085, 3492, 0);

    private static final Logger logger = LogManager.getLogger(World.class);

    public World() {
        definitionRepository = GameServer.definitions();
        examineRepository = new ExamineRepository(definitionRepository);
    }

    /**
     * World instance.
     */
    private static final World world = new World();

    /**
     * Gets the world instance.
     *
     * @return The world instance.
     */
    public static World getWorld() {
        return world;
    }

    public Int2ObjectMap<Shop> shops = new Int2ObjectOpenHashMap<>();

    public Shop shop(int id) {
        return shops.get(id);
    }

    public static long LAST_FLUSH;

    /**
     * The collection of active {@link Player}s.
     */
    private final MobList<Player> players = new MobList<>(GameConstants.PLAYERS_LIMIT);

    /**
     * The collection of active {@link Npc}s. Be careful when adding NPCs directly to the list without using the queue, try not to bypass the queue.
     */
    private final MobList<Npc> npcs = new MobList<>(GameConstants.NPCS_LIMIT);

    /**
     * The collection of active {@link GameObject}s..
     */
    private final List<GameObject> spawnedObjs = new LinkedList<>();
    private final List<GameObject> removedObjs = new LinkedList<>();

    /**
     * The manager for game synchronization.
     */
    private static final GameSyncExecutor executor = new GameSyncExecutor();

    /**
     * The sections of the World sequence, used for logging successful completion of the World sequence.
     */
    public boolean[] section = new boolean[11];

    private final Calendar calendar = new GregorianCalendar();

    public Calendar getCalendar() {
        return calendar;
    }

    private boolean applyDoubleExperience() {
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return (day == 5 || day == 6 || day == 7) || GameServer.properties().doubleExperienceEvent;
    }

    private boolean applyDoubleSlayerRewardPoints() {
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return (day == 5 || day == 6 || day == 7) || GameServer.properties().doubleSlayerRewardPointsEvent;
    }

    private boolean applyDoubleBM() {
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return (day == 5 || day == 6 || day == 7) || GameServer.properties().doubleBMEvent;
    }

    public boolean doubleVotePoints() {
        return isFirstWeekofMonth() && GameServer.properties().doubleBMEvent;
    }

    private boolean isFirstWeekofMonth() {
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth <= 7;
    }

    /**
     * Timing
     */
    public long currentTick() {
        return cycleCount();
    }

    public long getEnd(long ticks) {
        return currentTick() + ticks;
    }

    public boolean isPast(long end) {
        return currentTick() >= end;
    }

    public int xpMultiplier = applyDoubleExperience() ? 2 : 2;
    public int slayerRewardPointsMultiplier = applyDoubleSlayerRewardPoints() ? 2 : 2;
    public int bmMultiplier = applyDoubleBM() ? 1 : 1;

    private int elapsedTicks;

    private int lastPidUpdateTick;

    private long lastMinuteScan;

    private final DefinitionRepository definitionRepository;

    public DefinitionRepository definitions() {
        return definitionRepository;
    }

    private final ExamineRepository examineRepository;

    public ExamineRepository examineRepository() {
        return examineRepository;
    }

    protected final Map<String, OwnedObject> ownedObjects = Maps.newConcurrentMap();
    public double get() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public int get(int maxRange) {
        return (int) (get() * (maxRange + 1D));
    }

    public int get(int minRange, int maxRange) {
        return minRange + get(maxRange - minRange);
    }

    public <T> T randomTypeOfList(List<T> list) {
        if(list == null || list.size() == 0)
            return null;
        return list.get(new SecureRandom().nextInt(list.size()));
    }

    public static class WorldPerfTracker {
        public long skulls, tasks, login, logout, objects, packets, players,
            npcs, gpi, flush, reset, games;

        // track totals for all 2048 players this cycle
        public PlayerPerformanceTracker allPlayers = new PlayerPerformanceTracker();

        public void reset() {
            skulls = tasks = login = logout = objects = packets = players = npcs =
                gpi = flush = reset = games = 0;
        }

        static final DecimalFormat df = new DecimalFormat("#.##");

        @Override
        public String toString() {
            return breakdown();
        }

        public String breakdown() {
            if (!TimesCycle.BENCHMARKING_ENABLED) return "-";
            StringBuilder sb2 = new StringBuilder();
            if ((int) (1. * skulls / 1_000_000.) > 0)
                sb2.append(String.format("skulls:%s ms, ", df.format(1. * skulls / 1_000_000.)));
            if ((int) (1. * tasks / 1_000_000.) > 0)
                sb2.append(String.format("tasks:%s ms, ", df.format(1. * tasks / 1_000_000.)));
            if ((int) (1. * login / 1_000_000.) > 0)
                sb2.append(String.format("login:%s ms, ", df.format(1. * login / 1_000_000.)));
            if ((int) (1. * objects / 1_000_000.) > 0)
                sb2.append(String.format("objects:%s ms, ", df.format(1. * objects / 1_000_000.)));
            if ((int) (1. * packets / 1_000_000.) > 0)
                sb2.append(String.format("packets:%s ms, ", df.format(1. * packets / 1_000_000.)));
            if ((int) (1. * players / 1_000_000.) > 0)
                sb2.append(String.format("players:%s ms, ", df.format(1. * players / 1_000_000.)));
            if ((int) (1. * npcs / 1_000_000.) > 0)
                sb2.append(String.format("npcs:%s ms, ", df.format(1. * npcs / 1_000_000.)));
            if ((int) (1. * gpi / 1_000_000.) > 0)
                sb2.append(String.format("gpi:%s ms, ", df.format(1. * gpi / 1_000_000.)));
            if ((int) (1. * flush / 1_000_000.) > 0)
                sb2.append(String.format("flush:%s ms, ", df.format(1. * flush / 1_000_000.)));
            if ((int) (1. * reset / 1_000_000.) > 0)
                sb2.append(String.format("reset:%s ms, ", df.format(1. * reset / 1_000_000.)));
            if ((int) (1. * games / 1_000_000.) > 0)
                sb2.append(String.format("games:%s ms, ", df.format(1. * games / 1_000_000.)));

            if (!sb2.toString().isEmpty())
                return sb2 + allPlayers.breakdown();
            return "nil";
        }
    }

    List<Integer> playerRenderOrder;

    List<Integer> npcRenderOrder;

    public WorldPerfTracker benchmark = new WorldPerfTracker();


    public void sequence() {
        resetSection();
        readPlayerPackets();
        processTasks();
        processObjects();
        readNpcs();
        readPlayers();
        processGpi();
        flushEntities();
        incrementElapsedTicks();
    }

    /**
     * Resets the section array to false.
     */
    private void resetSection() {
        Arrays.fill(section, false);
    }

    /**
     * Shuffles the player render order if enabled in the server properties.
     */
    private void shufflePlayerRenderOrder() {
        if (GameServer.properties().enablePidShuffling) {
            long pidShuffleCounter = World.getWorld().random().nextInt(100);
            long pidIntervalTicks = GameServer.properties().pidIntervalTicks;
            if (pidShuffleCounter % pidIntervalTicks == 0) {
                players.shuffleRenderOrder();
            }
        }
    }

    /**
     * Processes all scheduled tasks.
     */
    private static void processTasks() {
        try {
            TaskManager.sequence();
            MinigameManager.onTick();
            ServerEvent.tick();
        } catch (Exception e) {
            logger.error("Error occurred while processing tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Processes all objects owned by the game.
     */
    private void processObjects() {
        for (OwnedObject object : ownedObjects.values()) {
            if (object != null) {
                try {
                    object.tick();
                } catch (Exception e) {
                    logger.error("Error occurred while processing object: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Reads packets from all connected players.
     */
    private void readPlayerPackets() {
        for (Player player : players) {
            if (player != null) {
                try {
                    player.getSession().read();
                    player.getSession().handleQueuedPackets();
                } catch (Exception e) {
                    logger.error("Error occurred while reading player packets: " + e.getMessage());
                    e.printStackTrace();
                    World.getWorld().getPlayers().remove(player);
                }
            }
        }
    }

    /**
     * Reads and processes all NPCs in the game.
     */
    private void readNpcs() {
        for (Npc npc : npcs) {
            if (npc != null && !npc.hidden() && checkIndex(npc.getIndex(), NodeType.NPC)) {
                try {
                    npc.sequence();
                    npc.inViewport(false);
                    npc.processed = true;
                } catch (Exception e) {
                    logger.error("Error occurred while processing NPC: " + e.getMessage());
                    e.printStackTrace();
                    World.getWorld().unregisterNpc(npc);
                }
            }
        }
    }

    /**
     * Reads and processes all players in the game.
     */
    private void readPlayers() {
        for (Player player : players) {
            if (player != null && checkIndex(player.getIndex(), NodeType.PLAYER)) {
                try {
                    player.sequence();
                    player.processed = true;
                } catch (Exception e) {
                    logger.error("Error occurred while processing player: " + e.getMessage());
                    e.printStackTrace();
                    World.getWorld().getPlayers().remove(player);
                }
            }
        }
    }

    /**
     * Processes game packet updates for all players and NPCs.
     */
    private void processGpi() {
        for (Player player : players) {
            if (player != null && checkIndex(player.getIndex(), NodeType.PLAYER)) {
                try {
                    PlayerUpdating.update(player);
                } catch (Exception e) {
                    logger.error("Error occurred while processing GPI: {}", e.getMessage(), e);
                    World.getWorld().getPlayers().remove(player);
                }
            }
        }
        for (Player player : players) {
            if (player != null && checkIndex(player.getIndex(), NodeType.PLAYER)) {
                try {
                    NPCUpdating.update(player);
                } catch (Exception e) {
                    logger.error("Error occurred while processing GPI: {}", e.getMessage(), e);
                    World.getWorld().getPlayers().remove(player);
                }
            }
        }
    }

    /**
     * Flushes all entities that need to be updated.
     */
    private void flushEntities() {
        flushNpcs();
        flushPlayers();
    }

    /**
     * Flushes all NPCs that need to be updated.
     */
    private void flushNpcs() {
        for (Npc npc : npcs) {
            if (npc != null && checkIndex(npc.getIndex(), NodeType.NPC)) {
                try {
                    npc.resetUpdating();
                    npc.clearAttrib(AttributeKey.CACHED_PROJECTILE_STATE);
                    npc.performance.reset();
                    npc.processed = false;
                } catch (Exception e) {
                    logger.error("Error occurred while flushing NPC: " + e.getMessage());
                    e.printStackTrace();
                    World.getWorld().unregisterNpc(npc);
                }
            }
        }
    }

    /**
     * Flushes all players that need to be updated.
     */
    private void flushPlayers() {
        for (Player player : players) {
            if (player != null && checkIndex(player.getIndex(), NodeType.PLAYER)) {
                try {
                    player.resetUpdating();
                    player.clearAttrib(AttributeKey.CACHED_PROJECTILE_STATE);
                    player.setCachedUpdateBlock(null);
                    player.getSession().flush();
                    player.processed = false;
                } catch (Exception e) {
                    logger.error("Error occurred while flushing player: " + e.getMessage());
                    e.printStackTrace();
                    World.getWorld().getPlayers().remove(player);
                }
            }
        }
    }

    /**
     * Increments the elapsed ticks counter.
     */
    private void incrementElapsedTicks() {
        elapsedTicks++;
    }

    protected boolean checkIndex(int index, NodeType type) {
        return type == NodeType.PLAYER ? getPlayers().get(index) != null : getNpcs().get(index) != null;
    }

    /**
     * Gets a player by their username, using {@link String#equalsIgnoreCase(String)}. case-sensitive usercase Does Not enforce uniqueness.
     * <br> by using ignoreCase we can be sure we get a player even if they have a capitalized name like HITEST.
     *
     * @param username The username of the player.
     * @return The player with the matching username.
     */
    public Optional<Player> getPlayerByName(String username) {
        return players.search(p -> p != null && p.getUsername().equalsIgnoreCase(username));
    }

    public Optional<Player> getPlayerByUUID(long uuid) {
        return players.search(p -> p != null && p.getUID() == uuid);
    }

    public void clearBroadcast() {
        sendBroadcast("");
    }

    public void sendBroadcast(String broadcast) {
        sendBroadcast(broadcast, false, "no_link");
    }

    public void sendBroadcast(String broadcast, boolean hasUrl, String link) {
        if (hasUrl)
            World.getWorld().sendWorldMessage("osrsbroadcast##" + broadcast + "%%" + link);
        else
            World.getWorld().sendWorldMessage("osrsbroadcast##" + broadcast + "%%" + "no_link");
    }

    /**
     * Broadcasts a message to all players in the game.
     *
     * @param message The message to broadcast.
     */
    public void sendWorldMessage(String message) {
        players.forEach(p -> p.getPacketSender().sendMessage(message));
    }

    /**
     * Broadcasts a message to all staff-members in the game.
     *
     * @param message The message to broadcast.
     */
    public void sendStaffMessage(String message) {
        players.stream().filter(p -> !Objects.isNull(p) && p.getPlayerRights().isStaffMember(p)).forEach(p -> p.getPacketSender().sendMessage(message));
    }

    /**
     * Looks for the amount of players inside the wilderness area
     *
     * @return players_in_wilderness
     */
    public int getPlayersInWild() {
        int players_in_wilderness = 0;

        for (Player player : BountyHunter.PLAYERS_IN_WILD) {
            if (player != null) {
                players_in_wilderness++;
            }
        }

        return players_in_wilderness;
    }

    public MobList<Player> getPlayers() {
        return players;
    }

    public int getRegularPlayers() {
        int regular_players = 0;

        for (Player player : players) {
            if (player != null && !player.getPlayerRights().isStaffMember(player)) {
                regular_players++;
            }
        }

        return regular_players;
    }

    public MobList<Npc> getNpcs() {
        return npcs;
    }

    public List<GameObject> getSpawnedObjs() {
        return spawnedObjs;
    }

    public List<GameObject> getRemovedObjs() {
        return removedObjs;
    }

    public com.twisted.game.service.LoginService ls = new com.twisted.game.service.LoginService();

    public int cycleCount() {
        return elapsedTicks;
    }

    public boolean registerNpc(Npc npc) {
        npcs.add(npc);
        Tile.occupy(npc);
        npc.setNeedsPlacement(true);
        npc.spawnStack = new Throwable().getStackTrace()[1].toString();
        return true;
    }

    public void unregisterNpc(Npc npc) {
        npcs.remove(npc);
    }

    private EquipmentInfo equipmentInfo;

    public EquipmentInfo equipmentInfo() {
        return equipmentInfo;
    }

    public void loadEquipmentInfo() {
        equipmentInfo = new EquipmentInfo(
            new File("data/list/equipment_info.json"),
            new File("data/list/renderpairs.txt"),
            new File("data/list/bonuses.json"),
            new File("data/list/weapon_types.txt"),
            new File("data/list/weapon_speeds.txt"));
    }

    public NpcCombatInfo combatInfo(int id) {
        return id > combatInfo.length - 1 ? null : combatInfo[id];
    }

    private NpcCombatInfo[] combatInfo;

    public void loadNpcCombatInfo() {
        combatInfo = new NpcCombatInfo[definitionRepository.total(NpcDefinition.class)];

        int total = 0;
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new GsonPropertyValidator()).create();
        File defs = new File("data/combat/npc");
        for (File def : Objects.requireNonNull(defs.listFiles())) {
            if (def.getName().endsWith(".json")) {
                NpcCombatInfo[] s = null;
                try {
                    s = gson.fromJson(new FileReader(def), NpcCombatInfo[].class);
                } catch (JsonParseException e) {
                    throw new RuntimeException("Failed to parse npc combat def: " + def.getAbsolutePath() + " (" + e.getMessage() + ")");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (s == null)
                    continue;

                int entryIndex = 0;
                for (NpcCombatInfo cbInfo : s) {
                    if (cbInfo == null)
                        continue;

                    if (cbInfo.ids == null) {
                        logger.error("Failed to parse entry {} in NpcCombatInfo {} due to missing field 'ids'", entryIndex, def.getName());
                        continue;
                    }

                    // Store original stats to restore after respawning.
                    cbInfo.originalStats = cbInfo.stats.clone();
                    cbInfo.originalBonuses = cbInfo.bonuses.clone();

                    // Resolve scripts
                    if (cbInfo.scripts != null) {
                        cbInfo.scripts.resolve();
                    }

                    // Insert the combat info reference into the array at the index respective to the concerning npc ids
                    for (int i : cbInfo.ids) {
                        combatInfo[i] = cbInfo;
                    }

                    entryIndex++;
                }
                total += s.length;
            }
        }
        logger.info("Loaded {} NPC combat info sheets.", total);
    }

    public void loadItemSpawns(File dir) {
        int total = 0;
        Gson gson = JGson.get();
        for (File spawn : Objects.requireNonNull(dir.listFiles())) {
            if (spawn.getName().endsWith(".json")) {
                try {
                    ItemSpawn[] s = gson.fromJson(new FileReader(spawn), ItemSpawn[].class);
                    for (ItemSpawn sp : s) {
                        if (sp == null) continue;

                        Tile spawnTile = new Tile(sp.x, sp.y, sp.level);
                        GroundItem item = new GroundItem(new Item(sp.id, sp.amount), spawnTile, null);
                        item.respawns(true).respawnTimer(sp.delay);
                        GroundItemHandler.createGroundItem(item);
                    }
                    total += s.length;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (spawn.isDirectory()) {
                loadItemSpawns(spawn);
            }
        }
        logger.info("Loaded {} item spawns.", total);
    }

    public void loadDrops() {
        ItemRepository.load();
        NpcDropRepository.loadAll(new File("data/combat/drops"));
    }

    public static void loadNpcSpawns(File dir) {
        long start = System.currentTimeMillis();
        Gson gson = new Gson();

        for (File spawn : Objects.requireNonNull(dir.listFiles())) {
            if (spawn.getName().endsWith(".json")) {
                try {
                    NpcSpawn[] s = gson.fromJson(new FileReader(spawn), NpcSpawn[].class);

                    for (NpcSpawn sp : s) {
                        if (sp == null) continue;

                        Tile spawnTile = new Tile(sp.x, sp.y, sp.z);
                        Npc npc = Npc.of(sp.id, spawnTile);
                        npc.spawnDirection(sp.dir());
                        npc.walkRadius(sp.walkRange);
                        npc.ancientSpawn(sp.ancientSpawn);

                        if (npc.id() == SHANOMI) {
                            Shanomi.shoutMessage(npc);
                        }

                        // successfully added to game world
                        KrakenBoss.onNpcSpawn(npc);

                        if (npc.id() == NpcIdentifiers.VENENATIS_6610) {
                            npc.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 30);
                        }

                        // Set the max return to spawnpoint distance for gwd room npcs
                        if (npc.def().gwdRoomNpc) {
                            npc.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 40);
                        }
                        if (npc.id() == CustomNpcIdentifiers.SEREN_Z) {
                            npc.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 1);
                        }

                        // successfully added to game world
                        World.getWorld().registerNpc(npc);
                    }
                } catch (JsonParseException e) {
                    throw new RuntimeException("Failed to parse npc spawn: " + spawn.getAbsolutePath() + " (" + e + ")");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (spawn.isDirectory()) {
                loadNpcSpawns(spawn);
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        logger.info("Loaded definitions for ./data/map/npcs. It took {}ms.", elapsed);
    }

    public void postLoad() {
        try {
            loadEquipmentInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            loadNpcCombatInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            loadNpcSpawns(new File("data/map/npcs"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Fishing.respawnAllSpots(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logger.info("Loaded {} NPC spawns.", npcs.size());

        try {
            loadItemSpawns(new File("data/map/items"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ItemWeight.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            loadDrops();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int meleeClip(int absx, int absy, int z) {
        return clipInMap(absx, absy, z, false);
    }

    public int projectileClip(int absx, int absy, int z) {
        return clipInMap(absx, absy, z, true);
    }

    private int clipInMap(int x, int y, int z, boolean projectile) {
        return projectile ? RegionManager.getClippingProj(x, y, z) : RegionManager.getClipping(x, y, z);
    }

    /*
     * checks clip
     */
    public boolean canMoveNPC(int plane, int x, int y, int size) {
        for (int tileX = x; tileX < x + size; tileX++)
            for (int tileY = y; tileY < y + size; tileY++)
                if (getMask(plane, tileX, tileY) != 0)
                    return false;
        return true;
    }

    /*
     * checks clip
     */
    public boolean isNotCliped(int plane, int x, int y, int size) {
        for (int tileX = x; tileX < x + size; tileX++)
            for (int tileY = y; tileY < y + size; tileY++)
                if ((getMask(plane, tileX, tileY) & 2097152) != 0)
                    return false;
        return true;
    }

    public int getMask(int plane, int x, int y) {
        Tile tile = new Tile(x, y, plane);
        int regionId = tile.region();
        Region region = RegionManager.getRegion(regionId);
        if (region == null) return -1;
        return region.getClip(x, y, tile.getLevel());
    }

    public static String clipstr(final int clip) {
        StringBuilder sb = new StringBuilder();
        Flags.getMASKS().forEach((s, integer) -> {
            if ((clip & integer) != 0) {
                sb.append(s).append(",");
            }
        });
        return sb.toString().length() == 0 ? "none" : sb.toString();
    }

    public static String clipstrMethods(Tile tile) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RegionManager.blockedEast(tile) ? "E, " : "");
        stringBuilder.append(RegionManager.blockedNorth(tile) ? "N, " : "");
        stringBuilder.append(RegionManager.blockedSouth(tile) ? "S, " : "");
        stringBuilder.append(RegionManager.blockedWest(tile) ? "W, " : "");
        stringBuilder.append(RegionManager.blockedNorthEast(tile) ? "NE, " : "");
        stringBuilder.append(RegionManager.blockedNorthWest(tile) ? "NW, " : "");
        stringBuilder.append(RegionManager.blockedSouthEast(tile) ? "SE, " : "");
        stringBuilder.append(RegionManager.blockedSouthWest(tile) ? "SW, " : "");
        return "blocked in dirs: " + stringBuilder.toString();
    }

    public Tile randomTileAround(Tile base, int radius) {
        int[][] clip = clipSquare(base.transform(-radius, -radius, 0), radius * 2 + 1);

        for (int i = 0; i < 100; i++) {
            int x = Utils.RANDOM_GEN.get().nextInt(radius * 2 + 1), z = Utils.RANDOM_GEN.get().nextInt(radius * 2 + 1);
            if (clip[x][z] == 0) {
                return base.transform(x - radius, z - radius, 0);
            }
        }

        return base;
    }

    public int[][] clipAround(Tile base, int radius) {
        Tile src = base.transform(-radius, -radius, 0);
        return clipSquare(src, radius * 2 + 1);
    }

    public int[][] clipSquare(Tile base, int size) {
        int[][] clipping = new int[size][size];

        Region active = RegionManager.getRegion(base.region());
        int activeId = base.region();

        for (int x = base.x; x < base.x + size; x++) {
            for (int y = base.y; y < base.y + size; y++) {
                int reg = Tile.coordsToRegion(x, y);
                if (reg != activeId) {
                    activeId = reg;
                    active = RegionManager.getRegion(activeId);
                }

                if (active != null && active.clips[base.level % 4] != null)
                    clipping[x - base.x][y - base.y] = active.clips[base.level % 4][x & 63][y & 63];
            }
        }

        return clipping;
    }

    public int clipAt(int x, int z, int level) {
        return clipAt(new Tile(x, z, level));
    }

    public int clipAt(Tile tile) {
        Region active = RegionManager.getRegion(tile.region());
        return active == null ? 0 : active.clips == null ? 0 : active.clips[tile.level % 4] == null ? 0 : active.clips[tile.level % 4][tile.x & 63][tile.y & 63];
    }

    public int floorAt(Tile tile) {
        Region active = RegionManager.getRegion(tile.region());
        return active == null ? 0 : active.heightMap != null ? active.heightMap[tile.level % 4][tile.x & 63][tile.y & 63] : 0;
    }

    public void tileGraphic2(int id, Tile tile, int height, int delay) {
        players.forEachInRegion(tile.region(), player -> {
            if(player.tile().level == tile.level) {
                player.getPacketSender().sendTileGraphic(id, tile, height, delay);
            }
        });
    }

    public void tileGraphic(int id, Tile tile, int height, int delay) {
        for (Player player : players) {
            if (player == null) continue;
            if (player.getZ() != tile.getZ()) continue;
            if (!player.tile().isViewableFrom(tile)) continue;
            player.getPacketSender().sendTileGraphic(id, tile, height, delay);
        }
    }

    private final Random random = new SecureRandom();

    public Random random() {
        return random;
    }

    /**
     * @param i Maximum - INCLUSIVE!
     * @return Integer between 1 - MAX
     */
    public int random(int i) {
        if (i < 1) {
            return 0;
        }

        return random.nextInt(i + 1);
    }

    public int random(final int min, final int max) {
        final int n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random.nextInt(n + 1));
    }

    public double randomDouble() {
        return random.nextDouble();
    }

    public <T> T random(T[] i) {
        return i[random.nextInt(i.length)];
    }

    public int random(int[] i) {
        return i[random.nextInt(i.length)];
    }

    public int random(IntRange range) {
        return random.nextInt(range.getEndInclusive() - range.getStart() + 1) + range.getStart();
    }

    public boolean rollDie(int dieSides, int chance) {
        return random(dieSides) < chance;
    }

    public boolean rollDie(int maxRoll) {
        return rollDie(maxRoll, 1);
    }

    public Optional<Player> getPlayerByUid(int userId) {
        return players.filter(plr -> plr.getIndex() == userId).findFirst();
    }

    public Player getPlayer(int userId, boolean onlineReq) {
        if (onlineReq) {
            for (Player player : players) {
                if (player != null && player.getIndex() == userId)
                    return player;
            }
        } else {
            for (Player player : players.entities.values()) {
                if (player != null && player.getIndex() == userId)
                    return player;
            }
        }
        return null;
    }

    public Map<String, OwnedObject> getOwnedObjects() {
        return ownedObjects;
    }

    public void registerOwnedObject(OwnedObject object) {
        ownedObjects.put(object.getOwnerUID() + ":" + object.getIdentifier(), object);
    }

    public OwnedObject getOwnedObject(Player owner, String identifier) {
        return ownedObjects.get(owner.getIndex() + ":" + identifier);
    }

    public void deregisterOwnedObject(OwnedObject object) {
        ownedObjects.remove(object.getOwnerUID() + ":" + object.getIdentifier());
    }
}

