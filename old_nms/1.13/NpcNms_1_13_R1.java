package com.orbitmines.spigot.api.nms.npc;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import com.orbitmines.spigot.api.utils.ReflectionUtils;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class NpcNms_1_13_R1 implements NpcNms {

    @Override
    public void setNoAI(Entity entity) {
        net.minecraft.server.v1_13_R1.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
    }

    @Override
    public float[] handleMovement(Entity rideable, float speed, float backMultiplier, float sideMultiplier, float walkHeight) {
        EntityLiving nmsRideable = ((CraftLivingEntity) rideable).getHandle();

        nmsRideable.lastYaw = nmsRideable.yaw = nmsRideable.passengers.get(0).yaw;
        nmsRideable.pitch = nmsRideable.passengers.get(0).pitch * 0.5F;

        try {
            ReflectionUtils.getDeclaredMethod(net.minecraft.server.v1_13_R1.Entity.class, "setYawPitch", float.class, float.class).invoke(nmsRideable, nmsRideable.yaw, nmsRideable.pitch);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        nmsRideable.aQ = nmsRideable.aO = nmsRideable.yaw;

        nmsRideable.Q = walkHeight;

        float sideMot = ((EntityLiving) nmsRideable.passengers.get(0)).bh * 0.5F;
        float highMot = ((EntityLiving) nmsRideable.passengers.get(0)).bi;
        float forMot = ((EntityLiving) nmsRideable.passengers.get(0)).bj;

        if (forMot <= 0.0F)
            forMot *= backMultiplier;

        sideMot *= sideMultiplier;

        nmsRideable.o(speed);

        return new float[] { sideMot, highMot, forMot };
    }

    @Override
    public void handleJump(Entity rideable, float jumpHeight) {
        EntityLiving nmsRideable = ((CraftLivingEntity) rideable).getHandle();

        if (!nmsRideable.onGround)
            return;

        Field field = ReflectionUtils.getDeclaredField(EntityLiving.class, "bg");

        try {
            nmsRideable.motY = field.getBoolean(nmsRideable.passengers.get(0)) ? jumpHeight : 0D;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
