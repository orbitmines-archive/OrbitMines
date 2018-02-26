package com.orbitmines.spigot.servers.survival.handlers.region;

import com.orbitmines.spigot.api.utils.ConsoleUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class RegionBuilder {


    private World world;
    private int id;

    private int inventoryX;
    private int inventoryY;

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

    public Location getFixedSpawnLocation() {
        return location.clone().add(0, 1, 0);
    }

    public void build() {
        ConsoleUtils.msg("Locating Region '" + id + "' for world " + world.getName() + "...");
        RegionLocator locator = new RegionLocator(world, id);
        locator.locate();
        int x = locator.getX();
        int y = locator.locateY();
        int z = locator.getZ();
        location = new Location(world, x + 0.5, locator.isGenerated() ? y - 3 : y, z + 0.5, 0, 0);
        inventoryX = locator.getInventoryX();
        inventoryY = locator.getInventotyY();

        if (locator.isGenerated()) {
            ConsoleUtils.msg("Already generated, moving on.");
            return;
        }

        ConsoleUtils.msg("Location found! (" + x + ", " + y + ", " + z + ") Preparing Chunk...");

        Chunk chunk = world.getChunkAt(location);
        if (chunk == null) {
            ConsoleUtils.warn("Error while detecting chunk at '" + world.getName() + "': " + x + ", " + y + ", " + z + ".");
            return;
        }
        chunk.load();

        ConsoleUtils.msg("Building Region Spawn...");

        /* Generated with SchematicGenerator */
        setBlock(x - 3, y - 1, z - 2, Material.WOOD, 5);
        setBlock(x - 3, y, z - 2, Material.WOOD, 5);
        setBlock(x - 3, y + 1, z - 2, Material.DARK_OAK_FENCE, 0);
        setBlock(x - 3, y + 2, z - 2, Material.AIR, 0);
        setBlock(x - 3, y + 3, z - 2, Material.AIR, 0);
        setBlock(x - 3, y + 4, z - 2, Material.AIR, 0);
        setBlock(x - 3, y - 2, z - 1, Material.DARK_OAK_STAIRS, 4);
        setBlock(x - 3, y - 1, z - 1, Material.WOOD, 5);
        setBlock(x - 3, y, z - 1, Material.WOOD, 5);
        setBlock(x - 3, y + 1, z - 1, Material.DARK_OAK_FENCE, 0);
        setBlock(x - 3, y + 2, z - 1, Material.AIR, 0);
        setBlock(x - 3, y + 3, z - 1, Material.AIR, 0);
        setBlock(x - 3, y + 4, z - 1, Material.AIR, 0);
        setBlock(x - 3, y - 2, z, Material.DARK_OAK_STAIRS, 4);
        setBlock(x - 3, y - 1, z, Material.WOOD, 5);
        setBlock(x - 3, y, z, Material.WOOD, 1);
        setBlock(x - 3, y + 1, z, Material.AIR, 0);
        setBlock(x - 3, y + 2, z, Material.AIR, 0);
        setBlock(x - 3, y + 3, z, Material.AIR, 0);
        setBlock(x - 3, y + 4, z, Material.AIR, 0);
        setBlock(x - 3, y - 2, z + 1, Material.DARK_OAK_STAIRS, 4);
        setBlock(x - 3, y - 1, z + 1, Material.WOOD, 5);
        setBlock(x - 3, y, z + 1, Material.WOOD, 5);
        setBlock(x - 3, y + 1, z + 1, Material.DARK_OAK_FENCE, 0);
        setBlock(x - 3, y + 2, z + 1, Material.AIR, 0);
        setBlock(x - 3, y + 3, z + 1, Material.AIR, 0);
        setBlock(x - 3, y + 4, z + 1, Material.AIR, 0);
        setBlock(x - 3, y - 1, z + 2, Material.WOOD, 5);
        setBlock(x - 3, y, z + 2, Material.WOOD, 5);
        setBlock(x - 3, y + 1, z + 2, Material.DARK_OAK_FENCE, 0);
        setBlock(x - 3, y + 2, z + 2, Material.AIR, 0);
        setBlock(x - 3, y + 3, z + 2, Material.AIR, 0);
        setBlock(x - 3, y + 4, z + 2, Material.AIR, 0);
        setBlock(x - 2, y - 2, z - 3, Material.AIR, 0);
        setBlock(x - 2, y - 1, z - 3, Material.WOOD, 5);
        setBlock(x - 2, y, z - 3, Material.WOOD, 5);
        setBlock(x - 2, y + 1, z - 3, Material.DARK_OAK_FENCE, 0);
        setBlock(x - 2, y + 2, z - 3, Material.AIR, 0);
        setBlock(x - 2, y + 3, z - 3, Material.AIR, 0);
        setBlock(x - 2, y + 4, z - 3, Material.AIR, 0);
        setBlock(x - 2, y - 2, z - 2, Material.WOOD, 5);
        setBlock(x - 2, y - 1, z - 2, Material.WOOD, 5);
        setBlock(x - 2, y, z - 2, Material.WOOD, 5);
        setBlock(x - 2, y + 1, z - 2, Material.DARK_OAK_FENCE, 0);
        setBlock(x - 2, y + 2, z - 2, Material.AIR, 0);
        setBlock(x - 2, y + 3, z - 2, Material.AIR, 0);
        setBlock(x - 2, y + 4, z - 2, Material.AIR, 0);
        setBlock(x - 2, y - 3, z - 1, Material.WOOD, 5);
        setBlock(x - 2, y - 2, z - 1, Material.WOOD, 5);
        setBlock(x - 2, y - 1, z - 1, Material.WOOD, 5);
        setBlock(x - 2, y, z - 1, Material.WOOD, 1);
        setBlock(x - 2, y + 1, z - 1, Material.AIR, 0);
        setBlock(x - 2, y + 2, z - 1, Material.AIR, 0);
        setBlock(x - 2, y + 3, z - 1, Material.AIR, 0);
        setBlock(x - 2, y + 4, z - 1, Material.AIR, 0);
        setBlock(x - 2, y - 3, z, Material.WOOD, 5);
        setBlock(x - 2, y - 2, z, Material.WOOD, 5);
        setBlock(x - 2, y - 1, z, Material.WOOD, 5);
        setBlock(x - 2, y, z, Material.WOOD, 1);
        setBlock(x - 2, y + 1, z, Material.AIR, 0);
        setBlock(x - 2, y + 2, z, Material.AIR, 0);
        setBlock(x - 2, y + 3, z, Material.AIR, 0);
        setBlock(x - 2, y + 4, z, Material.AIR, 0);
        setBlock(x - 2, y - 3, z + 1, Material.WOOD, 5);
        setBlock(x - 2, y - 2, z + 1, Material.WOOD, 5);
        setBlock(x - 2, y - 1, z + 1, Material.WOOD, 5);
        setBlock(x - 2, y, z + 1, Material.WOOD, 1);
        setBlock(x - 2, y + 1, z + 1, Material.AIR, 0);
        setBlock(x - 2, y + 2, z + 1, Material.AIR, 0);
        setBlock(x - 2, y + 3, z + 1, Material.AIR, 0);
        setBlock(x - 2, y + 4, z + 1, Material.AIR, 0);
        setBlock(x - 2, y - 2, z + 2, Material.WOOD, 5);
        setBlock(x - 2, y - 1, z + 2, Material.WOOD, 5);
        setBlock(x - 2, y, z + 2, Material.WOOD, 5);
        setBlock(x - 2, y + 1, z + 2, Material.DARK_OAK_FENCE, 0);
        setBlock(x - 2, y + 2, z + 2, Material.AIR, 0);
        setBlock(x - 2, y + 3, z + 2, Material.AIR, 0);
        setBlock(x - 2, y + 4, z + 2, Material.AIR, 0);
        setBlock(x - 2, y - 1, z + 3, Material.WOOD, 5);
        setBlock(x - 2, y, z + 3, Material.WOOD, 5);
        setBlock(x - 2, y + 1, z + 3, Material.DARK_OAK_FENCE, 0);
        setBlock(x - 2, y + 2, z + 3, Material.AIR, 0);
        setBlock(x - 2, y + 3, z + 3, Material.AIR, 0);
        setBlock(x - 2, y + 4, z + 3, Material.AIR, 0);
        setBlock(x - 1, y - 2, z - 3, Material.DARK_OAK_STAIRS, 6);
        setBlock(x - 1, y - 1, z - 3, Material.WOOD, 5);
        setBlock(x - 1, y, z - 3, Material.WOOD, 5);
        setBlock(x - 1, y + 1, z - 3, Material.DARK_OAK_FENCE, 0);
        setBlock(x - 1, y + 2, z - 3, Material.AIR, 0);
        setBlock(x - 1, y + 3, z - 3, Material.AIR, 0);
        setBlock(x - 1, y + 4, z - 3, Material.AIR, 0);
        setBlock(x - 1, y - 3, z - 2, Material.WOOD, 5);
        setBlock(x - 1, y - 2, z - 2, Material.WOOD, 5);
        setBlock(x - 1, y - 1, z - 2, Material.WOOD, 5);
        setBlock(x - 1, y, z - 2, Material.WOOD, 1);
        setBlock(x - 1, y + 1, z - 2, Material.AIR, 0);
        setBlock(x - 1, y + 2, z - 2, Material.AIR, 0);
        setBlock(x - 1, y + 3, z - 2, Material.AIR, 0);
        setBlock(x - 1, y + 4, z - 2, Material.AIR, 0);
        setBlock(x - 1, y - 3, z - 1, Material.WOOD, 5);
        setBlock(x - 1, y - 2, z - 1, Material.EMERALD_BLOCK, 0);
        setBlock(x - 1, y - 1, z - 1, Material.WOOD, 5);
        setBlock(x - 1, y, z - 1, Material.WOOD, 1);
        setBlock(x - 1, y + 1, z - 1, Material.AIR, 0);
        setBlock(x - 1, y + 2, z - 1, Material.AIR, 0);
        setBlock(x - 1, y + 3, z - 1, Material.AIR, 0);
        setBlock(x - 1, y + 4, z - 1, Material.AIR, 0);
        setBlock(x - 1, y - 3, z, Material.WOOD, 5);
        setBlock(x - 1, y - 2, z, Material.EMERALD_BLOCK, 0);
        setBlock(x - 1, y - 1, z, Material.REDSTONE_BLOCK, 0);
        setBlock(x - 1, y, z, Material.REDSTONE_LAMP_ON, 0);
        setBlock(x - 1, y + 1, z, Material.AIR, 0);
        setBlock(x - 1, y + 2, z, Material.AIR, 0);
        setBlock(x - 1, y + 3, z, Material.AIR, 0);
        setBlock(x - 1, y - 3, z + 1, Material.WOOD, 5);
        setBlock(x - 1, y - 2, z + 1, Material.EMERALD_BLOCK, 0);
        setBlock(x - 1, y - 1, z + 1, Material.WOOD, 5);
        setBlock(x - 1, y, z + 1, Material.WOOD, 1);
        setBlock(x - 1, y + 1, z + 1, Material.AIR, 0);
        setBlock(x - 1, y + 2, z + 1, Material.AIR, 0);
        setBlock(x - 1, y + 3, z + 1, Material.AIR, 0);
        setBlock(x - 1, y + 4, z + 1, Material.AIR, 0);
        setBlock(x - 1, y - 3, z + 2, Material.WOOD, 5);
        setBlock(x - 1, y - 2, z + 2, Material.WOOD, 5);
        setBlock(x - 1, y - 1, z + 2, Material.WOOD, 5);
        setBlock(x - 1, y, z + 2, Material.WOOD, 1);
        setBlock(x - 1, y + 1, z + 2, Material.AIR, 0);
        setBlock(x - 1, y + 2, z + 2, Material.AIR, 0);
        setBlock(x - 1, y + 3, z + 2, Material.AIR, 0);
        setBlock(x - 1, y + 4, z + 2, Material.AIR, 0);
        setBlock(x - 1, y - 2, z + 3, Material.DARK_OAK_STAIRS, 7);
        setBlock(x - 1, y - 1, z + 3, Material.WOOD, 5);
        setBlock(x - 1, y, z + 3, Material.WOOD, 5);
        setBlock(x - 1, y + 1, z + 3, Material.DARK_OAK_FENCE, 0);
        setBlock(x - 1, y + 2, z + 3, Material.AIR, 0);
        setBlock(x - 1, y + 3, z + 3, Material.AIR, 0);
        setBlock(x - 1, y + 4, z + 3, Material.AIR, 0);
        setBlock(x, y - 2, z - 3, Material.DARK_OAK_STAIRS, 6);
        setBlock(x, y - 1, z - 3, Material.WOOD, 5);
        setBlock(x, y, z - 3, Material.WOOD, 1);
        setBlock(x, y + 1, z - 3, Material.AIR, 0);
        setBlock(x, y + 2, z - 3, Material.AIR, 0);
        setBlock(x, y + 3, z - 3, Material.AIR, 0);
        setBlock(x, y + 4, z - 3, Material.AIR, 0);
        setBlock(x, y - 3, z - 2, Material.WOOD, 5);
        setBlock(x, y - 2, z - 2, Material.WOOD, 5);
        setBlock(x, y - 1, z - 2, Material.WOOD, 5);
        setBlock(x, y, z - 2, Material.WOOD, 1);
        setBlock(x, y + 1, z - 2, Material.AIR, 0);
        setBlock(x, y + 2, z - 2, Material.AIR, 0);
        setBlock(x, y + 3, z - 2, Material.AIR, 0);
        setBlock(x, y + 4, z - 2, Material.AIR, 0);
        setBlock(x, y - 3, z - 1, Material.WOOD, 5);
        setBlock(x, y - 2, z - 1, Material.EMERALD_BLOCK, 0);
        setBlock(x, y - 1, z - 1, Material.REDSTONE_BLOCK, 0);
        setBlock(x, y, z - 1, Material.REDSTONE_LAMP_ON, 0);
        setBlock(x, y + 1, z - 1, Material.AIR, 0);
        setBlock(x, y + 2, z - 1, Material.AIR, 0);
        setBlock(x, y + 3, z - 1, Material.AIR, 0);
        setBlock(x, y - 3, z, Material.WOOD, 5);
        setBlock(x, y - 2, z, Material.EMERALD_BLOCK, 0);
        setBlock(x, y - 1, z, Material.BEACON, 0);
        setBlock(x, y, z, Material.STAINED_GLASS, 5);
        setBlock(x, y + 1, z, Material.AIR, 0);
        setBlock(x, y + 2, z, Material.AIR, 0);
        setBlock(x, y + 3, z, Material.AIR, 0);
        setBlock(x, y + 3, z, Material.BARRIER, 0);
        setBlock(x, y - 3, z + 1, Material.WOOD, 5);
        setBlock(x, y - 2, z + 1, Material.EMERALD_BLOCK, 0);
        setBlock(x, y - 1, z + 1, Material.REDSTONE_BLOCK, 0);
        setBlock(x, y, z + 1, Material.REDSTONE_LAMP_ON, 0);
        setBlock(x, y + 1, z + 1, Material.AIR, 0);
        setBlock(x, y + 2, z + 1, Material.AIR, 0);
        setBlock(x, y + 3, z + 1, Material.AIR, 0);
        setBlock(x, y - 3, z + 2, Material.WOOD, 5);
        setBlock(x, y - 2, z + 2, Material.WOOD, 5);
        setBlock(x, y - 1, z + 2, Material.WOOD, 5);
        setBlock(x, y, z + 2, Material.WOOD, 1);
        setBlock(x, y + 1, z + 2, Material.AIR, 0);
        setBlock(x, y + 2, z + 2, Material.AIR, 0);
        setBlock(x, y + 3, z + 2, Material.AIR, 0);
        setBlock(x, y + 4, z + 2, Material.AIR, 0);
        setBlock(x, y - 2, z + 3, Material.DARK_OAK_STAIRS, 7);
        setBlock(x, y - 1, z + 3, Material.WOOD, 5);
        setBlock(x, y, z + 3, Material.WOOD, 1);
        setBlock(x, y + 1, z + 3, Material.AIR, 0);
        setBlock(x, y + 2, z + 3, Material.AIR, 0);
        setBlock(x, y + 3, z + 3, Material.AIR, 0);
        setBlock(x, y + 4, z + 3, Material.AIR, 0);
        setBlock(x + 1, y - 2, z - 3, Material.DARK_OAK_STAIRS, 6);
        setBlock(x + 1, y - 1, z - 3, Material.WOOD, 5);
        setBlock(x + 1, y, z - 3, Material.WOOD, 5);
        setBlock(x + 1, y + 1, z - 3, Material.DARK_OAK_FENCE, 0);
        setBlock(x + 1, y + 2, z - 3, Material.AIR, 0);
        setBlock(x + 1, y + 3, z - 3, Material.AIR, 0);
        setBlock(x + 1, y + 4, z - 3, Material.AIR, 0);
        setBlock(x + 1, y - 3, z - 2, Material.WOOD, 5);
        setBlock(x + 1, y - 2, z - 2, Material.WOOD, 5);
        setBlock(x + 1, y - 1, z - 2, Material.WOOD, 5);
        setBlock(x + 1, y, z - 2, Material.WOOD, 1);
        setBlock(x + 1, y + 1, z - 2, Material.AIR, 0);
        setBlock(x + 1, y + 2, z - 2, Material.AIR, 0);
        setBlock(x + 1, y + 3, z - 2, Material.AIR, 0);
        setBlock(x + 1, y + 4, z - 2, Material.AIR, 0);
        setBlock(x + 1, y - 3, z - 1, Material.WOOD, 5);
        setBlock(x + 1, y - 2, z - 1, Material.EMERALD_BLOCK, 0);
        setBlock(x + 1, y - 1, z - 1, Material.WOOD, 5);
        setBlock(x + 1, y, z - 1, Material.WOOD, 1);
        setBlock(x + 1, y + 1, z - 1, Material.AIR, 0);
        setBlock(x + 1, y + 2, z - 1, Material.AIR, 0);
        setBlock(x + 1, y + 3, z - 1, Material.AIR, 0);
        setBlock(x + 1, y + 4, z - 1, Material.AIR, 0);
        setBlock(x + 1, y - 3, z, Material.WOOD, 5);
        setBlock(x + 1, y - 2, z, Material.EMERALD_BLOCK, 0);
        setBlock(x + 1, y - 1, z, Material.REDSTONE_BLOCK, 0);
        setBlock(x + 1, y, z, Material.REDSTONE_LAMP_ON, 0);
        setBlock(x + 1, y + 1, z, Material.AIR, 0);
        setBlock(x + 1, y + 2, z, Material.AIR, 0);
        setBlock(x + 1, y + 3, z, Material.AIR, 0);
        setBlock(x + 1, y - 3, z + 1, Material.WOOD, 5);
        setBlock(x + 1, y - 2, z + 1, Material.EMERALD_BLOCK, 0);
        setBlock(x + 1, y - 1, z + 1, Material.WOOD, 5);
        setBlock(x + 1, y, z + 1, Material.WOOD, 1);
        setBlock(x + 1, y + 1, z + 1, Material.AIR, 0);
        setBlock(x + 1, y + 2, z + 1, Material.AIR, 0);
        setBlock(x + 1, y + 3, z + 1, Material.AIR, 0);
        setBlock(x + 1, y + 4, z + 1, Material.AIR, 0);
        setBlock(x + 1, y - 3, z + 2, Material.WOOD, 5);
        setBlock(x + 1, y - 2, z + 2, Material.WOOD, 5);
        setBlock(x + 1, y - 1, z + 2, Material.WOOD, 5);
        setBlock(x + 1, y, z + 2, Material.WOOD, 1);
        setBlock(x + 1, y + 1, z + 2, Material.AIR, 0);
        setBlock(x + 1, y + 2, z + 2, Material.AIR, 0);
        setBlock(x + 1, y + 3, z + 2, Material.AIR, 0);
        setBlock(x + 1, y + 4, z + 2, Material.AIR, 0);
        setBlock(x + 1, y - 2, z + 3, Material.DARK_OAK_STAIRS, 7);
        setBlock(x + 1, y - 1, z + 3, Material.WOOD, 5);
        setBlock(x + 1, y, z + 3, Material.WOOD, 5);
        setBlock(x + 1, y + 1, z + 3, Material.DARK_OAK_FENCE, 0);
        setBlock(x + 1, y + 2, z + 3, Material.AIR, 0);
        setBlock(x + 1, y + 3, z + 3, Material.AIR, 0);
        setBlock(x + 1, y + 4, z + 3, Material.AIR, 0);
        setBlock(x + 2, y - 1, z - 3, Material.WOOD, 5);
        setBlock(x + 2, y, z - 3, Material.WOOD, 5);
        setBlock(x + 2, y + 1, z - 3, Material.DARK_OAK_FENCE, 0);
        setBlock(x + 2, y + 2, z - 3, Material.AIR, 0);
        setBlock(x + 2, y + 3, z - 3, Material.AIR, 0);
        setBlock(x + 2, y + 4, z - 3, Material.AIR, 0);
        setBlock(x + 2, y - 2, z - 2, Material.WOOD, 5);
        setBlock(x + 2, y - 1, z - 2, Material.WOOD, 5);
        setBlock(x + 2, y, z - 2, Material.WOOD, 5);
        setBlock(x + 2, y + 1, z - 2, Material.DARK_OAK_FENCE, 0);
        setBlock(x + 2, y + 2, z - 2, Material.AIR, 0);
        setBlock(x + 2, y + 3, z - 2, Material.AIR, 0);
        setBlock(x + 2, y + 4, z - 2, Material.AIR, 0);
        setBlock(x + 2, y - 3, z - 1, Material.WOOD, 5);
        setBlock(x + 2, y - 2, z - 1, Material.WOOD, 5);
        setBlock(x + 2, y - 1, z - 1, Material.WOOD, 5);
        setBlock(x + 2, y, z - 1, Material.WOOD, 1);
        setBlock(x + 2, y + 1, z - 1, Material.AIR, 0);
        setBlock(x + 2, y + 2, z - 1, Material.AIR, 0);
        setBlock(x + 2, y + 3, z - 1, Material.AIR, 0);
        setBlock(x + 2, y + 4, z - 1, Material.AIR, 0);
        setBlock(x + 2, y - 3, z, Material.WOOD, 5);
        setBlock(x + 2, y - 2, z, Material.WOOD, 5);
        setBlock(x + 2, y - 1, z, Material.WOOD, 5);
        setBlock(x + 2, y, z, Material.WOOD, 1);
        setBlock(x + 2, y + 1, z, Material.AIR, 0);
        setBlock(x + 2, y + 2, z, Material.AIR, 0);
        setBlock(x + 2, y + 3, z, Material.AIR, 0);
        setBlock(x + 2, y + 4, z, Material.AIR, 0);
        setBlock(x + 2, y - 3, z + 1, Material.WOOD, 5);
        setBlock(x + 2, y - 2, z + 1, Material.WOOD, 5);
        setBlock(x + 2, y - 1, z + 1, Material.WOOD, 5);
        setBlock(x + 2, y, z + 1, Material.WOOD, 1);
        setBlock(x + 2, y + 1, z + 1, Material.AIR, 0);
        setBlock(x + 2, y + 2, z + 1, Material.AIR, 0);
        setBlock(x + 2, y + 3, z + 1, Material.AIR, 0);
        setBlock(x + 2, y + 4, z + 1, Material.AIR, 0);
        setBlock(x + 2, y - 2, z + 2, Material.WOOD, 5);
        setBlock(x + 2, y - 1, z + 2, Material.WOOD, 5);
        setBlock(x + 2, y, z + 2, Material.WOOD, 5);
        setBlock(x + 2, y + 1, z + 2, Material.DARK_OAK_FENCE, 0);
        setBlock(x + 2, y + 2, z + 2, Material.AIR, 0);
        setBlock(x + 2, y + 3, z + 2, Material.AIR, 0);
        setBlock(x + 2, y + 4, z + 2, Material.AIR, 0);
        setBlock(x + 2, y - 1, z + 3, Material.WOOD, 5);
        setBlock(x + 2, y, z + 3, Material.WOOD, 5);
        setBlock(x + 2, y + 1, z + 3, Material.DARK_OAK_FENCE, 0);
        setBlock(x + 2, y + 2, z + 3, Material.AIR, 0);
        setBlock(x + 2, y + 3, z + 3, Material.AIR, 0);
        setBlock(x + 2, y + 4, z + 3, Material.AIR, 0);
        setBlock(x + 3, y - 1, z - 2, Material.WOOD, 5);
        setBlock(x + 3, y, z - 2, Material.WOOD, 5);
        setBlock(x + 3, y + 1, z - 2, Material.DARK_OAK_FENCE, 0);
        setBlock(x + 3, y + 2, z - 2, Material.AIR, 0);
        setBlock(x + 3, y + 3, z - 2, Material.AIR, 0);
        setBlock(x + 3, y + 4, z - 2, Material.AIR, 0);
        setBlock(x + 3, y - 2, z - 1, Material.DARK_OAK_STAIRS, 5);
        setBlock(x + 3, y - 1, z - 1, Material.WOOD, 5);
        setBlock(x + 3, y, z - 1, Material.WOOD, 5);
        setBlock(x + 3, y + 1, z - 1, Material.DARK_OAK_FENCE, 0);
        setBlock(x + 3, y + 2, z - 1, Material.AIR, 0);
        setBlock(x + 3, y + 3, z - 1, Material.AIR, 0);
        setBlock(x + 3, y + 4, z - 1, Material.AIR, 0);
        setBlock(x + 3, y - 2, z, Material.DARK_OAK_STAIRS, 5);
        setBlock(x + 3, y - 1, z, Material.WOOD, 5);
        setBlock(x + 3, y, z, Material.WOOD, 1);
        setBlock(x + 3, y + 1, z, Material.AIR, 0);
        setBlock(x + 3, y + 2, z, Material.AIR, 0);
        setBlock(x + 3, y + 3, z, Material.AIR, 0);
        setBlock(x + 3, y + 4, z, Material.AIR, 0);
        setBlock(x + 3, y - 2, z + 1, Material.DARK_OAK_STAIRS, 5);
        setBlock(x + 3, y - 1, z + 1, Material.WOOD, 5);
        setBlock(x + 3, y, z + 1, Material.WOOD, 5);
        setBlock(x + 3, y + 1, z + 1, Material.DARK_OAK_FENCE, 0);
        setBlock(x + 3, y + 2, z + 1, Material.AIR, 0);
        setBlock(x + 3, y + 3, z + 1, Material.AIR, 0);
        setBlock(x + 3, y + 4, z + 1, Material.AIR, 0);
        setBlock(x + 3, y - 1, z + 2, Material.WOOD, 5);
        setBlock(x + 3, y, z + 2, Material.WOOD, 5);
        setBlock(x + 3, y + 1, z + 2, Material.DARK_OAK_FENCE, 0);
        setBlock(x + 3, y + 2, z + 2, Material.AIR, 0);
        setBlock(x + 3, y + 3, z + 2, Material.AIR, 0);
        setBlock(x + 3, y + 4, z + 2, Material.AIR, 0);

        for (Block block : new Block[] { setBlock(x - 1, y + 3, z, Material.WALL_SIGN, 4), setBlock(x, y + 3, z - 1, Material.WALL_SIGN, 2), setBlock(x, y + 3, z + 1, Material.WALL_SIGN, 3), setBlock(x + 1, y + 3, z, Material.WALL_SIGN, 5) }) {
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

        ConsoleUtils.success("Successfully created Region.");
    }

    private Block setBlock(int x, int y, int z, Material material, int data) {
        Block block = world.getBlockAt(x, y, z);
        block.setType(material);
        block.setData((byte) data);

        return block;
    }
}
