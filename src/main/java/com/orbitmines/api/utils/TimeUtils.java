package com.orbitmines.api.utils;

import java.util.concurrent.TimeUnit;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class TimeUtils {

    public static String fromTimeStamp(long millis) {
        int seconds = (int) millis / 1000;

        long M = (seconds / (60 * 60 * 24 * 7 * 30)) % 12;
        long w = (seconds / (60 * 60 * 24 * 7)) % 30;
        long d = (seconds / (60 * 60 * 24)) % 7;
        long h = (seconds / (60 * 60)) % 24;
        long m = (seconds / 60) % 60;
        long s = seconds % 60;

        StringBuilder stringBuilder = new StringBuilder();

        if (M != 0)
            stringBuilder.append(M).append("M");
        if (w != 0)
            stringBuilder.append(w).append("w");
        if (d != 0)
            stringBuilder.append(d).append("d");
        if (h != 0)
            stringBuilder.append(h).append("h");
        if (m != 0)
            stringBuilder.append(m).append("m");
        if (s != 0)
            stringBuilder.append(s).append("s");

        return stringBuilder.toString();
    }

    public static String biggestTimeUnit(long millis) {
        int seconds = (int) millis / 1000;

        long M = (seconds / (60 * 60 * 24 * 7 * 30)) % 12;
        long w = (seconds / (60 * 60 * 24 * 7)) % 30;
        long d = (seconds / (60 * 60 * 24)) % 7;
        long h = (seconds / (60 * 60)) % 24;
        long m = (seconds / 60) % 60;
        long s = seconds % 60;

        if (M != 0)
            return M + (M == 1 ? " month" : " months");
        else if (w != 0)
            return w + (w == 1 ? " week" : " weeks");
        else if (d != 0)
            return d + (d == 1 ? " day" : " days");
        else if (h != 0)
            return h + (h == 1 ? " hour" : " hours");
        else if (m != 0)
            return m + (m == 1 ? " minute" : " minutes");
        else
            return s + (s == 1 ? " second" : " seconds");
    }

    public static String timeUnit(TimeUnit timeUnit, long millis) {
        switch (timeUnit) {

            case MILLISECONDS: {
                return millis + (millis == 1 ? " millisecond" : " milliseconds");
            }
            case SECONDS: {
                int i = (int) millis / 1000;
                return i + (i == 1 ? " second" : " seconds");
            }
            case MINUTES: {
                int i = (int) millis / (1000 * 60);
                return i + (i == 1 ? " minute" : " minutes");
            }
            case HOURS: {
                int i = (int) millis / (1000 * 60 * 60);
                return i + (i == 1 ? " hour" : " hours");
            }
            case DAYS: {
                int i = (int) millis / (1000 * 60 * 60 * 7);
                return i + (i == 1 ? " day" : " days");
            }
            default: {
                return null;
            }
        }
    }
}
