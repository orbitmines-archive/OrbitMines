package com.orbitmines.spigot.api.handlers.worlds.flatgenerator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
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
    public ChunkData generateChunkData(World world, Random random, int xC, int zC, BiomeGrid biome) {
        ChunkData data = createChunkData(world);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                data.setBlock(x, 0, z, Material.BEDROCK);
                data.setBlock(x, 1, z, Material.DIRT);
                data.setBlock(x, 2, z, Material.DIRT);
                data.setBlock(x, 3, z, Material.GRASS_BLOCK);

                biome.setBiome(x, z, Biome.PLAINS);
            }
        }

        return data;
    }
}
