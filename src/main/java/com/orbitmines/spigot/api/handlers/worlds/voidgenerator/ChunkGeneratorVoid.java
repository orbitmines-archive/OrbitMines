package com.orbitmines.spigot.api.handlers.worlds.voidgenerator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ChunkGeneratorVoid extends ChunkGenerator {

    public Location getFixedSpawnLocation(World w, Random r) {
        return new Location(w, 0, 70, 0);
    }

    public List<BlockPopulator> getWorldPopulators(World w) {
        return new ArrayList<>();
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        return createChunkData(world);
    }
}
