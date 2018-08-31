package com.orbitmines.spigot.servers.kitpvp;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

public interface Attributes {

    /* 20.0D default value */
    double getMaxHealth();

    /* 0.0 - 1.0 */
    double getKnockbackResistance();

    HealthRegen getHealthRegen();

}
