package com.orbitmines.api.utils;

import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class NumberUtils {

    public static String toRoman(int number) {
        if (number <= 0)
            return "" + number;

        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

        String romanEqui = "";

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            int key = entry.getKey();
            if (number / key != 0) {
                for (int i = 0; i < (number / key); i++) {
                    romanEqui = romanEqui + map.get(key);
                }
                number = number % key;
            }
        }
        return romanEqui;
    }

    public static String biggestCountUnit(double count) {
        int unitSize = 1000;

        if (count < unitSize)
            return String.format("%.1f", count);

        int exponent = (int) (Math.log(count) / Math.log(unitSize));
        char pre = "kMGTPE".charAt(exponent-1);

        return String.format("%.1f%s", count / Math.pow(unitSize, exponent), pre);
    }

    public static String locale(long number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static String locale(double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }
}
