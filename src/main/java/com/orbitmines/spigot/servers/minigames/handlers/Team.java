package com.orbitmines.spigot.servers.minigames.handlers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 3/23/2018.
 */
public class Team {

    private Settings settings;
    private List<MiniGamePlayer> teammates;

    public Team(Settings settings, MiniGamePlayer... players){
        this.teammates = new ArrayList<>();
        this.settings = settings;
        for(MiniGamePlayer player : players){
            if(!isFull()){
                add(player);
            }
        }
    }

    public boolean isFull(){
        return settings.getMaxPlayers() == teammates.size();
    }

    public void add(MiniGamePlayer player){
        this.teammates.add(player);
    }

    public void remove(MiniGamePlayer player){
        this.teammates.remove(player);
    }

    public static class Settings {

        private int maxPlayers;
        private boolean friendlyFire;

        public Settings(int maxPlayers, boolean friendlyFire){
            this.maxPlayers = maxPlayers;
            this.friendlyFire = friendlyFire;
        }

        public int getMaxPlayers() {
            return maxPlayers;
        }

        public boolean canFriendlyFire(){
            return friendlyFire;
        }
    }
}
