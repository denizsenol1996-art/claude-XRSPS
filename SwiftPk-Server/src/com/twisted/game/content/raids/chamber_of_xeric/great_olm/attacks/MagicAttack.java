package com.twisted.game.content.raids.chamber_of_xeric.great_olm.attacks;

import com.twisted.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.twisted.game.content.raids.chamber_of_xeric.great_olm.OlmAnimations;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 16, 2021, 13:34
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class MagicAttack {

    public static void performAttack(Party party) {
        //System.out.println("MagicAttack");
        party.setOlmAttackTimer(6);
        Npc olm = party.getGreatOlmNpc();
        olm.performGreatOlmAttack(party);
        Chain.bound(null).runFn(1, () -> {
            if (olm.dead() || party.isSwitchingPhases()) {
                return;
            }

            for (Player member : party.getMembers()) {
                if (member != null && member.getRaids() != null && member.getRaids().raiding(member) && GreatOlm.insideChamber(member)) {
                    if(member.dead() || !member.isRegistered()) {
                        return;
                    }
                    var delay = olm.getProjectileHitDelay(member);
                    new Projectile(olm, member, Attacks.DARK_GREEN_FLYING, 25, olm.projectileSpeed(member), 70, 31, 0).sendProjectile();
                    member.hit(olm, CombatFactory.calcDamageFromType(olm, member, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                }
            }

            Chain.bound(null).runFn(2, () -> OlmAnimations.resetAnimation(party));
        });
    }
}
