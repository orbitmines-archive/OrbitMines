package com.orbitmines.spigot.api.datapoints;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class DataPointLoader {

    protected final World world;
    protected final Map<DataPoint.Type, List<DataPoint>> dataPoints;

    public DataPointLoader(World world, Map<DataPoint.Type, List<DataPoint>> dataPoints) {
        this.world = world;
        this.dataPoints = dataPoints;
    }

    public World getWorld() {
        return world;
    }

    public Map<DataPoint.Type, List<DataPoint>> getDataPoints() {
        return dataPoints;
    }

    protected boolean buildAt(DataPoint dataPoint, Location location) {
        return dataPoint.buildAt(this, location);
    }

    public void load() {
        Set<DataPoint.Type> types = dataPoints.keySet();

        for (Chunk chunk : world.getLoadedChunks()) {
            int cX = chunk.getX() << 4;
            int cZ = chunk.getZ() << 4;

            for (int x = cX; x < cX + 16; x++) {
                for (int z = cZ; z < cZ + 16; z++) {
                    for (int y = 0; y < 255; y++) {
                        Material material = world.getBlockAt(x, y, z).getType();

                        if (material == Material.AIR)
                            continue;

                        loop:
                        for (DataPoint.Type type : types) {
                            if (material != type.getMaterial())
                                continue;

                            /* Might be a DataPoint: */
                            BlockState under = world.getBlockAt(world.getBlockAt(x, y, z).getLocation().add(0, -1, 0)).getState();

                            for (DataPoint dataPoint : dataPoints.get(type)) {
                                /* DataPoint found */
                                if (dataPoint.equals(under)) {
                                    Location location = under.getLocation().clone();

                                    if (buildAt(dataPoint, location)) {
                                        /* Clear DataPoint */
                                        world.getBlockAt(x, y, z).setType(Material.AIR);
                                        under.getBlock().setType(Material.AIR);
                                    }

                                    break loop;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
