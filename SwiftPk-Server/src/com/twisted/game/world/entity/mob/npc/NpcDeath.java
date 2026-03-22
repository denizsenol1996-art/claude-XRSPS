package com.twisted.game.world.entity.mob.npc;

import com.twisted.EventNpcSpawn;
import com.twisted.GameServer;
import com.twisted.NpcSpawnInWild;
import com.twisted.fs.NpcDefinition;
import com.twisted.game.content.EffectTimer;
import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.announcements.ServerAnnouncements;
import com.twisted.game.content.areas.burthope.warriors_guild.MagicalAnimator;
import com.twisted.game.content.areas.wilderness.content.boss_event.WildernessBossEvent;
import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.twisted.game.content.raids.chamber_of_xeric.great_olm.Phases;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.content.seasonal_events.christmas.SnowMonsterSpawnTask;
import com.twisted.game.content.skill.impl.slayer.Slayer;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.content.skill.impl.slayer.slayer_partner.SlayerPartner;
import com.twisted.game.content.tasks.impl.Tasks;
import com.twisted.game.content.treasure.TreasureRewardCaskets;
import com.twisted.game.content.wilderness.keys.WildernessKeys;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.bountyhunter.EarningPotential;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.kalphite.KalphiteQueenFirstForm;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.kalphite.KalphiteQueenSecondForm;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.vorkath.VorkathState;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness.vetion.VetionMinion;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.zulrah.Zulrah;
import com.twisted.game.world.entity.combat.method.impl.npcs.fightcaves.TzTokJad;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.GwdLogic;
import com.twisted.game.world.entity.combat.method.impl.npcs.hydra.AlchemicalHydra;
import com.twisted.game.world.entity.combat.method.impl.npcs.karuulm.Drake;
import com.twisted.game.world.entity.combat.method.impl.npcs.karuulm.Wyrm;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.Gargoyle;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.Nechryael;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.superiors.nechryarch.NechryarchDeathSpawn;
import com.twisted.game.world.entity.mob.npc.droptables.ItemDrops;
import com.twisted.game.world.entity.mob.npc.droptables.ScalarLootTable;
import com.twisted.game.world.entity.mob.npc.pets.Pet;
import com.twisted.game.world.entity.mob.npc.pets.PetAI;
import com.twisted.game.world.entity.mob.player.GameMode;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.*;
import com.twisted.util.chainedwork.Chain;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.twisted.game.content.collection_logs.CollectionLog.RAIDS_KEY;
import static com.twisted.game.content.collection_logs.data.LogType.BOSSES;
import static com.twisted.game.content.collection_logs.data.LogType.OTHER;
import static com.twisted.game.content.instance.impl.VorkathInstance.ENTRANCE_POINT;
import static com.twisted.game.content.seasonal_events.christmas.SnowMonsterSpawnTask.*;
import static com.twisted.game.world.entity.AttributeKey.*;
import static com.twisted.util.CustomItemIdentifiers.VOTEBOSS_MYSTERY_BOX;
import static com.twisted.util.CustomNpcIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.BLOOD_MONEY;
import static com.twisted.util.NpcIdentifiers.*;

/**
 * Represents an npc's death task, which handles everything
 * an npc does before and after their death animation (including it),
 * such as dropping their drop table items.
 *
 * @author relex lawl
 * @author Created by Bart on 10/6/2015.
 */
public class NpcDeath {

    private static final Logger logger = LogManager.getLogger(NpcDeath.class);
    private static final Logger npcDropLogs = LogManager.getLogger("NpcDropLogs");
    private static final Level NPC_DROPS;

    static {
        NPC_DROPS = Level.getLevel("NPC_DROPS");
    }

    private static final List<Integer> customDrops = Arrays.asList(WHIRLPOOL_496, KRAKEN, CAVE_KRAKEN, WHIRLPOOL, ZULRAH, ZULRAH_2043, ZULRAH_2044);

    public static void execute(Npc npc) {
        try {
            if (npc.getCombatMethod() instanceof CommonCombatMethod commonCombatMethod) {
                if (commonCombatMethod.rollSuperior(npc)) return;
            }

            var respawnTimer = Utils.secondsToTicks(45);// default 45 seconds
            NpcDefinition def = World.getWorld().definitions().get(NpcDefinition.class, npc.id());
            if (def != null) {
                if (def.combatlevel >= 1 && def.combatlevel <= 50) {
                    respawnTimer = Utils.secondsToTicks(30);//30 seconds
                } else if (def.combatlevel >= 51 && def.combatlevel <= 150) {
                    respawnTimer = Utils.secondsToTicks(25);//25 seconds
                } else {
                    respawnTimer = Utils.secondsToTicks(20);// 20 seconds
                }
            }

            npc.getMovementQueue().clear();
            npc.lockNoDamage();

            // Reset interacting entity..
            npc.setEntityInteraction(null);

            Optional<Player> killer_id = npc.getCombat().getKiller();

            // Player that did the most damage.
            Player killer = killer_id.orElse(null);

            Chain.bound(null).runFn(1, () -> {
                // 1t later facing is reset. Stops npcs looking odd when they reset facing their target the tick they die.
                npc.face(null);
            });

            if (killer != null) {
                respawnTimer -= switch (killer.getMemberRights()) {
                    case NONE -> 0;
                    case RUBY_MEMBER -> Utils.secondsToTicks(2);
                    case SAPPHIRE_MEMBER -> Utils.secondsToTicks(4);
                    case EMERALD_MEMBER -> Utils.secondsToTicks(6);
                    case DIAMOND_MEMBER -> Utils.secondsToTicks(8);
                    case DRAGONSTONE_MEMBER, ONYX_MEMBER, ZENYTE_MEMBER -> Utils.secondsToTicks(10);
                };

                var biggest_and_baddest_perk = killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.BIGGEST_AND_BADDEST) && Slayer.creatureMatches(killer, npc.id());
                var ancientRevSpawnRoll = 25;
                var menRevSpawnRoll = 100;//bloodrevsupdate
                var superiorSpawnRoll = biggest_and_baddest_perk ? 4 : 6;

                var reduction = ancientRevSpawnRoll * killer.memberAncientRevBonus() / 100;
                ancientRevSpawnRoll -= reduction;

                var legendaryInsideCave = killer.tile().memberCave() && killer.getMemberRights().isDragonstoneOrGreater(killer);
                var VIPInsideCave = killer.tile().memberCave() && killer.getMemberRights().isDragonstoneOrGreater(killer);
                var SponsorInsideCave = killer.tile().memberCave() && killer.getMemberRights().isDragonstoneOrGreater(killer);
                if (legendaryInsideCave)
                    respawnTimer = 34;
                if (VIPInsideCave)
                    respawnTimer = 30;
                if (SponsorInsideCave)
                    respawnTimer = 25;

                killer.getCombat().reset();

                // Increment kill.
                killer.getSlayerKillLog().addKill(npc);
                if (!npc.isWorldBoss() || npc.id() != THE_NIGHTMARE_9430 || npc.id() != KALPHITE_QUEEN_6500 || npc.id() != 11278) {
                    killer.getBossKillLog().addKill(npc);
                }

                if (npc.def().name.equalsIgnoreCase("Yak")) {
                    AchievementsManager.activate(killer, Achievements.YAK_HUNTER, 1);
                }

                if (npc.def().name.equalsIgnoreCase("Rock Crab")) {
                    AchievementsManager.activate(killer, Achievements.ROCK_CRAB_HUNTER, 1);
                }

                if (npc.def().name.equalsIgnoreCase("Sand Crab")) {
                    AchievementsManager.activate(killer, Achievements.SAND_CRAB_HUNTER, 1);
                }

                if (npc.def().name.equalsIgnoreCase("Experiment")) {
                    AchievementsManager.activate(killer, Achievements.EXPERIMENTS_HUNTER, 1);
                }

                if (npc.def().name.equalsIgnoreCase("Adamant dragon")) {
                    var kc = killer.<Integer>getAttribOr(ADAMANT_DRAGONS_KILLED, 0) + 1;
                    killer.putAttrib(ADAMANT_DRAGONS_KILLED, kc);
                }

                if (npc.def().name.equalsIgnoreCase("Rune dragon")) {
                    var kc = killer.<Integer>getAttribOr(RUNE_DRAGONS_KILLED, 0) + 1;
                    killer.putAttrib(RUNE_DRAGONS_KILLED, kc);
                }

                if (npc.def().name.equalsIgnoreCase("Lava dragon")) {
                    var kc = killer.<Integer>getAttribOr(LAVA_DRAGONS_KILLED, 0) + 1;
                    killer.putAttrib(LAVA_DRAGONS_KILLED, kc);
                }

                if (npc.def().name.contains("dragon") || npc.def().name.contains("Dragon")) {
                    if (npc.id() != TENTACLE_5912) {
                        AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_I, 1);
                        killer.getTaskMasterManager().increase(Tasks.DRAGONS);
                    }
                }

                if (npc.def().name.contains("Black dragon") || npc.def().name.contains("black dragon")) {
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_II, 1);
                }

                if (npc.def().name.equalsIgnoreCase("K'ril Tsutsaroth") || npc.def().name.equalsIgnoreCase("General Graardor") || npc.def().name.equalsIgnoreCase("Commander Zilyana") || npc.def().name.equalsIgnoreCase("Kree'arra")) {
                    AchievementsManager.activate(killer, Achievements.GODWAR, 1);
                }

                if (npc.def().name.contains("Revenant") || npc.def().name.contains("revenant")) {
                    AchievementsManager.activate(killer, Achievements.REVENANT_HUNTER_I, 1);
                    AchievementsManager.activate(killer, Achievements.REVENANT_HUNTER_II, 1);
                    AchievementsManager.activate(killer, Achievements.REVENANT_HUNTER_III, 1);
                    AchievementsManager.activate(killer, Achievements.REVENANT_HUNTER_IV, 1);
                    killer.getTaskMasterManager().increase(Tasks.REVENANTS);
                    DailyTaskManager.increase(DailyTasks.REVENANTS, killer);
                }

                if (npc.def().name.equalsIgnoreCase("Alchemical Hydra")) {
                    killer.getTaskMasterManager().increase(Tasks.ALCHEMICAL_HYDRA);
                }

                if (npc.def().name.equalsIgnoreCase("Chaos Fanatic")) {
                    killer.getTaskMasterManager().increase(Tasks.CHAOS_FANATIC);
                }

                if (npc.def().name.equalsIgnoreCase("Crazy archaeologist")) {
                    killer.getTaskMasterManager().increase(Tasks.CRAZY_ARCHAEOLOGIST);
                }

                if (npc.def().name.equalsIgnoreCase("Demonic gorilla")) {
                    killer.getTaskMasterManager().increase(Tasks.DEMONIC_GORILLA);
                }

                if (npc.def().name.equalsIgnoreCase("King Black Dragon")) {
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_II, 1);
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_III, 1);
                    killer.getTaskMasterManager().increase(Tasks.KING_BLACK_DRAGON);

                    if (World.getWorld().rollDie(10, 1)) {
                        npc.respawns(false);//King black dragon can no longer spawn his ancient version spawns.
                        var ancientKingBlackDragon = new Npc(ANCIENT_KING_BLACK_DRAGON, npc.spawnTile()).respawns(false);
                        World.getWorld().getNpcs().add(ancientKingBlackDragon);
                    }
                }

                if (npc.id() == ANCIENT_KING_BLACK_DRAGON) {
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_II, 1);
                    AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_III, 1);
                    killer.getTaskMasterManager().increase(Tasks.KING_BLACK_DRAGON);
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);
                    if (!npc.ancientSpawn()) {
                        Chain.bound(null).runFn(30, () -> {
                            var kingBlackDragon = new Npc(KING_BLACK_DRAGON, npc.spawnTile());
                            World.getWorld().getNpcs().add(kingBlackDragon);
                        });
                    }
                }

                if (npc.def().name.equalsIgnoreCase("Lizardman shaman")) {
                    killer.getTaskMasterManager().increase(Tasks.LIZARDMAN_SHAMAN);
                }

                if (npc.def().name.equalsIgnoreCase("Thermonuclear smoke devil")) {
                    killer.getTaskMasterManager().increase(Tasks.THERMONUCLEAR_SMOKE_DEVIL);
                }

                if (npc.def().name.equalsIgnoreCase("Vet'ion")) {
                    killer.getTaskMasterManager().increase(Tasks.VETION);
                }

                if (npc.def().name.equalsIgnoreCase("Chaos Elemental")) {
                    killer.getTaskMasterManager().increase(Tasks.CHAOS_ELEMENTAL);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_I, 1);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_II, 1);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_III, 1);
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);

                    if (World.getWorld().rollDie(10, 1)) {
                        npc.respawns(false);//Chaos elemental can no longer spawn his ancient version spawns.
                        var ancientChaosEle = new Npc(ANCIENT_CHAOS_ELEMENTAL, npc.spawnTile()).respawns(false);
                        World.getWorld().getNpcs().add(ancientChaosEle);
                    }
                }

                if (npc.id() == ANCIENT_CHAOS_ELEMENTAL) {
                    killer.getTaskMasterManager().increase(Tasks.CHAOS_ELEMENTAL);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_I, 1);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_II, 1);
                    AchievementsManager.activate(killer, Achievements.ULTIMATE_CHAOS_III, 1);
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);

                    if (!npc.ancientSpawn()) {
                        Chain.bound(null).runFn(30, () -> {
                            var chaosElemental = new Npc(CHAOS_ELEMENTAL, npc.spawnTile());
                            World.getWorld().getNpcs().add(chaosElemental);
                        });
                    }
                }

                if (npc.def().name.contains("Zulrah")) {
                    killer.getTaskMasterManager().increase(Tasks.ZULRAH);
                    DailyTaskManager.increase(DailyTasks.ZULRAH, killer);
                }

                if (npc.def().name.equalsIgnoreCase("Vorkath")) {
                    killer.getTaskMasterManager().increase(Tasks.VORKATH);
                    DailyTaskManager.increase(DailyTasks.VORKATH, killer);
                }

                if (npc.def().name.equalsIgnoreCase("Zombies Champion") || npc.def().name.equalsIgnoreCase("Skotizo") || npc.def().name.equalsIgnoreCase("Tekton")) {
                    killer.getTaskMasterManager().increase(Tasks.WORLD_BOSS);
                }

                if (npc.def().name.equalsIgnoreCase("Kalphite Queen")) {
                    killer.getTaskMasterManager().increase(Tasks.KALPHITE_QUEEN);
                }

                if (npc.def().name.equalsIgnoreCase("Dagannoth Supreme") || npc.def().name.equalsIgnoreCase("Dagannoth Prime") || npc.def().name.equalsIgnoreCase("Dagannoth Rex")) {
                    AchievementsManager.activate(killer, Achievements.LORD_OF_THE_RINGS_I, 1);
                    AchievementsManager.activate(killer, Achievements.LORD_OF_THE_RINGS_II, 1);
                    killer.getTaskMasterManager().increase(Tasks.DAGANNOTH_KINGS);
                }

                if (npc.def().name.equalsIgnoreCase("Giant Mole")) {
                    AchievementsManager.activate(killer, Achievements.HOLEY_MOLEY_I, 1);
                    AchievementsManager.activate(killer, Achievements.HOLEY_MOLEY_II, 1);
                    AchievementsManager.activate(killer, Achievements.HOLEY_MOLEY_III, 1);
                    killer.getTaskMasterManager().increase(Tasks.GIANT_MOLE);
                }

                if (npc.def().name.equalsIgnoreCase("Barrelchest")) {
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);

                    if (World.getWorld().rollDie(10, 1)) {
                        npc.respawns(false);//Barrelchest can no longer spawn his ancient version spawns.
                        var ancientBarrelchest = new Npc(ANCIENT_BARRELCHEST, npc.spawnTile()).respawns(false);
                        World.getWorld().getNpcs().add(ancientBarrelchest);
                    }
                }

                if (npc.id() == ANCIENT_BARRELCHEST) {
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);

                    if (!npc.ancientSpawn()) {
                        Chain.bound(null).runFn(30, () -> {
                            var barrelchest = new Npc(BARRELCHEST_6342, npc.spawnTile());
                            World.getWorld().getNpcs().add(barrelchest);
                        });
                    }
                }

                if (npc.id() == SNOW_MONSTER) {
                    //Break old task
                    SnowMonsterSpawnTask.getSingleton().stop();
                    killer.message(Color.PURPLE.wrap("A new Ice demon will appear in 5 minutes."));
                    //New task
                    Chain.bound(null).runFn(TESTING ? TEST_TICKS : 500, () -> {
                        killer.getSnowMonster().spawn();//Respawn new snow monster after 100 ticks (1min)
                    });
                }

                Slayer.reward(killer, npc);
                SlayerPartner.reward(killer, npc);

                if (killer.getMinigame() != null) {
                    killer.getMinigame().killed(killer, npc);
                }

                // Check if the dead npc is a barrows brother. Award killcount.
                var isBarrowsBro = false;

                switch (npc.id()) {

                    case DHAROK_THE_WRETCHED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(DHAROK, 1);
                    }
                    case AHRIM_THE_BLIGHTED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(AHRIM, 1);
                    }
                    case VERAC_THE_DEFILED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(VERAC, 1);
                    }
                    case TORAG_THE_CORRUPTED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(TORAG, 1);
                    }
                    case KARIL_THE_TAINTED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(KARIL, 1);
                    }
                    case GUTHAN_THE_INFESTED -> {
                        isBarrowsBro = true;
                        killer.putAttrib(GUTHAN, 1);
                    }

                    case KrakenBoss.KRAKEN_NPCID -> {// Kraken boss transmogged KC
                        AchievementsManager.activate(killer, Achievements.SQUIDWARD_I, 1);
                        AchievementsManager.activate(killer, Achievements.SQUIDWARD_II, 1);
                        AchievementsManager.activate(killer, Achievements.SQUIDWARD_III, 1);
                        killer.getTaskMasterManager().increase(Tasks.KRAKEN);
                    }

                    case CORRUPTED_NECHRYARCH -> DailyTaskManager.increase(DailyTasks.CORRUPTED_NECHRYARCHS, killer);

                    case ADAMANT_DRAGON, ADAMANT_DRAGON_8090, RUNE_DRAGON, RUNE_DRAGON_8031, RUNE_DRAGON_8091 ->
                        AchievementsManager.activate(killer, Achievements.DRAGON_SLAYER_IV, 1);

                    case CERBERUS, CERBERUS_5863, CERBERUS_5866 -> {
                        killer.getTaskMasterManager().increase(Tasks.CERBERUS);
                        AchievementsManager.activate(killer, Achievements.FLUFFY_I, 1);
                        AchievementsManager.activate(killer, Achievements.FLUFFY_II, 1);
                    }

                    case KALPHITE_QUEEN_6501 -> {
                        AchievementsManager.activate(killer, Achievements.BUG_EXTERMINATOR_I, 1);
                        AchievementsManager.activate(killer, Achievements.BUG_EXTERMINATOR_II, 1);
                    }

                    case LIZARDMAN_SHAMAN_6767 -> {
                        AchievementsManager.activate(killer, Achievements.DR_CURT_CONNORS_I, 1);
                        AchievementsManager.activate(killer, Achievements.DR_CURT_CONNORS_II, 1);
                        AchievementsManager.activate(killer, Achievements.DR_CURT_CONNORS_III, 1);
                    }

                    case THERMONUCLEAR_SMOKE_DEVIL -> {
                        AchievementsManager.activate(killer, Achievements.TSJERNOBYL_I, 1);
                        AchievementsManager.activate(killer, Achievements.TSJERNOBYL_II, 1);
                        AchievementsManager.activate(killer, Achievements.TSJERNOBYL_III, 1);
                    }

                    case VETION, VETION_REBORN -> {
                        AchievementsManager.activate(killer, Achievements.VETION_I, 1);
                        AchievementsManager.activate(killer, Achievements.VETION_II, 1);
                        AchievementsManager.activate(killer, Achievements.VETION_III, 1);
                        DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);
                    }

                    case VENENATIS_6610 -> {
                        killer.getTaskMasterManager().increase(Tasks.VENENATIS);
                        AchievementsManager.activate(killer, Achievements.BABY_ARAGOG_I, 1);
                        AchievementsManager.activate(killer, Achievements.BABY_ARAGOG_II, 1);
                        AchievementsManager.activate(killer, Achievements.BABY_ARAGOG_III, 1);
                        DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);
                    }

                    case CALLISTO_6609 -> {
                        killer.getTaskMasterManager().increase(Tasks.CALLISTO);
                        AchievementsManager.activate(killer, Achievements.BEAR_GRYLLS_I, 1);
                        AchievementsManager.activate(killer, Achievements.BEAR_GRYLLS_II, 1);
                        AchievementsManager.activate(killer, Achievements.BEAR_GRYLLS_III, 1);
                        DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);
                    }

                    case 492 -> Chain.bound(null).runFn(30, () -> {//update1
                        var karken = new Npc(493, npc.spawnTile()).respawns(false);
                        World.getWorld().getNpcs().add(karken);
                    });
                    case MENDING_REVENANT_KNIGHT, MENDING_REVENANT_ORK -> {//bloodrevsupdate
                        NpcSpawnInWild.spawned--;
                        if (World.getWorld().rollDie(menRevSpawnRoll, 1) && !WildernessArea.inDiamondZone(npc.tile()) && !WildernessArea.inZenyteZone(npc.tile())) {
                            npc.respawns(false);//
                            var menEev = new Npc(MENDING_SUPERIOR_REVEANT, npc.spawnTile()).respawns(false);
                            World.getWorld().getNpcs().add(menEev);
                        }
                    }

                    case SUMMER_IMP -> {
                        EventNpcSpawn.spawned--;
                        int eventKills = killer.<Integer>getAttribOr(AttributeKey.EVENT_MONSTERS_KILLED, 0) + 1;
                        killer.putAttrib(AttributeKey.EVENT_MONSTERS_KILLED, eventKills);
                    }

                    case ZULRAH, ZULRAH_2043, ZULRAH_2044 -> {
                        AchievementsManager.activate(killer, Achievements.SNAKE_CHARMER_I, 1);
                        AchievementsManager.activate(killer, Achievements.SNAKE_CHARMER_II, 1);
                        AchievementsManager.activate(killer, Achievements.SNAKE_CHARMER_III, 1);
                    }

                    case VORKATH_8061 -> {

                        Chain.bound(killer).runFn(1, () -> {

                            AchievementsManager.activate(killer, Achievements.VORKY_I, 1);
                            AchievementsManager.activate(killer, Achievements.VORKY_II, 1);
                            AchievementsManager.activate(killer, Achievements.VORKY_III, 1);
                        }).then(5, () -> {
                            if (killer.getVorkathInstance().getInstance() != null) {
                                if (killer.getVorkathInstance() != null) {
                                    killer.getVorkathInstance().npcList.clear();

                                    //Create a Vorkath instance
                                    killer.getVorkathInstance().sleepingVorkath = new Npc(VORKATH_8059, ENTRANCE_POINT.transform(-3, 9, killer.getVorkathInstance().getInstance().getzLevel()));

                                    Npc vorkath = killer.getVorkathInstance().sleepingVorkath;
                                    vorkath.getMovementQueue().setBlockMovement(true);
                                    World.getWorld().registerNpc(vorkath);
                                    killer.getVorkathInstance().npcList.add(vorkath);

                                    //Just to make sure when entering the area reset vorkath's state
                                    killer.setVorkathState(VorkathState.SLEEPING);
                                }
                            }
                        });
                    }

                    case BATTLE_MAGE, BATTLE_MAGE_1611, BATTLE_MAGE_1612 -> {
                        AchievementsManager.activate(killer, Achievements.MAGE_ARENA_I, 1);
                        AchievementsManager.activate(killer, Achievements.MAGE_ARENA_II, 1);
                        AchievementsManager.activate(killer, Achievements.MAGE_ARENA_III, 1);
                        AchievementsManager.activate(killer, Achievements.MAGE_ARENA_IV, 1);
                        DailyTaskManager.increase(DailyTasks.BATTLE_MAGE, killer);
                    }

                }

                if (isBarrowsBro) {
                    killer.clearAttrib(barrowsBroSpawned);
                    killer.putAttrib(BARROWS_MONSTER_KC, 1 + (int) killer.getAttribOr(BARROWS_MONSTER_KC, 0));
                    var newkc = killer.getAttribOr(BARROWS_MONSTER_KC, 0);
                    killer.getPacketSender().sendString(4536, "Kill Count: " + newkc);
                    killer.getPacketSender().sendEntityHintRemoval(false);
                }

                //Make sure spawns are killed on boss death
                if (npc.id() == SCORPIA) {
                    killer.getTaskMasterManager().increase(Tasks.SCORPIA);
                    npc.clearAttrib(AttributeKey.SCORPIA_GUARDIANS_SPAWNED);
                    AchievementsManager.activate(killer, Achievements.BARK_SCORPION_I, 1);
                    AchievementsManager.activate(killer, Achievements.BARK_SCORPION_II, 1);
                    AchievementsManager.activate(killer, Achievements.BARK_SCORPION_III, 1);
                    DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);
                    World.getWorld().getNpcs().forEachInArea(new Area(3219, 3248, 10329, 10353), n -> {
                        if (n.id() == SCORPIAS_GUARDIAN) {
                            World.getWorld().unregisterNpc(n);
                        }
                    });

                    if (World.getWorld().rollDie(superiorSpawnRoll, 1)) {
                        npc.respawns(false);//Cerberus can no longer spawn his superior spawns in 1 minute.
                        var skorpios = new Npc(SKORPIOS, npc.spawnTile()).respawns(false);
                        World.getWorld().getNpcs().add(skorpios);
                    }
                }

                if (npc.id() == SKORPIOS) {
                    World.getWorld().getNpcs().forEachInArea(new Area(3219, 3248, 10329, 10353), n -> {
                        if (n.id() == SCORPIAS_GUARDIAN) {
                            World.getWorld().unregisterNpc(n);
                        }
                    });

                    Chain.bound(null).runFn(30, () -> {
                        var scorpia = new Npc(SCORPIA, npc.spawnTile());
                        World.getWorld().getNpcs().add(scorpia);
                    });
                }

                //Do custom area deaths
                if (killer.getController() != null) {
                    killer.getController().defeated(killer, npc);
                }

                //Do bots death
                if (npc.getBotHandler() != null) {
                    npc.getBotHandler().onDeath(killer);
                }

                var killerOpp = killer.<Mob>getAttribOr(AttributeKey.LAST_DAMAGER, null);
                if (killer.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA, -1) == 0 && killerOpp != null && killerOpp == npc) { // Last fighting with this dead npc.
                    killer.clearAttrib(AttributeKey.LAST_WAS_ATTACKED_TIME); // Allow instant aggro from other npcs/players.
                }

                var done = false;
                for (MagicalAnimator.ArmourSets set : MagicalAnimator.ArmourSets.values()) {
                    if (!done && set.npc == npc.id()) {
                        done = true;
                        killer.getPacketSender().sendEntityHintRemoval(true);// remove hint arrow
                    }
                }
            }

            //Do death animation
            if (npc instanceof AlchemicalHydra) {
                npc.animate(8257);
                Chain.bound(null).runFn(2, () -> {
                    npc.transmog(8622);
                    npc.animate(8258);
                });
            } else if (npc instanceof Drake) {
                npc.animate(8277);
                Chain.bound(null).runFn(1, () -> {
                    npc.transmog(8613);
                    npc.animate(8278);
                });
            } else if (npc instanceof TzTokJad) {
                npc.graphic(453);
            } else {
                npc.animate(npc.combatInfo() != null && npc.combatInfo().animations != null ? npc.combatInfo().animations.death : -1);
            }

            // Death sound!
            if (killer != null) {
                if (npc.combatInfo() != null && npc.combatInfo().sounds != null) {
                    var sounds = npc.combatInfo().sounds.death;
                    if (sounds != null && sounds.length > 0) {
                        killer.sound(Utils.randomElement(sounds));
                    }
                }
            }

            int finalRespawnTimer = respawnTimer;
            Chain.bound(null).runFn(npc.combatInfo() != null ? npc.combatInfo().deathlen : 5, () -> {
                if (killer != null) {
                    //Do inferno minigame death here and fight caves

                    if (WildernessArea.inWilderness(killer.tile())) {
                        WildernessKeys.rollWildernessKey(killer, npc);
                    }

                    //Do death scripts
                    if (npc.id() == KRAKEN) {
                        KrakenBoss.onDeath(npc); //Kraken uses its own death script
                    }

                    if (npc.getCombatMethod() instanceof CommonCombatMethod commonCombatMethod) {
                        commonCombatMethod.set(npc, killer);
                        commonCombatMethod.onDeath(killer, npc);
                    }

                    //Rock crabs
                    if (npc.id() == 101 || npc.id() == 103) {
                        switch (npc.id()) {
                            case 101 -> npc.transmog(101);
                            case 103 -> npc.transmog(103);
                        }
                        npc.walkRadius(0);
                    }

                    // so in java .. we dont have functions so we need to hardcode the id check
                    if (WildernessBossEvent.getINSTANCE().getActiveNpc().isPresent() &&
                        npc == WildernessBossEvent.getINSTANCE().getActiveNpc().get()) {
                        WildernessBossEvent.getINSTANCE().bossDeath(npc);
                    }

                    if (npc.id() == THE_NIGHTMARE_9430) {
                        nightmareDrops(npc);
                    }
                    if (npc.id() == 15003) {
                        donationBossDrop(npc);
                    }
                    if (npc.id() == 13391) {
                        miniBossDrop(npc);
                    }
                    if (npc.id() == 8633) {//s23update
                        votebossDrop(npc);//updatevoteboss
                    }
                    if (npc.id() == FRAGMENT_OF_SEREN) {
                        serenDrops(npc);
                    }
                    if (npc.id() == 11278) {
                        nexDrop(npc);
                    }

                    if (npc.id() == 12223) {
                        vardorvisDrop(npc);
                    }

                    if (npc.id() == 12078) {
                        muspahDrop(npc);
                    }

                    killer.getBossTimers().submit(npc.def().name, (int) killer.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), killer);

                    //Drop loot, but the first form of KQ, Runite golem and world bosses do not drop anything.
                    if ((npc.id() != KALPHITE_QUEEN_6500 && npc.id() != RUNITE_GOLEM && !npc.isWorldBoss() && npc.id() != THE_NIGHTMARE_9430 && npc.id() != 12223 && npc.id() != 12078)) {

                        ItemDrops.rollTheDropTable(killer, npc);

                        //Only give BM when the npc is flagged as boss and we have the perk unlocked
                        if (npc.combatInfo().boss && killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.BLOOD_MONEY_FROM_KILLING_BOSSES)) {
                            int combat = 0;
                            if (def != null) {
                                combat = def.combatlevel;
                            }

                            var amount = 0;
                            if (combat > 200) {
                                amount = Utils.random(350, 750);
                            } else {
                                amount = Utils.random(125, 350);
                            }

                            var blood_reaper = killer.hasPetOut("Blood Reaper pet");
                            if (blood_reaper) {
                                int extraBM = amount * 10 / 100;
                                amount += extraBM;
                            }

                            Item BM = new Item(BLOOD_MONEY, amount);

                            //Niffler should only pick up items of monsters and players that you've killed.
                            if (killer.nifflerPetOut() && killer.nifflerCanStore(npc)) {
                                killer.nifflerStore(BM);
                            }
                        }
                    }

                    // Custom drop tables
                    if (npc.combatInfo() != null && npc.combatInfo().scripts != null && npc.combatInfo().scripts.droptable_ != null) {
                        npc.combatInfo().scripts.droptable_.reward(npc, killer);
                    }

                    EarningPotential.increaseByKill(killer, npc);
                }


                if (npc.id() == KALPHITE_QUEEN_6500) {
                    KalphiteQueenFirstForm.death(npc);
                    return;
                } else if (npc.id() == KALPHITE_QUEEN_6501) {
                    KalphiteQueenSecondForm.death(npc);
                }

                if (npc.id() == Phases.OLM_LEFT_HAND) {
                    if (killer == null) return;
                    Party party = killer.raidsParty;
                    if (party != null) {
                        party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getLeftHandObject(), 7370));
                        Chain.bound(null).runFn(2, () -> {
                            ObjectManager.addObj(new GameObject(29885, new Tile(3238, 5733, killer.tile().getZ()), 10, 1));
                            ObjectManager.addObj(new GameObject(29885, new Tile(3220, 5743, killer.tile().getZ()), 10, 3));
                        });
                        party.setLeftHandDead(true);
                    }
                }

                if (npc.id() == Phases.OLM_RIGHT_HAND) {
                    if (killer == null) return;
                    Party party = killer.raidsParty;
                    if (party != null) {
                        party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getRightHandObject(), 7352));
                        Chain.bound(null).runFn(2, () -> {
                            party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getRightHandObject(), 7353));
                        });
                        party.setRightHandDead(true);
                        party.setCanAttackLeftHand(true);
                    }
                }

                if (npc.id() == VETION_REBORN) {
                    npc.putAttrib(AttributeKey.VETION_REBORN_ACTIVE, false);
                    npc.clearAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED);
                    npc.transmog(VETION);
                }

                if (npc.def().name.equalsIgnoreCase("Alchemical hydra")) {
                    if (killer != null && killer.getAlchemicalHydraInstance() != null) {
                        killer.getAlchemicalHydraInstance().death(killer);//Do Alchemical hydra death
                    }
                }
               /* if(npc.id() == ELVARG_HARD && !PetAI.hasUnlocked(killer, Pet.ELVARG_JR)){
                    killer.addUnlockedPet(Pet.ELVARG_JR.varbit);
                    killer.message("ok added");
                }*///im 100% sure this what make that problem do you remember when you told me to work on pets? yeah //i was testing on elvarg// ohh lol so all good now?
                //i think so but i haven't got the problem on my side or ur side as i see lol yeah idk how they've done it but to reset him i have to restart server

                //i think you're good now for elvarg  sweet ty bro really want the dupe things fixed lol

                // for collection log? yeah you're able to relog and keep colleting reward, same for daily task you can relog and do it againwtf this is big bug lol syhow me it
                if (npc.id() == 6613) {
                    VetionMinion.death(npc); //Do Vetíon minion death
                }

                if (npc.id() == 6716 || npc.id() == 6723 || npc.id() == 7649) {
                    NechryarchDeathSpawn.death(npc); //Do death spawn death
                }

                if (npc.id() == NECHRYAEL || npc.id() == NECHRYAEL_11) {
                    new Nechryael().onDeath(npc);
                }

                Zulrah.death(killer, npc);

                if (npc.id() == CORPOREAL_BEAST) { // Corp beast
                    // Reset damage counter

                    Npc corp = npc.getAttribOr(AttributeKey.BOSS_OWNER, null);
                    if (corp != null) {
                        //Check for any minions.
                        List<Npc> minList = corp.getAttribOr(AttributeKey.MINION_LIST, null);
                        if (minList != null) {
                            minList.remove(npc);
                        }
                    }
                }

                //Forgot to say its ALL npcs, happens to bots, kraken any npc
                if (killer != null) {
                    if (npc.respawns() && !npc.isBot())
                        killer.getPacketSender().sendEffectTimer((int) Utils.ticksToSeconds(finalRespawnTimer), EffectTimer.MONSTER_RESPAWN);
                }

                deathReset(npc);
                if (npc.respawns()) {
                    npc.teleport(npc.spawnTile());
                    npc.hidden(true);

                    // Remove from area..
                    if (npc.getController() != null) {
                        npc.getController().leave(npc);
                        npc.setController(null);
                    }

                    Chain.bound(null).runFn(finalRespawnTimer, () -> {
                        GwdLogic.onRespawn(npc);
                        respawn(npc);
                    });
                } else if (npc.id() != KALPHITE_QUEEN_6500) {
                    npc.hidden(true);
                    World.getWorld().unregisterNpc(npc);
                }
            });
        } catch (
            Exception e) {
            logger.catching(e);
        }

    }

    private static void treasure(Player killer, Npc npc, Tile tile) {
        if (!killer.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.TREASURE_HUNT)) {
            return;
        }

        if (!Slayer.creatureMatches(killer, npc.id())) {
            return;
        }

        int treasureCasketChance;
        if (killer.getMemberRights().isZenyteOrGreater(killer))
            treasureCasketChance = 95;
        else if (killer.getMemberRights().isOnyxOrGreater(killer))
            treasureCasketChance = 100;
        else if (killer.getMemberRights().isDragonstoneOrGreater(killer))
            treasureCasketChance = 105;
        else if (killer.getMemberRights().isDiamondOrGreater(killer))
            treasureCasketChance = 110;
        else if (killer.getMemberRights().isEmeraldOrGreater(killer))
            treasureCasketChance = 115;
        else if (killer.getMemberRights().isSaphireOrGreater(killer))
            treasureCasketChance = 120;
        else if (killer.getMemberRights().isRubyOrGreater(killer))
            treasureCasketChance = 125;
        else
            treasureCasketChance = 128;

        var reduction = treasureCasketChance * killer.masterCasketMemberBonus() / 100;
        treasureCasketChance -= reduction;

        if (World.getWorld().rollDie(killer.getPlayerRights().isDeveloperOrGreater(killer) && !GameServer.properties().production ? 1 : treasureCasketChance, 1)) {
            Item clueItem = new Item(TreasureRewardCaskets.MASTER_CASKET);
            GroundItem groundItem = new GroundItem(clueItem, tile, killer);
            GroundItemHandler.createGroundItem(groundItem);
            notification(killer, clueItem);
            killer.message("<col=0B610B>You have received a treasure casket drop!");
        }

        boolean inWilderness = WildernessArea.inWilderness(killer.tile());
        Item smallCasket = new Item(ItemIdentifiers.CASKET_7956);
        Item bigChest = new Item(CustomItemIdentifiers.BIG_CHEST);
        int combat = killer.skills().combatLevel();
        int mul;

        if ((killer.mode() == GameMode.TRAINED_ACCOUNT))
            mul = 2;
        else mul = 1;

        int chance;

        if (combat <= 10)
            chance = 1;
        else if (combat <= 20)
            chance = 2;
        else if (combat <= 80)
            chance = 3;
        else if (combat <= 120)
            chance = 4;
        else
            chance = 5;

        int regularOdds = 100;

        chance *= mul;

        //If the player is in the wilderness, they have an increased chance at a casket drop
        if ((npc.maxHp() > 20 || inWilderness)) {
            if (inWilderness && Utils.random(regularOdds - 15) < chance) {
                if (npc.combatInfo() != null && npc.combatInfo().boss && Utils.random(3) == 2) {
                    killer.message("<col=0B610B>You have received a Big chest drop!");
                    GroundItem groundItem = new GroundItem(bigChest, tile, killer);
                    GroundItemHandler.createGroundItem(groundItem);
                    notification(killer, bigChest);
                } else {
                    killer.message("<col=0B610B>You have received a small casket drop!");
                    GroundItem groundItem = new GroundItem(smallCasket, tile, killer);
                    GroundItemHandler.createGroundItem(groundItem);
                    notification(killer, smallCasket);
                }
            } else if (!inWilderness && Utils.random(regularOdds) < chance) {
                if (npc.combatInfo() != null && npc.combatInfo().boss && Utils.random(5) == 2) {
                    killer.message("<col=0B610B>You have received a Big chest drop!");
                    GroundItem groundItem = new GroundItem(bigChest, tile, killer);
                    GroundItemHandler.createGroundItem(groundItem);
                    notification(killer, bigChest);
                } else {
                    killer.message("<col=0B610B>You have received a small casket drop!");
                    GroundItem groundItem = new GroundItem(smallCasket, tile, killer);
                    GroundItemHandler.createGroundItem(groundItem);
                    notification(killer, smallCasket);
                }
            }
        }
    }

    /**
     * If you're resetting an Npc as if it were by death but not, for example maybe kraken tentacles which go back down to
     * the depths when the boss is killed.
     */
    public static void deathReset(Npc npc) {
        if (npc.id() != KALPHITE_QUEEN_6500) { // KQ first stage keeps damage onto stage 2!
            npc.getCombat().clearDamagers(); //Clear damagers
        }

        npc.clearAttrib(AttributeKey.TARGET);
        npc.clearAttrib(AttributeKey.LAST_ATTACKED_MAP);
        npc.putAttrib(AttributeKey.VENOM_TICKS, 0);
        npc.putAttrib(AttributeKey.POISON_TICKS, 0);
        npc.clearAttrib(VENOMED_BY);
    }

    public static void respawn(Npc npc) {

        if (npc.id() == KrakenBoss.KRAKEN_NPCID) {
            npc.transmog(KrakenBoss.KRAKEN_WHIRLPOOL);
            // Transmog kraken info after the drop table is done otherwise it'll look for the wrong table
            npc.combatInfo(World.getWorld().combatInfo(KrakenBoss.KRAKEN_WHIRLPOOL));
        }

        if (npc.id() == KrakenBoss.TENTACLE_WHIRLPOOL || npc.id() == NpcIdentifiers.ENORMOUS_TENTACLE) {
            Npc boss = npc.getAttrib(AttributeKey.BOSS_OWNER);
            if (boss != null && npc.dead()) {
                // only respawn minions if our boss is alive
                return;
            }
        }

        if (npc.id() == NpcIdentifiers.GARGOYLE) {
            Gargoyle.onDeath(npc);
        }

        if (npc.getCombatMethod() instanceof CommonCombatMethod commonCombatMethod) {
            commonCombatMethod.onRespawn(npc);
        }

        if (npc.id() == NpcIdentifiers.VETION) {//Just do it again for extra safety
            npc.clearAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED);
            npc.putAttrib(AttributeKey.VETION_REBORN_ACTIVE, false);
        }

        if (npc.hidden()) { // not respawned yet. we do this check incase it was force-respawned by .. group spawning (gwd)
            deathReset(npc);
            npc.hidden(false);
            if (npc.combatInfo() != null) {
                if (npc.combatInfo().stats != null || npc.combatInfo().originalStats != null)
                    npc.combatInfo().stats = npc.combatInfo().originalStats.clone(); // Replenish all stats on this NPC.
                if (npc.combatInfo().bonuses != null || npc.combatInfo().originalBonuses != null)
                    npc.combatInfo().bonuses = npc.combatInfo().originalBonuses.clone(); // Replenish all stats on this NPC.
            }

            npc.hp(npc.maxHp(), 0); // Heal up to full hp
            npc.animate(-1); // Reset death animation
            npc.unlock();
            if (npc instanceof Drake) {
                npc.transmog(DRAKE_8612);
            }

            if (npc instanceof Wyrm) {
                npc.transmog(Wyrm.IDLE);
            }
        }
    }

    public static boolean hasRingOut(Player killer) {//updatevoidor
        return killer.getEquipment().containsAny(CustomItemIdentifiers.RING_OF_TRINITY, ItemIdentifiers.RING_OF_WEALTH_I) && !killer.nifflerPetOut();


    }

    public static Optional<Pet> checkForPet(Player killer, ScalarLootTable table) {
        Optional<Pet> pet = table.rollForPet(killer);
        if (pet.isPresent()) {
            // Do we already own this pet?
            boolean caught = killer.isPetUnlocked(pet.get().varbit);

            // RS tries to add it as follower first. That only works if you don't have one.
            Npc currentPet = killer.pet();

            if (caught && pet.get().varbit != -1) {//Only applies to untradeable pets
                killer.message("You have a funny feeling like you would have been followed...");
            } else if (currentPet == null) {
                killer.message("You have a funny feeling like you're being followed.");
                PetAI.spawnPet(killer, pet.get(), false);
            } else {
                killer.inventory().addOrBank(new Item(pet.get().item));
                killer.message("You feel something weird sneaking into your backpack.");
            }

            if (!killer.isPetUnlocked(pet.get().varbit)) {
                if (pet.get().varbit != -1) { // -1 means tradeable pet
                    if (!killer.isPetUnlocked(pet.get().varbit)) {
                        killer.addUnlockedPet(pet.get().varbit);
                    }
                }
            }

            //Special collection log unlocks
            switch (pet.get()) {
                case CENTAUR_MALE -> BOSSES.log(killer, RAIDS_KEY, new Item(Pet.CENTAUR_MALE.item));

                //start //done1
                case ELVARG_JR -> OTHER.log(killer, ELVARG_HARD, new Item(Pet.ELVARG_JR.item));
                case LAVA_BABY -> OTHER.log(killer, LAVA_BEAST, new Item(Pet.LAVA_BABY.item));
                case BABY_LAVA_DRAGON -> {
                    OTHER.log(killer, LAVA_DRAGON, new Item(Pet.BABY_LAVA_DRAGON.item));
                    OTHER.log(killer, EL_FUEGO, new Item(Pet.BABY_LAVA_DRAGON.item));
                }

                case DERANGED_ARCHAEOLOGIST ->
                    OTHER.log(killer, NpcIdentifiers.DERANGED_ARCHAEOLOGIST, new Item(Pet.DERANGED_ARCHAEOLOGIST.item));
                //end


                case CENTAUR_FEMALE -> BOSSES.log(killer, RAIDS_KEY, new Item(Pet.CENTAUR_FEMALE.item));
                case DEMENTOR -> BOSSES.log(killer, RAIDS_KEY, new Item(Pet.DEMENTOR.item));
                case FLUFFY_JR -> BOSSES.log(killer, RAIDS_KEY, new Item(Pet.FLUFFY_JR.item));
                case FENRIR_GREYBACK_JR -> BOSSES.log(killer, RAIDS_KEY, new Item(Pet.FENRIR_GREYBACK_JR.item));
                case SKELETON_HELLHOUND_PET -> OTHER.log(killer, KILLER, new Item(Pet.SKELETON_HELLHOUND_PET.item));
            }

            npcDropLogs.log(NPC_DROPS, "Player " + killer.getUsername() + " got pet " + new Item(pet.get().item).name());
            World.getWorld().sendWorldMessage("<img=1081> <col=844e0d>" + killer.getUsername() + " has received a: " + new Item(pet.get().item).name() + ".");
        }
        return pet;
    }

    public static void notification(Player killer, Item drop) {
        Item loot = drop.unnote();
        //TODO: implement these
        // Enabled? Untradable buttons are only enabled if the threshold is enabled. Can't have one without the other.
        boolean notifications_enabled = killer.getAttribOr(AttributeKey.ENABLE_LOOT_NOTIFICATIONS_BUTTONS, false);
        boolean untrade_notifications = killer.getAttribOr(AttributeKey.UNTRADABLE_LOOT_NOTIFICATIONS, false);
        int lootDropThresholdValue = killer.getAttribOr(AttributeKey.LOOT_DROP_THRESHOLD_VALUE, 0);
        if (notifications_enabled) {
            if (loot.rawtradable()) {
                if (untrade_notifications) {
                    killer.message("Untradable drop: " + loot.getAmount() + " x <col=cc0000>" + loot.name() + "</col>.");
                }
            } else if (loot.getValue() >= lootDropThresholdValue) {
                killer.message("Valuable drop: " + loot.getAmount() + " x <col=cc0000>" + loot.name() + "</col> (" + loot.getValue() * loot.getAmount() + "coins).");
            }
        }
    }

    public static void corpDrop(Mob mob) {
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to The corp beast !"));
            if (mob.tile().isWithinDistance(player.tile(), 10) && hits.getDamage() >= 100) {
                if (mob instanceof Npc) {
                    player.message("You received a drop roll from the table for dealing at least 100 damage!");
                    Npc npc = mob.getAsNpc();

                    //Always log kill timers
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);

                    //Always increase kill counts
                    player.getBossKillLog().addKill(npc);

                    //Always drop random BM
                    if (hasRingOut(player)) {//up0
                        player.getInventory().addOrDrop(new Item(BLOOD_MONEY, World.getWorld().random(250, 500)));
                    } else {
                        GroundItemHandler.createGroundItem(new GroundItem(new Item(BLOOD_MONEY, World.getWorld().random(250, 500)), npc.tile(), player));
                    }
                    //Random drop from the table
                    ItemDrops.rollTheDropTable(player, npc);
                }
            }
        });
    }

    private static void nexDrop(Mob mob) {
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to The nex!"));
            // Only people nearby are rewarded. This is to avoid people 'poking' the boss to do some damage
            // without really risking being there.
            if (mob.tile().isWithinDistance(player.tile(), 10) && hits.getDamage() >= 75) {
                if (mob instanceof Npc) {
                    player.message("You received a drop roll from the table for dealing at least 75 damage!");
                    Npc npc = mob.getAsNpc();

                    //Always log kill timers
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);

                    //Always increase kill counts
                    player.getBossKillLog().addKill(npc);

                    //Always drop random BM/vbmb
                    if (hasRingOut(player)) {//up0
                        player.getInventory().addOrDrop(new Item(BLOOD_MONEY, World.getWorld().random(500, 5_500)));
                    } else {
                        GroundItemHandler.createGroundItem(new GroundItem(new Item(BLOOD_MONEY, World.getWorld().random(500, 5_500)), mob.tile(), player));
                    }

                }
            }
        });
    }

    private static void serenDrops(Mob mob) {
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to The Seren!"));
            // Only people nearby are rewarded. This is to avoid people 'poking' the boss to do some damage
            // without really risking being there.
            if (mob.tile().isWithinDistance(player.tile(), 10) && hits.getDamage() >= 200) {
                if (mob instanceof Npc) {
                    player.message("You received a drop roll from the table for dealing at least 500 damage!");
                    Npc npc = mob.getAsNpc();

                    //Always log kill timers
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);

                    //Always increase kill counts
                    player.getBossKillLog().addKill(npc);

                    //Always drop random BM
                    if (hasRingOut(player)) {//up0
                        player.getInventory().addOrDrop(new Item(BLOOD_MONEY, World.getWorld().random(500, 5_500)));
                    } else {
                        GroundItemHandler.createGroundItem(new GroundItem(new Item(BLOOD_MONEY, World.getWorld().random(500, 5_500)), npc.tile(), player));
                    }
                    //Random drop from the table
                    ScalarLootTable table = ScalarLootTable.forNPC(npc.id());
                    if (table != null) {
                        Item reward = table.randomItem(World.getWorld().random());
                        if (reward != null) {

                            // bosses, find npc ID, find item ID
                            BOSSES.log(player, npc.id(), reward);

                            //Niffler doesn't loot world The Nightmare loot
                            GroundItemHandler.createGroundItem(new GroundItem(reward, npc.tile(), player));
                            ServerAnnouncements.tryBroadcastDrop(player, npc, reward);

                            Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " got drop item " + reward, "npcdrops");
                        }
                    }
                }
            }
        });
    }

    private static void donationBossDrop(Mob mob) {//updateox
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to The Donation Boss!"));
            // Only people nearby are rewarded. This is to avoid people 'poking' the boss to do some damage
            // without really risking being there.
            if (mob.tile().isWithinDistance(player.tile(), 10) && hits.getDamage() >= 100) {
                if (hasRingOut(player)) {//up0
                    player.getInventory().addOrDrop(new Item(BLOOD_MONEY, World.getWorld().random(100, 500)));
                } else {
                    GroundItemHandler.createGroundItem(new GroundItem(new Item(BLOOD_MONEY, World.getWorld().random(100, 500)), mob.tile(), player));
                }
                if (mob instanceof Npc) {
                    player.message("You received a drop roll from the table for dealing at least 100 damage!");
                    Npc npc = mob.getAsNpc();
                    //Always log kill timers
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);

                    //Always increase kill counts
                    player.getBossKillLog().addKill(npc);

                    //Always drop random BM
                    //  GroundItemHandler.createGroundItem(new GroundItem(new Item(VOTEBOSS_MYSTERY_BOX, 1), npc.tile(), player));
                    //Random drop from the table
                    ScalarLootTable table = ScalarLootTable.forNPC(npc.id());
                    if (table != null) {
                        Item reward = table.randomItem(World.getWorld().random());
                        if (reward != null) {

                            // bosses, find npc ID, find item ID
                            BOSSES.log(player, npc.id(), reward);

                            //Niffler doesn't loot world The Nightmare loot
                            GroundItemHandler.createGroundItem(new GroundItem(reward, npc.tile(), player));
                            ServerAnnouncements.tryBroadcastDrop(player, npc, reward);

                            Utils.sendDiscordInfoLog(player,"Player " + player.getUsername() + " got drop item " + reward, "npcdrops");
                        }
                    }
                }
            }
        });

    }

    private static void miniBossDrop(Mob mob) {//donet
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to The Mini Donation Boss!"));
            // Only people nearby are rewarded. This is to avoid people 'poking' the boss to do some damage
            // without really risking being there.
            if (mob.tile().isWithinDistance(player.tile(), 10) && hits.getDamage() >= 100) {
                if (hasRingOut(player)) {//up0
                    player.getInventory().addOrDrop(new Item(BLOOD_MONEY, World.getWorld().random(100, 500)));
                } else {
                    GroundItemHandler.createGroundItem(new GroundItem(new Item(BLOOD_MONEY, World.getWorld().random(100, 500)), mob.tile(), player));
                }
                if (mob instanceof Npc) {
                    player.message("You received a drop roll from the table for dealing at least 100 damage!");
                    Npc npc = mob.getAsNpc();
                    //Always log kill timers
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);

                    //Always increase kill counts
                    player.getBossKillLog().addKill(npc);

                    //Always drop random BM
                    //  GroundItemHandler.createGroundItem(new GroundItem(new Item(VOTEBOSS_MYSTERY_BOX, 1), npc.tile(), player));
                    //Random drop from the table
                    ScalarLootTable table = ScalarLootTable.forNPC(npc.id());
                    if (table != null) {
                        Item reward = table.randomItem(World.getWorld().random());
                        if (reward != null) {

                            // bosses, find npc ID, find item ID
                            BOSSES.log(player, npc.id(), reward);

                            //Niffler doesn't loot world The Nightmare loot
                            GroundItemHandler.createGroundItem(new GroundItem(reward, npc.tile(), player));
                            ServerAnnouncements.tryBroadcastDrop(player, npc, reward);

                            Utils.sendDiscordInfoLog(player,"Player " + player.getUsername() + " got drop item " + reward, "npcdrops");
                        }
                    }
                }
            }
        });

    }

    private static void corporealDrop(Mob mob) {
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to the Corporeal Beast!"));
            if (mob.tile().isWithinDistance(player.tile(), 14) && hits.getDamage() >= 100) {
                if (mob instanceof Npc npc) {
                    player.message("You received a drop roll from the table for dealing at least 100 damage!");
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);
                    ItemDrops.rollTheDropTable(player, npc);
                }
            }
        });
    }

    private static void muspahDrop(Mob mob) {
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to the Phantom Muspah!"));
            if (mob.tile().isWithinDistance(player.tile(), 14) && hits.getDamage() >= 100) {
                if (mob instanceof Npc npc) {
                    player.message("You received a drop roll from the table for dealing at least 100 damage!");
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);
                    ItemDrops.rollTheDropTable(player, npc);
                }
            }
        });
    }

    private static void vardorvisDrop(Mob mob) {
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to Vardorvis!"));
            if (mob.tile().isWithinDistance(player.tile(), 14) && hits.getDamage() >= 100) {
                if (mob instanceof Npc npc) {
                    player.message("You received a drop roll from the table for dealing at least 100 damage!");
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);
                    ItemDrops.rollTheDropTable(player, npc);
                }
            }
        });
    }

    private static void votebossDrop(Mob mob) {
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to The Vote Boss!"));
            if (mob.tile().isWithinDistance(player.tile(), 10) && hits.getDamage() >= 35) {
                if (mob instanceof Npc) {
                    player.message("You received a drop roll from the table for dealing at least 35 damage!");
                    Npc npc = mob.getAsNpc();

                    //Always log kill timers
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);

                    //Always increase kill counts
                    player.getBossKillLog().addKill(npc);

                    //Always drop random BM/vbmb
                    GroundItemHandler.createGroundItem(new GroundItem(new Item(VOTEBOSS_MYSTERY_BOX, 1), npc.tile(), player));
                    if (hasRingOut(player)) {//up0
                        player.getInventory().addOrDrop(new Item(BLOOD_MONEY, World.getWorld().random(1000, 5000)));
                    } else {
                        GroundItemHandler.createGroundItem(new GroundItem(new Item(BLOOD_MONEY, World.getWorld().random(1000, 5000)), npc.tile(), player));
                    }


                }
            }
        });
    }

    private static void nightmareDrops(Mob mob) {
        mob.getCombat().getDamageMap().forEach((key, hits) -> {
            Player player = (Player) key;
            player.message(Color.RED.wrap("You've dealt " + hits.getDamage() + " damage to The Nightmare!"));
            // Only people nearby are rewarded. This is to avoid people 'poking' the boss to do some damage
            // without really risking being there.
            if (mob.tile().isWithinDistance(player.tile(), 10) && hits.getDamage() >= 500) {
                if (mob instanceof Npc) {
                    player.message("You received a drop roll from the table for dealing at least 500 damage!");
                    Npc npc = mob.getAsNpc();

                    //Always log kill timers
                    player.getBossTimers().submit(npc.def().name, (int) player.getCombat().getFightTimer().elapsed(TimeUnit.SECONDS), player);

                    //Always increase kill counts
                    player.getBossKillLog().addKill(npc);

                    //Always drop random BM
                    if (hasRingOut(player)) {//up0
                        player.getInventory().addOrDrop(new Item(BLOOD_MONEY, World.getWorld().random(500, 5_500)));
                    } else {
                        GroundItemHandler.createGroundItem(new GroundItem(new Item(BLOOD_MONEY, World.getWorld().random(500, 5_500)), npc.tile(), player));
                    }
                    //Random drop from the table
                    ScalarLootTable table = ScalarLootTable.forNPC(npc.id());
                    if (table != null) {
                        Item reward = table.randomItem(World.getWorld().random());
                        if (reward != null) {

                            // bosses, find npc ID, find item ID
                            BOSSES.log(player, npc.id(), reward);

                            //Niffler doesn't loot world The Nightmare loot
                            GroundItemHandler.createGroundItem(new GroundItem(reward, npc.tile(), player));
                            ServerAnnouncements.tryBroadcastDrop(player, npc, reward);

                            Utils.sendDiscordInfoLog(player, "Player " + player.getUsername() + " got drop item " + reward, "npcdrops");
                        }
                    }
                }
            }
        });
    }
}
