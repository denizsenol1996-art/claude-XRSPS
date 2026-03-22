package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.superiorbosses;

import com.twisted.game.content.achievements.Achievements;
import com.twisted.game.content.achievements.AchievementsManager;
import com.twisted.game.content.tasks.impl.Tasks;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.cerberus.Cerberus;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.NpcIdentifiers.CERBERUS;

public class Kerberos extends CommonCombatMethod {

    private void rangedAttack() {
        new Projectile(mob, target, 1381, 106, 25, 125, 31, 0, 15, 220).sendProjectile();
        mob.animate(4492);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED), 4, CombatType.RANGED).checkAccuracy().submit();
        target.delayedGraphics(1715, 100, 4);
    }

    private void magicAttack() {
        new Projectile(mob, target, 1382, 106, 25, 125, 31, 0, 15, 220).sendProjectile();
        mob.animate(4492);
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), 4, CombatType.RANGED).checkAccuracy().submit();
        target.delayedGraphics(1710, 100, 4);
    }

    private void meleeAttack() {
        mob.animate(mob.attackAnimation());
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
    }

    private void doubleAttack() {
        mob.forceChat("RAWRRRRRRRRRRRRRR");
        Chain.bound(null).runFn(1, this::rangedAttack).then(3, this::magicAttack);
    }

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target) && Utils.rollPercent(25)) {
            meleeAttack();
        } else if (Utils.rollPercent(50)) {
            rangedAttack();
        } else if (Utils.rollPercent(10)) {
            doubleAttack();
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
        if (npc.tile().region() != 9770) {
            npc.transmog(CERBERUS);
            npc.setCombatMethod(new Cerberus());
        }
    }


    @Override
    public void onDeath(Player killer, Npc npc) {
        killer.getTaskMasterManager().increase(Tasks.CERBERUS);
        AchievementsManager.activate(killer, Achievements.FLUFFY_I, 1);
        AchievementsManager.activate(killer, Achievements.FLUFFY_II, 1);
    }

}
