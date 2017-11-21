package com.orbitmines.spigot.api.nms.pet;

import net.minecraft.server.v1_8_R1.EntityLiving;

import java.lang.reflect.Field;

/**
 * Created by Fadi on 14-7-2016.
 */
public abstract class Pet_1_8_R1 {

    protected abstract void setYawPitchPet(float yaw, float pitch);

    public float[] handleMovement(EntityLiving pet, float sideMot, float forMot, float speed) {
        pet.lastYaw = pet.yaw = pet.passenger.yaw;
        pet.pitch = pet.passenger.pitch * 0.5F;

        setYawPitchPet(pet.yaw, pet.pitch);
        pet.aG = pet.aE = pet.yaw;

        pet.S = 1.0F;

        sideMot = (((EntityLiving) pet.passenger).aX * 0.5F);
        forMot = ((EntityLiving) pet.passenger).aY;

        if (forMot <= 0.0F) {
            forMot *= 0.25F;
        }
        sideMot *= 0.75F;

        pet.j(speed);

        float[] xy = {sideMot, forMot};
        return xy;
    }

    public void handleJump(EntityLiving pet) {
        Field jump;
        try {
            jump = EntityLiving.class.getDeclaredField("aW");
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
            return;
        }

        jump.setAccessible(true);

        if (pet.onGround) {
            try {
                if (jump.getBoolean(pet.passenger)) {
                    double jumpHeight = 0.5D;
                    pet.motY = jumpHeight;
                } else {
                    pet.motY = 0D;
                }
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }
}
