package com.orbitmines.spigot.api.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.orbitmines.spigot.OrbitMines;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

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

    public static String toJSONString(ItemStack item) {
        Method asNMSCopyMethod = getDeclaredMethod(getOBCClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class);

        Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
        Method saveNmsItemStackMethod = getDeclaredMethod(getNMSClass("ItemStack"), "save", nbtTagCompoundClass);

        try {
            return saveNmsItemStackMethod.invoke(asNMSCopyMethod.invoke(null, item), nbtTagCompoundClass.newInstance()).toString();
        } catch (Exception ex) {
            return null;
        }
    }

    public static GameProfile getGameProfile(String texture) {
        GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), null);
        newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + texture + "\"}}}")));
        return newSkinProfile;//https://bukkit.org/threads/how-to-get-the-skull-with-texture-as-itemstack-correctly-alex-head-error.461178/
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
        String version = OrbitMines.getInstance().getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNMSClassName(String name) {
        String version = OrbitMines.getInstance().getServer().getClass().getPackage().getName().split("\\.")[3];
        return "net.minecraft.server." + version + "." + name;
    }

    public static Class<?> getOBCClass(String name) {
        String version = OrbitMines.getInstance().getServer().getClass().getPackage().getName().split("\\.")[3];
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
