package com.twisted.game.world.entity.combat.method.impl;

import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.weapon.WeaponType;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.route.Direction;
import com.twisted.util.CustomNpcIdentifiers;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

import java.util.concurrent.atomic.AtomicInteger;

import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.CRUOURS_VOW;
import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.FUMUS_VOW;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR;
import static com.twisted.util.CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR;
import static com.twisted.util.CustomNpcIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * Represents the combat method for melee attacks.
 *
 * @author Professor Oak
 */
public class MeleeCombatMethod extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (mob == null || target == null)
            return false;

        mob.animate(mob.attackAnimation());

        if (mob.isPlayer()) {
            Player player = (Player) mob;
            boolean hasFenrirPet = player.hasPetOut("Fenrir greyback Jr pet");
            if (hasFenrirPet) {
                if (Utils.percentageChance(15)) {
                    target.hit(mob, World.getWorld().random(1, 15), CombatType.MELEE).checkAccuracy().submit();
                }
            }
        }

        if (target.isNpc() && mob.isPlayer()) {
            Player player = (Player) mob;
            final Tile el = mob.getCentrePosition();
            final Tile vl = target.getCentrePosition();
            Direction dir = Direction.getDirection(el, vl);
            int gfx;
            switch (dir) {
                case SOUTH, SOUTH_EAST -> {
                    dir = Direction.SOUTH;
                    gfx = 478;
                }
                case NORTH, NORTH_WEST -> {
                    gfx = 506;
                    dir = Direction.NORTH;
                }
                case EAST, NORTH_EAST -> {
                    gfx = 1172;
                    dir = Direction.EAST;
                }
                default -> {
                    gfx = 1231;
                    dir = Direction.WEST;
                }
            }

            if (player.getEquipment().containsAny(ItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR, ItemIdentifiers.HOLY_SCYTHE_OF_VITUR, SCYTHE_OF_VITUR, ETHEREAL_SCYTHE)) {
                Tile gfxTile = mob.getCentrePosition().transform(dir.deltaX, dir.deltaY);
                World.getWorld().tileGraphic(gfx, gfxTile, 96, 20);

                int hitCount = player.getEquipment().containsAny(SANGUINE_SCYTHE_OF_VITUR, HOLY_SCYTHE_OF_VITUR, ETHEREAL_SCYTHE) && Utils.rollDie(15, 1) ? 4 : 3;
                Hit[] hits = new Hit[hitCount];

                for (int i = 0; i < hits.length; i++) {
                    hits[i] = new Hit(mob, target, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy();
                }

                for (int i = 1; i < hits.length; i++) {
                    double damageFactor = switch (i) {
                        case 1 -> 2.0D;
                        case 2 -> 4.25D;
                        case 3 -> 4.50D;
                        default -> 1.0D;
                    };
                    if (hits[i].isAccurate()) {
                        if (hits[i].getDamage() > 0) {
                            int d1 = hits[i].getDamage();
                            d1 /= damageFactor;
                            hits[i].setDamage(d1);
                        }
                    }
                }

                for (Hit hit : hits) {
                    hit.submit();

                    checkGrandmastersCurse(target, hit, player);

                    if (Prayers.usingPrayer(player, CRUOURS_VOW)) {
                        hit.postDamage(_ -> player.healHit(player, 3, 0));
                    } else if (Prayers.usingPrayer(player, FUMUS_VOW)) {
                        if (target instanceof Player t && !t.getEquipment().containsAny(SERPENTINE_HELM, MAGMA_HELM, GRANDMASTER_HELM, 30326)) {
                            hit.postDamage(_ -> new Hit(player, t, World.getWorld().random(1, 5), 0, SplatType.POISON_HITSPLAT, null).submit());
                        } else if (target instanceof Npc npc) {
                            if (npc.id() != CustomNpcIdentifiers.SUMMER_IMP) {
                                hit.postDamage(_ -> new Hit(player, npc, World.getWorld().random(1, 15), 0, SplatType.POISON_HITSPLAT, null).submit());
                            }
                        }
                    }
                }
                return true;
            }
        }

        final Hit hit = new Hit(mob, target, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy();
        hit.submit();

        if (mob instanceof Player player) {
            checkGrandmastersCurse(target, hit, player);

            if (hit.isAccurate() && hit.getDamage() > 0) {
                if (Prayers.usingPrayer(player, CRUOURS_VOW)) {
                    hit.postDamage(_ -> player.healHit(player, 3, 0));
                    return true;
                }

                if (Prayers.usingPrayer(player, FUMUS_VOW)) {
                    if (target instanceof Player t) {
                        if (!t.getEquipment().containsAny(SERPENTINE_HELM, MAGMA_HELM, GRANDMASTER_HELM, 30326)) {
                            hit.postDamage(_ -> new Hit(player, t, World.getWorld().random(1, 15), 0, SplatType.POISON_HITSPLAT, null).submit());
                        }
                    } else if (target instanceof Npc npc) {
                        if (!npc.isEventNpc()) {
                            hit.postDamage(_ -> new Hit(player, npc, World.getWorld().random(1, 15), 0, SplatType.POISON_HITSPLAT, null).submit());
                        }
                    }
                }
            }
        }
        return true;
    }

    private static void checkGrandmastersCurse(Mob target, Hit hit, Player player) {
        int cachedDamage = hit.getDamage();
        if ((player.getEquipment().contains(GRANDMASTER_MACE) || player.hasPetOut(CURSED_ELEMENT) || player.hasPetOut(THE_ONE_ABOVE_ALL))) {
            if (Utils.rollDie(15, 1) && cachedDamage > 0) {
                World.getWorld().tileGraphic(1416, target.tile(), 0, 0);
                AtomicInteger count = new AtomicInteger(5);
                if (!target.hasAttrib(AttributeKey.GRANDMASTER_CURSE)) {
                    target.putAttrib(AttributeKey.GRANDMASTER_CURSE, true);
                    Chain.noCtxRepeat().repeatingTask(2, task -> {
                        if (count.get() == 0) {
                            target.clearAttrib(AttributeKey.GRANDMASTER_CURSE);
                            task.stop();
                            return;
                        }
                        new Hit(player, target, cachedDamage, 0, CombatType.MELEE).submit();
                        count.getAndDecrement();
                    });
                }
            }
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        if (mob.getCombat().getWeaponType() == WeaponType.HALBERD) {
            return 2;
        }
        return 1;
    }

}
