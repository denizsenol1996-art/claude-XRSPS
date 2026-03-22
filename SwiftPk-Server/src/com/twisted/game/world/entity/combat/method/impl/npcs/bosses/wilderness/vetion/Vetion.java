package com.twisted.game.world.entity.combat.method.impl.npcs.bosses.wilderness.vetion;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;

import java.util.ArrayList;
import java.util.List;

public class Vetion extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (mob.hp() <= 125 && !mob.hasAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED)) {
            spawnHellhounds((Npc) mob, target);
        }

        //If the 5 minute timer has expired we revert vetion back to his original form.
        if (!mob.getTimerRepository().has(TimerKey.VETION_REBORN_TIMER) && ((Npc) mob).transmog() == 6612) {
            ((Npc) mob).transmog(6611);
        }

        if (Utils.rollDie(20, 1)) { // 5% chance the target sends his bitch ass lightning
            doMagic();
        } else if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            mob.animate(5499);
            target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        } else {
            doMagic();
        }
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return 6;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }

    private void doMagic() {
        mob.animate(mob.attackAnimation());
        Tile lightning_one = target.tile();
        Tile lightning_two = lightning_one.transform(1, 0);
        Tile lightning_three = lightning_one.transform(1, 1);
        int tileDist = mob.tile().distance(target.tile());
        int delay = Math.max(1, (30 + tileDist * 12) / 30);

        Chain.bound(null).runFn(2, () -> {
            new Projectile(new Tile(mob.tile().x + -1, mob.tile().y + 1), lightning_one, 0, 280, 10 * tileDist, delay, 70, 45, 0).sendProjectile();
            new Projectile(new Tile(mob.tile().x + -1, mob.tile().y + 1), lightning_two, 0, 280, 10 * tileDist, delay, 70, 45, 0).sendProjectile();
            new Projectile(new Tile(mob.tile().x + -1, mob.tile().y + 1), lightning_three, 0, 280, 10 * tileDist, delay, 70, 45, 0).sendProjectile();

            World.getWorld().tileGraphic(281, lightning_one,0, 10 * tileDist);
            World.getWorld().tileGraphic(281, lightning_one, 0, 10 * tileDist);
            World.getWorld().tileGraphic(281, lightning_two, 0, 10 * tileDist);
            World.getWorld().tileGraphic(281, lightning_three, 0, 10 * tileDist);
        }).then(3, () -> {
            if (target.tile() == (lightning_one) || target.tile() == (lightning_one.transform(1, 0)) || target.tile() == (lightning_one.transform(1, 1))) {
                target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), CombatType.MAGIC).checkAccuracy().submit();
            }
        });
        mob.getCombat().delayAttack(6);
    }

    private void spawnHellhounds(Npc vetion, Mob target) {
        vetion.forceChat("Kill my pets!");
        List<Npc> minions = new ArrayList<>();
        for (int index = 0; index < 2; index++) {
            VetionMinion minion = new VetionMinion(vetion, target);
            minions.add(minion);
            World.getWorld().registerNpc(minion);
        }

        vetion.putAttrib(AttributeKey.VETION_HELLHOUND_SPAWNED, true);
        vetion.putAttrib(AttributeKey.MINION_LIST, minions);
    }

    public boolean customOnDeath(Mob mob) {
        if (mob.isNpc()) {
            Npc purpleVetion = (Npc) mob;
            if ((purpleVetion.hp() == 0 || purpleVetion.dead()) && !purpleVetion.<Boolean>getAttribOr(AttributeKey.VETION_REBORN_ACTIVE, false)) {
                purpleVetion.heal(255); // Heal vetion
                purpleVetion.transmog(6612); //Transform into orange vetion
                purpleVetion.setTile(purpleVetion.tile());//Update tile
                purpleVetion.forceChat("Do it again!!");
                purpleVetion.getTimerRepository().register(TimerKey.VETION_REBORN_TIMER, 500);
                purpleVetion.putAttrib(AttributeKey.VETION_REBORN_ACTIVE, true);
                return purpleVetion.transmog() != 6612;
            }
        }
        return false;
    }
}
