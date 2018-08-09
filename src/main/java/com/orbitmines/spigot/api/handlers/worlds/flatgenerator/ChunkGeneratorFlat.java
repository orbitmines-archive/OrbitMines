package com.orbitmines.spigot.api.handlers.worlds.flatgenerator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class ChunkGeneratorFlat extends ChunkGenerator {

    public Location getFixedSpawnLocation(World w, Random r) {
        return new Location(w, 0, 4, 0);
    }

    public List<BlockPopulator> getWorldPopulators(World w) {
        return new ArrayList<>();
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData data = createChunkData(world);

//        biome.setBiome(x, z, Biome.PLAINS);

        data.setBlock(x << 4, 0, z << 4, Material.BEDROCK);
        data.setBlock(x << 4, 0, z << 4, Material.DIRT);
        data.setBlock(x << 4, 0, z << 4, Material.DIRT);
        data.setBlock(x << 4, 0, z << 4, Material.GRASS);

        return data;
    }
}
