package com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.UHSurvival;
import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food.water.Water;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.FoodType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 3/1/2018.
 */
public class FoodManager {

    public static final int MAX_FOOD_AMOUNT = 10;

    private List<Food> foods;
    private Water water;

    public FoodManager(UHSurvival uhSurvival) {
        this.foods = new ArrayList<>();
        this.water = new Water();
        this.foods.add(water);
        new Foods(uhSurvival);
    }

    public void consume(UHPlayer uhPlayer, Material m, byte b) {
        for (Food food : foods) {
            if (food.getMaterial() == m && food.getData() == b) {
                int addedFoodLevel = food.getFoodLevel(uhPlayer);
                float saturation = food.getSaturation(uhPlayer);
                food.consume(uhPlayer);
                uhPlayer.getProfile().consumeFood(food.getType(), 1);
            }
        }
    }

    public void addFoodType(Food food) {
        this.foods.add(food);
    }

    public Water getWater() {
        return water;
    }

    public static abstract class Food {

        private FoodType type;

        private int max_food_level;
        private float max_saturation;

        private Material m;
        private byte data;

        public Food(FoodType type, Material m, byte data, int max_food_level, float max_saturation) {
            this.type = type;
            this.m = m;
            this.data = data;
            this.max_food_level = max_food_level;
            this.max_saturation = max_saturation;
        }

        public void consume(UHPlayer uhPlayer) {
        }

        public FoodType getType() {
            return type;
        }

        public byte getData() {
            return data;
        }

        public Material getMaterial() {
            return m;
        }

        private int getFoodLevel(UHPlayer player) {
            return MathUtils.clamp(max_food_level - ((max_food_level / MAX_FOOD_AMOUNT) * player.getProfile().getAmount(type)), 0, max_food_level);
        }

        private float getSaturation(UHPlayer player) {
            return max_saturation - ((max_saturation / MAX_FOOD_AMOUNT) * player.getProfile().getAmount(type));
        }
    }
}
