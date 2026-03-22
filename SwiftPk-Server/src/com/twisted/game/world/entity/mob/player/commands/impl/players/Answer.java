package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.content.TriviaBot;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;


public class Answer implements Command {


    @Override
    public void execute(Player player, String command, String[] parts) {
        if (player.muted()) {
            player.message("You are muted and cannot use this features. Please try again later.");
            return;
        }
        if (player.jailed()) {
            player.message("You are jailed and cannot use this features. Please try again later.");
            return;
        }
        String triviaAnswer = command.substring(7);
        if(TriviaBot.acceptingQuestion()){
            TriviaBot.attemptAnswer(player, triviaAnswer);
        } else {
            player.message("There is no trivia going on at the moment..");
        }
        return;

    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
