package com.twisted.game.world.entity.mob.player.commands.impl.players;

import com.twisted.game.content.announcements.dyk.DidYouKnowTask;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Color;
import com.twisted.util.Utils;
import org.apache.commons.text.WordUtils;

import java.util.Arrays;
import java.util.List;

public class ShowDidYouKnowCommand implements Command {


    public void execute(Player player, String command, String[] parts) {
        int position = Utils.random(0, DidYouKnowTask.MESSAGES.size() - 1);
        List<String> messages = Arrays.asList(WordUtils.wrap(DidYouKnowTask.MESSAGES.get(position), 75).split("<br>"));
        messages.set(0, "<img=1880>[<col=" + Color.GREEN.getColorValue() + ">Did you know?</col>]: <col="+Color.PURPLE.getColorValue()+">" + messages.get(0) + (messages.size() > 1 ? "..." : "")); //Maroon and Purple are the best colours for this type of message
        messages.forEach(player::message);
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
