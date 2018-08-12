package com.orbitmines.api;

import com.orbitmines.api.utils.RandomUtils;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Color {

    AQUA("§b", "Light Blue", 85, 255, 255),
    BLACK("§0", "Black", 1, 1, 1),/* Discord uses 0,0,0 as default color */
    BLUE("§9", "Blue", 85, 85, 255),
    FUCHSIA("§d", "Pink", 255, 85, 255),
    GRAY("§8", "Dark Gray", 85, 85, 85),
    GREEN("§2", "Green", 0, 170, 0),
    LIME("§a", "Light Green", 85, 255, 85),
    MAROON("§4", "Dark Red", 170, 0, 0),
    NAVY("§1", "Dark Blue", 0, 0, 170),
    ORANGE("§6", "Orange", 255, 170, 0),
    PURPLE("§5", "Purple", 170, 0, 170),
    RED("§c", "Red", 255, 85, 85),
    SILVER("§7", "Gray", 170, 170, 170),
    TEAL("§3", "Cyan", 0, 170, 170),
    WHITE("§f", "White", 255, 255, 255),
    YELLOW("§e", "Yellow", 255, 255, 85);

    private final String chatColor;
    private final String name;

    private final int r;
    private final int g;
    private final int b;
    private final java.awt.Color awtColor;

    Color(String chatColor, String name, int r, int g, int b) {
        this.chatColor = chatColor;
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.awtColor = new java.awt.Color(r, g, b);
    }

    public String getChatColor() {
        return chatColor;
    }

    public String getName() {
        return name;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public java.awt.Color getAwtColor() {
        return awtColor;
    }

    public static Color random() {
        return RandomUtils.randomFrom(values());
    }

    public Color next() {
        Color[] values = Color.values();

        return ordinal() == values.length - 1 ? values[0] : values[ordinal() + 1];
    }
}
