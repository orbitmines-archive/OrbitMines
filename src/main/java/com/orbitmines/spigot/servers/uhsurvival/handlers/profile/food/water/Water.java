package com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food.water;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food.FoodManager;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.FoodType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 3/1/2018.
 */
public class Water extends FoodManager.Food {

    public static final int MAXIMUM_WATER = 100;
    private List<WaterOutput> outputs;
    private int MINIMUM_WATER_BOTTLE = 20;
    private int MAXIMUM_WATER_BOTTLE = 35;

    public Water() {
        super(FoodType.WATER, Material.POTION, (byte) 0, 2, 2.0F);
        this.outputs = new ArrayList<>();
    }

    public void registerOutput(WaterOutput output){
        this.outputs.add(output);
    }

    public List<WaterOutput> getOutputs() {
        return outputs;
    }

    @Override
    public void consume(UHPlayer uhPlayer) {
        uhPlayer.getProfile().addWater(MathUtils.randomInteger(MAXIMUM_WATER_BOTTLE -  MINIMUM_WATER_BOTTLE) + MINIMUM_WATER_BOTTLE);
    }

    public static abstract class WaterOutput {

        private int minimumWater;
        private int maximumWater;

        public WaterOutput(int minimumWater, int maximumWater){
            this.minimumWater = minimumWater;
            this.maximumWater = maximumWater;
        }

        public boolean isBetween(int water){
            return minimumWater <= water && water <= maximumWater;
        }

        public abstract void output(UHPlayer player);
    }
}
