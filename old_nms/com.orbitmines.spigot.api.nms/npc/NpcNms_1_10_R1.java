package com.orbitmines.spigot.api.nms.npc;

import com.orbitmines.spigot.api.utils.ReflectionUtils;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.EntityTypes;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by Fadi on 30-4-2016.
 */
public class NpcNms_1_10_R1 implements NpcNms {

    private Field mapStringToClassField, mapClassToStringField, mapIdToClassField, mapClassToIdField, mapStringToIdField;

    public void setClassFields() {
        mapStringToClassField = ReflectionUtils.getDeclaredField(EntityTypes.class, "c");
        mapClassToStringField = ReflectionUtils.getDeclaredField(EntityTypes.class, "d");
        //TODO 'e" for enderman?
        mapClassToIdField = ReflectionUtils.getDeclaredField(EntityTypes.class, "f");
        mapStringToIdField = ReflectionUtils.getDeclaredField(EntityTypes.class, "g");
    }

    public void addCustomEntity(Class entityClass, String name, int id) {
        if (mapStringToClassField == null || mapStringToIdField == null || mapClassToStringField == null || mapClassToIdField == null)
            return;

        try {
            Map mapStringToClass = (Map) mapStringToClassField.get(null);
            Map mapStringToId = (Map) mapStringToIdField.get(null);
            Map mapClassToString = (Map) mapClassToStringField.get(null);
            Map mapClassToId = (Map) mapClassToIdField.get(null);

            mapStringToClass.put(name, entityClass);
            mapStringToId.put(name, id);
            mapClassToString.put(entityClass, name);
            mapClassToId.put(entityClass, id);

            mapStringToClassField.set(null, mapStringToClass);
            mapStringToIdField.set(null, mapStringToId);
            mapClassToStringField.set(null, mapClassToString);
            mapClassToIdField.set(null, mapClassToId);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setNoAI(org.bukkit.entity.Entity entity) {
        net.minecraft.server.v1_10_R1.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
    }

    public float[] handleMovement(org.bukkit.entity.Entity rideable, float speed, float backMultiplier, float sideMultiplier, float walkHeight) {
        EntityLiving nmsRideable = ((CraftLivingEntity) rideable).getHandle();

        nmsRideable.lastYaw = nmsRideable.yaw = nmsRideable.passengers.get(0).yaw;
        nmsRideable.pitch = nmsRideable.passengers.get(0).pitch * 0.5F;

        try {
            ReflectionUtils.getDeclaredMethod(net.minecraft.server.v1_10_R1.Entity.class, "setYawPitch", float.class, float.class).invoke(nmsRideable.yaw, nmsRideable.pitch);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        nmsRideable.aO = nmsRideable.aM = nmsRideable.yaw;

        nmsRideable.P = walkHeight;

        float sideMot = ((EntityLiving) nmsRideable.passengers.get(0)).bf * 0.5F;
        float forMot = ((EntityLiving) nmsRideable.passengers.get(0)).bg;

        if (forMot <= 0.0F)
            forMot *= backMultiplier;

        sideMot *= sideMultiplier;

        nmsRideable.l(speed);

        return new float[]{ sideMot, forMot };
    }

    public void handleJump(org.bukkit.entity.Entity rideable, float jumpHeight) {
        EntityLiving nmsRideable = ((CraftLivingEntity) rideable).getHandle();

        if (!nmsRideable.onGround)
            return;

        Field field = ReflectionUtils.getDeclaredField(EntityLiving.class, "be");

        try {
            nmsRideable.motY = field.getBoolean(nmsRideable.passengers.get(0)) ? jumpHeight : 0D;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}