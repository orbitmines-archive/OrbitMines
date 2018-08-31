package com.orbitmines.api;

import com.orbitmines.api.utils.RandomUtils;
import net.md_5.bungee.api.ChatColor;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Color {

    AQUA("§b", "Light Blue", ChatColor.AQUA, 85, 255, 255),
    BLACK("§0", "Black", ChatColor.BLACK, 1, 1, 1),/* Discord uses 0,0,0 as default color */
    BLUE("§9", "Blue", ChatColor.BLUE, 85, 85, 255),
    FUCHSIA("§d", "Pink", ChatColor.LIGHT_PURPLE, 255, 85, 255),
    GRAY("§8", "Dark Gray", ChatColor.DARK_GRAY, 85, 85, 85),
    GREEN("§2", "Green", ChatColor.DARK_GREEN, 0, 170, 0),
    LIME("§a", "Light Green", ChatColor.GREEN, 85, 255, 85),
    MAROON("§4", "Dark Red", ChatColor.DARK_RED, 170, 0, 0),
    NAVY("§1", "Dark Blue", ChatColor.DARK_BLUE, 0, 0, 170),
    ORANGE("§6", "Orange", ChatColor.GOLD, 255, 170, 0),
    PURPLE("§5", "Purple", ChatColor.DARK_PURPLE, 170, 0, 170),
    RED("§c", "Red", ChatColor.RED, 255, 85, 85),
    SILVER("§7", "Gray", ChatColor.GRAY, 170, 170, 170),
    TEAL("§3", "Cyan", ChatColor.DARK_AQUA, 0, 170, 170),
    WHITE("§f", "White", ChatColor.WHITE, 255, 255, 255),
    YELLOW("§e", "Yellow", ChatColor.YELLOW, 255, 255, 85);

    private final String chatColor;
    private final String name;
    private final ChatColor md5;

    private final int r;
    private final int g;
    private final int b;
    private final java.awt.Color awtColor;

    Color(String chatColor, String name, ChatColor md5, int r, int g, int b) {
        this.chatColor = chatColor;
        this.name = name;
        this.md5 = md5;
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

    public ChatColor getMd5() {
        return md5;
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
