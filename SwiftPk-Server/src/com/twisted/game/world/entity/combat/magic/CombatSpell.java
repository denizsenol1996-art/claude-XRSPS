package com.twisted.game.world.entity.combat.magic;

import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.formula.accuracy.data.MagicAccuracy;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.combat.method.impl.MagicCombatMethod;
import com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.twisted.game.world.entity.combat.skull.SkullType;
import com.twisted.game.world.entity.combat.skull.Skulling;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.animations.Animation;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.MagicSpellbook;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import com.twisted.util.timers.TimerKey;

import java.util.Optional;

import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.CRUOURS_VOW;
import static com.twisted.game.world.entity.combat.prayer.default_prayer.Prayers.FUMUS_VOW;
import static com.twisted.util.CustomItemIdentifiers.GRANDMASTER_HELM;
import static com.twisted.util.CustomItemIdentifiers.HOLY_SANGUINESTI_STAFF;
import static com.twisted.util.ItemIdentifiers.*;

/**
 * A {@link Spell} implementation used for combat related spells.
 *
 * @author lare96
 */
public abstract class CombatSpell extends Spell {

    @Override
    public void startCast(Mob cast, Mob castOn) {
        // On 07, the player gets unfrozen when the freezer is at least X tiles away
        CombatFactory.unfreezeWhenOutOfRange(castOn);

        MagicAccuracy accuracy = new MagicAccuracy(cast, castOn, CombatType.MAGIC);
        double chance = World.getWorld().random().nextDouble();
        boolean success = accuracy.success(chance);

        int castAnimation = -1;

        Npc npc = cast.isNpc() ? ((Npc) cast) : null;

        if (castAnimation().isPresent()) {
            castAnimation().ifPresent(cast::animate);
        } else {
            cast.animate(new Animation(castAnimation));
        }

        // Then send the starting graphic.
        if (npc != null) {
            if (npc.id() != 2000 && npc.id() != 109 && npc.id() != 3580 && npc.id() != 2007) {
                startGraphic().ifPresent(cast::performGraphic);
            }
        } else {
            startGraphic().ifPresent(cast::performGraphic);
        }

        CombatSpell spell = cast.getCombat().getCastSpell() != null ? cast.getCombat().getCastSpell() : cast.getCombat().getAutoCastSpell();

        if (spell != null && spell.name().equalsIgnoreCase("Cruciatus Curse")) {
            cast.forceChat("Crucio!");
        } else if (spell != null && spell.name().equalsIgnoreCase("Petrificus Totalus")) {
            cast.forceChat("Petrificus Totalus!");
        } else if (spell != null && spell.name().equalsIgnoreCase("Avada Kedavra")) {
            cast.forceChat("Avada Kedavra!");
        } else if (spell != null && spell.name().equalsIgnoreCase("Expelliarmus")) {
            cast.forceChat("Expelliarmus!");
        } else if (spell != null && spell.name().equalsIgnoreCase("Sectumsempra")) {
            cast.forceChat("Sectumsempra!");
        }

        //Special spells

        if (spell != null) {
            if (spell.name().equalsIgnoreCase("Confuse")) {
                if (cast.isPlayer()) {
                    cast.graphic(102, 100, 0);
                    new Projectile(cast, castOn, 103, 0, 20, 43, 31, 0).sendProjectile();
                    var tileDist = cast.tile().getChevDistance(castOn.tile());
                    cast.skills().addXp(Skills.MAGIC, 13.0, castOn.isPlayer());
                    boolean isNpc = castOn.isNpc();
                    if (success) {
                        var level = 0;
                        if (isNpc) {
                            if (castOn.getAsNpc().combatInfo() != null && castOn.getAsNpc().combatInfo().stats != null) {
                                level = castOn.getAsNpc().combatInfo().stats.attack;
                            }
                        } else {
                            level = castOn.skills().level(Skills.ATTACK);
                        }

                        if (level < 1) {
                            cast.message("The spell has no effect because the player has already been weakened.");
                            return;
                        }

                        var delay = tileDist <= 2 ? 1 : tileDist <= 5 ? 2 : 3;
                        castOn.graphic(104, 100, delay * 30);//a tick in graphic format/maths is 30.

                        if (isNpc) {
                            int decrease = (int) (0.05 * level);
                            castOn.getAsNpc().combatInfo().stats.attack -= decrease;
                        } else {
                            int decrease = (int) (0.05 * (castOn.skills().level(Skills.ATTACK)));
                            castOn.skills().setLevel(Skills.ATTACK, castOn.skills().level(Skills.ATTACK) - decrease);
                            castOn.skills().update(Skills.ATTACK);
                            castOn.message("You feel slightly weakened.");
                        }
                    }
                }
                return;
            }

            if (spell.name().equalsIgnoreCase("Weaken")) {
                if (cast.isPlayer()) {
                    cast.graphic(102, 100, 0);
                    new Projectile(cast, castOn, 105, 0, 20, 43, 31, 0).sendProjectile();
                    var tileDist = cast.tile().getChevDistance(castOn.tile());
                    cast.skills().addXp(Skills.MAGIC, 21, castOn.isPlayer());
                    boolean isNpc = castOn.isNpc();
                    if (success) {
                        var level = 0;
                        if (isNpc) {
                            if (castOn.getAsNpc().combatInfo() != null && castOn.getAsNpc().combatInfo().stats != null) {
                                level = castOn.getAsNpc().combatInfo().stats.strength;
                            }
                        } else {
                            level = castOn.skills().level(Skills.STRENGTH);
                        }

                        if (level < 1) {
                            cast.message("The spell has no effect because the player has already been weakened.");
                            return;
                        }

                        var delay = tileDist <= 2 ? 1 : tileDist <= 5 ? 2 : 3;
                        castOn.graphic(107, 100, delay * 30);//a tick in graphic format/maths is 30.

                        if (isNpc) {
                            int decrease = (int) (0.05 * level);
                            castOn.getAsNpc().combatInfo().stats.strength -= decrease;
                        } else {
                            int decrease = (int) (0.05 * (castOn.skills().level(Skills.STRENGTH)));
                            castOn.skills().setLevel(Skills.STRENGTH, castOn.skills().level(Skills.STRENGTH) - decrease);
                            castOn.skills().update(Skills.STRENGTH);
                            castOn.message("You feel slightly weakened.");
                        }
                    }
                }
                return;
            }

            if (spell.name().equalsIgnoreCase("Curse")) {
                if (cast.isPlayer()) {
                    cast.graphic(102, 100, 0);
                    new Projectile(cast, castOn, 108, 0, 20, 43, 31, 0).sendProjectile();
                    var tileDist = cast.tile().getChevDistance(castOn.tile());
                    cast.skills().addXp(Skills.MAGIC, 29.0, castOn.isPlayer());
                    boolean isNpc = castOn.isNpc();
                    if (success) {
                        var level = 0;
                        if (isNpc) {
                            if (castOn.getAsNpc().combatInfo() != null && castOn.getAsNpc().combatInfo().stats != null) {
                                level = castOn.getAsNpc().combatInfo().stats.defence;
                            }
                        } else {
                            level = castOn.skills().level(Skills.DEFENCE);
                        }

                        if (level < 1) {
                            cast.message("The spell has no effect because the player has already been weakened.");
                            return;
                        }

                        var delay = tileDist <= 2 ? 1 : tileDist <= 5 ? 2 : 3;
                        castOn.graphic(110, 100, delay * 30);//a tick in graphic format/maths is 30.

                        if (isNpc) {
                            int decrease = (int) (0.05 * level);
                            castOn.getAsNpc().combatInfo().stats.defence -= decrease;
                        } else {
                            int decrease = (int) (0.05 * (castOn.skills().level(Skills.DEFENCE)));
                            castOn.skills().setLevel(Skills.DEFENCE, castOn.skills().level(Skills.DEFENCE) - decrease);
                            castOn.skills().update(Skills.DEFENCE);
                            castOn.message("You feel slightly weakened.");
                        }
                    }
                }
                return;
            }

            if (spell.name().equalsIgnoreCase("Vulnerability")) {
                if (cast.isPlayer()) {
                    cast.graphic(167, 100, 0);
                    new Projectile(cast, castOn, 168, 0, 20, 43, 31, 0).sendProjectile();
                    var tileDist = cast.tile().getChevDistance(castOn.tile());
                    cast.skills().addXp(Skills.MAGIC, 76.0, castOn.isPlayer());
                    boolean isNpc = castOn.isNpc();
                    if (success) {
                        var level = 0;
                        if (isNpc) {
                            if (castOn.getAsNpc().combatInfo() != null && castOn.getAsNpc().combatInfo().stats != null) {
                                level = castOn.getAsNpc().combatInfo().stats.defence;
                            }
                        } else {
                            level = castOn.skills().level(Skills.DEFENCE);
                        }

                        if (level < 1) {
                            cast.message("The spell has no effect because the player is already weakened.");
                            return;
                        }

                        var delay = tileDist <= 2 ? 1 : tileDist <= 5 ? 2 : 3;
                        castOn.graphic(169, 0, delay * 30);//a tick in graphic format/maths is 30.

                        if (isNpc) {
                            int decrease = (int) (0.10 * level);
                            castOn.getAsNpc().combatInfo().stats.defence -= decrease;
                        } else {
                            int decrease = (int) (0.10 * (castOn.skills().level(Skills.DEFENCE)));
                            castOn.skills().setLevel(Skills.DEFENCE, castOn.skills().level(Skills.DEFENCE) - decrease);
                            castOn.skills().update(Skills.DEFENCE);
                            castOn.message("You feel slightly weakened.");
                        }
                    }
                }
                return;
            }

            if (spell.name().equalsIgnoreCase("Enfeeble")) {
                if (cast.isPlayer()) {
                    cast.graphic(170, 100, 0);
                    new Projectile(cast, castOn, 171, 0, 20, 43, 31, 0).sendProjectile();
                    var tileDist = cast.tile().getChevDistance(castOn.tile());
                    cast.skills().addXp(Skills.MAGIC, 83, castOn.isPlayer());
                    boolean isNpc = castOn.isNpc();
                    if (success) {
                        var level = 0;
                        if (isNpc) {
                            if (castOn.getAsNpc().combatInfo() != null && castOn.getAsNpc().combatInfo().stats != null) {
                                level = castOn.getAsNpc().combatInfo().stats.strength;
                            }
                        } else {
                            level = castOn.skills().level(Skills.STRENGTH);
                        }

                        if (level < 1) {
                            cast.message("The spell has no effect because the player is already weakened.");
                            return;
                        }

                        var delay = tileDist <= 2 ? 1 : tileDist <= 5 ? 2 : 3;
                        castOn.graphic(172, 0, delay * 30);//a tick in graphic format/maths is 30.

                        if (isNpc) {
                            int decrease = (int) (0.10 * level);
                            castOn.getAsNpc().combatInfo().stats.strength -= decrease;
                        } else {
                            int decrease = (int) (0.10 * (castOn.skills().level(Skills.STRENGTH)));
                            castOn.skills().setLevel(Skills.STRENGTH, castOn.skills().level(Skills.STRENGTH) - decrease);
                            castOn.skills().update(Skills.STRENGTH);
                            castOn.message("You feel slightly weakened.");
                        }
                    }
                }
                return;
            }

            if (spell.name().equalsIgnoreCase("Stun")) {
                if (cast.isPlayer()) {
                    cast.graphic(173, 100, 0);
                    new Projectile(cast, castOn, 174, 0, 20, 43, 31, 0).sendProjectile();
                    var tileDist = cast.tile().getChevDistance(castOn.tile());
                    cast.skills().addXp(Skills.MAGIC, 90.0, castOn.isPlayer());
                    boolean isNpc = castOn.isNpc();
                    if (success) {
                        var level = 0;
                        if (isNpc) {
                            if (castOn.getAsNpc().combatInfo() != null && castOn.getAsNpc().combatInfo().stats != null) {
                                level = castOn.getAsNpc().combatInfo().stats.attack;
                            }
                        } else {
                            level = castOn.skills().level(Skills.ATTACK);
                        }

                        if (level < 1) {
                            cast.message("The spell has no effect because the player is already weakened.");
                            return;
                        }

                        var delay = tileDist <= 2 ? 1 : tileDist <= 5 ? 2 : 3;
                        castOn.graphic(107, 0, delay * 30);//a tick in graphic format/maths is 30.

                        if (isNpc) {
                            int decrease = (int) (0.10 * level);
                            castOn.getAsNpc().combatInfo().stats.attack -= decrease;
                        } else {
                            int decrease = (int) (0.10 * (castOn.skills().level(Skills.ATTACK)));
                            castOn.skills().setLevel(Skills.ATTACK, castOn.skills().level(Skills.ATTACK) - decrease);
                            castOn.skills().update(Skills.ATTACK);
                            castOn.message("You feel slightly weakened.");
                        }
                    }
                }
                return;
            }

            if (spell.name().equalsIgnoreCase("Teleblock")) {
                if (cast.isPlayer() && castOn.isPlayer()) {
                    Player player = (Player) cast;
                    Player target = (Player) castOn;
                    new Projectile(player, target, success ? 1299 : 1300, 41, player.projectileSpeed(target), 43, 31, 0).sendProjectile();
                    var tileDist = player.tile().getChevDistance(target.tile());
                    var tbTime = 495;
                    var tb_delay = tileDist <= 2 ? 1 : tileDist <= 5 ? 2 : 3;
                    if (success) {
                        target.getTimerRepository().extendOrRegister(TimerKey.COMBAT_LOGOUT, 20);
                        target.graphic(345, 0, tb_delay * 30);//a tick in graphic format/maths is 30.

                        var half = Prayers.usingPrayer(target, Prayers.PROTECT_FROM_MAGIC) || Prayers.usingPrayer(target, Prayers.DAMPEN_MAGIC);
                        if (half) {
                            tbTime = 248; //Half teleblock
                        }
                        // After investigating on RS - the teleblock lands instantly even from 11 tiles away.
                        target.teleblock(tbTime);

                        // Add base XP - 82xp if a half, 95 is full!
                        player.skills().addXp(Skills.MAGIC, half ? 82.0 : 95.0, target.isPlayer());
                        Skulling.skull(player, target, SkullType.WHITE_SKULL);
                        player.message(Color.PURPLE.wrap("The teleblock was successful!"));
                    }
                }
                return;
            }
        }

        // Finally send the projectile after two ticks.
        int castProjectileTicks = 2;
        int endGraphicDelay = castProjectileTicks * 30;
        Optional<Projectile> optP = castProjectile(cast, castOn, cast.tile().getChevDistance(castOn.tile()));
        if (optP.isPresent()) {
            Projectile p = optP.get();
            endGraphicDelay += p.getClientTicks();
            TaskManager.submit(new Task("CastProjectileTask", castProjectileTicks, cast, false) {
                @Override
                public void execute() {
                    p.sendProjectile();
                    this.stop();
                }
            });
        }

        Hit hit = castOn.hit(cast, CombatFactory.calcDamageFromType(cast, castOn, CombatType.MAGIC), cast.getCombat().magicSpellDelay(castOn), CombatType.MAGIC).checkAccuracy().spell(spell).postDamage(h -> ((MagicCombatMethod) CombatFactory.MAGIC_COMBAT).handleAfterHit(h));
        hit.submit();

        if (hit.isAccurate()) {
            Optional<Graphic> optEG = endGraphic();
            if (optEG.isPresent()) {
                Graphic endG = optEG.get();
                castOn.performGraphic(new Graphic(endG.id(), endG.height(), endG.delay() + endGraphicDelay));
            }

            if (cast instanceof Player player) {
                if (hit.getDamage() > 0) {
                    if (Prayers.usingPrayer(player, CRUOURS_VOW)) {
                        hit.postDamage(post -> player.healHit(player, 3, 0));
                    } else if (Prayers.usingPrayer(player, FUMUS_VOW)) {
                        if (castOn instanceof Player t) {
                            if (!t.getEquipment().containsAny(SERPENTINE_HELM, MAGMA_HELM, GRANDMASTER_HELM, 30326)) {
                                hit.postDamage(post -> new Hit(player, t, World.getWorld().random(1, 15), 0, SplatType.POISON_HITSPLAT, null).submit());
                            }
                        } else if (castOn instanceof Npc n_) {
                            if (!n_.isEventNpc()) {
                                hit.postDamage(post -> new Hit(player, castOn, World.getWorld().random(1, 15), 0, SplatType.POISON_HITSPLAT, null).submit());
                            }
                        }
                    }
                }
            }
        } else {
            //Unsuccessful hit. Send splash graphics for the spell because it wasn't accurate
            castOn.delayedGraphics(new Graphic(85, 90, 30), cast.getCombat().magicSpellDelay(castOn) - 1);
        }
        if (spell instanceof CombatEffectSpell) {
            //Effects only hit when we don't splash
            if (hit.isAccurate()) {
                CombatEffectSpell combatEffectSpell = (CombatEffectSpell) spell;
                combatEffectSpell.whenSpellCast(cast, castOn);
                // Do the spell effect here.
                combatEffectSpell.spellEffect(cast, castOn, hit);
            }
        }
    }

    public int getAttackSpeed(Mob attacker) {
        int speed = 5;
        if (attacker.isPlayer()) {
            Player player = (Player) attacker;

            if (player.getEquipment().hasAt(EquipSlot.WEAPON, HARMONISED_NIGHTMARE_STAFF) || player.getEquipment().hasAt(EquipSlot.WEAPON, TUMEKENS_SHADOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, TRIDENT_OF_THE_SWAMP) || player.getEquipment().hasAt(EquipSlot.WEAPON, TRIDENT_OF_THE_SEAS) || player.getEquipment().hasAt(EquipSlot.WEAPON, DAWNBRINGER) || player.getEquipment().hasAt(EquipSlot.WEAPON, SANGUINESTI_STAFF) || player.getEquipment().hasAt(EquipSlot.WEAPON, HOLY_SANGUINESTI_STAFF)) {
                speed = 4;
            }
        }
        //System.out.println("speed = "+speed+" ticks.");
        return speed;
    }

    public int[] elementalStaff() {
        int projectile, gfx_impact;
        final int roll = Utils.random(5);
        if (roll == 0 || roll == 1) {
            projectile = 159; // Air
            gfx_impact = 160;
        } else if (roll == 2 || roll == 3) {
            projectile = 162; // Water
            gfx_impact = 163;
        } else if (roll == 4) {
            projectile = 165; // Earth
            gfx_impact = 166;
        } else {
            projectile = 156; // Fire
            gfx_impact = 157;
        }
        return new int[]{projectile, gfx_impact};
    }

    public abstract String name();

    /**
     * The fixed ID of the spell implementation as recognized by the protocol.
     *
     * @return the ID of the spell, or <tt>-1</tt> if there is no ID for this
     * spell.
     */
    public abstract int spellId();

    /**
     * The maximum hit an {@link Mob} can deal with this spell.
     *
     * @return the maximum hit able to be dealt with this spell implementation.
     */
    public abstract int baseMaxHit();

    /**
     * The animation played when the spell is cast.
     *
     * @return the animation played when the spell is cast.
     */
    public abstract Optional<Animation> castAnimation();

    /**
     * The starting graphic played when the spell is cast.
     *
     * @return the starting graphic played when the spell is cast.
     */
    public abstract Optional<Graphic> startGraphic();

    /**
     * The projectile played when this spell is cast.
     *
     * @param cast   the entity casting the spell.
     * @param castOn the entity targeted by the spell.
     * @return the projectile played when this spell is cast.
     */
    public Optional<Projectile> castProjectile(Mob cast, Mob castOn, int dist) {
        return castProjectile(cast, castOn);
    }

    /**
     * The projectile played when this spell is cast.
     *
     * @param cast   the entity casting the spell.
     * @param castOn the entity targeted by the spell.
     * @return the projectile played when this spell is cast.
     */
    public Optional<Projectile> castProjectile(Mob cast, Mob castOn) {
        return castProjectile(cast, castOn, 3);
    }

    public Optional<Projectile> bind(Mob cast, Mob castOn, int dist, int graphicID) {
        int delayMod = -60;// because server is old-fashion has manual server 2 tick delay
        int realDelay = Math.max(0, 75 + delayMod);
        int speed = Math.max(0, delayMod + 56 + (dist * 10));
        return Optional.of(new Projectile(cast, castOn, graphicID,
            realDelay, speed, 45, 0, 16));
    }

    public Optional<Projectile> modern(Mob cast, Mob castOn, int dist, int graphicID) {
        int delay = 51;
        int delayMod = -60;// because server is old-fashion has manual server 2 tick delay
        int realDelay = Math.max(0, delay + delayMod);
        int speed = Math.max(0, delayMod + delay + 5 + (dist * 10));
        return Optional.of(new Projectile(cast, castOn, graphicID,
            realDelay, speed, 43, 31, 16));
    }

    public Optional<Projectile> ancient(Mob cast, Mob castOn, int dist, int graphicID) {
        int delay = 51;
        int delayMod = -60;// because server is old-fashion has manual server 2 tick delay
        int realDelay = Math.max(0, delay + delayMod);
        int speed = Math.max(0, delayMod + delay + 5 + (dist * 10));
        return Optional.of(new Projectile(cast, castOn, graphicID,
            realDelay, speed, 43, 0, 16));
    }

    /**
     * The ending graphic played when the spell hits the victim.
     *
     * @return the ending graphic played when the spell hits the victim.
     */
    public abstract Optional<Graphic> endGraphic();

    public abstract MagicSpellbook spellbook();
}
