package com.orbitmines.spigot.api.nms.world;

import org.bukkit.Location;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public interface WorldNms {

    void chestAnimation(Location location, boolean opened);

    void enderchestAnimation(Location location, boolean opened);

}
