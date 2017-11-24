package com.orbitmines.spigot.servers.survival.utils;

import com.orbitmines.spigot.api.handlers.itembuilders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Biome;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class BiomeUtils {

    public static ItemBuilder item(Biome biome) {
        switch (biome) {

            case PLAINS:
                return new ItemBuilder(Material.GRASS);
            case EXTREME_HILLS:
            case EXTREME_HILLS_WITH_TREES:
            case SMALLER_EXTREME_HILLS:
            case MUTATED_EXTREME_HILLS:
            case MUTATED_EXTREME_HILLS_WITH_TREES:
            case STONE_BEACH:
                return new ItemBuilder(Material.STONE);
            case FOREST:
            case FOREST_HILLS:
                return new ItemBuilder(Material.SAPLING);
            case SWAMPLAND:
            case MUTATED_SWAMPLAND:
                return new ItemBuilder(Material.VINE);
            case HELL:
                return new ItemBuilder(Material.NETHERRACK);
            case SKY:
                return new ItemBuilder(Material.SNOW_BLOCK);
            case ICE_FLATS:
            case ICE_MOUNTAINS:
            case MUTATED_ICE_FLATS:
                return new ItemBuilder(Material.PACKED_ICE);
            case MUSHROOM_ISLAND:
            case MUSHROOM_ISLAND_SHORE:
                return new ItemBuilder(Material.HUGE_MUSHROOM_2);
            case BEACHES:
            case DESERT:
            case DESERT_HILLS:
            case MUTATED_DESERT:
                return new ItemBuilder(Material.SAND);
            case COLD_BEACH:
            case FROZEN_OCEAN:
            case FROZEN_RIVER:
                return new ItemBuilder(Material.ICE);
            case JUNGLE:
            case JUNGLE_HILLS:
            case JUNGLE_EDGE:
            case MUTATED_JUNGLE:
            case MUTATED_JUNGLE_EDGE:
                return new ItemBuilder(Material.SAPLING, 1, 3);
            case OCEAN:
            case RIVER:
            case DEEP_OCEAN:
                return new ItemBuilder(Material.WATER_BUCKET);
            case BIRCH_FOREST:
            case BIRCH_FOREST_HILLS:
            case MUTATED_BIRCH_FOREST:
            case MUTATED_BIRCH_FOREST_HILLS:
                return new ItemBuilder(Material.SAPLING, 1, 2);
            case ROOFED_FOREST:
            case MUTATED_ROOFED_FOREST:
                return new ItemBuilder(Material.SAPLING, 1, 5);
            case TAIGA:
            case TAIGA_HILLS:
            case MUTATED_TAIGA:
            case TAIGA_COLD:
            case TAIGA_COLD_HILLS:
            case MUTATED_TAIGA_COLD:
                return new ItemBuilder(Material.SAPLING, 1, 1);
            case REDWOOD_TAIGA:
            case REDWOOD_TAIGA_HILLS:
            case MUTATED_REDWOOD_TAIGA:
            case MUTATED_REDWOOD_TAIGA_HILLS:
                return new ItemBuilder(Material.DIRT, 1, 2);
            case SAVANNA:
            case SAVANNA_ROCK:
            case MUTATED_SAVANNA:
            case MUTATED_SAVANNA_ROCK:
                return new ItemBuilder(Material.SAPLING, 1, 4);
            case MESA:
            case MESA_ROCK:
            case MESA_CLEAR_ROCK:
            case MUTATED_MESA:
            case MUTATED_MESA_ROCK:
            case MUTATED_MESA_CLEAR_ROCK:
                return new ItemBuilder(Material.STAINED_CLAY, 1, 4);
            case VOID:
                return new ItemBuilder(Material.BEDROCK);
            case MUTATED_FOREST:
            case MUTATED_PLAINS:
                return new ItemBuilder(Material.RED_ROSE);
            default:
                throw new IllegalStateException();
        }
    }

    public static String name(Biome biome) {
        switch (biome) {

            case OCEAN:
                return "§9Ocean";
            case PLAINS:
                return "§aPlains";
            case DESERT:
                return "§eDesert";
            case EXTREME_HILLS:
            case EXTREME_HILLS_WITH_TREES:
                return "§7Extreme Hills";
            case FOREST:
                return "§aForest";
            case TAIGA:
                return "§2Taiga";
            case SWAMPLAND:
            case MUTATED_SWAMPLAND:
                return "§8Swampland";
            case RIVER:
                return "§9River";
            case HELL:
                return "§cNether";
            case SKY:
                return "§fSky";
            case FROZEN_OCEAN:
                return "§bFrozen Ocean";
            case FROZEN_RIVER:
                return "§fFrozen River";
            case ICE_FLATS:
                return "§bIce Plains";
            case ICE_MOUNTAINS:
                return "§bCold Mountains";
            case MUSHROOM_ISLAND:
            case MUSHROOM_ISLAND_SHORE:
                return "§cMushroom Island";
            case BEACHES:
                return "§eBeach";
            case DESERT_HILLS:
                return "§eDesert Hills";
            case FOREST_HILLS:
                return "§aForest Hills";
            case TAIGA_HILLS:
                return "§2Taiga Hills";
            case SMALLER_EXTREME_HILLS:
                return "§7Mountains";
            case JUNGLE:
                return "§2Jungle";
            case JUNGLE_EDGE:
                return "§2Jungle Mountains";
            case JUNGLE_HILLS:
                return "§2Jungle Hills";
            case DEEP_OCEAN:
                return "§9Deep Ocean";
            case STONE_BEACH:
                return "§7Stone Beach";
            case COLD_BEACH:
                return "§bCold Beach";
            case BIRCH_FOREST:
                return "§fBirch Forest";
            case BIRCH_FOREST_HILLS:
                return "§fBirch Forest Hills";
            case ROOFED_FOREST:
                return "§2Roofed Forest";
            case TAIGA_COLD:
                return "§2Cold Taiga";
            case TAIGA_COLD_HILLS:
                return "§2Cold Taiga Hills";
            case REDWOOD_TAIGA:
            case MUTATED_REDWOOD_TAIGA:
                return "§2Mega Taiga";
            case REDWOOD_TAIGA_HILLS:
            case MUTATED_REDWOOD_TAIGA_HILLS:
                return "§2Mega Taiga Hills";
            case SAVANNA:
            case MUTATED_SAVANNA:
                return "§eSavanna";
            case SAVANNA_ROCK:
            case MUTATED_SAVANNA_ROCK:
                return "§eSavanna Mountains";
            case MESA:
            case MESA_ROCK:
            case MESA_CLEAR_ROCK:
            case MUTATED_MESA:
            case MUTATED_MESA_ROCK:
            case MUTATED_MESA_CLEAR_ROCK:
                return "§6Mesa";
            case VOID:
                return "§8Void";
            case MUTATED_PLAINS:
                return "§6Sunflower Plains";
            case MUTATED_DESERT:
                return "§eDesert Mountains";
            case MUTATED_EXTREME_HILLS:
            case MUTATED_EXTREME_HILLS_WITH_TREES:
                return "§7Extreme Mountain Hills";
            case MUTATED_FOREST:
                return "§cFlower Forest";
            case MUTATED_TAIGA:
                return "§2Mega Taiga";
            case MUTATED_ICE_FLATS:
                return "§bIce Spike Plains";
            case MUTATED_JUNGLE:
            case MUTATED_JUNGLE_EDGE:
                return "§2Jungle Mountains";
            case MUTATED_BIRCH_FOREST:
                return "§fBirch Forest Mountains";
            case MUTATED_BIRCH_FOREST_HILLS:
                return "§fBirch Forest Mountain Hills";
            case MUTATED_ROOFED_FOREST:
                return "§2Roofed Mountain Forest";
            case MUTATED_TAIGA_COLD:
                return "§2Cold Taiga Mountains";
            default:
                throw new IllegalStateException();
        }
    }
}
