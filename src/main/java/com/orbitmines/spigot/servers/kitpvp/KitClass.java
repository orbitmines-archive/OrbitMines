package com.orbitmines.spigot.servers.kitpvp;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

public enum KitClass {

    MELEE("Melee"),
    RANGED("Ranged"),
    SPELLCASTER("Spellcaster");

    private final String name;

    KitClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
