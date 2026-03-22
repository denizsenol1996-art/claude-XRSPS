package com.twisted.game.content.seasonal_events.christmas;

import com.twisted.game.task.Task;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Color;

public class SnowMonsterSpawnTask extends Task {

    private static final SnowMonsterSpawnTask SINGLETON = new SnowMonsterSpawnTask(null, null);

    public static SnowMonsterSpawnTask getSingleton() {
        return SINGLETON;
    }

    public static final boolean TESTING = false;
    public static final int TEST_TICKS = 18;//10 seconds
    public static final int SNOW_MONSTER = 15031;
    public static final int WALK_RADIUS = 12;

    private final Player player;
    private Npc snowMonster;

    /**
     * The snow monster task, checks every 500 ticks if the npc should respawn.
     * @param player The task owner
     * @param snowMonster The personal snow monster for the task owner {@link Player}
     */
    SnowMonsterSpawnTask(Player player, Npc snowMonster) {
        super("SnowMonsterSpawnTask",1,false);
        this.player = player;
        this.snowMonster = snowMonster;
    }

    @Override
    protected void execute() {
        //Player is offline stop task
        if(!player.isRegistered()) {
            stop();
            return;
        }

        var timeUntillNextCheck = player.<Integer>getAttribOr(AttributeKey.SNOW_MONSTER_TIMER, TESTING ? TEST_TICKS : 500) - 1;
        player.putAttrib(AttributeKey.SNOW_MONSTER_TIMER, timeUntillNextCheck);

        //Timer check
        if(timeUntillNextCheck <= 0) {
            player.putAttrib(AttributeKey.SNOW_MONSTER_TIMER, TESTING ? TEST_TICKS : 500);//Reset back to 500 ticks
            //First check if monster is still alive
            if(snowMonster != null && snowMonster.isRegistered()) {
                //Monster is still alive, send current where abouts.

                //Location
                SnowMonsterLocations snowMonsterLocations = player.getAttribOr(AttributeKey.SNOW_MONSTER_LOCATION, null);

                //Location set, write current monster location
                if(snowMonsterLocations != null) {
                    player.message(Color.PURPLE.wrap("The assigned " + snowMonster.def().name + " is still roaming around the "+ snowMonsterLocations.location)+".");
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(snowMonster != null) {
            //Snow monster was still alive somehow kill it!
            snowMonster.remove();
            snowMonster = null;
        }
    }
}
