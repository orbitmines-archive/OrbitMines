package com.orbitmines.spigot.api.handlers.particle.animation;

import com.madblock.spigot.api.handlers.particle.ParticleAnimation;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class ParticleAnimationCircle extends ParticleAnimation {

    protected double angle;

    public ParticleAnimationCircle(EnumParticle particle) {
        super(particle);
    }

    public ParticleAnimationCircle(EnumParticle particle, Location location) {
        super(particle, location);
    }

    /* Before particle is sent*/
    public abstract void onAnimation();

    /* After particle is sent*/
    public abstract void afterAnimation();

    /* Amount of particles in circle */
    public abstract int getAmount();

    /* Circle width */
    public abstract double getDistance();

    /* Amount of particles per ParticleAnimation#getInterval */
    public abstract int getAmountPerTick();

    @Override
    public void playAnimation() {
        for (int i = 0; i < getAmountPerTick(); i++) {
            if (angle > 360)
                angle %= 360;

            angle += (360 / getAmount());

            double rad = toRadians(angle);
            vector.setX(getDistance() * Math.sin(rad));
            vector.setY(0);
            vector.setZ(getDistance() * Math.cos(rad));

            onAnimation();

            send();
        }

        afterAnimation();
    }
}
