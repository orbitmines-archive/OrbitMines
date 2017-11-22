package com.orbitmines.api;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum StaffRank {

    NONE("None", Color.GRAY, Color.SILVER),
    BUILDER("Builder", Color.FUCHSIA, Color.SILVER),
    MODERATOR("Moderator", Color.AQUA, Color.SILVER),
    DEVELOPER("Developer", Color.RED, Color.SILVER),
    OWNER("Owner", Color.MAROON, Color.SILVER);

    private final String name;
    private final Color prefixColor;
    private final Color chatColor;

    StaffRank(String name, Color prefixColor, Color chatColor) {
        this.name = name;
        this.prefixColor = prefixColor;
        this.chatColor = chatColor;
    }

    public String getName() {
        return name;
    }

    public Color getPrefixColor() {
        return prefixColor;
    }

    public Color getChatColor() {
        return chatColor;
    }

    public String getPrefix() {
        return getPrefix(Color.SILVER);
    }

    public String getPrefix(Color color) {
        return this == NONE ? color.getChatColor() : prefixColor.getChatColor() + "§l" + name + (color == null ? "" : "§r " + color.getChatColor());
    }

    public String getDisplayName() {
        return this == NONE ? "§f§lNone" : prefixColor.getChatColor() + "§l" + name;
    }
}
