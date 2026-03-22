package com.twisted.game.world.entity.combat.hit;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.formula.accuracy.AbstractAccuracy;
import com.twisted.game.world.entity.combat.formula.accuracy.data.MagicAccuracy;
import com.twisted.game.world.entity.combat.formula.accuracy.data.MeleeAccuracy;
import com.twisted.game.world.entity.combat.formula.accuracy.data.RangeAccuracy;
import com.twisted.game.world.entity.combat.magic.CombatSpell;
import com.twisted.game.world.entity.combat.method.CombatMethod;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.Flag;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.PlayerStatus;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.CustomNpcIdentifiers;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

/**
 * Represents a pending hit.
 *
 * @author Professor Oak
 */
public class Hit {
    public boolean toremove;
    public boolean showSplat;
    public boolean reflected;
    public boolean forceShowSplashWhenMissMagic;
    public boolean pidded;
    @Getter
    private final Mob attacker;
    @Getter
    private final Mob target;
    public CombatSpell spell;
    @Getter
    @Setter
    public static boolean debugAccuracy = false;
    @Getter
    public AbstractAccuracy abstractAccuracy;
    public SplatType splatType;
    @Getter
    @Setter
    private int damage;
    @Getter
    private int delay;
    private boolean checkAccuracy;
    @Getter
    private boolean accurate;
    @Getter
    private CombatType combatType;

    public Hit setSplatType(SplatType splatType) {
        this.splatType = splatType;
        return this;
    }

    private void adjustDelay() {

        if (target != null && target.isNpc()) {
            return;
        }
        if (attacker != null) {
            if (attacker.isNpc() || attacker.pidOrderIndex == -1) {
                return;
            }

            if (attacker.pidOrderIndex <= target.pidOrderIndex) {
                delay -= 1;
            }
        }

        if (delay < 1 && combatType != CombatType.MELEE) {
            delay = 1;
        }
    }

    public Hit(Mob attacker, Mob target, CombatMethod method, boolean checkAccuracy, int delay, int damage) {
        this.attacker = attacker;
        this.target = target;
        if (method instanceof CommonCombatMethod commonCombatMethod) { // should be the base class of all scripts now
            combatType = commonCombatMethod.styleOf();
        }
        this.checkAccuracy = checkAccuracy;
        this.damage = damage;
        applyAccuracyToMiss();
        this.delay = delay;
        this.splatType = damage < 1 ? SplatType.BLOCK_HITSPLAT : SplatType.HITSPLAT;
    }

    public Hit(Mob attacker, Mob target, int damage, int delay, SplatType type, CombatType combatType) {
        this.attacker = attacker;
        this.target = target;
        this.damage = damage;
        this.delay = delay;
        this.splatType = type;
        this.combatType = combatType;
    }

    public Hit(Mob attacker, Mob target, int damage, int delay, CombatType combatType) {
        this.attacker = attacker;
        this.target = target;
        this.damage = damage;
        this.delay = delay;
        this.combatType = combatType;
        this.splatType = damage < 1 ? SplatType.BLOCK_HITSPLAT : SplatType.HITSPLAT;
    }

    public static Hit builder(Mob attacker, Mob target, int damage) {
        return builder(attacker, target, damage, 1);
    }

    public static Hit builder(Mob attacker, Mob target, int damage, int delay) {
        return builder(attacker, target, damage, delay, CombatType.MELEE);
    }

    public static Hit builder(Mob attacker, Mob target, int damage, int delay, CombatType type) {
        Hit hit = new Hit(attacker, target, null, false, delay, damage);
        hit.combatType = type;
        return hit;
    }

    public Hit delay(int d) {
        this.delay = Math.max(0, d);
        return this;
    }

    public int decrementAndGetDelay() {
        return --delay;
    }

    public Hit setAccurate(boolean accurate) {
        this.accurate = accurate;
        return this;
    }

    public Hit setCombatType(CombatType type) {
        this.combatType = type;
        return this;
    }

    private void applyAccuracyToMiss() {
        if (attacker == null || target == null) {
            return;
        }

        if (target.dead()) {
            return;
        }

        if (attacker.isPlayer() && target.isPlayer()) {
            if (target.getAsPlayer().getStatus() == PlayerStatus.TRADING) {
                target.getAsPlayer().getTrading().abortTrading();
            }
        }

        if (target.isNpc() && target.getAsNpc().isCombatDummy()) {
            checkAccuracy = false;
        }

        var success = false;

        double chance = World.getWorld().random().nextDouble();
        if (combatType != null) {
            switch (combatType) {
                case MELEE -> abstractAccuracy = new MeleeAccuracy(this.attacker, this.target, this.combatType);
                case RANGED -> abstractAccuracy = new RangeAccuracy(this.attacker, this.target, this.combatType);
                case MAGIC -> abstractAccuracy = new MagicAccuracy(this.attacker, this.target, this.combatType);
            }
        }

        if (abstractAccuracy != null) {
            success = abstractAccuracy.success(chance);
        }

        accurate = !checkAccuracy || success;

        int damage;

        final int alwaysHitDamage = attacker.getAttribOr(AttributeKey.ALWAYS_HIT, 0);
        final boolean alwaysHitActive = alwaysHitDamage > 0;
        final boolean oneHitActive = attacker.getAttribOr(AttributeKey.ONE_HIT_MOB, false);

        if (alwaysHitActive || oneHitActive)
            accurate = true;

        if (!accurate) {
            damage = 0;
        } else {
            if (oneHitActive) {
                damage = target.hp();
            } else if (alwaysHitActive) {
                damage = alwaysHitDamage;
            } else {
                damage = this.damage;
            }
        }

        if (this.attacker instanceof Player player && this.target instanceof Npc npc) {
            if (npc.id() == CustomNpcIdentifiers.SUMMER_IMP && player.getEquipment().contains(CustomItemIdentifiers.SUMMER_SOAKER) && !this.reflected) {
                accurate = true;
                damage = World.getWorld().random(5, 15);
            }
        }

        //Update total damage
        this.damage = damage;
    }

    public void submit() {
        if (target != null && !invalid()) {
            adjustDelay();
            CombatFactory.addPendingHit(this);
        }
    }

    public boolean invalid() {
        return target.locked() && !target.isDamageOkLocked() && !target.isDelayDamageLocked();
    }

    @Override
    public String toString() {
        return "PendingHit{" +
            "attacker=" + attacker +
            ", target=" + target +
            ", dmg=" + damage +
            ", delay=" + delay +
            ", checkAccuracy=" + checkAccuracy +
            ", accurate=" + accurate +
            ", combatType=" + combatType +
            '}';
    }

    public Hit setIsReflected() {
        reflected = true;
        return this;
    }

    public Hit checkAccuracy() {
        checkAccuracy = true;
        applyAccuracyToMiss();
        return this;
    }

    public Hit spell(CombatSpell spell) {
        this.spell = spell;
        return this;
    }

    /**
     * called after a hit has been executed and appears visually. will be finalized and damage cannot change.
     */
    public Consumer<Hit> postDamage;

    public Hit postDamage(Consumer<Hit> postDamage) {
        this.postDamage = postDamage;
        return this;
    }

    public void playerSync() {
        if (target == null) return;
        if (target.splats.size() >= 4)
            return;
        target.splats.add(new Splat(getDamage(), splatType));
        target.getUpdateFlag().flag(Flag.FIRST_SPLAT);
    }

    public Hit graphic(Graphic graphic) {
        this.target.graphic(graphic.id(), graphic.height(), graphic.delay());
        return this;
    }
}
