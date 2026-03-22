package com.twisted.game.content.raids.chamber_of_xeric.great_olm.attacks.left_hand;

import com.twisted.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.twisted.game.content.raids.chamber_of_xeric.great_olm.attacks.Attacks;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.mob.movement.MovementQueue;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 16, 2021, 18:41
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class CrystalBurst {

    public static void performAttack(Party party) {
        //System.out.println("CrystalBurst");
        party.setLeftHandAttackTimer(20);
        Chain.bound(null).runFn(1, () -> {
            if (party.isLeftHandDead()) {
                return;
            }
            party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getLeftHandObject(), 7356));
        }).then(2, () -> {
            int index = 0;

            for (Player member : party.getMembers()) {
                if (member != null && member.getRaids() != null && member.getRaids().raiding(member) && GreatOlm.insideChamber(member)) {
                    party.getCrystalBursts()[index] = member.tile();
                    ObjectManager.addObj(new GameObject(30033, party.getCrystalBursts()[index], 10, 3));
                    index++;
                }
            }
            party.setCrystalAmount(index);
        }).then(4, () -> {
            party.forPlayers(player -> player.getPacketSender().sendObjectAnimation(party.getLeftHandObject(), 7355));
            for (int i = 0; i < party.getCrystalAmount(); i++) {
                ObjectManager.addObj(new GameObject(30034, party.getCrystalBursts()[i], 10, 3));
                for (Player member : party.getMembers()) {
                    if (member != null && member.getRaids() != null && member.getRaids().raiding(member) && GreatOlm.insideChamber(member)) {
                        if (member.tile().sameAs(party.getCrystalBursts()[i])) {
                            member.message("The crystal beneath your feet grows rapidly and shunts you to the side.");
                            MovementQueue.clippedStep(member,1);
                            member.hit(party.getGreatOlmNpc(), World.getWorld().random(20,40), CombatType.MAGIC).checkAccuracy().submit();
                        }
                    }
                }
            }
        }).then(2, () -> {
            for (int i = 0; i < party.getCrystalAmount(); i++) {
                World.getWorld().tileGraphic(Attacks.LEFTOVER_CRYSTALS, party.getCrystalBursts()[i],50,0);
                ObjectManager.addObj(new GameObject(-1, party.getCrystalBursts()[i], 10, 3));
            }
        });
    }
}
