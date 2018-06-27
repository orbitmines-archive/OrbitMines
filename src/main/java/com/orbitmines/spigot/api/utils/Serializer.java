package com.orbitmines.spigot.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static String serializeUUIDList(List<UUID> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0)
                stringBuilder.append("|");

            stringBuilder.append(list.get(i).toString());
        }
        return stringBuilder.toString();
    }

    public static List<UUID> parseUUIDList(String string) {
        List<UUID> list = new ArrayList<>();

        if (string.equals(""))
            return list;

        for (String uuidString : string.split("\\|")) {
            list.add(UUID.fromString(uuidString));
        }

        return list;
    }

    public static String serializeLongList(List<Long> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0)
                stringBuilder.append("|");

            stringBuilder.append(list.get(i));
        }
        return stringBuilder.toString();
    }

    public static List<Long> parseLongList(String string) {
        List<Long> list = new ArrayList<>();

        if (string.equals(""))
            return list;

        for (String longString : string.split("\\|")) {
            list.add(Long.parseLong(longString));
        }

        return list;
    }
}
