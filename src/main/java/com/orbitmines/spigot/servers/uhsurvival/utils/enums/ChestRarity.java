package com.orbitmines.spigot.servers.uhsurvival.utils.enums;

/**
 * Created by Robin on 3/7/2018.
 */
public enum ChestRarity {

    NORMAL(50),
    RARE(30),
    LEGENDARY(10);

    private int chance;

    ChestRarity(int chance){
        this.chance = chance;
    }

    public int getChance() {
        return chance;
    }
}
