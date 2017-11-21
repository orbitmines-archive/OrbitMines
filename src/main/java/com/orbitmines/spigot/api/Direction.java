package com.orbitmines.spigot.api;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public enum Direction {

    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    private final int addX;
    private final int addZ;

    Direction(int addX, int addZ) {
        this.addX = addX;
        this.addZ = addZ;
    }

    public int getAddX() {
        return addX;
    }

    public int getAddZ() {
        return addZ;
    }

    public Location getAsNewLocation(Location location) {
        Location l = location.clone();
        l.add(addX, 0, addZ);
        return l;
    }

    public Location getAsNewLocation(Location location, double multiplier) {
        Location l = location.clone();
        l.add(addX * multiplier, 0, addZ * multiplier);
        return l;
    }

    public BlockFace getBlockFace() {
        switch (this) {
            case EAST:
                return BlockFace.EAST;
            case WEST:
                return BlockFace.WEST;
            case NORTH:
                return BlockFace.NORTH;
            case SOUTH:
                return BlockFace.SOUTH;
        }
        return BlockFace.DOWN;
    }

    public static Direction getDirection(Location l1, Location l2) {
        Location l = l2.subtract(l1);

        int x = l.getBlockX();
        int z = l.getBlockZ();

        if (x == 0 && z == 0)
            return null;

        if (x == 0) {
            if (z > 0)
                return SOUTH;
            else
                return NORTH;
        } else {
            if (x > 0)
                return EAST;
            else
                return WEST;
        }
    }
}
