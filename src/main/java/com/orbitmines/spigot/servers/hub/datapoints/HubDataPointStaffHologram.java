package com.orbitmines.spigot.servers.hub.datapoints;

import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.spigot.api.datapoints.DataPointLoader;
import com.orbitmines.spigot.api.datapoints.DataPointSign;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public class HubDataPointStaffHologram extends DataPointSign {

    private Map<Location, UUID> staffHolograms;

    public HubDataPointStaffHologram() {
        super("STAFF_HOLO", Type.IRON_PLATE, Material.BLACK_WOOL);

        staffHolograms = new HashMap<>();
    }

    @Override
    public boolean buildAt(DataPointLoader loader, Location location, String[] data) {
        StringBuilder trimmedUuid = new StringBuilder();
        for (String s : data) {
            trimmedUuid.append(s.replaceAll(" ", ""));
        }

        UUID uuid;
        try {
            uuid = UUIDUtils.parse(trimmedUuid.toString());
        } catch (IndexOutOfBoundsException ex) {
            failureMessage = "Invalid UUID.";
            return false;
        }
        staffHolograms.put(location.add(0.5, 0, 0.5), uuid);

        return true;
    }

    @Override
    public boolean setup() {
        return true;
    }

    public Map<Location, UUID> getStaffHolograms() {
        return staffHolograms;
    }
}
