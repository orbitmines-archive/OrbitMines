package com.orbitmines.spigot.servers.uhsurvival.handlers.profile;

import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food.water.Water;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.FoodType;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Robin on 2/28/2018.
 */
public class PlayerProfile {

    private HashMap<FoodType, Integer> foods;

    private int water;

    private UUID id;

    private Date bannedDate;

    public PlayerProfile(UUID id){
        this.id = id;
        this.foods = new HashMap<>();
        this.water = Water.MAXIMUM_WATER;
        this.bannedDate = null;
    }

    public void addWater(int water){
        this.setWater(this.water +  water);
    }

    public int getWater() {
        return water;
    }

    /* WATER METHODS */
    public void setWater(int water){
        this.water = MathUtils.clamp(water, 0, Water.MAXIMUM_WATER);
    }

    public void removeWater(int water){
        this.setWater(this.water -  water);
    }

    public boolean isBanned(){
        Date now = new Date(System.currentTimeMillis());
        return (bannedDate != null) && (now.compareTo(bannedDate) < 0);
    }

    /* BANNED METHODS */
    public void setBanned(boolean banned){
        boolean b = isBanned();
        if(b && !banned){
            this.bannedDate =  null;
        } else if(!b && banned){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 2);
            this.bannedDate = calendar.getTime();
        }
    }

    /* TEMPERATURE METHODS */

    /* FOOD METHODS */
    public void consumeFood(FoodType type, int amount){
        if(foods.containsKey(type)){
            int newAmount = MathUtils.clamp(foods.get(type) + amount, 0, 10);
            foods.put(type, newAmount);
        } else {
            foods.put(type, MathUtils.clamp(amount, 0, 10));
        }
    }

    public int getAmount(FoodType type){
        return foods.get(type);
    }

    public void removeFood(FoodType type, int amount){
        if(foods.containsKey(type)){
            int newAmount = MathUtils.clamp(foods.get(type) - amount, 0, 10);
            foods.put(type, newAmount);
        }
    }
}
