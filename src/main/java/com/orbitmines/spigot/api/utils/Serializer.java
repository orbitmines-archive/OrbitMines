package com.orbitmines.spigot.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class Serializer {

    public static String serialize(Location location) {
        if (location == null)
            return null;

        return location.getWorld().getName() + "|" + location.getX() + "|" + location.getY() + "|" + location.getZ() + "|" + location.getYaw() + "|" + location.getPitch();
    }

    public static Location parseLocation(String string) {
        if (string == null)
            return null;

        String[] data = string.split("\\|");
        return new Location(Bukkit.getWorld(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5]));
    }
}
