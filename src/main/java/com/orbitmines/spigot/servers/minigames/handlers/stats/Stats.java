package com.orbitmines.spigot.servers.minigames.handlers.stats;

import com.orbitmines.spigot.servers.minigames.MiniGameType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Robin on 3/20/2018.
 */
public abstract class Stats {

    private static List<Stats> stats = new ArrayList<>();

    private MiniGameType type;

    private List<String> stat;

    public Stats(MiniGameType type){
        this.type = type;
        stats.add(this);
    }

    /* SETTERS */
    public void add(String key){
        this.stat.add(key);
    }

    /* BOOLEANS */
    public boolean isStat(String key){
        return stat.contains(key);
    }

    /* GETTERS */
    public List<String> getStats() {
        return stat;
    }

    public MiniGameType getType() {
        return type;
    }

    public static Stats getStats(MiniGameType type){
        for(Stats stats : stats){
            if(stats.getType() == type){
                return stats;
            }
        }
        return null;
    }

    /* DATA CLASS */
    public static class Data {

        private Stats stats;

        private HashMap<String, Integer> data;

        private UUID id;

        private int wins;
        private int loses;

        public Data(UUID id, Stats stats) {
            this.stats = stats;
            this.data = new HashMap<>();
            this.id = id;
            this.wins = 0;
            this.loses = 0;
            for (String statName : stats.getStats()) {
                data.put(statName, 0);
            }
        }

        public void addLose() {
            this.loses++;
        }

        public void addWin() {
            this.wins++;
        }

        public int getWins() {
            return wins;
        }

        public int getLoses() {
            return loses;
        }

        public int getCount(String key) {
            if (stats.isStat(key)) {
                return data.get(key);
            }
            return 0;
        }

        public void add(String key) {
            if (stats.isStat(key)) {
                data.put(key, data.get(key) + 1);
            }
        }

        public Stats getStats() {
            return stats;
        }
    }
}
