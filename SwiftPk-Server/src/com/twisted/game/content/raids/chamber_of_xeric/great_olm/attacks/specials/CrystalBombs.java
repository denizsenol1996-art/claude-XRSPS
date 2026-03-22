package com.twisted.game.content.raids.chamber_of_xeric.great_olm.attacks.specials;

import com.twisted.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.twisted.game.content.raids.chamber_of_xeric.great_olm.OlmAnimations;
import com.twisted.game.content.raids.chamber_of_xeric.great_olm.attacks.Attacks;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.object.ObjectManager;
import com.twisted.game.world.position.Tile;
import com.twisted.util.Utils;
import com.twisted.util.chainedwork.Chain;

/**
 * @author Patrick van Elderen | May, 16, 2021, 18:58
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class CrystalBombs {

    public static void performAttack(Party party, int height) {
        //System.out.println("CrystalBombs");
        party.getGreatOlmNpc().performGreatOlmAttack(party);
        party.setOlmAttackTimer(6);
        Chain.bound(null).runFn(1, () -> {
            if (party.getGreatOlmNpc().dead() || party.isSwitchingPhases()) {
                return;
            }

            for (int i = 0; i < 2; i++) {
                Tile pos = Attacks.randomLocation(height);
                GameObject bomb = new GameObject(29766, pos);
                Npc placeholder = Npc.of(7556, pos);
                new Projectile(party.getGreatOlmNpc(), placeholder, Attacks.FALLING_CRYSTAL, 60, party.getGreatOlmNpc().projectileSpeed(placeholder), 70, 10, 0).sendProjectile();
                Chain.bound(null).runFn(1, () -> {
                    ObjectManager.spawnTempObject(bomb, 6);
                }).then(6, () -> {
                    World.getWorld().tileGraphic(40, pos, 0, 0);
                    for (Player member : party.getMembers()) {
                        if (member != null && member.getRaids() != null && member.getRaids().raiding(member) && GreatOlm.insideChamber(member)) {
                            if(member.dead() || !member.isRegistered()) {
                                return;
                            }
                            if (member.tile().sameAs(pos)) {
                            } else if (Utils.isWithinDiagonalDistance(member.tile(), pos, 1, 1, 1)) {
                                member.hit(party.getGreatOlmNpc(), 10 + World.getWorld().random(5), 1, CombatType.MAGIC).checkAccuracy().submit();
                            } else if (Utils.isWithinDiagonalDistance(member.tile(), pos, 1, 1, 2)) {
                                member.hit(party.getGreatOlmNpc(), 10 + World.getWorld().random(2), 1, CombatType.MAGIC).checkAccuracy().submit();
                            }
                        }
                    }
                    World.getWorld().unregisterNpc(placeholder);
                });
            }
        }).then(2, () -> {
            OlmAnimations.resetAnimation(party);
        });
    }
}
