package com.twisted.game.world.entity.mob.player.commands.impl.dev;

import com.twisted.game.content.tournaments.TournamentManager;
import com.twisted.game.world.entity.mob.player.Player;
import com.twisted.game.world.entity.mob.player.commands.Command;

import java.util.Arrays;

import static java.lang.String.format;

/**
 * For {@link TournamentManager}
 */
public class SetTornHoursCommand implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        if (TournamentManager.getSettings() == null) {
            player.message("The tournament system must be initialized using the conf file first.");
        } else {
            try {
                String[] hours = parts[1].split(",");
                for (String hour : hours) {
                    if (hour == null || hour.length() != 5)
                        throw new HourFormatException(format("Hour input %s is null or not five characters. Format must be 12:34", hour));
                    int hr = Integer.parseInt(hour.substring(0, 2));
                    int sec = Integer.parseInt(hour.substring(3, 5));
                    if (hr < 0 || sec < 0 || hr > 24 || sec > 59)
                        throw new HourFormatException(format("Invalid range for input hour %s. Must be 00:00 - 23:59", hour));
                }
                TournamentManager.setNextTorn(null); // process will re-init next one
                TournamentManager.getSettings().setStartTimes(hours);
                player.getInterfaceManager().close();
                player.message("New tournament system times are: "+ Arrays.toString(hours));
            }
            catch (HourFormatException e) {
                player.message(e.getMessage());
                e.printStackTrace();
            }
            catch (Exception e) {
                player.message(format("Input could not be parsed: %s - %s", command, e));
                player.message("Use format ::settornhours 00:00,05:00,14:00,14:30,23:59");
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getPlayerRights().isDeveloperOrGreater(player);
    }

    static class HourFormatException extends Exception {
        public HourFormatException() {
            super();
        }

        public HourFormatException(String message) {
            super(message);
        }

        public HourFormatException(String message, Throwable cause) {
            super(message, cause);
        }

        public HourFormatException(Throwable cause) {
            super(cause);
        }

        protected HourFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
