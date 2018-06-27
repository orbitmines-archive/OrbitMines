package com.orbitmines.discordbot.utils;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import java.awt.*;

public class ColorUtils {

    public static Color from(com.orbitmines.api.Color color) {
        switch (color) {

            case AQUA:
                return new Color(85, 255, 255);
            case BLACK:
                return new Color(0, 0, 0);
            case BLUE:
                return new Color(85, 85, 255);
            case FUCHSIA:
                return new Color(255, 85, 255);
            case GRAY:
                return new Color(85, 85, 85);
            case GREEN:
                return new Color(0, 170, 0);
            case LIME:
                return new Color(85, 255, 85);
            case MAROON:
                return new Color(170, 0, 0);
            case NAVY:
                return new Color(0, 0, 170);
            case ORANGE:
                return new Color(255, 170, 0);
            case PURPLE:
                return new Color(170, 0, 170);
            case RED:
                return new Color(255, 85, 85);
            case SILVER:
                return new Color(170, 170, 170);
            case TEAL:
                return new Color(0, 170, 170);
            case WHITE:
                return new Color(255, 255, 255);
            case YELLOW:
                return new Color(255, 255, 85);
        }
        throw new NullPointerException();
    }
}
