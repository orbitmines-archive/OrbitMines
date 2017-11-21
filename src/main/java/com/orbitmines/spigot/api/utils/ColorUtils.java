package com.orbitmines.spigot.api.utils;

import com.orbitmines.api.utils.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ColorUtils {

    public static Color[] VALUES = { Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.MAROON, Color.NAVY, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW };

    public static Color random(){
        return RandomUtils.randomFrom(VALUES);
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static byte getWoolData(com.orbitmines.api.Color color) {
        switch (color) {
            case AQUA:
                return 3;
            case BLACK:
                return 15;
            case BLUE:
                return 11;
            case FUCHSIA:
                return 2;
            case GRAY:
                return 7;
            case GREEN:
                return 13;
            case LIME:
                return 5;
            case MAROON:
                return 14;
            case NAVY:
                return 11;
            case ORANGE:
                return 1;
            case PURPLE:
                return 10;
            case RED:
                return 14;
            case SILVER:
                return 8;
            case TEAL:
                return 9;
            case WHITE:
                return 0;
            case YELLOW:
                return 4;
            default:
                return 0;
        }
    }

    public static Color toBukkitColor(com.orbitmines.api.Color color) {
        switch (color) {

            case AQUA:
                return Color.AQUA;
            case BLACK:
                return Color.BLACK;
            case BLUE:
                return Color.BLUE;
            case FUCHSIA:
                return Color.FUCHSIA;
            case GRAY:
                return Color.GRAY;
            case GREEN:
                return Color.GREEN;
            case LIME:
                return Color.LIME;
            case MAROON:
                return Color.MAROON;
            case NAVY:
                return Color.NAVY;
            case ORANGE:
                return Color.ORANGE;
            case PURPLE:
                return Color.PURPLE;
            case RED:
                return Color.RED;
            case SILVER:
                return Color.SILVER;
            case TEAL:
                return Color.TEAL;
            case WHITE:
                return Color.WHITE;
            case YELLOW:
                return Color.YELLOW;
        }
        return Color.BLACK;
    }
}
