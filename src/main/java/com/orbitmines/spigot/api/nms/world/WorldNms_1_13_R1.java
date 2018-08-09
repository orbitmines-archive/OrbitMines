package com.orbitmines.spigot.api.nms.world;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */

import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.Collection;

public class WorldNms_1_13_R1 implements WorldNms {

    @Override
    public void chestAnimation(Location location, boolean opened) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        TileEntityChest tileChest = (TileEntityChest) world.getTileEntity(position);
        world.playBlockAction(position, tileChest.getBlock().getBlock(), 1, opened ? 1 : 0);
    }

    @Override
    public void enderchestAnimation(Location location, boolean opened) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        TileEntityEnderChest tileChest = (TileEntityEnderChest) world.getTileEntity(position);
        world.playBlockAction(position, tileChest.getBlock().getBlock(), 1, opened ? 1 : 0);
    }

    @Override
    public void conduitAnimation(Location location, Collection<? extends Player> players) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        TileEntityConduit tileConduit = (TileEntityConduit) world.getTileEntity(position);
        tileConduit.Y_();
    }
}
