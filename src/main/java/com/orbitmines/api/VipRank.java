package com.orbitmines.api;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum VipRank {

    NONE("NONE", Color.GRAY, Color.SILVER, 0),
    IRON("IRON", Color.SILVER, Color.WHITE, 500),
    GOLD("GOLD", Color.ORANGE, Color.WHITE, 750),
    DIAMOND("DIAMOND", Color.BLUE, Color.WHITE, 1500),
    EMERALD("EMERALD", Color.LIME, Color.WHITE, 2500);

    private final String name;
    private final Color prefixColor;
    private final Color chatColor;
    private final int solars;

    VipRank(String name, Color prefixColor, Color chatColor, int solars) {
        this.name = name;
        this.prefixColor = prefixColor;
        this.chatColor = chatColor;
        this.solars = solars;
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

    public int getSolars(VipRank previousRank) {
        return previousRank == null ? solars : (solars - previousRank.solars);
    }

    public String getPrefix() {
        return getPrefix(prefixColor);
    }

    public String getPrefix(Color color) {
        return this == NONE ? (color == null ? " " : color.getChatColor()) : prefixColor.getChatColor() + "§l" + name + (color == null ? "" : "§r " + color.getChatColor());
    }

    public String getDisplayName() {
        return this == NONE ? "§f§lNone" : prefixColor.getChatColor() + "§l" + name;
    }
}
