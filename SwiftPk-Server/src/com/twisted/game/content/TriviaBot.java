package com.twisted.game.content;


import com.twisted.game.task.Task;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.items.Item;
import com.twisted.util.Utils;

import java.util.concurrent.TimeUnit;

public class TriviaBot extends Task {
    private static final int INTERVAL = Utils.toCyclesOrDefault(20, 1, TimeUnit.MINUTES);


    public TriviaBot() {
        super("TriviaBot", INTERVAL);

    }

    @Override
    protected void execute() {
        if (getPlayerCount() > 0){
            didSend = false;
            askQuestion();
        }

    }

    /* public static void startup() {
         EventManager.getSingleton().addEvent(new Event() {
             @Override
             public void execute(EventContainer e) {
                 if (getPlayerCount() > 0 && currentQuestion.equals("")){
                     didSend = false;
                     askQuestion();
                 }
             }
         }, 300000);
     }*/
    public static void attemptAnswer(Player p, String attempt) {
        if (!currentQuestion.equals("") && attempt.replaceAll("_", " ").equalsIgnoreCase(currentAnswer)) {
            currentQuestion = "";
            didSend = false;
            sendServerMessage("<img=1894><col=800000>Trivia:<col=80000> "+Utils.capitalizeFirst(p.getUsername()) + " You've got the correct answer!");
            p.getInventory().addOrBank(new Item(13307, World.getWorld().random(2500,5000)));
            return;

        } else {
            if(attempt.contains("question") || attempt.contains("repeat")){
                p.message("<img=1894><col=800000>"+Utils.capitalizeFirst(currentQuestion));
                return;
            }
            p.message("<col=800000>Sorry! Wrong answer! "+Utils.capitalizeFirst(currentQuestion));
            return;
        }
    }
    public static boolean acceptingQuestion() {
        return !currentQuestion.equals("");
    }
    private static void askQuestion() {
        for (int i = 0; i < TRIVIA_DATA.length; i++) {
            if (Utils.random(TRIVIA_DATA.length - 1) == i) {
                if(!didSend) {
                    didSend = true;
                    currentQuestion = TRIVIA_DATA[i][0];
                    currentAnswer = TRIVIA_DATA[i][1];
                    sendServerMessage(currentQuestion);
                }
            }
        }
    }
    public static boolean didSend = false;
    private static int getPlayerCount() {
        int players = 0;
        for (Player p : World.getWorld().getPlayers()) {
            if (p != null)
                players++;
        }
        return players;
    }
    private static void sendServerMessage(String message) {
        World.getWorld().sendWorldMessage(message);

    }
    private static final String[][] TRIVIA_DATA = {
        {"<img=1894><col=800000>Trivia:<col=80000> What is the highest combat level in the game?", "126"},
        {"<img=1894><col=800000>Trivia:<col=80000> Which city do you spawn in for the first time?", "edgeville"},
        {"<img=1894><col=800000>Trivia:<col=80000> What is the key called that give you a double roll on boxes?", "key of boxes"},
        {"<img=1894><col=800000>Trivia:<col=80000> What is the name of this server?", "TwistedScape"},
        {"<img=1894><col=800000>Trivia:<col=80000> What is first item in blood money shop?", "abyssal dagger"},
        {"<img=1894><col=800000>Trivia:<col=80000> What is the best mystery box in the game?", "mystery chest"},
        {"<img=1894><col=800000>Trivia:<col=80000> what is the name of hazy owner ingame?", "Hero"}


    };
    public static String currentQuestion;
    private static String currentAnswer;
}
