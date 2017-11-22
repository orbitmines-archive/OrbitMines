package com.orbitmines.spigot.api.handlers.particle.image.builder;

import com.orbitmines.spigot.api.handlers.particle.ParticleImage;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class Particle2DImageBuilder extends ParticleImageBuilder {

    private String[] strings;

    public Particle2DImageBuilder(String... strings) {
        this.strings = strings;
    }

    public ParticleImage.Pixel[][][] build() {
        int xL = -1;
        /* Get max x bound */
        for (int i = 0; i < strings.length; i++) {
            if (xL == -1 || strings[i].length() > xL)
                xL = strings[i].length();
        }

        if (xL == -1)
            throw new ArrayIndexOutOfBoundsException("Invalid X.");

        /* Cube of particles from 3d image */
        ParticleImage.Pixel[][][] image = new ParticleImage.Pixel[xL][strings.length][1];

        for (int y = 0; y < strings.length; y++) {
            String string = strings[y];
            char[] chars = string.toCharArray();

            for (int x = 0; x < chars.length; x++) {
                /* Image is given in order from top to bottom, so we flip the y around */
                image[x][strings.length - 1 - y][0] = parse(chars[x]);
            }
        }

        return image;
    }
}
