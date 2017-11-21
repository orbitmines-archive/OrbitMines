package com.orbitmines.spigot.api.utils;

import org.bukkit.Bukkit;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ConsoleUtils {

    public static void empty() {
        Bukkit.getLogger().info("");
    }

    public static void msg(String msg) {
        Bukkit.getConsoleSender().sendMessage("[OrbitMines] " + msg);
    }

    public static void warn(String msg) {
        Bukkit.getConsoleSender().sendMessage("[OrbitMines] §c" + msg);
    }

    public static void success(String msg) {
        Bukkit.getConsoleSender().sendMessage("[OrbitMines] §a" + msg);
    }

}
