package com.orbitmines.spigot.api.handlers;

import com.orbitmines.spigot.api.utils.VectorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class BlockFalling {

    private FallingBlock fallingBlock;
    private final Location location;
    private boolean drop;

    private final Material material;

    public BlockFalling(Location location, Material material) {
        this.location = location;
        this.material = material;
    }

    public Location getLocation() {
        return location;
    }

    public boolean hasDrop() {
        return drop;
    }

    public void setDrop(boolean drop) {
        this.drop = drop;
    }

    public Material getMaterial() {
        return material;
    }

    public FallingBlock spawn() {
        fallingBlock = location.getWorld().spawnFallingBlock(location, material.createBlockData());
        fallingBlock.setDropItem(hasDrop());
        return fallingBlock;
    }

    public void randomVelocity() {
        randomVelocity(1.0D);
    }

    public void randomVelocity(double multiply) {
        fallingBlock.setVelocity(VectorUtils.random().multiply(multiply));
    }
}
