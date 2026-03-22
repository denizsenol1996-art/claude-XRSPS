package com.hazy.draw.flames;

import java.time.Instant;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public enum FlameImages {
    // Halloween Icons
    BAT(1919, FlameImages::isHalloween),
    PUMPKIN(1920, FlameImages::isHalloween),
    SKULL(1921, FlameImages::isHalloween),
    MOON(1922, FlameImages::isHalloween),


    // Christmas Icons
    SNOWFLAKE(1923, FlameImages::isChristmas),
    HOLLY(1924, FlameImages::isChristmas),
    SNOWMAN(1925, FlameImages::isChristmas),
    ANGEL(1926, FlameImages::isChristmas),

    // Normal Icons
    FIRE_RUNE(1927, FlameImages::isNotHoliday),
    WATER_RUNE(1928, FlameImages::isNotHoliday),
    EARTH_RUNE(1929, FlameImages::isNotHoliday),
    AIR_RUNE(1930, FlameImages::isNotHoliday),
    BODY_RUNE(1931, FlameImages::isNotHoliday),
    WRATH_RUNE(1932, FlameImages::isNotHoliday),
    CHAOS_RUNE(1933, FlameImages::isNotHoliday),
    COSMIC_RUNE(1934, FlameImages::isNotHoliday),
    NATURE_RUNE(1935, FlameImages::isNotHoliday),
    LAW_RUNE(1936, FlameImages::isNotHoliday),
    DEATH_RUNE(1937, FlameImages::isNotHoliday),
    SOUL_RUNE(1938, FlameImages::isNotHoliday);

    private final int id;
    private final BooleanSupplier active;

    public int getId() {
        return id;
    }

    FlameImages(int id, BooleanSupplier active) {
        this.id = id;
        this.active = active;
    }

    private static final int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public static boolean isNotHoliday() {
        return !isChristmas() && !isHalloween();
    }

    public static boolean isHalloween() {
        Date dateStart = new GregorianCalendar(currentYear, Calendar.OCTOBER, 21).getTime();
        Date dateEnd = new GregorianCalendar(currentYear, Calendar.NOVEMBER, 11).getTime();
        return isWithinRange(dateStart, dateEnd);
    }

    public static boolean isChristmas() {
        Date dateStart = new GregorianCalendar(currentYear, Calendar.DECEMBER, 16).getTime();
        Date dateEnd = new GregorianCalendar(currentYear, Calendar.JANUARY, 5).getTime();
        return isWithinRange(dateStart, dateEnd);
    }

    private static boolean isWithinRange(Date startDate, Date endDate) {
        Date currentDate = Date.from(Instant.now());
        return !(currentDate.before(startDate) || currentDate.after(endDate));
    }

    private static List<FlameImages> flameImages = Collections.emptyList();

    public static int getRandomImage() {
        if (flameImages.isEmpty()) {
            flameImages = Arrays.stream(FlameImages.values())
                .filter(image -> image.active != null && image.active.getAsBoolean())
                .collect(Collectors.toList());
        }
        return flameImages.get(new Random().nextInt(flameImages.size())).id;
    }
}
