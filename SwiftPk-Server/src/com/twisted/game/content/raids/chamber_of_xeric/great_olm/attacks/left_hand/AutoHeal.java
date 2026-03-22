package com.twisted.game.content.raids.chamber_of_xeric.great_olm.attacks.left_hand;

import com.twisted.game.content.raids.party.Party;
import com.twisted.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 16, 2021, 18:41
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class AutoHeal {

    public static void performAttack(Party party) {
        //System.out.println("AutoHeal");
        party.setLeftHandAttackTimer(20);
        Chain.bound(null).runFn(1, () -> {
            if (party.isLeftHandDead()) {
                return;
            }
            party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getLeftHandObject(), 7357));
        }).then(2, () -> {
            party.setHealingOlmLeftHand(true);
        }).then(12, () -> {
            party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getLeftHandObject(), 7355));
            party.setHealingOlmLeftHand(false);
        });
    }
}
