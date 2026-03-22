package com.twisted.game.content.announcements.dyk;

import com.twisted.game.task.Task;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.AttributeKey;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import org.apache.commons.text.WordUtils;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DidYouKnowTask extends Task {

    /**
     * The amount of time in game cycles (600ms) that the event pulses at
     */
    private static final int INTERVAL = Utils.toCyclesOrDefault(5, 1, TimeUnit.MINUTES);

    /**
     * A {@link java.util.Collection} of messages that are to be displayed
     */
    public static final List<String> MESSAGES = Utils.jsonArrayToList(Paths.get("data", "def", "server_announcements/", "did_you_know.json"), String[].class);

    /**
     * The index or position in the list that we're currently at
     */
    private int position;

    /**
     * Creates a new event to cycle through messages for the entirety of the runtime
     */
    public DidYouKnowTask() {
        super("DidYouKnowTask", INTERVAL);
    }

    @Override
    public void execute() {
        //We read the messages top to bottom, so most important messages should go at the top of did_you_know.json
        position++;
        if(MESSAGES == null) {
            stop();
            return;
        }
        if (position >= MESSAGES.size()) {
            position = 0;
        }


        
        List<String> messages = Arrays.asList(WordUtils.wrap(MESSAGES.get(position), 75).split("<br>"));
        messages.set(0, "<img=1894>[<=col=FF0000>DYK?</col>]: <shad=2><col="+Color.BLUE.getColorValue()+">" + messages.get(0) + (messages.size() > 1 ? "..." : "")); //Maroon and Purple are the best colours for this type of message

        for (Player player : World.getWorld().getPlayers()) {// thats fine just wanted it like bold or whatever
            if (player != null) {
                boolean didYouKnowActivated = player.getAttribOr(AttributeKey.DID_YOU_KNOW, true);
                if (didYouKnowActivated)
                    messages.forEach(player::message);
            }
        }
    }

}
