package com.orbitmines.spigot.api.handlers.particle.image.builder;

import com.orbitmines.spigot.api.handlers.particle.ParticleImage;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class Particle3DImageBuilder extends ParticleImageBuilder {

    /* z, string(x&y) */
    private String[][] strings;

    public Particle3DImageBuilder(String[]... strings) {
        this.strings = strings;
    }

    public ParticleImage.Pixel[][][] build() {
        int xL = -1;
        int yL = -1;

        /* Get max x and y bounds */
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings[i].length; j++) {
                if (xL == -1 || strings[i][j].length() > xL)
                    xL = strings[i][j].length();
            }

            if (yL == -1 || strings[i].length > yL)
                yL = strings[i].length;
        }

        if (xL == -1)
            throw new ArrayIndexOutOfBoundsException("Invalid X.");
        else if (yL == -1)
            throw new ArrayIndexOutOfBoundsException("Invalid Y.");

        /* Cube of particles from 3d image */
        ParticleImage.Pixel[][][] image = new ParticleImage.Pixel[xL][yL][strings.length];

        for (int z = 0; z < strings.length; z++) {
            for (int y = 0; y < strings[z].length; y++) {
                String string = strings[z][y];
                char[] chars = string.toCharArray();

                for (int x = 0; x < chars.length; x++) {
                    /* Image is given in order from top to bottom, so we flip the y around */
                    image[x][strings[z].length - 1 - y][z] = parse(chars[x]);
                }
            }
        }

        return image;
    }
}

