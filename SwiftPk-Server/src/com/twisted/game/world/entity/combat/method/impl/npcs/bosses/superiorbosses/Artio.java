package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.superiorbosses;

import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.daily_tasks.DailyTaskManager;
import com.twisted.game.content.daily_tasks.DailyTasks;
import com.twisted.game.content.tasks.impl.Tasks;
import com.twisted.game.task.TaskManager;
import com.twisted.game.task.impl.ForceMovementTask;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness.Callisto;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.entity.mob.FaceDirection;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.ForceMovement;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.NpcIdentifiers.CALLISTO_6609;

public class Artio extends CommonCombatMethod {

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;//Should be one because melee bear
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        Npc npc = (Npc) mob;

        //All attacks are melee
        if (CombatFactory.canReach(npc, CombatFactory.MELEE_COMBAT, target)) {
            // At all times, callisto can initiate the heal.
            if (Utils.rollDie(18, 1)) {
                prepareHeal(npc);
            }
            // Determine if we do a special hit, or a regular hit.
            if (Utils.rollDie(18, 1)) {
                fury(npc, target);
            } else if (Utils.rollDie(6, 1) && !npc.<Boolean>getAttribOr(AttributeKey.CALLISTO_ROAR, false)) {
                roar(npc, target);
            } else {
                target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
                npc.animate(npc.attackAnimation());
            }
        }
        return true;
    }

    /**
     * Callisto unleashes a shockwave against his target. When this happens, a game message will appear saying that he has used the ability against you,
     * just like Vet'ion's earthquake and Venenatis' web attack. This attack has no cooldown and can hit up to 60 in one attack. Callisto will use this ability much more often
     * the further you are from him. The projectile of this attack is similar to Wind Wave.
     */
    private void fury(Npc npc, Mob target) {
        npc.animate(4925);
        new Projectile(npc, target, 395, 40, 60, 31, 43, 0).sendProjectile();

        Chain.bound(null).name("CallistoFuryTask").runFn(2, () -> {
            target.hit(npc, Utils.random(60), CombatType.MELEE).checkAccuracy().submit();
            ((Player)target).message("Callisto's fury sends an almighty shockwave through you.");
            target.stun(4);
            target.graphic(245, 124, 0);
        });
    }

    /**
     * A blast will appear under Callisto. Even though the game states that Callisto will prepare to heal himself based on the damage dealt to him,
     * he actually heals himself during this time for a small amount.
     */
    private void prepareHeal(Npc npc) {
        npc.graphic(157);
        npc.putAttrib(AttributeKey.CALLISTO_DMG_HEAL, true);
        npc.heal(Utils.random(3, 10));
    }

    /**
     * A small white projectile flies from the player straight to Callisto. When this happens, you will be knocked back several spaces from your current location,
     * and a game message will appear stating "Callisto's roar knocks you backwards.", dealing 3 damage.
     */
    private void roar(Npc npc, Mob target) {
        npc.putAttrib(AttributeKey.CALLISTO_ROAR, true);
        if (target.isPlayer()) {
            Direction direction = Direction.of(target.tile().x - npc.tile().x, target.tile().y - npc.tile().y);

            Tile tile = target.tile().transform(direction.x() * 3, direction.y() * 3);

            FaceDirection face = FaceDirection.forTargetTile(npc.tile(), target.tile());

            int[][] area = World.getWorld().clipAround(tile, 3);

            for (int[] array : area) {
                for (int value : array) {
                    if (value != 0) {
                        npc.clearAttrib(AttributeKey.CALLISTO_ROAR);
                        return;
                    }
                }
            }
            ((Player)target).message("Callisto's roar throws you backwards.");
            target.animate(846);
            TaskManager.submit(new ForceMovementTask(target.getAsPlayer(), 3, new ForceMovement(target.getAsPlayer().tile().clone(), new Tile(direction.x() * 3, direction.y() * 3), 0, 15, face.direction)));
            Chain.bound(null).name("CallistoRoarTask").runFn(3, () -> target.hit(npc, 3, CombatType.MELEE).checkAccuracy().submit());
        }
        npc.clearAttrib(AttributeKey.CALLISTO_ROAR);
    }

    @Override
    public void onRespawn(Npc npc) {
        npc.transmog(CALLISTO_6609);
        npc.setCombatMethod(new Callisto());
    }

    @Override
    public void onDeath(Player killer, Npc npc) {
        killer.getTaskMasterManager().increase(Tasks.CALLISTO);
        AchievementsManager.activate(killer, Achievements.BEAR_GRYLLS_I, 1);
        AchievementsManager.activate(killer, Achievements.BEAR_GRYLLS_II, 1);
        AchievementsManager.activate(killer, Achievements.BEAR_GRYLLS_III, 1);
        DailyTaskManager.increase(DailyTasks.WILDERNESS_BOSS, killer);
    }
}
