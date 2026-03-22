package com.twisted.game.content.mechanics;

import com.twisted.GameServer;
import com.twisted.game.content.EffectTimer;
import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.twisted.game.content.duel.Dueling;
import com.twisted.game.content.mechanics.break_items.BreakItemsOnDeath;
import com.twisted.game.content.minigames.impl.fight_caves.FightCavesMinigame;
import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.bountyhunter.BountyHunter;
import com.twisted.game.world.entity.combat.magic.Autocasting;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.combat.weapon.WeaponInterfaces;
import com.twisted.game.world.entity.mob.Flag;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.pets.PetAI;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.twisted.game.world.entity.AttributeKey.*;
import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.RETRIBUTION;

/**
 * Created by Bart on 8/15/2015.
 * Retribution by Jak 12/16/2015
 */
public class Death {

    private static final Logger logger = LogManager.getLogger(Death.class);

    private static final String[] KILL_MESSAGES = {
        "%s will probably tell you he wanted a free teleport after that performance.",
        "Such a shame that %s can't play this game.",
        "%s was made to sit down.",
        "You have defeated %s.",
        "A humiliating defeat for %s.",
        "How not to do it right: Written by %s.",
        "The struggle for %s is real.",
        "%s falls before your might.",
        "Can anyone defeat you? Certainly not %s.",
        "%s didn't stand a chance against you.",
        "What was %s thinking challenging you...",
        "%s should take lessons from you. You're clearly too good for him."
    };

    public static String randomKillMessage() {
        return KILL_MESSAGES[Utils.random(KILL_MESSAGES.length - 1)];
    }

    private static void retrib(Player player) {
        //Retribution. example: https://www.youtube.com/watch?v=7c6idspnxak
        try {
            if (Prayers.usingPrayer(player, RETRIBUTION) && !player.inActiveTournament()) {
                var pker = player.getCombat().getKiller(); // Person who killed the dead player. Might be a 73 AGS spec pj.
                player.graphic(437);
                var damage = (int) (player.skills().level(Skills.PRAYER) * 0.25);
                if (player.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA, -1) == 1) {
                    var list = new LinkedList<Player>();
                    for (Player p : player.closePlayers(1)) {
                        if (!WildernessArea.inAttackableArea(p) || p.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA, -1) == 0) {
                            //not in the multi area and we were, don't carry over.
                            continue;
                        }
                        if (player.tile().inSqRadius(p.tile(), 1)) {
                            list.add(p);
                        }
                    }

                    var damagePerPlayer = (int) Math.max(1.0, (double) damage / Math.max(1, list.size()));
                    list.forEach(p -> {
                        p.hit(player, damagePerPlayer);
                    });
                } else if (player.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA, -1) == 0 && pker.isPresent()) {
                    if (player.tile().inSqRadius(pker.get().tile(), 1)) {
                        pker.get().hit(player, damage);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Death error!", e);
        }
    }


    public static void checkTeleblockRemoval(Player killed) {
        if (killed != null) {
            if (killed.currentTeleblockedPlayers.isEmpty())
                return;

            for (final Mob entity : killed.currentTeleblockedPlayers) {
                if (entity == null)
                    continue;

                if (entity.getTimerRepository().has(TimerKey.TELEBLOCK) && entity.getTimerRepository().left(TimerKey.TELEBLOCK) > 0) {
                    entity.getTimerRepository().cancel(TimerKey.TELEBLOCK);
                    entity.getTimerRepository().cancel(TimerKey.SPECIAL_TELEBLOCK);
                    entity.getTimerRepository().cancel(TimerKey.TELEBLOCK_IMMUNITY);
                    entity.clearAttrib(AttributeKey.TELEBLOCKED_BY);
                    entity.getTimerRepository().register(TimerKey.TELEBLOCK_IMMUNITY, 5);
                    entity.message("<col=4f006f>The teleblock spell cast on you fades away.");
                }
            }

            killed.currentTeleblockedPlayers.clear();
        }
    }

    public static void death(Player player) {
        player.lock(); //Lock the player

        Chain.bound(null).name("check_double_death_task").runFn(3, () -> {// Finish the proper delay after death (2 ticks)
            try {
                Dueling.check_double_death(player); // must be checked after damage shows (because of PID you can't do it on the same cycle!)
            } catch (Exception e) {
                logger.error("Double death check error!", e);
            }
        });

        player.stopActions(true);
        player.action.reset();

        Death.retrib(player);

        var mostdmg = player.getCombat().getKiller();
        var killer = mostdmg.orElse(null);

        if (killer != null) {
            Death.makePetShoutOnKill(killer);
        }

        player.animate(836); //Animate the player

        player.runOnceTask(4, r -> {
            player.stopActions(true);

            Mob lastAttacker = player.getAttrib(AttributeKey.LAST_DAMAGER);

            //Handle player dying to a bot.
            if (lastAttacker != null && lastAttacker.isNpc() && lastAttacker.getAsNpc().getBotHandler() != null) {
                Npc bot = (Npc) lastAttacker;
                bot.stopActions(true);
                int botDeaths = player.getAttribOr(AttributeKey.BOT_DEATHS, 0);
                botDeaths++;
                player.putAttrib(AttributeKey.BOT_DEATHS, botDeaths);
                player.message("You now have " + botDeaths + " bot " + Utils.pluralOrNot("death", botDeaths) + ".");
                DailyTaskManager.increase(DailyTasks.BOTS, player);
            }

            //Minigame logic
            if (killer != null && killer.getMinigame() != null) {
                killer.getMinigame().killed(killer, player);
            }

            if (player.getMinigame() != null) {
                player.getMinigame().end(player);
            }

            Npc barrowsBro = player.getAttribOr(barrowsBroSpawned, null);
            if (barrowsBro != null) {
                World.getWorld().unregisterNpc(barrowsBro);
            }

            //BH death logic
            if (killer != null && killer.isPlayer()) {
                BountyHunter.onDeath(killer, player);
            }

            if (killer != null && player.getController() != null) {
                player.getController().defeated(killer, player);
            }

            player.clearAttrib(AttributeKey.LASTDEATH_VALUE);
            try {
                if (!player.getUsername().equalsIgnoreCase("Box Test")) {
                    ItemsOnDeath.droplootToKiller(player, killer);
                    PetAI.onDeath(player, killer);
                }
            } catch (Exception e) {
                logger.error("Error dropping items and loot!", e);
            }

            player.clearAttrib(AttributeKey.TARGET); // Clear last attacked or interacted.

            // Close open interface. do this BEFORE MINIGAME HANDLING -> such as arena deaths.
            player.stopActions(true);

            // If we died in an instance, clean it up.
            player.clearInstance();

            var died_under_7_wild = WildernessArea.wildernessLevel(player.tile()) <= 7; // Or in edge pvp (not classed as wildy)
            var duel_arena = player.getDueling().inDuel();
            var in_tournament = player.inActiveTournament() || player.isInTournamentLobby();

            // If you die in FFA clan wars, you respawn at the lobby place.
            if (duel_arena) {
                player.getDueling().onDeath();
            } else if (player.getMinigame() instanceof FightCavesMinigame) {
                player.setMinigame(null);//Clear minigame when we die too
                player.teleport(World.getWorld().randomTileAround(new Tile(2438, 5170), 1));
            } else if (player.<Integer>getAttribOr(AttributeKey.JAILED, 0) == 1) {
                player.message("You've died, but you cannot run from your jail sentence!");
                player.teleport(player.tile());
            } else if (in_tournament) {
                TournamentManager.handleDeath(player);
            } else if (player.getRaids() != null) {
                player.getRaids().death(player);
            } else {
                player.teleport(GameServer.properties().defaultTile); //Teleport the player to Varrock square
                player.message("Oh dear, you are dead!"); //Send the death message
            }

            if (player.<Boolean>getAttribOr(HP_EVENT_ACTIVE, false)) {
                player.clearAttrib(HP_EVENT_ACTIVE);
                World.getWorld().clearBroadcast();
            }

            player.putAttrib(AttributeKey.DEATH_TELEPORT_TIMER, String.valueOf(System.currentTimeMillis()));

            //If the player is transmog'd then we reset the render.
            if (Transmogrify.isTransmogrified(player) && !in_tournament) {
                Transmogrify.hardReset(player);
            }

            //Remove auto-select
            Autocasting.setAutocast(player, null); // Set auto-cast to default; 0
            WeaponInterfaces.updateWeaponInterface(player); //Update the weapon interface
            player.getCombat().setRangedWeapon(null);

            //Reset some values
            player.skills().resetStats(); //Reset all players stats
            Poison.cure(player); //Cure the player from any poisons
            player.getTimerRepository().cancel(TimerKey.FROZEN); //Remove frozen timer key
            player.getTimerRepository().cancel(TimerKey.STUNNED); //Remove stunned timer key
            player.getTimerRepository().cancel(TimerKey.TELEBLOCK); //Remove teleblock timer key
            player.getTimerRepository().cancel(TimerKey.TELEBLOCK_IMMUNITY); //Remove the teleblock immunity timer key
            if (!died_under_7_wild && !player.getTimerRepository().has(TimerKey.RECHARGE_SPECIAL_ATTACK)) {
                player.restoreSpecialAttack(100); //Set energy to 100%
                player.getTimerRepository().register(TimerKey.RECHARGE_SPECIAL_ATTACK, 150); //Set the value of the timer. Currently 1:30m
            }
            player.setSpecialActivated(false); //Disable special attack
            player.restoreSpecialAttack(100); //Restore spec
            player.getTimerRepository().cancel(TimerKey.COMBAT_LOGOUT); //Remove combat logout timer key

            //Remove timers
            player.getPacketSender().sendEffectTimer(0, EffectTimer.FREEZE);
            player.getPacketSender().sendEffectTimer(0, EffectTimer.TELEBLOCK);
            player.getPacketSender().sendEffectTimer(0, EffectTimer.VENGEANCE);

            player.getPacketSender().sendEffectTimer(0, EffectTimer.ANTIFIRE);
            player.getPacketSender().sendEffectTimer(0, EffectTimer.VENOM);
            player.getPacketSender().sendEffectTimer(0, EffectTimer.STAMINA);

            checkTeleblockRemoval(player);

            // Fact: forfeit and death in the duel arena doesn't reset skull related stuff.
            if (!duel_arena) {
                Skulling.unskull(player);
            }

            player.getCombat().clearDamagers(); //Clear damagers
            player.face(null); // Reset entity facing
            Prayers.closeAllPrayers(player); //Disable all prayers
            player.getPacketSender().sendInteractionOption("null", 2, false); //Remove the player attack option
            player.setRunningEnergy(100.0, true); //Set the players run energy to 100
            player.graphic(-1); //Set player graphics to -1
            player.hp(100, 0); //Set hitpoints to 100%
            player.animate(-1);  //Set player animation to -1
            player.getTimerRepository().cancel(TimerKey.CHARGE_SPELL); //Removes the spell charge timer from the player
            player.putAttrib(AttributeKey.MAGEBANK_MAGIC_ONLY, false); //Let our players use melee again! : )
            player.clearAttrib(AttributeKey.VENOM_TICKS);
            player.clearAttrib(VENOMED_BY);
            player.looks().hide(false);

            player.getUpdateFlag().flag(Flag.APPEARANCE); //Update the players looks
            player.unlock(); //Unlock the player
            player.getMovementQueue().setBlockMovement(false); //Incase the player movement was locked elsewhere unlock it on death.

            //Open presets when dieing if enabled
            if (player.getPresetManager().openOnDeath()) {
                player.getPresetManager().open();
            }

            //Auto repair broken items if enabled
            var autoRepairOnDeath = player.<Boolean>getAttribOr(AttributeKey.REPAIR_BROKEN_ITEMS_ON_DEATH, false);
            if (autoRepairOnDeath) {
                BreakItemsOnDeath.repair(player);
            }
        });
    }

    // Messages your pet will shout when you kill someone, if the mechanic is enabled.
    private static final List<String> SHOUTS = Arrays.asList("green:gundrilla!!!!11", "TNS tbh", "TNS");

    /**
     * Is the custom mechanic for your pet shouting when you kill someone enabled
     */
    private static final boolean PET_SHOUTING_ENABLED = true;

    /**
     * When you kill someone, if you have a follower Pet, they can shout something such as your Clan name.
     * In future we could customize this and sellout!
     */
    private static void makePetShoutOnKill(Player player) {
        // Mechanic enabled?
        if (!PET_SHOUTING_ENABLED) return;

        // Do we have a pet?
        var pet = player.pet();
        if (pet == null) return;

        // Have we paid (or are an admin) to have the mechanic?
        if (ItemsOnDeath.hasShoutAbility(player)) {

            // Shout something.
            pet.forceChat(SHOUTS.get(World.getWorld().random(SHOUTS.size() - 1)));
        }
    }
}
