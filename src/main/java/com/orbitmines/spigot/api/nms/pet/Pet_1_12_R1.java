package com.orbitmines.spigot.api.nms.pet;

import net.minecraft.server.v1_12_R1.EntityLiving;

import java.lang.reflect.Field;

/**
 * Created by Fadi on 14-7-2016.
 */
public abstract class Pet_1_12_R1 {

    protected abstract void setYawPitchPet(float yaw, float pitch);

    public float[] handleMovement(EntityLiving pet, float sideMot, float highMot, float forMot, float speed) {
        pet.lastYaw = pet.yaw = pet.passengers.get(0).yaw;
        pet.pitch = pet.passengers.get(0).pitch * 0.5F;

        setYawPitchPet(pet.yaw, pet.pitch);
        pet.aN = pet.aL = pet.yaw;

        pet.P = 1.0F;

        sideMot = ((EntityLiving) pet.passengers.get(0)).be * 0.5F;
        highMot = ((EntityLiving) pet.passengers.get(0)).bf;
        forMot = ((EntityLiving) pet.passengers.get(0)).bg;

        if (forMot <= 0.0F) {
            forMot *= 0.3F;
        }
        sideMot *= 0.75F;

        pet.k(speed);

        float[] xy = {sideMot, highMot, forMot};
        return xy;
    }

    public void handleJump(EntityLiving pet) {
        Field jump;
        try {
            jump = EntityLiving.class.getDeclaredField("bd");
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
            return;
        }

        jump.setAccessible(true);

        try {
            if (pet.onGround && jump.getBoolean(pet.passengers.get(0))) {
                double jumpHeight = 0.5D;
                pet.motY = jumpHeight;
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
