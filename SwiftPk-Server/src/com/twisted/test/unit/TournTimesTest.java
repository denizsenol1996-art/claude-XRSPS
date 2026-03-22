package com.twisted.test.unit;

import com.twisted.StackLogger;
import com.twisted.game.content.tournaments.TournamentManager;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.twisted.game.content.tournaments.TournamentManager.active;
import static org.junit.jupiter.api.Assertions.*;

class TournTimesTest {

    @Test
    void times() throws ParseException {
        StackLogger.enableStackLogger();
        assertTrue(active("00:00", "00:00"));
        assertFalse(active("04:01", "04:00"));

        ZonedDateTime c1 = TournamentManager.calForTime("10:25");
        assertEquals(c1.getYear(), ZonedDateTime.now(ZoneId.of("GMT")).getYear());

        System.out.println(ZonedDateTime.now(ZoneId.of("GMT+1")));

        final ZonedDateTime next       = TournamentManager.calForTime("04:00");
        final ZonedDateTime       cal        = TournamentManager.calForTime("02:30");
        long           difference = next.toEpochSecond() - cal.toEpochSecond();
        System.out.printf("%s | %sh %sm %ss | %s%n", difference, difference / 3600, difference / 60, difference, difference % 3600);

        System.out.printf("%s %s %s %s%n", 10 % 2 == 0, 2 % 2 == 0, 3 % 2 == 0, 1 % 2 == 0);

        for (int i = 0; i < 3; i++) {
            System.out.println("i "+i+" "+(i%2==0));
        }
    }

}
