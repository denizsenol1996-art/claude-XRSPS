package com.twisted.game.world.entity.combat.method.impl.npcs.raids.cox;

import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.position.Area;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 31, 2021
 */
public class MuttadileInTheWater extends CommonCombatMethod {

    @Override
    public boolean prepareAttack(Mob mob, Mob target) {
        magicAttack((Npc) mob, (Player) target);
        return true;
    }

    private void magicAttack(Npc npc, Player target) {
        Party party = target.raidsParty;
        if (party == null) {
            return;
        }

        for (Player member : party.getMembers()) {
            if (member != null && member.getRaids() != null && member.getRaids().raiding(member) && member.tile().inArea(new Area(3300, 5319, 3324, 5338, member.raidsParty.getHeight()))) {
                var tileDist = npc.tile().transform(1, 1, 0).distance(target.tile());
                var delay = Math.max(1, (50 + (tileDist * 12)) / 30);
                new Projectile(npc, target, 393, 20, 12 * tileDist, 0, 30, 0).sendProjectile();
                target.hit(npc, CombatFactory.calcDamageFromType(npc, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
            }
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 15;
    }
}
