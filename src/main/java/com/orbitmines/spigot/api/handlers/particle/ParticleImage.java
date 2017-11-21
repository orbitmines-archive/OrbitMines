package com.orbitmines.spigot.api.handlers.particle;

import com.madblock.spigot.api.utils.LocationUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class ParticleImage extends ParticleAnimation {

    private static final double OFFSET = 0.2D;

    public ParticleImage(Location center) {
        super(null, center);
    }

    /* 3d image, x, y, z */
    public abstract Pixel[][][] get3DImage();

    /* Image Rotation */
    public abstract float getYaw();

    public abstract void onAnimation();

    @Override
    public void playAnimation() {
        Location center = LocationUtils.copy(location);
        float yaw = getYaw();
        if (yaw < 0)
            yaw = 180 + Math.abs(yaw);

        double angle = toRadians(yaw);

        Pixel[][][] image = get3DImage();

        //TODO CENTER

        for (int x = 0; x < image.length; x++) {
            Pixel[][] pX = image[x];

            for (int y = 0; y < pX.length; y++) {
                Pixel[] pY = pX[y];

                for (int z = 0; z < pY.length; z++) {
                    Pixel pZ = pY[z];

                    if (pZ == null)
                        continue;

                    /* Apply pixel & show */
                    vector.setX(x * OFFSET).setY(y * OFFSET).setZ(z * OFFSET);

                    rotateAroundY(angle);

                    this.particle = pZ.particle;
                    this.xOff = pZ.xOff;
                    this.yOff = pZ.yOff;
                    this.zOff = pZ.zOff;
                    this.speed = pZ.speed;
                    this.amount = pZ.amount;
                    this.location = center;

                    onAnimation();

                    send();
                }
            }
        }
    }

    public static class Pixel {

        private EnumParticle particle;
        private float xOff;
        private float yOff;
        private float zOff;
        private float speed;
        private int amount;

        public Pixel(EnumParticle particle, float xOff, float yOff, float zOff, float speed, int amount) {
            this.particle = particle;
            this.xOff = xOff;
            this.yOff = yOff;
            this.zOff = zOff;
            this.speed = speed;
            this.amount = amount;
        }

        public EnumParticle getParticle() {
            return particle;
        }

        public void setParticle(EnumParticle particle) {
            this.particle = particle;
        }

        public float getXOff() {
            return xOff;
        }

        public void setXOff(float xOff) {
            this.xOff = xOff;
        }

        public float getYOff() {
            return yOff;
        }

        public void setYOff(float yOff) {
            this.yOff = yOff;
        }

        public float getZOff() {
            return zOff;
        }

        public void setZOff(float zOff) {
            this.zOff = zOff;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        /* Only 0-255 */
        public void setRGB(int red, int green, int blue) {
            xOff = (red == 0 ? 0.001f : red) / 255;
            yOff = (green == 0 ? 0.001f: green) / 255;
            zOff = (blue == 0 ? 0.001f : blue) / 255;

            speed = 1;
            amount = 0;
        }
    }
}
