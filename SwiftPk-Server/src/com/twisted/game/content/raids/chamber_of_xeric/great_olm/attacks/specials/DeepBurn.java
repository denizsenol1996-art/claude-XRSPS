package com.twisted.game.content.raids.chamber_of_xeric.great_olm.attacks.specials;

import com.twisted.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.twisted.game.content.raids.chamber_of_xeric.great_olm.OlmAnimations;
import com.twisted.game.content.raids.chamber_of_xeric.great_olm.attacks.Attacks;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.task.Task;
import com.twisted.game.task.TaskManager;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.masks.Projectile;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Utils;

/**
 * @author Patrick van Elderen | May, 16, 2021, 18:58
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class DeepBurn {

    public static void performAttack(Party party) {
        //System.out.println("DeepBurn");
        Npc olm = party.getGreatOlmNpc();
        olm.performGreatOlmAttack(party);
        party.setOlmAttackTimer(6);
        party.teamMessage("The Basilisk sounds a cry...");

        TaskManager.submit(new Task("DeepBurn:performAttackTask", 1, party, true) {
            int tick = 0;

            @Override
            public void execute() {
                if (olm.dead() || party.isSwitchingPhases()) {
                    stop();
                }
                //Set first target for projectile
                Player target = party.randomPartyPlayer();
                if(target.dead() || !target.isRegistered()) {
                    stop();
                    return;
                }

                if (tick == 1) {
                    party.getBurnPlayers().add(target);
                    new Projectile(olm, target, Attacks.GREEN_PROJECTILE, 25,
                        olm.projectileSpeed(target), 70, 10, 0).sendProjectile();
                }
                if (tick == 2) {
                    OlmAnimations.resetAnimation(party);
                    if (target.getRaids() != null && target.getRaids().raiding(target) && GreatOlm.insideChamber(target)) {
                        target.forceChat("Burn with me!");
                        target.hit(olm, World.getWorld().random(25), 1, CombatType.MAGIC).checkAccuracy().submit();
                    }
                }
                if (tick == 19) {
                    if (party.getBurnPlayers() != null) {
                        for (int i = 0; i < party.getBurnPlayers().size(); i++) {
                            Player burnPlayer = party.getBurnPlayers().get(i);
                            if (burnPlayer != null
                                && burnPlayer.getRaids() != null && burnPlayer.getRaids().raiding(burnPlayer) && GreatOlm.insideChamber(party.getBurnPlayers().get(0))) {
                                party.getBurnPlayers().get(i)
                                    .message("You feel the deep burning inside dissipate.");
                            }
                        }
                        party.getBurnPlayers().clear();
                    }
                    stop();
                }
                for (int iz = 0; iz < 19; iz++) {
                    if (tick == (4 * iz) + 6) {
                        if (party.getBurnPlayers() != null) {
                            for (Player member : party.getMembers()) {
                                if (member != null && member.getRaids() != null && member.getRaids().raiding(member) && GreatOlm.insideChamber(member)) {
                                    for (int i = 0; i < party.getBurnPlayers().size() - 1; i++) {
                                        if (Utils.isWithinDiagonalDistance(member.tile(), party.getBurnPlayers().get(i).tile(),1,1,1)) {
                                            if (!party.getBurnPlayers().contains(member))
                                                party.getBurnPlayers().add(member);
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < party.getBurnPlayers().size(); i++) {
                                Player member = party.getBurnPlayers().get(i);
                                if (member != null
                                    && member.getRaids() != null && member.getRaids().raiding(member) && GreatOlm.insideChamber(member)) {
                                    member.forceChat("Burn with me!");
                                    var delay = olm.getProjectileHitDelay(member);
                                    member.hit(olm, World.getWorld().random(25), delay, CombatType.MAGIC).checkAccuracy().submit();
                                }
                            }
                        } else {
                            party.teamMessage("NULL, report to an Administrator.");
                        }
                    }
                }
                tick++;
            }
        });

    }
}
