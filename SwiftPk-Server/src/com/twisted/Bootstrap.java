package com.twisted;

import com.twisted.game.GameBuilder;
import com.twisted.game.content.TriviaBot;
import com.twisted.game.content.announcements.dyk.DidYouKnowTask;
import com.twisted.game.content.areas.wilderness.content.boss_event.WildernessBossEvent;
import com.twisted.game.content.areas.wilderness.content.todays_top_pkers.TopPkers;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.GwdLogic;
import com.twisted.game.world.items.Item;
import com.twisted.net.HostBlacklist;
import com.twisted.net.NetworkBuilder;

/**
 * The bootstrap that will prepare the game, network, and various utilities.
 * This class effectively enables Eldritch to be put online.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Bootstrap {

    /**
     * The port that the {@link NetworkBuilder} will listen for connections on.
     */
    private final int port;

    /**
     * The network builder that will initialize the core components of the
     * network.
     */
    private final NetworkBuilder networkBuilder = new NetworkBuilder();

    /**
     * The game builder that will initialize the core components of the game.
     */
    private final GameBuilder gameBuilder = new GameBuilder();

    /**
     * Creates a new {@link Bootstrap}.
     *
     * @param port
     *            the port that the network handler will listen on.
     */
    Bootstrap(int port) {
        this.port = port;
    }

    /**
     * Binds the core of the server together and puts Eldritch online.
     *
     * @throws Exception
     *             if any errors occur while putting the server online.
     */
    public void bind() throws Exception {
        gameBuilder.initialize();
        networkBuilder.initialize(port);
        GwdLogic.onServerStart();
        TaskManager.submit(new TriviaBot());//bloodrevsupdate
        TaskManager.submit(new NpcSpawnInWild());//bloodrevsupdate
        TaskManager.submit(new EventNpcSpawn());
        HostBlacklist.loadBlacklist();
        if (GameServer.properties().enableDidYouKnowMessages) {
            TaskManager.submit(new DidYouKnowTask());
        }
        if (GameServer.properties().enableWildernessBossEvents && GameServer.properties().pvpMode) {// Events only on PvP.
            WildernessBossEvent.onServerStart();
            TopPkers.SINGLETON.init();
        }
        Item.onServerStart();
    }
}
