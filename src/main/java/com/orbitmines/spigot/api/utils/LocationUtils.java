package com.orbitmines.spigot.api.utils;

import com.orbitmines.spigot.api.Direction;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class LocationUtils {

    public static boolean equals(Block block1, Block block2) {
        return equals(block1.getLocation(), block2.getLocation());
    }

    public static boolean equals(Location l1, Location l2) {
        return l1.getWorld().getName().equals(l2.getWorld().getName()) && l1.getBlockX() == l2.getBlockX() && l1.getBlockY() == l2.getBlockY() && l1.getBlockZ() == l2.getBlockZ();
    }

    public static BlockFace getFacingDirectionAt(Location location) {
        Direction[] values = Direction.values();

        for (Direction direction : values) {
            if (direction.getAsNewLocation(location).getBlock().getType().isSolid()) {

                /* Safer than valueOf */
                switch (direction) {
                    case NORTH:
                        return BlockFace.SOUTH;
                    case EAST:
                        return BlockFace.WEST;
                    case SOUTH:
                        return BlockFace.NORTH;
                    case WEST:
                        return BlockFace.EAST;
                }
            }
        }
        return BlockFace.DOWN;
    }

    public static Block getFirstEmpty(Block block, BlockFace blockFace) {
        while (block.getType().isSolid())
            block = block.getRelative(blockFace);

        return block;
    }

    public static List<Block> getBlocksBetween(Location l1, Location l2) {
        List<Block> blocks = new ArrayList<>();

        int topBlockX = (l1.getBlockX() < l2.getBlockX() ? l2.getBlockX() : l1.getBlockX());
        int bottomBlockX = (l1.getBlockX() > l2.getBlockX() ? l2.getBlockX() : l1.getBlockX());

        int topBlockY = (l1.getBlockY() < l2.getBlockY() ? l2.getBlockY() : l1.getBlockY());
        int bottomBlockY = (l1.getBlockY() > l2.getBlockY() ? l2.getBlockY() : l1.getBlockY());

        int topBlockZ = (l1.getBlockZ() < l2.getBlockZ() ? l2.getBlockZ() : l1.getBlockZ());
        int bottomBlockZ = (l1.getBlockZ() > l2.getBlockZ() ? l2.getBlockZ() : l1.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = l1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    public static void addIdenticalBlocksTouching(Block block, List<Block> blocks) {
        blocks.add(block);

        for (BlockFace blockFace : new BlockFace[] { BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST,
                BlockFace.NORTH, BlockFace.SOUTH}) {
            Block b = block.getRelative(blockFace);

            if (!blocks.contains(b) && b.getType() == block.getType() && b.getData() == block.getData())
                addIdenticalBlocksTouching(b, blocks);
        }
    }
}
