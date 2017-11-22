package com.orbitmines.spigot.api.handlers.particle.image;

import com.orbitmines.spigot.api.handlers.particle.ParticleImage;
import org.bukkit.Location;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public abstract class Particle3DImage extends ParticleImage {

    protected Pixel[][][][] pixels;

    private int index;

    public Particle3DImage(Location center, Pixel[][][]... pixels) {
        super(center);

        this.pixels = pixels;
        this.index = 0;
    }

    @Override
    public Pixel[][][] get3DImage() {
        index++;

        if (index == pixels.length)
            index = 0;

        return pixels[index];
    }
}
