package com.twisted.game.world.entity.combat.method.impl.npcs.raids.cox;

import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Area;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Color;

import static com.twisted.game.world.entity.AttributeKey.MUTTADILE_EATING_STATE;
import static com.twisted.game.world.entity.AttributeKey.MUTTADILE_HEAL_COUNT;
import static com.twisted.util.NpcIdentifiers.MUTTADILE_7562;
import static com.twisted.util.NpcIdentifiers.MUTTADILE_7563;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 29, 2021
 */
public class Muttadile extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        if (mob.isNpc()) {
            Npc npc = mob.getAsNpc();
            Player player = target.getAsPlayer();

            int maxHp = npc.maxHp();
            int currentHp = npc.hp();
            final int hpPercentage = (currentHp * 100 / maxHp);
            //As the muttadiles' health reaches about 40%, they will go to the nearby meat tree and start eating it to replenish health, up to three times each.
            if ((hpPercentage < 40 || npc.<Boolean>getAttribOr(MUTTADILE_EATING_STATE,false)) && npc.<Integer>getAttribOr(MUTTADILE_HEAL_COUNT,0) < 3) {
                // when <40 trigger, then when state is true, still healing
                npc.setEntityInteraction(null);
                npc.getMovementQueue().interpolate(new Tile(3303, 5321, npc.tile().level));
                npc.putAttrib(MUTTADILE_EATING_STATE, true);

                Party party = player.raidsParty;
                if (party == null) return false;

                GameObject meatTree = party.getMeatTree();
                if (npc.tile().isWithinDistance(meatTree.tile(), 2)) {
                    npc.noRetaliation(true);
                    npc.face(meatTree.tile());
                    npc.animate(npc.attackAnimation());
                    npc.heal(World.getWorld().random(125, 200));

                    int newHpPercentage = (currentHp * 100 / maxHp);
                    if(newHpPercentage > 70) {
                        var healCount = npc.<Integer>getAttribOr(MUTTADILE_HEAL_COUNT,0) + 1;
                        npc.putAttrib(MUTTADILE_HEAL_COUNT, healCount);
                        npc.putAttrib(MUTTADILE_EATING_STATE, false);
                    }
                }
            } else {
                //The small muttadile attacks with both Melee and Ranged.
                if (npc.id() == MUTTADILE_7562) {//Small
                    if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
                        meleeAttack(npc, target);
                    } else {
                        rangeAttack(npc, target);
                    }
                    //Apart from having higher stats and using all three forms of combat, this larger muttadile will also perform a shockwave attack if anyone is within melee range,
                    // dealing massive damage to everyone in melee range, which can hit as high as 118.
                } else if (npc.id() == MUTTADILE_7563) {//Big
                    if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
                        if (World.getWorld().rollDie(2, 1)) {
                            shockwaveAttack(npc, target);
                        } else {
                            meleeAttack(npc, target);
                        }
                    } else if (World.getWorld().rollDie(2, 1)) {
                        rangeAttack(npc, target);
                    } else {
                        magicAttack(npc, target);
                    }
                }
            }
        }
        return true;
    }

    private void meleeAttack(Npc npc, Mob target) {
        npc.combatInfo().maxhit = 78;
        npc.animate(npc.attackAnimation());
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
    }

    private void rangeAttack(Npc npc, Mob target) {
        if(target.isPlayer()) {
            Party party = target.getAsPlayer().raidsParty;
            if (party == null) {
                return;
            }

            for (Player member : party.getMembers()) {
                if (member != null && member.getRaids() != null && member.getRaids().raiding(member) && member.tile().inArea(new Area(3300, 5313, 3324, 5338, member.raidsParty.getHeight()))) {
                    npc.combatInfo().maxhit = 35;
                    npc.animate(npc.attackAnimation());
                    var tileDist = npc.tile().transform(1, 1, 0).distance(member.tile());
                    var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
                    new Projectile(npc, member, 1291, 20, 12 * tileDist, npc.id() == MUTTADILE_7562 ? 15 : 35, 30, 0).sendProjectile();
                    member.hit(npc, CombatFactory.calcDamageFromType(npc, member, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
                }
            }
        }
    }

    private void magicAttack(Npc npc, Mob target) {
        if(target.isPlayer()) {
            Party party = target.getAsPlayer().raidsParty;
            if (party == null) {
                return;
            }

            for (Player member : party.getMembers()) {
                if (member != null && member.getRaids() != null && member.getRaids().raiding(member) && member.tile().inArea(new Area(3300, 5313, 3324, 5338, member.raidsParty.getHeight()))) {
                    npc.combatInfo().maxhit = 45;
                    npc.animate(npc.attackAnimation());
                    var tileDist = npc.tile().transform(1, 1, 0).distance(member.tile());
                    var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
                    new Projectile(npc, member, 393, 20, 12 * tileDist, 35, 30, 0).sendProjectile();
                    member.hit(npc, CombatFactory.calcDamageFromType(npc, member, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                }
            }
        }
    }

    private void shockwaveAttack(Npc npc, Mob target) {
        npc.combatInfo().maxhit = 118;
        npc.animate(7424);
        target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
        target.message(Color.RED.wrap("You have been hit by the Muttadiles stomp attack!"));
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
