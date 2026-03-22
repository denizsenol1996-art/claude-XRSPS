package com.twisted.game.world.entity.mob.player.skills.prestige;

import com.twisted.game.world.entity.dialogue.Dialogue;
import com.twisted.game.world.entity.dialogue.DialogueType;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.skills.Skill;

public class PrestigeDialogue extends Dialogue {

    final Player player;
    final String skillName;
    final Skill skill;

    public PrestigeDialogue(final Player player, final Skill skill, final String skillName) {
        this.player = player;
        this.skill = skill;
        this.skillName = skillName;
    }

    @Override
    protected void start(Object... parameters) {
        setPhase(0);
        send(DialogueType.STATEMENT, "Are you sure you want to prestige your " + skillName + " skill?", "This will reset your " + skillName + " level to 1, and cannot be undone.");
    }

    @Override
    protected void next() {
        if(isPhase(0)) {
            setPhase(1);
            send(DialogueType.OPTION, "Are you sure?", "Yes.", "No.");
        }
    }

    @Override
    protected void select(int option) {
        if (option == 1) {
            Prestige.increase(player, skill);
            stop();
        }

        if (option == 2) {
            stop();
        }
    }
}
