package com.orbitmines.spigot.api.nms.npc;

import com.orbitmines.spigot.api.utils.ReflectionUtils;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

/**
 * Created by Fadi on 30-4-2016.
 */
public class NpcNms_1_11_R1 implements NpcNms {

    public void setClassFields() {

    }

    public void addCustomEntity(Class entityClass, String name, int id) {
        MinecraftKey key = new MinecraftKey(name);

        try {
            ((RegistryMaterials) ReflectionUtils.getDeclaredObject("b", EntityTypes.class, null)).a(id, key, entityClass);
            ((Set) ReflectionUtils.getDeclaredObject("d", EntityTypes.class, null)).add(key);
            ((List) ReflectionUtils.getDeclaredObject("g", EntityTypes.class, null)).set(id, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNoAI(org.bukkit.entity.Entity entity) {
        net.minecraft.server.v1_11_R1.Entity nmsEntity = ((CraftEntity) entity).getHandle();
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
            ReflectionUtils.getDeclaredMethod(net.minecraft.server.v1_11_R1.Entity.class, "setYawPitch", float.class, float.class).invoke(nmsRideable.yaw, nmsRideable.pitch);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        nmsRideable.aN = nmsRideable.aL = nmsRideable.yaw;

        nmsRideable.P = walkHeight;

        float sideMot = ((EntityLiving) nmsRideable.passengers.get(0)).be * 0.5F;
        float forMot = ((EntityLiving) nmsRideable.passengers.get(0)).bf;

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

        Field field = ReflectionUtils.getDeclaredField(EntityLiving.class, "bd");

        try {
            nmsRideable.motY = field.getBoolean(nmsRideable.passengers.get(0)) ? jumpHeight : 0D;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
