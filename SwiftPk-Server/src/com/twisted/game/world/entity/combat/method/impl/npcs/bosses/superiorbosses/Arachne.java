package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.superiorbosses;

import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.twisted.game.content.tasks.impl.Tasks;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness.Venenatis;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;

import static com.twisted.util.NpcIdentifiers.VENENATIS_6610;

public class Arachne extends CommonCombatMethod {

    private void rangedAttack() {
        mob.animate(5322);
        // Throw a ranged projectile
        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        Projectile projectile = new Projectile(mob, target, 1379, 20,12 * tileDist, 10, 10, 0);
        projectile.sendProjectile();
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
    }

    private void magicAttack() {
        mob.animate(5322);
        // Throw a magic projectile
        var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
        Projectile projectile = new Projectile(mob, target, 1380, 20,12 * tileDist, 10, 10, 0);
        projectile.sendProjectile();
        var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
    }

    private void meleeAttack() {
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
    }

    private void webAttack() {
        Tile[] positions = {target.tile().copy(),
            new Tile(mob.getAbsX() + Utils.random(-4, mob.getSize() + 4), mob.getAbsY() + Utils.random(-4, mob.getSize() + 4), mob.getZ()),
            new Tile(mob.getAbsX() + Utils.random(-4, mob.getSize() + 4), mob.getAbsY() + Utils.random(-4, mob.getSize() + 4), mob.getZ())};
        for (Tile pos : positions) {
            mob.runFn(1, () -> World.getWorld().tileGraphic(1601, new Tile(pos.getX(), pos.getY(), pos.getZ()), 0, 0)).then(2, () -> {
                if (target == null)
                    return;
                if (target.tile().equals(pos)) {
                    target.hit(mob, World.getWorld().random(10, 15));
                } else if (Utils.getDistance(target.tile(), pos) == 1) {
                    target.hit(mob, 7);
                }
            }).then(2, () -> World.getWorld().tileGraphic(1601, new Tile(pos.getX(), pos.getY(), pos.getZ()), 0, 0)).then(1, () -> {
                if (target == null)
                    return;
                if (target.tile().equals(pos)) {
                    target.hit(mob,World.getWorld().random(10, 18));
                } else if (Utils.getDistance(target.tile(), pos) == 1) {
                    target.hit(mob,10);
                }
            });
        }
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollPercent(25)) {
            meleeAttack();
        } else if (Utils.rollPercent(50)) {
            rangedAttack();
        } else if (Utils.rollPercent(10)) {
            webAttack();
        } else {
            magicAttack();
        }
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 10;
    }

    @Override
    public void onRespawn(Npc npc) {
        npc.transmog(VENENATIS_6610);
        npc.setCombatMethod(new Venenatis());
    }

    @Override
    public void onDeath(Player killer, Npc npc) {
        killer.getTaskMasterManager().increase(Tasks.VENENATIS);
        AchievementsManager.activate(killer, Achievements.BABY_ARAGOG_I, 1);
        AchievementsManager.activate(killer, Achievements.BABY_ARAGOG_II, 1);
        AchievementsManager.activate(killer, Achievements.BABY_ARAGOG_III, 1);
        DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);
    }
}
