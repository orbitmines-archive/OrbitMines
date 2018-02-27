package com.orbitmines.spigot.api.utils;

import java.util.Random;

/**
 * Created by Robin on 2/27/2018.
 */
public class MathUtils {

    private static Random random = new Random();

    public static int getInteger(String s){
        try{
            return Integer.parseInt(s);
        }catch(NumberFormatException ex){
            ex.printStackTrace();
        }
        return -1;
    }

    public static int clamp(int x, int min, int max){
        return max < x ? max : x < min ? min : x;
    }

    public static boolean randomize(int min, int max, int x){
        int r = random.nextInt(max);
        return min <= x && x <= r;
    }
}
