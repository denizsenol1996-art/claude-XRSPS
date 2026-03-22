package com.twisted.game.content.tournaments;

import com.twisted.GameServer;
import com.twisted.game.content.interfaces.BonusesInterface;
import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.mob.player.MagicSpellbook;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import com.twisted.util.timers.TimerKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.twisted.game.content.tournaments.TournamentUtils.*;
import static java.lang.String.format;

/**
 * To read how it works see {@link TournamentManager}
 *
 * @author Shadowrs (tardisfan121@gmail.com)
 */
public class Tournament {
    public Map<Player, Map<double[], int[]>> playerSkillMap = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(Tournament.class);
    public int maxParticipants = GameServer.properties().tournamentMaxParticipants, minimumParticipants = 2;
    final List<Player> inLobby = new ArrayList<>(0);
    final List<String> macAddressesInLobby = new ArrayList<>(0);
    private final TournamentManager.TornConfig config;

    public long lobbyOpenTimeMs;

    final List<Player> fighters = new ArrayList<>(0);

    final List<Player> spectators = new ArrayList<>(0);

    private long startedAt;
    Player winner;

    final Item reward;
    private int tick;

    public Tournament(TournamentManager.TornConfig config) {
        this.config = config;
        reward = setRewards();
    }

    public Tournament(TournamentManager.TornConfig config, Item reward) {
        this.config = config;
        this.reward = reward;
    }

    boolean inProgress() {
        return fighters.size() > 0;
    }

    String getTypeName() {
        return config.key;
    }

    boolean lobbyFull() {
        return inLobby.size() >= maxParticipants;
    }

    String lobbyFullMessage() {
        return format("The lobby has reached its maximum participants (%s). Be sure to arrive earlier next time!", maxParticipants);
    }

    void enterLobby(Player player) {
        if (player.ironMode().isUltimateIronman()) {
            player.message(Color.RED.wrap("Ultimate Ironman cannot participate in this activity."));
            return;
        }
        if (inLobby.size() >= maxParticipants) return;
        if (!fighters.isEmpty()) return;
        if (player.getParticipatingTournament() != null) return;
        if (!player.getInventory().isEmpty()) player.getBank().depositInventory();
        if (!player.getEquipment().isEmpty()) player.getBank().depositeEquipment();
        if (!player.getLootingBag().isEmpty()) player.getLootingBag().depositLootingBag();
        if (!player.getRunePouch().isEmpty()) player.getRunePouch().bankRunesFromNothing();
        double[] cachedXp = Arrays.copyOf(player.skills().xp(), player.skills().xp().length);
        int[] cachedLevels = Arrays.copyOf(player.skills().levels(), player.skills().levels().length);
        player.setSavedTornamentXp(cachedXp);
        player.setSavedTornamentLevels(cachedLevels);
        Map<double[], int[]> skillCache = new HashMap<>();
        skillCache.put(cachedXp, cachedLevels);
        playerSkillMap.put(player, skillCache);
        player.teleport(getRandomTile(LOBBY_AREA));
        player.setInTournamentLobby(true);
        player.setParticipatingTournament(this);
        inLobby.add(player);
        player.getRunePouch().clear();
        player.getInterfaceManager().close();
        Skulling.unskull(player);
        player.message(format("You've entered the %s tournament. Good luck %s.", config.key, player.getUsername()));
        if (inLobby.size() < minimumParticipants)
            player.message("The tournament requires at least " + (minimumParticipants - inLobby.size()) + " more players to start.");
        player.getPacketSender().sendString(TournamentUtils.TOURNAMENT_WALK_TIMER, "Starting in 30...");
        setLoadoutOnPlayer(player);
    }

    public void resetAllVars(Player player) {
        player.resetAttributes();
    }

    public void tick() {
        if (tick++ == Integer.MAX_VALUE) {
            tick = 0;
        }
        if (fighters.size() == 0 && inLobby.size() > 0) {
            if (tick % 60 == 0) {
                if (inLobby.size() < minimumParticipants) {
                    for (Player player : inLobby) {
                        player.message("The tournament requires at least " + (minimumParticipants - inLobby.size()) + " more players to start.");
                    }
                }
            }
        }
        checkForNextRoundStart();
    }

    public void checkForWinner() {
        if (fighters.size() == 1) {
            winner = fighters.get(0);
            var wins = winner.<Integer>getAttribOr(AttributeKey.TOURNAMENT_WINS, 0);
            wins += 1;
            winner.putAttrib(AttributeKey.TOURNAMENT_WINS, wins);
            var points = winner.<Integer>getAttribOr(AttributeKey.TOURNAMENT_POINTS, 0);
            points += 4;
            winner.putAttrib(AttributeKey.TOURNAMENT_POINTS, points);
            winner.message("You have won the " + fullName() + " Tournament! You now have a total of " + Color.BLUE.wrap("" + points) + " tournaments.");
            winner.message("You've received 4 tournament points!.");
            World.getWorld().sendWorldMessage(format("<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Tournament</col>]: %s has won the %s tournament!", winner.getUsername(), fullName()));
            logger.info(format("<img=1876>[<col=" + Color.MEDRED.getColorValue() + ">Tournament</col>]: %s has won the %s tournament!", winner.getUsername(), fullName()));
            logger.info("PvP tournament rewards: " + reward.toString());
            winner.getPacketSender().sendInteractionOption("null", 2, true);
            winner.getPacketSender().sendEntityHintRemoval(true);
            onTournyClosed();
        }
    }

    public boolean firstRoundStarted;

    public void checkForNextRoundStart() {
        if (fighters.size() >= 2) {
            boolean fightInProgress = false;
            for (Player player : fighters) {
                if (player.getTournamentOpponent() != null) {
                    fightInProgress = true;
                    break;
                }
            }

            if (!fightInProgress) {
                startedAt = System.currentTimeMillis();
                Player last = null;
                Collections.shuffle(fighters);
                for (int i = 0; i < fighters.size(); i++) {
                    final Player player = fighters.get(i);
                    if (i % 2 == 0) {
                        last = player;
                        if (i == fighters.size() - 1) {
                            player.message("There was no opponent available. You will fight the winner of next match.");
                            player.getMovementQueue().setBlockMovement(false);
                            player.setTournamentSpectating(true);
                            break;
                        }
                    } else {
                        if (last != null) {
                            prepareMatch(player, last);
                            prepareMatch(last, player);
                            last = null;
                        } else {
                            player.message("There was no opponent available, so you will fight the winner of next match.");
                            player.getMovementQueue().setBlockMovement(false);
                            player.setTournamentSpectating(true);
                            break;
                        }
                    }
                }
                if (!firstRoundStarted) {
                    firstRoundStarted = true;
                }
            }
        }
    }

    public void onTournyClosed() {
        macAddressesInLobby.clear();
        List<Player> temp = new ArrayList<>(0);
        temp.addAll(inLobby);
        temp.addAll(spectators);
        temp.addAll(fighters);
        for (Player player : temp) {
            TournamentManager.leaveTourny(player, false);
        }
    }

    public void setLoadoutOnPlayer(Player player) {
        TournamentManager.wipeLoadout(player);
        player.setSpecialAttackPercentage(100);
        CombatSpecial.updateBar(player);

        TournamentManager.Loadout loadout = TournamentManager.loadouts.stream().filter(l -> l.key.equalsIgnoreCase(config.key)).findFirst().orElse(null);
        if (loadout == null) {
            logger.error("no loadout exists for tournaments loadout key " + config.key + "!");
            loadout = TournamentManager.loadouts.get(0);
        }
        for (TournamentManager.Tuple<Integer, Integer> stat : loadout.stats) {
            int skill = stat.x;
            int level = stat.y;
            player.skills().setLevel(skill, level);
            player.skills().setXp(skill, Skills.levelToXp(level));
            player.skills().update(skill);
        }
        player.skills().recalculateCombat();
        for (int i = 0; i < loadout.equip.capacity(); i++) {
            if (loadout.equip.getItems()[i] == null)
                continue;
            player.inventory().set(i, loadout.equip.getItems()[i].clone(), false);
        }
        for (int i = 0; i < player.inventory().capacity(); i++) {
            if (player.inventory().get(i) == null) continue;
            player.getEquipment().equip(i);
            BonusesInterface.sendBonuses(player);
        }
        for (int i = 0; i < loadout.inv.capacity(); i++) {
            if (loadout.inv.getItems()[i] == null)
                continue;
            player.inventory().set(i, loadout.inv.getItems()[i].clone(), false);
        }
        for (int i = 0; i < loadout.runepouch.capacity(); i++) {
            if (loadout.runepouch.getItems()[i] == null) continue;
            player.getRunePouch().deposit(loadout.runepouch.getItems()[i].clone());
            player.getRunePouch().refresh();
        }
        player.getRunePouch().refresh();
        player.setPreviousSpellbook(player.getSpellbook());
        MagicSpellbook.changeSpellbook(player, MagicSpellbook.forId(loadout.spellbook), false);
        player.inventory().refresh();
        player.getEquipment().refresh();
        player.setQueuedAppearanceUpdate(true);
    }

    /**
     * Configure the player for battle: set equipment, inventory, spellbook, runepouch etc to Tournament Preset
     *
     * @param player
     * @param last
     */
    private void prepareMatch(Player player, Player last) {
        if (firstRoundStarted) {
            // only give loadout on rounds after 1. they get the loadout in lobby for round 1, allowing them to move inv about.
            setLoadoutOnPlayer(player);
        }
        //Reset spectating state, we could of been spectating when we had a free tournament by
        player.setTournamentSpectating(false);

        player.setTournamentOpponent(last);
        //logger.trace("Sending entity hint for tournament");
        player.getPacketSender().sendEntityHint(last);

        //Set new waiting timers
        player.getTimerRepository().extendOrRegister(TimerKey.TOURNAMENT_FIGHT_IMMUNE, TournamentUtils.FIGHT_IMMUME_TIMER);
        player.getPacketSender().sendString(TournamentUtils.TOURNAMENT_WALK_TIMER, "00:30");
        TaskManager.submit(new Task() {
            // does this repeat or is it a 1 time thing? Task one time thing
            @Override
            protected void execute() {

                if (player.getIndex() < 0) {
                    logger.error("Player index less than 0 for prepareMatch");
                    stop();
                    return;
                }

                if (!player.inActiveTournament()) {
                    stop();
                    return;
                }

                int secs = (int) (player.getTimerRepository().left(TimerKey.TOURNAMENT_FIGHT_IMMUNE) * 0.6); //times 0.6 makes it secs but interface / updates are every 0.6 so it needs to be a client side timer
                player.getPacketSender().sendString(TournamentUtils.TOURNAMENT_WALK_TIMER, secs > 9 ? "00:" + secs : "00:0" + secs);

                if (secs == 0) {
                    player.forceChat("Fight!");
                    player.getPacketSender().sendString(TournamentUtils.TOURNAMENT_WALK_TIMER, "Fight!");
                    player.clearAttrib(AttributeKey.TOURNAMENT_COUNTDOWN);
                    stop();
                } else {
                    if (secs != (int) player.getAttribOr(AttributeKey.TOURNAMENT_COUNTDOWN, -1)) {
                        //If maximum seconds is greater than 20 we use do % 10 otherwise use % 5.
                        if (secs % 10 == 0) {
                            player.message(format("Fight starts in seconds %s...", secs));
                        }
                        player.putAttrib(AttributeKey.TOURNAMENT_COUNTDOWN, secs);
                    }
                }
            }
        }.bind(player));
    }

    String lobbyTimeMessage() {
        int secs = TournamentManager.settings.lobbyTime;
        return secs < 60 ? secs + " seconds" : secs % 60 == 1 ? "1 minute" : secs / 60 + " minutes";
    }

    /**
     * @return true when the tournament has run, battles fought and winner announced
     */
    public boolean finished() {
        return winner != null && startedAt > 0;
    }

    /**
     * Translates the config.key ("nhpure") to a full name "Nh Pure Tournament"
     */
    public String fullName() {
        return switch (config.key) {
            case "Hybrid" -> "Hybrid PK tournament";
            case "NH Pure" -> "Pure No Honor PK tournament";
            default -> config.key;
        };
    }

    /**
     * The list of items rewarded from a tournament
     */
    private Item setRewards() {
        var possible_rewards = new Item[]{new Item(30223, 1)};
        return World.getWorld().random(possible_rewards);
    }

    public int getMaxParticipants() {
        return this.maxParticipants;
    }

    public int getMinimumParticipants() {
        return this.minimumParticipants;
    }

    public List<Player> getInLobby() {
        return this.inLobby;
    }

    public TournamentManager.TornConfig getConfig() {
        return this.config;
    }

    public long getLobbyOpenTimeMs() {
        return this.lobbyOpenTimeMs;
    }

    public List<Player> getFighters() {
        return this.fighters;
    }

    public List<Player> getSpectators() {
        return this.spectators;
    }

    public Player getWinner() {
        return this.winner;
    }

    public Item getReward() {
        return this.reward;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setMinimumParticipants(int minimumParticipants) {
        this.minimumParticipants = minimumParticipants;
    }

    public void setLobbyOpenTimeMs(long lobbyOpenTimeMs) {
        this.lobbyOpenTimeMs = lobbyOpenTimeMs;
    }
}


