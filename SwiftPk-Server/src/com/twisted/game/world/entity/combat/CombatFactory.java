package com.twisted.game.world.entity.combat;

import com.twisted.GameServer;
import com.twisted.game.GameEngine;
import com.twisted.game.content.EffectTimer;
import com.twisted.game.content.duel.Dueling;
import com.twisted.game.content.mechanics.MultiwayCombat;
import com.twisted.game.content.members.MemberZone;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.content.skill.impl.slayer.slayer_task.SlayerCreature;
import com.twisted.game.content.teleport.Teleports;
import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.formula.FormulaUtils;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.hit.Splat;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.magic.Autocasting;
import com.twisted.game.world.entity.combat.magic.CombatSpell;
import com.twisted.game.world.entity.combat.magic.CombatSpells;
import com.twisted.game.world.entity.combat.method.CombatMethod;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.MagicCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.MeleeCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.RangedCombatMethod;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.abyssalsire.AbyssalSireState;
import com.twisted.game.world.entity.combat.method.impl.npcs.bosses.vorkath.Vorkath;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.armadyl.KreeArra;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.bandos.Graardor;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.saradomin.Zilyana;
import com.twisted.game.world.entity.combat.method.impl.npcs.godwars.zamorak.Kril;
import com.twisted.game.world.entity.combat.method.impl.npcs.hydra.AlchemicalHydra;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.CaveKraken;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.DesertLizards;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.Gargoyle;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.kraken.EnormousTentacle;
import com.twisted.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.ranged.BowReqs;
import com.twisted.game.world.entity.combat.ranged.CbowReqs;
import com.twisted.game.world.entity.combat.ranged.RangedData;
import com.twisted.game.world.entity.combat.ranged.RangedData.RangedWeapon;
import com.twisted.game.world.entity.combat.ranged.RangedData.RangedWeaponType;
import com.twisted.game.world.entity.combat.weapon.AttackType;
import com.twisted.game.world.entity.combat.weapon.FightStyle;
import com.twisted.game.world.entity.combat.weapon.WeaponInterfaces;
import com.twisted.game.world.entity.combat.weapon.WeaponType;
import com.twisted.game.world.entity.masks.animations.Animation;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.Direction;
import com.twisted.game.world.entity.mob.Flag;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skill;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.game.world.items.Item;
import com.twisted.game.world.items.container.equipment.Equipment;
import com.twisted.game.world.items.ground.GroundItem;
import com.twisted.game.world.items.ground.GroundItemHandler;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.position.areas.ControllerManager;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.game.world.route.routes.DumbRoute;
import com.twisted.net.SessionState;
import com.twisted.util.*;
import com.twisted.util.chainedwork.Chain;
import com.twisted.util.timers.TimerKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;

import static com.twisted.game.world.InterfaceConstants.BARROWS_REWARD_WIDGET;
import static com.twisted.game.world.entity.AttributeKey.STARTER_WEAPON_DAMAGE;
import static com.twisted.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss.KRAKEN_WHIRLPOOL;
import static com.twisted.game.world.entity.combat.method.impl.npcs.slayer.kraken.KrakenBoss.TENTACLE_WHIRLPOOL;
import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.*;
import static com.twisted.util.CustomItemIdentifiers.*;
import static com.twisted.util.CustomNpcIdentifiers.LORD_VOLDEMORT;
import static com.twisted.util.ItemIdentifiers.*;
import static com.twisted.util.ItemIdentifiers.HOLY_SANGUINESTI_STAFF;
import static com.twisted.util.NpcIdentifiers.*;

/**
 * Acts as a utility class for combat.
 *
 * @author Professor Oak
 */
public class CombatFactory {

    private static final Logger logger = LogManager.getLogger(CombatFactory.class);

    /**
     * The default melee combat method.
     */
    public static final CombatMethod MELEE_COMBAT = new MeleeCombatMethod();

    /**
     * The default ranged combat method
     */
    public static final CombatMethod RANGED_COMBAT = new RangedCombatMethod();

    /**
     * The default magic combat method
     */
    public static final CombatMethod MAGIC_COMBAT = new MagicCombatMethod();

    public static Set<BannedItem> bannedPvpItems = Set.of(
        new BannedItem(GRANDMASTER_MACE, EquipSlot.WEAPON),
        new BannedItem(GRANDMASTER_SCEPTRE, EquipSlot.WEAPON),
        new BannedItem(GRANDMASTER_BOW, EquipSlot.WEAPON),
        new BannedItem(ETHEREAL_SCYTHE, EquipSlot.WEAPON),
        new BannedItem(TUMEKENS_SHADOW, EquipSlot.WEAPON),
        new BannedItem(ELDER_WAND, EquipSlot.WEAPON),
        new BannedItem(GRANDMASTER_HELM, EquipSlot.HEAD),
        new BannedItem(ENCHANTED_TOTEMIC_BODY, EquipSlot.BODY),
        new BannedItem(ENCHANTED_TOTEMIC_LEGS, EquipSlot.LEGS),
        new BannedItem(GRANDMASTER_BOOTS, EquipSlot.FEET),
        new BannedItem(ENCHANTED_BOOTS, EquipSlot.FEET),
        new BannedItem(GRANDMASTER_GLOVES, EquipSlot.HANDS),
        new BannedItem(ENCHANTED_GLOVES, EquipSlot.HANDS));

    /**
     * Upon using a special attack, block teleporting for up to 6 seconds after that battle started.
     */
    public static void check_spec_and_tele(Player player, Mob target) {
        if (target.isPlayer()) {
            if (WildernessArea.wildernessLevel(target.tile()) >= 1) {
                Map<Integer, Integer> historyMap = player.getAttribOr(AttributeKey.PVP_WILDY_AGGRESSION_TRACKER, new HashMap<Integer, Integer>());
                var agroTick = historyMap.getOrDefault(target.getIndex(), World.getWorld().cycleCount());
                var dif = World.getWorld().cycleCount() - agroTick;
                if (dif < 10) {
                    player.getTimerRepository().extendOrRegister(TimerKey.BLOCK_SPEC_AND_TELE, 10 - dif);
                }
            }
        }
    }

    private static final Area[] GWD_ROOMS = {KreeArra.getENCAMPMENT(), Zilyana.getENCAMPMENT(), Kril.getENCAMPMENT(), Graardor.getBandosArea()};

    public static boolean bothInFixedRoom(Mob mob, Mob other) {
        for (Area area : GWD_ROOMS) {
            if (area.contains(mob) && !area.contains(other) || !area.contains(mob) && area.contains(other)) {
                return false;
            }
        }
        return true;
    }

    public static boolean takeSpecialEnergy(Player player, int specialAmount) {
        if (player.getSpecialAttackPercentage() < specialAmount) {
            player.message("You don't have enough power left.");
            player.setSpecialActivated(false);
            CombatSpecial.updateBar(player);
            return false;
        }
        CombatSpecial.drain(player, specialAmount);
        return true;
    }

    /**
     * Gets a mob's combat method.
     *
     * @param attacker The mob to get the combat method for.
     * @return
     */
    public static CombatMethod getMethod(Mob attacker) {
        if (attacker.isPlayer()) {
            Player p = attacker.getAsPlayer();

            // Update player data..

            // Update ranged ammo / weapon
            if (p.getEquipment().getWeapon() != null)
                p.getCombat().setRangedWeapon(RangedWeapon.getFor(p));

            /**
             * KINDA COMPLEX ORDERING:
             * the first one to trigger is the one that is executes, the others are ignored
             *   1. TRIGGER EXISTING SPELL (MANUAL SPELL ON PLAYER)
             *   2. TRIGGER NMS SPEC
             *   3. TRIGGER AUTOCAST
             *   4. TRIGGER NMS NORMAL SPELL
             *   5. TRIGGER ALL SPECIAL ATTACKS
             *   6. TRIGGER RANGE-WEP BASED ATTACK
             *   7. DEFAULT TO MELEE
             */

            int wep = p.getEquipment().getId(3);
            boolean specialWeapons = wep == ELDRITCH_NIGHTMARE_STAFF || wep == DAWNBRINGER || wep == VOLATILE_NIGHTMARE_STAFF || wep == DRAGON_THROWNAXE || wep == DRAGON_THROWNAXE_21207;

            // check spec activated on NMS staff.
            if (specialWeapons && p.isSpecialActivated()) {
                return p.getCombatSpecial().getCombatMethod();
            }

            // Order here is important: Spell on player takes priority over
            // having the special attack button just "on"
            // Check if player is maging..
            if (p.getCombat().getCastSpell() != null || p.getCombat().getAutoCastSpell() != null) {
                return MAGIC_COMBAT;
            }

            // Check special attacks..
            if (p.getCombatSpecial() != null) {
                boolean isGmaul = Combat.gmauls.stream().anyMatch(granite_maul -> p.getEquipment().hasAt(EquipSlot.WEAPON, granite_maul));

                if (p.isSpecialActivated() || (isGmaul && p.<Integer>getAttribOr(AttributeKey.GRANITE_MAUL_SPECIALS, 0) > 0)) { // spec bar can be off and gmaul will still activate
                    return p.getCombatSpecial().getCombatMethod();
                }
            }

            // Check if player is ranging..
            if (p.getCombat().getRangedWeapon() != null) {
                //System.out.println("Player is ranging with " + p.getCombat().getRangedWeapon());
                return RANGED_COMBAT;
            }
        } else if (attacker.isNpc()) {
            Npc npc = attacker.getAsNpc();

            // Attempt to return the npc's defined combat method..
            if (npc.getCombatMethod() != null) {
                //System.out.println("Combat method is: " + npc.getCombatMethod());
                return npc.getCombatMethod();
            }
        }

        // Return melee by default
        return MELEE_COMBAT;
    }

    /**
     * Generates a random {@link Splat} based on the argued entity's stats.
     *
     * @param attacker the entity to generate the random hit for.
     * @param target   the victim being attacked.
     * @param type     the combat type being used.
     * @return the HitDamage.
     */
    public static int calcDamageFromType(Mob attacker, Mob target, CombatType type) {
        int max_damage = 1;

        //We have a null typ, ignore.
        if (type == null) {
            return 0;
        }

        CombatType combat_type = type;

        switch (combat_type) {
            case MELEE:
                max_damage = attacker.isNpc() ? attacker.getAsNpc().combatInfo() == null ? 0 : attacker.getAsNpc().combatInfo().maxhit : attacker.getCombat().maximumMeleeHit();
                if (target instanceof Player) {
                    if (max_damage >= 85) {
                        max_damage = 85;
                    }
                }
                break;
            case RANGED:
                if (attacker.isPlayer()) {
                    Player p = attacker.getAsPlayer();
                    RangedWeapon rangeWeapon = p.getCombat().getRangedWeapon();
                    boolean ignoreArrows = rangeWeapon != null && rangeWeapon.ignoreArrowsSlot();
                    boolean special_attack = p.isSpecialActivated();

                    max_damage = p.getCombat().maximumRangedHit(ignoreArrows);
                    if (target instanceof Player) {
                        if (max_damage >= 78) {
                            max_damage = 78;
                        }
                    }
                } else {
                    //Npcs
                    max_damage = attacker.getCombat().maximumRangedHit(true);
                }
                break;

            case MAGIC:
                max_damage = attacker.getCombat().maximumMagicHit();
                if (target instanceof Player) {
                    if (max_damage >= 45) {
                        max_damage = 45;
                    }
                }
                break;
        }

        int damage = Utils.inclusive(0, max_damage);

        if (target != null && target.isNpc() && target.getAsNpc().isCombatDummy()) {
            CombatSpell spell = attacker.getCombat().getCastSpell() != null ? attacker.getCombat().getCastSpell() : attacker.getCombat().getAutoCastSpell();
            damage = spell == CombatSpells.CRUMBLE_UNDEAD.getSpell() ? target.hp() : max_damage;
        }

        if (type == CombatType.MELEE) {
            // Do melee effects with the calculated damage..
            if (attacker.isPlayer() && target != null) {
                boolean pvpDummy = target.isNpc() && target.getAsNpc().isPvPCombatDummy();
                if (target.isPlayer() || pvpDummy) {

                    Player player = attacker.getAsPlayer();

                    //Hard cap melee special attacks
                    if (player.isSpecialActivated()) {
                        if (player.getCombatSpecial() == CombatSpecial.DRAGON_DAGGER) {
                            if (damage > 48) {
                                damage = 48;
                            }
                        }
                        if (player.getCombatSpecial() == CombatSpecial.ARMADYL_GODSWORD) {
                            if (damage > 87) {
                                damage = 87;
                            }
                        }
                        if (player.getCombatSpecial() == CombatSpecial.ARMADYL_GODSWORD_OR) {
                            if (damage > 88) {
                                damage = 88;
                            }
                        }
                        if (player.getCombatSpecial() == CombatSpecial.ANCIENT_WARRIOR_MAUL) {
                            if (damage > 86) {
                                damage = player.getEquipment().hasAt(EquipSlot.WEAPON, ANCIENT_WARRIOR_MAUL_C) ? 89 : 86;
                            }
                        }
                        if (player.getCombatSpecial() == CombatSpecial.ANCIENT_WARRIOR_AXE) {
                            if (damage > 86) {
                                damage = player.getEquipment().hasAt(EquipSlot.WEAPON, ANCIENT_WARRIOR_AXE_C) ? 89 : 86;
                            }
                        }
                    }
                }
            }
        } else if (type == CombatType.RANGED) {
            if (attacker.isPlayer()) {
                Player player = attacker.getAsPlayer();
                // Handle bolt special effects for a player whose using crossbow
                if (player.getCombat().getWeaponType() == WeaponType.CROSSBOW) {
                    if (damage > 0) {
                        damage = RangedData.getBoltSpecialAttack(player, target, damage);
                    }
                }

                //Hard cap ranged special attacks
                if (player.isSpecialActivated()) {
                    if (player.getCombatSpecial() == CombatSpecial.DARK_BOW) {
                        if (damage > 48) {
                            damage = 48;
                        }
                    }
                }

                if (Equipment.hasAmmyOfDamned(player) && Equipment.fullKaril(player) && Utils.rollDie(100, 25)) {
                    // Second hit deals 50% of first hit. First hit will have protection prayers accounted for.
                    attacker.hit(player, damage / 2, CombatType.RANGED).checkAccuracy().submit();
                }
            }
        } else if (type == CombatType.MAGIC) {
            // Do magic effects with the calculated damage..
        }

        assert target != null;
        if (target.isNpc() && attacker.isPlayer()) {
            Npc npc = target.getAsNpc();
            Player player = (Player) attacker;

            //Disturb the whirlpools
            if (npc.id() == TENTACLE_WHIRLPOOL) {
                EnormousTentacle.onHit(player, npc);
            }

            if (npc.id() == KRAKEN_WHIRLPOOL) {
                damage = 0;
                KrakenBoss.onHit(player, npc);
            }

            if (player.getCombatSpecial() == CombatSpecial.VOLATILE_NIGHTMARE_STAFF) {
                if (damage > 87) {
                    damage = 87;
                }
            }

            /*if (npc.id() == VESPULA || npc.id() == VESPULA_7532) {
                Vespula.onHit(npc, damage);
            }*/

            //Cave Kraken
            if (npc.id() == 493) {
                npc.animate(7135);
                npc.transmog(492);
                npc.setHitpoints(125);//reset hp when disturbed
                npc.setHitpoints(target.hp() - damage);
                npc.setCombatMethod(new CaveKraken());
            }

            if (npc instanceof AlchemicalHydra hydra) {
                if (!hydra.getShieldDropped()) {
                    player.message("The Alchemical Hydra's defences partially absorb your attack!");
                    damage = Math.round(damage * 0.5F);
                }
            }

            Item weapon = player.getEquipment().get(EquipSlot.WEAPON);
            AttackType attackType = player.getCombat().getFightType().getAttackType();
            if (npc.id() == CORPOREAL_BEAST) {
                if (weapon != null && player.getEquipment().corpbeastArmour(weapon) && attackType != null && attackType.equals(AttackType.STAB)) {
                    damage *= 0.5d;
                }
            }
        }

        //PvP protection prayers
        if (damage > 0 && target != null) {

            if (target.isPlayer()) {
                int recoiled = (int) (damage * 0.10D);
                int rDamage = Math.round(recoiled);
                if (type == CombatType.MELEE && Prayers.usingPrayer(target, DAMPED_MELEE)) {
                    if (Utils.rollDie(50, 1)) {
                        attacker.hit(target, rDamage);
                    }
                } else if (type == CombatType.RANGED && Prayers.usingPrayer(target, DAMPEN_RANGE)) {
                    if (Utils.rollDie(50, 1)) {
                        attacker.hit(target, rDamage);
                    }
                } else if (type == CombatType.MAGIC && Prayers.usingPrayer(target, DAMPEN_MAGIC)) {
                    if (Utils.rollDie(50, 1)) {
                        attacker.hit(target, rDamage);
                    }
                }
            }

            if (target.isPlayer() && attacker.isPlayer()) {
                if (type == CombatType.MELEE && Prayers.usingPrayer(target, DAMPED_MELEE)) {
                    damage *= 0.4;
                } else if (type == CombatType.RANGED && Prayers.usingPrayer(target, DAMPEN_RANGE)) {
                    damage *= 0.4;
                } else if (type == CombatType.MAGIC && Prayers.usingPrayer(target, DAMPEN_MAGIC)) {
                    damage *= 0.4;
                } else if (type == CombatType.MELEE && Prayers.usingPrayer(target, PROTECT_FROM_MELEE)) {
                    damage *= 0.6;
                } else if (type == CombatType.RANGED && Prayers.usingPrayer(target, PROTECT_FROM_MISSILES)) {
                    damage *= 0.6;
                } else if (type == CombatType.MAGIC && Prayers.usingPrayer(target, PROTECT_FROM_MAGIC)) {
                    damage *= 0.6;
                }
            }
        }

        // Passive effect of the Elysian spirit shield
        var random = Utils.random(100);
        if (target.isPlayer() && random < 0.7) {
            Item shield = target.getAsPlayer().getEquipment().get(EquipSlot.SHIELD);
            if (shield != null && shield.getId() == 12817) {
                damage *= CombatConstants.ELYSIAN_DAMAGE_REDUCTION;
                target.performGraphic(new Graphic(321, 40)); // Elysian spirit shield effect gfx
            }
        }

        if (target.isPlayer()) {
            if (damage > 0 && Equipment.justiciarSet(target.getAsPlayer())) {
                int abosrbed = (int) (damage * 0.15);
                damage = damage - abosrbed;
            }
        }

        if (target.isPlayer()) {
            if (damage > 0 && Equipment.corruptedCrystalSet(target.getAsPlayer())) {
                int abosrbed = (int) (damage * 0.15);
                damage = damage - abosrbed;
            }
        }

        //If we have used the SOTD special attack, reduce incoming melee damage by 50%.
        if (target.isPlayer()) {
            if (target.getTimerRepository().has(TimerKey.SOTD_DAMAGE_REDUCTION) && target.getAsPlayer().getEquipment().containsAny(STAFF_OF_THE_DEAD, TOXIC_STAFF_OF_THE_DEAD, TOXIC_STAFF_UNCHARGED, TOXIC_STAFF_OF_THE_DEAD_C, STAFF_OF_LIGHT) && type == CombatType.MELEE) {
                damage *= CombatConstants.TSTOD_DAMAGE_REDUCTION;
            }
        }

        // Passive effect of Dinh's Bulwark on Block mode
        if (target.isPlayer()) {
            Item shield = target.getAsPlayer().getEquipment().get(EquipSlot.SHIELD);
            if (shield != null && shield.getId() == 21015 && target.getAsPlayer().getCombat().getFightType().getChildId() == 2) {
                damage -= damage / 5;
                damage *= CombatConstants.DINHS_BULWARK_REDUCTION;
            }
        }

        if (target.isPlayer()) {
            Player player = (Player) target;
            // Under 10% hp, hit won't kill us
            if (player.hp() - damage > 0 && player.hp() <= player.skills().xpLevel(Skills.HITPOINTS) / 10) {
                boolean ring = player.getEquipment().contains(2570);

                boolean defenceCape = (int) player.getAttribOr(AttributeKey.DEFENCE_PERK_TOGGLE, 0) == 1 && (player.getEquipment().contains(DEFENCE_CAPE) || player.getEquipment().contains(DEFENCE_CAPET));
                if (ring || (player.getEquipment().wearingMaxCape() && (int) player.getAttribOr(AttributeKey.MAXCAPE_ROL_ON, 0) == 1) || defenceCape) {

                    if (Teleports.rolTeleport(player)) {
                        Teleports.ringOfLifeTeleport(player);
                        if (ring) {
                            player.getEquipment().remove(new Item(RING_OF_LIFE), EquipSlot.RING, true);
                            player.message("Your Ring of Life shatters as it teleports you to safety!");
                        } else if (defenceCape) {
                            player.message("Your Defence Cape's Ring of Life effects kicks in and teleports you to safety!");
                        } else {
                            player.message("Your Max Cape's Ring of Life effect kicks in and teleports you to safety!");
                        }
                    }
                }
            }
        }

        target.takeHit();

        if (attacker.isPlayer()) {
            Player player = (Player) attacker;

            if (Prayers.usingPrayer(player, UMBRAS_VOW)) {
                double drainPercentage = 0.15;
                int skill;
                if (target instanceof Player targ) {
                    skill = targ.skills().getMaxLevel(Skill.DEFENCE);
                    int drainAmount = (int) (skill * drainPercentage);
                    skill -= drainAmount;
                    if (target.skills().level(Skill.DEFENCE.getId()) > skill) {
                        var drain = target.skills().level(Skills.DEFENCE) * 0.05D;
                        target.skills().setLevel(Skills.DEFENCE, (int) (target.skills().level(Skills.DEFENCE) - drain));
                    }
                } else if (target instanceof Npc npc) {
                    skill = npc.combatInfo().stats.defence;
                    int drainAmount = (int) (skill * drainPercentage);
                    skill -= drainAmount;
                    var drain = npc.combatInfo().stats.defence - skill;
                    npc.combatInfo().stats.defence -= drain;
                }
            }

            if (Prayers.usingPrayer(player, GLACIES_VOW)) {
                double drainPercentage = 0.15;
                int skill;
                if (target instanceof Player targ) {
                    skill = targ.skills().getMaxLevel(Skill.MAGIC);
                    int drainAmount = (int) (skill * drainPercentage);
                    skill -= drainAmount;
                    if (target.skills().level(Skill.MAGIC.getId()) > skill) {
                        var drain = target.skills().level(Skills.MAGIC) * 0.05D;
                        target.skills().setLevel(Skills.MAGIC, (int) (target.skills().level(Skills.MAGIC) - drain));
                    }
                } else if (target instanceof Npc npc) {
                    skill = npc.combatInfo().stats.magic;
                    int drainAmount = (int) (skill * drainPercentage);
                    skill -= drainAmount;
                    var drain = npc.combatInfo().stats.magic - skill;
                    npc.combatInfo().stats.magic -= drain;
                }
            }
        }

        if (attacker.isPlayer()) {
            Player player = (Player) attacker;

            if (player.hasPetOut("Wampa") && combat_type == CombatType.MAGIC) {
                //10% chance to activate the freeze effect
                if (World.getWorld().rollDie(100, player.getPlayerRights().isDeveloperOrGreater(player) ? 98 : 10)) {
                    player.pet().forceChat("FREEZEEEEE");
                    player.pet().graphic(540);

                    Chain.bound(null).runFn(3, () -> {
                        player.freeze(33, target);
                        target.graphic(369);
                        target.hit(player, World.getWorld().random(15));
                    });
                }
            }

            if (player.getEquipment().hasAt(EquipSlot.AMULET, ENCHANTED_SALAZAR_LOCKET)) {
                if (World.getWorld().rollDie(20, 1)) {
                    int healAmount = (int) Math.ceil(damage * 0.30D);
                    player.heal(healAmount);
                    player.graphic(1542);
                }
            }
            if (player.getEquipment().hasAt(EquipSlot.WEAPON, MENDING_WAND)) {//BLOODREVSUPDATE
                if (World.getWorld().rollDie(100, 20)) {
                    int healAmount = damage / 5;
                    player.heal(healAmount);
                    player.graphic(1542);
                }
            }
            if (player.getEquipment().hasAt(EquipSlot.WEAPON, 30103)) {//dzone3
                if (World.getWorld().rollDie(100, 15)) {
                    int healAmount = damage / 5;
                    player.heal(healAmount);
                    player.graphic(1542);
                }
            }

            if (player.getEquipment().hasAt(EquipSlot.RING, RING_OF_DIVINATION)) {
                if (World.getWorld().rollDie(100, 10)) {
                    int healAmount = damage / 5;
                    player.heal(healAmount);
                    player.graphic(1812);
                }
            }
            if (player.hasPetOut("Mending life bird")) {//updatem
                if (World.getWorld().rollDie(10, 2)) {//20% chance to heal
                    int healAmount = damage * 15 / 100;//15% of the total damage
                    player.heal(healAmount);
                    player.graphic(1605);
                }
            }
            if (player.getEquipment().hasAt(EquipSlot.WEAPON, CustomItemIdentifiers.HOLY_GHRAZI_RAPIER)) {//s23update
                if (World.getWorld().rollDie(10, 2)) {//20% chance to heal
                    int healAmount = damage * 15 / 100;//15% of the total damage
                    player.heal(healAmount);
                    player.graphic(1605);
                }
            }
            if (player.getEquipment().hasAt(EquipSlot.WEAPON, 23214)) {//s23update
                if (World.getWorld().rollDie(10, 2)) {//20% chance to heal
                    int healAmount = damage * 15 / 100;//15% of the total damage
                    player.heal(healAmount);
                    player.graphic(1605);
                }
            }
            if (player.hasPetOut("Blood firebird")) {
                if (World.getWorld().rollDie(10, 2)) {//20% chance to heal
                    int healAmount = damage * 15 / 100;//15% of the total damage
                    player.heal(healAmount);
                    player.graphic(1605);
                }
            }
        }

        // Return our hitDamage that may have been modified slightly.
        return damage;
    }

    /**
     * Checks if an entity is a valid target.
     */
    public static boolean validTarget(Mob attacker, Mob target) {
        //Check if target is online and alive
        if (!target.isRegistered() || !attacker.isRegistered() || target.hp() <= 0) {
            attacker.getCombat().reset();//Target not valid reset combat
            return false;
        }

        // Check if teleporting away/teleported away
        if (attacker.tile().distance(target.tile()) >= 16) {
            attacker.getCombat().reset();
            return false;
        }

        // Check if any of the two have wrong session state.
        if (target.isPlayer()) {
            if (target.getAsPlayer().getSession().getState() != SessionState.LOGGED_IN) {
                return false;
            }
        }
        if (attacker.isPlayer()) {
            if (attacker.getAsPlayer().getSession().getState() != SessionState.LOGGED_IN) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if an entity can reach a target.
     * <br> PERFORMANCE NOTE: this includes PROJECTILE PATH FINDING - try to only call it once per cycle!
     *
     * @param attacker The entity which wants to attack.
     * @param method   The combat type the attacker is using.
     * @param target   The victim.
     * @return True if attacker has the proper distance to attack, otherwise false.
     */
    public static boolean canReach(Mob attacker, CombatMethod method, Mob target) {
        Debugs.CB_FOLO.debug(attacker, "enter can reach via " + method + " vs " + target, target, true);
        if (attacker == null || target == null || method == null) {
            return false;
        }

        if (!validTarget(attacker, target)) {
            if (attacker.isPlayer()) {
                attacker.getAsPlayer().debugMessage("You do not have a valid target.");
            }
            return false;
        }

        if (attacker.isNpc()) {
            if (method instanceof CommonCombatMethod) {
                CommonCombatMethod commonCombatMethod = (CommonCombatMethod) method;
                commonCombatMethod.set(attacker, target);
                if (attacker.isNpc() && !commonCombatMethod.inAttackRange()) {
                    return false;
                }
            } else {
                return DumbRoute.withinDistance(attacker, target, method.getAttackDistance(attacker));
            }
        }
        // P.s dont reset queue here. done elsewhere.
        return true;
    }

    private static long pjTimerForArena() {
        return 4_600L;
    }

    /**
     * Checks if an entity can attack a target.
     *
     * @param entity The entity which wants to attack.
     * @param method The combat type the attacker is using.
     * @param other  The victim.
     * @return True if attacker has the requirements to attack, otherwise false.
     */
    public static boolean canAttack(Mob entity, CombatMethod method, Mob other) {
        Debugs.CMB.debug(entity, "enter can attack", other, true);

        if (entity == null || other == null) {
            Debugs.CMB.debug(entity, "attacker or target null", other, true);
            return false;
        }

        if (entity.dead() || other.dead()) {
            Debugs.CMB.debug(entity, "ded", other, true);
            return false;
        }

        if (entity.getIndex() == -1 || other.getIndex() == -1) { // Target logged off.
            Debugs.CMB.debug(entity, "attacker or target logged off", other, true);
            return false;
        }

        if (entity.stunned()) {
            // Calling stun interrupts combat, but this will force stop it too.
            Debugs.CMB.debug(entity, "cant attack stunned", other, true);
            return false;
        }

        if (entity.locked()) {
            Debugs.CMB.debug(entity, "cant attack locked", other, true);
            return false;
        }

        if (other.tile().level != entity.tile().level) {
            Debugs.CMB.debug(entity, "cant attack not on the same height level", other, true);
            return false;
        }

        if (entity.isNpc() && entity.getAsNpc().getBotHandler() != null) {
            if (!(WildernessArea.inWilderness(entity.tile())) || !(WildernessArea.inWilderness(other.tile()))) {
                //Only reset combat if NPC is trying to attack player outside of wilderness. Don't reset players combat regardless of if they are attacker or target.
                Debugs.CMB.debug(entity, "bots cannot attack outside of wilderness.", other, true);
                return false;
            }
        }

        if (other.isNpc() && other.getAsNpc().getBotHandler() != null) {
            if (!(WildernessArea.inWilderness(entity.tile())) || !(WildernessArea.inWilderness(other.tile()))) {
                Debugs.CMB.debug(entity, "bots cannot be attacked outside of wilderness.", other, true);
                return false;
            }
        }

        if (entity instanceof Player player && !(other instanceof Npc)) {
            for (var i : bannedPvpItems) {
                if (i == null)
                    continue;

                if (player.getEquipment().contains(i.getItemId())) {
                    player.message(Color.RED.wrap("You cannot attack this player while using " + player.getEquipment().getSlotForItem(i.getEquipSlot()).name() + "."));
                    return false;
                }
            }
        }

        if (!MemberZone.canAttack(entity, other)) {
            Debugs.CMB.debug(entity, "cant attack member zone npc", other, true);
            return false;
        }

        if (!TournamentManager.canAttack(entity, other)) {
            Debugs.CMB.debug(entity, "cant attack tourny", other, true);
            return false;
        }

        if (entity.isNpc() && entity.getAsNpc().def().gwdRoomNpc && !CombatFactory.bothInFixedRoom(entity, other)) {
            Debugs.CMB.debug(entity, "not in same room", other, true);
            return false;
        }

        // Check if we can attack in this area
        if (!ControllerManager.canAttack(entity, other)) {
            Debugs.CMB.debug(entity, "You can't attack that player.", other, true);
            entity.getMovementQueue().reset();
            return false;
        }

        if (other.isNpc()) {
            var npc = other.getAsNpc();
            var player = entity.getAsPlayer();
            if (npc.hidden() || (entity.isPlayer() && npc.id() == 7707) || npc.locked() && npc.id() != 5886 && npc.id() != 2668) {
                Debugs.CMB.debug(entity, "cant attack idk what this is hidden" + npc.hidden(), other, true);
                return false;
            }

            if (npc.id() == KILLER) {
                if (!player.getEquipment().hasAt(EquipSlot.WEAPON, KILLERS_KNIFE_21059)) {
                    player.message(Color.RED.wrap("You can only kill these killers with a Killer's knife."));
                    return false;
                }
            }
            if (npc.id() == FRAGMENT_OF_SEREN && !player.tile().inSerenRoom(player) && !player.tile().inDzoneThree(player)) {//updateseren
                player.getCombat().reset();
                player.message("Hmm... can't be attacked from here");
                npc.getCombat().reset();
                return false;

            }
            if (npc.id() == GIANT_MOLE && player.tile().inArea(1742, 5224, 1742, 5224)) {//upos
                player.getCombat().reset();
                player.message("Hmm... can't be attacked from here");
                return false;

            }
            if (player.tile().inArea(3311, 5279, 3312, 5281)) {
                player.getCombat().reset();
                player.message("Hmm... can't be attacked from here");
                return false;

            }
            if (player.tile().x == 3311 && player.tile().y == 5309) {
                player.getCombat().reset();
                player.message("Hmm... can't be attacked from here");
                return false;
            }
            if (player.tile().x == 3189 && player.tile().y == 4596) {
                player.getCombat().reset();
                player.message("Hmm... can't be attacked from here");
                return false;
            }
            if (player.tile().inArea(3311, 5310, 3312, 5313)) {
                player.getCombat().reset();
                player.message("Hmm... can't be attacked from here");
                return false;

            }
            if (player.tile().inArea(3138, 4566, 3138, 4566)) {
                player.getCombat().reset();
                player.message("Hmm... can't be attacked from here");
                return false;
            }
            if (player.tile().inArea(3154, 4651, 3154, 4651)) {
                player.getCombat().reset();
                player.message("Hmm... can't be attacked from here");
                return false;
            }
            if (player.tile().inArea(3310, 5323, 3315, 5323)) {
                player.getCombat().reset();
                player.message("Hmm... can't be attacked from here");
                return false;

            }
            if (player.tile().inArea(3142, 4558, 3144, 4559)) {
                player.message("Hmm... can't be attacked from here");
                return false;
            }

            if (player.tile().inArea(3137, 4567, 3144, 4567)) {
                player.message("Hmm... can't be attacked from here");
                return false;
            }
            if (player.tile().inArea(3145, 4559, 3145, 4566)) {
                player.message("Hmm... can't be attacked from here");
                return false;
            }
            if (player.tile().inArea(3137, 4560, 3137, 4566)) {
                player.message("Hmm... can't be attacked from here");
                return false;
            }
            if (player.tile().inArea(3177, 4630, 3181, 4633)) {
                player.message("Hmm... can't be attacked from here");
                return false;
            }
            if (player.tile().inArea(3194, 4596, 3198, 4601)) {
                player.message("Hmm... can't be attacked from here");
                return false;
            }
            if (entity.isPlayer() && other.isNpc()) {
                Party party = player.raidsParty;
                if (party != null) {
                    if (npc.id() == LORD_VOLDEMORT) {
                        boolean canDamageVoldy = player.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND_RAIDS) || player.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND);
                        if (!canDamageVoldy) {
                            player.message(Color.RED.wrap("You can only damage Lord Voldemort with a elder wand."));
                            return false;
                        }
                    }

                    var melee = entity.getCombat().getCombatType() == CombatType.MELEE;
                    var magic = entity.getCombat().getCombatType() == CombatType.MAGIC;
                    var range = entity.getCombat().getCombatType() == CombatType.RANGED;
                    if (party.getLeftHandNpc() != null && other.getAsNpc().id() == party.getLeftHandNpc().id() && !melee) {
                        entity.message(Color.RED.wrap("You can only fight this claw with melee."));
                        return false;
                    } else if (party.getRightHandNpc() != null && other.getAsNpc().id() == party.getRightHandNpc().id() && !magic) {
                        entity.message(Color.RED.wrap("You can only fight this claw with magic."));
                        return false;
                    } else if (party.getGreatOlmNpc() != null && other.getAsNpc().id() == party.getGreatOlmNpc().id() && !range) {
                        entity.message(Color.RED.wrap("You can only fight the Basilisk with range."));
                        return false;
                    }
                }
            }

            if (entity.isPlayer() && npc.id() == 7555) {
                Party party = player.raidsParty;
                if (party != null) {
                    if (!party.isCanAttackLeftHand()) {
                        entity.message(Color.RED.wrap("You can't attack it's left claw, kill the right claw first!"));
                        Debugs.CMB.debug(entity, "You can't attack it's left claw, kill the right claw first!", other, true);
                        return false;
                    }
                }
            }
            if (entity.isPlayer() && npc.id() == 7554) {
                Party party = player.raidsParty;
                if (party != null) {
                    if (!party.isLeftHandDead()) {
                        entity.message(Color.RED.wrap("You can't attack it's left claw, kill the right claw first!"));
                        Debugs.CMB.debug(entity, "You can't attack it's left claw, kill the right claw first!", other, true);
                        return false;
                    }
                }
            }

            if (npc.id() == 2668) // you can always attack combat dummies
                return true;
        }

        var wep = -1;

        if (entity.isPlayer()) {
            wep = (entity.getAsPlayer()).getEquipment().get(EquipSlot.WEAPON) != null ? (entity.getAsPlayer()).getEquipment().get(EquipSlot.WEAPON).getId() : -1;

            // Check if we're using a special attack..
            if (entity.isSpecialActivated() && entity.getAsPlayer().getCombatSpecial() != null) {
                if (entity.isPlayer()) {
                    Player player = entity.getAsPlayer();
                    if (player.hasPetOut("Baby Abyssal Demon") && player.getCombatSpecial() == CombatSpecial.DRAGON_DAGGER) {
                        player.getCombatSpecial().setDrainAmount(20);
                    } else if (player.getCombatSpecial() == CombatSpecial.DRAGON_DAGGER) {
                        player.getCombatSpecial().setDrainAmount(25);
                    }
                }
                // Check if we have enough special attack percentage.
                // If not, reset special attack.
                double vigour = 0;
                if (entity.isPlayer() && entity.getAsPlayer().getEquipment().hasAt(EquipSlot.RING, RING_OF_VIGOUR)) {
                    vigour += entity.getSpecialAttackPercentage() * 0.1;
                }

                int specPercentage = (int) (entity.getSpecialAttackPercentage() + vigour);

                //Make sure the player has enough special attack
                if (specPercentage < entity.getAsPlayer().getCombatSpecial().getDrainAmount()) {
                    entity.message("You do not have enough special attack energy left!");
                    entity.setSpecialActivated(false);
                    CombatSpecial.updateBar(entity.getAsPlayer());
                    return false;
                }
            }
        }

        if (other.isPlayer()) {
            // Can't attack invis
            var them = other.getAsPlayer();
            if (them.looks().hidden()) {
                Debugs.CMB.debug(entity, "cant attack invisible target", other, true);
                return false;
            }


            if (entity instanceof Player player) {
                if (player.getEquipment().containsAny(CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR, SANGUINE_TWISTED_BOW, TWISTED_BOW_I, DRAGON_COMPANION, TUMEKENS_SHADOW, TRIDENT_OF_THE_SEAS, TRIDENT_OF_THE_SEAS_E,
                    TRIDENT_OF_THE_SEAS_FULL, TRIDENT_OF_THE_SWAMP, TRIDENT_OF_THE_SWAMP_E, SANGUINESTI_STAFF, HOLY_SANGUINESTI_STAFF, STARTER_SWORD, STARTER_BOW, STARTER_STAFF)) {
                    player.message(Color.RED.wrap("You cannot attack players with a " + player.getEquipment().getWeapon().name() + "."));
                    player.getMovementQueue().clear();
                    player.getCombat().reset();
                    return false;
                }
            }

            // This check for being in an attackable zone has no message because this check is done elsewhere
            // Which gives a messages in those other places.
            // This check if for multi-target attacks like barrage/burst/chins where extra targets have to be checked
            // For combat validity.
            if (entity.isPlayer() && !WildernessArea.inAttackableArea(them)) {
                Debugs.CMB.debug(entity, "cant attack not in an attackable area", other, true);
                return false;
            }

            // Kraken attacking Players.
            if (entity.isNpc()) {
                if ((entity.getAsNpc()).id() == KrakenBoss.KRAKEN_NPCID || (entity.getAsNpc()).id() == KrakenBoss.TENTACLE_NPCID || (entity.getAsNpc()).id() == KrakenBoss.TENTACLE_NPCID || (entity.getAsNpc()).id() == TENTACLE_WHIRLPOOL) {
                    return true;
                }
            }
        } else if (other.isNpc()) {
            if ((other.getAsNpc()).combatInfo() == null) {
                entity.message("Without combat attributes this monster is unattackable.");
                return false;
            } else if ((other.getAsNpc()).combatInfo().unattackable) {
                entity.message("You cannot attack this monster.");
                return false;
            } else if (entity instanceof Player player && other instanceof Npc npc && !player.getEquipment().contains(CustomItemIdentifiers.SUMMER_SOAKER) && npc.id() == CustomNpcIdentifiers.SUMMER_IMP) {
                player.message(Color.RED.wrap("You can only damage a Summer Imp while using a Summer Soaker."));
                player.getCombat().reset();
                return false;
            }

            //If we're attacking the Abyssal Sire and it's currently disoriented..
            if ((other.getAsNpc()).id() == 5886) {
                var combatState = other.<AbyssalSireState>getAttribOr(AttributeKey.ABYSSAL_SIRE_STATE, AbyssalSireState.DISORIENTED);

                if (combatState == AbyssalSireState.DISORIENTED) {
                    entity.message("The sire is disoriented. Maybe you can do something useful while it's unable to control the tentacles.");
                    return false;
                }
            }
        }

        // The last time our target was attacked
        var targetLastAttackedTime = System.currentTimeMillis() - other.<Long>getAttribOr(AttributeKey.LAST_WAS_ATTACKED_TIME, 0L);

        if (entity.isPlayer() && other.isNpc()) {
            var oppNpc = other.getAsNpc();
            if (oppNpc.combatInfo() != null) {
                var noRequirementNeeded = entity.getAsPlayer().getSlayerRewards().getUnlocks().containsKey(SlayerConstants.NO_SLAYER_REQ);
                var slayerReq = Math.max(SlayerCreature.slayerReq(oppNpc.id()), oppNpc.combatInfo().slayerlvl);
                if (!noRequirementNeeded && slayerReq > (entity.getAsPlayer()).skills().level(Skills.SLAYER)) {
                    entity.message("You need a slayer level of " + slayerReq + " to harm this NPC.");
                    return false;
                }
            }

            if (wep == 10501) {
                entity.message("You can only pelt other players with a snowball.");
                return false;
            } else if (oppNpc.id() == 5914) {
                var respiratoryState = other.<AbyssalSireState>getAttribOr(AttributeKey.ABYSSAL_SIRE_STATE, AbyssalSireState.STASIS);

                if (respiratoryState == AbyssalSireState.STASIS) {
                    entity.message("I can't reach that!");
                    return false;
                }
            }

            // The kraken boss already has a focus. Multi doesn't matter now.
            if (oppNpc.id() == KRAKEN_WHIRLPOOL && oppNpc.transmog() == KrakenBoss.KRAKEN_NPCID) {
                if (other.<WeakReference<Mob>>getAttribOr(AttributeKey.TARGET, new WeakReference(null)).get() != entity && targetLastAttackedTime < 10000L) {
                    entity.message("The Kraken already has a target.");
                    return false;
                }
            }

        }
        if (entity.getX() >= 1883 && entity.getX() <= 1895 && entity.getY() >= 3861 && entity.getY() <= 3892) {
            entity.message("Please move from here to be able to attack this npc");
            entity.getCombat().reset();
            return false;
        }
        if (entity.getX() >= 1898 && entity.getX() <= 1912 && entity.getY() >= 3852 && entity.getY() <= 3857) {
            entity.message("Please move from here to be able to attack this npc");
            entity.getCombat().reset();
            return false;
        }
        if (entity.getX() >= 1846 && entity.getX() <= 1870 && entity.getY() >= 3841 && entity.getY() <= 3852) {
            entity.message("Please move from here to be able to attack this npc");
            entity.getCombat().reset();
            return false;
        }
        // Last person to hit our target.
        var targetLastAttacker = other.<Mob>getAttrib(AttributeKey.LAST_DAMAGER);

        // Last time we were attacked
        var myLastAttackedTime = System.currentTimeMillis() - entity.<Long>getAttribOr(AttributeKey.LAST_WAS_ATTACKED_TIME, 0L);

        // Last person to hit us.
        var myLastAttacker = entity.<Mob>getAttrib(AttributeKey.LAST_DAMAGER);

        var me_edgepk = GameServer.properties().edgeDitch10secondPjTimerEnabled && WildernessArea.inside_extended_pj_timer_zone(entity.tile());
        var targ_edgepk = GameServer.properties().edgeDitch10secondPjTimerEnabled && WildernessArea.inside_extended_pj_timer_zone(other.tile());

        var myTimeToPj = me_edgepk ? 10_000L : 4_600L;
        var areaPjTimer = pjTimerForArena();
        if (areaPjTimer != 4_600L)
            myTimeToPj = areaPjTimer;
        var targTimeToPj = targ_edgepk ? 10_000L : 4_600L;
        if (areaPjTimer != 4_600L)
            targTimeToPj = areaPjTimer;

        if (entity.isPlayer() && other.isPlayer()) {
            Player playerAttacker = entity.getAsPlayer();

            //As of 06/07/2021 you can no longer use tridents and elder wand on players
            if (playerAttacker.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND) || playerAttacker.getEquipment().hasAt(EquipSlot.WEAPON, TRIDENT_OF_THE_SWAMP) || playerAttacker.getEquipment().hasAt(EquipSlot.WEAPON, TRIDENT_OF_THE_SEAS) || playerAttacker.getEquipment().hasAt(EquipSlot.WEAPON, SANGUINESTI_STAFF) || playerAttacker.getEquipment().hasAt(EquipSlot.WEAPON, HOLY_SANGUINESTI_STAFF)) {
                entity.message(Color.RED.wrap("You cannot use a this magic weapon against a player."));
                return false;
            }
//you want only elder wand or all of those work? yeah you can put all back in , you mean only elder wand to work? no the trident, and sang are fine to work as well in pvp i didnt know they didnt.

            //cool all done your the man how easy is this real quck to fix another safe spot
            // Staking security
            if (Dueling.not_in_area(entity, other, "You can't attack them.")) {
                return false;
            }
            if (Dueling.stake_not_started(entity, other)) {
                entity.message("The fight hasn't started yet!");
                return false;
            }
        }

        //If the NPC isn't visible we should no longer be attacking them.
        if (entity.isNpc()) {
            var npc = entity.getAsNpc();
            if (npc.hidden()) {
                Debugs.CMB.debug(entity, "cant attack npc not visible", other, true);
                return false;
            }
            if (other.isPlayer()) {
                if ((other.getAsPlayer()).getInterfaceManager().getMain() == BARROWS_REWARD_WIDGET) {
                    // When viewing the barrows loot interface, NPCs are not aggressive. Interesting eh.
                    Debugs.CMB.debug(entity, "cant attack player in barrows reward widget", other, true);
                    return false;
                }
            }
        }

        var inArena = Dueling.in_duel(entity);
        var lastDamager = other.<Mob>getAttribOr(AttributeKey.LAST_DAMAGER, null);

        // Level checks only apply to PvP
        if (other.isPlayer() && entity.isPlayer()) {
            // Is the player deep enough in the wilderness?
            // FFA Clan wars does not make any checks for levels. Free for all :)
            if (!inArena) {

                var oppWithinLvl = entity.skills().combatLevel() >= getLowestLevel(other, entity) &&
                    entity.skills().combatLevel() <= getHighestLevel(other, entity);

                if (!oppWithinLvl) {
                    entity.message((!WildernessArea.inWilderness(entity.tile())) ? "Your level difference is too great! You need to move deeper into the Wilderness." : "Your level difference is too great.");
                    return false;
                } else {
                    var withinLvl = (other.skills().combatLevel() >= getLowestLevel(entity, other) &&
                        other.skills().combatLevel() <= getHighestLevel(entity, other));
                    if (!withinLvl) {
                        entity.message((!WildernessArea.inWilderness(entity.tile())) ? "Your level difference is too great! You need to move deeper into the Wilderness." : "Your level difference is too great.");
                        return false;
                    }
                }
            }
        }

        var isOpponentDead = myLastAttacker == null || myLastAttacker.dead();

        if (myLastAttackedTime < myTimeToPj && myLastAttacker != null && myLastAttacker != other && !isOpponentDead) {
            // Multiway check bro!
            if (entity.isPlayer()) {
                if (entity.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA, -1) != 1 && !MultiwayCombat.includes(other)) {
                    entity.message("You're already under attack.");
                    return false;
                }
            } else {
                if (!MultiwayCombat.includes(entity)) {
                    entity.message("I'm already under attack.");
                    return false;
                }
            }
        }

        if (targetLastAttackedTime < targTimeToPj && targetLastAttacker != null && targetLastAttacker != entity) {
            // Multiway check bro!
            if (other.isPlayer()) {
                if (other.<Integer>getAttribOr(AttributeKey.MULTIWAY_AREA, -1) != 1 && !MultiwayCombat.includes(other)) {
                    entity.message("Someone else is already fighting your opponent.");
                    return false;
                }
            } else {
                if (!MultiwayCombat.includes(other)) {
                    entity.message("Someone else is fighting that.");
                    return false;
                }
            }
        }

        if (other.isNpc() && other.getAsNpc().getCombatMethod() != null && other.getAsNpc().getCombatMethod().canMultiAttackInSingleZones()) {
            return true;
        }

        // Check immune npcs..
        if (other.isNpc()) {
            Npc npc = (Npc) other;
            if (npc.getBotHandler() != null) {
                if (entity.isPlayer()) {
                    var oppWithinLvl = entity.skills().combatLevel() >= getLowestLevel(entity, npc) &&
                        entity.skills().combatLevel() <= getHighestLevel(entity, npc);

                    if (!oppWithinLvl) {
                        entity.message((!WildernessArea.inWilderness(entity.tile())) ? "Your level difference is too great! You need to move deeper into the Wilderness." : "Your level difference is too great.");
                        return false;
                    } else {
                        var withinLvl = npc.def().combatlevel >= getLowestLevel(entity, npc) && npc.def().combatlevel <= getHighestLevel(entity, npc);
                        if (!withinLvl) {
                            entity.message((!WildernessArea.inWilderness(entity.tile())) ? "Your level difference is too great! You need to move deeper into the Wilderness." : "Your level difference is too great.");
                            return false;
                        }
                    }
                }
            }

            if (npc.getTimerRepository().has(TimerKey.ATTACK_IMMUNITY)) {
                if (entity.isPlayer()) {
                    ((Player) entity).message("This npc is currently immune to attacks.");
                }
                Debugs.CMB.debug(entity, "cant attack 7", other, true);
                return false;
            }
        }

        Debugs.CMB.debug(entity, "Passed canAttack checks", other, true);
        return true;
    }

    /**
     * handles printing/logging. will always print. should only be called from {@link Debugs#} which includes enabled check.
     *
     * @param attacker
     * @param s
     * @param victim
     * @param debugMessage
     */
    public static void debug(Mob attacker, String s, @Nullable Mob victim, boolean debugMessage) {
        debugMessage = true;
        boolean print = !GameServer.properties().production && (attacker != null && attacker.isPlayer() && attacker.getAsPlayer().getPlayerRights().isDeveloperOrGreater(attacker.getAsPlayer())) ||
            (victim != null && victim.isPlayer() && victim.getAsPlayer().getPlayerRights().isDeveloperOrGreater(victim.getAsPlayer()));
        boolean debug = false;
        Player player = null;
        if (attacker != null && attacker.isPlayer()) {
            player = attacker.getAsPlayer();
            debug = player.getAttribOr(AttributeKey.DEBUG_MESSAGES, debug);
        } else if (victim != null && victim.isPlayer()) {
            player = victim.getAsPlayer();
            debug = player.getAttribOr(AttributeKey.DEBUG_MESSAGES, debug);
        }
        String vicname = victim == null ? "?" : victim.getMobName();
        if (attacker != null && print && debug) {
            attacker.forceChat(GameEngine.gameTicksIncrementor + ": " + attacker.getMobName() + " v " + vicname + ": " + s);
        }
        if (attacker != null && debug && debugMessage && player != null) {
            System.out.println(GameEngine.gameTicksIncrementor + ": " + attacker.getMobName() + " v " + vicname + ": " + s);
        }
        if (attacker != null) {
            if (attacker.getLocalPlayers().stream().filter(p -> p.tile().distance(attacker.tile()) < 10).findAny().isPresent()) {
                logger.info(GameEngine.gameTicksIncrementor + ": " + attacker.getMobName() + " v " + vicname + ": " + s);
            }
        } else {
            logger.info(GameEngine.gameTicksIncrementor + ": " + s);
        }
    }

    /**
     * Adds a hit to a target's queue.
     *
     * @param hit
     */
    public static void addPendingHit(Hit hit) {
        Mob attacker = hit.getAttacker();
        Mob target = hit.getTarget();

        if (target.dead()) {
            return;
        }

        if (attacker instanceof Npc npc) {
            if (npc.def().name != null) {
                boolean isRevenant = npc.def().name.toLowerCase().contains("revenant");
                if (isRevenant && target.isPlayer()) {
                    Player playerTarget = target.getAsPlayer();
                    var memberCave = playerTarget.tile().memberCave() && playerTarget.getMemberRights().isDiamondOrGreater(playerTarget);
                    if (playerTarget.getEquipment().hasAt(EquipSlot.HANDS, BRACELET_OF_ETHEREUM) || memberCave) {
                        int newDamage = hit.getDamage() * 25 / 100;
                        hit.setDamage(newDamage);
                    }
                }
            }
        }

        if (attacker instanceof Player player) {
            if (player.hasPetOut(366)) {
                if (Utils.rollDie(25, 1)) {
                    if (hit.getDamage() > 0) {
                        handleGuthans(player, target, hit.getDamage());
                    }
                }
            }
        }

        if (target.isNpc() && attacker.isPlayer()) {
            if (target instanceof Npc) {
                Npc npc = (Npc) target;
                Player player = (Player) attacker;
                if (player.getCombat() == null) return; // should never happen lol

                if (npc.getCombatMethod() instanceof Vorkath) {
                    Vorkath combatMethod = (Vorkath) npc.getCombatMethod();
                    if (combatMethod.resistance != null) {
                        switch (combatMethod.resistance) {
                            case PARTIAL -> hit.setDamage((int) (hit.getDamage() * 0.5d));
                            case FULL -> hit.setDamage(0);
                        }
                    }
                }

                CombatSpell spell = player.getCombat().getCastSpell() != null ? player.getCombat().getCastSpell() : player.getCombat().getAutoCastSpell();
                if (spell != null && spell.name().equals("Crumble Undead")) {
                    if (npc.def().name.equalsIgnoreCase("Zombified Spawn")) {
                        hit.setDamage(npc.hp());
                    }
                }

                if (spell != null && spell.name().equalsIgnoreCase("Avada Kedavra")) {
                    if (target.isNpc()) {
                        boolean insideAnyRaids = false;
                        if (player.getRaids() != null) {
                            insideAnyRaids = player.getRaids().raiding(player);
                        }
                        hit.setDamage(npc.isWorldBoss() || insideAnyRaids || npc.id() == THE_NIGHTMARE ? 500 : npc.hp());
                    }
                }

                //One in 175 chance of dealing the finishing blow. This does not count towards world bosses//up00
                boolean ignore = npc.isWorldBoss() || npc.id() == 11278 || npc.id() == FRAGMENT_OF_SEREN || npc.id() == NpcIdentifiers.TZTOKJAD || npc.id() == NpcIdentifiers.CORPOREAL_BEAST || npc.isCombatDummy() || (player.getRaids() != null && player.getRaids().raiding(player)) || npc.id() == THE_NIGHTMARE_9430;
                if (player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.KILL_BLOW) && World.getWorld().rollDie(350, 1) && !ignore && !npc.locked()) {
                    hit.setDamage(npc.hp());
                }
                if (target.getAsNpc().id() == TENTACLE_5912 && WildernessArea.inDiamondZone(player.tile()) && !player.tile().inArea(2468, 2763, 2476, 2772) && !player.tile().inArea(2445, 2763, 2453, 2772)) {
                    player.getCombat().reset();
                    player.message("enter the area to be able to attack");
                    npc.getCombat().reset();
                    return;
                }
                if (target.getAsNpc().id() == TENTACLE_5913 && WildernessArea.inDiamondZone(player.tile()) && !player.tile().inArea(2445, 2779, 2453, 2791) && !player.tile().inArea(2468, 2779, 2476, 2791)) {
                    player.getCombat().reset();
                    player.message("enter the area to be able to attack");
                    npc.getCombat().reset();
                    return;
                }
                if (target.getAsNpc().id() == 5923 && WildernessArea.inDiamondZone(player.tile()) && !player.tile().inArea(2468, 2798, 2476, 2807) && !player.tile().inArea(2445, 2798, 2453, 2807)) {
                    player.getCombat().reset();
                    player.message("enter the area to be able to attack");
                    npc.getCombat().reset();
                    return;
                }
                if (target.getAsNpc().id() == 2616 && WildernessArea.inDiamondZone(player.tile()) && !player.tile().inArea(2458, 2800, 2463, 2807)) {
                    player.getCombat().reset();
                    player.message("enter the area to be able to attack");
                    npc.getCombat().reset();
                    return;
                }
                if (target.getAsNpc().id() == 2617 && WildernessArea.inDiamondZone(player.tile()) && !player.tile().inArea(2458, 2800, 2463, 2807)) {
                    player.getCombat().reset();
                    player.message("enter the area to be able to attack");
                    npc.getCombat().reset();
                    return;
                }
                if (player.tile().inArea(Tile.MB) && hit.getCombatType() != CombatType.MAGIC) {
                    player.message("You can only use magic inside the arena!");
                    player.getCombat().reset();
                    return;
                }


                if ((hit.getCombatType() == CombatType.RANGED || hit.getCombatType() == CombatType.MAGIC) && npc.id() == NpcIdentifiers.KALPHITE_QUEEN_6500) {
                    hit.setDamage(0);
                }

                if (npc.id() == NpcIdentifiers.KALPHITE_QUEEN_6501 && hit.getCombatType() == CombatType.MELEE) {
                    hit.setDamage(0);
                }

                if (npc.id() == VANGUARD_7527 && hit.getCombatType() != CombatType.MELEE) {
                    hit.setDamage(0);
                }

                if (npc.id() == TEKTON_ENRAGED_7544 && hit.getCombatType() != CombatType.MELEE) {
                    hit.setDamage(0);
                }

                if (npc.id() == NpcIdentifiers.VANGUARD_7528 && hit.getCombatType() != CombatType.RANGED) {
                    hit.setDamage(0);
                }

                if (npc.id() == NpcIdentifiers.VANGUARD_7529 && hit.getCombatType() != CombatType.MAGIC) {
                    hit.setDamage(0);
                }

                if (npc.id() == NpcIdentifiers.DEMONIC_GORILLA && hit.getCombatType() == CombatType.MELEE) {
                    hit.setDamage(0);
                }

                if (npc.id() == NpcIdentifiers.DEMONIC_GORILLA_7145 && hit.getCombatType() == CombatType.RANGED) {
                    hit.setDamage(0);
                }

                if (npc.id() == NpcIdentifiers.DEMONIC_GORILLA_7146 && hit.getCombatType() == CombatType.MAGIC) {
                    hit.setDamage(0);
                }

                if (npc.id() == NpcIdentifiers.CORRUPTED_HUNLLEF && hit.getCombatType() == CombatType.MELEE) {
                    hit.setDamage(0);
                }

                if (npc.id() == NpcIdentifiers.CORRUPTED_HUNLLEF_9036 && hit.getCombatType() == CombatType.RANGED) {
                    hit.setDamage(0);
                }

                if (npc.id() == NpcIdentifiers.CORRUPTED_HUNLLEF_9037 && hit.getCombatType() == CombatType.MAGIC) {
                    hit.setDamage(0);
                }

                boolean vetionHellhoundSpawned = npc.getAttribOr(AttributeKey.VETION_HELLHOUND_SPAWNED, false);

                if ((npc.id() == 6611 || npc.id() == 6612) && vetionHellhoundSpawned) {
                    hit.setDamage(0);
                    player.message("Vet'ion is immune until the hellhound spawns are killed off.");
                }
            }
        }

        if (attacker.isPlayer() && !hit.reflected && hit.getCombatType() != null) {
            Player playerAttacker = (Player) attacker;

            // Reward the player experience for this attack (as long as it's not a combat dummy)..
            if (!target.isNpc() || !target.getAsNpc().isCombatDummy()) {
                // while damage can appear visually higher than opponents HP, you cannot get that EXP (stops boosters)
                addCombatXp(playerAttacker, target, Math.min(hit.getDamage(), target.hp()), hit.getCombatType(), playerAttacker.getCombat().getFightType().getStyle());
            }

            playerAttacker.getPacketSender().sendHitPredictor(hit.getDamage());
        }

        // If target is teleporting or needs placement
        // Dont continue to add the hit.
        if (target.isNullifyDamageLock() || target.isNeedsPlacement()) {
            return;
        }

        if (attacker.isPlayer() && target.isNpc()) {
            CombatMethod method = CombatFactory.getMethod(target);
            if (method instanceof CommonCombatMethod o) {
                o.preDefend(hit);
            }
        }

        if (hit.pidded && hit.getDelay() <= 0) {
            executeHit(hit);
            hit.getTarget().decrementHealth(hit);
            hit.playerSync();
        } else {
            target.getCombat().getHitQueue().add(hit);
        }
    }

    /**
     * Executes a hit that has been ticking until now.
     *
     * @param hit The QueueableHit to execute.
     */
    public static void executeHit(Hit hit) {
        final Mob attacker = hit.getAttacker();
        final Mob target = hit.getTarget();
        final CombatType combatType = hit.getCombatType();
        int damage = hit.getDamage();

        if (attacker instanceof Player player) {
            player.sigil.process(player, target);
        }

        if (target.isNpc()) {
            Npc npc = (Npc) target;

            if (npc.combatInfo() == null) {
                return;
            }
        }

        if (target.dead())
            return;

        // If target/attacker is dead, don't continue.
        // hits with no type and method are probably recoil, retribution, wrath, which can apply when the source is of course death
        if (attacker.dead() && combatType != null) {
            return;
        }

        // If target is teleporting or needs placement
        // Don't continue to add the hit.
        if (target.isNullifyDamageLock() || target.isNeedsPlacement()) {
            return;
        }

        // Before target takes damage, manipulate the hit to handle
        // last-second effects
        hit = target.manipulateHit(hit);

        // Do block animation
        target.action.reset();
        if (attacker.isNpc() && hit.getCombatType() == CombatType.MAGIC && !target.getUpdateFlag().flagged(Flag.ANIMATION)) {
            target.animate(new Animation(target.getBlockAnim()));
        }

        // no need to process anything more
        if (hit.splatType == SplatType.NPC_HEALING_HITSPLAT) {
            hit.getTarget().heal(damage, attacker.getMobName().equalsIgnoreCase("Xarpus") ? 1000 : 0);
            return;
        }
        if (attacker.isNpc()) {
            Npc npc = attacker.getAsNpc();
            if (npc.getCombatMethod() != null && npc.getCombatMethod() instanceof CommonCombatMethod commonCombatMethod) {
                commonCombatMethod.onHit(npc, target, hit);
            }
        }
        if (target.isNpc() && attacker.isPlayer()) {
            Player player = attacker.getAsPlayer();
            Npc npcTarget = target.getAsNpc();
            if (player.getRaids() != null) {
                if (player.getRaids().raiding(player)) {
                    player.getRaids().addDamagePoints(player, npcTarget, hit.getDamage());
                }
            }
        }

        // Do other stuff for players..
        if (target.isPlayer()) {
            final Player targetAsPlayer = target.getAsPlayer();

            // Prayer effects
            if (hit.isAccurate()) {

                // full dh 25% chance to cause 15% of damage as additional hit
                if (Equipment.hasAmmyOfDamned(targetAsPlayer) && fullDharoks(targetAsPlayer) && Utils.rollDie(100, 25)) {
                    targetAsPlayer.hit(attacker, (int) (damage * 0.15));
                }

                handlePrayerEffects(attacker, target, damage, hit.getCombatType());
            }
        }

        if (attacker instanceof Player player) player.sigil.processDamage(player, hit);

        if (hit.postDamage != null)
            hit.postDamage.accept(hit);

        if (target instanceof Player toHeal) {
            if (Prayers.usingPrayer(toHeal, Prayers.VINDICATION)) {
                if (hit.isAccurate() && hit.getDamage() > 0) {
                    int heal = (int) (hit.getDamage() * 0.25D);
                    int healAmount = Math.round(heal);
                    if (healAmount <= 0) healAmount = 1;
                    toHeal.healHit(toHeal, healAmount, 0);
                }
            }
        }

        // Check for poisonous weapons..
        // And do other effects, such as barrows effects..
        if (attacker.isPlayer()) {

            Player attackerAsPlayer = attacker.getAsPlayer();

            if (attackerAsPlayer.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND_RAIDS) && target.isNpc() && target.getAsNpc().id() != LORD_VOLDEMORT) {
                attackerAsPlayer.message(Color.RED.wrap("Your elder wand crumbles to dust."));
                attackerAsPlayer.getEquipment().remove(new Item(ELDER_WAND_RAIDS), true);
                Autocasting.setAutocast(attackerAsPlayer, null); // Set auto-cast to default; 0
                WeaponInterfaces.updateWeaponInterface(attackerAsPlayer); //Update the weapon interface
            }

            if (attackerAsPlayer.getEquipment().wearingBeginnerWeapon()) {
                var hitsLeft = attackerAsPlayer.<Integer>getAttribOr(STARTER_WEAPON_DAMAGE, 500) - 1;
                attackerAsPlayer.putAttrib(STARTER_WEAPON_DAMAGE, hitsLeft);

                Item weapon = attackerAsPlayer.getEquipment().getWeapon();
                if (weapon != null && hitsLeft == 100)
                    attackerAsPlayer.message(Color.RED.wrap("You have 100 hits left on your " + weapon.name() + "."));
                if (weapon != null && hitsLeft == 50)
                    attackerAsPlayer.message(Color.RED.wrap("You have 50 hits left on your " + weapon.name() + "."));
                if (weapon != null && hitsLeft == 25)
                    attackerAsPlayer.message(Color.RED.wrap("You have 25 hits left on your " + weapon.name() + "."));

                if (weapon != null && hitsLeft <= 0) {
                    attackerAsPlayer.message(Color.RED.wrap("Your " + weapon.name() + " crumbles to dust."));
                    attackerAsPlayer.getEquipment().remove(weapon, EquipSlot.WEAPON, true);

                    //Always reset ranged weapon when unequipping weapon
                    if (attackerAsPlayer.getCombat().getRangedWeapon() != null) {
                        attackerAsPlayer.getCombat().setRangedWeapon(null);
                    }
                }
            }

            if (attacker.isPlayer()) {
                Player player = (Player) attacker;
                if (hit.isAccurate() && combatType == CombatType.MELEE) {
                    if (player.getEquipment().hasAt(EquipSlot.AMULET, AMULET_OF_BLOOD_FURY)) {
                        if (Utils.securedRandomChance(0.20F)) {
                            int healAmount = damage * 30 / 100;
                            player.heal(healAmount);
                            player.graphic(1542);
                        }
                    }
                }
            }

            if (target.isNpc()) {
                Npc npc = target.getAsNpc();

                npc.onHit(hit);
                if (npc.def().name != null) {
                    if (npc.def().name.equalsIgnoreCase("turoth") || npc.def().name.equalsIgnoreCase("kurask")) {
                        boolean block = false;
                        boolean leafbladedWeapon = attackerAsPlayer.getEquipment().contains(ItemIdentifiers.LEAFBLADED_SWORD) || attackerAsPlayer.getEquipment().contains(ItemIdentifiers.LEAFBLADED_BATTLEAXE) || attackerAsPlayer.getEquipment().contains(ItemIdentifiers.LEAFBLADED_SPEAR);
                        boolean leafbladedAmmo = attackerAsPlayer.getEquipment().getId(EquipSlot.AMMO) == BROAD_BOLTS || attackerAsPlayer.getEquipment().getId(EquipSlot.AMMO) == AMETHYST_BROAD_BOLTS || attackerAsPlayer.getEquipment().getId(EquipSlot.AMMO) == BROAD_ARROWS_4160;
                        boolean magicDart = attackerAsPlayer.getCombat().getCastSpell() != null &&
                            attackerAsPlayer.getCombat().getCastSpell().spellId() == CombatSpells.MAGIC_DART.getSpell().spellId();
                        if (hit.getCombatType() == CombatType.MELEE && !leafbladedWeapon) {
                            block = true;
                        } else if (hit.getCombatType() == CombatType.RANGED && !leafbladedAmmo) {
                            block = true;
                        } else if (hit.getCombatType() == CombatType.MAGIC && !magicDart) {
                            block = true;
                        }

                        if (block) {
                            hit.setDamage(0);
                            attackerAsPlayer.message("This monster is only vulnerable to leaf-bladed melee weapons and broad ammunition.");
                        }
                    }
                }

                //Dustdevil
                if (npc.id() == NpcIdentifiers.DUST_DEVIL || npc.id() == NpcIdentifiers.DUST_DEVIL_7249 || npc.id() == NpcIdentifiers.CHOKE_DEVIL) {
                    if (attackerAsPlayer.getEquipment().getId(EquipSlot.HEAD) != FACEMASK && !attackerAsPlayer.getEquipment().wearingSlayerHelm()) {
                        hit.setDamage(0);
                        attackerAsPlayer.message("Blinded by the monster's dust, you miss your attack!");
                    }
                }

                // Gargs
                if (npc.hp() - damage <= 0) {
                    if (npc.id() == 412) {
                        damage = npc.hp();
                        hit.setDamage(npc.hp());

                        boolean isGmaul = Combat.gmauls.stream().anyMatch(granite_maul -> attackerAsPlayer.getEquipment().hasAt(EquipSlot.WEAPON, granite_maul));
                        if (attackerAsPlayer.inventory().contains(ROCK_HAMMER) || isGmaul) {
                            Gargoyle.smash(attackerAsPlayer, npc, false);
                        } else {
                            attackerAsPlayer.message("Gargoyles can only be killed using a Rockhammer.");
                            hit.setDamage(npc.hp() == 1 ? 0 : npc.hp() - 1);
                        }
                    }
                }

                // Desert lizards
                if (npc.hp() - damage <= 0) {
                    if (npc.id() == NpcIdentifiers.DESERT_LIZARD || npc.id() == NpcIdentifiers.DESERT_LIZARD_460 || npc.id() == NpcIdentifiers.DESERT_LIZARD_461) {
                        damage = npc.hp();
                        hit.setDamage(npc.hp());
                        DesertLizards.iceCooler(attackerAsPlayer, npc, false);
                    }
                }

                // rockslugs
                if (npc.hp() - damage <= 0) {
                    if (npc.id() == NpcIdentifiers.ROCKSLUG || npc.id() == NpcIdentifiers.ROCKSLUG_422) {
                        damage = npc.hp();
                        hit.setDamage(npc.hp());
                        if (attackerAsPlayer.inventory().contains(BAG_OF_SALT, 1) || attackerAsPlayer.getEquipment().contains(BRINE_SABRE)) {
                            if (!attackerAsPlayer.getEquipment().contains(BRINE_SABRE)) {
                                attackerAsPlayer.animate(1574);
                                npc.graphic(327);
                            }
                        } else {
                            attackerAsPlayer.message("Rockslugs can only be killed using a bag of salt.");
                            hit.setDamage(npc.hp() == 1 ? 0 : npc.hp() - 1);
                        }
                    }
                }
            }

            var weapon = attackerAsPlayer.getEquipment().get(EquipSlot.WEAPON);
            if (weapon != null && hit.getCombatType() == CombatType.MELEE && !hit.reflected) {

                // Abyssal tent and dds have 25% chance to poison. Poison strength varies.
                if (weapon.getId() == 12006 && World.getWorld().rollDie(100, 25)) {
                    target.poison(4);
                } else if ((weapon.getId() == 5698 || weapon.getId() == 13271) && World.getWorld().rollDie(100, 10)) {
                    target.poison(6);
                }
            }
            boolean venom = Venom.attempt(attacker, target, hit.getCombatType(), hit.isAccurate());
            if (venom)
                target.venom(attacker);
            // Handle barrows effects if damage is more than zero.
            if (hit.getDamage() > 0 && !hit.reflected) {
                if (Utils.getRandom(10) >= 8) {

                    // Apply Guthan's effect..
                    if (fullGuthans(attackerAsPlayer)) {
                        handleGuthans(attackerAsPlayer, target, hit.getDamage());
                    }

                    // Other barrows effects here..
                }
            }
        } else if (attacker.isNpc()) {
            Npc npc = attacker.getAsNpc();
            // Poison?
            if (hit.getDamage() > 0 && (npc.combatInfo() != null && npc.combatInfo().poisonous())) {
                var chance = npc.combatInfo().poisonchance;
                if (chance >= 100 || World.getWorld().rollDie(100, npc.combatInfo().poisonchance)) {
                    target.poison(npc.combatInfo().poison);
                }
            }
        }

        // Handle ring of recoil for target
        // Also handle vengeance for target
        if (hit.getDamage() > 0) {
            if (target.isPlayer()) {
                Player player = target.getAsPlayer();

                if (player.getEquipment().hasAt(EquipSlot.WEAPON, FROZEN_ABYSSAL_WHIP)) {
                    //20% chance to freeze your target.
                    if (Utils.rollDie(5, 1)) {
                        player.freeze(5, target);
                    }
                } else if (attacker.isPlayer() && attacker.getAsPlayer().getEquipment().hasAt(EquipSlot.WEAPON, VOLCANIC_ABYSSAL_WHIP)) {
                    //20% chance to set your target on fire.
                    if (Utils.rollDie(5, 1)) {
                        target.hit(player, Utils.random(1, 5));
                        if (target instanceof Player) {
                            Player ptarg = (Player) target;
                            ptarg.animate(3170);
                            ptarg.message("You feel a hot blaze caused by the lava whip.");
                        }
                    }
                }

                //We don't have to check if ring is null here we already check that in the main method.
                if (!hit.reflected)
                    handleRecoil(player, attacker, hit.getDamage());
            }

            boolean hasVengeance = target.getAttribOr(AttributeKey.VENGEANCE_ACTIVE, false);

            if (hasVengeance && !hit.reflected) {
                handleVengeance(target, attacker, hit.getDamage());
            }

            //Set the dmg at the bottom after are effects have been calculated for.
            if (attacker.isPlayer()) {
                Player player = attacker.getAsPlayer();
                if (hit.getCombatType() == CombatType.MELEE) {
                    player.putAttrib(AttributeKey.MELEE_DAMAGE, player.<Integer>getAttribOr(AttributeKey.MELEE_DAMAGE, 0) + damage);
                    player.putAttrib(AttributeKey.LAST_ATTACK_WAS_MELEE, true);
                    player.clearAttrib(AttributeKey.LAST_ATTACK_WAS_RANGED);
                    player.clearAttrib(AttributeKey.LAST_ATTACK_WAS_MAGIC);
                } else if (hit.getCombatType() == CombatType.RANGED) {
                    player.putAttrib(AttributeKey.RANGED_DAMAGE, player.<Integer>getAttribOr(AttributeKey.RANGED_DAMAGE, 0) + damage);
                    player.putAttrib(AttributeKey.LAST_ATTACK_WAS_RANGED, true);
                    player.clearAttrib(AttributeKey.LAST_ATTACK_WAS_MELEE);
                    player.clearAttrib(AttributeKey.LAST_ATTACK_WAS_MAGIC);
                } else if (hit.getCombatType() == CombatType.MAGIC) {
                    player.putAttrib(AttributeKey.MAGIC_DAMAGE, player.<Integer>getAttribOr(AttributeKey.MAGIC_DAMAGE, 0) + damage);
                    player.putAttrib(AttributeKey.LAST_ATTACK_WAS_MAGIC, true);
                    player.clearAttrib(AttributeKey.LAST_ATTACK_WAS_MELEE);
                    player.clearAttrib(AttributeKey.LAST_ATTACK_WAS_RANGED);
                }
            }
        }

        // Auto-retaliate
        if (!CombatFactory.isAttacking(target) && !hit.reflected) { // is mob fighting someone?
            if (attacker.isPlayer()) {
                Player player = ((Player) attacker);
                //if (player.getCombat().autoRetaliate()) {
                // Players only auto retal the attacker when out of combat.
                boolean mayAttack = true;

                // Check attackability
                if (!canAttack(attacker, getMethod(attacker), target)) {
                    mayAttack = false;
                    attacker.getCombat().reset();//Can't attack, reset combat
                }

                if (mayAttack) {
                    target.autoRetaliate(attacker);
                }
                //}
            } else {
                Npc npc = (Npc) attacker;

                if (!npc.isCombatDummy())
                    target.autoRetaliate(attacker);
            }
        } else if (target.isNpc()) {
            // Npcs do switch aggro context if they get attacked.
            if (!target.getAsNpc().isCombatDummy())
                target.autoRetaliate(attacker);
        }

        if (!attacker.getCombat().getFightTimer().isRunning()) {
            attacker.getCombat().getFightTimer().start();
        }

        //PvM protection prayers
        if (damage > 0) {
            if (target.isPlayer() && attacker.isNpc()) {
                // 'bots' are npcs but are fake players so use prayer ratio
                if (attacker.getAsNpc().getBotHandler() != null) {
                    if (Prayers.usingPrayer(target, Prayers.getProtectingPrayer(hit.getCombatType()))) {
                        hit.setDamage((int) (damage * 0.6));
                    }
                } else {
                    Npc npc = attacker.getAsNpc();
                    if (hit.getCombatType() == CombatType.MELEE && Prayers.usingPrayer(target, DAMPED_MELEE)) {
                        hit.setDamage(0);
                    } else if (hit.getCombatType() == CombatType.RANGED && Prayers.usingPrayer(target, DAMPEN_RANGE)) {
                        hit.setDamage(0);
                    } else if (hit.getCombatType() == CombatType.MAGIC && Prayers.usingPrayer(target, DAMPEN_MAGIC)) {
                        hit.setDamage(0);
                    } else if (hit.getCombatType() == CombatType.MELEE && Prayers.usingPrayer(target, PROTECT_FROM_MELEE)) {
                        if (npc.id() == VANGUARD_7527 || npc.id() == TEKTON_ENRAGED_7544 || npc.id() == 7858) {
                            hit.setDamage((int) (damage * 0.6));
                        } else {
                            hit.setDamage(0);
                        }
                    } else if (hit.getCombatType() == CombatType.RANGED && Prayers.usingPrayer(target, PROTECT_FROM_MISSILES)) {
                        if (npc.id() == VANGUARD_7528 || npc.id() == VESPULA || npc.id() == VESPULA_7532) {
                            hit.setDamage((int) (damage * 0.6));
                        }
                        if (npc.id() == 7858) {
                            hit.setDamage((int) (damage * 0.2));//20% of damage
                        } else {
                            hit.setDamage(0);
                        }
                    } else if (hit.getCombatType() == CombatType.MAGIC && Prayers.usingPrayer(target, PROTECT_FROM_MAGIC)) {
                        if (npc.id() == LORD_VOLDEMORT || npc.id() == VANGUARD_7529) {
                            hit.setDamage((int) (damage * 0.6));
                        } else {
                            hit.setDamage(0);
                        }
                    }
                }
            }
        }

        // Add damage to target damage map
        target.getCombat().addDamage(attacker, hit.getDamage());

        //Store the latest damage being dealt to a player.
        if (target.isPlayer()) {
            attacker.putAttrib(AttributeKey.LATEST_DAMAGE, hit.getDamage());
        }

        CombatMethod method = CombatFactory.getMethod(target);

        if (method instanceof CommonCombatMethod o) {
            o.postDamage(hit);
        }

        //Send the hit sound
        attacker.takehitSound(hit);
    }

    private static void addCombatXp(Player player, Mob target, int hitDamage, CombatType style, FightStyle mode) {
        var hit = hitDamage;
        // Rather than putting hooks into every style of attacking a target, centralize it and stop giving XP
        // when certain styles can't damage an opponent.
        if (target.isNpc()) {
            var ntarg = target.getAsNpc();
            var id = ntarg.id();
            if (id == 5534) hit = 0; // Can't damage tentacles much
            if (id == 496) {
                if (ntarg.transmog() != 494) // Whirlpool as kraken
                    hit = 0;
                else if (style != CombatType.MAGIC)
                    hit = 0;
            }
            if (id == 319) { // Corp beast

            }
            var oppNpc = target.getAsNpc();
            var attacker = player;
            if (oppNpc.id() == NpcIdentifiers.DUST_DEVIL || oppNpc.id() == NpcIdentifiers.DUST_DEVIL_7249 || oppNpc.id() == NpcIdentifiers.CHOKE_DEVIL) {
                if (attacker != null && (attacker.getAsPlayer().getEquipment().getId(EquipSlot.HEAD) != FACEMASK && !attacker.getAsPlayer().getEquipment().wearingSlayerHelm())) {
                    hit = 0;
                }
            }
        }

        if (style == null) return;
        switch (style) {
            case MELEE -> {
                switch (mode) {
                    case ACCURATE -> {
                        player.skills().addXp(Skills.HITPOINTS, (hit + (hit / 3.0)), !target.isPlayer());
                        player.skills().addXp(Skills.ATTACK, (hit * 4.0), !target.isPlayer());
                    }

                    case AGGRESSIVE -> {
                        player.skills().addXp(Skills.HITPOINTS, (hit + (hit / 3.0)), !target.isPlayer());
                        player.skills().addXp(Skills.STRENGTH, (hit * 4.0), !target.isPlayer());
                    }

                    case DEFENSIVE -> {
                        player.skills().addXp(Skills.HITPOINTS, (hit + (hit / 3.0)), !target.isPlayer());
                        player.skills().addXp(Skills.DEFENCE, (hit * 4.0), !target.isPlayer());
                    }

                    case CONTROLLED -> {
                        var xp = (hit * 4.0);
                        player.skills().addXp(Skills.HITPOINTS, (hit + (hit / 3.0)), !target.isPlayer());
                        player.skills().addXp(Skills.ATTACK, xp / 3.0, !target.isPlayer());
                        player.skills().addXp(Skills.STRENGTH, xp / 3.0, !target.isPlayer());
                        player.skills().addXp(Skills.DEFENCE, xp / 3.0, !target.isPlayer());
                    }
                }
            }

            case RANGED -> {
                switch (mode) {
                    case ACCURATE, AGGRESSIVE -> {
                        player.skills().addXp(Skills.HITPOINTS, (hit + (hit / 3.0)), !target.isPlayer());
                        player.skills().addXp(Skills.RANGED, (hit * 4.0), !target.isPlayer());
                    }

                    case DEFENSIVE -> {
                        player.skills().addXp(Skills.HITPOINTS, (hit + (hit / 3.0)), !target.isPlayer());
                        player.skills().addXp(Skills.RANGED, (hit * 2.0), !target.isPlayer());
                        player.skills().addXp(Skills.DEFENCE, (hit * 2.0), !target.isPlayer());
                    }
                }
            }

            case MAGIC -> {
                CombatSpell spell = player.getCombat().getCastSpell() != null ? player.getCombat().getCastSpell() : player.getCombat().getAutoCastSpell();
                if (spell != null) {
                    if (hit > 0) {
                        // Accurate? Or normal autocast? aka non defensive
                        if (!player.<Boolean>getAttribOr(AttributeKey.DEFENSIVE_AUTOCAST, false)) {
                            player.skills().addXp(Skills.HITPOINTS, (hit + (hit / 3.0)), !target.isPlayer());
                            player.skills().addXp(Skills.MAGIC, (hit * 2.0 + spell.baseExperience()), !target.isPlayer());
                        } else {
                            // Defensive autocast...
                            player.skills().addXp(Skills.HITPOINTS, (hit + (hit / 3.0)), !target.isPlayer());
                            player.skills().addXp(Skills.MAGIC, (hit + spell.baseExperience() + (hit / 3.0)), !target.isPlayer());
                            player.skills().addXp(Skills.DEFENCE, hit + spell.baseExperience(), !target.isPlayer());
                        }
                    } else {
                        //Splash should only give 52 exp..
                        player.skills().addXp(Skills.MAGIC, 52);
                    }
                }
            }
        }
    }

    /**
     * Checks if a mob is currently attacking.
     *
     * @param mob The mob to check for.
     * @return true if mob is attacking, false otherwise.
     */
    public static boolean isAttacking(Mob mob) {
        return mob.getCombat().getTarget() != null;
    }

    /**
     * Finds the last person that attacked the given Entity
     */
    public static Mob lastAttacker(Mob entity) {
        return entity.getAttribOr(AttributeKey.LAST_DAMAGER, null);
    }

    /**
     * Checks if a mob is currently in combat.
     *
     * @param mob The mob to check for.
     * @return true if mob is in combat, false otherwise.
     */
    public static boolean inCombat(Mob mob) {
        var target = ((WeakReference<Mob>) mob.getAttribOr(AttributeKey.TARGET, new WeakReference<>(null))).get();
        var lastAttacked = System.currentTimeMillis() - mob.<Long>getAttribOr(AttributeKey.LAST_WAS_ATTACKED_TIME, 0L);
        var lastAttack = System.currentTimeMillis() - mob.<Long>getAttribOr(AttributeKey.LAST_ATTACK_TIME, 0L);
        return (mob.getTimerRepository().has(TimerKey.COMBAT_LOGOUT) || lastAttack < 10000L || lastAttacked < 10000L) && target != null && mob != target;
    }

    public static boolean wasRecentlyAttacked(Mob mob) {
        var lastAttacked = System.currentTimeMillis() - mob.<Long>getAttribOr(AttributeKey.LAST_WAS_ATTACKED_TIME, 0L);
        return lastAttacked < 10000L;
    }

    /**
     * Disables protection prayers for a player.
     *
     * @param player The player to disable protection prayers for.
     */
    public static void disableProtectionPrayers(Player player) {
        player.getTimerRepository().register(TimerKey.OVERHEADS_BLOCKED, 9);
        Prayers.deactivatePrayer(player, PROTECT_FROM_MAGIC);
        Prayers.deactivatePrayer(player, PROTECT_FROM_MISSILES);
        Prayers.deactivatePrayer(player, PROTECT_FROM_MELEE);
        Prayers.deactivatePrayer(player, RETRIBUTION);
        Prayers.deactivatePrayer(player, REDEMPTION);
        player.message("You have been disabled and can no longer use protection prayers.");
    }

    /**
     * Handles the item "Ring of Recoil" for a player. The item returns damage to
     * the attacker.
     *
     * @param player
     * @param attacker
     * @param damage
     */
    public static void handleRecoil(Player player, Mob attacker, int damage) {
        if (player == attacker) // dont recoil self-caused damage (rockcake)
            return;

        if (damage <= 1)
            return;

        if (Prayers.usingPrayer(player, REBUKE)) {
            damage *= 1.50D;
        }

        if (player.hasPetOut("Jal-Nib-Rek pet")) {
            attacker.hit(player, damage > 10 ? (damage / 10) : 1, 1, null).setIsReflected().submit();
            return;
        }

        if (player.getEquipment().hasAt(EquipSlot.RING, RING_OF_MANHUNTING)) {
            attacker.hit(player, damage > 10 ? (damage / 10) : 1, 1, null).setIsReflected().submit();
            return;
        }

        if (player.getEquipment().hasAt(EquipSlot.RING, RING_OF_SORCERY)) {
            attacker.hit(player, damage > 10 ? (damage / 10) : 1, 1, null).setIsReflected().submit();
            return;
        }

        if (player.getEquipment().hasAt(EquipSlot.RING, RING_OF_PRECISION)) {
            attacker.hit(player, damage > 10 ? (damage / 10) : 1, 1, null).setIsReflected().submit();
            return;
        }

        if (player.getEquipment().hasAt(EquipSlot.RING, RING_OF_TRINITY)) {
            attacker.hit(player, damage > 10 ? (damage / 10) : 1, 1, null).setIsReflected().submit();
            return;
        }

        if (player.getEquipment().hasAt(EquipSlot.RING, RING_OF_SUFFERING)) {
            attacker.hit(player, damage > 10 ? (damage / 10) : 1, 1, null).setIsReflected().submit();
        }

        if (player.getEquipment().hasAt(EquipSlot.RING, RING_OF_SUFFERING_I)) {
            attacker.hit(player, damage > 10 ? (damage / 10) : 1, 1, null).setIsReflected().submit();
        }

        if (player.getEquipment().hasAt(EquipSlot.RING, 2550)) {
            int charges;
            charges = (int) player.getAttribOr(AttributeKey.RING_OF_RECOIL_CHARGES, 40) - 1;

            if (charges == 0) {
                player.putAttrib(AttributeKey.RING_OF_RECOIL_CHARGES, 40);
                player.getEquipment().remove(new Item(2550), true);
                player.message("<col=804080>Your ring of recoil has shattered!");
            } else {
                player.putAttrib(AttributeKey.RING_OF_RECOIL_CHARGES, charges);

                // hmm ok so this doesnt throw an exception because its adding a hit to
                // the Attacker, which is not the same Iterator i think
                attacker.hit(player, damage > 10 ? (damage / 10) : 1, 1, null).setIsReflected().submit();
                if (attacker.isNpc()) {
                    if (((Npc) attacker).id() == 319) {
                        //TODO update string corp beast lair, we don't have this string yet
                    }
                }
            }
        }
    }

    /**
     * Handles the spell "Vengeance" for a player. The spell returns damage to the
     * attacker.
     *
     * @param mob
     * @param attacker
     * @param damage
     */
    public static void handleVengeance(Mob mob, Mob attacker, int damage) {
        if (mob == attacker) // dont recoil self-caused damage (rockcake)
            return;
        if (damage <= 0)
            return;

        mob.clearAttrib(AttributeKey.VENGEANCE_ACTIVE);
        attacker.hit(mob, (int) (damage * 0.75), 1, null).setIsReflected().submit();
        mob.forceChat("Taste Vengeance!");
    }

    /**
     * Handles the Guthan's set effect for a player. Wearing full guthan's has a
     * small chance of healing the player.
     *
     * @param player
     * @param target
     * @param damage
     */
    public static void handleGuthans(Player player, Mob target, int damage) {
        // Ammy of damned allows healing 10HP above base HP level
        player.heal(damage, Equipment.hasAmmyOfDamned(player) ? 10 : 0);
        target.graphic(398, 92, 0);
    }

    public static void unfreezeWhenOutOfRange(Mob mob) {
        Mob freezer = mob.getAttribOr(AttributeKey.FROZEN_BY, null);

        if (freezer == null) {
            return;
        }
        //plateau 34
        if (freezer.tile().distance(mob.tile()) >= 15 || !freezer.isRegistered()) {//off screen or unregistered
            mob.getTimerRepository().cancel(TimerKey.FROZEN); //Remove frozen timer key
            mob.getTimerRepository().cancel(TimerKey.REFREEZE); //Remove frozen timer key
            if (mob.isPlayer()) {
                Player player = mob.getAsPlayer();
                player.getPacketSender().sendEffectTimer(0, EffectTimer.FREEZE);
            }
            //unfreeze as freezer is out of distance. Alt: If freezer logged off, we get unfrozen.
        }
    }

    /**
     * Handles various prayer effects for the attacker and victim.
     *
     * @param attacker the attacker's combat builder.
     * @param target   the attacker's combat container.
     * @param damage   the total amount of damage dealt.
     */
    protected static void handlePrayerEffects(Mob attacker, Mob target, int damage, CombatType combatType) {
        // note : code on death wont work here. victim.hp will be its value BEFORE damage is actually taken
        if (attacker == null || target == null)
            return;
        // Prayer effects can only be done with victims that are players.
        if (target.isPlayer() && damage > 0) {
            Player victim = (Player) target;

            // The redemption (HEALING) prayer effect.
            if (Prayers.usingPrayer(victim, Prayers.REDEMPTION)
                && victim.hp() <= (victim.skills().xpLevel(Skills.HITPOINTS) / 10)) {
                int amountToHeal = (int) (victim.skills().xpLevel(Skills.PRAYER) * .25);
                victim.performGraphic(new Graphic(436));
                victim.skills().setLevel(Skills.PRAYER, 0);
                victim.skills().setLevel(Skills.HITPOINTS, victim.hp() + amountToHeal);
                victim.message("You've run out of prayer points!");
                Prayers.closeAllPrayers(victim);
                return;
            }

            // These last prayers can only be done with player attackers.
            if (attacker.isPlayer()) {
                Player playerAttacker = (Player) attacker;

                if (Prayers.usingPrayer(playerAttacker, PURGE)) {
                    int reduction = (int) (damage * 0.35);
                    victim.skills().alterSkill(Skills.PRAYER, -reduction);
                }

                if (Prayers.usingPrayer(playerAttacker, Prayers.SMITE)) {
                    int removePoints = damage / 4;
                    boolean pet = playerAttacker.hasPetOut("Dementor pet");
                    if (pet) {
                        var boost = removePoints * 0.35;
                        removePoints += boost;
                    }
                    victim.skills().alterSkill(Skills.PRAYER, -removePoints);

                    int smiteDmg = (Integer) playerAttacker.getAttribOr(AttributeKey.SMITE_DAMAGE, 0) + removePoints;
                    playerAttacker.putAttrib(AttributeKey.SMITE_DAMAGE, smiteDmg);
                }
            }
        }
    }

    /**
     * Checks if a player has enough ammo to perform a ranged attack
     *
     * @param player The player to run the check for
     * @return True if player has ammo, false otherwise
     */
    public static boolean checkAmmo(Player player) {
        var weaponId = player.getEquipment().getId(EquipSlot.WEAPON);
        var wepName = weaponId == -1 ? "" : new Item(weaponId).definition(World.getWorld()).name.toLowerCase();
        var ammoId = player.getEquipment().getId(EquipSlot.AMMO);
        var ammoName = ammoId == -1 ? "" : new Item(ammoId).definition(World.getWorld()).name.toLowerCase();
        var crystalBow = (weaponId >= 4212 && weaponId <= 4223);
        var crawsBow = weaponId == CRAWS_BOW || weaponId == CRAWS_BOW_C || weaponId == BEGINNER_CRAWS_BOW || weaponId == HWEEN_CRAWS_BOW || weaponId == WEBWEAVER_BOW;
        var bowOfFaerdhinen = FormulaUtils.hasBowOfFaerdhenin(player);

        WeaponType weaponType = player.getCombat().getWeaponType();

        if (!bowOfFaerdhinen && !crawsBow && !crystalBow && ((weaponType == WeaponType.BOW || weaponType == WeaponType.CROSSBOW) && ammoName == "")) {
            player.message("There's no ammo left in your quiver.");
            return false;
        }
        if ((Equipment.darkbow(weaponId) || player.getEquipment().hasAt(EquipSlot.WEAPON, SANGUINE_TWISTED_BOW)) && ammoId != -1 && player.getEquipment().get(EquipSlot.AMMO).getAmount() < 2) {
            //d bow shoots 2 arrows. doubt accumulator affects ability to shoot
            player.message("You don't have enough ammo.");
            return false;
        }

        // Check if we use the right type of ammo first
        if (weaponType == WeaponType.CROSSBOW) {
            if (!correctBolts(weaponId, ammoId)) {// bolt ammo
                player.message("You can't use that ammo with your crossbow.");
                return false;
            }
        } else if (weaponId == 19478 || weaponId == 19481) { // Light & heavy ballista
            if (!(ammoId >= 825 && ammoId <= 830) && !(ammoId >= 19484 && ammoId <= 19490)) {
                player.message("You can't use that ammo with your Ballista.");
                return false;
            }
        }

        if (!bowOfFaerdhinen && !crawsBow && !crystalBow && (weaponType == WeaponType.BOW && !ammoName.contains(" arrow"))) {
            player.message("You can't use that ammo with your bow.");
            return false;
        }

        if (wepName.replace(" (i)", "").endsWith("bow") && !bowOfFaerdhinen && !crawsBow && !crystalBow && !wepName.startsWith("kari") && !wepName.contains("cross")) {
            // bolt ammo
            if (!correctArrows(weaponId, ammoId)) {
                var lit = (ammoName.contains("rack") || ammoName.contains("arrow")) && !ammoName.contains("arrows") ? "s" : "";
                player.message("You can't use " + ammoName + "" + lit + " with a " + wepName + ".");
                return false;
            }
        }
        return true;
    }

    public static boolean correctArrows(int bow1, int ammo) {
        var ok = false;
        for (BowReqs bow : BowReqs.values()) {
            if (bow1 == bow.bow) {
                for (int ammo2 : bow.ammo) {
                    if (ammo == ammo2) {
                        ok = true;
                    }
                }
            }
        }
        return ok;
    }

    public static boolean correctBolts(int bow1, int ammo) {
        var ok = false;
        for (CbowReqs bow : CbowReqs.values()) {
            if (bow1 == bow.bow) {
                for (int ammo2 : bow.ammo) {
                    if (ammo == ammo2) {
                        ok = true;
                    }
                }
            }
        }
        return ok;
    }

    /**
     * Decrements the amount ammo the {@link Player} currently has equipped.
     *
     * @param player the player to decrement ammo for.
     */
    public static void decrementAmmo(Player player) {

        // Get the ranged weapon data
        final RangedWeapon rangedWeapon = player.getCombat().getRangedWeapon();

        if (rangedWeapon == null)
            return;

        Mob target = player.getCombat().getTarget();

        boolean targ_is_dummy = false;
        if (target instanceof Npc) {
            Npc npc = (Npc) target;

            if (npc.isCombatDummy()) {
                targ_is_dummy = true;
            }
        }
        boolean blowpipe = rangedWeapon == RangedWeapon.TOXIC_BLOWPIPE;
        boolean cryBow = rangedWeapon == RangedWeapon.CRYSTAL_BOW;
        boolean bowOfFaerdhinen = FormulaUtils.hasBowOfFaerdhenin(player);
        boolean crawsBow = rangedWeapon == RangedWeapon.CRAWS_BOW;
        boolean chins = rangedWeapon == RangedWeapon.CHINCHOMPA;
        boolean axes = rangedWeapon.getType() == RangedWeaponType.THROWING_AXES;
        boolean darts = rangedWeapon.getType() == RangedWeaponType.DARTS;
        boolean knifes = rangedWeapon.getType() == RangedWeaponType.KNIVES;
        boolean ballista = rangedWeapon.getType() == RangedWeaponType.BALLISTA;

        if (!blowpipe && !cryBow && !bowOfFaerdhinen && !crawsBow && !targ_is_dummy) {

            // Determine which slot we are decrementing ammo from.
            int equipSlot = (chins || darts || knifes || axes) ? EquipSlot.WEAPON : EquipSlot.AMMO;

            Item ammo = player.getEquipment().get(equipSlot);

            // chins & javelins always lost. darts and ammo slot can be saved!
            boolean remove = (chins || ballista) || Equipment.notAvas(player);

            if (remove && ammo != null) {
                Item weapon = player.getEquipment().get(EquipSlot.WEAPON);
                int ammoToRemove = weapon != null && Equipment.darkbow(weapon.getId()) ? 2 : 1; // dark bow removes 2 arrows

                // |------save------|---break---|-drop-|
                // As per wiki, normal avas is 72% save, 20% break, leaving 8% to go to the floor.
                // New avas is 80% save, 20% break, 0% floor
                double saveChance = player.getEquipment().containsAny(21898, 22109) ? 0.8001 : Equipment.wearingAvasEffect(player) ? 0.7201 : 0.0;
                double roll = Math.random();

                if (saveChance == 0.0 || roll >= saveChance) {
                    ammo.decrementAmountBy(ammoToRemove);
                    if (ammo.getAmount() == 0) {
                        player.getEquipment().remove(ammo, equipSlot, true);
                        player.getCombat().setRangedWeapon(null);
                    }

                    boolean skipAmmo = player.getDueling().inDuel() || player.getDueling().endingDuel() || player.inActiveTournament() || ammo.getValue() <= 0;
                    // These items do not drop ammo to the floor if lost.
                    if (!chins && !ballista && ammo.getId() != 4740 && roll >= saveChance + 0.2) {
                        // 8% chance to drop if normal ava, otherwise will never drop (roll cannot be over 1.0)
                        if (player.tile().inArea(2269, 10023, 2302, 10046)) {
                            GroundItemHandler.createGroundItem(new GroundItem(new Item(ammo.getId(), 1), player.tile(), player));
                        } else {
                            if (!skipAmmo)
                                GroundItemHandler.createGroundItem(new GroundItem(new Item(ammo.getId(), 1), player.getCombat().getTarget().tile(), player));
                        }
                        if (weapon != null && Equipment.darkbow(weapon.getId())) {// support dropping 2nd arrow as well
                            if (player.tile().inArea(2269, 10023, 2302, 10046)) {
                                GroundItemHandler.createGroundItem(new GroundItem(new Item(ammo.getId(), 1), player.tile(), player));
                            } else {
                                //In these areas ammo is saved to return after the fight
                                if (!skipAmmo)
                                    GroundItemHandler.createGroundItem(new GroundItem(new Item(ammo.getId(), 1), player.getCombat().getTarget().tile(), player));
                            }
                        }
                    }
                }

                // Refresh the equipment interface.
                player.getEquipment().refresh();
            }
        }
    }

    /**
     * Determines if the entity is wearing full veracs.
     *
     * @param entity the entity to determine this for.
     * @return true if the player is wearing full veracs.
     */
    public static boolean fullVeracs(Mob entity) {
        return entity.isNpc() ? entity.getAsNpc().id() == NpcIdentifiers.VERAC_THE_DEFILED
            : entity.getAsPlayer().getEquipment().containsAll(4753, 4757, 4759, 4755);
    }

    /**
     * Determines if the entity is wearing full dharoks.
     *
     * @param entity the entity to determine this for.
     * @return true if the player is wearing full dharoks.
     */
    public static boolean fullDharoks(Mob entity) {
        return entity.isNpc() ? entity.getAsNpc().id() == NpcIdentifiers.DHAROK_THE_WRETCHED
            : entity.getAsPlayer().getEquipment().containsAll(4716, 4720, 4722, 4718);
    }

    /**
     * Determines if the entity is wearing full karils.
     *
     * @param entity the entity to determine this for.
     * @return true if the player is wearing full karils.
     */
    public static boolean fullKarils(Mob entity) {
        return entity.isNpc() ? entity.getAsNpc().id() == NpcIdentifiers.KARIL_THE_TAINTED
            : entity.getAsPlayer().getEquipment().containsAll(4732, 4736, 4738, 4734);
    }

    /**
     * Determines if the entity is wearing full ahrims.
     *
     * @param entity the entity to determine this for.
     * @return true if the player is wearing full ahrims.
     */
    public static boolean fullAhrims(Mob entity) {
        return entity.isNpc() ? entity.getAsNpc().id() == NpcIdentifiers.AHRIM_THE_BLIGHTED
            : entity.getAsPlayer().getEquipment().containsAll(4708, 4712, 4714, 4710);
    }

    /**
     * Determines if the entity is wearing full torags.
     *
     * @param entity the entity to determine this for.
     * @return true if the player is wearing full torags.
     */
    public static boolean fullTorags(Mob entity) {
        return entity.isNpc() ? entity.getAsNpc().def().name.equals("Torag the Corrupted")
            : entity.getAsPlayer().getEquipment().containsAll(4745, 4749, 4751, 4747);
    }

    /**
     * Determines if the entity is wearing full guthans.
     *
     * @param entity the entity to determine this for.
     * @return true if the player is wearing full guthans.
     */
    public static boolean fullGuthans(Mob entity) {
        return entity.isNpc() ? entity.getAsNpc().def().name.equals("Guthan the Infested")
            : entity.getAsPlayer().getEquipment().containsAll(4724, 4728, 4730, 4726);
    }

    public static int getLowestLevel(Mob entity, Mob target) {
        var combat = entity.isNpc() ? entity.getAsNpc().def().combatlevel : entity.skills().combatLevel();
        var wilderness = WildernessArea.wildernessLevel(entity.tile());
        var min = combat - wilderness;
        if (min < 3) {
            min = 3;
        }
        return min;
    }

    public static int getHighestLevel(Mob entity, Mob target) {
        var combat = entity.isNpc() ? entity.getAsNpc().def().combatlevel : entity.skills().combatLevel();
        var wilderness = WildernessArea.wildernessLevel(entity.tile());
        var max = combat + wilderness;
        if (max > 126) {
            max = 126;
        }
        return max;
    }

    public static Queue<Direction> pendingSpears(Mob p) {
        return p.<Queue<Direction>>getAttribOr(AttributeKey.SPEAR_MOVES, new LinkedList<Direction>());
    }

    /**
     * Records the game tick of when you attacked that target. Used for anti-rag mechanics.
     * Should always be called for any PvP attack - sits next to skulling code nicely.
     */
    public static void trackPvpAggression(Player player, Player target) {
        Map<Integer, Integer> historyMap = player.getAttribOr(AttributeKey.PVP_WILDY_AGGRESSION_TRACKER, new HashMap<Integer, Integer>());
        int targetEnterWildTick = target.getAttribOr(AttributeKey.INWILD, 0);
        int lastAgroToTarget = historyMap.getOrDefault(target.getIndex(), -1);
        // Either not tracked yet or they left the wildy and re-entered, so we'll start tracking this new battle from after they returned to the wild.
        if (lastAgroToTarget == -1 || (lastAgroToTarget != -1 && targetEnterWildTick > lastAgroToTarget)) {
            historyMap.put(target.getIndex(), World.getWorld().cycleCount());
            //player.debugMessage("Recorded battle start vs " + target.getUsername() + " on tick " + World.getWorld().getElapsedTicks() + ".");
        }
        player.putAttrib(AttributeKey.PVP_WILDY_AGGRESSION_TRACKER, historyMap);
    }

    public static boolean targetOk(Mob me, Mob target) {
        return targetOk(me, target, 16);
    }

    // While this returns true, the npc will continue aggressing a target.
    public static boolean targetOk(Mob me, Mob target, int maxdist) {
        var ok = target != null && !target.finished() && !me.finished() && !target.dead() && !me.dead() && me.hp() > 0 && (target.tile().distance(me.tile()) < maxdist && bothInFixedRoom(me, target));
        if (!ok) {
            me.stopActions(true);
        }
        return ok;
    }
}
