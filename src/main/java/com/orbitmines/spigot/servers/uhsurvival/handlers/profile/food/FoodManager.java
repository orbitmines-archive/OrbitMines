package com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food;

import com.orbitmines.spigot.servers.uhsurvival.handlers.UHPlayer;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.FoodType;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 3/1/2018.
 */
public class FoodManager {

    private List<Food> foods;

    public FoodManager(){
        this.foods = new ArrayList<>();
    }

    public void consume(UHPlayer uhPlayer, Material m, byte b){
        for(Food food : foods){
            if(food.getMaterial() == m && food.getData() == b){
               food.consume(uhPlayer);
               uhPlayer.getProfile().consumeFood(food.getType(), 1);
            }
        }
    }

    public static abstract class Food {

        private FoodType type;

        private Material m;
        private byte data;

        public Food(FoodType type, Material m, byte data){
            this.type = type;
            this.m = m;
            this.data = data;
        }

        public abstract void consume(UHPlayer uhPlayer);

        public FoodType getType() {
            return type;
        }

        public byte getData() {
            return data;
        }

        public Material getMaterial(){
            return m;
        }
    }
}
