package com.twisted.game.world.entity.mob.player;

import com.twisted.GameServer;
import com.twisted.game.content.skill.impl.slayer.SlayerConstants;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.position.areas.impl.WildernessArea;
import com.twisted.util.timers.TimerKey;

public class RunEnergy {

    private final Player player;

    public RunEnergy(Player player) {
        this.player = player;
    }

    public void update() {
        double energy = player.getAttribOr(AttributeKey.RUN_ENERGY, 0.0);

        double add = player.getRecoveryRate();

        if (!WildernessArea.inWilderness(player.tile())) {
            add *= 2; // Double energy regeneration if we're not in the wilderness.
        }

        if (player.getEquipment().wearsFullGraceful() || player.getEquipment().wearingMaxCape()) {
            add *= 1.3; // 30% increase in restore rate when wearing full graceful
        }

        player.setRunningEnergy(energy + add, true);
    }

    public void drainForMove() {
        //Don't drain for developers at all.
        if(player.getPlayerRights().isDeveloperOrGreater(player) && !GameServer.properties().production) {
            return;
        }

        //Being an extreme member grants you infinite run outside of the wilderness.
        if (player.getMemberRights().isDiamondOrGreater(player) && !WildernessArea.inWilderness(player.tile())) {
            return;
        }

        //Having the perk runaway unlocked grants you infinite run outside of the wilderness.
        if(player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.RUNAWAY) && WildernessArea.inWilderness(player.tile())) {
            return;
        }

        //Being an V.I.P member grants you infinite run anywhere.
        if (player.getMemberRights().isOnyxOrGreater(player)) {
            return;
        }

        boolean hamstrung = false;

        //Grabs the players energy %
        double energy = player.getAttribOr(AttributeKey.RUN_ENERGY, 0);
        //Grabs the change in energy
        double change = player.getEnergyDeprecation();
        //Check to see if the player has drank a stamina potion
        int stamina = player.getAttribOr(AttributeKey.STAMINA_POTION_TICKS, 0);
        //If the player has drank a stamina potion, energy drain is reduced by 70%
        if (stamina > 0) change *= 0.3;
        //If for some reason the change is less then 0, we set it to 0.05
        if (change < 0) change = 0.05;
        if (player.getTimerRepository().has(TimerKey.HAMSTRUNG))
            hamstrung = true;
        //Only drain the players run energy if they are running.
        if (player.getMovementQueue().isRunning()) {
            //We apply the change to the players energy level
            player.setRunningEnergy(hamstrung ? energy - change * 6 : energy - change, true);
        }
    }
}
