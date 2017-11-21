package com.orbitmines.spigot.api.handlers.particle.image;

import com.madblock.spigot.api.handlers.particle.ParticleImage;
import org.bukkit.Location;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class Particle2DImage extends Particle3DImage {

    public Particle2DImage(Location center, Type type, Pixel[][]... pixels) {
        super(center);

        this.pixels = new Pixel[pixels.length][][][];

        /* Create 2D Images for different axises */
        switch (type) {

            case X_Y:
                for (int i = 0; i < pixels.length; i++) {
                    Pixel[][] p2d = pixels[i];

                    this.pixels[i] = new Pixel[p2d.length][][];

                    for (int x = 0; x < p2d.length; x++) {
                        Pixel[] p1d = p2d[x];

                        this.pixels[i][x] = new Pixel[p1d.length][1];

                        for (int y = 0; y < p1d.length; y++) {
                            this.pixels[i][x][y][0] = p1d[y];
                        }
                    }
                }
                break;
            case X_Z:
                for (int i = 0; i < pixels.length; i++) {
                    Pixel[][] p2d = pixels[i];

                    this.pixels[i] = new Pixel[p2d.length][1][];

                    for (int x = 0; x < p2d.length; x++) {
                        Pixel[] p1d = p2d[x];

                        this.pixels[i][x][0] = p1d;
                    }
                }
                break;
            case Y_Z:
                for (int i = 0; i < pixels.length; i++) {
                    this.pixels[i] = new ParticleImage.Pixel[1][][];
                    this.pixels[i][0] = pixels[i];
                }
                break;
        }
    }

    public enum Type {

        X_Y,
        X_Z,
        Y_Z

    }
}
