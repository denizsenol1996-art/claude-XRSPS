package com.twisted.game.world.entity.combat.hit;

import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatFactory;
import com.twisted.game.world.entity.combat.CombatType;
import com.twisted.game.world.entity.mob.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a "hitqueue", processing pending hits aswell as pending damage.
 *
 * @author Shadowrs|Jak refactored to fix design flaws.
 * @author V1 Professor Oak.
 */
public class HitQueue {

    private static final Logger logger = LogManager.getLogger(HitQueue.class);

    // Our list containing all our incoming hits waiting to be processed.
    private final List<Hit> hits = new ArrayList<Hit>();

    public void clear() {
        hits.clear();
    }

    public void process(Mob mob) {

        // If we are dead, clear all pending and current hits.
        if (mob.dead() || (mob.locked() && !mob.isDelayDamageLocked() && !mob.isDamageOkLocked() && !mob.isLogoutOkLocked())) {
            hits.clear();
            //System.out.println("alrady dead .. did you mess with mob hp in the wrong place? "+(mob.isNpc()?mob.getAsNpc().getMobName():""));
            return;
        }
        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            if (TournamentManager.stopDamage(player)) {
                hits.clear();
                return;
            }

            // Immediately nullify damage for the player alive within a duel upon one dying.
            if (player.getDueling().inDuel() && player.getDueling().getOpponent().dead() && !player.locked()) {
                hits.clear();
                player.lockNoDamage();
                return;
            }
        }

        // only handle hits when not locked, otherwise they'll sit in the queue until next cycle
        if (mob.isDelayDamageLocked() || mob.isLogoutOkLocked() || hits.size() == 0) {
            return;
        }
        // Process the pending hits..
        for (Hit hit : new ArrayList<>(hits)) {
            try {
                // skip processing when these fields are null
                if (hit == null || hit.getTarget() == null || hit.getAttacker() == null || hit.getTarget().isNullifyDamageLock()) {
                    hit.toremove = true;
                    continue;
                }
                if(hit.getAttacker().isNpc() && hit.getTarget().isPlayer()) {
                    if(hit.getAttacker().dead()) {
                        hit.toremove = true;
                        continue;
                    }
                }
                if (hit.decrementAndGetDelay() <= 0) {
                    CombatFactory.executeHit(hit);
                    hit.toremove = true;
                    if (shouldShowSplat(hit))
                        hit.showSplat = true;
                    mob.decrementHealth(hit);
                }
            } catch (RuntimeException e) {
                hit.toremove = true;
                logger.error(mob.getMobName()+": RTE in hits - hopefully this stack helps pinpoint the cause: "+hit, e);
                throw e; // send it up the callstack
            }
        }
        List<Hit> toShow = hits.stream().filter(e -> e.showSplat).collect(Collectors.toList());
        hits.removeIf(o -> o.toremove);
        if (toShow.size() == 0)
            return;
        for (Hit hit : toShow) {
            hit.playerSync();
        }
        toShow.clear();
        toShow = null;

    }

    /**
     * don't DISPLAY damage if the attack was a magic splash by a player. (0 blue hitsplat)
     */
    private boolean shouldShowSplat(Hit hit) {
        boolean magic_splash = hit.getCombatType() == CombatType.MAGIC && !hit.isAccurate() && !hit.forceShowSplashWhenMissMagic;
        // only hide 0 magic dmg hitplat in PVP, example npcs can splash and it will show a 0 hitsplat (like kraken)
        return !(magic_splash && hit.getAttacker().isPlayer());
    }

    /**
     * Add a pending hit to our queue.
     */
    public void add(Hit c_h) {
        hits.add(c_h);
    }

    public int size() {
        return hits.size();
    }
}
