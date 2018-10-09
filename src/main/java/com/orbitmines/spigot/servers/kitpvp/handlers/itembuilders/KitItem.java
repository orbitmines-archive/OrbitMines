package com.orbitmines.spigot.servers.kitpvp.handlers.itembuilders;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.servers.kitpvp.handlers.actives.Active;
import com.orbitmines.spigot.servers.kitpvp.handlers.passives.Passive;

public interface KitItem<T> {

    T addPassive(Passive passive, Integer integer);

    T addActive(Active active, Integer integer);

}
