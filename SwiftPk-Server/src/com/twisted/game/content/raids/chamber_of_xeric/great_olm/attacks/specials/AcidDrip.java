package com.twisted.game.content.raids.chamber_of_xeric.great_olm.attacks.specials;

import com.twisted.game.content.raids.chamber_of_xeric.great_olm.GreatOlm;
import com.twisted.game.content.raids.party.Party;
import com.twisted.game.task.Task;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.hit.SplatType;
import com.twisted.game.world.entity.mob.npc.Npc;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.object.GameObject;
import com.twisted.game.world.position.Tile;
import com.twisted.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Patrick van Elderen | May, 16, 2021, 18:58
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class AcidDrip {

    private static boolean finished(Mob mob, Player player) {
        if (mob != null) {
            return mob.isNpc() && (mob.dead() || !mob.isRegistered()) || (player != null && (player.dead() || !player.isRegistered() || player.tile().distance(mob.tile()) > 12));
        }
        return false;
    }

    public static void performAttack(Party party) {
        Npc npc = party.getGreatOlmNpc();
        npc.performGreatOlmAttack(party);
        party.setOlmAttackTimer(6);

        Player player = party.randomPartyPlayer();
        List<GameObject> poisons = new ArrayList<>();
        List<Tile> poisonTiles = new ArrayList<>();
        Player[] closePlayers = npc.closePlayers(64);

        Task.repeatingTask(t -> {
            if (finished(npc, player) || t.tick >= 23) {
                t.stop();
            } else {
                if (player != null && (player.getRaids() != null && player.getRaids().raiding(player) && GreatOlm.insideChamber(player))) {
                    Tile acidSpotPosition = player.tile();
                    GameObject pool = new GameObject(30032, acidSpotPosition, 10, World.getWorld().random(3)).setSpawnedfor(Optional.of(player));
                    poisons.add(pool);
                    Chain.bound(null).runFn(6, () -> {
                        poisonTiles.add(acidSpotPosition);
                    });
                }

                for (GameObject object : poisons) {
                    for (Player p : closePlayers) {
                        p.getPacketSender().sendObject(object);
                    }
                }

                //System.out.println("closeplayers: "+closePlayers.length);
                for (Player p : closePlayers) {
                    if (poisonTiles.contains(p.tile())) {
                        int hit = World.getWorld().random(1, 3);
                        p.hit(npc, hit, SplatType.POISON_HITSPLAT);
                    }
                }
            }
        });

        Chain.bound(null).runFn(1 + 23 + 1, () -> {
            poisons.forEach(object -> {
                for (Player p : closePlayers) {
                    p.getPacketSender().sendObjectRemoval(object);
                }
            });
            poisons.clear();
            poisonTiles.clear();
        });
    }
}
