package com.orbitmines.api;

import com.orbitmines.api.utils.RandomUtils;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Color {

    AQUA("§b", "Light Blue"),
    BLACK("§0", "Black"),
    BLUE("§9", "Blue"),
    FUCHSIA("§d", "Pink"),
    GRAY("§8", "Dark Gray"),
    GREEN("§2", "Green"),
    LIME("§a", "Light Green"),
    MAROON("§4", "Dark Red"),
    NAVY("§1", "Dark Blue"),
    ORANGE("§6", "Orange"),
    PURPLE("§5", "Purple"),
    RED("§c", "Red"),
    SILVER("§7", "Gray"),
    TEAL("§3", "Cyan"),
    WHITE("§f", "White"),
    YELLOW("§e", "Yellow");

    private final String chatColor;
    private final String name;

    Color(String chatColor, String name) {
        this.chatColor = chatColor;
        this.name = name;
    }

    public String getChatColor() {
        return chatColor;
    }

    public String getName() {
        return name;
    }

    public static Color random() {
        return RandomUtils.randomFrom(values());
    }

    public static Color next(Color color) {
        Color[] values = Color.values();

        return color.ordinal() == values.length - 1 ? values[0] : values[color.ordinal() + 1];
    }
}
