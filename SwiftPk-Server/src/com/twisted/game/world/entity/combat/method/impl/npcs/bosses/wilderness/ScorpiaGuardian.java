package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness;

import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.route.routes.DumbRoute;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.NpcIdentifiers.SCORPIA;

/**
 * @author Patrick van Elderen | February, 24, 2021, 19:13
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class ScorpiaGuardian {

    public static void heal(Npc scorpia, Npc minion) {
        if (scorpia.id() == SCORPIA) {
            Chain.bound(null).runFn(8, () -> {
                if(minion.tile().isWithinDistance(scorpia.tile(), 2)) {
                    scorpia.heal(1);
                    new Projectile(minion, scorpia,109,50,100,53,31,0).sendProjectile();
                }
            });
        }

        //If they do not heal Scorpia in 15 seconds, they will despawn.
        TaskManager.submit(new Task("ScorpiaGuardianTask", 1) {
            int no_heal_ticks = 0;
            @Override
            protected void execute() {
                if(minion.dead() || minion.finished() || scorpia.dead() || scorpia.finished()) {
                    stop();
                    return;
                }

                if(!minion.tile().isWithinDistance(scorpia.tile(), 2) && !minion.finished()) {
                    no_heal_ticks++;
                }

                if(no_heal_ticks == 25) {
                    World.getWorld().unregisterNpc(minion);
                    stop();
                    return;
                }

                DumbRoute.step(minion, scorpia, 1);
            }
        });
    }
}
