package com.orbitmines.spigot.api.datapoints;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

/*
 * OrbitMines - @author Fadi Shawki - 2018
 */
public abstract class DataPoint {

    protected final Type type;

    protected final Material material;
    protected final byte data;

    /* Used for testing */
    protected String failureMessage;

    public DataPoint(Type type, Material material) {
        this(type, material, 0);
    }

    public DataPoint(Type type, Material material, int data) {
        this.type = type;
        this.material = material;
        this.data = (byte) data;
    }

    /* Returns whether or not the datapoint successfully loaded */
    public abstract boolean buildAt(DataPointLoader loader, Location location);

    /* Returns whether or not the final setup is completed */
    public abstract boolean setup();

    public Type getType() {
        return type;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public boolean equals(BlockState blockState) {
        return blockState.getType() == material && blockState.getData().getData() == data;
    }

    public enum Type {

        GOLD_PLATE(Material.GOLD_PLATE),
        IRON_PLATE(Material.IRON_PLATE);

        private final Material material;

        Type(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }

    }
}
