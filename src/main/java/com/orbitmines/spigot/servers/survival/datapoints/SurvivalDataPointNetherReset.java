package com.orbitmines.spigot.servers.survival.datapoints;

import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.datapoints.DataPointSign;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class SurvivalDataPointNetherReset extends DataPointSign {

    private List<Location> locations;

    public SurvivalDataPointNetherReset() {
        super("NETHER_RESET", Type.GOLD_PLATE, Material.RED_WOOL);

        locations = new ArrayList<>();
    }

    @Override
    public boolean buildAt(DataPointLoader loader, Location location, String[] data) {
        locations.add(location.add(0.5, 0, 0.5));

        return true;
    }

    @Override
    public boolean setup() {
        return true;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
