package com.orbitmines.spigot.servers.hub.datapoints;

import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.datapoints.DataPointSign;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class HubDataPointSpawnpoint extends DataPointSign {

    private List<Location> spawns;

    public HubDataPointSpawnpoint() {
        super("SPAWNPOINT", Type.IRON_PLATE, Material.LIME_WOOL);

        spawns = new ArrayList<>();
    }

    @Override
    public boolean buildAt(DataPointLoader loader, Location location, String[] data) {
        float yaw;
        if (data.length >= 1) {
            try {
                yaw = Float.parseFloat(data[0]);
            } catch(NumberFormatException ex) {
                failureMessage = "Invalid Yaw.";
                return false;
            }
        } else {
            failureMessage = "Yaw not given.";
            return false;
        }

        float pitch;
        if (data.length >= 2) {
            try {
                pitch = Float.parseFloat(data[1]);
            } catch(NumberFormatException ex) {
                failureMessage = "Invalid Pitch.";
                return false;
            }
        } else {
            failureMessage = "Pitch not given.";
            return false;
        }

        location.setYaw(yaw);
        location.setPitch(pitch);
        spawns.add(location.add(0.5, 0, 0.5));
        return true;
    }

    @Override
    public boolean setup() {
        if (spawns.size() == 0) {
            failureMessage = "No Spawnpoints located.";
            return false;
        }
        return true;
    }

    public List<Location> getSpawns() {
        return spawns;
    }
}
