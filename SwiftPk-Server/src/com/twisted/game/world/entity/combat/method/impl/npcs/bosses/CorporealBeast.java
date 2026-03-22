package com.twisted.game.world.entity.combat.method.impl.npcs.bosses;

import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.twisted.game.content.tasks.impl.Tasks;
import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.NpcDeath;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

public class CorporealBeast extends CommonCombatMethod {

    private final int splashing_magic_gfx = 315;
    private final int splashing_magic_tile_gfx = 317;
    private final int corporeal_beast_animation = 1680;
    private final int splashing_magic_attack_damage = 30;

    public static final Area CORPOREAL_BEAST_AREA = new Area(2974, 4371, 2998, 4395);

    private Task stompTask;
    private void checkStompTask() {
        if (stompTask == null) {
            stompTask = new Task("checkStompTask",7) {
                @Override
                protected void execute() {
                    if (mob.dead() || !mob.isRegistered() || !target.tile().inArea(CORPOREAL_BEAST_AREA)) {
                        stop();
                        return;
                    }
                    if (target.tile().region() != 9770) {
                        World.getWorld().getPlayers().forEachInArea(CORPOREAL_BEAST_AREA, p -> {
                            if (p.boundaryBounds().inside(mob.tile(), mob.getSize())) {
                                stompAttack(mob.getAsNpc(), p);
                            }
                        });
                    } else {
                        if (target.boundaryBounds().inside(mob.tile(), mob.getSize())) {
                            stompAttack(mob.getAsNpc(), (Player) target);
                        }
                    }
                }

                @Override
                public void onStop() {
                    mob.getCombat().reset();
                }
            }.bind(mob);
            TaskManager.submit(stompTask);
        }
    }

    /**
     * If the player steps under the Corporeal Beast, it may perform a stomp attack that will always deal 30–51 damage.
     * This attack is on a timer that checks if any players are under the Corporeal Beast every 7 ticks (4.2 seconds). OK
     */
    public void stompAttack(Npc corp, Player player) {
        int maxHit = Utils.random(31, 51);

        corp.animate(1686);
        player.hit(corp, maxHit, 1, CombatType.MELEE).checkAccuracy().submit();
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        checkStompTask();
        //Check if we're able to melee our opponent.. and if we are, roll a die to smack em in the mouth
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollDie(3, 1)) {
            //The Melee attack is only used when the player is standing in Melee distance and can hit up to 33.
            //The Protect from Melee prayer will negate all damage from this attack.
            mob.animate(1682);
            mob.getAsNpc().combatInfo().maxhit = 33;
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target,CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
            //Check if we attack the player using a stat draining magic attack
        } else if (World.getWorld().rollDie(2, 1)) {
            mob.animate(corporeal_beast_animation);
            mob.getAsNpc().combatInfo().maxhit = 55;
            int stat_draining_ranged_gfx = 314;
            new Projectile(mob, target, stat_draining_ranged_gfx, 15, 45, 40, 25, 10).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target,CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().submit();
            stat_draining_magic_attack(target);
        } else if (World.getWorld().rollDie(2, 1)) {
            mob.animate(corporeal_beast_animation);
            mob.getAsNpc().combatInfo().maxhit = 65;
            splashing_magic_attack(((Npc)mob), target);
            mob.getTimerRepository().register(TimerKey.COMBAT_ATTACK, 4);
        } else {
            mob.animate(corporeal_beast_animation);
            mob.getAsNpc().combatInfo().maxhit = 65;
            int high_damage_magic_gfx = 316;
            new Projectile(mob, target, high_damage_magic_gfx, 15, 45, 40, 25, 10).sendProjectile();
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target,CombatType.MAGIC), 2, CombatType.MAGIC).checkAccuracy().submit();
        }
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 14;
    }

    private void stat_draining_magic_attack(Mob target) {
        Player player = (Player) target;
        //Check to see if we apply the 'special' effect.
        if (Utils.rollDie(3, 1)) {
            int reduction = Utils.random(3);
            //Decide if we reduce their magic or prayer.
            if (Utils.rollDie(2, 1)) {
                if (player.skills().level(Skills.MAGIC) < reduction) {
                    target.skills().setLevel(Skills.MAGIC, 0);
                } else {
                    player.skills().setLevel(Skills.MAGIC, player.skills().level(Skills.MAGIC) - reduction);
                }
                player.message("Your Magic has been slightly drained.");
            } else if (player.skills().level(Skills.PRAYER) > reduction) {
                if (player.skills().level(Skills.PRAYER) < reduction) {
                    player.skills().setLevel(Skills.PRAYER, 0);
                } else {
                    player.skills().setLevel(Skills.PRAYER, player.skills().level(Skills.PRAYER) - reduction);
                }
                player.message("Your Prayer has been slightly drained.");
            }
        }
    }

    private void splashing_magic_attack(Npc npc, Mob target) {
        int x = target.tile().x; //The target's x tile
        int z = target.tile().y; //The target's z tile

        int random_one = Utils.random(1);
        int random_two = Utils.random(2);

        //Handle the initial spell
        Tile initial_splash = new Tile(x, z, target.tile().level);
        int initial_splash_distance = npc.tile().distance(initial_splash) / 2;
        int initial_splash_delay = Math.max(1, (20 + initial_splash_distance * 12) / 30);

        //Send the projectile from the NPC -> players tile
        new Projectile(npc.tile().transform(2, 2, 0), initial_splash, 0, splashing_magic_gfx, 28 * initial_splash_distance, initial_splash_delay, 40, 0, 10).sendProjectile();

        //Send the tile graphic
        target.getAsPlayer().getPacketSender().sendTileGraphic(splashing_magic_tile_gfx, initial_splash, 1, 28 * initial_splash_distance);

        //Animate the NPC
        npc.animate(corporeal_beast_animation, initial_splash_delay);

        //Create a delay before checking if the player is on the initial tile
        Chain.bound(null).name("initial_splash_distance_1_task").runFn(initial_splash_distance, () -> {
            //Check to see if the player's on the initial tile
            if (target.tile().inSqRadius(initial_splash,1) && target.tile().inArea(2974, 4371, 2998, 4395)) {
                target.hit(npc, Utils.random(splashing_magic_attack_damage));
            }
        });

        //Handle the first splash
        Tile splash_one = new Tile(initial_splash.x + 2 + random_one, initial_splash.y + 2 + random_one);
        int splash_one_distance = initial_splash.distance(splash_one);
        int splash_one_delay = Math.max(1, (20 + splash_one_distance * 12) / 30);

        //Handle the second splash
        Tile splash_two = new Tile(initial_splash.x, initial_splash.y + 2 + random_one);
        int splash_two_distance = initial_splash.distance(splash_two);

        //Handle the third splash
        Tile splash_three = new Tile(initial_splash.x + 2 + random_one, initial_splash.y + random_two);
        int splash_three_distance = initial_splash.distance(splash_three);

        //Handle the fourth splash
        Tile splash_four = new Tile(initial_splash.x + 2 + random_one, initial_splash.y - 2 + random_one);
        int splash_four_distance = initial_splash.distance(splash_four);

        //Handle the fifth splash
        Tile splash_five = new Tile(initial_splash.x - 2 + random_one, initial_splash.y + 2 + random_two);
        int splash_five_distance = initial_splash.distance(splash_five);

        //Handle the sixth splash
        Tile splash_six = new Tile(initial_splash.x - 2 + random_two, initial_splash.y - 2 + random_two);
        int splash_six_distance = initial_splash.distance(splash_six);

        //Create a delay before sending the splash projectiles
        Chain.bound(null).name("initial_splash_distance_2_task").runFn(initial_splash_distance, () -> {
            Player[] close = target.closePlayers(64);

            //Send the projectiles
            for (Player player : close) {
                new Projectile(initial_splash, splash_one, 0, splashing_magic_gfx, 28 * initial_splash_distance, splash_one_delay, 0, 0, 10).sendFor(player);
                new Projectile(initial_splash, splash_two, 0, splashing_magic_gfx, 28 * initial_splash_distance, splash_one_delay, 0, 0, 10).sendFor(player);
                new Projectile(initial_splash, splash_three, 0, splashing_magic_gfx, 28 * initial_splash_distance, splash_one_delay, 0, 0, 10).sendFor(player);
                new Projectile(initial_splash, splash_four, 0, splashing_magic_gfx, 28 * initial_splash_distance, splash_one_delay, 0, 0, 10).sendFor(player);
                new Projectile(initial_splash, splash_five, 0, splashing_magic_gfx, 28 * initial_splash_distance, splash_one_delay, 0, 0, 10).sendFor(player);
                new Projectile(initial_splash, splash_six, 0, splashing_magic_gfx, 28 * initial_splash_distance, splash_one_delay, 0, 0, 10).sendFor(player);
            }

            //Send the tile graphic
            target.getAsPlayer().getPacketSender().sendTileGraphic(splashing_magic_tile_gfx, splash_one, 1, 28 * splash_one_distance);
            target.getAsPlayer().getPacketSender().sendTileGraphic(splashing_magic_tile_gfx, splash_two, 1, 28 * splash_two_distance);
            target.getAsPlayer().getPacketSender().sendTileGraphic(splashing_magic_tile_gfx, splash_three, 1, 28 * splash_three_distance);
            target.getAsPlayer().getPacketSender().sendTileGraphic(splashing_magic_tile_gfx, splash_four, 1, 28 * splash_four_distance);
            target.getAsPlayer().getPacketSender().sendTileGraphic(splashing_magic_tile_gfx, splash_five, 1, 28 * splash_five_distance);
            target.getAsPlayer().getPacketSender().sendTileGraphic(splashing_magic_tile_gfx, splash_six, 1, 28 * splash_six_distance);

            //If any player on the world is on the tile -> deal damage.
            hitAfterDelay(splash_one, close);
            hitAfterDelay(splash_two, close);
            hitAfterDelay(splash_three, close);
            hitAfterDelay(splash_four, close);
            hitAfterDelay(splash_five, close);
            hitAfterDelay(splash_six, close);
        });
    }

    private void hitAfterDelay(Tile tile, Player[] opts) {
        for (Player p : opts) {
            if (p.tile().inSqRadius(tile, 1) && p.tile().inArea(2974, 4371, 2998, 4395))
                p.hit(mob, Utils.random(30), 1);
        }
    }

    @Override
    public void onDeath(Player killer, Npc npc) {
        AchievementsManager.activate(killer, Achievements.CORPOREAL_CRITTER, 1);
        DailyTaskManager.increase(DailyTasks.CORPOREAL_BEAST, killer);
        killer.getTaskMasterManager().increase(Tasks.CORPOREAL_BEAST);
        NpcDeath.corpDrop(npc);
    }
}
