package com.orbitmines.spigot.servers.survival.handlers.region;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class RegionLocator {

    private final List<Material> yFinder = Arrays.asList(Material.LOG, Material.LOG_2);

    private final int id;

    private final World world;
    private int x;
    private int z;

    private int inventoryX;
    private int inventotyY;

    private boolean generated;

    public RegionLocator(World world, int id) {
        this.id = id;
        this.world = world;
        x = Region.START_X;
        z = Region.START_Z;
        inventoryX = 0;
        inventotyY = 0;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getInventoryX() {
        return inventoryX;
    }

    public int getInventotyY() {
        return inventotyY;
    }

    public int locateY() {
        for (int y = world.getMaxHeight(); y > 0; y--) {
            Block b = world.getBlockAt(x, y, z);

            if (b.getType() == Material.BARRIER)
                generated = true;

            if (b.isEmpty() || yFinder.contains(b.getType()) || !b.getType().isSolid() && !b.isLiquid())
                continue;

            return b.getY();
        }

        throw new IllegalArgumentException("Error while locating Region, Void World?");
    }

    public boolean isGenerated() {
        return generated;
    }

    public void locate() {
        for (int i = 0; i < id; i++) {
            if (x < z) {
                if (-1 * x < z) {
                    x += Region.OFFSET;
                    inventoryX++;
                    continue;
                }
                z += Region.OFFSET;
                inventotyY++;
                continue;
            }
            if (x > z) {
                if (-1 * x >= z) {
                    x -= Region.OFFSET;
                    inventoryX--;
                    continue;
                }
                z -= Region.OFFSET;
                inventotyY--;
                continue;
            }
            if (x <= 0) {
                z += Region.OFFSET;
                inventotyY++;
                continue;
            }
            z -= Region.OFFSET;
            inventotyY--;
        }
    }
}
