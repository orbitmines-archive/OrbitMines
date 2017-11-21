package com.orbitmines.spigot.api.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ReflectionUtils {

    public static void setMaxCapacity(int maxCapacity) {
        try {
            Object server = getOBCClass("CraftServer").getMethod("getHandle").invoke(Bukkit.getServer());
            getDeclaredField(server.getClass().getSuperclass(), "maxPlayers").set(server, maxCapacity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    public static Object getDeclaredObject(String fieldName, Class<?> clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

    public static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Method getDeclaredMethod(Class<?> clazz, String method, Class<?>... types) {
        try {
            Method m = clazz.getDeclaredMethod(method, types);
            m.setAccessible(true);
            return m;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getNMSClass(String name) {
        String version = MadBlock.getInstance().getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNMSClassName(String name) {
        String version = MadBlock.getInstance().getServer().getClass().getPackage().getName().split("\\.")[3];
        return "net.minecraft.server." + version + "." + name;
    }

    public static Class<?> getOBCClass(String name) {
        String version = MadBlock.getInstance().getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCMAClass(String name) {
        try {
            return Class.forName("com.mojang.authlib." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
