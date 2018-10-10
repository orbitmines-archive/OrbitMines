package com.orbitmines.spigot.servers.kitpvp.datapoints;

import com.orbitmines.spigot.api.datapoints.DataPoint;
import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class KitPvPDataPointMapBarrier extends DataPoint {

    private List<Block> barriers;

    public KitPvPDataPointMapBarrier() {
        super(Type.GOLD_PLATE, Material.WHITE_WOOL);

        barriers = new ArrayList<>();
    }

    @Override
    public boolean buildAt(DataPointLoader loader, Location location) {
        barriers.add(location.getBlock());
        return true;
    }

    @Override
    public boolean setup() {
        return true;
    }

    public List<Block> getBarriers() {
        return barriers;
    }
}
