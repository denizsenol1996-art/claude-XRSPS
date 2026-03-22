package com.twisted.game.world.entity.combat.method.impl.npcs.raids.tob;

import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.position.Area;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since January 07, 2022
 */
public  class Sotetseg extends CommonCombatMethod {

    private static final Graphic RED_BALL_SPLASH = new Graphic(1605);
    private static final Area ARENA = new Area(3273, 4308, 3287, 4334);
    private static final int ATTACK = 8139;

    private int attacks = 0;
    private int normalAttacks = 0;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        attacks++;
        if (attacks == 10) { //big spec
            attacks = 0;
            mob.animate(ATTACK - 1);
            new Projectile(mob, target, 1604, 1, 350, 35, 30, 0).sendProjectile();
            Task task = new Task("SotetsegBigSpecTask", 1) {

                int cycle = 0;
                int inRange = 0;
                final List<Mob> targets = getPossibleTargets(mob);
                final int damage = targets.size() > 2 ? World.getWorld().random(135, 150) : World.getWorld().random(55, 70);

                @Override
                public void execute() {
                    if (cycle >= 12) {
                        if (mob.dead()) {
                            stop();
                            return;
                        }
                        for (Mob t : targets) {
                            if (t.isPlayer() && !t.getAsPlayer().dead() && t.tile().isWithinDistance(target.tile(), 1)) {
                                inRange++;
                            }
                        }
                        for (Mob t : targets) {
                            if (t.isPlayer() && !t.getAsPlayer().dead() && t.tile().isWithinDistance(target.tile(), 1)) {
                                t.hit(mob, damage / inRange, 0);
                                t.getAsPlayer().graphic(RED_BALL_SPLASH.id(), RED_BALL_SPLASH.height(), 0);
                            }
                        }
                        stop();
                    }
                    cycle++;
                }

            };
            TaskManager.submit(task);
        } else {
            mob.animate(ATTACK);
            //Normal attack
            normalAttacks++;
            if(normalAttacks == 5) {
                normalAttacks = 0;
                final List<Mob> chainable = getPossibleTargets(mob);
                Mob chain = World.getWorld().randomTypeOfList(chainable
                    .stream()
                    .filter(player -> player.tile().inArea(ARENA))
                    .collect(Collectors.toList()));
                if (chain != null) {
                    if (chain.isPlayer() && target.isPlayer() && !chain.getAsPlayer().dead()) {
                        new Projectile(mob, target, 1607, 0, 175, 35, 30, 0).sendProjectile();
                        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 5, CombatType.RANGED).checkAccuracy().submit();
                    }
                }
                return false;
            }

            new Projectile(mob, target, 1606, 1, 175, 35, 30, 0).sendProjectile();
            if (target.isPlayer()) {
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 6, CombatType.MAGIC).checkAccuracy().submit();
            }
        }
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 24;
    }
}
