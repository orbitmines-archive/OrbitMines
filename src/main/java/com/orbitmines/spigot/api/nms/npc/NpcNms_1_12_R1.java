package com.orbitmines.spigot.api.nms.npc;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * Created by Fadi on 30-4-2016.
 */
public class NpcNms_1_12_R1 implements NpcNms {

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
        NBTTagCompound tag = new NBTTagCompound();
        nmsEnt.c(tag);
        tag.setInt("NoAI", 1);
        nmsEnt.f(tag);
    }


    /* Used for 1.8-1.10 */
    @Override
    public void setClassFields() {
    }

    /* Changed for 1.11, again... */
    public void addCustomEntity(Class entityClass, String name, int id) {
        MinecraftKey key = new MinecraftKey(name);

        try {
            ((RegistryMaterials) getPrivateField("b", EntityTypes.class, null)).a(id, key, entityClass);
            ((Set) getPrivateField("d", EntityTypes.class, null)).add(key);
            ((List) getPrivateField("g", EntityTypes.class, null)).set(id, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
