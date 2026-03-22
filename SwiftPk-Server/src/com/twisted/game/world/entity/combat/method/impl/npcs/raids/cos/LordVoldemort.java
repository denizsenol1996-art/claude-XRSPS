package com.twisted.game.world.entity.combat.method.impl.npcs.raids.cos;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.magic.Autocasting;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.weapon.WeaponInterfaces;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.position.Area;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import static com.twisted.game.content.items.combine.ElderWand.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 25, 2021
 */
public class LordVoldemort extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        var random = Utils.random(7);
        switch (random) {
            case 0, 1 -> crucio(((Npc) mob), target);
            case 2, 3 -> petrificusTotalus(((Npc) mob), target);
            case 4 -> sectumsempra(((Npc) mob), target);
            case 5 -> expelliarmus(((Npc) mob), target);
            default -> avadaKedavra(((Npc) mob), target);
        }
        return true;
    }

    private void expelliarmus(Npc npc, Mob target) {
        if (target.isPlayer()) {
            Player player = target.getAsPlayer();
            //Expelliarmus has a max hit of 35
            npc.combatInfo().maxhit = 35;
            npc.animate(401);
            npc.forceChat("Expelliarmus!");

            npc.face(null); // Stop facing the target
            //Target all raids party members
            if (player.raidsParty != null) {
                for (Player p : player.raidsParty.getMembers()) {
                    if (p != null && p.getRaids() != null && p.getRaids().raiding(player) && p.tile().inArea(new Area(3143, 4652, 3160, 4665, p.raidsParty.getHeight()))) {
                        new Projectile(npc, p, EXPELLIARMUS_PROJECTILE, 32, mob.projectileSpeed(target), 30, 30, 0).sendProjectile();
                        var delay = mob.getProjectileHitDelay(target);
                        Chain.bound(null).runFn(delay, () -> disarm(p));
                        p.hit(npc, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                    }
                }
            }
            mob.face(target.tile()); // Go back to facing the target.
        }
    }

    private void disarm(Player player) {
        final Item item = player.getEquipment().get(EquipSlot.WEAPON);
        if (item != null && player.inventory().hasCapacityFor(item)) {
            player.getEquipment().remove(item, EquipSlot.WEAPON, true);
            player.getEquipment().unequip(EquipSlot.WEAPON);
            WeaponInterfaces.updateWeaponInterface(player);
            CombatSpecial.updateBar(player);
            player.setSpecialActivated(false);
            Autocasting.setAutocast(player, null);
            player.looks().resetRender();
            player.inventory().add(item);
        }
    }

    private void petrificusTotalus(Npc npc, Mob target) {
        if (target.isPlayer()) {
            Player player = target.getAsPlayer();
            //petrificus totalus has a max hit of 32
            npc.combatInfo().maxhit = 32;
            npc.animate(401);
            npc.forceChat("Petrificus Totalus!");

            npc.face(null); // Stop facing the target
            //Target all raids party members
            if (player.raidsParty != null) {
                for (Player p : player.raidsParty.getMembers()) {
                    if (p != null && p.getRaids() != null && p.getRaids().raiding(player) && p.tile().inArea(new Area(3143, 4652, 3160, 4665, p.raidsParty.getHeight()))) {
                        new Projectile(npc, p, PETRIFICUS_TOTALUS_PROJECTILE, 32, mob.projectileSpeed(target), 30, 30, 0).sendProjectile();
                        var delay = mob.getProjectileHitDelay(target);
                        p.hit(npc, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                    }
                }
            }
            mob.face(target.tile()); // Go back to facing the target.
        }
    }

    private void sectumsempra(Npc npc, Mob target) {
        if (target.isPlayer()) {
            Player player = target.getAsPlayer();
            //sectumsempra totalus has a max hit of 42
            npc.combatInfo().maxhit = 42;
            npc.animate(401);
            npc.forceChat("Sectumsempra!");

            npc.face(null); // Stop facing the target
            //Target all raids party members
            if (player.raidsParty != null) {
                for (Player p : player.raidsParty.getMembers()) {
                    if (p != null && p.getRaids() != null && p.getRaids().raiding(player) && p.tile().inArea(new Area(3143, 4652, 3160, 4665, p.raidsParty.getHeight()))) {
                        new Projectile(npc, p, SECTUMSEMPRA_PROJECTILE, 32, mob.projectileSpeed(target), 30, 30, 0).sendProjectile();
                        var delay = mob.getProjectileHitDelay(target);
                        p.hit(npc, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                    }
                }
            }
            mob.face(target.tile()); // Go back to facing the target.
        }
    }

    private void crucio(Npc npc, Mob target) {
        if (target.isPlayer()) {
            Player player = target.getAsPlayer();
            //sectumsempra totalus has a max hit of 36
            npc.combatInfo().maxhit = 36;
            npc.animate(401);
            npc.forceChat("Crucio!");

            npc.face(null); // Stop facing the target
            //Target all raids party members
            if (player.raidsParty != null) {
                for (Player p : player.raidsParty.getMembers()) {
                    if (p != null && p.getRaids() != null && p.getRaids().raiding(player) && p.tile().inArea(new Area(3143, 4652, 3160, 4665, p.raidsParty.getHeight()))) {
                        new Projectile(npc, p, CRUCIATUS_CURSE_PROJECTILE, 32, mob.projectileSpeed(target), 30, 30, 0).sendProjectile();
                        var delay = mob.getProjectileHitDelay(target);
                        p.hit(npc, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                    }
                }
            }
            mob.face(target.tile()); // Go back to facing the target.
        }
    }

    private void avadaKedavra(Npc npc, Mob target) {
        if (target.isPlayer()) {
            Player player = target.getAsPlayer();
            npc.animate(805);
            npc.forceChat("Avada Kedavra!");

            npc.face(null); // Stop facing the target
            //Target all raids party members
            if (player.raidsParty != null) {
                for (Player p : player.raidsParty.getMembers()) {
                    if (p != null && p.getRaids() != null && p.getRaids().raiding(player) && p.tile().inArea(new Area(3143, 4652, 3160, 4665, p.raidsParty.getHeight()))) {
                        new Projectile(npc, p, AVADA_KEDAVRA_PROJECTILE, 32, mob.projectileSpeed(target), 30, 30, 0).sendProjectile();
                        var delay = mob.getProjectileHitDelay(target);
                        p.hit(npc, Prayers.usingPrayer(p, Prayers.PROTECT_FROM_MAGIC) ? World.getWorld().random(1, 40) : World.getWorld().random(1, 80), delay);
                    }
                }
            }
            mob.face(target.tile()); // Go back to facing the target.
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 12;
    }
}
