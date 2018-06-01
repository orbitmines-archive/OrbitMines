package com.orbitmines.api.utils;

import com.orbitmines.api.Language;
import com.orbitmines.api.Message;

import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TimeUtils {

    public static String fromTimeStamp(long millis, Language language) {
        int seconds = (int) millis / 1000;

        long M = (long) (int) (seconds / (60L * 60L * 24L * 7L * 4L));
        long w = (seconds / (60L * 60L * 24L * 7L)) % 4L;
        long d = (seconds / (60L * 60L * 24L)) % 7;
        long h = (seconds / (60L * 60L)) % 24L;
        long m = (seconds / 60L) % 60L;
        long s = seconds % 60L;

        StringBuilder stringBuilder = new StringBuilder();

        if (M != 0)
            stringBuilder.append(M).append("M");
        if (w != 0)
            stringBuilder.append(w).append("w");
        if (d != 0)
            stringBuilder.append(d).append("d");
        if (h != 0)
            stringBuilder.append(h).append(new Message("u", "h").lang(language));
        if (m != 0)
            stringBuilder.append(m).append("m");
        if (s != 0)
            stringBuilder.append(s).append("s");

        return stringBuilder.toString();
    }

    public static String biggestTimeUnit(long millis, Language language) {
        long seconds = (int) (millis / 1000L);

        long M = (int) (seconds / (60L * 60L * 24L * 30L));
        long w = (int) (seconds / (60L * 60L * 24L * 7L));
        long d = (int) (seconds / (60L * 60L * 24L));
        long h = (int) (seconds / (60L * 60L));
        long m = (int) (seconds / 60L);

        if (M != 0)
            return M + (M == 1 ? new Message(" maand", " month").lang(language) : new Message(" maanden", " months").lang(language));
        else if (w != 0)
            return w + (w == 1 ? new Message(" week", " week").lang(language) : new Message(" weken", " weeks").lang(language));
        else if (d != 0)
            return d + (d == 1 ? new Message(" dag", " day").lang(language) : new Message(" dagen", " days").lang(language));
        else if (h != 0)
            return h + (h == 1 ? new Message(" uur", " hour").lang(language) : new Message(" uren", " hours").lang(language));
        else if (m != 0)
            return m + (m == 1 ? new Message(" minuut", " minute").lang(language) : new Message(" minuten", " minutes").lang(language));
        else if (seconds != 0)
            return seconds + (seconds == 1 ? new Message(" seconde", " second").lang(language) : new Message(" seconden", " seconds").lang(language));
        else
            return "-";
    }

    public static String timeUnit(TimeUnit timeUnit, long millis, Language language) {
        switch (timeUnit) {

            case MILLISECONDS: {
                return millis + (millis == 1 ? new Message(" milliseconde", " millisecond").lang(language) : new Message(" milliseconden", " milliseconds").lang(language));
            }
            case SECONDS: {
                int i = (int) (millis / 1000L);
                return i + (i == 1 ? new Message(" seconde", " second").lang(language) : new Message(" seconden", " seconds").lang(language));
            }
            case MINUTES: {
                int i = (int) (millis / (1000L * 60L));
                return i + (i == 1 ? new Message(" minuut", " minute").lang(language) : new Message(" minuten", " minutes").lang(language));
            }
            case HOURS: {
                int i = (int) (millis / (1000L * 60L * 60L));
                return i + (i == 1 ? new Message(" uur", " hour").lang(language) : new Message(" uur", " hours").lang(language));
            }
            case DAYS: {
                int i = (int) (millis / (1000L * 60L * 60L * 24L));
                return i + (i == 1 ? new Message(" dag", " day").lang(language) : new Message(" dagen", " days").lang(language));
            }
            default: {
                return null;
            }
        }
    }

    public static String limitTimeUnitBy(long millis, TimeUnit timeUnit,  Language language) {
        int d = (int) (millis / (1000L * 60L * 60L * 24L));
        int h = (int) (millis / (1000L * 60L * 60L));
        int m = (int) (millis / (1000L * 60L));
        int s = (int) (millis / 1000L);

        if (d != 0 && timeUnit.ordinal() >= TimeUnit.DAYS.ordinal())
            return d + (d == 1 ? new Message(" dag", " day").lang(language) : new Message(" dagen", " days").lang(language));
        else if (h != 0 && timeUnit.ordinal() >= TimeUnit.HOURS.ordinal())
            return h + (h == 1 ? new Message(" uur", " hour").lang(language) : new Message(" uren", " hours").lang(language));
        else if (m != 0 && timeUnit.ordinal() >= TimeUnit.MINUTES.ordinal())
            return m + (m == 1 ? new Message(" minuut", " minute").lang(language) : new Message(" minuten", " minutes").lang(language));
        else if (s != 0 && timeUnit.ordinal() >= TimeUnit.SECONDS.ordinal())
            return s + (s == 1 ? new Message(" seconde", " second").lang(language) : new Message(" seconden", " seconds").lang(language));
        else if (millis != 0)
            return millis + (millis == 1 ? new Message(" milliseconde", " millisecond").lang(language) : new Message(" milliseconden", " milliseconds").lang(language));
        else
            return "-";
    }
}
