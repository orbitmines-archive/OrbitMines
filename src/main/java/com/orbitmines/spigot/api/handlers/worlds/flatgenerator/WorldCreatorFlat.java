package com.orbitmines.spigot.api.handlers.worlds.flatgenerator;

import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class WorldCreatorFlat extends WorldCreator {

    private ChunkGenerator chunkGenerator = new ChunkGeneratorFlat();

    public WorldCreatorFlat(String name) {
        super(name);
    }

    @Override
    public ChunkGenerator generator() {
        return chunkGenerator;
    }
}
