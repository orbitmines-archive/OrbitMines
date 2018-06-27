package com.orbitmines.api.utils;

import com.orbitmines.api.Language;
import com.orbitmines.api.Message;

import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TimeUtils {

    public static String fromTimeStamp(long millis, Language language) {
        long seconds = (millis / 1000L);

        long d = ((seconds - (seconds % (60L * 60L * 24L))) / (60L * 60L * 24L));
        long h = (seconds / (60L * 60L)) % 24L;
        long m = (seconds / 60L) % 60L;
        long s = seconds % 60L;

        StringBuilder stringBuilder = new StringBuilder();

        if (d != 0)
            stringBuilder.append(NumberUtils.locale(d)).append("d");
        if (h != 0)
            stringBuilder.append(NumberUtils.locale(h)).append(new Message("u", "h").lang(language));
        if (m != 0)
            stringBuilder.append(NumberUtils.locale(m)).append("m");
        if (d == 0 && s != 0)
            stringBuilder.append(NumberUtils.locale(s)).append("s");

        return stringBuilder.toString();
    }

    public static String biggestTimeUnit(long millis, Language language) {
        long seconds = (millis / 1000L);

        long M = (int) (seconds / (60L * 60L * 24L * 30L));
        long w = (int) (seconds / (60L * 60L * 24L * 7L));
        long d = (int) (seconds / (60L * 60L * 24L));
        long h = (int) (seconds / (60L * 60L));
        long m = (int) (seconds / 60L);

        if (M != 0)
            return NumberUtils.locale(M) + (M == 1 ? new Message(" maand", " month").lang(language) : new Message(" maanden", " months").lang(language));
        else if (w != 0)
            return NumberUtils.locale(w) + (w == 1 ? new Message(" week", " week").lang(language) : new Message(" weken", " weeks").lang(language));
        else if (d != 0)
            return NumberUtils.locale(d) + (d == 1 ? new Message(" dag", " day").lang(language) : new Message(" dagen", " days").lang(language));
        else if (h != 0)
            return NumberUtils.locale(h) + (h == 1 ? new Message(" uur", " hour").lang(language) : new Message(" uren", " hours").lang(language));
        else if (m != 0)
            return NumberUtils.locale(m) + (m == 1 ? new Message(" minuut", " minute").lang(language) : new Message(" minuten", " minutes").lang(language));
        else if (seconds != 0)
            return NumberUtils.locale(seconds) + (seconds == 1 ? new Message(" seconde", " second").lang(language) : new Message(" seconden", " seconds").lang(language));
        else
            return "-";
    }

    public static String timeUnit(TimeUnit timeUnit, long millis, Language language) {
        switch (timeUnit) {

            case MILLISECONDS: {
                return NumberUtils.locale(millis) + (millis == 1 ? new Message(" milliseconde", " millisecond").lang(language) : new Message(" milliseconden", " milliseconds").lang(language));
            }
            case SECONDS: {
                int i = (int) (millis / 1000L);
                return NumberUtils.locale(i) + (i == 1 ? new Message(" seconde", " second").lang(language) : new Message(" seconden", " seconds").lang(language));
            }
            case MINUTES: {
                int i = (int) (millis / (1000L * 60L));
                return NumberUtils.locale(i) + (i == 1 ? new Message(" minuut", " minute").lang(language) : new Message(" minuten", " minutes").lang(language));
            }
            case HOURS: {
                int i = (int) (millis / (1000L * 60L * 60L));
                return NumberUtils.locale(i) + (i == 1 ? new Message(" uur", " hour").lang(language) : new Message(" uur", " hours").lang(language));
            }
            case DAYS: {
                int i = (int) (millis / (1000L * 60L * 60L * 24L));
                return NumberUtils.locale(i) + (i == 1 ? new Message(" dag", " day").lang(language) : new Message(" dagen", " days").lang(language));
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
            return NumberUtils.locale(d) + (d == 1 ? new Message(" dag", " day").lang(language) : new Message(" dagen", " days").lang(language));
        else if (h != 0 && timeUnit.ordinal() >= TimeUnit.HOURS.ordinal())
            return NumberUtils.locale(h) + (h == 1 ? new Message(" uur", " hour").lang(language) : new Message(" uren", " hours").lang(language));
        else if (m != 0 && timeUnit.ordinal() >= TimeUnit.MINUTES.ordinal())
            return NumberUtils.locale(m) + (m == 1 ? new Message(" minuut", " minute").lang(language) : new Message(" minuten", " minutes").lang(language));
        else if (s != 0 && timeUnit.ordinal() >= TimeUnit.SECONDS.ordinal())
            return NumberUtils.locale(s) + (s == 1 ? new Message(" seconde", " second").lang(language) : new Message(" seconden", " seconds").lang(language));
        else if (millis != 0)
            return NumberUtils.locale(millis) + (millis == 1 ? new Message(" milliseconde", " millisecond").lang(language) : new Message(" milliseconden", " milliseconds").lang(language));
        else
            return "-";
    }
}
