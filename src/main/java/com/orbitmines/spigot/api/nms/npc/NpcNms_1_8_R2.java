package com.orbitmines.spigot.api.nms.npc;

import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.EntityTypes;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Fadi on 30-4-2016.
 */
public class NpcNms_1_8_R2 implements NpcNms {

    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
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

    public static void setNoAI(CraftEntity e) {
        Entity nmsEnt = e.getHandle();
        NBTTagCompound tag = nmsEnt.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEnt.c(tag);
        tag.setInt("NoAI", 1);
        nmsEnt.f(tag);
    }

    private Field mapStringToClassField, mapClassToStringField, mapIdToClassField, mapClassToIdField, mapStringToIdField;

    @Override
    public void setClassFields() {
        try {
            mapStringToClassField = EntityTypes.class.getDeclaredField("c");
            mapClassToStringField = EntityTypes.class.getDeclaredField("d");
            mapClassToIdField = EntityTypes.class.getDeclaredField("f");
            mapStringToIdField = EntityTypes.class.getDeclaredField("g");

            mapStringToClassField.setAccessible(true);
            mapClassToStringField.setAccessible(true);
            mapClassToIdField.setAccessible(true);
            mapStringToIdField.setAccessible(true);
        } catch (Exception ex) {
        }
    }

    public void addCustomEntity(Class entityClass, String name, int id) {
        if (mapStringToClassField == null || mapStringToIdField == null || mapClassToStringField == null || mapClassToIdField == null) {
            return;
        } else {
            try {
                Map mapStringToClass = (Map) mapStringToClassField.get(null);
                Map mapStringToId = (Map) mapStringToIdField.get(null);
                Map mapClasstoString = (Map) mapClassToStringField.get(null);
                Map mapClassToId = (Map) mapClassToIdField.get(null);

                mapStringToClass.put(name, entityClass);
                mapStringToId.put(name, id);
                mapClasstoString.put(entityClass, name);
                mapClassToId.put(entityClass, id);

                mapStringToClassField.set(null, mapStringToClass);
                mapStringToIdField.set(null, mapStringToId);
                mapClassToStringField.set(null, mapClasstoString);
                mapClassToIdField.set(null, mapClassToId);
            } catch (Exception e) {
            }
        }
    }
}
