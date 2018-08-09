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

    /* Used for testing */
    protected String failureMessage;

    public DataPoint(Type type, Material material) {
        this.type = type;
        this.material = material;
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

    public String getFailureMessage() {
        return failureMessage;
    }

    public boolean equals(BlockState blockState) {
        return blockState.getType() == material;
    }

    public enum Type {

        GOLD_PLATE(Material.LIGHT_WEIGHTED_PRESSURE_PLATE),
        IRON_PLATE(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);

        private final Material material;

        Type(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }

    }
}
