package com.orbitmines.spigot.api.handlers;

import com.orbitmines.spigot.api.utils.VectorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.material.MaterialData;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class BlockFalling {

    private FallingBlock fallingBlock;
    private final Location location;
    private boolean drop;

    private final Material material;
    private final byte durability;

    public BlockFalling(Location location, Material material) {
        this(location, material, 0);
    }

    public BlockFalling(Location location, Material material, int durability) {
        this.location = location;
        this.material = material;
        this.durability = (byte) durability;
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

    public byte getDurability() {
        return durability;
    }

    public FallingBlock spawn() {
        fallingBlock = location.getWorld().spawnFallingBlock(location, new MaterialData(material, durability));
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
