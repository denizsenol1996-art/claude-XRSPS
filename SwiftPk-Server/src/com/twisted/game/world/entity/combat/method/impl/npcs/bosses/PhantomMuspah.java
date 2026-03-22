package com.twisted.game.world.entity.combat.method.impl.npcs.bosses;

import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;

import java.util.*;

public class PhantomMuspah extends CommonCombatMethod {

    boolean generated = false;
    public int spikeProgressionCount = 4;
    public Set<GameObject> spikes = new HashSet<>();
    AttackType currentType = AttackType.MELEE;
    int adjustCount = 0;

    @Override
    public boolean prepareAttack(Mob entity, Mob target) {
        if (adjustCount >= 3) {
            currentType = AttackType.VALUES[(currentType.ordinal() + 1) % AttackType.VALUES.length];
            adjustCount = 0;
        }
        if (AttackType.MELEE.equals(currentType)) {
            if (!withinDistance(1) && adjustCount < 3) {
                this.follow(1);
                adjustCount++;
                return true;
            }
            melee();
            adjustCount++;
        } else if (AttackType.MAGIC.equals(currentType)) {
            magic();
            adjustCount++;
        } else {
            magic();
            adjustCount++;
        }
        return true;
    }

    private void melee() {
        final Mob target = Utils.randomElement(this.getPossibleTargets(this.mob));
        this.mob.animate(9920);
        new Hit(this.mob, target, CombatFactory.calcDamageFromType(this.mob, target, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy().submit();
    }

    private void magic() {
        this.mob.animate(9918);
        for (final Mob target : this.getPossibleTargets(this.mob)) {
            Tile centerPosition = this.mob.getCentrePosition();
            var distance = centerPosition.distanceTo(target.tile());
            int duration = (int) (80 + (15 + (distance * 5)));
            Projectile projectile = new Projectile(this.mob, target, 2007, duration, 80, 31, 31, 1, 0, 0);
            projectile.sendProjectile();
            new Hit(this.mob, target, CombatFactory.calcDamageFromType(this.mob, target, CombatType.MAGIC), 4, CombatType.MAGIC).checkAccuracy().submit();
        }
    }

    @Override
    public int getAttackSpeed(Mob entity) {
        return 5;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return this.mob instanceof Npc npc ? npc.id() == 12078 ? 1 : 6 : 0;
    }

    @Override
    public boolean canMultiAttackInSingleZones() {
        return true;
    }

    @Override
    public void onDeath(Player killer, Npc npc) {
        for (final GameObject object : this.spikes) {
            object.remove();
        }
    }
}
