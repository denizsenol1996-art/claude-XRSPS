package com.twisted.game.content.minigames.impl.fight_caves;

import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.minigames.Minigame;
import com.twisted.game.content.minigames.MinigameManager.ItemRestriction;
import com.twisted.game.content.minigames.MinigameManager.ItemType;
import com.twisted.game.content.minigames.MinigameManager.MinigameType;
import com.twisted.game.task.Task;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.dialogue.Expression;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Color;
import com.twisted.util.NpcIdentifiers;

import java.util.HashSet;
import java.util.Set;

import static com.twisted.util.ItemIdentifiers.FIRE_CAPE;
import static com.twisted.util.NpcIdentifiers.*;
import static com.twisted.util.Utils.randomElement;

/**
 * Handles the fight caves minigames
 *
 * @author 2012 <http://www.rune-server.org/members/dexter+morgan/>
 */
public class FightCavesMinigame extends Minigame {

    /**
     * The starting wave
     */
    private int wave;

    /**
     * The amount to kill
     */
    private int toKill;

    /**
     * The total killed
     */
    private int totalKilled;

    /**
     * Represents the fight cave minigame
     *
     * @param wave   the wave
     */
    public FightCavesMinigame(int wave) {
        this.wave = wave;
        this.toKill = 0;
        this.totalKilled = 0;
    }

    public final static Tile JAD_SPAWN = new Tile(2401,5084);

    /**
     * The spawn coordinates
     */
    public final static Tile[] COORDINATES = {new Tile(2416, 5083), new Tile(2403, 5070),
        new Tile(2380, 5071), new Tile(2379, 5105), new Tile(2400, 5084)};

    /**
     * The possition outside the cave
     */
    public static final Tile OUTSIDE = new Tile(2438, 5169, 0);

    /**
     * All the 63 waves
     */
    private static final int[][] WAVES = {{TZKIH_3116}, {TZKIH_3116, TZKIH_3116}, {TZKEK_3119},
        {TZKEK_3119, TZKIH_3116}, {TZKEK_3119, TZKIH_3116, TZKIH_3116}, {TZKEK_3119, TZKEK_3119},
        {TOKXIL_3121}, {TOKXIL_3121, TZKIH_3116}, {TOKXIL_3121, TZKIH_3116, TZKIH_3116},
        {TOKXIL_3121, TZKEK_3119}, {TOKXIL_3121, TZKEK_3119, TZKIH_3116},
        {TOKXIL_3121, TZKEK_3119, TZKIH_3116, TZKIH_3116}, {TOKXIL_3121, TZKEK_3119, TZKEK_3119},
        {TOKXIL_3121, TOKXIL_3121}, {YTMEJKOT_3124}, {YTMEJKOT_3124, TZKIH_3116},
        {YTMEJKOT_3124, TZKIH_3116, TZKIH_3116}, {YTMEJKOT_3124, TZKEK_3119},
        {YTMEJKOT_3124, TZKEK_3119, TZKIH_3116}, {YTMEJKOT_3124, TZKEK_3119, TZKIH_3116, TZKIH_3116},
        {YTMEJKOT_3124, TZKEK_3119, TZKEK_3119}, {YTMEJKOT_3124, TOKXIL_3121},
        {YTMEJKOT_3124, TOKXIL_3121, TZKIH_3116}, {YTMEJKOT_3124, TOKXIL_3121, TZKIH_3116, TZKIH_3116},
        {YTMEJKOT_3124, TOKXIL_3121, TZKEK_3119}, {YTMEJKOT_3124, TOKXIL_3121, TZKEK_3119, TZKIH_3116},
        {YTMEJKOT_3124, TOKXIL_3121, TZKEK_3119, TZKIH_3116, TZKIH_3116},
        {YTMEJKOT_3124, TOKXIL_3121, TZKEK_3119, TZKEK_3119}, {YTMEJKOT_3124, TOKXIL_3121, TOKXIL_3121},
        {YTMEJKOT_3124, YTMEJKOT_3124}, {KETZEK}, {KETZEK, TZKIH_3116},
        {KETZEK, TZKIH_3116, TZKIH_3116}, {KETZEK, TZKEK_3119}, {KETZEK, TZKEK_3119, TZKIH_3116},
        {KETZEK, TZKEK_3119, TZKIH_3116, TZKIH_3116}, {KETZEK, TZKEK_3119, TZKEK_3119},
        {KETZEK, TOKXIL_3121}, {KETZEK, TOKXIL_3121, TZKIH_3116},
        {KETZEK, TOKXIL_3121, TZKIH_3116, TZKIH_3116}, {KETZEK, TOKXIL_3121, TZKEK_3119},
        {KETZEK, TOKXIL_3121, TZKEK_3119, TZKIH_3116},
        {KETZEK, TOKXIL_3121, TZKEK_3119, TZKIH_3116, TZKIH_3116},
        {KETZEK, TOKXIL_3121, TZKEK_3119, TZKEK_3119}, {KETZEK, TOKXIL_3121, TOKXIL_3121},
        {KETZEK, YTMEJKOT_3124}, {KETZEK, YTMEJKOT_3124, TZKIH_3116},
        {KETZEK, YTMEJKOT_3124, TZKIH_3116, TZKIH_3116}, {KETZEK, YTMEJKOT_3124, TZKEK_3119},
        {KETZEK, YTMEJKOT_3124, TZKEK_3119, TZKIH_3116},
        {KETZEK, YTMEJKOT_3124, TZKEK_3119, TZKIH_3116, TZKIH_3116},
        {KETZEK, YTMEJKOT_3124, TZKEK_3119, TZKEK_3119}, {KETZEK, YTMEJKOT_3124, TOKXIL_3121},
        {KETZEK, YTMEJKOT_3124, TOKXIL_3121, TZKIH_3116},
        {KETZEK, YTMEJKOT_3124, TOKXIL_3121, TZKIH_3116, TZKIH_3116},
        {KETZEK, YTMEJKOT_3124, TOKXIL_3121, TZKEK_3119},
        {KETZEK, YTMEJKOT_3124, TOKXIL_3121, TZKEK_3119, TZKIH_3116},
        {KETZEK, YTMEJKOT_3124, TOKXIL_3121, TZKEK_3119, TZKIH_3116, TZKIH_3116},
        {KETZEK, YTMEJKOT_3124, TOKXIL_3121, TZKEK_3119, TZKEK_3119},
        {KETZEK, YTMEJKOT_3124, TOKXIL_3121, TOKXIL_3121}, {KETZEK, YTMEJKOT_3124, YTMEJKOT_3124},
        {KETZEK, KETZEK_3126}, {TZTOKJAD}};

    /* The highest reachable wave. */
    private static final int MAX_WAVE = WAVES.length;

    private final Set<Npc> npcSet = new HashSet<>();

    /**
     * Spawns a wave
     *
     * @param wave the wave
     */
    private void spawnWave(Player player, int wave) {
        Tile lastPos = new Tile(COORDINATES[0].x, COORDINATES[0].y, player.tile().getLevel());

        for (int i = 0; i < WAVES[wave - 1].length; i++) {
            Tile pos = randomElement(COORDINATES);

            if (pos.equals(lastPos.x, lastPos.y)) {
                for (int n = 100; n >= 1; n--) {
                    pos = wave == 63 ? JAD_SPAWN : randomElement(COORDINATES);
                    if (!pos.equals(lastPos.x, lastPos.y)) break;
                }
            }

            lastPos = pos = new Tile(pos.x, pos.y, player.tile().getLevel());
            Npc monster = Npc.of(WAVES[wave - 1][i], pos);
            npcSet.add(monster);
            monster.walkRadius(200);
            monster.respawns(false);
            monster.putAttrib(AttributeKey.MAX_DISTANCE_FROM_SPAWN, 100);
            World.getWorld().registerNpc(monster);

            //player.message(npc.tile().toString()+" vs "+player.tile().toString());
            toKill++;
            Task.runOnceTask(3, t -> {
                monster.setEntityInteraction(player);
                monster.face(player.tile());
                monster.getCombat().attack(player);
            });
        }
        player.message(Color.RED.tag()+"Wave: " + wave);
    }

    public void addNpc(Npc npc) {
        npcSet.add(npc);
    }

    @Override
    public void start(Player player) {
        int level = player.getIndex() * 4;
        player.teleport(new Tile(2401,5089, level));
        player.getPacketSender().sendString(4536, "Wave: " + wave);
        player.getInterfaceManager().openWalkable(4535);

        Npc npc = Npc.of(NpcIdentifiers.TZHAARMEJJAL, new Tile(3222, 3222, 0));
        DialogueManager.npcChat(player, Expression.CALM_TALK, npc.id(),"Good luck, Jal-Yt!");
        spawnWave(player, wave);
    }

    @Override
    public Task getTask(Player player) {
        return null;
    }

    @Override
    public void end(Player player) {
        player.teleport(OUTSIDE);
        if (wave == (MAX_WAVE + 1)) {

            Npc npc = Npc.of(NpcIdentifiers.TZHAARMEJJAL, new Tile(3222, 3222, 0));
            DialogueManager.npcChat(player, Expression.CALM_TALK, npc.id(), "You even defeated TzTok-Jad, I am most impressed!", "Please accept this gift.", "Give cape back to me if you not want it.");

            player.inventory().addOrDrop(new Item(FIRE_CAPE, 1));
            AchievementsManager.activate(player, Achievements.FIGHT_CAVES_I, 1);
            AchievementsManager.activate(player, Achievements.FIGHT_CAVES_II, 1);
        }
        npcSet.forEach(npc -> World.getWorld().unregisterNpc(npc));
        player.setMinigame(null);
    }

    @Override
    public void killed(Player player, Mob entity) {
        if (entity.isNpc()) {
            Npc npc = entity.getAsNpc();
            npcSet.remove(npc);
            if (entity.getAsNpc().id() == NpcIdentifiers.YTHURKOT) {
                return;
            }
            toKill--;
            totalKilled++;
            if (toKill == 0) {
                wave++;
                toKill = 0;
                if (wave == (MAX_WAVE + 1)) {
                    end(player);
                } else {
                    spawnWave(player, wave);
                }
            }
            // Tz-Kek splits itself in two smaller NPCs on death
            if (npc.id() == TZKEK_3119) {
                Npc kek = Npc.of(TZKEK_3120, entity.tile().copy());
                Npc kek2 = Npc.of(TZKEK_3120, entity.tile().copy());
                npcSet.add(kek);
                npcSet.add(kek2);

                World.getWorld().registerNpc(kek);
                World.getWorld().registerNpc(kek2);
            }
        }
    }

    @Override
    public ItemType getType() {
        return ItemType.SAFE;
    }

    @Override
    public ItemRestriction getRestriction() {
        return ItemRestriction.NONE;
    }

    @Override
    public MinigameType getMinigameType() {
        return MinigameType.SAFE_MULTI;
    }

    @Override
    public boolean canTeleportOut() {
        return false;
    }

    @Override
    public boolean hasRequirements(Player player) {
        return true;
    }

}
