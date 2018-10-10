package com.orbitmines.spigot.servers.survival.handlers.region;

import com.orbitmines.spigot.OrbitMines;
import com.orbitmines.spigot.api.handlers.ConfigHandler;
import com.orbitmines.spigot.api.utils.BlockDataUtils;
import com.orbitmines.spigot.api.utils.ConsoleUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class RegionBuilder {

    private World world;
    private int id;

    private int inventoryX;
    private int inventoryY;

    private boolean underWaterRegion;

    private Location location;

    public RegionBuilder(World world, int id) {
        this.world = world;
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public int getInventoryX() {
        return inventoryX;
    }

    public int getInventoryY() {
        return inventoryY;
    }

    public boolean isUnderWaterRegion() {
        return underWaterRegion;
    }

    public Location getFixedSpawnLocation() {
        return location.clone().add(0, 1, 0);
    }

    public void build(List<Integer> generated) {
        RegionLocator locator = new RegionLocator(world, id, generated);
        locator.locate();
        int x = locator.getX();
        int y = locator.locateY();
        int z = locator.getZ();
        location = new Location(world, x + 0.5, y, z + 0.5, 0, 0);
        inventoryX = locator.getInventoryX();
        inventoryY = locator.getInventoryY();

        underWaterRegion = locator.isUnderWaterRegion();

        if (locator.isGenerated())
            return;

        ConsoleUtils.msg("Location found (Region #" + id + ")! (" + x + ", " + y + ", " + z + ") Preparing Chunk...");

        Chunk chunk = world.getChunkAt(location);
        if (chunk == null) {
            ConsoleUtils.warn("Error while detecting chunk at '" + world.getName() + "': " + x + ", " + y + ", " + z + ".");
            return;
        }
        chunk.load();

        ConsoleUtils.msg("Building Region SurvivalSpawn...");

        Material barrier = underWaterRegion ? Material.CONDUIT : Material.BARRIER;
        Material air = underWaterRegion ? Material.WATER : Material.AIR;
        Material lamp = underWaterRegion ? Material.SEA_LANTERN : Material.REDSTONE_LAMP;
        Material planks = underWaterRegion ? Material.DARK_PRISMARINE : Material.DARK_OAK_PLANKS;
        Material light_planks = underWaterRegion ? Material.PRISMARINE_BRICKS : Material.SPRUCE_PLANKS;
        BlockDataUtils.FenceType fenceType = underWaterRegion ? BlockDataUtils.FenceType.BIRCH : BlockDataUtils.FenceType.DARK_OAK;
        BlockDataUtils.StairsType stairsType = underWaterRegion ? BlockDataUtils.StairsType.DARK_PRISMARINE : BlockDataUtils.StairsType.DARK_OAK;

        /* Generated with SchematicGenerator */
        BlockDataUtils.setBlock(world, x + 1, y, z, lamp);
        BlockDataUtils.setBlock(world, x - 1, y, z, lamp);
        BlockDataUtils.setBlock(world, x, y, z - 1, lamp);
        BlockDataUtils.setBlock(world, x, y, z + 1, lamp);

        BlockDataUtils.setBlock(world, x - 3, y - 1, z - 2, planks);
        BlockDataUtils.setBlock(world, x - 3, y, z - 2, planks);
        BlockDataUtils.setFence(world, x - 3, y + 1, z - 2, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 3, y + 2, z - 2, air);
        BlockDataUtils.setBlock(world, x - 3, y + 3, z - 2, air);
        BlockDataUtils.setBlock(world, x - 3, y + 4, z - 2, air);
        BlockDataUtils.setStairs(world, x - 3, y - 2, z - 1, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.EAST, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 3, y - 1, z - 1, planks);
        BlockDataUtils.setBlock(world, x - 3, y, z - 1, planks);
        BlockDataUtils.setFence(world, x - 3, y + 1, z - 1, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 3, y + 2, z - 1, air);
        BlockDataUtils.setBlock(world, x - 3, y + 3, z - 1, air);
        BlockDataUtils.setBlock(world, x - 3, y + 4, z - 1, air);
        BlockDataUtils.setStairs(world, x - 3, y - 2, z, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.EAST, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 3, y - 1, z, planks);
        BlockDataUtils.setBlock(world, x - 3, y, z, light_planks);
        BlockDataUtils.setBlock(world, x - 3, y + 1, z, air);
        BlockDataUtils.setBlock(world, x - 3, y + 2, z, air);
        BlockDataUtils.setBlock(world, x - 3, y + 3, z, air);
        BlockDataUtils.setBlock(world, x - 3, y + 4, z, air);
        BlockDataUtils.setStairs(world, x - 3, y - 2, z + 1, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.EAST, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 3, y - 1, z + 1, planks);
        BlockDataUtils.setBlock(world, x - 3, y, z + 1, planks);
        BlockDataUtils.setFence(world, x - 3, y + 1, z + 1, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 3, y + 2, z + 1, air);
        BlockDataUtils.setBlock(world, x - 3, y + 3, z + 1, air);
        BlockDataUtils.setBlock(world, x - 3, y + 4, z + 1, air);
        BlockDataUtils.setBlock(world, x - 3, y - 1, z + 2, planks);
        BlockDataUtils.setBlock(world, x - 3, y, z + 2, planks);
        BlockDataUtils.setFence(world, x - 3, y + 1, z + 2, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 3, y + 2, z + 2, air);
        BlockDataUtils.setBlock(world, x - 3, y + 3, z + 2, air);
        BlockDataUtils.setBlock(world, x - 3, y + 4, z + 2, air);
        BlockDataUtils.setBlock(world, x - 2, y - 2, z - 3, air);
        BlockDataUtils.setBlock(world, x - 2, y - 1, z - 3, planks);
        BlockDataUtils.setBlock(world, x - 2, y, z - 3, planks);
        BlockDataUtils.setFence(world, x - 2, y + 1, z - 3, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 2, y + 2, z - 3, air);
        BlockDataUtils.setBlock(world, x - 2, y + 3, z - 3, air);
        BlockDataUtils.setBlock(world, x - 2, y + 4, z - 3, air);
        BlockDataUtils.setBlock(world, x - 2, y - 2, z - 2, planks);
        BlockDataUtils.setBlock(world, x - 2, y - 1, z - 2, planks);
        BlockDataUtils.setBlock(world, x - 2, y, z - 2, planks);
        BlockDataUtils.setFence(world, x - 2, y + 1, z - 2, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 2, y + 2, z - 2, air);
        BlockDataUtils.setBlock(world, x - 2, y + 3, z - 2, air);
        BlockDataUtils.setBlock(world, x - 2, y + 4, z - 2, air);
        BlockDataUtils.setBlock(world, x - 2, y - 3, z - 1, planks);
        BlockDataUtils.setBlock(world, x - 2, y - 2, z - 1, planks);
        BlockDataUtils.setBlock(world, x - 2, y - 1, z - 1, planks);
        BlockDataUtils.setBlock(world, x - 2, y, z - 1, light_planks);
        BlockDataUtils.setBlock(world, x - 2, y + 1, z - 1, air);
        BlockDataUtils.setBlock(world, x - 2, y + 2, z - 1, air);
        BlockDataUtils.setBlock(world, x - 2, y + 3, z - 1, air);
        BlockDataUtils.setBlock(world, x - 2, y + 4, z - 1, air);
        BlockDataUtils.setBlock(world, x - 2, y - 3, z, planks);
        BlockDataUtils.setBlock(world, x - 2, y - 2, z, planks);
        BlockDataUtils.setBlock(world, x - 2, y - 1, z, planks);
        BlockDataUtils.setBlock(world, x - 2, y, z, light_planks);
        BlockDataUtils.setBlock(world, x - 2, y + 1, z, air);
        BlockDataUtils.setBlock(world, x - 2, y + 2, z, air);
        BlockDataUtils.setBlock(world, x - 2, y + 3, z, air);
        BlockDataUtils.setBlock(world, x - 2, y + 4, z, air);
        BlockDataUtils.setBlock(world, x - 2, y - 3, z + 1, planks);
        BlockDataUtils.setBlock(world, x - 2, y - 2, z + 1, planks);
        BlockDataUtils.setBlock(world, x - 2, y - 1, z + 1, planks);
        BlockDataUtils.setBlock(world, x - 2, y, z + 1, light_planks);
        BlockDataUtils.setBlock(world, x - 2, y + 1, z + 1, air);
        BlockDataUtils.setBlock(world, x - 2, y + 2, z + 1, air);
        BlockDataUtils.setBlock(world, x - 2, y + 3, z + 1, air);
        BlockDataUtils.setBlock(world, x - 2, y + 4, z + 1, air);
        BlockDataUtils.setBlock(world, x - 2, y - 2, z + 2, planks);
        BlockDataUtils.setBlock(world, x - 2, y - 1, z + 2, planks);
        BlockDataUtils.setBlock(world, x - 2, y, z + 2, planks);
        BlockDataUtils.setFence(world, x - 2, y + 1, z + 2, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 2, y + 2, z + 2, air);
        BlockDataUtils.setBlock(world, x - 2, y + 3, z + 2, air);
        BlockDataUtils.setBlock(world, x - 2, y + 4, z + 2, air);
        BlockDataUtils.setBlock(world, x - 2, y - 1, z + 3, planks);
        BlockDataUtils.setBlock(world, x - 2, y, z + 3, planks);
        BlockDataUtils.setFence(world, x - 2, y + 1, z + 3, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 2, y + 2, z + 3, air);
        BlockDataUtils.setBlock(world, x - 2, y + 3, z + 3, air);
        BlockDataUtils.setBlock(world, x - 2, y + 4, z + 3, air);
        BlockDataUtils.setStairs(world, x - 1, y - 2, z - 3, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.SOUTH, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 1, y - 1, z - 3, planks);
        BlockDataUtils.setBlock(world, x - 1, y, z - 3, planks);
        BlockDataUtils.setFence(world, x - 1, y + 1, z - 3, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 1, y + 2, z - 3, air);
        BlockDataUtils.setBlock(world, x - 1, y + 3, z - 3, air);
        BlockDataUtils.setBlock(world, x - 1, y + 4, z - 3, air);
        BlockDataUtils.setBlock(world, x - 1, y - 3, z - 2, planks);
        BlockDataUtils.setBlock(world, x - 1, y - 2, z - 2, planks);
        BlockDataUtils.setBlock(world, x - 1, y - 1, z - 2, planks);
        BlockDataUtils.setBlock(world, x - 1, y, z - 2, light_planks);
        BlockDataUtils.setBlock(world, x - 1, y + 1, z - 2, air);
        BlockDataUtils.setBlock(world, x - 1, y + 2, z - 2, air);
        BlockDataUtils.setBlock(world, x - 1, y + 3, z - 2, air);
        BlockDataUtils.setBlock(world, x - 1, y + 4, z - 2, air);
        BlockDataUtils.setBlock(world, x - 1, y - 3, z - 1, planks);
        BlockDataUtils.setBlock(world, x - 1, y - 2, z - 1, Material.EMERALD_BLOCK);
        BlockDataUtils.setBlock(world, x - 1, y - 1, z - 1, planks);
        BlockDataUtils.setBlock(world, x - 1, y, z - 1, light_planks);
        BlockDataUtils.setBlock(world, x - 1, y + 1, z - 1, air);
        BlockDataUtils.setBlock(world, x - 1, y + 2, z - 1, air);
        BlockDataUtils.setBlock(world, x - 1, y + 3, z - 1, air);
        BlockDataUtils.setBlock(world, x - 1, y + 4, z - 1, air);
        BlockDataUtils.setBlock(world, x - 1, y - 3, z, planks);
        BlockDataUtils.setBlock(world, x - 1, y - 2, z, Material.EMERALD_BLOCK);
        BlockDataUtils.setBlock(world, x - 1, y - 1, z, Material.REDSTONE_BLOCK);
        BlockDataUtils.setBlock(world, x - 1, y + 1, z, air);
        BlockDataUtils.setBlock(world, x - 1, y + 2, z, air);
        BlockDataUtils.setBlock(world, x - 1, y + 3, z, air);
        BlockDataUtils.setBlock(world, x - 1, y - 3, z + 1, planks);
        BlockDataUtils.setBlock(world, x - 1, y - 2, z + 1, Material.EMERALD_BLOCK);
        BlockDataUtils.setBlock(world, x - 1, y - 1, z + 1, planks);
        BlockDataUtils.setBlock(world, x - 1, y, z + 1, light_planks);
        BlockDataUtils.setBlock(world, x - 1, y + 1, z + 1, air);
        BlockDataUtils.setBlock(world, x - 1, y + 2, z + 1, air);
        BlockDataUtils.setBlock(world, x - 1, y + 3, z + 1, air);
        BlockDataUtils.setBlock(world, x - 1, y + 4, z + 1, air);
        BlockDataUtils.setBlock(world, x - 1, y - 3, z + 2, planks);
        BlockDataUtils.setBlock(world, x - 1, y - 2, z + 2, planks);
        BlockDataUtils.setBlock(world, x - 1, y - 1, z + 2, planks);
        BlockDataUtils.setBlock(world, x - 1, y, z + 2, light_planks);
        BlockDataUtils.setBlock(world, x - 1, y + 1, z + 2, air);
        BlockDataUtils.setBlock(world, x - 1, y + 2, z + 2, air);
        BlockDataUtils.setBlock(world, x - 1, y + 3, z + 2, air);
        BlockDataUtils.setBlock(world, x - 1, y + 4, z + 2, air);
        BlockDataUtils.setStairs(world, x - 1, y - 2, z + 3, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.NORTH, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 1, y - 1, z + 3, planks);
        BlockDataUtils.setBlock(world, x - 1, y, z + 3, planks);
        BlockDataUtils.setFence(world, x - 1, y + 1, z + 3, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x - 1, y + 2, z + 3, air);
        BlockDataUtils.setBlock(world, x - 1, y + 3, z + 3, air);
        BlockDataUtils.setBlock(world, x - 1, y + 4, z + 3, air);
        BlockDataUtils.setStairs(world, x, y - 2, z - 3, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.SOUTH, underWaterRegion);
        BlockDataUtils.setBlock(world, x, y - 1, z - 3, planks);
        BlockDataUtils.setBlock(world, x, y, z - 3, light_planks);
        BlockDataUtils.setBlock(world, x, y + 1, z - 3, air);
        BlockDataUtils.setBlock(world, x, y + 2, z - 3, air);
        BlockDataUtils.setBlock(world, x, y + 3, z - 3, air);
        BlockDataUtils.setBlock(world, x, y + 4, z - 3, air);
        BlockDataUtils.setBlock(world, x, y - 3, z - 2, planks);
        BlockDataUtils.setBlock(world, x, y - 2, z - 2, planks);
        BlockDataUtils.setBlock(world, x, y - 1, z - 2, planks);
        BlockDataUtils.setBlock(world, x, y, z - 2, light_planks);
        BlockDataUtils.setBlock(world, x, y + 1, z - 2, air);
        BlockDataUtils.setBlock(world, x, y + 2, z - 2, air);
        BlockDataUtils.setBlock(world, x, y + 3, z - 2, air);
        BlockDataUtils.setBlock(world, x, y + 4, z - 2, air);
        BlockDataUtils.setBlock(world, x, y - 3, z - 1, planks);
        BlockDataUtils.setBlock(world, x, y - 2, z - 1, Material.EMERALD_BLOCK);
        BlockDataUtils.setBlock(world, x, y - 1, z - 1, Material.REDSTONE_BLOCK);
        BlockDataUtils.setBlock(world, x, y + 1, z - 1, air);
        BlockDataUtils.setBlock(world, x, y + 2, z - 1, air);
        BlockDataUtils.setBlock(world, x, y + 3, z - 1, air);
        BlockDataUtils.setBlock(world, x, y - 3, z, planks);
        BlockDataUtils.setBlock(world, x, y - 2, z, Material.EMERALD_BLOCK);
        BlockDataUtils.setBlock(world, x, y - 1, z, Material.BEACON);
        BlockDataUtils.setBlock(world, x, y, z, Material.LIME_STAINED_GLASS);
        BlockDataUtils.setBlock(world, x, y + 1, z, air);
        BlockDataUtils.setBlock(world, x, y + 2, z, air);
        BlockDataUtils.setBlock(world, x, y + 3, z, air);

        BlockDataUtils.setBlock(world, x, y + 3, z, barrier);

        BlockDataUtils.setBlock(world, x, y - 3, z + 1, planks);
        BlockDataUtils.setBlock(world, x, y - 2, z + 1, Material.EMERALD_BLOCK);
        BlockDataUtils.setBlock(world, x, y - 1, z + 1, Material.REDSTONE_BLOCK);
        BlockDataUtils.setBlock(world, x, y + 1, z + 1, air);
        BlockDataUtils.setBlock(world, x, y + 2, z + 1, air);
        BlockDataUtils.setBlock(world, x, y + 3, z + 1, air);
        BlockDataUtils.setBlock(world, x, y - 3, z + 2, planks);
        BlockDataUtils.setBlock(world, x, y - 2, z + 2, planks);
        BlockDataUtils.setBlock(world, x, y - 1, z + 2, planks);
        BlockDataUtils.setBlock(world, x, y, z + 2, light_planks);
        BlockDataUtils.setBlock(world, x, y + 1, z + 2, air);
        BlockDataUtils.setBlock(world, x, y + 2, z + 2, air);
        BlockDataUtils.setBlock(world, x, y + 3, z + 2, air);
        BlockDataUtils.setBlock(world, x, y + 4, z + 2, air);
        BlockDataUtils.setStairs(world, x, y - 2, z + 3, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.NORTH, underWaterRegion);
        BlockDataUtils.setBlock(world, x, y - 1, z + 3, planks);
        BlockDataUtils.setBlock(world, x, y, z + 3, light_planks);
        BlockDataUtils.setBlock(world, x, y + 1, z + 3, air);
        BlockDataUtils.setBlock(world, x, y + 2, z + 3, air);
        BlockDataUtils.setBlock(world, x, y + 3, z + 3, air);
        BlockDataUtils.setBlock(world, x, y + 4, z + 3, air);
        BlockDataUtils.setStairs(world, x + 1, y - 2, z - 3, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.SOUTH, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 1, y - 1, z - 3, planks);
        BlockDataUtils.setBlock(world, x + 1, y, z - 3, planks);
        BlockDataUtils.setFence(world, x + 1, y + 1, z - 3, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 1, y + 2, z - 3, air);
        BlockDataUtils.setBlock(world, x + 1, y + 3, z - 3, air);
        BlockDataUtils.setBlock(world, x + 1, y + 4, z - 3, air);
        BlockDataUtils.setBlock(world, x + 1, y - 3, z - 2, planks);
        BlockDataUtils.setBlock(world, x + 1, y - 2, z - 2, planks);
        BlockDataUtils.setBlock(world, x + 1, y - 1, z - 2, planks);
        BlockDataUtils.setBlock(world, x + 1, y, z - 2, light_planks);
        BlockDataUtils.setBlock(world, x + 1, y + 1, z - 2, air);
        BlockDataUtils.setBlock(world, x + 1, y + 2, z - 2, air);
        BlockDataUtils.setBlock(world, x + 1, y + 3, z - 2, air);
        BlockDataUtils.setBlock(world, x + 1, y + 4, z - 2, air);
        BlockDataUtils.setBlock(world, x + 1, y - 3, z - 1, planks);
        BlockDataUtils.setBlock(world, x + 1, y - 2, z - 1, Material.EMERALD_BLOCK);
        BlockDataUtils.setBlock(world, x + 1, y - 1, z - 1, planks);
        BlockDataUtils.setBlock(world, x + 1, y, z - 1, light_planks);
        BlockDataUtils.setBlock(world, x + 1, y + 1, z - 1, air);
        BlockDataUtils.setBlock(world, x + 1, y + 2, z - 1, air);
        BlockDataUtils.setBlock(world, x + 1, y + 3, z - 1, air);
        BlockDataUtils.setBlock(world, x + 1, y + 4, z - 1, air);
        BlockDataUtils.setBlock(world, x + 1, y - 3, z, planks);
        BlockDataUtils.setBlock(world, x + 1, y - 2, z, Material.EMERALD_BLOCK);
        BlockDataUtils.setBlock(world, x + 1, y - 1, z, Material.REDSTONE_BLOCK);
        BlockDataUtils.setBlock(world, x + 1, y + 1, z, air);
        BlockDataUtils.setBlock(world, x + 1, y + 2, z, air);
        BlockDataUtils.setBlock(world, x + 1, y + 3, z, air);
        BlockDataUtils.setBlock(world, x + 1, y - 3, z + 1, planks);
        BlockDataUtils.setBlock(world, x + 1, y - 2, z + 1, Material.EMERALD_BLOCK);
        BlockDataUtils.setBlock(world, x + 1, y - 1, z + 1, planks);
        BlockDataUtils.setBlock(world, x + 1, y, z + 1, light_planks);
        BlockDataUtils.setBlock(world, x + 1, y + 1, z + 1, air);
        BlockDataUtils.setBlock(world, x + 1, y + 2, z + 1, air);
        BlockDataUtils.setBlock(world, x + 1, y + 3, z + 1, air);
        BlockDataUtils.setBlock(world, x + 1, y + 4, z + 1, air);
        BlockDataUtils.setBlock(world, x + 1, y - 3, z + 2, planks);
        BlockDataUtils.setBlock(world, x + 1, y - 2, z + 2, planks);
        BlockDataUtils.setBlock(world, x + 1, y - 1, z + 2, planks);
        BlockDataUtils.setBlock(world, x + 1, y, z + 2, light_planks);
        BlockDataUtils.setBlock(world, x + 1, y + 1, z + 2, air);
        BlockDataUtils.setBlock(world, x + 1, y + 2, z + 2, air);
        BlockDataUtils.setBlock(world, x + 1, y + 3, z + 2, air);
        BlockDataUtils.setBlock(world, x + 1, y + 4, z + 2, air);
        BlockDataUtils.setStairs(world, x + 1, y - 2, z + 3, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.NORTH, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 1, y - 1, z + 3, planks);
        BlockDataUtils.setBlock(world, x + 1, y, z + 3, planks);
        BlockDataUtils.setFence(world, x + 1, y + 1, z + 3, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 1, y + 2, z + 3, air);
        BlockDataUtils.setBlock(world, x + 1, y + 3, z + 3, air);
        BlockDataUtils.setBlock(world, x + 1, y + 4, z + 3, air);
        BlockDataUtils.setBlock(world, x + 2, y - 1, z - 3, planks);
        BlockDataUtils.setBlock(world, x + 2, y, z - 3, planks);
        BlockDataUtils.setFence(world, x + 2, y + 1, z - 3, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 2, y + 2, z - 3, air);
        BlockDataUtils.setBlock(world, x + 2, y + 3, z - 3, air);
        BlockDataUtils.setBlock(world, x + 2, y + 4, z - 3, air);
        BlockDataUtils.setBlock(world, x + 2, y - 2, z - 2, planks);
        BlockDataUtils.setBlock(world, x + 2, y - 1, z - 2, planks);
        BlockDataUtils.setBlock(world, x + 2, y, z - 2, planks);
        BlockDataUtils.setFence(world, x + 2, y + 1, z - 2, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 2, y + 2, z - 2, air);
        BlockDataUtils.setBlock(world, x + 2, y + 3, z - 2, air);
        BlockDataUtils.setBlock(world, x + 2, y + 4, z - 2, air);
        BlockDataUtils.setBlock(world, x + 2, y - 3, z - 1, planks);
        BlockDataUtils.setBlock(world, x + 2, y - 2, z - 1, planks);
        BlockDataUtils.setBlock(world, x + 2, y - 1, z - 1, planks);
        BlockDataUtils.setBlock(world, x + 2, y, z - 1, light_planks);
        BlockDataUtils.setBlock(world, x + 2, y + 1, z - 1, air);
        BlockDataUtils.setBlock(world, x + 2, y + 2, z - 1, air);
        BlockDataUtils.setBlock(world, x + 2, y + 3, z - 1, air);
        BlockDataUtils.setBlock(world, x + 2, y + 4, z - 1, air);
        BlockDataUtils.setBlock(world, x + 2, y - 3, z, planks);
        BlockDataUtils.setBlock(world, x + 2, y - 2, z, planks);
        BlockDataUtils.setBlock(world, x + 2, y - 1, z, planks);
        BlockDataUtils.setBlock(world, x + 2, y, z, light_planks);
        BlockDataUtils.setBlock(world, x + 2, y + 1, z, air);
        BlockDataUtils.setBlock(world, x + 2, y + 2, z, air);
        BlockDataUtils.setBlock(world, x + 2, y + 3, z, air);
        BlockDataUtils.setBlock(world, x + 2, y + 4, z, air);
        BlockDataUtils.setBlock(world, x + 2, y - 3, z + 1, planks);
        BlockDataUtils.setBlock(world, x + 2, y - 2, z + 1, planks);
        BlockDataUtils.setBlock(world, x + 2, y - 1, z + 1, planks);
        BlockDataUtils.setBlock(world, x + 2, y, z + 1, light_planks);
        BlockDataUtils.setBlock(world, x + 2, y + 1, z + 1, air);
        BlockDataUtils.setBlock(world, x + 2, y + 2, z + 1, air);
        BlockDataUtils.setBlock(world, x + 2, y + 3, z + 1, air);
        BlockDataUtils.setBlock(world, x + 2, y + 4, z + 1, air);
        BlockDataUtils.setBlock(world, x + 2, y - 2, z + 2, planks);
        BlockDataUtils.setBlock(world, x + 2, y - 1, z + 2, planks);
        BlockDataUtils.setBlock(world, x + 2, y, z + 2, planks);
        BlockDataUtils.setFence(world, x + 2, y + 1, z + 2, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 2, y + 2, z + 2, air);
        BlockDataUtils.setBlock(world, x + 2, y + 3, z + 2, air);
        BlockDataUtils.setBlock(world, x + 2, y + 4, z + 2, air);
        BlockDataUtils.setBlock(world, x + 2, y - 1, z + 3, planks);
        BlockDataUtils.setBlock(world, x + 2, y, z + 3, planks);
        BlockDataUtils.setFence(world, x + 2, y + 1, z + 3, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 2, y + 2, z + 3, air);
        BlockDataUtils.setBlock(world, x + 2, y + 3, z + 3, air);
        BlockDataUtils.setBlock(world, x + 2, y + 4, z + 3, air);
        BlockDataUtils.setBlock(world, x + 3, y - 1, z - 2, planks);
        BlockDataUtils.setBlock(world, x + 3, y, z - 2, planks);
        BlockDataUtils.setFence(world, x + 3, y + 1, z - 2, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 3, y + 2, z - 2, air);
        BlockDataUtils.setBlock(world, x + 3, y + 3, z - 2, air);
        BlockDataUtils.setBlock(world, x + 3, y + 4, z - 2, air);
        BlockDataUtils.setStairs(world, x + 3, y - 2, z - 1, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.WEST, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 3, y - 1, z - 1, planks);
        BlockDataUtils.setBlock(world, x + 3, y, z - 1, planks);
        BlockDataUtils.setFence(world, x + 3, y + 1, z - 1, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 3, y + 2, z - 1, air);
        BlockDataUtils.setBlock(world, x + 3, y + 3, z - 1, air);
        BlockDataUtils.setBlock(world, x + 3, y + 4, z - 1, air);
        BlockDataUtils.setStairs(world, x + 3, y - 2, z, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.WEST, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 3, y - 1, z, planks);
        BlockDataUtils.setBlock(world, x + 3, y, z, light_planks);
        BlockDataUtils.setBlock(world, x + 3, y + 1, z, air);
        BlockDataUtils.setBlock(world, x + 3, y + 2, z, air);
        BlockDataUtils.setBlock(world, x + 3, y + 3, z, air);
        BlockDataUtils.setBlock(world, x + 3, y + 4, z, air);
        BlockDataUtils.setStairs(world, x + 3, y - 2, z + 1, stairsType, Stairs.Shape.STRAIGHT, Bisected.Half.TOP, BlockFace.WEST, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 3, y - 1, z + 1, planks);
        BlockDataUtils.setBlock(world, x + 3, y, z + 1, planks);
        BlockDataUtils.setFence(world, x + 3, y + 1, z + 1, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 3, y + 2, z + 1, air);
        BlockDataUtils.setBlock(world, x + 3, y + 3, z + 1, air);
        BlockDataUtils.setBlock(world, x + 3, y + 4, z + 1, air);
        BlockDataUtils.setBlock(world, x + 3, y - 1, z + 2, planks);
        BlockDataUtils.setBlock(world, x + 3, y, z + 2, planks);
        BlockDataUtils.setFence(world, x + 3, y + 1, z + 2, fenceType, underWaterRegion);
        BlockDataUtils.setBlock(world, x + 3, y + 2, z + 2, air);
        BlockDataUtils.setBlock(world, x + 3, y + 3, z + 2, air);
        BlockDataUtils.setBlock(world, x + 3, y + 4, z + 2, air);

        for (Block block : new Block[] { BlockDataUtils.setWallSign(world, x - 1, y + 3, z, BlockFace.WEST, underWaterRegion), BlockDataUtils.setWallSign(world, x, y + 3, z - 1, BlockFace.NORTH, underWaterRegion), BlockDataUtils.setWallSign(world, x, y + 3, z + 1, BlockFace.SOUTH, underWaterRegion), BlockDataUtils.setWallSign(world, x + 1, y + 3, z, BlockFace.EAST, underWaterRegion) }) {
            if (!(block.getState() instanceof Sign)) {
                ConsoleUtils.warn("");
                ConsoleUtils.warn("");
                ConsoleUtils.warn("CANNOT CREATE SIGN");
                ConsoleUtils.warn("");
                ConsoleUtils.warn("");
                continue;
            }

            Sign sign = (Sign) block.getState();
            sign.setLine(1, "§0§lRegion");
            sign.setLine(2, "§0§l[" + (id + 1) + "]");
            sign.update();
        }

        BlockDataUtils.update(world, x + 1, y, z);
        BlockDataUtils.update(world, x - 1, y, z);
        BlockDataUtils.update(world, x, y, z - 1);
        BlockDataUtils.update(world, x, y, z + 1);

        ConsoleUtils.success("Successfully created Region.");

        ConfigHandler configHandler = OrbitMines.getInstance().getConfigHandler();
        FileConfiguration configuration = configHandler.get("survival_regions");
        List<Integer> list = configuration.getIntegerList("regions");
        list.add(id);

        configuration.set("regions", list);
        configHandler.save("survival_regions");
    }
}
