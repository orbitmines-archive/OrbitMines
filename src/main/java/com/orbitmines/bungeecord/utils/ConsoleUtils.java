package com.orbitmines.bungeecord.utils;

import net.md_5.bungee.api.ProxyServer;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ConsoleUtils {

    public static void empty() {
        ProxyServer.getInstance().getLogger().info("");
    }

    public static void msg(String msg) {
        ProxyServer.getInstance().getConsole().sendMessage("[OrbitMines] " + msg);
    }

    public static void warn(String msg) {
        ProxyServer.getInstance().getConsole().sendMessage("[OrbitMines] §c" + msg);
    }

    public static void succes(String msg) {
        ProxyServer.getInstance().getConsole().sendMessage("[OrbitMines] §a" + msg);
    }
}
