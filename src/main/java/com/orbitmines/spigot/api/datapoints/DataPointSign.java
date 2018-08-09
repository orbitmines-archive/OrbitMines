package com.orbitmines.spigot.api.datapoints;

import com.orbitmines.spigot.api.Direction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public  abstract class DataPointSign extends DataPoint {

    private final String firstLine;

    public DataPointSign(String firstLine, Type type, Material material) {
        super(type, material);

        this.firstLine = firstLine;
    }

    public abstract boolean buildAt(DataPointLoader loader, Location location, String[] data);

    public String getFirstLine() {
        return firstLine;
    }

    @Override
    public boolean buildAt(DataPointLoader loader, Location location) {
        Sign sign = getSignAround(location.getWorld().getBlockAt(location).getState());

        String[] lines = sign.getLines();

        if (!(loader instanceof DataPointTester))
            sign.getBlock().setType(Material.AIR);

        /* All three lines into one string */
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < 4; i++) {
            if (i != 1)
                stringBuilder.append(" ");

            stringBuilder.append(lines[i]);
        }

        String[] rawData = stringBuilder.toString().split(" ");

        List<String> data = new ArrayList<>();
        /* There might be some double spaces, let's clean that up */
        for (String raw : rawData) {
            if (!raw.equals(""))
                data.add(raw);
        }

        return buildAt(loader, location, data.toArray(new String[data.size()]));
    }

    @Override
    public boolean equals(BlockState blockState) {
        if (!super.equals(blockState))
            return false;

        /* Material & Data are the same, now we check the first line */

        Sign sign = getSignAround(blockState);

        return sign != null && sign.getLine(0).equalsIgnoreCase(firstLine);
    }

    private Sign getSignAround(BlockState blockState) {
        for (Direction direction : Direction.values()) {
            BlockState possibleSign = blockState.getWorld().getBlockAt(direction.getAsNewLocation(blockState.getLocation())).getState();

            if (possibleSign instanceof Sign)
                return (Sign) possibleSign;
        }
        return null;
    }
}
