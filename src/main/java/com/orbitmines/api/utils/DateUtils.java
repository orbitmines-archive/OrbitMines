package com.orbitmines.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class DateUtils {

    public static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
}
