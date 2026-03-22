package com.twisted.game.world.entity.combat.method.impl.specials.range;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.container.equipment.EquipmentInfo;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.util.CustomItemIdentifiers.FIRE_ARROWS;
import static com.twisted.util.ItemIdentifiers.ICE_ARROWS;

public class ElementalBow extends CommonCombatMethod {

    private static final int FIRE_ARROW_PROJECTILE = 11;
    private static final int ICE_ARROW_PROJECTILE = 16;

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {

        if (mob.isPlayer()) {
            Player player = (Player) mob;
          //  player.getPacketSender().sendMessage("test");
            player.animate(EquipmentInfo.attackAnimationFor(player, CustomItemIdentifiers.ELEMENTAL_BOW));

            Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.RANGED),2, CombatType.RANGED).checkAccuracy();
            hit.submit();

            player.putAttrib(AttributeKey.ELEMENTAL_BOW_SPECIAL_COOLDOWN, true);

            Chain.bound(null).name("ElementalBowSpecTask").runFn(17, () -> player.putAttrib(AttributeKey.ELEMENTAL_BOW_SPECIAL_COOLDOWN, false));

            //Ice effect
            if (player.getEquipment().hasAt(EquipSlot.AMMO, ICE_ARROWS)) {
                new Projectile(player, target, ICE_ARROW_PROJECTILE, 60, 41, 40, 31, 0, 10, 15).sendProjectile();
                player.freeze(8, target);

                //A task that loops 5 times
                for (int index = 0; index < 6; index++) {
                    Chain.bound(null).name("ele_bow_freeze_effect").cancelWhen(() -> {
                        return !mob.tile().isWithinDistance(target.tile()) || target.dead(); // cancels as expected
                    }).runFn(index * 4, () -> {
                        target.graphic(540);
                        Hit freezeHit = target.hit(mob, World.getWorld().random(8, 15),2, CombatType.RANGED).setAccurate(true);
                        freezeHit.submit();
                    });
                }
                //Fire effect
            } else if (player.getEquipment().hasAt(EquipSlot.AMMO, FIRE_ARROWS)) {
                new Projectile(player, target, FIRE_ARROW_PROJECTILE, 60, 41, 40, 31, 0, 10, 15).sendProjectile();

                //A task that loops 5 times
                for (int index = 0; index < 6; index++) {
                    // cancels as expected
                    Chain.bound(null).name("ele_bow_fire_effect").cancelWhen(() -> {
                        return !mob.tile().isWithinDistance(target.tile()) || target.dead(); // cancels as expected
                    }).runFn(index * 4, () -> {
                        target.graphic(78);
                        Hit fireHit = target.hit(mob, World.getWorld().random(1, 5),2, CombatType.RANGED).setAccurate(true);
                        fireHit.submit();
                    });
                }
            }
        }
        CombatSpecial.drain(mob, CombatSpecial.ELEMENTAL_BOW.getDrainAmount());
        return true;
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return CombatFactory.RANGED_COMBAT.getAttackDistance(mob);
    }
}
