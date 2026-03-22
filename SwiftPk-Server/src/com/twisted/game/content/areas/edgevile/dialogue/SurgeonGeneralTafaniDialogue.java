package com.twisted.game.content.areas.edgevile.dialogue;

import com.twisted.game.content.mechanics.Poison;
import com.twisted.game.world.entity.combat.CombatSpecial;
import com.twisted.game.world.entity.combat.Venom;
import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.dialogue.Expression;
import com.twisted.game.world.entity.masks.graphics.Graphic;
import com.twisted.game.world.entity.mob.player.skills.Skills;
import com.twisted.util.Color;
import com.twisted.util.timers.TimerKey;

public class SurgeonGeneralTafaniDialogue extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        setPhase(0);
        send(DialogueType.NPC_STATEMENT, player.getInteractingNpcId(), Expression.DEFAULT, "Would you like me to heal you?");

    }

    @Override
    public void next() {
        if (isPhase(0)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No thanks.");
            setPhase(1);
        }
    }

    @Override
    public void select(int option) {
        if (isPhase(1)) {
            setPhase(2);
            if (option == 1) {
                player.performGraphic(new Graphic(683));
                player.message("<col="+ Color.BLUE.getColorValue()+">You have restored your hitpoints, run energy and prayer.");
                player.message("<col="+ Color.HOTPINK.getColorValue()+">You've also been cured of poison and venom.");
                player.hp(Math.max(player.skills().level(Skills.HITPOINTS), player.skills().xpLevel(Skills.HITPOINTS)), 20); //Set hitpoints to 100%
                player.skills().replenishSkill(5, player.skills().xpLevel(5)); //Set the players prayer level to full
                player.skills().replenishStatsToNorm();
                player.setRunningEnergy(100.0, true);
                Poison.cure(player);
                Venom.cure(2, player, false);

                if(player.getMemberRights().isEmeraldOrGreater(player)) {
                    if (player.getTimerRepository().has(TimerKey.RECHARGE_SPECIAL_ATTACK)) {
                        player.message("Special attack energy can only be restored every couple of minutes.");
                    } else {
                        player.setSpecialAttackPercentage(100);
                        player.setSpecialActivated(false);
                        CombatSpecial.updateBar(player);
                        player.getTimerRepository().register(TimerKey.RECHARGE_SPECIAL_ATTACK,150); //Set the value of the timer.
                        player.message("<col="+ Color.HOTPINK.getColorValue()+">You have restored your special attack.");
                    }
                }
            }
            stop();
        }
    }

}
