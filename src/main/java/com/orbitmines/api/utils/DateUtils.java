package com.orbitmines.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class DateUtils {

    public static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat SIMPLE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static Date now() {
        return new Date(Calendar.getInstance().getTimeInMillis());
    }

    public static Date parse(SimpleDateFormat format, String string) {
        try {
            return format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMonth() {
        return getMonth(Month.of(Calendar.getInstance().get(Calendar.MONTH) + 1));
    }

    public static String getPrevMonth() {
        return getMonth(Month.of(Calendar.getInstance().get(Calendar.MONTH) + 1).minus(1));
    }

    private static String getMonth(Month month) {
        return month.toString().substring(0, 1).toUpperCase() + month.toString().substring(1, month.toString().length()).toLowerCase();
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
