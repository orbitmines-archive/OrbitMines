package com.orbitmines.spigot.servers.minigames.handlers.stats;

import com.orbitmines.spigot.servers.minigames.handlers.MiniGameType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Robin on 3/27/2018.
 */
public class Stats {

    private static List<Stats> stat = new ArrayList<>();

    private MiniGameType type;

    private List<String> keys;

    public Stats(MiniGameType type){
        this.type = type;
        this.keys = new ArrayList<>();
        stat.add(this);
    }

    public MiniGameType getType() {
        return type;
    }

    public boolean isKey(String key){
        return keys.contains(key);
    }

    public List<String> getKeys(){
        return keys;
    }

    public static Stats getStats(MiniGameType type){
        for(Stats stats : stat){
            if(stats.getType() == type){
                return stats;
            }
        }
        return null;
    }

    public static class Data {

        private Stats stats;

        private HashMap<String, Integer> data;

        private UUID id;

        private int wins;
        private int loses;

        public Data(MiniGameType type, UUID id){
            this.stats = Stats.getStats(type);
            this.id = id;
            this.loses = 0;
            this.wins = 0;
            this.data = new HashMap<>();
        }

        public int getWins() {
            return wins;
        }

        public int getLoses() {
            return loses;
        }

        public void addWin(){
            this.wins++;
        }

        public void addLose(){
            this.loses++;
        }

        public void add(String key){
            if(stats.isKey(key)){
                data.put(key, data.get(key) + 1);
            } else {
                throw new IllegalStateException("Key is not found!");
            }
        }

        public int get(String key){
            if(stats.isKey(key)) {
                return data.get(key);
            } else {
                throw new IllegalStateException("Key is not found!");
            }
        }

        public Stats getStats() {
            return stats;
        }
    }
}
