package com.orbitmines.spigot.servers.uhsurvival.utils.enums;

/**
 * Created by Robin on 3/7/2018.
 */
public enum ChestRarity {

    NORMAL(50, 10 * 60),
    RARE(30, 5 * 60),
    LEGENDARY(10, 3 * 60);

    private int chance;
    private int time;

    ChestRarity(int chance, int time){
        this.chance = chance;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public int getChance() {
        return chance;
    }
}
