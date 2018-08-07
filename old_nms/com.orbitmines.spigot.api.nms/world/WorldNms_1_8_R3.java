package com.orbitmines.spigot.api.nms.world;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntityChest;
import net.minecraft.server.v1_8_R3.TileEntityEnderChest;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class WorldNms_1_8_R3 implements WorldNms {

    @Override
    public void chestAnimation(Location location, boolean opened) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        TileEntityChest tileChest = (TileEntityChest) world.getTileEntity(position);
        world.playBlockAction(position, tileChest.w(), 1, opened ? 1 : 0);
    }

    @Override
    public void enderchestAnimation(Location location, boolean opened) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(location.getX(), location.getY(), location.getZ());

        TileEntityEnderChest tileChest = (TileEntityEnderChest) world.getTileEntity(position);
        world.playBlockAction(position, tileChest.w(), 1, opened ? 1 : 0);
    }
}
