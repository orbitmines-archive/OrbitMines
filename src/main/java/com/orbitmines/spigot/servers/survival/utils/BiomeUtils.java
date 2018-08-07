package com.orbitmines.spigot.servers.survival.utils;

import com.orbitmines.api.Color;
import org.bukkit.Material;
import org.bukkit.block.Biome;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class BiomeUtils {

    public static boolean isOcean(Biome biome) {
        switch (biome) {
            case OCEAN:
            case DEEP_OCEAN:
            case WARM_OCEAN:
            case FROZEN_OCEAN:
            case LUKEWARM_OCEAN:
            case COLD_OCEAN:
            case DEEP_COLD_OCEAN:
            case DEEP_WARM_OCEAN:
            case DEEP_FROZEN_OCEAN:
            case DEEP_LUKEWARM_OCEAN:
                return true;
        }
        return false;
    }

    public static boolean isOceanNotFrozen(Biome biome) {
        switch (biome) {
            case OCEAN:
            case DEEP_OCEAN:
            case WARM_OCEAN:
            case LUKEWARM_OCEAN:
            case COLD_OCEAN:
            case DEEP_COLD_OCEAN:
            case DEEP_WARM_OCEAN:
            case DEEP_LUKEWARM_OCEAN:
                return true;
        }
        return false;
    }

    public static Material material(Biome biome) {
        switch (biome) {

            case RIVER:
                return Material.WATER_BUCKET;
            case OCEAN:
            case DEEP_OCEAN:
                return Material.SEAGRASS;
            case PLAINS:
                return Material.GRASS_BLOCK;
            case DESERT:
            case BEACH:
            case DESERT_HILLS:
            case DESERT_LAKES:
                return Material.SAND;
            case MOUNTAINS:
            case MOUNTAIN_EDGE:
            case STONE_SHORE:
                return Material.STONE;
            case FOREST:
            case WOODED_HILLS:
            case WOODED_MOUNTAINS:
                return Material.OAK_SAPLING;
            case TAIGA:
            case TAIGA_HILLS:
            case SNOWY_TAIGA:
            case SNOWY_TAIGA_HILLS:
            case GIANT_TREE_TAIGA:
            case GIANT_TREE_TAIGA_HILLS:
            case TAIGA_MOUNTAINS:
            case SNOWY_TAIGA_MOUNTAINS:
            case GIANT_SPRUCE_TAIGA:
            case GIANT_SPRUCE_TAIGA_HILLS:
                return Material.SPRUCE_SAPLING;
            case SWAMP:
            case SWAMP_HILLS:
                return Material.VINE;
            case NETHER:
                return Material.NETHERRACK;
            case THE_END:
            case SMALL_END_ISLANDS:
            case END_MIDLANDS:
            case END_HIGHLANDS:
            case END_BARRENS:
                return Material.END_STONE;
            case FROZEN_RIVER:
            case COLD_OCEAN:
            case DEEP_COLD_OCEAN:
                return Material.ICE;
            case SNOWY_TUNDRA:
            case SNOWY_MOUNTAINS:
            case SNOWY_BEACH:
                return Material.SNOW_BLOCK;
            case MUSHROOM_FIELDS:
            case MUSHROOM_FIELD_SHORE:
                return Material.RED_MUSHROOM_BLOCK;
            case JUNGLE:
            case JUNGLE_HILLS:
            case JUNGLE_EDGE:
            case MODIFIED_JUNGLE:
            case MODIFIED_JUNGLE_EDGE:
                return Material.JUNGLE_SAPLING;
            case BIRCH_FOREST:
            case BIRCH_FOREST_HILLS:
            case TALL_BIRCH_FOREST:
            case TALL_BIRCH_HILLS:
                return Material.BIRCH_SAPLING;
            case DARK_FOREST:
            case DARK_FOREST_HILLS:
                return Material.DARK_OAK_SAPLING;
            case SAVANNA:
            case SAVANNA_PLATEAU:
            case SHATTERED_SAVANNA:
            case SHATTERED_SAVANNA_PLATEAU:
                return Material.ACACIA_SAPLING;
            case BADLANDS:
            case WOODED_BADLANDS_PLATEAU:
            case BADLANDS_PLATEAU:
            case ERODED_BADLANDS:
            case MODIFIED_BADLANDS_PLATEAU:
            case MODIFIED_WOODED_BADLANDS_PLATEAU:
                return Material.YELLOW_TERRACOTTA;
            case WARM_OCEAN:
            case LUKEWARM_OCEAN:
            case DEEP_WARM_OCEAN:
            case DEEP_LUKEWARM_OCEAN:
                return Material.HORN_CORAL;
            case THE_VOID:
                return Material.BEDROCK;
            case SUNFLOWER_PLAINS:
                return Material.SUNFLOWER;
            case GRAVELLY_MOUNTAINS:
            case MODIFIED_GRAVELLY_MOUNTAINS:
                return Material.GRAVEL;
            case FLOWER_FOREST:
                return Material.ROSE_BUSH;
            case ICE_SPIKES:
            case FROZEN_OCEAN:
            case DEEP_FROZEN_OCEAN:
                return Material.PACKED_ICE;
        }
        throw new IllegalArgumentException();
    }

    public static String name(Biome biome) {
        StringBuilder stringBuilder = new StringBuilder();

        String[] parts = biome.toString().split("_");
        for (int i = 0; i < parts.length; i++) {
            if (i != 0)
                stringBuilder.append(" ");

            stringBuilder.append(parts[i].substring(0, 1).toUpperCase());
            stringBuilder.append(parts[i].substring(1).toLowerCase());
        }

        return color(biome).getChatColor() + stringBuilder.toString();
    }

    private static Color color(Biome biome) {
        switch (biome) {

            case OCEAN:
            case RIVER:
            case DEEP_OCEAN:
                return Color.BLUE;
            case PLAINS:
                return Color.LIME;
            case DESERT:
            case BEACH:
            case DESERT_HILLS:
            case DESERT_LAKES:
                return Color.YELLOW;
            case MOUNTAINS:
            case MOUNTAIN_EDGE:
            case STONE_SHORE:
                return Color.SILVER;
            case FOREST:
            case WOODED_HILLS:
            case WOODED_MOUNTAINS:
                return Color.LIME;
            case TAIGA:
            case TAIGA_HILLS:
            case SNOWY_TAIGA:
            case SNOWY_TAIGA_HILLS:
            case GIANT_TREE_TAIGA:
            case GIANT_TREE_TAIGA_HILLS:
            case TAIGA_MOUNTAINS:
            case SNOWY_TAIGA_MOUNTAINS:
            case GIANT_SPRUCE_TAIGA:
            case GIANT_SPRUCE_TAIGA_HILLS:
                return Color.GREEN;
            case SWAMP:
            case SWAMP_HILLS:
                return Color.GREEN;
            case NETHER:
                return Color.RED;
            case THE_END:
            case SMALL_END_ISLANDS:
            case END_MIDLANDS:
            case END_HIGHLANDS:
            case END_BARRENS:
                return Color.YELLOW;
            case FROZEN_OCEAN:
            case FROZEN_RIVER:
            case COLD_OCEAN:
            case DEEP_COLD_OCEAN:
            case DEEP_FROZEN_OCEAN:
                return Color.AQUA;
            case SNOWY_TUNDRA:
            case SNOWY_MOUNTAINS:
            case SNOWY_BEACH:
                return Color.WHITE;
            case MUSHROOM_FIELDS:
            case MUSHROOM_FIELD_SHORE:
                return Color.RED;
            case JUNGLE:
            case JUNGLE_HILLS:
            case JUNGLE_EDGE:
            case MODIFIED_JUNGLE:
            case MODIFIED_JUNGLE_EDGE:
                return Color.GREEN;
            case BIRCH_FOREST:
            case BIRCH_FOREST_HILLS:
            case TALL_BIRCH_FOREST:
            case TALL_BIRCH_HILLS:
                return Color.WHITE;
            case DARK_FOREST:
            case DARK_FOREST_HILLS:
                return Color.GREEN;
            case SAVANNA:
            case SAVANNA_PLATEAU:
            case SHATTERED_SAVANNA:
            case SHATTERED_SAVANNA_PLATEAU:
                return Color.ORANGE;
            case BADLANDS:
            case WOODED_BADLANDS_PLATEAU:
            case BADLANDS_PLATEAU:
            case ERODED_BADLANDS:
            case MODIFIED_BADLANDS_PLATEAU:
            case MODIFIED_WOODED_BADLANDS_PLATEAU:
                return Color.ORANGE;
            case WARM_OCEAN:
            case LUKEWARM_OCEAN:
            case DEEP_WARM_OCEAN:
            case DEEP_LUKEWARM_OCEAN:
                return Color.BLUE;
            case THE_VOID:
                return Color.GRAY;
            case SUNFLOWER_PLAINS:
                return Color.YELLOW;
            case GRAVELLY_MOUNTAINS:
            case MODIFIED_GRAVELLY_MOUNTAINS:
                return Color.SILVER;
            case FLOWER_FOREST:
                return Color.RED;
            case ICE_SPIKES:
                return Color.AQUA;
        }
        throw new IllegalArgumentException();
    }
}
