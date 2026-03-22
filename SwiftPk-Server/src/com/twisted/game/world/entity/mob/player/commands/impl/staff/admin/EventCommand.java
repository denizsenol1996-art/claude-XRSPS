package com.twisted.game.world.entity.mob.player.commands.impl.staff.admin;

import com.twisted.game.content.events.ServerEvent;
import com.twisted.game.content.events.WOGWData;
import com.twisted.game.world.World;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;
import com.twisted.util.Color;

public class EventCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        ServerEvent event = null;
        String msg = switch (parts[1]) {
            case "drops" -> {
                event = WOGWData.DOUBLE_DROPS.event;
                yield "A Double Drops";
            }
            case "exp" -> {
                event = WOGWData.DOUBLE_EXP.event;
                yield "A Double Experience";
            }
            case "dr" -> {
                event = WOGWData.DROP_RATE.event;
                yield "A 10% Drop Rate";
            }
            case "bm" -> {
                event = WOGWData.BLOOD_MONEY_BOOST.event;
                yield "A 2X Blood Money";
            }
            case "slayer" -> {
                event = WOGWData.DOUBLE_SLAYER_POINTS.event;
                yield "A Double Slayer Points";
            }
            case "votes" -> {
                event = WOGWData.DOUBLE_VOTES.event;
                yield "A Double Votes";
            }
            default -> "";
        };

        if (event == null)
            return;

        if (ServerEvent.activeEvents.contains(event)) {
            player.message(Color.RED.wrap("You cannot activate the same event twice."));
            return;
        }

        event.start();
        ServerEvent.activeEvents.add(event);

        World world = World.getWorld();
        world.sendWorldMessage(Color.ORANGE_2.wrap("<shad=0> " + msg + " Event Has Been activated!</shad>"));
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isAdminOrGreater(player);
    }
}
