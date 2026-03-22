package com.twisted.game;

import com.twisted.GameServer;
import com.twisted.db.Referral;
import com.twisted.game.content.areas.wilderness.content.activity.WildernessActivityManager;
import com.twisted.game.content.clan.ClanRepository;
import com.twisted.game.content.items.mystery_box.MysteryBox;
import com.twisted.game.content.items.mystery_box.impl.ClanBox;
import com.twisted.game.content.new_players.StarterBox;
import com.twisted.game.content.new_players.StarterBoxDarkLord;
import com.twisted.game.content.skill.impl.crafting.Crafting;
import com.twisted.game.content.skill.impl.fletching.Fletching;
import com.twisted.game.content.skill.impl.slayer.Slayer;
import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.content.tradingpost.TradingPost;
import com.twisted.game.world.World;
import com.twisted.game.world.definition.loader.impl.*;
import com.twisted.game.world.entity.mob.player.commands.impl.players.PromoCodeCommand;
import com.twisted.game.world.region.RegionManager;
import com.twisted.net.packet.interaction.PacketInteractionManager;
import com.twisted.util.BackgroundLoader;
import com.twisted.util.PlayerPunishment;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Loads all required necessities and starts processes required
 * for the game to work.
 * 
 * @author Lare96
 */
public class GameBuilder {

    /**
     * The background loader that will load various utilities in the background
     * while the bootstrap is preparing the server.
     */
    private final BackgroundLoader backgroundLoader = new BackgroundLoader();

    /**
     * Initializes this game builder effectively preparing the background
     * startup tasks and game processing.
     *
     * @throws Exception
     *             if any issues occur while starting the network.
     */
    public void initialize() throws Exception {
        RegionManager.init();
        backgroundLoader.init(createBackgroundTasks());
        System.gc();
        GameEngine.getInstance().start();
        if (!backgroundLoader.awaitCompletion()) throw new IllegalStateException("Background load did not complete normally!");
    }

    /**
     * Returns a queue containing all of the background tasks that will be
     * executed by the background loader. Please note that the loader may use
     * multiple threads to load the utilities concurrently, so utilities that
     * depend on each other <b>must</b> be executed in the same task to ensure
     * thread safety.
     *
     * @return the queue of background tasks.
     */
    public Queue<Runnable> createBackgroundTasks() {
        Queue<Runnable> tasks = new ArrayDeque<>();
        tasks.add(ClanRepository::load);
        tasks.add(StarterBox::init);
        tasks.add(Referral::loadReferrals);
        tasks.add(StarterBoxDarkLord::init);//Sbox
        tasks.add(ClanBox::init);
        tasks.add(PromoCodeCommand::init);
        tasks.add(PlayerPunishment::init);
        tasks.add(PacketInteractionManager::init);

        if (GameServer.properties().enableWildernessActivities && GameServer.properties().pvpMode) {
            tasks.add(WildernessActivityManager.getSingleton()::init);
        }

        //Load definitions..
        tasks.add(new BloodMoneyPriceLoader());
        tasks.add(TradingPost::init);
        tasks.add(MysteryBox::load);
        tasks.add(new Slayer()::loadMasters);
        tasks.add(Crafting::load);
        tasks.add(Fletching::load);
        tasks.add(new ShopLoader());
        tasks.add(new ObjectSpawnDefinitionLoader());
        /*if(GameServer.properties().pvpMode) {
            tasks.add(new PresetLoader());// commented for tomorw
        }*/
        tasks.add(new DoorDefinitionLoader());
        if (GameServer.properties().tournamentsEnabled) {
            tasks.add(TournamentManager::initalizeTournaments);
        }
        tasks.add(World.getWorld()::postLoad);
        return tasks;
    }
}
