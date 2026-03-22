package com.twisted.game.world.entity.combat.method.impl;

import com.twisted.game.content.duel.DuelRule;
import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.hit.Hit;
import com.twisted.game.world.entity.combat.magic.CombatSpell;
import com.twisted.game.world.entity.combat.method.CombatMethod;
import com.twisted.game.world.entity.dialogue.DialogueManager;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.npc.droptables.ItemDrops;
import com.twisted.game.world.entity.mob.player.EquipSlot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Tile;
import com.twisted.game.world.route.routes.DumbRoute;
import com.twisted.util.Debugs;
import com.twisted.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.List;

import static com.twisted.util.CustomItemIdentifiers.ELDER_WAND;
import static com.twisted.util.CustomItemIdentifiers.ELDER_WAND_RAIDS;
import static com.twisted.util.NpcIdentifiers.VESPULA;

/**
 * reduce code replication for the 90+ npc classes
 *
 * @author Jak Shadowrs tardisfan121@gmail.com
 */
public abstract class CommonCombatMethod implements CombatMethod {

    public Mob mob, target;

    public void set(Mob mob, Mob target) {
        this.mob = mob;
        this.target = target;
    }

    protected boolean withinDistance(int distance) {
        return DumbRoute.withinDistance(mob, target, distance);
    }

    /**
     * npc only
     */
    public void doFollowLogic() {
        DumbRoute.step(mob, target, getAttackDistance(mob));
    }

    public void onHit(Mob mob, Mob target, Hit hit) {

    }
    /**
     * npc only
     */
    public boolean inAttackRange() {
        boolean instance = mob.tile().getZ() > 4;
        if (mob.tile().distance(target.tile()) >= 16 && !instance) {
            mob.getCombat().reset();//Target out of distance reset combat
            return false;
        }
        return DumbRoute.withinDistance(mob, target, getAttackDistance(mob));
    }


    /**
     * npc only
     */
    public void preDefend(Hit hit) {

    }



    /**
     * npc only
     */
    public void onDeath(Player killer, Npc npc) {

    }

    /**
     * player only
     */
    public void postAttack() {

    }


    /**
     * npc only
     */
    public void postDamage(Hit hit) {

    }


    public boolean isCustomDropTable(Npc npc, ItemDrops drops) {
        return false;
    }

    public final void handleDodgableAttack(final Mob mob, final Mob target, final Projectile projectile, final Graphic graphic, final int damage, final int delay, Task onhit) {
        if (mob == null || target == null)
            return;

        final Tile hitLoc = target.tile().copy();
        if (target.isPlayer()) {
            projectile.sendProjectile();
        }

        Chain.bound(null).runFn(delay, () -> {
            if (target.tile().equals(hitLoc)) {
                target.hit(mob, damage);
                if (graphic != null)
                    target.graphic(graphic.id(), graphic.height(), 0);
            }
        });

        TaskManager.submit(onhit);
    }
    public boolean canAttackStyle(Mob entity, Mob other, CombatType type) {
        //Specific combat style checks
        if (entity.isPlayer()) {
            Player player = (Player) entity;
            boolean magicOnly = player.getAttribOr(AttributeKey.MAGEBANK_MAGIC_ONLY, false);
            CombatSpell spell = player.getCombat().getCastSpell() != null ? player.getCombat().getCastSpell() : player.getCombat().getAutoCastSpell();



            // If you're in the mage arena, where it is magic only.
            if (type != CombatType.MAGIC && magicOnly) {
                player.message("You can only use magic inside the arena!");
                player.getCombat().reset();
                return false;
            }

            if (type == CombatType.MAGIC) {
                if (spell != null && !spell.canCast(player, other, false)) {
                    player.getCombat().reset();//We can't cast this spell reset combat
                    player.getCombat().setCastSpell(null);
                    Debugs.CMB.debug(entity, "spell !cancast.", other, true);
                    return false;
                }

                // Duel, disabled magic?
                if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MAGIC.ordinal()]) {
                    DialogueManager.sendStatement(player, "Magic has been disabled in this duel!");
                    player.getCombat().reset();
                    Debugs.CMB.debug(entity, "no magic in duel.", other, true);
                    return false;
                }
            } else if (type == CombatType.RANGED) {
                // Duel, disabled ranged?
                if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_RANGED.ordinal()]) {
                    DialogueManager.sendStatement(player, "Ranged has been disabled in this duel!");
                    player.getCombat().reset();//Ranged attacks disabled, stop combat
                    Debugs.CMB.debug(entity, "no range in duel.", other, true);
                    return false;
                }

                // Check that we have the ammo required
                if (!CombatFactory.checkAmmo(player)) {
                    Debugs.CMB.debug(entity, "no ammo", other, true);
                    player.getCombat().reset();//Out of ammo, stop combat
                    return false;
                }
            } else if (type == CombatType.MELEE) {
                if (player.getDueling().inDuel() && player.getDueling().getRules()[DuelRule.NO_MELEE.ordinal()]) {
                    DialogueManager.sendStatement(player, "Melee has been disabled in this duel!");
                    player.getCombat().reset();//Melee attacks disabled, stop combat
                    Debugs.CMB.debug(entity, "no melee in duel.", other, true);
                    return false;
                }
                //Att acking Aviansie with melee.
                if (other.isNpc()) {
                    int id = other.getAsNpc().id();

                    if (id == VESPULA) {
                        entity.message("Vespula is flying too high for you to hit with melee!");
                        entity.getCombat().reset();//Vespula out of range, stop combat
                        return false;
                    }

                    if (id == 3166 || id == 3167 || id == 3168 || id == 3169 || id == 3170 || id == 3171
                        || id == 3172 || id == 3173 || id == 3174 || id == 3175 || id == 3176 || id == 3177
                        || id == 3178 || id == 3179 || id == 3180 || id == 3181 || id == 3182 || id == 3183) {
                        entity.message("The Aviansie is flying too high for you to attack using melee.");
                        entity.getCombat().reset();//Aviansie out of range, stop combat
                        return false;
                    } else if (id >= 3162 && id <= 3165 || id == 15016 || id == 11113) {
                        entity.message("It's flying too high for you to attack using melee.");
                        entity.getCombat().reset();//Monster out of range, stop combat
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public CombatType styleOf() {
        if (mob == null) return null;
        if (!mob.isPlayer()) // this mtd is players only
            return null;
        if (this instanceof MagicCombatMethod)
            return CombatType.MAGIC;
        if (this instanceof RangedCombatMethod)
            return CombatType.RANGED;
        if (this instanceof MeleeCombatMethod)
            return CombatType.MELEE;
        if (this.getClass().getPackageName().contains("magic"))
            return CombatType.MAGIC;
        if (this.getClass().getPackageName().contains("melee"))
            return CombatType.MELEE;
        if (this.getClass().getPackageName().contains("range"))
            return CombatType.RANGED;
        if (mob.getAsPlayer().getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND) || mob.getAsPlayer().getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND_RAIDS))
            return CombatType.MAGIC;
        System.err.println("unknown player styleOf combat script: " + this + " wep " + mob.getAsPlayer().getEquipment().getId(3));
        return null;
    }
    public List<Mob> getPossibleTargets(Mob entity) {
        return getPossibleTargets(entity, 14, true, false);
    }

    public List<Mob> getPossibleTargets(Mob entity, int ratio, boolean players, boolean npcs) {
        List<Mob> possibleTargets = new ArrayList<>();
        if (players) {
            for (Player player : World.getWorld().getPlayers()) {
                if (player == null || player.dead() || (player.tile().distance(entity.getCentrePosition()) > ratio) || (player.tile().level != entity.tile().level)) {
                    continue;
                }
                possibleTargets.add(player);
            }
        }
        if (npcs) {
            for (Npc npc : World.getWorld().getNpcs()) {
                if (npc == null || npc == entity || npc.dead() || npc.getCentrePosition().distance(entity.getCentrePosition()) > ratio || npc.tile().level != entity.tile().level) {
                    continue;
                }
                possibleTargets.add(npc);
            }
        }
        return possibleTargets;
    }

    protected void follow(int distance) {
        DumbRoute.step(mob, target, distance);
    }

    public boolean rollSuperior(Npc npc) {
        return false;
    }

    public void onRespawn(Npc npc) {

    }

    public boolean isAggressive() {
        if (this.mob == null)
            return false;
        if (!this.mob.isNpc())
            return false;
        return this.mob.isNpc() && this.mob.getAsNpc().getCombatInfo() != null && this.mob.getAsNpc().getCombatInfo().aggressive && this.mob.getAsNpc().inViewport();
    }
}
