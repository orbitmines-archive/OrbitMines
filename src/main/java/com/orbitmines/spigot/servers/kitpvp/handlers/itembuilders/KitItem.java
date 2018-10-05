package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.servers.kitpvp.handlers.Passive;

public interface KitItem<T> {

    T addPassive(Passive passive, Integer integer);

}
