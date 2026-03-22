package com.twisted.game.content.seasonal_events.christmas;

import com.twisted.game.task.TaskManager;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Color;
import com.twisted.util.Tuple;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.game.world.entity.AttributeKey.SNOW_MONSTER_LOCATION;



public class SnowMonster {

    private final Player player;

    public SnowMonster(Player player) {
        this.player = player;
    }

    public static boolean SNOW_MONSTER_SPAWN_TASK_ENABLED = false;
    /**
        * Spawns a snow monster
     */
    public void spawn() {

        if (!SNOW_MONSTER_SPAWN_TASK_ENABLED) return;

        //Generate a location
        SnowMonsterLocations snowMonsterLocations = SnowMonsterLocations.generateLocation();
        //Store the player based location
        player.putAttrib(SNOW_MONSTER_LOCATION, snowMonsterLocations);
        //Create npc instance
        Npc snowMonster = new Npc(SnowMonsterSpawnTask.SNOW_MONSTER, snowMonsterLocations.tile).spawn(false);
        snowMonster.walkRadius(SnowMonsterSpawnTask.WALK_RADIUS);
        snowMonster.putAttrib(AttributeKey.OWNING_PLAYER, new Tuple<>(player.getIndex(), player));//Set a player owner, only this player can kill it.
        player.message(Color.PURPLE.wrap("An " + snowMonster.def().name + " has been seen somewhere near the " + snowMonsterLocations.location + "."));

        //Activate task
        player.putAttrib(AttributeKey.SNOW_MONSTER_TIMER, SnowMonsterSpawnTask.TESTING ? SnowMonsterSpawnTask.TEST_TICKS : 500);
        TaskManager.submit(new SnowMonsterSpawnTask(player, snowMonster));
    }

    /**
        * Spawn first monster on login
     */
    public void activateTask() {

        if (!SNOW_MONSTER_SPAWN_TASK_ENABLED) return;

        player.message(Color.PURPLE.wrap("You sense a powerful presence somewhere on the map."));
        Chain.bound(null).runFn(SnowMonsterSpawnTask.TESTING ? SnowMonsterSpawnTask.TEST_TICKS : 500, this::spawn);
    }

}
