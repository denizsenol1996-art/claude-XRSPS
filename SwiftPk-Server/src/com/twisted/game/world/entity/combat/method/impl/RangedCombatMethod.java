package com.twisted.game.world.entity.combat.method.impl;

import com.twisted.fs.NpcDefinition;
import com.twisted.game.content.mechanics.MultiwayCombat;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.formula.FormulaUtils;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.ranged.*;
import com.twisted.game.world.entity.combat.ranged.RangedData.RangedWeapon;
import com.twisted.game.world.entity.combat.weapon.WeaponType;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.container.equipment.Equipment;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.region.Region;
import com.twisted.util.CustomItemIdentifiers;
import com.twisted.util.ItemIdentifiers;
import com.twisted.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.CRUOURS_VOW;
import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.FUMUS_VOW;
import static com.twisted.game.world.entity.combat.weapon.WeaponType.BOW;
import static com.twisted.game.world.entity.combat.weapon.WeaponType.THROWN;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.CRAWS_BOW;

/**
 * Represents the combat method for ranged attacks.
 *
 * @author Professor Oak
 */
public class RangedCombatMethod extends CommonCombatMethod {

    private int ballistaProjectileFor(int ammo) {
        return switch (ammo) {
            case BRONZE_JAVELIN -> 200;
            case IRON_JAVELIN -> 201;
            case STEEL_JAVELIN -> 202;
            case MITHRIL_JAVELIN -> 203;
            case ADAMANT_JAVELIN -> 204;
            case RUNE_JAVELIN -> 205;
            case DRAGON_JAVELIN -> 1301;
            case AMETHYST_JAVELIN -> 1386;
            default -> 1301;
        };
    }

    @Override
    public boolean prepareAttack(Mob attacker, Mob target) {
        //Get data

        //TODO sound here
        attacker.animate(attacker.attackAnimation());

        if (attacker.isNpc()) {
            new Projectile(attacker, target, attacker.getAsNpc().combatInfo().projectile, 41, 60, 40, 36, 15).sendProjectile();
            return true;
        }

        if (attacker.isPlayer()) {
            Player player = attacker.getAsPlayer();

            //Decrement ammo
            CombatFactory.decrementAmmo(player);

            WeaponType weaponType = player.getCombat().getWeaponType();
            int speed = attacker.projectileSpeed(target);
            int distance = attacker.tile().getChevDistance(target.tile());
            var weaponId = player.getEquipment().getId(EquipSlot.WEAPON);
            var ammoId = player.getEquipment().getId(EquipSlot.AMMO);
            var crystalBow = (weaponId >= 4212 && weaponId <= 4223);
            var crawsBow = weaponId == CRAWS_BOW || weaponId == CRAWS_BOW_C || weaponId == BEGINNER_CRAWS_BOW || weaponId == HWEEN_CRAWS_BOW || weaponId == WEBWEAVER_BOW;
            var bowOfFaerdhinen = FormulaUtils.hasBowOfFaerdhenin((Player) attacker);
            var blowpipe = weaponId == 12926 || weaponId == 12924;
            var chins = weaponType == WeaponType.CHINCHOMPA;
            var ballista = weaponId == 19478 || weaponId == 19481;

            var baseDelay = chins ? 20 : ballista ? 30 : 41;
            var startHeight = 40;
            var endHeight = 36;
            var curve = 15;
            var graphic = -1; // 228

            if (crystalBow) {
                attacker.graphic(250, 96, 0);
                graphic = 249;

                baseDelay = 41;
                speed = 51 + (distance * 5);
            }

            if (player.getEquipment().contains(VENATOR_BOW)) {
                graphic = 2289;
            }

            if (crawsBow) {
                graphic = 1574;
                if (weaponId == CRAWS_BOW_C) {
                    graphic = 61;
                }
            }

            if (chins) {
                graphic = weaponId == 10033 ? 908 : weaponId == 10034 ? 909 : 1272;
            }

            // Bows need special love.. projectile and graphic :D
            if (weaponType == BOW && !crystalBow && !crawsBow && !bowOfFaerdhinen) {
                var db = ArrowDrawBack.find(ammoId);

                // Find the gfx and do it : -)
                if (db != null) {
                    if (Equipment.darkbow(weaponId)) {
                        var db2 = DblArrowDrawBack.find(ammoId);
                        if (db2 != null) {
                            player.graphic(db2.gfx, 96, 0);
                            graphic = db.projectile;
                        }
                    } else {
                        if (db.gfx != -1) {
                            graphic = db.gfx;
                        }
                        player.graphic(graphic, 96, 0);
                        graphic = db.projectile;
                    }
                }
            }

            if (player.getEquipment().contains(CustomItemIdentifiers.BOW_OF_FAERDHINEN)) {
                player.graphic(1888, 100, 0);
                graphic = 1887;
            } else if (player.getEquipment().contains(25866)) {
                player.graphic(1923, 100, 0);
                graphic = 1922;
            } else if (player.getEquipment().contains(25884)) {
                player.graphic(1925, 100, 0);
                graphic =1924;
            } else if (player.getEquipment().contains(25886)) {
                player.graphic(1927, 100, 0);
                graphic =1926;
            } else if (player.getEquipment().contains(25888)) {
                player.graphic(1929, 100, 0);
                graphic =1928;
            } else if (player.getEquipment().contains(25890)) {
                player.graphic(1931, 100, 0);
                graphic =1930;
            } else if (player.getEquipment().contains(25892)) {
                player.graphic(1933, 100, 0);
                graphic =1932;
            } else if (player.getEquipment().contains(25896)) {
                player.graphic(25896, 100, 0);
                graphic =1934;
            }

            if (player.getEquipment().contains(WEBWEAVER_BOW)) {
                player.graphic(2283, 100, 0);
                graphic = 2282;
            }

            // Knives are not your de-facto stuff either
            if (weaponType == THROWN) {
                if(weaponId == TOXIC_BLOWPIPE || weaponId == MAGMA_BLOWPIPE || weaponId == HWEEN_BLOWPIPE) {
                    graphic = 1122;
                }
                // Is this a knife? There are more than only knives that people throw.. think.. asparagus. uwat? darts, thrownaxes, javelins
                var drawback = KnifeDrawback.find(weaponId);
                if (drawback != null) {
                    player.graphic(drawback.gfx, 96, 0);
                    graphic = drawback.projectile;
                } else {
                    var db2 = DartDrawback.find(weaponId);
                    if (db2 != null) {
                        player.graphic(db2.gfx, 96, 0);
                        graphic = db2.projectile;
                    } else {
                        var db3 = ThrownaxeDrawback.find(weaponId);
                        if (db3 != null) {
                            player.graphic(db3.gfx, 96, 0);
                            graphic = db3.projectile;
                        }
                    }
                }
            }

            if(ballista) {
                baseDelay = 31;
                startHeight = 40;
                endHeight = 30;
                curve = 5;
                graphic = ballistaProjectileFor(ammoId);
            }

            // Crossbows are the other type of special needs
            if (weaponType == WeaponType.CROSSBOW) {
                baseDelay = 41;
                startHeight = 40;
                endHeight = 36;
                curve = 5;
                graphic = 27;
            }

            if (graphic != -1) {
                Projectile projectile = new Projectile(attacker, target, graphic, attacker.projectileSpeed(target), baseDelay, startHeight, endHeight, 0, curve, 11);
                projectile.sendProjectile();
            }

            if (Equipment.darkbow(weaponId) || weaponId == SANGUINE_TWISTED_BOW) {
                Projectile projectile = new Projectile(attacker, target, graphic, 10 + attacker.projectileSpeed(target), 10 + baseDelay, startHeight, endHeight, 0, curve, 105);
                projectile.sendProjectile();
            }
        }

        if(mob.isPlayer()) {
            Player player = (Player) mob;
            boolean memoryOfSerenPet = player.hasPetOut("Memory of seren");
            if (memoryOfSerenPet) {
                if (Utils.percentageChance(25)) {
                    target.hit(mob, World.getWorld().random(1, 10), CombatType.RANGED).checkAccuracy().submit();
                }
            }
        }

        // Darkbow is double hits.
        if (attacker.getCombat().getRangedWeapon() != null) {
            var weaponId = -1;
            WeaponType weaponType = WeaponType.UNARMED;
            if(attacker.isPlayer()) {
                weaponId = attacker.getAsPlayer().getEquipment().getId(EquipSlot.WEAPON);
                weaponType = attacker.getAsPlayer().getCombat().getWeaponType();
            }

            // no it separate thing ah okay thanks for the help
            var tileDist = attacker.tile().getChevDistance(target.tile());
            var delay = calcHitDelay(weaponId, weaponType, tileDist) + 1; // need +1 because non-Jagex processing order.


            //System.out.println("delay: "+delay);

            // At range 3 the hits hit the same time. may indicate mathematical rounding in calc.
            var secondArrowDelay = delay + (tileDist == 3 ? 0 : tileDist == 7 || tileDist == 8 || tileDist == 10 ? 2 : 1);
            //System.out.println("secondArrowDelay: "+secondArrowDelay);

            // primary range hit
            Hit hit = new Hit(attacker, target, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED), delay, CombatType.RANGED);
            hit.checkAccuracy().submit();

            if (attacker instanceof Player player) {
                checkVenatorBow(target, player, hit);
            }

            // secondary hits
            if (attacker.getCombat().getRangedWeapon() == RangedWeapon.DARK_BOW) {
                target.hit(attacker, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED), secondArrowDelay, CombatType.RANGED).checkAccuracy().submit();
            } else if (weaponId == SANGUINE_TWISTED_BOW) {
                target.hit(attacker, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED) / 2, secondArrowDelay, CombatType.RANGED).checkAccuracy().submit();
            }


            if (hit.isAccurate() && hit.getDamage() > 0) {
                if (Prayers.usingPrayer(attacker, CRUOURS_VOW)) {
                    hit.postDamage(_ -> attacker.healHit(attacker, 2, 0));
                    return true;
                }

                if (Prayers.usingPrayer(attacker, FUMUS_VOW)) {
                    if (target instanceof Player t) {
                        if (!t.getEquipment().containsAny(SERPENTINE_HELM, MAGMA_HELM, GRANDMASTER_HELM, 30326)) {
                            hit.postDamage(_ -> new Hit(attacker, t, World.getWorld().random(1, 15), 0, SplatType.POISON_HITSPLAT, null).submit());
                        }
                    } else if (target instanceof Npc npc) {
                        if (!npc.isEventNpc()) {
                            hit.postDamage(_ -> new Hit(attacker, npc, World.getWorld().random(1, 15), 0, SplatType.POISON_HITSPLAT, null).submit());
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean ballista(int weaponId) {
        return weaponId == 19478 || weaponId == 19481;
    }

    // The hitmark delay without pid. If pid, it gets adjusted elsewhere.
    // https://i.gyazo.com/d84b1fb9969e3166ff5abf2978e77b4d.png
    private int calcHitDelay(int weaponId, WeaponType weaponType, int dist) {
        if (ballista(weaponId))
            return (dist <= 4) ? 2 : 3;
        if (weaponId == 12926 || weaponType == WeaponType.CHINCHOMPA)   // Blowpipe / chins longer range throwning weps
            return (dist <= 5) ? 2 : 3;
        if (weaponType == THROWN) {
            return 1 + dist / 6; // darts / knives with max dist 4
        } else {
            return 1 + (3 + dist) / 6;
            // shortbow (and darkbow), longbow, karils xbow, crystal bow, crossbows
        }
    }

    void checkVenatorBow(final Mob target, final Player player, final Hit hit) {
        if (player.getEquipment().contains(ItemIdentifiers.VENATOR_BOW) && MultiwayCombat.includes(target.tile())) {
            hit.postDamage(post -> sendBouncingArrows(post.getAttacker(), post.getTarget()));
        }
    }

    void sendBouncingArrows(Mob attacker, Mob originalTarget) {
        final Mob original = originalTarget;

        Area area = original.getCentrePosition().area(original.getSize()).enlarge(2);

        final Region region = original.tile().getRegion();

        final Result firstResult = getResult(attacker, original, region, area);

        if (firstResult.closestToPoint().isPresent()) {
            Mob secondTarget = firstResult.temp().stream().filter(entity -> !entity.equals(original)).findFirst().orElse(null);
            if (secondTarget != null) {
                Tile secondCenterPosition = secondTarget.getCentrePosition();
                var distance = secondCenterPosition.distanceTo(original.tile());
                int duration = (int) (15 + (distance));
                Projectile projectile = new Projectile(original, secondTarget, 2007, duration, 0, 31, 31, 1, 0, 0);
                projectile.sendProjectile();
                int delay = (int) (projectile.getSpeed() / 30D);
                Hit hit = new Hit(attacker, secondTarget, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy();
                hit.submit();
                hit.postDamage(h -> h.setDamage(h.getDamage() * 2 / 3));
                area = secondCenterPosition.area(secondTarget.getSize());
                Result secondResult = getResult(attacker, secondTarget, region, area);
                if (secondResult.closestToPoint().isPresent()) {
                    boolean returnToOriginal = World.getWorld().random(2) == 0;
                    Mob thirdTarget;
                    if (returnToOriginal) {
                        thirdTarget = original;
                    } else {
                        List<Mob> potentialThirdTargets = secondResult.temp().stream().filter(entity -> !entity.equals(original) && !entity.equals(secondTarget)).toList();
                        if (!potentialThirdTargets.isEmpty()) {
                            int randomIndex = World.getWorld().random(potentialThirdTargets.size() - 1);
                            thirdTarget = potentialThirdTargets.get(randomIndex);
                        } else {
                            thirdTarget = original;
                        }
                    }
                    if (thirdTarget != null) {
                        distance = secondCenterPosition.distanceTo(thirdTarget.tile());
                        duration = (int) (projectile.getSpeed() + 15 + (distance));
                        projectile = new Projectile(secondTarget, thirdTarget, 2007, duration, projectile.getSpeed(), 31, 31, 1, 0, 0);
                        projectile.sendProjectile();
                        delay = (int) (projectile.getSpeed() / 30D);
                        new Hit(attacker, thirdTarget, CombatFactory.calcDamageFromType(attacker, target, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
                    }
                }
            }
        }
    }

    @NotNull
    Result getResult(Mob attacker, Mob target, Region region, Area area) {
        Set<Mob> temp = new HashSet<>();

        var cached = NpcDefinition.cached;
        for (Npc npc : attacker.getLocalNpcs()) {

            if ((npc == null || npc.hidden() || !npc.tile().isViewableFrom(attacker.tile()))) {
                continue;
            }

            var def = cached.get(npc.getId());
            if (def.ispet || !def.rightclick || def.options[1] == null) {
                continue;
            }

            if (area.contains(npc.tile()) && !npc.equals(target)) {
                temp.add(npc);
                break;
            }
        }

        OptionalInt closestToPoint = temp.stream()
            .filter(Objects::nonNull)
            .mapToInt(npc -> npc.getCentrePosition().getNorthEastTile().distance(target.tile()))
            .sorted()
            .findFirst();
        return new Result(temp, closestToPoint);
    }

    private record Result(Set<Mob> temp, OptionalInt closestToPoint) {
    }


    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        RangedWeapon weapon = mob.getCombat().getRangedWeapon();
        if (weapon != null) {

            // Long range fight type has longer attack distance than other types
            if (mob.getCombat().getFightType() == weapon.getType().getLongRangeFightType()) {
                return weapon.getType().getLongRangeDistance();
            }

            return weapon.getType().getDefaultDistance();
        }
        return 6;
    }

    public void handleAfterHit(Hit hit) {
        if (hit.getAttacker() == null) {
            return;
        }

        final RangedWeapon rangedWeapon = hit.getAttacker().getCombat().getRangedWeapon();
        if (rangedWeapon == null) {
            return;
        }

        boolean chins = rangedWeapon == RangedWeapon.CHINCHOMPA;

        if (chins) {
            hit.getTarget().performGraphic(new Graphic(157, 100, 0));
        }
    }
}
