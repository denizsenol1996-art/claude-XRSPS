package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.custom;

import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.ForceMovementTask;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.ForceMovement;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.game.world.route.Direction;
import com.twisted.game.world.route.routes.DumbRoute;
import com.twisted.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | June, 14, 2021, 14:44
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class BrutalLavaDragon extends CommonCombatMethod {

    private static final int FLYING_DRAGONFIRE = 7871;
    private static final int HEADBUTT = 91;

    private boolean headbutt; //prevents headbutt twice in a row

    private void knockback() {
        int vecX = (target.getAbsX() - getClosestX());
        if (vecX != 0)
            vecX /= Math.abs(vecX); // determines X component for knockback
        int vecY = (target.getAbsY() - getClosestY());
        if (vecY != 0)
            vecY /= Math.abs(vecY); // determines Y component for knockback
        int endX = target.getAbsX();
        int endY = target.getAbsY();
        for (int i = 0; i < 4; i++) {
            if (DumbRoute.getDirection(endX, endY, mob.getZ(), target.getSize(), endX + vecX, endY + vecY) != null) { // we can take this step!
                endX += vecX;
                endY += vecY;
            } else
                break; // cant take the step, stop here
        }
        Direction dir;
        if (vecX == -1)
            dir = Direction.EAST;
        else if (vecX == 1)
            dir = Direction.WEST;
        else if (vecY == -1)
            dir = Direction.NORTH;
        else
            dir = Direction.SOUTH;

        if (endX != target.getAbsX() || endY != target.getAbsY()) { // only do movement if we can take at least one step
            if (target != null) {
                Chain.bound(null).runFn(1, () -> {
                    final Player p = target.getAsPlayer();

                    p.lock();
                    p.animate(1157);
                    p.graphic(245, 5, 124);
                    p.hit(mob, 20);
                    p.stun(2, true);
                    int diffX = World.getWorld().random(2);
                    int diffY = World.getWorld().random(2);
                    TaskManager.submit(new ForceMovementTask(p, 1, new ForceMovement(p.tile().clone(), new Tile(diffX, diffY), 10, 60, dir.clientValue)));
                    p.message("The brutal lava dragon roars and throws you backwards.");
                    p.unlock();
                });
            }
        } else {
            target.hit(mob, 20);
            target.animate(1157);
            target.graphic(245, 5, 124);
            target.stun(2, true);
            if (target != null)
                target.message("The brutal lava dragon roars and throws you backwards.");
        }

    }

    private int getClosestX() {
        if (target.getAbsX() < mob.getAbsX())
            return mob.getAbsX();
        else if (target.getAbsX() >= mob.getAbsX() && target.getAbsX() <= mob.getAbsX() + mob.getSize() - 1)
            return target.getAbsX();
        else
            return mob.getAbsX() + mob.getSize() - 1;
    }

    private int getClosestY() {
        if (target.getAbsY() < mob.getAbsY())
            return mob.getAbsY();
        else if (target.getAbsY() >= mob.getAbsY() && target.getAbsY() <= mob.getAbsY() + mob.getSize() - 1)
            return target.getAbsY();
        else
            return mob.getAbsY() + mob.getSize() - 1;
    }


    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
            //10% chance that the wold boss skulls you!
            if (World.getWorld().rollDie(10, 1) && !WildernessArea.inDiamondZone(target.tile())) {
                Skulling.assignSkullState(((Player) target), SkullType.WHITE_SKULL);
                target.message("The " + mob.getMobName() + " has skulled you, be careful!");
            }

            if (World.getWorld().rollDie(15, 1) && !headbutt && target.tile().isWithinDistance(mob.tile(), 3)) {
                mob.forceChat("HEADBUTT");
                mob.animate(HEADBUTT);
                knockback();
                headbutt = true;
            }

            magicAttack((Npc) mob, target);

        return true;
        }


    private void magicAttack(Npc npc, Mob target) {
        npc.face(null); // Stop facing the target
        World.getWorld().getPlayers().forEach(p -> {
                if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                    boolean dragon_fire = World.getWorld().rollDie(2, 1);
                    var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
                    var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

                    Projectile DRAGONFIRE_PROJ_FLYING = new Projectile(mob, p, 54, 50, 12 * tileDist, 150, 32, 0);
                    Projectile FIRE_PROJ_FLYING = new Projectile(mob, p, 1465, 51, 12 * tileDist, 160, 31, 0);

                    (dragon_fire ? DRAGONFIRE_PROJ_FLYING : FIRE_PROJ_FLYING).sendProjectile();
                    mob.animate(FLYING_DRAGONFIRE);
                    mob.getAsNpc().combatInfo().maxhit = dragon_fire ? 58 : 21;

                    p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                    headbutt = false;

            }
        });

        npc.face(target.tile()); // Go back to facing the target.
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }

}
