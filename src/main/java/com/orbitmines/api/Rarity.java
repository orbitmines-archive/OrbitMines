package com.orbitmines.api;

/*
 * OrbitMines - @author Fadi Shawki - 15-6-2018
 */

public enum Rarity {

    COMMON(Color.LIME),
    UNCOMMON(Color.AQUA),
    RARE(Color.ORANGE),
    EPIC(Color.PURPLE),
    LEGENDARY(Color.RED);

    private final Color color;

    Rarity(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getDisplayName() {
        return color.getChatColor() + "§l" + toString();
    }
}
