package com.orbitmines.spigot.api.nms.pet;

import net.minecraft.server.v1_9_R1.EntityLiving;

import java.lang.reflect.Field;

/**
 * Created by Fadi on 14-7-2016.
 */
public abstract class Pet_1_9_R1 {

    protected abstract void setYawPitchPet(float yaw, float pitch);

    public float[] handleMovement(EntityLiving pet, float sideMot, float forMot, float speed) {
        if (pet.passengers.get(0) == null)
            return null;

        pet.lastYaw = pet.yaw = pet.passengers.get(0).yaw;
        pet.pitch = pet.passengers.get(0).pitch * 0.5F;

        setYawPitchPet(pet.yaw, pet.pitch);
        pet.aM = pet.aK = pet.yaw;

        pet.P = 1.0F;

        sideMot = (((EntityLiving) pet.passengers.get(0)).bd * 0.5F);
        forMot = ((EntityLiving) pet.passengers.get(0)).be;

        if (forMot <= 0.0F) {
            forMot *= 0.25F;
        }
        sideMot *= 0.75F;

        pet.l(speed);

        float[] xy = {sideMot, forMot};
        return xy;
    }

    public void handleJump(EntityLiving pet) {
        if (pet.passengers.get(0) == null)
            return;

        Field jump;
        try {
            jump = EntityLiving.class.getDeclaredField("bc");
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
            return;
        }

        jump.setAccessible(true);

        if (pet.onGround) {
            try {
                if (jump.getBoolean(pet.passengers.get(0))) {
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
