package com.orbitmines.spigot.servers.uhsurvival.handlers.profile;

import com.orbitmines.api.database.Database;
import com.orbitmines.api.database.Set;
import com.orbitmines.api.database.Table;
import com.orbitmines.api.database.Where;
import com.orbitmines.api.database.tables.uhsurvival.TableUHPlayers;
import com.orbitmines.spigot.api.utils.MathUtils;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food.FoodManager;
import com.orbitmines.spigot.servers.uhsurvival.handlers.profile.food.water.Water;
import com.orbitmines.spigot.servers.uhsurvival.utils.enums.FoodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Robin on 2/28/2018.
 */
public class PlayerProfile {

    private HashMap<FoodType, Integer> foods;

    private FoodManager.Food lastFood;

    private int water;

    private UUID id;

    private Date bannedDate;

    public PlayerProfile(UUID id){
        this.id = id;
        this.foods = new HashMap<>();
        this.water = Water.MAXIMUM_WATER;
        this.bannedDate = null;
        if(!Database.get().contains(Table.UHS_PLAYERS, TableUHPlayers.UUID, new Where(TableUHPlayers.UUID, id.toString()))){
            Database.get().insert(Table.UHS_PLAYERS, Table.UHS_PLAYERS.values(id.toString(), water, foodToString(), bannedDate));
        } else {
            this.water = Database.get().getInt(Table.UHS_PLAYERS, TableUHPlayers.WATER, new Where(TableUHPlayers.UUID, id.toString()));
            String date = Database.get().getString(Table.UHS_PLAYERS, TableUHPlayers.BANNED_DATE, new Where(TableUHPlayers.UUID, id.toString()));
            String foods = Database.get().getString(Table.UHS_PLAYERS, TableUHPlayers.FOOD, new Where(TableUHPlayers.UUID, id.toString()));
            String[] s = foods.split("\\|");
            for(String str : s){
                String[] s1 = str.split(":");
                FoodType type = FoodType.valueOf(s1[0]);
                int amount = MathUtils.getInteger(s1[1]);
                this.foods.put(type, amount);
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss");
            try {
                this.bannedDate = sf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /* WATER METHODS */
    public void removeWater(int water){
        this.setWater(this.water -  water);
    }

    public void addWater(int water){
        this.setWater(this.water +  water);
    }

    public int getWater() {
        return water;
    }


    public void setWater(int water){
        this.water = MathUtils.clamp(water, 0, Water.MAXIMUM_WATER);
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

    public boolean isBanned(){
        Date now = new Date(System.currentTimeMillis());
        return (bannedDate != null) && (now.compareTo(bannedDate) < 0);
    }

    /* TEMPERATURE METHODS */

    /* FOOD METHODS */
    public void consumeFood(FoodType type, int amount){
        if(foods.containsKey(type)){
            int newAmount = MathUtils.clamp(foods.get(type) + amount, 0, FoodManager.MAX_FOOD_AMOUNT);
            foods.put(type, newAmount);
        } else {
            foods.put(type, MathUtils.clamp(amount, 0, FoodManager.MAX_FOOD_AMOUNT));
        }
    }

    public int getAmount(FoodType type){
        return foods.get(type);
    }

    public void removeFood(FoodType type, int amount){
        if(foods.containsKey(type)){
            int newAmount = MathUtils.clamp(foods.get(type) - amount, 0, FoodManager.MAX_FOOD_AMOUNT);
            foods.put(type, newAmount);
        }
    }

    public void setLastFood(FoodManager.Food food){
        this.lastFood = food;
    }

    public FoodManager.Food getLastEatenFood(){
        return lastFood;
    }

    public boolean hasLastEatenFood(){
        return lastFood != null;
    }

    /* DATABASE METHODS */
    public void update(SaveType type) {
        switch (type) {
            case WATER:
                Database.get().update(Table.UHS_PLAYERS, new Set(TableUHPlayers.WATER, water), new Where(TableUHPlayers.UUID, id.toString()));
                break;
            case FOOD:
                Database.get().update(Table.UHS_PLAYERS, new Set(TableUHPlayers.FOOD, foodToString()), new Where(TableUHPlayers.UUID, id.toString()));
                break;
            case BANNED_DATE:
                Database.get().update(Table.UHS_PLAYERS, new Set(TableUHPlayers.BANNED_DATE, dateToString()), new Where(TableUHPlayers.UUID, id.toString()));
                break;
            case ALL:
                Database.get().update(Table.UHS_PLAYERS, new Set[]{
                                new Set(TableUHPlayers.WATER, water),
                                new Set(TableUHPlayers.FOOD, foodToString()),
                                new Set(TableUHPlayers.BANNED_DATE, dateToString())},
                                new Where(TableUHPlayers.UUID, id.toString()));
                break;
        }
    }

    private String foodToString(){
        StringBuilder s = new StringBuilder();
        for(FoodType type : foods.keySet()){
            int amount = foods.get(type);
            s.append(type.name() + ":" + amount + "|");
        }
        return s.substring(0, s.length() - 1);
    }

    private String dateToString(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss");
        if(bannedDate != null) {
            return sf.format(bannedDate);
        } else {
            return "null";
        }
    }

    public enum SaveType {

        WATER,
        FOOD,
        BANNED_DATE,
        ALL;

    }
}
