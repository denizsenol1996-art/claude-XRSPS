package com.twisted.game.content.newplateau;

import java.util.Calendar;

public class DoubleSlayer {//up9

    public static boolean isDoubleSlayer() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
            || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
            || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
    }
}
