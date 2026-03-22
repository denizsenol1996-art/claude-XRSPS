package com.twisted.game.task.impl;

import com.twisted.game.task.Task;
import com.twisted.game.world.entity.Mob;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.mob.player.Player;
/**
 * A {@link Task} implementation which handles
 * the regeneration of special attack.
 *
 * @author Professor Oak
 */
public class RestoreSpecialAttackTask extends Task {

    public RestoreSpecialAttackTask(Mob mob) {
        super("RestoreSpecialAttackTask", 50, mob, false);
        this.mob = mob;
        mob.setRecoveringSpecialAttack(true);
    }

    private final Mob mob;

    @Override
    public void execute() {
        if (mob == null || !mob.isRegistered() || mob.getSpecialAttackPercentage() >= 100 || !mob.isRecoveringSpecialAttack()) {
            if (mob != null) {
                mob.setRecoveringSpecialAttack(false);
            }
            stop();
            return;
        }

        boolean ancientChaosElementalPet = false;
        if(mob.isPlayer()) {
            Player player = (Player) mob;
            ancientChaosElementalPet = player.hasPetOut("Ancient chaos elemental");
        }

        int healSpecPercentageBy = ancientChaosElementalPet ? 15: 10;
        int amount = mob.getSpecialAttackPercentage() + healSpecPercentageBy;
        if (amount >= 100) {
            amount = 100;
            mob.setRecoveringSpecialAttack(false);
            stop();
        }
        mob.setSpecialAttackPercentage(amount);

        if (mob.isPlayer()) {
            Player player = mob.getAsPlayer();
            CombatSpecial.updateBar(player);
            if (amount == 50 || amount == 100) {
                player.message("Your special attack energy is now " + player.getSpecialAttackPercentage() + "%.");
            }
        }
    }
}
