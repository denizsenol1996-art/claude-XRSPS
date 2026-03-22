package com.twisted.game.content.newplateau;

import java.util.Calendar;

public class DoubleExperience {//up9

    public static boolean isDoubleExperience() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
            || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
            || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
    }
}
